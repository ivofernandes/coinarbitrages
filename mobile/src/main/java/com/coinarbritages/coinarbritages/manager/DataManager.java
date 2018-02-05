package com.coinarbritages.coinarbritages.manager;

import com.coinarbritages.coinarbritages.MainActivity;
import com.coinarbritages.coinarbritages.OpenWeatherMap.ExchangeDataRequest;
import com.coinarbritages.coinarbritages.common.Log;
import com.coinarbritages.coinarbritages.scheduler.NotificationSchedulingService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ivo on 20-06-2015.
 *
 * Class that manage how the data is requested, received, and distributed to interface manager
 */
public class DataManager {

    // Singleton
    private static DataManager instance = new DataManager();

    private DataManager() {}

    public static DataManager getInstance() {
        return instance;
    }

    private static final String TAG = "DataManager";

    // Enums
    public enum RequestType {
        UPDATE_VIEWS, // Request called by the user, refresh all views
        NOTIFICATION, // Request to send a notifications
    };

    public enum RequestSource{
        GDAX_ETH("https://api.gdax.com/products/ETH-EUR/trades"),
        Kraken_ETH("https://api.kraken.com/0/public/Ticker?pair=ETHEUR"),


        GDAX_BTC("https://api.gdax.com/products/BTC-EUR/trades"),
        Kraken_BTC("https://api.kraken.com/0/public/Ticker?pair=XBTEUR"),

        GDAX_LTC("https://api.gdax.com/products/LTC-EUR/trades"),
        Kraken_LTC("https://api.kraken.com/0/public/Ticker?pair=LTCEUR");

        private String url;

        RequestSource(String url){
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }

    // Constants
    private final ExchangeDataRequest exchangeDataRequest = ExchangeDataRequest.getInstance();

    // Fields
    private Map<String,String> options = new HashMap<String,String>();

    private JSONArray gdax_ETH = null;
    private JSONObject kraken_ETH = null;

    private JSONArray gdax_BTC = null;
    private JSONObject kraken_BTC = null;

    private JSONArray gdax_LTC = null;
    private JSONObject kraken_LTC = null;


    // Actions
    public void requestAllData(RequestType requestType) {
        exchangeDataRequest.requestAllExchangeData(requestType);
    }

    // Responses


    public void responseGDAX_ETH(String response, JSONArray json, RequestSource requestSource, RequestType requestType, Date lastUpdateDate) throws JSONException {
        this.gdax_ETH = json;

        processRequests(requestType);
    }

    public void responseKraken_ETH(String response, JSONObject json, RequestSource requestSource, RequestType requestType, Date lastUpdateDate) throws JSONException {
        this.kraken_ETH = json;

        processRequests(requestType);
    }

    public void responseGDAX_BTC(String response, JSONArray json, RequestSource requestSource, RequestType requestType, Date lastUpdateDate) throws JSONException {
        this.gdax_BTC = json;

        processRequests(requestType);
    }

    public void responseKraken_BTC(String response, JSONObject json, RequestSource requestSource, RequestType requestType, Date lastUpdateDate) throws JSONException {
        this.kraken_BTC = json;

        processRequests(requestType);
    }

    public void responseGDAX_LTC(String response, JSONArray json, RequestSource requestSource, RequestType requestType, Date lastUpdateDate) throws JSONException {
        this.gdax_LTC = json;

        processRequests(requestType);
    }

    public void responseKraken_LTC(String response, JSONObject json, RequestSource requestSource, RequestType requestType, Date lastUpdateDate) throws JSONException {
        this.kraken_LTC = json;

        processRequests(requestType);
    }

    private void processRequests(RequestType requestType) throws JSONException {
        // If got all the data go ahead

        boolean ethComplete = (this.gdax_ETH != null && this.kraken_ETH != null);
        boolean btcComplete = (this.gdax_BTC != null && this.kraken_BTC != null);
        boolean ltcComplete = (this.gdax_LTC != null && this.kraken_LTC != null);

        if(ethComplete && btcComplete && ltcComplete){
            ExchangeDataProcessor processor = new ExchangeDataProcessor(
                    this.gdax_ETH, this.kraken_ETH,
                    this.gdax_BTC, this.kraken_BTC,
                    this.gdax_LTC, this.kraken_LTC);

            if(RequestType.UPDATE_VIEWS.equals(requestType)) {
                MainActivity.getInstance().showData(processor);
            }else{
                // Update the wake up control
                NotificationSchedulingService.getInstance().updated(processor);

                try{
                    MainActivity.getInstance().showData(processor);
                }catch (Exception e){
                    Log.w(TAG, "error updating data in main activity: " + e.getMessage());
                }
            }
        }
    }

}