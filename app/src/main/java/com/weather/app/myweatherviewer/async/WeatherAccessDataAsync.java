package com.weather.app.myweatherviewer.async;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jordan.tuffery on 29/10/2015.
 */


public class WeatherAccessDataAsync extends AsyncTask<String, Void, Boolean> {

    private WeakReference<Context> weakRef = null;
    private URL api = null;
    private String days = "http://api.openweathermap.org/data/2.5/forecast/daily?q=";
    private String days2 = "&mode=json&units=metric&cnt=5&appid=";
    private String details = "http://api.openweathermap.org/data/2.5/forecast?q=";
    private String details2 = "&cnt=8&mode=json&appid=";
    private String apiKey = "6389fdc9f5e7bc1748b9c059ec9bd7c2";
    private String actu = "http://api.openweathermap.org/data/2.5/weather?q=";
    private String actu2 = "&appid=";
    private String str = "com.intent.action.REFRESH";
    private String temp = null;
    private StringBuilder builder = null;
    private String choix = null;
    private String name = null;
    private String type = null;

    public WeatherAccessDataAsync(Context context) {
        super();
        weakRef = new WeakReference<>(context);
    }

    public static String callAPIString(String urlApi) {
        URL url;
        HttpURLConnection urlConnection = null;
        InputStream in = null;
        try {
            url = new URL(urlApi);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = urlConnection.getInputStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder(in.available());
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
            return total.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            closeStream(in);
            if (urlConnection != null) {
                try {
                    urlConnection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected Boolean doInBackground(String... params) {
        this.name = params[1];
        this.str = params[2];
        this.type= params[0];
        if (params[0].equals("DAYS")) {
            choix = days + params[1].toUpperCase() + days2 + apiKey;
        } else if (params[0].equals("DETAILS")) {
            choix = details + params[1].toUpperCase() + details2 + apiKey;
        } else if (params[0].equals("ACTU")) {
            choix = actu + params[1].toUpperCase() + actu2 + apiKey;
        } else {
            return false;
        }

        // Permet de stocker le fichier JSON dans un StringBuilder



        //Permet d'écrire le StringBuilder dans un fichier
        String data = callAPIString(choix);
        try {
            if (choix != null) {
                String file = null;
                if (params[0].equals("DAYS")) {
                    file = "days" + params[1].toUpperCase() + "Data.json";
                } else if (params[0].equals("DETAILS")) {
                    file = "details" + params[1].toUpperCase() + "Data.json";
                } else if (params[0].equals("ACTU")) {
                    file = "actu" + params[1].toUpperCase() + "Data.json";
                }
                FileOutputStream newfile = null;
                newfile = weakRef.get().openFileOutput(file, weakRef.get().MODE_PRIVATE);
                if (newfile != null)
                    newfile.write(data.getBytes());
                    newfile.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean bool){
        if(bool){
            Intent i = new Intent(str);
            i.putExtra("town",this.name);
            i.putExtra("type",this.type);
            this.weakRef.get().sendBroadcast(i);
        }
    }
}
