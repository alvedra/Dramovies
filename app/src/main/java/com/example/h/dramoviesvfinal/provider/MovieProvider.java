package com.example.h.dramoviesvfinal.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.h.dramoviesvfinal.database.MovieHelper;

import static com.example.h.dramoviesvfinal.database.DatabaseContract.AUTHORITY;
import static com.example.h.dramoviesvfinal.database.DatabaseContract.MovieEntry.CONTENT_URI;
import static com.example.h.dramoviesvfinal.database.DatabaseContract.MovieEntry.TABLE_NAME;

public class MovieProvider extends ContentProvider {

    private static final int MOVIE = 1;
    private static final int MOVIE_ID = 2;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private MovieHelper movieHelper;

    static {
        // content://com.h.example.dramovies/movie
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, MOVIE);
        // content://com.h.example.dramovies/movie/id
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", MOVIE_ID);
    }

    @Override
    public boolean onCreate() {
        movieHelper = MovieHelper.getInstance(getContext());
        return true;
    }
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        movieHelper.open();
        Cursor cursor;
        switch (sUriMatcher.match(uri)){
            case MOVIE:
                cursor = movieHelper.queryProvider();
                break;
            case MOVIE_ID:
                cursor = movieHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }
        return cursor;
    }
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        movieHelper.open();
        long added;
        switch (sUriMatcher.match(uri)){
            case MOVIE:
                added = movieHelper.insertProvider(contentValues);
                break;
            default:
                added = 0;
                break;
        }
//        getContext().getContentResolver().notifyChange(CONTENT_URI, new FavoriteFragment.DataObserver(new Handler(),getContext()));
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(CONTENT_URI + "/" + added);
    }
    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        movieHelper.open();
        int deleted;
        switch (sUriMatcher.match(uri)){
            case MOVIE_ID:
                deleted = movieHelper.deleteProvider(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }
//        getContext().getContentResolver().notifyChange(CONTENT_URI, new FavoriteFragment.DataObserver(new Handler(),getContext()));
        getContext().getContentResolver().notifyChange(uri, null);
        return deleted;
    }
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        movieHelper.open();
        int update;
        switch (sUriMatcher.match(uri)){
            case MOVIE:
                update = movieHelper.updateProvider(uri.getLastPathSegment(), contentValues);
                break;
            default:
                update = 0;
                break;
        }
//        getContext().getContentResolver().notifyChange(CONTENT_URI, new FavoriteFragment.DataObserver(new Handler(),getContext()));
        getContext().getContentResolver().notifyChange(uri, null);
        return update;
    }
}
