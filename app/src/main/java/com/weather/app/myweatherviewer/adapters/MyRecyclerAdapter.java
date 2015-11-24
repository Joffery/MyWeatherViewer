package com.weather.app.myweatherviewer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weather.app.myweatherviewer.R;
import com.weather.app.myweatherviewer.dao.ControlWeatherAccess;
import com.weather.app.myweatherviewer.dao.MyViewHolder;

/**
 * Created by jordan.tuffery on 20/11/2015.
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<MyViewHolder> {

    public final static int DAYS = 1;
    public final static int TIME = 2;
    private int state;
    private ControlWeatherAccess cwa;
    private String name;

    public MyRecyclerAdapter(String name,int state,Context context){
        this.name = name;
        this.state = state;
        this.cwa = new ControlWeatherAccess(this.name,context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_details_recycler,parent,false);
        MyViewHolder hld = new MyViewHolder(v);
        return hld;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(state == TIME) {
            if(this.cwa.getFlagDaily()) {
                holder.getDate().setText(this.cwa.getDailyStr(position));
                holder.getImg().setImageDrawable(this.cwa.getDailyDraw(position));
                holder.getCel().setText(this.cwa.getDailyTemp(position) + " C°");
            }
        }else{
            if(this.cwa.getFlagWeek()) {
                holder.getDate().setText(this.cwa.getWeeklyStr(position));
                holder.getImg().setImageDrawable(this.cwa.getWeeklyDraw(position));
                holder.getCel().setText(this.cwa.getWeeklyTemp(position) + " C°");
            }
        }
    }

    @Override
    public int getItemCount() {
        if(state == DAYS){
            return this.cwa.getWeeklyStrCpt();
        }else{
            return this.cwa.getDailyStrCpt();
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
