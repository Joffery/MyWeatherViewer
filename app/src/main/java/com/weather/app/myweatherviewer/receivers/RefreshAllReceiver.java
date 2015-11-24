package com.weather.app.myweatherviewer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.weather.app.myweatherviewer.adapters.MyPagerAdapter;
import com.weather.app.myweatherviewer.async.WeatherAccessIconAsync;
import com.weather.app.myweatherviewer.dao.WeatherJSONDataAccess;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by jordan.tuffery on 23/11/2015.
 */
public class RefreshAllReceiver extends BroadcastReceiver {

    private MyPagerAdapter adapter;
    private ArrayList<String> list;
    private static String ACTION = "com.intent.action.ALLRECEIVE";

    public RefreshAllReceiver(MyPagerAdapter adapter, ArrayList<String> list){
        super();
        this.adapter = adapter;
        this.list = list;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(this.ACTION)) {
            ConnectivityManager cM = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo infos = cM.getActiveNetworkInfo();
            try {
                WeatherJSONDataAccess weatherJSONDataAccess = new WeatherJSONDataAccess(context, intent.getStringExtra("town"));
                JSONObject obj = null;
                WeatherAccessIconAsync async = null;
                if(intent.getStringExtra("type").equals("DETAILS")) {
                    if (new File(context.getFilesDir()+"/"+"details"+intent.getStringExtra("town").toUpperCase()+"Data.json").exists()) {
                        obj = weatherJSONDataAccess.getDailyJSON();
                        for (int x = 0; x < obj.getJSONArray("list").length(); x++) {
                            if (!(new File(context.getFilesDir() + "/" + (obj.getJSONArray("weather").getJSONObject(0).getString("icon")) + ".png").exists()) && infos != null && infos.isAvailable() && infos.isConnected()) {
                                async = new WeatherAccessIconAsync(context);
                                async.execute(obj.getJSONArray("list").getJSONObject(x).getJSONArray("weather").getJSONObject(0).getString("icon"));
                            }
                        }
                    }
                }
                if(intent.getStringExtra("type").equals("DAYS")) {
                    if (new File(context.getFilesDir()+"/"+"days"+intent.getStringExtra("town").toUpperCase()+"Data.json").exists()) {
                        obj = weatherJSONDataAccess.getActuJSON();
                        for (int x = 0; x < obj.getJSONArray("list").length(); x++) {
                            if (!(new File(context.getFilesDir() + "/" + (obj.getJSONArray("list").getJSONObject(x).getJSONArray("weather").getJSONObject(0).getString("icon")) + ".png").exists()) && infos != null && infos.isAvailable() && infos.isConnected()) {
                                async = new WeatherAccessIconAsync(context);
                                async.execute(obj.getJSONArray("list").getJSONObject(x).getJSONArray("weather").getJSONObject(0).getString("icon"));
                            }
                        }
                    }
                }

                if(intent.getStringExtra("type").equals("ACTU")) {
                    if (new File(context.getFilesDir()+"/"+"actu"+intent.getStringExtra("town").toUpperCase()+"Data.json").exists()) {
                        obj = weatherJSONDataAccess.getDetailsJSON();
                        if (!(new File(context.getFilesDir() + "/" + (obj.getJSONArray("weather").getJSONObject(0).getString("icon")) + ".png").exists()) && infos != null && infos.isAvailable() && infos.isConnected()) {
                            async = new WeatherAccessIconAsync(context);
                            async.execute(obj.getJSONArray("weather").getJSONObject(0).getString("icon"));
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter.notifyDataSetChanged();
        }
    }
}
