package com.example.quranapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.quranapp.fragment.BookmarkFragment;
import com.example.quranapp.fragment.JuzFragment;
import com.example.quranapp.fragment.SurahFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new SurahFragment();
            case 1:
                return new JuzFragment();
            case 2:
                return new BookmarkFragment();
            default:
                return new SurahFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}