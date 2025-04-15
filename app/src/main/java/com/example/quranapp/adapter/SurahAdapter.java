package com.example.quranapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quranapp.R;
import com.example.quranapp.SurahDetailActivity;
import com.example.quranapp.model.Surah;
import java.util.List;

public class SurahAdapter extends RecyclerView.Adapter<SurahAdapter.SurahViewHolder> {
    private List<Surah> surahList;
    private Context context;

    public SurahAdapter(Context context, List<Surah> surahList) {
        this.context = context;
        this.surahList = surahList;
    }


    @NonNull
    @Override
    public SurahViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_surah, parent, false);
        return new SurahViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SurahViewHolder holder, int position) {
        Surah surah = surahList.get(position);
        holder.tvSurahNumber.setText(String.valueOf(surah.getNumber()));
        holder.tvSurahName.setText(surah.getEnglishName());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SurahDetailActivity.class);
            intent.putExtra("surah_number", surah.getNumber());
            intent.putExtra("surah_name", surah.getEnglishName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return surahList.size();
    }

    static class SurahViewHolder extends RecyclerView.ViewHolder {
        TextView tvSurahNumber, tvSurahName;

        SurahViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSurahNumber = itemView.findViewById(R.id.tvSurahNumber);
            tvSurahName = itemView.findViewById(R.id.tvSurahName);
        }
    }
}