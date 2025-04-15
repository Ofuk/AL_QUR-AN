// File: java/com/example/quranapp/repository/QuranRepository.java
package com.example.quranapp.repository;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.quranapp.model.Ayat;
import com.example.quranapp.model.AyatResponse;
import com.example.quranapp.model.Surah;
import com.example.quranapp.model.SurahResponse;
import com.example.quranapp.network.ApiService;
import com.example.quranapp.network.RetrofitClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuranRepository {
    private ApiService apiService;
    private MutableLiveData<List<Surah>> surahLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Ayat>> ayatLiveData = new MutableLiveData<>();

    public QuranRepository() {
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
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
        apiService.getAyats(surahNumber).enqueue(new Callback<AyatResponse>() {
            @Override
            public void onResponse(Call<AyatResponse> call, Response<AyatResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Ayat> ayats = response.body().getData().getAyats();
                    // Fetch Latin
                    apiService.getLatin(surahNumber).enqueue(new Callback<AyatResponse>() {
                        @Override
                        public void onResponse(Call<AyatResponse> call, Response<AyatResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                List<Ayat> latinAyats = response.body().getData().getAyats();
                                for (int i = 0; i < ayats.size(); i++) {
                                    ayats.get(i).setLatin(latinAyats.get(i).getText());
                                }
                                // Fetch Translation
                                apiService.getTranslation(surahNumber).enqueue(new Callback<AyatResponse>() {
                                    @Override
                                    public void onResponse(Call<AyatResponse> call, Response<AyatResponse> response) {
                                        if (response.isSuccessful() && response.body() != null) {
                                            List<Ayat> transAyats = response.body().getData().getAyats();
                                            for (int i = 0; i < ayats.size(); i++) {
                                                ayats.get(i).setTranslation(transAyats.get(i).getText());
                                            }
                                            // Fetch Audio
                                            apiService.getAudioAyats(surahNumber).enqueue(new Callback<AyatResponse>() {
                                                @Override
                                                public void onResponse(Call<AyatResponse> call, Response<AyatResponse> response) {
                                                    if (response.isSuccessful() && response.body() != null) {
                                                        List<Ayat> audioAyats = response.body().getData().getAyats();
                                                        for (int i = 0; i < ayats.size(); i++) {
                                                            ayats.get(i).setAudio(audioAyats.get(i).getAudio());
                                                        }
                                                        Log.d("QuranRepository", "Audio URLs fetched for surah " + surahNumber + ": " + (ayats.isEmpty() ? "empty" : ayats.get(0).getAudio()));
                                                        ayatLiveData.setValue(ayats);
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<AyatResponse> call, Throwable t) {
                                                    Log.e("QuranRepository", "Audio fetch failed for surah " + surahNumber + ": " + t.getMessage());
                                                    ayatLiveData.setValue(ayats); // Tetap lanjutkan meskipun audio gagal
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<AyatResponse> call, Throwable t) {
                                        Log.e("QuranRepository", "Translation fetch failed: " + t.getMessage());
                                        ayatLiveData.setValue(null);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<AyatResponse> call, Throwable t) {
                            Log.e("QuranRepository", "Latin fetch failed: " + t.getMessage());
                            ayatLiveData.setValue(null);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<AyatResponse> call, Throwable t) {
                Log.e("QuranRepository", "Ayats fetch failed: " + t.getMessage());
                ayatLiveData.setValue(null);
            }
        });
        return ayatLiveData;
    }
}