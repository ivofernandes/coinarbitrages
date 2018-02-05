package com.coinarbritages.coinarbritages.OpenWeatherMap;

import com.coinarbritages.coinarbritages.common.Log;

import com.coinarbritages.coinarbritages.common.SharedResources;
import com.coinarbritages.coinarbritages.manager.DataManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Ivo on 20-06-2015.
 *
 * http://api.openweathermap.org/data/2.5/forecast?appid=0f3f0b0574edaf1267d5439472b77891&lat=38.714&lon=-9.14
 */
public class ExchangeDataRequest {

    //Singleton
    public static ExchangeDataRequest instance = new ExchangeDataRequest();

    public static ExchangeDataRequest getInstance(){
        return instance;
    }

    private ExchangeDataRequest(){}

    // Constants
    public static final String TAG = "ExchangeDataRequest";

    private SharedResources sharedResources = SharedResources.getInstance();

    /**
     * Make the requests to get the weather data
     */
    public void requestAllExchangeData(DataManager.RequestType requestType){

        // Request GDAX_ETH
        Log.i(TAG, "Request GDAX_ETH data");
        request(DataManager.RequestSource.GDAX_ETH,  requestType);

        Log.i(TAG, "Request kraken_ETH data");
        request(DataManager.RequestSource.Kraken_ETH, requestType);


        Log.i(TAG, "Request GDAX_BTC data");
        request(DataManager.RequestSource.GDAX_BTC,  requestType);

        Log.i(TAG, "Request kraken_BTC data");
        request(DataManager.RequestSource.Kraken_BTC, requestType);


        Log.i(TAG, "Request GDAX_LTC data");
        request(DataManager.RequestSource.GDAX_LTC,  requestType);

        Log.i(TAG, "Request kraken_LTC data");
        request(DataManager.RequestSource.Kraken_LTC, requestType);

    }

    private void request(DataManager.RequestSource requestSource,
                         DataManager.RequestType weatherRequestType) {
        // If have internet access
        if(sharedResources.haveInternetAccess()){
            makeRequest(requestSource, weatherRequestType);
        }
    }

    private void makeRequest(DataManager.RequestSource requestSource, DataManager.RequestType requestType){

        if(requestSource != null) {
            String url = requestSource.getUrl();

            RequestTask requestTask = new RequestTask(requestSource, this, requestType);

            requestTask.execute(url);

       }
    }

    public void response(String response, DataManager.RequestSource requestSource,
                         DataManager.RequestType requestType,
                         Date lastUpdateDate){

        Log.v(TAG, requestType + " request type: " + requestSource + " response: " + response);

        try {
            if(requestSource.equals(DataManager.RequestSource.GDAX_ETH)) {
                JSONArray json = new JSONArray(response);
                if (json != null) {
                    DataManager.getInstance().responseGDAX_ETH(response, json, requestSource,
                            requestType, lastUpdateDate);
                } else {
                    Log.e(TAG, "Error getting data " + requestType + "for type " + requestType
                            + ", unexpected format");
                }
            }else if(requestSource.equals(DataManager.RequestSource.Kraken_ETH)){
                JSONObject json = new JSONObject(response);
                if (json != null) {
                    DataManager.getInstance().responseKraken_ETH(response, json, requestSource,
                            requestType, lastUpdateDate);
                } else {
                    Log.e(TAG, "Error getting data " + requestType + "for type " + requestType
                            + ", unexpected format");
                }
            }
            // BTC
            else if(requestSource.equals(DataManager.RequestSource.GDAX_BTC)) {
                JSONArray json = new JSONArray(response);
                if (json != null) {
                    DataManager.getInstance().responseGDAX_BTC(response, json, requestSource,
                            requestType, lastUpdateDate);
                } else {
                    Log.e(TAG, "Error getting data " + requestType + "for type " + requestType
                            + ", unexpected format");
                }
            }else if(requestSource.equals(DataManager.RequestSource.Kraken_BTC)){
                JSONObject json = new JSONObject(response);
                if (json != null) {
                    DataManager.getInstance().responseKraken_BTC(response, json, requestSource,
                            requestType, lastUpdateDate);
                } else {
                    Log.e(TAG, "Error getting data " + requestType + "for type " + requestType
                            + ", unexpected format");
                }
            }
            // LTC
            else if(requestSource.equals(DataManager.RequestSource.GDAX_LTC)) {
                JSONArray json = new JSONArray(response);
                if (json != null) {
                    DataManager.getInstance().responseGDAX_LTC(response, json, requestSource,
                            requestType, lastUpdateDate);
                } else {
                    Log.e(TAG, "Error getting data " + requestType + "for type " + requestType
                            + ", unexpected format");
                }
            }else if(requestSource.equals(DataManager.RequestSource.Kraken_LTC)){
                JSONObject json = new JSONObject(response);
                if (json != null) {
                    DataManager.getInstance().responseKraken_LTC(response, json, requestSource,
                            requestType, lastUpdateDate);
                } else {
                    Log.e(TAG, "Error getting data " + requestType + "for type " + requestType
                            + ", unexpected format");
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting data " + requestType + "for type " + requestType
                    + ", data: " + e.getMessage(),e);
        }
    }



}
