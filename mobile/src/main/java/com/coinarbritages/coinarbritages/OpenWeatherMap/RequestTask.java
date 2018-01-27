package com.coinarbritages.coinarbritages.OpenWeatherMap;

import android.os.AsyncTask;
import com.coinarbritages.coinarbritages.common.Log;

import com.coinarbritages.coinarbritages.manager.DataManager;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * Created by Ivo on 20-06-2015.
 */
public class RequestTask extends AsyncTask<String, String, String> {

    private static final String TAG = "RequestTask";

    private final String requestType;
    private final OpenWeatherRequest openWeatherRequest;
    private final DataManager.WeatherRequestType weatherRequestType;
    private final double latitude;
    private final double longitude;

    private String urlString = null;

    public RequestTask(String requestType, OpenWeatherRequest openWeatherRequest,
                       DataManager.WeatherRequestType weatherRequestType,
                       double latitude, double longitude) {
        this.requestType = requestType;
        this.openWeatherRequest = openWeatherRequest;
        this.weatherRequestType = weatherRequestType;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    protected String doInBackground(String... uri) {
        try {
            urlString = uri[0];
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(20 * 1000);
            conn.setReadTimeout(20 * 1000);
            conn.setRequestMethod("GET");

            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            String response = IOUtils.toString(in, "UTF-8");
            return response;
        } catch (IOException e) {
            Log.e(TAG,"Error making request for " + urlString,e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            super.onPostExecute(result);

            Log.v(TAG, "received forecast: " + result);
            boolean dataFromCache = false;
            openWeatherRequest.response(result, requestType, weatherRequestType, dataFromCache, new Date(), latitude, longitude);
        }catch(Exception e){
            Log.e(TAG, "Error processing request " + requestType + " > " + weatherRequestType, e);
        }
    }

}
