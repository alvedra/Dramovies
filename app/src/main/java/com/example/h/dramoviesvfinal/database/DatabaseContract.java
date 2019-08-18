package com.example.h.dramoviesvfinal.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String AUTHORITY = "com.example.h.dramovies";
    private static final String SCHEME = "content";

    private DatabaseContract(){}

    public static final class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorite";
        public static final String COL_TITLE = "title";
        public static final String COL_RELEASE = "release";
        public static final String COL_LANGUAGE = "language";
        public static final String COL_RATING = "rating";
        public static final String COL_OVERVIEW = "overview";
        public static final String COL_GENRE = "genre";
        public static final String COL_POSTER_PATH = "poster_path";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }
    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

}
