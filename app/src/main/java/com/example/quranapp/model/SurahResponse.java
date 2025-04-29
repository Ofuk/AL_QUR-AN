package com.example.quranapp.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SurahResponse {
    @SerializedName("data")
    private List<Surah> surahs;

    public List<Surah> getSurahs() {
        return surahs;
    }
}