package com.example.h.dramoviesvfinal.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.h.dramoviesvfinal.R;
import com.example.h.dramoviesvfinal.adapter.FavoriteCursorAdapter;

import static android.provider.BaseColumns._ID;
import static com.example.h.dramoviesvfinal.database.DatabaseContract.MovieEntry.COL_GENRE;
import static com.example.h.dramoviesvfinal.database.DatabaseContract.MovieEntry.COL_LANGUAGE;
import static com.example.h.dramoviesvfinal.database.DatabaseContract.MovieEntry.COL_OVERVIEW;
import static com.example.h.dramoviesvfinal.database.DatabaseContract.MovieEntry.COL_POSTER_PATH;
import static com.example.h.dramoviesvfinal.database.DatabaseContract.MovieEntry.COL_RATING;
import static com.example.h.dramoviesvfinal.database.DatabaseContract.MovieEntry.COL_RELEASE;
import static com.example.h.dramoviesvfinal.database.DatabaseContract.MovieEntry.COL_TITLE;
import static com.example.h.dramoviesvfinal.database.DatabaseContract.MovieEntry.CONTENT_URI;

public class FavoriteFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private ProgressBar loadingBar;
    private RecyclerView listMovie ;
    private FavoriteCursorAdapter adapter;
    private TextView tvListEmpty;

    private static final int FAVORITE_LOADER_ID = 4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        loadingBar = view.findViewById(R.id.pg_loading4);
        loadingBar.setVisibility(View.GONE);
        tvListEmpty = view.findViewById(R.id.tv_favorite_list_empty);
        tvListEmpty.setVisibility(View.GONE);

        adapter = new FavoriteCursorAdapter(getContext(),null);

        listMovie = view.findViewById(R.id.list_movie_favorite);

        if(getActivity() != null) {
            listMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
            listMovie.setAdapter(adapter);
            getActivity().getSupportLoaderManager().initLoader(FAVORITE_LOADER_ID,null,this);

        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getLoaderManager().restartLoader(FAVORITE_LOADER_ID, null, this);
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        loadingBar.setVisibility(View.VISIBLE);

        String[] projection = {
                _ID,
                COL_TITLE,
                COL_LANGUAGE,
                COL_RATING,
                COL_RELEASE,
                COL_OVERVIEW,
                COL_GENRE,
                COL_POSTER_PATH
        };
        return new CursorLoader(getContext(), CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        loadingBar.setVisibility(View.GONE);
        if (data.getCount() > 0)
        {
            tvListEmpty.setVisibility(View.GONE);
            listMovie.setVisibility(View.VISIBLE);
            adapter.swapCursor(data);
        }else{
            tvListEmpty.setVisibility(View.VISIBLE);
            listMovie.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
