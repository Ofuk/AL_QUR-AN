package com.example.quranapp.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quranapp.R;
import com.example.quranapp.db.BookmarkDbHelper;
import com.example.quranapp.model.Ayat;
import java.io.IOException;
import java.util.List;

public class AyatAdapter extends RecyclerView.Adapter<AyatAdapter.AyatViewHolder> {
    private List<Ayat> ayatList;
    private Context context;
    private String surahName;
    private int surahNumber;
    private MediaPlayer mediaPlayer;
    private int currentPlayingPosition = -1;
    private BookmarkDbHelper dbHelper;
    private boolean isPreparing = false;
    private SharedPreferences sharedPreferences;
    private String language;

    public AyatAdapter(Context context, List<Ayat> ayatList, String surahName, int surahNumber) {
        this.context = context;
        this.ayatList = ayatList;
        this.surahName = surahName;
        this.surahNumber = surahNumber;
        this.dbHelper = new BookmarkDbHelper(context);
        this.mediaPlayer = new MediaPlayer();
        this.sharedPreferences = context.getSharedPreferences("QuranAppPrefs", Context.MODE_PRIVATE);
        this.language = sharedPreferences.getString("language", "Indonesia");
    }

    @NonNull
    @Override
    public AyatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ayat, parent, false);
        return new AyatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AyatViewHolder holder, int position) {
        Ayat ayat = ayatList.get(position);
        holder.tvAyatNumber.setText(context.getString(R.string.ayat_number, ayat.getNumber()));
        holder.tvArabic.setText(ayat.getText());
        holder.tvLatin.setText(ayat.getLatin());
        holder.tvTranslation.setText(ayat.getTranslation());
        int textSize = sharedPreferences.getInt("textSize", 16);
        holder.tvArabic.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        holder.tvTranslation.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

        // Bookmark
        boolean isBookmarked = dbHelper.isBookmarked(surahNumber, ayat.getNumber());
        holder.btnBookmark.setImageResource(isBookmarked ?
                android.R.drawable.ic_menu_save : android.R.drawable.ic_menu_add);
        holder.btnBookmark.setOnClickListener(v -> {
            if (isBookmarked) {
                dbHelper.deleteBookmark(surahNumber, ayat.getNumber());
                holder.btnBookmark.setImageResource(android.R.drawable.ic_menu_add);
            } else {
                dbHelper.addBookmark(new com.example.quranapp.model.Bookmark(
                        surahNumber, ayat.getNumber(), surahName, ayat.getText(), ayat.getTranslation()));
                holder.btnBookmark.setImageResource(android.R.drawable.ic_menu_save);
            }
        });

        // Audio
        holder.btnPlay.setImageResource(currentPlayingPosition == position && mediaPlayer.isPlaying() ?
                android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play);
        holder.progressAudio.setVisibility(currentPlayingPosition == position && isPreparing ?
                View.VISIBLE : View.GONE);
        holder.btnPlay.setOnClickListener(v -> {
            if (currentPlayingPosition == position && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                holder.btnPlay.setImageResource(android.R.drawable.ic_media_play);
                holder.progressAudio.setVisibility(View.GONE);
                isPreparing = false;
            } else {
                playAudio(ayat.getAudio(), position);
            }
        });
    }

    private void playAudio(String audioUrl, int position) {
        if (audioUrl == null || audioUrl.isEmpty()) {
            Toast.makeText(context, "Audio tidak tersedia untuk ayat ini", Toast.LENGTH_SHORT).show();
            Log.w("AyatAdapter", "Audio URL missing for ayat at position " + position);
            return;
        }

        Log.d("AyatAdapter", "Attempting to play audio: " + audioUrl);
        try {
            if (mediaPlayer.isPlaying() || isPreparing) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                isPreparing = false;
                notifyItemChanged(currentPlayingPosition);
            }
            mediaPlayer.reset();
            mediaPlayer.setDataSource(audioUrl);
            isPreparing = true;
            notifyItemChanged(position);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> {
                Log.d("AyatAdapter", "Audio prepared, starting playback for position: " + position);
                if (!isFinishing()) {
                    mediaPlayer.start();
                    currentPlayingPosition = position;
                    isPreparing = false;
                    notifyItemChanged(position);
                }
            });
            mediaPlayer.setOnCompletionListener(mp -> {
                Log.d("AyatAdapter", "Audio completed for position: " + position);
                mediaPlayer.reset();
                isPreparing = false;
                notifyItemChanged(currentPlayingPosition);
                if (position < ayatList.size() - 1) {
                    playAudio(ayatList.get(position + 1).getAudio(), position + 1);
                } else {
                    currentPlayingPosition = -1;
                    notifyItemChanged(position);
                }
            });
            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                Log.e("AyatAdapter", "MediaPlayer error: what=" + what + ", extra=" + extra);
                Toast.makeText(context, "Gagal memutar audio", Toast.LENGTH_SHORT).show();
                mediaPlayer.reset();
                isPreparing = false;
                notifyItemChanged(currentPlayingPosition);
                currentPlayingPosition = -1;
                return true;
            });
        } catch (IOException e) {
            Log.e("AyatAdapter", "IOException: " + e.getMessage());
            Toast.makeText(context, "Error memutar audio: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            isPreparing = false;
            notifyItemChanged(position);
        }
    }

    private boolean isFinishing() {
        return ((AppCompatActivity) context).isFinishing();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
            isPreparing = false;
        }
    }

    @Override
    public int getItemCount() {
        return ayatList.size();
    }

    static class AyatViewHolder extends RecyclerView.ViewHolder {
        TextView tvAyatNumber, tvArabic, tvLatin, tvTranslation;
        ImageButton btnBookmark, btnPlay;
        ProgressBar progressAudio;

        AyatViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAyatNumber = itemView.findViewById(R.id.tvAyatNumber);
            tvArabic = itemView.findViewById(R.id.tvArabic);
            tvLatin = itemView.findViewById(R.id.tvLatin);
            tvTranslation = itemView.findViewById(R.id.tvTranslation);
            btnBookmark = itemView.findViewById(R.id.btnBookmark);
            btnPlay = itemView.findViewById(R.id.btnPlay);
            progressAudio = itemView.findViewById(R.id.progressAudio);
        }
    }
}