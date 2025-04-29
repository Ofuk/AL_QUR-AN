
package com.example.quranapp.network;

import com.example.quranapp.model.AyatResponse;
import com.example.quranapp.model.SurahResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("v1/surah")
    Call<SurahResponse> getSurahs();

    @GET("v1/surah/{surah}")
    Call<AyatResponse> getAyats(@Path("surah") int surahNumber);

    @GET("v1/surah/{surah}/id.indonesian")
    Call<AyatResponse> getTranslation(@Path("surah") int surahNumber);

    @GET("v1/surah/{surah}/en.transliteration")
    Call<AyatResponse> getLatin(@Path("surah") int surahNumber);

    @GET("v1/surah/{surah}/ar.alafasy")
    Call<AyatResponse> getAudioAyats(@Path("surah") int surahNumber);
}