// File: java/com/example/quranapp/worker/AyatFetchWorker.java
package com.example.quranapp.worker;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.example.quranapp.model.AppDatabase;
import com.example.quranapp.model.AyatEntity;
import com.example.quranapp.model.DatabaseClient;
import com.example.quranapp.network.ApiService;
import com.example.quranapp.network.RetrofitClient;
import com.example.quranapp.model.Ayat;
import com.example.quranapp.model.AyatResponse;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Response;

public class AyatFetchWorker extends Worker {
    private static final String TAG = "AyatFetchWorker";
    public static boolean isRunning = false;

    public AyatFetchWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        isRunning = true;
        int surahNumber = getInputData().getInt("surah_number", 1);
        Log.d(TAG, "Starting work for surah: " + surahNumber);

        try {
            ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
            List<AyatEntity> ayatEntities = new ArrayList<>();

            // Fetch Arabic text
            Call<AyatResponse> arabicCall = apiService.getAyats(surahNumber);
            Response<AyatResponse> arabicResponse = arabicCall.execute();
            if (arabicResponse.isSuccessful() && arabicResponse.body() != null) {
                List<Ayat> ayats = arabicResponse.body().getData().getAyats();

                // Fetch Latin
                Call<AyatResponse> latinCall = apiService.getLatin(surahNumber);
                Response<AyatResponse> latinResponse = latinCall.execute();
                List<Ayat> latinAyats = latinResponse.isSuccessful() && latinResponse.body() != null ?
                        latinResponse.body().getData().getAyats() : null;

                // Fetch Translation
                Call<AyatResponse> transCall = apiService.getTranslation(surahNumber);
                Response<AyatResponse> transResponse = transCall.execute();
                List<Ayat> transAyats = transResponse.isSuccessful() && transResponse.body() != null ?
                        transResponse.body().getData().getAyats() : null;

                // Fetch Audio
                Call<AyatResponse> audioCall = apiService.getAudioAyats(surahNumber);
                Response<AyatResponse> audioResponse = audioCall.execute();
                List<Ayat> audioAyats = audioResponse.isSuccessful() && audioResponse.body() != null ?
                        audioResponse.body().getData().getAyats() : null;

                // Combine data
                for (int i = 0; i < ayats.size(); i++) {
                    Ayat ayat = ayats.get(i);
                    String latin = latinAyats != null && i < latinAyats.size() ? latinAyats.get(i).getText() : "";
                    String translation = transAyats != null && i < transAyats.size() ? transAyats.get(i).getText() : "";
                    String audio = audioAyats != null && i < audioAyats.size() ? audioAyats.get(i).getAudio() : "";
                    ayatEntities.add(new AyatEntity(
                            surahNumber,
                            ayat.getNumber(),
                            ayat.getText(),
                            latin,
                            translation,
                            audio
                    ));
                }

                // Save to database
                AppDatabase db = DatabaseClient.getInstance(getApplicationContext());
                db.ayatDao().insertAll(ayatEntities);
                Log.d(TAG, "Saved " + ayatEntities.size() + " ayats for surah " + surahNumber);
                isRunning = false;
                return Result.success();
            } else {
                Log.e(TAG, "Failed to fetch Arabic text for surah " + surahNumber + ": " + arabicResponse.code());
                isRunning = false;
                return Result.retry();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching ayats for surah " + surahNumber + ": " + e.getMessage());
            isRunning = false;
            return Result.retry();
        }
    }
}