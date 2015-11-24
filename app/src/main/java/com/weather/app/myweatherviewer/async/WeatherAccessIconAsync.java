package com.weather.app.myweatherviewer.async;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by jordan.tuffery on 17/11/2015.
 */
public class WeatherAccessIconAsync extends AsyncTask<String,Void,Boolean>{

    private WeakReference<Context> weakRef;
    public WeatherAccessIconAsync(Context context) {
        weakRef = new WeakReference<>(context);

    }

    @Override
    protected Boolean doInBackground(String... params) {
        ByteArrayOutputStream output= null;
        try {
                System.setProperty("http.keepAlive","false");
                URL api = new URL("http://openweathermap.org/img/w/" + params[0] + ".png");
                URLConnection urlConnection = api.openConnection();
                HttpURLConnection httpAPIAccess = (HttpURLConnection) urlConnection;

                if (httpAPIAccess.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream is = httpAPIAccess.getInputStream();
                    int bytesRead;
                    byte[] buffer = new byte[1024];
                    output = new ByteArrayOutputStream();
                    while ((bytesRead = is.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesRead);
                    }
                }
                httpAPIAccess.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }

        //Permet d'écrire le StringBuilder dans un fichier

        try {
            if(output != null) {
                String file = params[0] + ".png";
                FileOutputStream newfile = null;
                newfile = weakRef.get().openFileOutput(file, weakRef.get().MODE_PRIVATE);
                newfile.write(output.toByteArray());
                if (newfile != null) {
                    newfile.close();
                }
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
            return false;
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean bool){
        if(bool){
            Intent i = new Intent();
            i.setAction("com.intent.action.ICON_UPDATE");
            weakRef.get().sendBroadcast(i);
        }
    }
}
