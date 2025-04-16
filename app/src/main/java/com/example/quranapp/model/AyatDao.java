// File: java/com/example/quranapp/model/AyatDao.java
package com.example.quranapp.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface AyatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<AyatEntity> ayats);

    @Query("SELECT * FROM ayat WHERE surahNumber = :surahNumber")
    LiveData<List<AyatEntity>> getAyatsBySurah(int surahNumber);

    @Query("SELECT * FROM ayat WHERE surahNumber = :surahNumber")
    List<AyatEntity> getAyatsBySurahSync(int surahNumber);
}