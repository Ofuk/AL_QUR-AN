package com.example.quranapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quranapp.R;
import com.example.quranapp.adapter.JuzAdapter;
import com.example.quranapp.model.Juz;
import java.util.ArrayList;
import java.util.List;

public class JuzFragment extends Fragment {
    private RecyclerView rvJuz;
    private JuzAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_juz, container, false);
        rvJuz = view.findViewById(R.id.rvJuz);
        rvJuz.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Juz> juzList = new ArrayList<>();
        // Tambahkan 30 Juz
        juzList.add(new Juz(1, "Al-Fatiha 1 - Al-Baqarah 141"));
        juzList.add(new Juz(2, "Al-Baqarah 142 - Al-Baqarah 252"));
        juzList.add(new Juz(3, "Al-Baqarah 253 - Al-Imran 92"));
        juzList.add(new Juz(4, "Al-Imran 93 - An-Nisa 23"));
        juzList.add(new Juz(5, "An-Nisa 24 - An-Nisa 147"));
        juzList.add(new Juz(6, "An-Nisa 148 - Al-Maida 81"));
        juzList.add(new Juz(7, "Al-Maida 82 - Al-An'am 110"));
        juzList.add(new Juz(8, "Al-An'am 111 - Al-A'raf 87"));
        juzList.add(new Juz(9, "Al-A'raf 88 - Al-Anfal 40"));
        juzList.add(new Juz(10, "Al-Anfal 41 - At-Tawba 92"));
        juzList.add(new Juz(11, "At-Tawba 93 - Hud 5"));
        juzList.add(new Juz(12, "Hud 6 - Yusuf 52"));
        juzList.add(new Juz(13, "Yusuf 53 - Ibrahim 52"));
        juzList.add(new Juz(14, "Al-Hijr 1 - An-Nahl 128"));
        juzList.add(new Juz(15, "Al-Isra 1 - Al-Kahf 74"));
        juzList.add(new Juz(16, "Al-Kahf 75 - Ta-Ha 135"));
        juzList.add(new Juz(17, "Al-Anbiya 1 - Al-Hajj 78"));
        juzList.add(new Juz(18, "Al-Mu'minun 1 - Al-Furqan 20"));
        juzList.add(new Juz(19, "Al-Furqan 21 - An-Naml 55"));
        juzList.add(new Juz(20, "An-Naml 56 - Al-Ankabut 45"));
        juzList.add(new Juz(21, "Al-Ankabut 46 - Al-Ahzab 30"));
        juzList.add(new Juz(22, "Al-Ahzab 31 - Ya-Sin 27"));
        juzList.add(new Juz(23, "Ya-Sin 28 - Az-Zumar 31"));
        juzList.add(new Juz(24, "Az-Zumar 32 - Fussilat 46"));
        juzList.add(new Juz(25, "Fussilat 47 - Al-Jathiya 37"));
        juzList.add(new Juz(26, "Al-Ahqaf 1 - Adh-Dhariyat 30"));
        juzList.add(new Juz(27, "Adh-Dhariyat 31 - Al-Hadid 29"));
        juzList.add(new Juz(28, "Al-Mujadila 1 - At-Tahrim 12"));
        juzList.add(new Juz(29, "Al-Mulk 1 - Al-Mursalat 50"));
        juzList.add(new Juz(30, "An-Naba 1 - An-Nas 6"));
        adapter = new JuzAdapter(getContext(), juzList);
        rvJuz.setAdapter(adapter);
        return view;
    }
}