// File: java/com/example/quranapp/model/AyatEntity.java
package com.example.quranapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ayat")
public class AyatEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int surahNumber;
    private int ayatNumber;
    private String text;
    private String latin;
    private String translation;
    private String audio;

    // Constructor
    public AyatEntity(int surahNumber, int ayatNumber, String text, String latin, String translation, String audio) {
        this.surahNumber = surahNumber;
        this.ayatNumber = ayatNumber;
        this.text = text;
        this.latin = latin;
        this.translation = translation;
        this.audio = audio;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSurahNumber() {
        return surahNumber;
    }

    public void setSurahNumber(int surahNumber) {
        this.surahNumber = surahNumber;
    }

    public int getAyatNumber() {
        return ayatNumber;
    }

    public void setAyatNumber(int ayatNumber) {
        this.ayatNumber = ayatNumber;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }
}