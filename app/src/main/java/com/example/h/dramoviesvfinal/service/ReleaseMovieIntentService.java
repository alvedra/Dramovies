package com.example.h.dramoviesvfinal.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.h.dramoviesvfinal.BuildConfig;
import com.example.h.dramoviesvfinal.R;
import com.example.h.dramoviesvfinal.entity.MovieItems;
import com.example.h.dramoviesvfinal.receiver.AlarmReceiver;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class ReleaseMovieIntentService extends IntentService {

    private static final String API_KEY = BuildConfig.TMDB_API_KEY;
    private static final String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=" + API_KEY + "&language=en-US&page=1";
    private static final int notifId = 102;

    public ReleaseMovieIntentService(){
        super("ReleaseMovieIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AlarmReceiver alarmReceiver = new AlarmReceiver();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date todayDate = Calendar.getInstance().getTime();
        ArrayList<MovieItems> listMovie = getData();
        for(MovieItems items : listMovie){
            String releaseDate = items.getRelease();
            try {
                Date date = sdf.parse(releaseDate);
                if(isSameDay(todayDate,date)){
                    String title = items.getTitle();
                    String message = getResources().getString(R.string.service_message);
                    alarmReceiver.showAlarmNotification(getApplicationContext(),title, title + message, notifId);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }


    }

    private ArrayList<MovieItems> getData(){
        SyncHttpClient client = new SyncHttpClient();

        final ArrayList<MovieItems> mMovieItems = new ArrayList<>();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                setUseSynchronousMode(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject movie = list.getJSONObject(i);
                        MovieItems movieItems = new MovieItems(movie);
                        mMovieItems.add(movieItems);
                    }
                    Log.d("Service : ", "Berhasil connect");
                    Log.d("Service : ",mMovieItems.get(0).getTitle());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Service : ", "Gagal connect ke url");
            }
        });

        return mMovieItems;
    }

    private boolean isSameDay(Date date1, Date date2){
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        boolean sameDay = cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
        return sameDay;
    }
}
