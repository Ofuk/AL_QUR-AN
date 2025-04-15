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
import com.example.quranapp.adapter.BookmarkAdapter;
import com.example.quranapp.db.BookmarkDbHelper;

public class BookmarkFragment extends Fragment {
    private RecyclerView rvBookmark;
    private BookmarkAdapter adapter;
    private BookmarkDbHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        rvBookmark = view.findViewById(R.id.rvBookmark);
        rvBookmark.setLayoutManager(new LinearLayoutManager(getContext()));
        dbHelper = new BookmarkDbHelper(getContext());
        adapter = new BookmarkAdapter(getContext(), dbHelper.getAllBookmarks());
        rvBookmark.setAdapter(adapter);
        return view;
    }
}