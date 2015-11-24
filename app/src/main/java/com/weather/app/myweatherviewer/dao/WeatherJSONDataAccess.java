package com.weather.app.myweatherviewer.dao;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;

/**
 * Created by jordan.tuffery on 29/10/2015.
 */
public class WeatherJSONDataAccess {

    private WeakReference<Context> weakRef = null;
    private JSONObject daily = null;
    private JSONObject details = null;
    private String name = null;


    public WeatherJSONDataAccess(Context context, String name){
        super();
        weakRef = new WeakReference<Context>(context);
        this.name=name;
    }

    //permet d'obtenir un JSONObjet du fichier daysData.json

    public JSONObject getDetailsJSON(){
        JSONObject file = null;
        try {
            return new JSONObject(fileToString("days"+this.name.toUpperCase()+"Data.json"));
        }catch(JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    //permet d'obtenir un JSONObjet du fichier detailsData.json

    public JSONObject getDailyJSON(){
        JSONObject file = null;
        try {
            String json = fileToString("details"+this.name.toUpperCase()+"Data.json");
            return new JSONObject(json);
        }catch(JSONException e){
            e.printStackTrace();
            return null;
        }
    }


    public JSONObject getActuJSON(){
        JSONObject file = null;
        try {
            return new JSONObject(fileToString("actu"+this.name.toUpperCase()+"Data.json"));
        }catch(JSONException e){
            e.printStackTrace();
            return null;
        }
    }


    //permet d'inserer le contenu d'un fichier dans une String

    public String fileToString(String name){
        StringBuilder builder = new StringBuilder();
        String str = null;
        try {
            BufferedReader buffer = new BufferedReader(new InputStreamReader(weakRef.get().openFileInput(name)));
            while ((str = buffer.readLine()) != null) {
                builder.append(str + "\n");
            }
            str = builder.toString();
        }catch(IOException e){
            e.printStackTrace();
        }
        return str;
    }

}
