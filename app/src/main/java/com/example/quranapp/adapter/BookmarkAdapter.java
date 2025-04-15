package com.example.quranapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quranapp.R;
import com.example.quranapp.db.BookmarkDbHelper;
import com.example.quranapp.model.Bookmark;
import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder> {
    private List<Bookmark> bookmarkList;
    private Context context;
    private BookmarkDbHelper dbHelper;

    public BookmarkAdapter(Context context, List<Bookmark> bookmarkList) {
        this.context = context;
        this.bookmarkList = bookmarkList;
        this.dbHelper = new BookmarkDbHelper(context);
    }

    @NonNull
    @Override
    public BookmarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bookmark, parent, false);
        return new BookmarkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkViewHolder holder, int position) {
        Bookmark bookmark = bookmarkList.get(position);
        holder.tvBookmarkInfo.setText(bookmark.getSurahName() + " - Ayat " + bookmark.getAyatNumber());
        holder.tvBookmarkArabic.setText(bookmark.getArabic());
        holder.tvBookmarkTranslation.setText(bookmark.getTranslation());
        holder.btnDeleteBookmark.setOnClickListener(v -> {
            dbHelper.deleteBookmark(bookmark.getSurahNumber(), bookmark.getAyatNumber());
            bookmarkList.remove(position);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return bookmarkList.size();
    }

    static class BookmarkViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookmarkInfo, tvBookmarkArabic, tvBookmarkTranslation;
        ImageButton btnDeleteBookmark;

        BookmarkViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookmarkInfo = itemView.findViewById(R.id.tvBookmarkInfo);
            tvBookmarkArabic = itemView.findViewById(R.id.tvBookmarkArabic);
            tvBookmarkTranslation = itemView.findViewById(R.id.tvBookmarkTranslation);
            btnDeleteBookmark = itemView.findViewById(R.id.btnDeleteBookmark);
        }
    }
}