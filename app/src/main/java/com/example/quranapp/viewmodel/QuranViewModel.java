// File: java/com/example/quranapp/viewmodel/QuranViewModel.java
package com.example.quranapp.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.quranapp.model.Ayat;
import com.example.quranapp.model.Surah;
import com.example.quranapp.repository.QuranRepository;
import java.util.List;

public class QuranViewModel extends AndroidViewModel {
    private QuranRepository repository;

    public QuranViewModel(Application application) {
        super(application);
        repository = new QuranRepository();
    }

    public LiveData<List<Surah>> getSurahs() {
        return repository.getSurahs();
    }

    public LiveData<List<Ayat>> getAyats(int surahNumber) {
        return repository.getAyats(surahNumber);
    }
}