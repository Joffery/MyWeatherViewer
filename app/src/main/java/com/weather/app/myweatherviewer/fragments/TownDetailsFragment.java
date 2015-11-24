package com.weather.app.myweatherviewer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.weather.app.myweatherviewer.dao.ControlWeatherAccess;
import com.weather.app.myweatherviewer.adapters.MyRecyclerAdapter;
import com.weather.app.myweatherviewer.R;

import java.util.Calendar;


public class TownDetailsFragment extends Fragment {
    private static final String ARG_PARAM1 = "name";
    private String name;

    public static TownDetailsFragment newInstance(String name) {
        TownDetailsFragment fragment = new TownDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, name);
        fragment.setArguments(args);
        return fragment;
    }

    public TownDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.name = getArguments().getString(ARG_PARAM1);
        return inflater.inflate(R.layout.fragment_details, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle save) {
        TextView town = (TextView) view.findViewById(R.id.townDetailsName);
        TextView cel = (TextView) view.findViewById(R.id.townDetailsTemp);
        TextView date = (TextView) view.findViewById(R.id.townDetailsDate);
        TextView hum = (TextView) view.findViewById(R.id.townDetailsHum);
        TextView atm = (TextView) view.findViewById(R.id.townDetailsAtm);
        ImageView img = (ImageView) view.findViewById(R.id.townDetailsWeather);
        town.setText(name);
        ControlWeatherAccess cwa = new ControlWeatherAccess(name, getContext());
        img.setImageDrawable(cwa.getActuDraw());
        cel.setText(cwa.getActuTemp() + " C°");
        String day = null;
        switch (cwa.getActuDate().get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                day = "Lundi";
                break;
            case Calendar.TUESDAY:
                day = "Mardi";
                break;
            case Calendar.WEDNESDAY:
                day = "Mercredi";
                break;
            case Calendar.THURSDAY:
                day = "Jeudi";
                break;
            case Calendar.FRIDAY:
                day = "Vendredi";
                break;
            case Calendar.SATURDAY:
                day = "Samedi";
                break;
            case Calendar.SUNDAY:
                day = "Dimanche";
                break;
        }
        String month = null;
        switch (cwa.getActuDate().get(Calendar.MONTH)) {
            case Calendar.JANUARY:
                month = "Janvier";
                break;
            case Calendar.FEBRUARY:
                month = "Fevrier";
                break;
            case Calendar.MARCH:
                month = "Mars";
                break;
            case Calendar.APRIL:
                month = "Avril";
                break;
            case Calendar.MAY:
                month = "Mai";
                break;
            case Calendar.JUNE:
                month = "Juin";
                break;
            case Calendar.JULY:
                month = "Juillet";
                break;
            case Calendar.AUGUST:
                month = "Aout";
                break;
            case Calendar.SEPTEMBER:
                month = "Septembre";
                break;
            case Calendar.OCTOBER:
                month = "Octobre";
                break;
            case Calendar.NOVEMBER:
                month = "Novembre";
                break;
            case Calendar.DECEMBER:
                month = "Decembre";
                break;
        }
        date.setText(day + " " + cwa.getActuDate().get(Calendar.DAY_OF_MONTH) + " " + month + " " + cwa.getActuDate().get(Calendar.YEAR));
        hum.setText("Humidity : " + cwa.getActuHumidity() + "%");
        atm.setText("Pression : " + cwa.getActuAtmo() + " Pa");

        if (cwa.getFlagWeek()) {
            RecyclerView rc1 = (RecyclerView) view.findViewById(R.id.townRecyclerDay);
            MyRecyclerAdapter ra1 = new MyRecyclerAdapter(this.name, MyRecyclerAdapter.DAYS, getContext());
            LinearLayoutManager llm1 = new LinearLayoutManager(getContext());
            llm1.setOrientation(LinearLayoutManager.HORIZONTAL);
            rc1.setLayoutManager(llm1);
            rc1.setAdapter(ra1);

        }
        if(cwa.getFlagDaily()){
            RecyclerView rc2 = (RecyclerView) view.findViewById(R.id.townRecyclerTime);
            MyRecyclerAdapter ra2 = new MyRecyclerAdapter(this.name, MyRecyclerAdapter.TIME, getContext());
            LinearLayoutManager llm2 = new LinearLayoutManager(getContext());
            llm2.setOrientation(LinearLayoutManager.HORIZONTAL);
            rc2.setLayoutManager(llm2);
            rc2.setAdapter(ra2);
        }
    }


}
