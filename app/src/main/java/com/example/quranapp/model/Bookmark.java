package com.example.quranapp.model;

public class Bookmark {
    private int surahNumber;
    private int ayatNumber;
    private String surahName;
    private String arabic;
    private String translation;

    public Bookmark(int surahNumber, int ayatNumber, String surahName, String arabic, String translation) {
        this.surahNumber = surahNumber;
        this.ayatNumber = ayatNumber;
        this.surahName = surahName;
        this.arabic = arabic;
        this.translation = translation;
    }

    public int getSurahNumber() {
        return surahNumber;
    }

    public int getAyatNumber() {
        return ayatNumber;
    }

    public String getSurahName() {
        return surahName;
    }

    public String getArabic() {
        return arabic;
    }

    public String getTranslation() {
        return translation;
    }
}