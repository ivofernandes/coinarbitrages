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


    private static final String API_KRAKEN = "https://api.kraken.com/0/public/Ticker?pair=";
    private static final String API_GDAX = "https://api.gdax.com/products/<p>/trades";
    private static final String API_BITSTAMP = "https://www.bitstamp.net/api/v2/ticker/";

    public enum RequestSource{
        // GDAX
        GDAX_BTC(API_GDAX.replace("<p>","BTC-EUR")),
        GDAX_ETH(API_GDAX.replace("<p>","ETH-EUR")),
        GDAX_LTC(API_GDAX.replace("<p>","LTC-EUR")),

        // Kraken
        Kraken_BTC(API_KRAKEN + "BTCEUR"),
        Kraken_ETH(API_KRAKEN + "ETHEUR"),
        Kraken_LTC(API_KRAKEN + "LTCEUR"),
        Kraken_XRP(API_KRAKEN + "XRPEUR"),

        // Bitstamp
        Bistamp_ETH(API_BITSTAMP + "etheur"),
        Bistamp_BTC(API_BITSTAMP + "btceur"),
        Bistamp_XRP(API_BITSTAMP + "xrpeur");

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

    private Map<DataManager.RequestSource,String> responses = new HashMap<>();

    // Actions
    public void requestAllData(RequestType requestType) {
        responses.clear();

        exchangeDataRequest.requestAllExchangeData(requestType);
    }

    public void response(RequestSource source, String response, RequestType requestType) throws JSONException {
        responses.put(source, response);

        processRequests(requestType);
    }

    // Responses


    private void processRequests(RequestType requestType) throws JSONException {
        if(responses.size() < calculateResponses()){
            return;
        }

        JSONArray gdax_ETH = new JSONArray(responses.get(RequestSource.GDAX_ETH));
        JSONArray gdax_BTC = new JSONArray(responses.get(RequestSource.GDAX_BTC));
        JSONArray gdax_LTC = new JSONArray(responses.get(RequestSource.GDAX_LTC));

        JSONObject kraken_ETH = new JSONObject(responses.get(RequestSource.Kraken_ETH));
        JSONObject kraken_BTC = new JSONObject(responses.get(RequestSource.Kraken_BTC));
        JSONObject kraken_LTC = new JSONObject(responses.get(RequestSource.Kraken_LTC));
        JSONObject kraken_XRP = new JSONObject(responses.get(RequestSource.Kraken_XRP));

        JSONObject bistamp_BTC = new JSONObject(responses.get(RequestSource.Bistamp_BTC));
        JSONObject bistamp_ETH = new JSONObject(responses.get(RequestSource.Bistamp_ETH));
        JSONObject bistamp_XRP = new JSONObject(responses.get(RequestSource.Bistamp_XRP));

        // If got all the data go ahead
        boolean ethComplete = (gdax_ETH != null && kraken_ETH != null);
        boolean btcComplete = (gdax_BTC != null && kraken_BTC != null);
        boolean ltcComplete = (gdax_LTC != null && kraken_LTC != null);

        if(ethComplete && btcComplete && ltcComplete){
            ExchangeDataProcessor processor = new ExchangeDataProcessor();
            processor.loadGdax(gdax_BTC,gdax_ETH,gdax_LTC);
            processor.loadKraken(kraken_BTC, kraken_ETH, kraken_LTC, kraken_XRP);
            processor.loadBitstamp(bistamp_BTC, bistamp_ETH, bistamp_XRP);
            processor.init();

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

    private int calculateResponses() {
        return 10;
    }

}