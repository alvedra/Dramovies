package com.example.h.dramoviesvfinal.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.h.dramoviesvfinal.DetailActivity;
import com.example.h.dramoviesvfinal.R;
import com.example.h.dramoviesvfinal.entity.MovieItems;

import static com.example.h.dramoviesvfinal.database.DatabaseContract.MovieEntry.COL_OVERVIEW;
import static com.example.h.dramoviesvfinal.database.DatabaseContract.MovieEntry.COL_POSTER_PATH;
import static com.example.h.dramoviesvfinal.database.DatabaseContract.MovieEntry.COL_TITLE;
import static com.example.h.dramoviesvfinal.database.DatabaseContract.MovieEntry.CONTENT_URI;
import static com.example.h.dramoviesvfinal.database.DatabaseContract.getColumnString;

public class FavoriteCursorAdapter extends CursorRecyclerViewAdapter<FavoriteCursorAdapter.FavoriteViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public FavoriteCursorAdapter(Context context, Cursor c){
        super(context,c);
        mContext = context;
    }

    @Override
    public void onBindViewHolder(final FavoriteCursorAdapter.FavoriteViewHolder viewHolder, final Cursor cursor) {
        viewHolder.tvMovieName.setText(getColumnString(cursor,COL_TITLE));
        viewHolder.tvMovieDesc.setText(getColumnString(cursor,COL_OVERVIEW));
        Glide.with(mContext).load(getColumnString(cursor,COL_POSTER_PATH)).into(viewHolder.ivImagePoster);
        viewHolder.itemsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor.moveToPosition(viewHolder.getAdapterPosition());
                MovieItems movieItems = new MovieItems(cursor);
                Uri uri = Uri.parse(CONTENT_URI + "/" + movieItems.getId());

                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.setData(uri);
                intent.putExtra(DetailActivity.EXTRA_MOVIE,movieItems);
                intent.putExtra("GENRE",movieItems.getGenre());
                mContext.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public FavoriteCursorAdapter.FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_items,viewGroup,false);

        return new FavoriteCursorAdapter.FavoriteViewHolder(view);
    }

    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {

        TextView tvMovieName;
        TextView tvMovieDesc;
        ImageView ivImagePoster;
        RelativeLayout itemsLayout;

        public FavoriteViewHolder(View itemView){
            super(itemView);

            tvMovieName = itemView.findViewById(R.id.tv_movie_name);
            tvMovieDesc = itemView.findViewById(R.id.tv_movie_desc);
            ivImagePoster = itemView.findViewById(R.id.iv_image_poster);

            itemsLayout = itemView.findViewById(R.id.rl_items);
        }
    }

}
