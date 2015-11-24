package com.weather.app.myweatherviewer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.weather.app.myweatherviewer.adapters.CustomTownNameAdapter;
import com.weather.app.myweatherviewer.async.WeatherAccessIconAsync;
import com.weather.app.myweatherviewer.dao.WeatherJSONDataAccess;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordan.tuffery on 23/11/2015.
 */
public class RefreshActuReceiver extends BroadcastReceiver {

    private List<String> tab = null;
    private CustomTownNameAdapter adapter;
    private ArrayList<String> str = new ArrayList<>();
    private ArrayList<String> totList;
    private int current = 0 ;

    public RefreshActuReceiver(CustomTownNameAdapter adapter, ArrayList<String> list){
        super();
        this.totList= list;
        this.adapter = adapter;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("com.intent.action.REFRESH")){
            ConnectivityManager cM = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo infos = cM.getActiveNetworkInfo();
            if(this.totList.size()==str.size()){
                str.clear();
                current = 0;
            }
            try {
                WeatherJSONDataAccess weatherJSONDataAccess = new WeatherJSONDataAccess(context,intent.getStringExtra("town"));
                JSONObject obj;
                WeatherAccessIconAsync async = null;
                if (new File(context.getFilesDir()+"/"+"actu"+intent.getStringExtra("town").toUpperCase()+"Data.json").exists()){
                    System.out.println("---------before");
                    obj = weatherJSONDataAccess.getActuJSON();
                    System.out.println("---------"+obj.toString());
                    if (!(new File(context.getFilesDir()+"/"+(obj.getJSONArray("weather").getJSONObject(0).getString("icon")) + ".png").exists())&&infos != null && infos.isAvailable() && infos.isConnected()){
                        async = new WeatherAccessIconAsync(context);
                        async.execute(obj.getJSONArray("weather").getJSONObject(0).getString("icon"));
                    }
                }
            }catch(JSONException e){
                e.printStackTrace();
            }
            str.add(intent.getStringExtra("town"));
            current++;
            adapter.setListItems(str);
            adapter.notifyDataSetChanged();
        }
    }

}
