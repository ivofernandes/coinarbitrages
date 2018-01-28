package com.coinarbritages.coinarbritages.manager;

import com.coinarbritages.coinarbritages.MainActivity;
import com.coinarbritages.coinarbritages.OpenWeatherMap.ExchangeDataRequest;
import com.coinarbritages.coinarbritages.common.SharedResources;
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

    public void responseGdax(String response, JSONArray json, String requestSource, RequestType requestType, Date lastUpdateDate) throws JSONException {
        this.gdax = json;

        processRequests(requestType,requestSource);
    }

    // Enums
    public enum RequestType {
        UPDATE_VIEWS, // Request called by the user, refresh all views
        NOTIFICATION, // Request to send a notifications
    };

    // Constants
    private final SharedResources sharedResources = SharedResources.getInstance();
    private final ExchangeDataRequest openWeatherRequest = ExchangeDataRequest.getInstance();

    // Fields
    private Map<String,String> options = new HashMap<String,String>();


    private JSONObject jsonDaily = null;

    private JSONArray gdax = null;

    private boolean step3h = false;
    private boolean stepDaily = false;
    private Date lastUpdate = null;


    // Actions
    public void requestAllData(RequestType weatherRequestType) {

        // Reset vars
        jsonDaily = null;
        step3h = false;
        stepDaily = false;

        openWeatherRequest.requestAllExchangeData(options, weatherRequestType);
    }

    private void processRequests(RequestType requestType, String requestSource) throws JSONException {

        // If got all the data go ahead
        if(this.gdax != null){
            // Process the current request

            MainActivity.getInstance().showData(this.gdax);
        }

        // Update the wake up control
        NotificationSchedulingService.getInstance().updated(requestSource);

    }

    /**
     * Verify if the current date plus the minutes is before the date passed by param
     * @param dateParam
     * @param minutes
     * @return
     */
    public static boolean insideDateThreshold(Date dateParam, int minutes) {
        Date date = new Date();

        int second = 1000;
        int minute = 60 * second;

        long threshold = minute * minutes;
        date.setTime(date.getTime() - threshold);

        boolean result = date.before(dateParam);
        return result;
    }

    /**
     * Verify if the current date plus the minutes is before the date passed by param
     * @param date1
     * @param minutes
     * @return
     */
    public static boolean insideDateThreshold(Date date1, Date date2,int minutes) {

        int second = 1000;
        int minute = 60 * second;

        long threshold = minute * minutes;
        date2.setTime(date2.getTime() - threshold);

        boolean result = date2.before(date1);
        return result;
    }

}