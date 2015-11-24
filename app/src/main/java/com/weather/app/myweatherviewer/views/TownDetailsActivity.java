package com.weather.app.myweatherviewer.views;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.weather.app.myweatherviewer.adapters.MyPagerAdapter;
import com.weather.app.myweatherviewer.R;
import com.weather.app.myweatherviewer.async.WeatherAccessDataAsync;
import com.weather.app.myweatherviewer.fragments.TownDetailsFragment;
import com.weather.app.myweatherviewer.receivers.RefreshAllReceiver;
import com.weather.app.myweatherviewer.receivers.RefreshIconReceiver;

import java.util.ArrayList;
import java.util.List;

public class TownDetailsActivity extends AppCompatActivity {

    private ArrayList<String> tab;
    private int pos;
    private RefreshAllReceiver receiverData;
    private RefreshIconReceiver receiverIcon;
    private IntentFilter filtreData;
    private IntentFilter filtreIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedInstanceState = getIntent().getExtras().getBundle("bundle");
        this.tab = savedInstanceState.getStringArrayList("list");
        this.pos = savedInstanceState.getInt("position");
        setContentView(R.layout.activity_town_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


        refresh();
        List<TownDetailsFragment> list = new ArrayList<TownDetailsFragment>();
        for (int i = 0; i < this.tab.size(); i++) {
            list.add(TownDetailsFragment.newInstance(this.tab.get(i)));
        }

        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), list);
        this.receiverData = new RefreshAllReceiver(pagerAdapter,this.tab);
        this.receiverIcon = new RefreshIconReceiver(pagerAdapter);
        this.filtreData = new IntentFilter("com.intent.action.ALLRECEIVE");
        this.filtreIcon = new IntentFilter("com.intent.action.ICON_UPDATE");

        this.registerReceiver(receiverData, filtreData);
        this.registerReceiver(receiverIcon, filtreIcon);

        refresh();
        ViewPager view = (ViewPager) findViewById(R.id.viewpager);
        view.setAdapter(pagerAdapter);
        view.setCurrentItem(pos);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
        } else if (id == R.id.action_refresh) {
            refresh();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refresh() {
        ConnectivityManager cM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo infos = cM.getActiveNetworkInfo();
        if(infos != null && infos.isAvailable() && infos.isConnected()) {
            for (int i = 0; i < tab.size(); i++) {
                new WeatherAccessDataAsync(this).execute("DAYS", this.tab.get(i), "com.intent.action.ALLRECEIVE");
                new WeatherAccessDataAsync(this).execute("ACTU", this.tab.get(i), "com.intent.action.ALLRECEIVE");
                new WeatherAccessDataAsync(this).execute("DETAILS", this.tab.get(i), "com.intent.action.ALLRECEIVE");
            }
        }else{
            Intent intent;
            for (int i=0;i<this.tab.size();i++){
                intent = new Intent("com.intent.action.ALLRECEIVE");
                intent.putExtra("town", this.tab.get(i));
                intent.putExtra("type","DAYS");
                sendBroadcast(intent);
                intent.putExtra("type", "ACTU");
                sendBroadcast(intent);
                intent.putExtra("type","DETAILS");
                sendBroadcast(intent);
            }
            Toast.makeText(this,"Si vous souhaitez actualiser les données, veuillez vous connecter a un reseau internet.",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_town_details_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(this.receiverIcon);
        unregisterReceiver(this.receiverData);
    }

    @Override
    protected void onResume() {
        registerReceiver(this.receiverData,this.filtreData);
        registerReceiver(this.receiverIcon,this.filtreIcon);
        super.onResume();
    }
}
