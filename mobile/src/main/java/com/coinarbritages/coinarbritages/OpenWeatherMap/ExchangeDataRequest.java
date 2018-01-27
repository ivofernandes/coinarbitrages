package com.coinarbritages.coinarbritages.OpenWeatherMap;

import com.coinarbritages.coinarbritages.common.Log;

import com.coinarbritages.coinarbritages.common.SharedResources;
import com.coinarbritages.coinarbritages.manager.DataManager;

import org.json.JSONArray;

import java.util.Date;
import java.util.Map;

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
    public static final String REQUEST_GDAX = "GDAX";
    public static final String REQUEST_DATA_DAILY = "forecast/daily";
    public static final String REQUEST_DATA_CURRENT = "weather";


    private SharedResources sharedResources = SharedResources.getInstance();


    /**
     * Make the requests to get the weather data
     */
    public void requestAllExchangeData(Map<String,String> options,
                                       DataManager.RequestType weatherRequestType){

        // Request GDAX
        Log.i(TAG, "Request GDAX data");
        request(REQUEST_GDAX, false, weatherRequestType);

    }


    /**
     * Request data for 3 hours interval
     */
    private void request(String requestDataType, boolean retry,
                         DataManager.RequestType weatherRequestType) {
        // If have internet access
        if(sharedResources.haveInternetAccess()){
            makeRequest(requestDataType, weatherRequestType);
        }

    }



    private void makeRequest(String requestType, DataManager.RequestType weatherRequestType){

        // https://api.gdax.com/products/ETH-EUR/trades
        // https://api.kraken.com/0/public/Ticker?pair=ETHEUR
        if(requestType != null) {
            String url = "https://api.gdax.com/products/ETH-EUR/trades";

            RequestTask requestTask = new RequestTask(requestType, this,weatherRequestType);

            requestTask.execute(url);

       }
    }

    public void response(String response, String requestSource,
                         DataManager.RequestType requestType,
                         Date lastUpdateDate){

        Log.v(TAG, requestType + " request type: " + requestSource + " response: " + response);

        // 3 Hours requestAllExchangeData
        if(requestType.equals(ExchangeDataRequest.REQUEST_GDAX)){
            try {
                JSONArray json = new JSONArray(response);
                if(validResponse(json,response)) {
                    DataManager.getInstance().responseGdax(response, json, requestSource,
                            requestType, lastUpdateDate);
                }else{
                    Log.e(TAG, "Error getting data " + requestType + "for type " + requestType
                            + ", unexpected format");
                }
            } catch (Exception e) {
                Log.e(TAG, "Error getting data " + requestType + "for type " + requestType
                        + ", data: " + e.getMessage(),e);
            }
        }

        else{
            Log.e(TAG, "Unknown requestAllData type: " + requestType);
        }
    }

    private boolean validResponse(JSONArray response, String jsonString) {
        if(response == null){
            return false;
        }

        return true;
    }

}
