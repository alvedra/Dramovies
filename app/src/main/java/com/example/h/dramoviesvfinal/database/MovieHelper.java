package com.example.h.dramoviesvfinal.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.h.dramoviesvfinal.entity.MovieItems;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.example.h.dramoviesvfinal.database.DatabaseContract.MovieEntry.COL_GENRE;
import static com.example.h.dramoviesvfinal.database.DatabaseContract.MovieEntry.COL_LANGUAGE;
import static com.example.h.dramoviesvfinal.database.DatabaseContract.MovieEntry.COL_OVERVIEW;
import static com.example.h.dramoviesvfinal.database.DatabaseContract.MovieEntry.COL_POSTER_PATH;
import static com.example.h.dramoviesvfinal.database.DatabaseContract.MovieEntry.COL_RATING;
import static com.example.h.dramoviesvfinal.database.DatabaseContract.MovieEntry.COL_RELEASE;
import static com.example.h.dramoviesvfinal.database.DatabaseContract.MovieEntry.COL_TITLE;
import static com.example.h.dramoviesvfinal.database.DatabaseContract.MovieEntry.TABLE_NAME;

public class MovieHelper {

    private static final String DATABASE_TABLE = TABLE_NAME;
    private final DatabaseHelper dataBaseHelper;
    private static MovieHelper INSTANCE;

    private SQLiteDatabase database;

    private MovieHelper(Context context) {
        dataBaseHelper = new DatabaseHelper(context);
    }

    public static MovieHelper getInstance(Context context) {
        if (INSTANCE == null){
            synchronized (SQLiteOpenHelper.class){
                if (INSTANCE == null){
                    INSTANCE = new MovieHelper(context);
                }
            }
        }
        return INSTANCE;
    }


    public void open() throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }

    public void close() {
        dataBaseHelper.close();

        if (database.isOpen())
            database.close();
    }

    public ArrayList<MovieItems> query() {
        ArrayList<MovieItems> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE
                , null
                , null
                , null
                , null
                , null, _ID + " DESC"
                , null);
        cursor.moveToFirst();
        MovieItems movieItems;
        if (cursor.getCount() > 0) {
            do {
                movieItems = new MovieItems();
                movieItems.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                movieItems.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE)));
                movieItems.setRelease(cursor.getString(cursor.getColumnIndexOrThrow(COL_RELEASE)));
                movieItems.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(COL_OVERVIEW)));
                movieItems.setLanguage(cursor.getString(cursor.getColumnIndexOrThrow(COL_LANGUAGE)));
                movieItems.setRating(cursor.getString(cursor.getColumnIndexOrThrow(COL_RATING)));
                movieItems.setGenre(cursor.getString(cursor.getColumnIndexOrThrow(COL_GENRE)));
                movieItems.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(COL_POSTER_PATH)));

                arrayList.add(movieItems);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insert(MovieItems movieItems) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(COL_TITLE, movieItems.getTitle());
        initialValues.put(COL_RELEASE, movieItems.getRelease());
        initialValues.put(COL_OVERVIEW, movieItems.getOverview());
        initialValues.put(COL_LANGUAGE, movieItems.getLanguage());
        initialValues.put(COL_RATING, movieItems.getRating());
        initialValues.put(COL_GENRE, movieItems.getGenre());
        return database.insert(DATABASE_TABLE, null, initialValues);
    }

    public int update(MovieItems movieItems) {
        ContentValues args = new ContentValues();
        args.put(COL_TITLE, movieItems.getTitle());
        args.put(COL_RELEASE, movieItems.getRelease());
        args.put(COL_OVERVIEW, movieItems.getOverview());
        args.put(COL_LANGUAGE, movieItems.getLanguage());
        args.put(COL_RATING, movieItems.getRating());
        return database.update(DATABASE_TABLE, args, _ID + "= '" + movieItems.getId() + "'", null);
    }

    public int delete(int id) {
        return database.delete(TABLE_NAME, _ID + " = '" + id + "'", null);
    }

    public Cursor queryByIdProvider(String id) {
        return database.query(DATABASE_TABLE, null
                , _ID + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    public Cursor queryProvider() {
        return database.query(DATABASE_TABLE
                , null
                , null
                , null
                , null
                , null
                , _ID + " ASC");
    }

    public long insertProvider(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int updateProvider(String id, ContentValues values) {
        return database.update(DATABASE_TABLE, values, _ID + " = ?", new String[]{id});
    }

    public int deleteProvider(String id) {
        return database.delete(DATABASE_TABLE, _ID + " = ?", new String[]{id});
    }

}
