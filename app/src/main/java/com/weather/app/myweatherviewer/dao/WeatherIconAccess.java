package com.weather.app.myweatherviewer.dao;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by jordan.tuffery on 18/11/2015.
 */

public class WeatherIconAccess {
    public static Drawable getDrawable(String nameIcon,Context context){
        try {
            Drawable draw = Drawable.createFromStream(context.openFileInput(nameIcon + ".png"), "yolo");
            return draw;
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
