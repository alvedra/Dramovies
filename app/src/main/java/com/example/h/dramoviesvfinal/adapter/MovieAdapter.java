package com.example.h.dramoviesvfinal.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.example.h.dramoviesvfinal.entity.MovieItems;
import com.example.h.dramoviesvfinal.R;

import java.util.ArrayList;

import static com.example.h.dramoviesvfinal.database.DatabaseContract.MovieEntry.CONTENT_URI;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<MovieItems> mData;

    public MovieAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_items,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

        viewHolder.tvMovieName.setText(mData.get(position).getTitle());
        viewHolder.tvMovieDesc.setText(mData.get(position).getOverview());

        Glide.with(mContext).load(mData.get(position).getPosterPath()).into(viewHolder.ivImagePoster);

        viewHolder.itemsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieItems movieItems = mData.get(position);
                Uri uri = Uri.parse(CONTENT_URI + "/" + movieItems.getId());

                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.setData(uri);
                intent.putExtra(DetailActivity.EXTRA_MOVIE,movieItems);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mData == null) return 0;
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvMovieName;
        TextView tvMovieDesc;
        ImageView ivImagePoster;
        RelativeLayout itemsLayout;

        public ViewHolder(View itemView){
            super(itemView);

            tvMovieName = itemView.findViewById(R.id.tv_movie_name);
            tvMovieDesc = itemView.findViewById(R.id.tv_movie_desc);
            ivImagePoster = itemView.findViewById(R.id.iv_image_poster);

            itemsLayout = itemView.findViewById(R.id.rl_items);
        }
    }

    public void setData(ArrayList<MovieItems> items){
        mData = items;
        notifyDataSetChanged();
    }

    public ArrayList<MovieItems> getListMovie(){
        return mData;
    }
}
