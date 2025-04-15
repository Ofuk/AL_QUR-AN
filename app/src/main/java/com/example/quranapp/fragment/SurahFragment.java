package com.example.quranapp.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quranapp.R;
import com.example.quranapp.adapter.SurahAdapter;
import com.example.quranapp.viewmodel.QuranViewModel;

public class SurahFragment extends Fragment {
    private RecyclerView rvSurah;
    private SurahAdapter adapter;
    private QuranViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_surah, container, false);
        rvSurah = view.findViewById(R.id.rvSurah);
        rvSurah.setLayoutManager(new LinearLayoutManager(getContext()));
        viewModel = new ViewModelProvider(this).get(QuranViewModel.class);
        viewModel.getSurahs().observe(getViewLifecycleOwner(), surahs -> {
            Log.d("SurahFragment", "Surah list: " + (surahs != null ? surahs.size() : "null"));
            if (surahs != null) {
                adapter = new SurahAdapter(getContext(), surahs);
                rvSurah.setAdapter(adapter);
            }
        });
        return view;
    }

}