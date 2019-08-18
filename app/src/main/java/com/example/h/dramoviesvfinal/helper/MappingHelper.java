package com.example.h.dramoviesvfinal.helper;

import android.database.Cursor;

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
import static com.example.h.dramoviesvfinal.database.DatabaseContract.getColumnInt;
import static com.example.h.dramoviesvfinal.database.DatabaseContract.getColumnString;

public class MappingHelper {

    public static ArrayList<MovieItems> mapCursorToArrayList(Cursor movieCursor){
        ArrayList movieList = new ArrayList<>();

        while (movieCursor.moveToNext()){
            int id = getColumnInt(movieCursor,_ID);
            String title = getColumnString(movieCursor, COL_TITLE);
            String release = getColumnString(movieCursor, COL_RELEASE);
            String overview = getColumnString(movieCursor, COL_OVERVIEW);
            String rating = getColumnString(movieCursor, COL_RATING);
            String language = getColumnString(movieCursor, COL_LANGUAGE);
            String posterPath = getColumnString(movieCursor, COL_POSTER_PATH);
            String genre = getColumnString(movieCursor, COL_GENRE);

            movieList.add(new MovieItems(id,title,release,overview,rating,language,posterPath,genre));
        }

        return movieList;
    }
}
