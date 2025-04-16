package com.example.quranapp.model;

import com.google.gson.annotations.SerializedName;

public class Ayat {
    @SerializedName("numberInSurah")
    private int number;

    @SerializedName("text")
    private String text;

    @SerializedName("audio")
    private String audio;

    private String latin;
    private String translation;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getLatin() {
        return latin;
    }

    public void setLatin(String latin) {
        this.latin = latin;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }
}