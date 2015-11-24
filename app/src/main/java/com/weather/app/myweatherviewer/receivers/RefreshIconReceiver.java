package com.weather.app.myweatherviewer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.weather.app.myweatherviewer.adapters.CustomTownNameAdapter;
import com.weather.app.myweatherviewer.adapters.MyPagerAdapter;

/**
 * Created by jordan.tuffery on 23/11/2015.
 */
public class RefreshIconReceiver extends BroadcastReceiver {

    private CustomTownNameAdapter adapter = null;
    private MyPagerAdapter mpa = null;
    private static int cpt = 0;

    public RefreshIconReceiver(CustomTownNameAdapter adapter){
        super();
        this.adapter = adapter;
    }

    public RefreshIconReceiver(MyPagerAdapter mpa){
        super();
        this.mpa = mpa;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("com.intent.action.ICON_UPDATE")){
            if(adapter!= null)
                adapter.notifyDataSetChanged();
            else if(mpa != null) {
                mpa.notifyDataSetChanged();
                cpt++;
            }
        }
    }
}
