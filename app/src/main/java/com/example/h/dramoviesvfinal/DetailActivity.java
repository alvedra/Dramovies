package com.example.h.dramoviesvfinal;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.h.dramoviesvfinal.entity.MovieItems;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static android.provider.BaseColumns._ID;
import static com.example.h.dramoviesvfinal.database.DatabaseContract.MovieEntry.COL_GENRE;
import static com.example.h.dramoviesvfinal.database.DatabaseContract.MovieEntry.COL_LANGUAGE;
import static com.example.h.dramoviesvfinal.database.DatabaseContract.MovieEntry.COL_OVERVIEW;
import static com.example.h.dramoviesvfinal.database.DatabaseContract.MovieEntry.COL_POSTER_PATH;
import static com.example.h.dramoviesvfinal.database.DatabaseContract.MovieEntry.COL_RATING;
import static com.example.h.dramoviesvfinal.database.DatabaseContract.MovieEntry.COL_RELEASE;
import static com.example.h.dramoviesvfinal.database.DatabaseContract.MovieEntry.COL_TITLE;
import static com.example.h.dramoviesvfinal.database.DatabaseContract.MovieEntry.CONTENT_URI;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "extra_movie";
    private static final String API_KEY = BuildConfig.TMDB_API_KEY;
    final int maxGenreToShow = 2;
    String genre = "";
    Boolean movieAdded;

    private Menu menu;
    private MovieItems movieItems;
    TextView tvRelease, tvLanguage, tvRating, tvOverview, tvGenre;
    ImageView imagePoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvRelease = findViewById(R.id.tv_release_date);
        tvLanguage = findViewById(R.id.tv_language);
        tvRating = findViewById(R.id.tv_rating);
        tvOverview = findViewById(R.id.tv_overview);
        tvGenre = findViewById(R.id.tv_genre);
        imagePoster = findViewById(R.id.iv_movie_image);

        movieItems = getIntent().getParcelableExtra(EXTRA_MOVIE);
        String title = movieItems.getTitle();
        String release = movieItems.getRelease();
        String language = movieItems.getLanguage();
        language = language.toUpperCase();

        String rating = movieItems.getRating();
        final int[] genreId = movieItems.getGenreId();
        String overview = movieItems.getOverview();
        String posterPath = movieItems.getPosterPath();

        setTitle(title);
        tvRelease.setText(release);
        tvLanguage.setText(language);
        tvRating.setText(rating);
        tvOverview.setText(overview);
        Glide.with(this).load(posterPath).into(imagePoster);

        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.themoviedb.org/3/genre/movie/list?api_key="+API_KEY+"&language=en-US";

        if(genreId != null) {
            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String result = new String(responseBody);
                    try {
                        JSONObject responseObject = new JSONObject(result);
                        JSONArray genreArray = responseObject.getJSONArray("genres");
                        int genreArraySize = genreArray.length();

                        for (int i = 0; i < maxGenreToShow; i++) {
                            for (int j = 0; j < genreArraySize; j++) {
                                int id = genreArray.getJSONObject(j).getInt("id");

                                if (genreId[i] == id) {
                                    String name = genreArray.getJSONObject(j).getString("name");
                                    genre += name + " | ";
                                    break;
                                }
                            }
                        }
                        if (genre != null) {
                            genre = genre.substring(0, genre.length() - 2);
                            tvGenre.setText(genre);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(getApplicationContext(), "Failed to fetch genre", Toast.LENGTH_SHORT);
                }
            });
        }else{
            String genre = getIntent().getStringExtra("GENRE");
            if(genre != null){
                tvGenre.setText(genre);
                this.genre = genre;
            }
        }
    }

    private void checkMovie(Menu menu){
        Uri uri = getIntent().getData();
        if(uri != null){
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);

            if (cursor.moveToFirst()) {
                if(menu != null) {
                    menu.findItem(R.id.menu_detail_favorite).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_like));
                    movieAdded = true;
                }
            }else{
                if(menu != null) {
                    menu.findItem(R.id.menu_detail_favorite).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_likewhite));
                    movieAdded = false;
                }
            }
            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_option_menu, menu);
        this.menu = menu;
        checkMovie(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_detail_favorite:
                if(movieAdded){
                    getContentResolver().delete(getIntent().getData(),null,null);
                    Toast.makeText(DetailActivity.this, "Satu item dihapus", Toast.LENGTH_SHORT).show();
                    checkMovie(menu);
                }else{
                    if(!genre.equals("")) {
                        int id = movieItems.getId();
                        String title = movieItems.getTitle();
                        String release = movieItems.getRelease();
                        String language = movieItems.getLanguage();
                        language = language.toUpperCase();
                        String rating = movieItems.getRating();
                        String overview = movieItems.getOverview();
                        String posterPath = movieItems.getPosterPath();

                        ContentValues values = new ContentValues();
                        values.put(_ID, id);
                        values.put(COL_TITLE, title);
                        values.put(COL_RELEASE, release);
                        values.put(COL_LANGUAGE, language);
                        values.put(COL_RATING, rating);
                        values.put(COL_OVERVIEW, overview);
                        values.put(COL_GENRE, genre);
                        values.put(COL_POSTER_PATH, posterPath);

                        getContentResolver().insert(CONTENT_URI, values);
                        Toast.makeText(DetailActivity.this, "Satu item berhasil disimpan", Toast.LENGTH_SHORT).show();
                        checkMovie(menu);
                    }
                }

                return super.onOptionsItemSelected(item);
            case R.id.menu_setting_language:
                Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(mIntent);
                return super.onOptionsItemSelected(item);
            case R.id.menu_setting_reminder:
                Intent preference = new Intent(this,PrefencesActivity.class);
                startActivity(preference);
                return super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
