package com.coinarbritages.coinarbritages.manager;

import com.coinarbritages.coinarbritages.MainActivity;
import com.coinarbritages.coinarbritages.OpenWeatherMap.ExchangeDataRequest;
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

    // Enums
    public enum RequestType {
        UPDATE_VIEWS, // Request called by the user, refresh all views
        NOTIFICATION, // Request to send a notifications
    };

    public enum RequestSource{
        GDAX("https://api.gdax.com/products/ETH-EUR/trades"),
        Kraken("https://api.kraken.com/0/public/Ticker?pair=ETHEUR");

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

    private JSONArray gdax = null;
    private JSONObject kraken = null;


    // Actions
    public void requestAllData(RequestType requestType) {
        exchangeDataRequest.requestAllExchangeData(requestType);
    }

    // Responses


    public void responseGDAX(String response, JSONArray json, RequestSource requestSource, RequestType requestType, Date lastUpdateDate) throws JSONException {
        this.gdax = json;

        processRequests(requestType,requestSource);
    }



    public void responseKraken(String response, JSONObject json, RequestSource requestSource, RequestType requestType, Date lastUpdateDate) throws JSONException {
        this.kraken = json;

        processRequests(requestType,requestSource);
    }

    private void processRequests(RequestType requestType, RequestSource requestSource) throws JSONException {
        // If got all the data go ahead

        if(this.gdax != null && this.kraken != null){
            ExchangeDataProcessor processor = new ExchangeDataProcessor(this.gdax, this.kraken);

            if(RequestType.UPDATE_VIEWS.equals(requestType)) {
                MainActivity.getInstance().showData(processor);
            }else{
                // Update the wake up control
                NotificationSchedulingService.getInstance().updated(processor);
            }
        }
    }

}