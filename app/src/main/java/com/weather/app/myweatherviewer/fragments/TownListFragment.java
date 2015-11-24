package com.weather.app.myweatherviewer.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.weather.app.myweatherviewer.adapters.CustomTownNameAdapter;
import com.weather.app.myweatherviewer.views.CustomTownNameView;
import com.weather.app.myweatherviewer.R;
import com.weather.app.myweatherviewer.views.TownDetailsActivity;
import com.weather.app.myweatherviewer.async.WeatherAccessDataAsync;
import com.weather.app.myweatherviewer.receivers.RefreshActuReceiver;
import com.weather.app.myweatherviewer.receivers.RefreshIconReceiver;

import java.util.ArrayList;


public class TownListFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener{

    private ArrayList<String> data;
    private CustomTownNameAdapter aa;
    private String str = "com.intent.action.REFRESH";
    private ArrayList<String> currentList = new ArrayList<>();
    private ArrayList<String> totList = new ArrayList<>();
    private RefreshActuReceiver actuReceiver;
    private RefreshIconReceiver iconReceiver;
    private IntentFilter iconfiltre;
    private IntentFilter filtre;
    private SwipeRefreshLayout layout = null;

    public TownListFragment(){
        super();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_town_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.totList.add("Paris");
        this.totList.add("London");
        this.totList.add("Madrid");
        this.totList.add("Lyon");
        aa = new CustomTownNameAdapter(getContext(),this.currentList);
        actuReceiver = new RefreshActuReceiver(this.aa,this.totList);
        filtre = new IntentFilter(this.str);
        getContext().registerReceiver(actuReceiver,filtre);
        iconReceiver = new RefreshIconReceiver(this.aa);
        iconfiltre = new IntentFilter("com.intent.action.ICON_UPDATE");
        getContext().registerReceiver(iconReceiver, iconfiltre);
        this.layout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_container);
        this.layout.setOnRefreshListener(this);
        setListAdapter(aa);
        refresh(getContext());
        this.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomTownNameView v = (CustomTownNameView) view;
                Intent i = new Intent(getActivity().getApplicationContext(), TownDetailsActivity.class);
                Bundle save = new Bundle();
                save.putStringArrayList("list", TownListFragment.this.totList);
                save.putInt("position", position);
                i.putExtra("bundle", save);
                startActivity(i);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.town_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_refresh){
            refresh(getContext());
            return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void refresh(Context context){
        ConnectivityManager cM = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo infos = cM.getActiveNetworkInfo();
        if(infos != null && infos.isAvailable() && infos.isConnected()) {
            for (int i = 0; i < this.totList.size(); i++) {
                new WeatherAccessDataAsync(context).execute("ACTU", this.totList.get(i), "com.intent.action.REFRESH");
            }
        }else{
            Intent intent;
            for (int i=0;i<this.totList.size();i++){
                intent = new Intent("com.intent.action.REFRESH");
                intent.putExtra("town", this.totList.get(i));
                context.sendBroadcast(intent);
            }
            Toast.makeText(context, "Si vous souhaitez actualiser les données, veuillez vous connecter a un reseau internet.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        this.getContext().unregisterReceiver(actuReceiver);
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        this.layout.setRefreshing(true);
        refresh(getContext());
        this.layout.setRefreshing(false);
    }
}
