package com.example.h.dramoviesvfinal;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.h.dramoviesvfinal.database.DatabaseContract.MovieEntry.COL_POSTER_PATH;
import static com.example.h.dramoviesvfinal.database.DatabaseContract.MovieEntry.COL_TITLE;
import static com.example.h.dramoviesvfinal.database.DatabaseContract.MovieEntry.CONTENT_URI;
import static com.example.h.dramoviesvfinal.database.DatabaseContract.getColumnString;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final List<String> mWidgetItems = new ArrayList<>();
    private final List<String> mWidgetTitles = new ArrayList<>();
    private final Context mContext;

    public StackRemoteViewsFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
//        mWidgetItems.add(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.darth_vader));
//        mWidgetItems.add(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.star_wars_logo));
//        mWidgetItems.add(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.storm_trooper));
//        mWidgetItems.add(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.starwars));
//        mWidgetItems.add(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.falcon));

        final long identityToken = Binder.clearCallingIdentity();

        String[] projection = { COL_TITLE, COL_POSTER_PATH };
        Cursor cursor = mContext.getContentResolver().query(CONTENT_URI, projection,null,null,null);
        if(cursor.getCount() > 0){
            try {
                while (cursor.moveToNext()) {
                    mWidgetItems.add(getColumnString(cursor,COL_POSTER_PATH));
                    mWidgetTitles.add(getColumnString(cursor, COL_TITLE));
                }
            } finally {
                cursor.close();
            }
        }
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
//        rv.setImageViewBitmap(R.id.imageView, mWidgetItems.get(position));

        String posterUrl = mWidgetItems.get(position);
        try {
            Bitmap preview = Glide.with(mContext)
                    .asBitmap()
                    .load(posterUrl)
                    .apply(new RequestOptions().fitCenter())
                    .submit()
                    .get();
            rv.setImageViewBitmap(R.id.imageView , preview);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Bundle extras = new Bundle();
        extras.putString(ImagesBannerWidget.EXTRA_ITEM, mWidgetTitles.get(position));
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
