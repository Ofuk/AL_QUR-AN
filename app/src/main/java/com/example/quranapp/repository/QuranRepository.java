// File: java/com/example/quranapp/repository/QuranRepository.java
package com.example.quranapp.repository;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import com.example.quranapp.model.AppDatabase;
import com.example.quranapp.model.Ayat;
import com.example.quranapp.model.AyatEntity;
import com.example.quranapp.model.DatabaseClient;
import com.example.quranapp.model.Surah;
import com.example.quranapp.model.SurahResponse;
import com.example.quranapp.network.ApiService;
import com.example.quranapp.network.RetrofitClient;
import com.example.quranapp.worker.AyatFetchWorker;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuranRepository {
    private ApiService apiService;
    private AppDatabase database;
    private MutableLiveData<List<Surah>> surahLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Ayat>> ayatLiveData = new MutableLiveData<>();
    private Context context;

    public QuranRepository(Context context) {
        this.context = context;
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        database = DatabaseClient.getInstance(context);
    }

    public LiveData<List<Surah>> getSurahs() {
        apiService.getSurahs().enqueue(new Callback<SurahResponse>() {
            @Override
            public void onResponse(Call<SurahResponse> call, Response<SurahResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("QuranRepository", "Surahs fetched: " + response.body().getSurahs().size());
                    surahLiveData.setValue(response.body().getSurahs());
                } else {
                    Log.e("QuranRepository", "Failed to fetch surahs: " + response.code());
                    surahLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<SurahResponse> call, Throwable t) {
                Log.e("QuranRepository", "API error: " + t.getMessage());
                surahLiveData.setValue(null);
            }
        });
        return surahLiveData;
    }

    public LiveData<List<Ayat>> getAyats(int surahNumber) {
        // Check database first
        LiveData<List<AyatEntity>> localAyats = database.ayatDao().getAyatsBySurah(surahNumber);
        localAyats.observeForever(ayatEntities -> {
            if (ayatEntities != null && !ayatEntities.isEmpty()) {
                Log.d("QuranRepository", "Returning " + ayatEntities.size() + " ayats from database for surah " + surahNumber);
                List<Ayat> ayats = new ArrayList<>();
                for (AyatEntity entity : ayatEntities) {
                    Ayat ayat = new Ayat();
                    ayat.setNumber(entity.getAyatNumber());
                    ayat.setText(entity.getText());
                    ayat.setLatin(entity.getLatin());
                    ayat.setTranslation(entity.getTranslation());
                    ayat.setAudio(entity.getAudio());
                    ayats.add(ayat);
                }
                ayatLiveData.setValue(ayats);
            } else {
                Log.d("QuranRepository", "No local data for surah " + surahNumber + ", scheduling Worker");
                // Schedule Worker to fetch data
                scheduleAyatFetch(surahNumber);
            }
        });
        return ayatLiveData;
    }

    private void scheduleAyatFetch(int surahNumber) {
        Data inputData = new Data.Builder()
                .putInt("surah_number", surahNumber)
                .build();

        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(AyatFetchWorker.class)
                .setInputData(inputData)
                .build();

        WorkManager.getInstance(context).enqueue(workRequest);

        // Observe database changes after Worker completes
        LiveData<List<AyatEntity>> localAyats = database.ayatDao().getAyatsBySurah(surahNumber);
        localAyats.observeForever(ayatEntities -> {
            if (ayatEntities != null && !ayatEntities.isEmpty()) {
                List<Ayat> ayats = new ArrayList<>();
                for (AyatEntity entity : ayatEntities) {
                    Ayat ayat = new Ayat();
                    ayat.setNumber(entity.getAyatNumber());
                    ayat.setText(entity.getText());
                    ayat.setLatin(entity.getLatin());
                    ayat.setTranslation(entity.getTranslation());
                    ayat.setAudio(entity.getAudio());
                    ayats.add(ayat);
                }
                ayatLiveData.setValue(ayats);
            }
        });
    }
}