package com.weather.app.myweatherviewer.dao;

import android.content.Context;
import android.graphics.drawable.Drawable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jordan.tuffery on 17/11/2015.
 */
public class ControlWeatherAccess {

    private String name = null;
    private Calendar[] weeklyStr = new Calendar[5];
    private String[] weeklyWeather = new String[5];
    private int[] weeklyTemp = new int[5];
    private Drawable[] weeklyDraw = new Drawable[5];
    private Calendar[] dailyStr = new Calendar[8];
    private String[] dailyWeather = new String[8];
    private int[] dailyTemp = new int[8];
    private Drawable[] dailyDraw = new Drawable[8];
    private Drawable currentDraw = null;
    private int currentHumidity;
    private int currentCel;
    private Calendar currentDate = null;
    private double currentAtmo;
    private WeatherJSONDataAccess wAccess;
    private boolean flagWeek=false;
    private boolean flagDaily=false;
    private boolean flagActu=false;

    public ControlWeatherAccess(String name, Context context){
        this.name = name;
        this.wAccess = new WeatherJSONDataAccess(context,name);
        if(new File(context.getFilesDir()+"/"+"details"+name.toUpperCase()+"Data.json").exists()){
            initDaily(context);
            flagDaily = true;
        }
        if(new File(context.getFilesDir()+"/"+"days"+name.toUpperCase()+"Data.json").exists()){
            initWeekly(context);
            flagWeek = true;
        }
        if(new File(context.getFilesDir()+"/"+"actu"+name.toUpperCase()+"Data.json").exists()){
            initActu(context);
            flagActu = true;
        }
    }

    public void initDaily(Context context){
        JSONObject objDaily = wAccess.getDailyJSON();
        try {
            JSONArray list = objDaily.getJSONArray("list");
            JSONObject elem;
            JSONObject weather;
            for (int i = 0; i < list.length(); i++) {
                elem = list.getJSONObject(i);
                weather = elem.getJSONArray("weather").getJSONObject(0);
                this.dailyStr[i] = Calendar.getInstance();
                this.dailyStr[i].setTime(new Date((long) (elem.getInt("dt")) * 1000));
                this.dailyWeather[i] = weather.getString("main");
                this.dailyTemp[i] = ((Double) (elem.getJSONObject("main").getDouble("temp") - 273.15f)).intValue();
                this.dailyDraw[i] = WeatherIconAccess.getDrawable(weather.getString("icon"), context);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public void initActu(Context context){
        JSONObject objActu = wAccess.getActuJSON();

        try {

            this.currentDraw = WeatherIconAccess.getDrawable(objActu.getJSONArray("weather").getJSONObject(0).getString("icon"), context);
            this.currentAtmo = objActu.getJSONObject("main").getDouble("pressure");
            this.currentCel = ((Double)(objActu.getJSONObject("main").getDouble("temp") - 273.15f)).intValue();
            this.currentHumidity = objActu.getJSONObject("main").getInt("humidity");
            this.currentDate = Calendar.getInstance();
            this.currentDate.setTime(new Date((long)(objActu.getInt("dt"))*1000));

        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public void initWeekly(Context context){
        JSONObject objWeekly = wAccess.getDetailsJSON();
        try {
            JSONArray list = objWeekly.getJSONArray("list");
            JSONObject elem;
            JSONObject weather;
            for (int i = 0; i < list.length(); i++) {
                elem = list.getJSONObject(i);
                weather = elem.getJSONArray("weather").getJSONObject(0);
                this.weeklyStr[i] = Calendar.getInstance();
                this.weeklyStr[i].setTime(new Date((long) (elem.getInt("dt")) * 1000));
                this.weeklyWeather[i] = weather.getString("main");
                this.weeklyTemp[i] = ((Double) (elem.getJSONObject("temp").getDouble("day"))).intValue();
                this.weeklyDraw[i] = WeatherIconAccess.getDrawable(weather.getString("icon"), context);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public boolean getFlagWeek(){
        return this.flagWeek;
    }

    public boolean getFlagDaily(){
        return this.flagDaily;
    }

    public boolean getFlagActu(){
        return this.flagActu;
    }
    public String getWeeklyWeather(int i){
        return this.weeklyWeather[i];
    }

    public String getWeeklyStr(int i){
        String weekDays = null;
            switch(this.weeklyStr[i].get(Calendar.DAY_OF_WEEK)) {
                case Calendar.SUNDAY:
                    weekDays = "Dimanche";
                    break;
                case Calendar.MONDAY:
                    weekDays = "Lundi";
                    break;
                case Calendar.TUESDAY:
                    weekDays = "Mardi";
                    break;
                case Calendar.WEDNESDAY:
                    weekDays = "Mercredi";
                    break;
                case Calendar.THURSDAY:
                    weekDays = "Jeudi";
                    break;
                case Calendar.FRIDAY:
                    weekDays = "Vendredi";
                    break;
                case Calendar.SATURDAY:
                    weekDays = "Samedi";
                    break;
            }
        return weekDays;
    }

    public int getWeeklyTemp(int i){
        return this.weeklyTemp[i];
    }

    public Drawable getWeeklyDraw(int i){
        return this.weeklyDraw[i];
    }


    public String getDailyWeather(int i){
        return this.dailyWeather[i];
    }

    public String getDailyStr(int i){
        String weekDays = String.valueOf(this.dailyStr[i].get(Calendar.HOUR_OF_DAY)) + "h";
        return weekDays;
    }

    public int getDailyTemp(int i){
        return this.dailyTemp[i];
    }

    public Drawable getDailyDraw(int i){
        return this.dailyDraw[i];
    }

    public double getActuAtmo(){
        return this.currentAtmo;
    }

    public Calendar getActuDate(){
        return currentDate;
    }

    public int getActuTemp(){
        return this.currentCel;
    }

    public Drawable getActuDraw(){
        return this.currentDraw;
    }

    public int getActuHumidity(){
        return this.currentHumidity;
    }

    public int getWeeklyStrCpt(){
        return this.weeklyStr.length;
    }
    public int getDailyStrCpt(){
        return this.dailyStr.length;
    }
}
