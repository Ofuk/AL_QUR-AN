// File: java/com/example/quranapp/model/AppDatabase.java
package com.example.quranapp.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {AyatEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AyatDao ayatDao();
}