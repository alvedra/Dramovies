package com.example.h.dramoviesvfinal.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.h.dramoviesvfinal.fragments.FavoriteFragment;
import com.example.h.dramoviesvfinal.fragments.NowPlayingFragment;
import com.example.h.dramoviesvfinal.fragments.SearchFragment;
import com.example.h.dramoviesvfinal.fragments.UpcomingFragment;
import com.example.h.dramoviesvfinal.R;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Fragment[] fragments;
    private Context context;

    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        fragments = new Fragment[]{
             new NowPlayingFragment(),
                new UpcomingFragment(),
                new FavoriteFragment(),
                new SearchFragment()
        };
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "Fragment";
        switch (position){
            case 0:
                title = context.getResources().getString(R.string.now_playing);
                break;
            case 1:
                title = context.getResources().getString(R.string.upcoming);
                break;
            case 2:
                title = context.getResources().getString(R.string.favorite);
                break;
            case 3:
                title = context.getResources().getString(R.string.search);
            default:
                break;
        }
        return title;
    }
}
