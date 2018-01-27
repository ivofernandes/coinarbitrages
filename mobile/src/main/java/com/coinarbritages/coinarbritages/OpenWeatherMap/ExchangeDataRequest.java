package com.coinarbritages.coinarbritages.OpenWeatherMap;

import android.content.SharedPreferences;
import android.widget.Toast;

import com.coinarbritages.coinarbritages.MainActivity;
import com.coinarbritages.coinarbritages.common.Log;

import com.coinarbritages.coinarbritages.common.SharedResources;
import com.coinarbritages.coinarbritages.manager.DataManager;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
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
    public void requestALlExchangeData( Map<String,String> options,
                                       DataManager.WeatherRequestType weatherRequestType){

        // Request GDAX
        Log.i(TAG, "Request GDAX data");
        request(REQUEST_GDAX, false, weatherRequestType);

    }


    /**
     * Request data for 3 hours interval
     */
    private void request(String requestDataType, boolean retry,
                         DataManager.WeatherRequestType weatherRequestType) {
        // If have internet access
        if(sharedResources.haveInternetAccess()){
            makeRequest(requestDataType, weatherRequestType);
        }

    }



    private void makeRequest(String requestType, DataManager.WeatherRequestType weatherRequestType){

        // https://api.gdax.com/products/ETH-EUR/trades
        // https://api.kraken.com/0/public/Ticker?pair=ETHEUR
        if(requestType != null) {
            String url = "https://api.gdax.com/products/ETH-EUR/trades";

            RequestTask requestTask = new RequestTask(requestType, this,weatherRequestType);

            requestTask.execute(url);

       }
    }

    public void response(String response, String requestType,
                         DataManager.WeatherRequestType weatherRequestType,
                         Date lastUpdateDate){

        Log.v(TAG, requestType + " weatherRequestType: " + weatherRequestType + " response: " + response);

        if(response == null){
            request(requestType, true, weatherRequestType); // retry
        }

        // 3 Hours requestALlExchangeData
        if(requestType.equals(ExchangeDataRequest.REQUEST_GDAX)
                || requestType.equals(ExchangeDataRequest.REQUEST_DATA_DAILY)
                || requestType.equals(ExchangeDataRequest.REQUEST_DATA_CURRENT)){
            try {
                JSONObject json = new JSONObject(response);
                if(validResponse(json,response)) {
                    DataManager.getInstance().response(response, json, requestType,
                            weatherRequestType, lastUpdateDate);
                }else{
                    Log.e(TAG, "Error getting data " + weatherRequestType + "for type " + requestType
                            + ", unexpected format");
                }
            } catch (Exception e) {
                Log.e(TAG, "Error getting data " + weatherRequestType + "for type " + requestType
                        + ", data: " + e.getMessage(),e);
            }
        }

        else{
            Log.e(TAG, "Unknown requestAllData type: " + requestType);
        }
    }

    private boolean validResponse(JSONObject response, String jsonString) {
        if(response == null){
            return false;
        }

        return true;
    }

}
