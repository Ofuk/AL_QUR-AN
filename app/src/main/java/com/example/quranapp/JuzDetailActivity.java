// File: java/com/example/quranapp/JuzDetailActivity.java
package com.example.quranapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quranapp.adapter.AyatAdapter;
import com.example.quranapp.model.Ayat;
import com.example.quranapp.viewmodel.QuranViewModel;
import java.util.ArrayList;
import java.util.List;

public class JuzDetailActivity extends AppCompatActivity {
    private TextView tvJuzTitle;
    private RecyclerView rvAyat;
    private AyatAdapter adapter;
    private LinearLayoutManager layoutManager;
    private List<Ayat> allAyats = new ArrayList<>();
    private QuranViewModel viewModel;
    private static final String PREFS_NAME = "JuzScroll";
    private boolean isDataLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juz_detail);

        tvJuzTitle = findViewById(R.id.tvJuzTitle);
        rvAyat = findViewById(R.id.rvAyat);
        layoutManager = new LinearLayoutManager(this);
        rvAyat.setLayoutManager(layoutManager);

        int juzNumber = getIntent().getIntExtra("juz_number", 1);
        tvJuzTitle.setText("Juz " + juzNumber);

        viewModel = new ViewModelProvider(this).get(QuranViewModel.class);

        // Hanya muat data jika belum dimuat
        if (!isDataLoaded) {
            loadJuzAyats(juzNumber);
        }

        // Pulihkan posisi scroll
        restoreScrollPosition(juzNumber);
    }

    private void loadJuzAyats(int juzNumber) {
        allAyats.clear();
        if (juzNumber == 1) {
            // Surah 1 (Al-Fatiha)
            viewModel.getAyats(1).observe(this, ayats -> {
                if (ayats != null) {
                    allAyats.addAll(ayats);
                    // Surah 2 (Al-Baqarah, ayat 1-141)
                    viewModel.getAyats(2).observe(this, ayats2 -> {
                        if (ayats2 != null) {
                            for (int i = 0; i < 141 && i < ayats2.size(); i++) {
                                allAyats.add(ayats2.get(i));
                            }
                            Log.d("JuzDetailActivity", "Total ayats loaded: " + allAyats.size());
                            // Verifikasi URL audio
                            for (Ayat ayat : allAyats) {
                                if (ayat.getAudio() == null) {
                                    Log.w("JuzDetailActivity", "Audio missing for ayat " + ayat.getNumber());
                                } else {
                                    Log.d("JuzDetailActivity", "Audio available for ayat " + ayat.getNumber() + ": " + ayat.getAudio());
                                }
                            }
                            updateRecyclerView();
                            isDataLoaded = true;
                            restoreScrollPosition(juzNumber);
                        }
                    });
                }
            });
        }
        // Tambahkan logika untuk Juz lain
    }

    private void updateRecyclerView() {
        if (adapter == null) {
            adapter = new AyatAdapter(this, allAyats, "Juz", 0);
            rvAyat.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void restoreScrollPosition(int juzNumber) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int position = prefs.getInt("juz_" + juzNumber, 0);
        if (position > 0) {
            Log.d("JuzDetailActivity", "Restoring scroll position to: " + position);
            layoutManager.scrollToPosition(position);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        int juzNumber = getIntent().getIntExtra("juz_number", 1);
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        int scrollPosition = layoutManager.findFirstVisibleItemPosition();
        editor.putInt("juz_" + juzNumber, scrollPosition);
        editor.apply();
        Log.d("JuzDetailActivity", "Saving scroll position: " + scrollPosition);
    }
    @Override
    protected void onResume() {
        super.onResume();
        int juzNumber = getIntent().getIntExtra("juz_number", 1);
        restoreScrollPosition(juzNumber);
        if (adapter != null) {
            adapter.notifyDataSetChanged(); // Perbarui UI tanpa mengatur ulang adapter
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.onDetachedFromRecyclerView(rvAyat); // Hentikan MediaPlayer jika aktivitas dihentikan
        }
    }
}