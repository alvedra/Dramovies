package com.example.h.dramoviesvfinal.fragments;


import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.h.dramoviesvfinal.BuildConfig;
import com.example.h.dramoviesvfinal.entity.MovieItems;
import com.example.h.dramoviesvfinal.adapter.MovieAdapter;
import com.example.h.dramoviesvfinal.adapter.MovieAsyncTask;
import com.example.h.dramoviesvfinal.R;

import java.util.ArrayList;

import static com.example.h.dramoviesvfinal.DetailActivity.EXTRA_MOVIE;


public class SearchFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<MovieItems>> {

    private RecyclerView listMovie ;
    private MovieAdapter adapter;
    ProgressBar loadingBar;

    private SearchView searchView;

    private static final String API_KEY = BuildConfig.TMDB_API_KEY;
    private static final String url = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&language=en-US&page=1";
    private static final int SEARCH_LOADER_ID = 2;

    public SearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        listMovie = rootView.findViewById(R.id.list_movie_search);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        loadingBar = view.findViewById(R.id.pg_loading3);
        loadingBar.setVisibility(View.GONE);

        adapter = new MovieAdapter(getActivity());
        adapter.notifyDataSetChanged();
        listMovie = view.findViewById(R.id.list_movie_search);
        listMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
        listMovie.setAdapter(adapter);

        Bundle bundle = new Bundle();
        String movieTitle = "Avenger";
        bundle.putString(EXTRA_MOVIE, movieTitle);
        getActivity().getSupportLoaderManager().initLoader(SEARCH_LOADER_ID,bundle,this);
    }

    @Override
    public Loader<ArrayList<MovieItems>> onCreateLoader(int id, Bundle args) {
        String movieTitle = "";
        if (args != null){
            movieTitle= args.getString(EXTRA_MOVIE);
        }
        loadingBar.setVisibility(View.VISIBLE);
        return new MovieAsyncTask(getActivity(),url,movieTitle);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieItems>> loader, ArrayList<MovieItems> data) {
        loadingBar.setVisibility(View.GONE);
        if (data == null){
            Log.d("Search Fragment : ","Data kosong");
        }
        adapter.setData(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieItems>> loader) {
        adapter.setData(null);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_option_menu, menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String movieTitle) {
                if (TextUtils.isEmpty(movieTitle)) return false;

                Bundle bundle = new Bundle();
                bundle.putString(EXTRA_MOVIE, movieTitle);
                getActivity().getSupportLoaderManager().restartLoader(SEARCH_LOADER_ID, bundle, SearchFragment.this);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }
}
