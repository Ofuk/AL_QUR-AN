package com.example.quranapp.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AyatResponse {
    @SerializedName("data")
    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data {
        @SerializedName("ayahs")
        private List<Ayat> ayats;

        public List<Ayat> getAyats() {
            return ayats;
        }
    }
}