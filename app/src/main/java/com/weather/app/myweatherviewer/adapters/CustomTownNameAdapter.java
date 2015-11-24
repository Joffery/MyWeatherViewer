package com.weather.app.myweatherviewer.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.weather.app.myweatherviewer.dao.ControlWeatherAccess;
import com.weather.app.myweatherviewer.views.CustomTownNameView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by jordan.tuffery on 17/11/2015.
 */
public class CustomTownNameAdapter extends BaseAdapter {
    private WeakReference<Context> weakRef = null;
    private ArrayList<String> mModel = new ArrayList<String>();

    public CustomTownNameAdapter(Context context, ArrayList<String> al){
       weakRef = new WeakReference<>(context);
        mModel = al;
    }
    @Override
    public int getCount() {
        return mModel.size();
    }

    @Override
    public Object getItem(int position) {
        return mModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomTownNameView v = null;
        // Notre vue n'a pas encore été construite, nous le faisons
        if (convertView == null) {
            v = new CustomTownNameView(weakRef.get());
        } // Notre vue peut être récupérée, nous le faisons
        else {
            v = (CustomTownNameView) convertView;
        }
        ControlWeatherAccess controleur = new ControlWeatherAccess(mModel.get(position),weakRef.get());
        v.bind(mModel.get(position),controleur.getActuDraw(),controleur.getActuTemp());
        return v;
    }

    public void setListItems(ArrayList<String> str){
        this.mModel = str;
    }
}
