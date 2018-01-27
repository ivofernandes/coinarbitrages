package com.coinarbritages.coinarbritages.manager;

import android.os.Handler;

import com.coinarbritages.coinarbritages.OpenWeatherMap.ExchangeDataRequest;
import com.coinarbritages.coinarbritages.common.SharedResources;
import com.coinarbritages.coinarbritages.scheduler.NotificationSchedulingService;

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
    public enum WeatherRequestType {
        UPDATE_VIEWS, // Request called by the user, refresh all views
        NOTIFICATION, // Request to send a notifications
    };

    private enum RequestState{
        REQUESTING,
        DONE
    }

    // Constants
    private final ForecastInterfaceManager forecastInterfaceManager = ForecastInterfaceManager.getInstance();
    private final SharedResources sharedResources = SharedResources.getInstance();
    private final ExchangeDataRequest openWeatherRequest = ExchangeDataRequest.getInstance();
    private static final int REQUESTS_TIMEOUT = 60 * 1000;

    // Fields
    private Map<String,String> options = new HashMap<String,String>();
    private RequestState state = RequestState.DONE;
    private WeatherRequestType weatherRequestType;


    private JSONObject jsonCurrent = null;
    private JSONObject json3h = null;
    private JSONObject jsonDaily = null;
    private boolean step3h = false;
    private boolean stepDaily = false;
    private Date lastUpdate = null;
    private int processCurrentAnd3hCount = 0;
    private Handler customHandler  = new Handler();;

    // Actions
    public void requestAllData(WeatherRequestType weatherRequestType) {

        // Reset vars
        this.state = RequestState.REQUESTING;
        this.jsonCurrent = null;
        this.json3h = null;
        jsonDaily = null;
        step3h = false;
        stepDaily = false;
        this.processCurrentAnd3hCount = 0;

        if(weatherRequestType.equals(DataManager.WeatherRequestType.UPDATE_VIEWS)) {
            forecastInterfaceManager.reset();
        }

        // Set global params
        this.weatherRequestType = weatherRequestType;

        openWeatherRequest.requestALlExchangeData(options, weatherRequestType);
    }

    public void response(String response, JSONObject json, String requestType,
                         WeatherRequestType weatherRequestType,
                         Date lastUpdateDate) throws JSONException {

        this.lastUpdate = lastUpdateDate;

        // Store the jsons
        if(requestType.equals(ExchangeDataRequest.REQUEST_GDAX)) {
            this.json3h = json;
        } else if(requestType.equals(ExchangeDataRequest.REQUEST_DATA_DAILY)) {
            this.jsonDaily = json;
        } else if(requestType.equals(ExchangeDataRequest.REQUEST_DATA_CURRENT)) {
            this.jsonCurrent = json;
        }

        // Try process the requests
        processRequests(weatherRequestType);

        // Update the wake up control
        if(this.weatherRequestType.equals(DataManager.WeatherRequestType.NOTIFICATION)){
            NotificationSchedulingService.getInstance().updated(requestType);
        }
    }

    private void processRequests(WeatherRequestType weatherRequestType) throws JSONException {
        this.processCurrentAnd3hCount++;

        // If got all the data go ahead
        if(this.jsonCurrent != null && this.json3h != null){
            // Process the current request

            forecastInterfaceManager.receives3HourForecast(this.jsonCurrent,this.json3h, weatherRequestType, lastUpdate);

            this.step3h = true;

            if(this.jsonDaily != null) {
                processDaily(weatherRequestType);
            }
        }

        if(this.jsonDaily != null){
            if(step3h) {
                processDaily(weatherRequestType);
            }

            this.stepDaily = true;
        }

        if(step3h && stepDaily){
            this.state = RequestState.DONE;
        }

    }

    private void processDaily(WeatherRequestType weatherRequestType) throws JSONException {

        forecastInterfaceManager.receivesDailyForecast(this.jsonDaily, weatherRequestType);
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