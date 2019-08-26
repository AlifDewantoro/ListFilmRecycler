package com.example.alifd.listfilmrecycler.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.alifd.listfilmrecycler.fragment.FilmFragment;
import com.example.alifd.listfilmrecycler.fragment.TvShowFragment;

public class CustomPagerAdapter extends FragmentStatePagerAdapter {
    private int tabs;

    public CustomPagerAdapter(FragmentManager fm, int tabs) {
        super(fm);
        this.tabs = tabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new FilmFragment();
            case 1:
                return new TvShowFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabs;
    }
}
