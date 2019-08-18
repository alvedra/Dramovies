package com.example.h.dramoviesvfinal.adapter;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.h.dramoviesvfinal.entity.MovieItems;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieAsyncTask extends AsyncTaskLoader<ArrayList<MovieItems>> {

    private ArrayList<MovieItems> mData;
    private Boolean mHasResult = false;
    private String mUrl;
    private String movieTitle;

    public MovieAsyncTask(final Context context, String url) {
        super(context);

        this.mUrl = url;
        onContentChanged();
    }

    public MovieAsyncTask(final Context context, String url, String movieTitle) {
        super(context);

        this.mUrl = url;
        this.movieTitle = movieTitle;
        onContentChanged();
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged())
            forceLoad();
        else if (mHasResult)
            deliverResult(mData);
    }

    @Override
    public void deliverResult(final ArrayList<MovieItems> data) {
        mData = data;
        mHasResult = true;
        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (mHasResult) {
            mData = null;
            mHasResult = false;
        }
    }

    @Override
    public ArrayList<MovieItems> loadInBackground() {
        SyncHttpClient client = new SyncHttpClient();

        final ArrayList<MovieItems> mMovieItems = new ArrayList<>();
        if(movieTitle != null){
            mUrl = mUrl + "&query="+movieTitle;
        }
        client.get(mUrl, new AsyncHttpResponseHandler() {
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
                    Log.d("AsyntaskLoader : ", "Berhasil connect");
                    Log.d("Asynctaskloader : ",mMovieItems.get(0).getTitle());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("AsyntaskLoader : ", "Gagal connect ke url");
            }
        });

        return mMovieItems;
    }

}