// File: java/com/example/quranapp/SurahDetailActivity.java
package com.example.quranapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quranapp.adapter.AyatAdapter;
import com.example.quranapp.viewmodel.QuranViewModel;

public class SurahDetailActivity extends AppCompatActivity {
    private TextView tvSurahName;
    private RecyclerView rvAyat;
    private ProgressBar progressLoading;
    private AyatAdapter adapter;
    private QuranViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surah_detail);

        tvSurahName = findViewById(R.id.tvSurahName);
        rvAyat = findViewById(R.id.rvAyat);
        progressLoading = findViewById(R.id.progressLoading);
        rvAyat.setLayoutManager(new LinearLayoutManager(this));

        int surahNumber = getIntent().getIntExtra("surah_number", 1);
        String surahName = getIntent().getStringExtra("surah_name");

        tvSurahName.setText(surahName);
        viewModel = new ViewModelProvider(this).get(QuranViewModel.class);
        progressLoading.setVisibility(View.VISIBLE);
        viewModel.getAyats(surahNumber).observe(this, ayats -> {
            if (ayats != null) {
                Log.d("SurahDetailActivity", "Ayats loaded: " + ayats.size());
                adapter = new AyatAdapter(this, ayats, surahName, surahNumber);
                rvAyat.setAdapter(adapter);
                progressLoading.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.onDetachedFromRecyclerView(rvAyat);
        }
    }
}