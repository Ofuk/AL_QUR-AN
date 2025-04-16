// File: java/com/example/quranapp/model/DatabaseClient.java
package com.example.quranapp.model;

import android.content.Context;
import androidx.room.Room;

public class DatabaseClient {
    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "quran_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}