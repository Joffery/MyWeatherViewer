package com.weather.app.myweatherviewer.dao;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.weather.app.myweatherviewer.R;

/**
 * Created by jordan.tuffery on 20/11/2015.
 */
public class MyViewHolder extends RecyclerView.ViewHolder {
    private TextView date;
    private ImageView img;
    private TextView cel;

    public MyViewHolder(View itemView) {
        super(itemView);
        date = (TextView) itemView.findViewById(R.id.townRecyclerDate);
        img = (ImageView) itemView.findViewById(R.id.townRecyclerWeather);
        cel = (TextView) itemView.findViewById(R.id.townRecyclerTemp);
    }

    public TextView getDate(){
        return this.date;
    }
    public ImageView getImg(){
        return this.img;
    }
    public TextView getCel(){
        return this.cel;
    }
}
