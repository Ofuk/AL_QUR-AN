package com.example.quranapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quranapp.JuzDetailActivity;
import com.example.quranapp.R;
import com.example.quranapp.model.Juz;
import java.util.List;

// File: java/com/example/quranapp/adapter/JuzAdapter.java
public class JuzAdapter extends RecyclerView.Adapter<JuzAdapter.JuzViewHolder> {
    private List<Juz> juzList;
    private Context context;

    public JuzAdapter(Context context, List<Juz> juzList) {
        this.context = context;
        this.juzList = juzList;
    }

    @NonNull
    @Override
    public JuzViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_juz, parent, false);
        return new JuzViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JuzViewHolder holder, int position) {
        Juz juz = juzList.get(position);
        holder.tvJuzNumber.setText("Juz " + juz.getNumber());
        holder.tvJuzInfo.setText(juz.getInfo());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, JuzDetailActivity.class);
            intent.putExtra("juz_number", juz.getNumber());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return juzList.size();
    }

    static class JuzViewHolder extends RecyclerView.ViewHolder {
        TextView tvJuzNumber, tvJuzInfo;

        JuzViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJuzNumber = itemView.findViewById(R.id.tvJuzNumber);
            tvJuzInfo = itemView.findViewById(R.id.tvJuzInfo);
        }
    }
}