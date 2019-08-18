package com.example.h.dramoviesvfinal.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.h.dramoviesvfinal.BuildConfig;
import com.example.h.dramoviesvfinal.entity.MovieItems;
import com.example.h.dramoviesvfinal.adapter.MovieAdapter;
import com.example.h.dramoviesvfinal.adapter.MovieAsyncTask;
import com.example.h.dramoviesvfinal.R;

import java.util.ArrayList;


public class NowPlayingFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<MovieItems>> {

    private RecyclerView listMovie ;
    private MovieAdapter adapter;
    ProgressBar loadingBar;

    private static final String API_KEY = BuildConfig.TMDB_API_KEY;
    private static final String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=" + API_KEY + "&language=en-US&page=1";
    private static final int NOW_PLAYING_LOADER_ID = 0;

    public NowPlayingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_now_playing, container, false);
        listMovie = rootView.findViewById(R.id.list_movie_now_playing);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingBar = view.findViewById(R.id.pg_loading);
        loadingBar.setVisibility(View.GONE);

        adapter = new MovieAdapter(getActivity());
        adapter.notifyDataSetChanged();
        listMovie = view.findViewById(R.id.list_movie_now_playing);
        listMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
        listMovie.setAdapter(adapter);

        if (getActivity() != null){
            getActivity().getSupportLoaderManager().initLoader(NOW_PLAYING_LOADER_ID,null,this);
        }
    }

    @Override
    public Loader<ArrayList<MovieItems>> onCreateLoader(int id, Bundle args) {

        loadingBar.setVisibility(View.VISIBLE);
        return new MovieAsyncTask(getActivity(),url);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieItems>> loader, ArrayList<MovieItems> data) {
        loadingBar.setVisibility(View.GONE);
        if (data == null){
            Log.d("Now Playing Fragment : ","Data kosong");
        }
        adapter.setData(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieItems>> loader) {
        adapter.setData(null);
    }

}
