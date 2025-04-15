package com.example.quranapp.model;

public class Juz {
    private int number;
    private String info;

    public Juz(int number, String info) {
        this.number = number;
        this.info = info;
    }

    public int getNumber() {
        return number;
    }

    public String getInfo() {
        return info;
    }
}