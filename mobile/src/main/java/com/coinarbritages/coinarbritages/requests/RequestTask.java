package com.coinarbritages.coinarbritages.requests;

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

    private final DataManager.RequestSource requestSource;
    private final ExchangeDataRequest exchangeRequest;
    private final DataManager.RequestType weatherRequestType;

    private String urlString = null;

    public RequestTask(DataManager.RequestSource requestSource, ExchangeDataRequest openWeatherRequest,
                       DataManager.RequestType weatherRequestType) {
        this.requestSource = requestSource;
        this.exchangeRequest = openWeatherRequest;
        this.weatherRequestType = weatherRequestType;
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
            exchangeRequest.response(result, requestSource, weatherRequestType, new Date());
        }catch(Exception e){
            Log.e(TAG, "Error processing request " + requestSource + " > " + weatherRequestType, e);
        }
    }

}
