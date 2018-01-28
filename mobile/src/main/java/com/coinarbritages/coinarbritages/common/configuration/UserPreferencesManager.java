package com.coinarbritages.coinarbritages.common.configuration;

/**
 * Created by ivofernandes on 27/07/15.
 */
public class UserPreferencesManager {

    // Singleton
    private static UserPreferencesManager instance = new UserPreferencesManager();

    private UserPreferencesManager(){}

    public static UserPreferencesManager getInstance(){
        return instance;
    }

    // Fields
    private boolean isDirectTrade = true;
    private boolean notification = true;

    /**
     * Load data from local cache
     */
    public void init() {
        this.notification = CacheManager.getInstance().getBoolean(
                CacheManager.PREFERENCE_SETTINGS_NOTIFICATION, true);

        this.isDirectTrade = CacheManager.getInstance().getBoolean(
                CacheManager.PREFERENCE_SETTINGS_DIRECT_TRADE, true);


    }

    public boolean isDirectTrade() {
        return isDirectTrade;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setDirectTrade(boolean directTrade) {
        this.isDirectTrade = directTrade;

        CacheManager.getInstance().putBoolean(CacheManager.PREFERENCE_SETTINGS_DIRECT_TRADE,
                this.isDirectTrade);
    }

    public void setNotification(boolean notification) {
        this.notification = notification;

        CacheManager.getInstance().putBoolean(CacheManager.PREFERENCE_SETTINGS_NOTIFICATION,
                this.notification);
    }

}
