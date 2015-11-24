package com.weather.app.myweatherviewer.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weather.app.myweatherviewer.R;

/**
 * Created by jordan.tuffery on 30/10/2015.
 */
public class CustomTownNameView extends LinearLayout {
    private TextView mTownName;
    private TextView mCelsius;
    private ImageView mWeatherIcon;

    public CustomTownNameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomTownNameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTownNameView(Context context) {
        super(context);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_town_list, this);
        mTownName = (TextView) findViewById(R.id.townName);
        mWeatherIcon = (ImageView) findViewById(R.id.townWeather);
        mCelsius = (TextView) findViewById(R.id.townTemp);
    }

    public void bind(String city,Drawable icon, int temp) {
        mTownName.setText(city);
        mWeatherIcon.setImageDrawable(icon);
        mCelsius.setText(""+temp+" C°");
    }

    public String getName(){
        return this.mTownName.getText().toString();
    }
}
