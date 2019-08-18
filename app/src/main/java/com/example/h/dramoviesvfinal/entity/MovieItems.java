package com.example.h.dramoviesvfinal.entity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class MovieItems implements Parcelable {

    private int id;
    private int[] genreId;
    private String title;
    private String release;
    private String overview;
    private String rating;
    private String language;
    private String posterPath;

    private String genre;

    static final String POSTER_URL = "https://image.tmdb.org/t/p/w154";

    public MovieItems(){};

    public MovieItems(int id, String title, String release, String overview, String rating, String language, String posterPath, String genre) {
        this.id = id;
        this.title = title;
        this.release = release;
        this.overview = overview;
        this.rating = rating;
        this.language = language;
        this.posterPath = posterPath;
        this.genre = genre;
    }

    public MovieItems(Cursor cursor){
        this.id = getColumnInt(cursor, _ID);
        this.title = getColumnString(cursor, COL_TITLE);
        this.release = getColumnString(cursor, COL_RELEASE);
        this.overview = getColumnString(cursor, COL_OVERVIEW);
        this.rating = getColumnString(cursor, COL_RATING);
        this.language = getColumnString(cursor, COL_LANGUAGE);
        this.posterPath = getColumnString(cursor, COL_POSTER_PATH);
        this.genre = getColumnString(cursor, COL_GENRE);
    }

    public MovieItems(JSONObject object){

        try{

            int id = object.getInt("id");

            JSONArray genreIdArray = object.getJSONArray("genre_ids");
            int[] genreId = new int[genreIdArray.length()];
            for(int i = 0; i < genreIdArray.length(); i++){
                genreId[i] = genreIdArray.getInt(i);
            }

            String title = object.getString("title");
            String release = object.getString("release_date");
            String overview = object.getString("overview");
            String rating = object.getString("vote_average");
            String language = object.getString("original_language");
            String posterPath = object.getString("poster_path");
            posterPath = POSTER_URL + posterPath;

            this.id = id;
            this.genreId = genreId;
            this.title = title;
            this.release = release;
            this.overview = overview;
            this.rating = rating;
            this.language = language;
            this.posterPath = posterPath;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }
    public int[] getGenreId() {
        return genreId;
    }
    public String getTitle() {
        return title;
    }
    public String getPosterPath() {
        return posterPath;
    }
    public String getRelease() {
        return release;
    }
    public String getOverview() {
        return overview;
    }
    public String getRating() { return rating;}
    public String getLanguage() {
        return language;
    }
    public String getGenre() {
        return genre;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGenreId(int[] genreId) {
        this.genreId = genreId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }



    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeIntArray(this.genreId);
        dest.writeString(this.title);
        dest.writeString(this.release);
        dest.writeString(this.overview);
        dest.writeString(this.rating);
        dest.writeString(this.language);
        dest.writeString(this.posterPath);
    }

    protected MovieItems(Parcel in) {
        this.id = in.readInt();
        this.genreId = in.createIntArray();
        this.title = in.readString();
        this.release = in.readString();
        this.overview = in.readString();
        this.rating = in.readString();
        this.language = in.readString();
        this.posterPath = in.readString();
    }

    public static final Parcelable.Creator<MovieItems> CREATOR = new Parcelable.Creator<MovieItems>() {
        @Override
        public MovieItems createFromParcel(Parcel source) {
            return new MovieItems(source);
        }

        @Override
        public MovieItems[] newArray(int size) {
            return new MovieItems[size];
        }
    };
}

