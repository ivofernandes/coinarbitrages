package com.coinarbritages.coinarbritages.common.configuration;

import android.content.SharedPreferences;

import com.coinarbritages.coinarbritages.OpenWeatherMap.ExchangeDataRequest;
import com.coinarbritages.coinarbritages.common.Log;
import com.coinarbritages.coinarbritages.common.SharedResources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ivofernandes on 29/10/15.
 */
public class CacheManager {

    private static final String TAG = "CacheManager";

    // Singleton
    private static final CacheManager instance = new CacheManager();

    private CacheManager(){}

    public static CacheManager getInstance(){
        return instance;
    }

    // Cache Constants Settings

    public static final String PREFERENCE_SETTINGS_NOTIFICATION = "SETTINGS_NOTIFICATION";
    public static final String PREFERENCE_SETTINGS_DIRECT_TRADE = "SETTINGS_APPARENT_TEMPERATURE";
    public static final String PREFERENCE_SETTINGS_USE_GPS = "SETTINGS_USE_GPS";

    public static final String PREFERENCE_LOCATION_USE_GPS = "SETTINGS_NOTIFICATION";

    // Other constants
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    // Fields
    private SharedPreferences sharedPreferences;


    // Methods
    public SharedPreferences getSharedPreferences() {
        if(sharedPreferences == null){

            this.sharedPreferences = SharedResources.getInstance().getContext().getSharedPreferences(
                    SharedResources.class.getName(), SharedResources.getInstance().getContext().MODE_PRIVATE);

            Log.d(TAG, "cache manager setted with shared preferences: " + sharedPreferences);
        }

        return sharedPreferences;
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key, String defaultValue) {
        return getSharedPreferences().getString(key, defaultValue);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return getSharedPreferences().getBoolean(key, defaultValue);
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public Date getDate(String key,Date defaultValue){
        String dateString = getString(key, DATE_FORMAT.format(defaultValue));

        if(dateString != null){
            try {
                Date date = DATE_FORMAT.parse(dateString);
                return date;
            } catch (ParseException e) {
                Log.e(TAG,"Error parsing the date " + dateString + " from cache key " + key);
            }
        }

        return new Date();
    }

    public void putDate(String key, Date date){
        if(date != null){
            String dateString = DATE_FORMAT.format(date);
            putString(key,dateString);
        }
    }
}
