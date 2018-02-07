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
    private boolean isEthBuyKrakenSellGdax = true;
    private boolean isEthBuyGdaxSellKraken = false;
    private boolean isBtcBuyKrakenSellGdax = false;
    private boolean isBtcBuyGdaxSellKraken = false;
    private boolean isLtcBuyKrakenSellGdax = false;
    private boolean isLtcBuyGdaxSellKraken = false;

    private boolean notification = true;

    /**
     * Load data from local cache
     */
    public void init() {
        this.notification = CacheManager.getInstance().getBoolean(
                CacheManager.PREFERENCE_SETTINGS_NOTIFICATION, true);

        this.isEthBuyKrakenSellGdax = CacheManager.getInstance().getBoolean(
                CacheManager.PREFERENCE_SETTINGS_ETH_BUY_KRAKEN_SELL_GDAX, true);
        this.isEthBuyGdaxSellKraken = CacheManager.getInstance().getBoolean(
                CacheManager.PREFERENCE_SETTINGS_ETH_BUY_GDAX_SELL_KRAKEN, true);

        this.isBtcBuyKrakenSellGdax = CacheManager.getInstance().getBoolean(
                CacheManager.PREFERENCE_SETTINGS_BTC_BUY_KRAKEN_SELL_GDAX, true);
        this.isBtcBuyGdaxSellKraken = CacheManager.getInstance().getBoolean(
                CacheManager.PREFERENCE_SETTINGS_BTC_BUY_GDAX_SELL_KRAKEN, true);

        this.isLtcBuyKrakenSellGdax = CacheManager.getInstance().getBoolean(
                CacheManager.PREFERENCE_SETTINGS_LTC_BUY_KRAKEN_SELL_GDAX, true);
        this.isLtcBuyGdaxSellKraken = CacheManager.getInstance().getBoolean(
                CacheManager.PREFERENCE_SETTINGS_LTC_BUY_GDAX_SELL_KRAKEN, true);


    }

    public boolean isEthBuyKrakenSellGdax() {
        return isEthBuyKrakenSellGdax;
    }

    public boolean isEthBuyGdaxSellKraken() {
        return isEthBuyGdaxSellKraken;
    }

    public boolean isBtcBuyKrakenSellGdax() {
        return isBtcBuyKrakenSellGdax;
    }

    public boolean isBtcBuyGdaxSellKraken() {
        return isBtcBuyGdaxSellKraken;
    }

    public boolean isLtcBuyKrakenSellGdax() {
        return isLtcBuyKrakenSellGdax;
    }

    public boolean isLtcBuyGdaxSellKraken() {
        return isLtcBuyGdaxSellKraken;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setEthBuyKrakenSellGdax(boolean ethBuyKrakenSellGdax) {
        this.isEthBuyKrakenSellGdax = ethBuyKrakenSellGdax;

        CacheManager.getInstance().putBoolean(CacheManager.PREFERENCE_SETTINGS_ETH_BUY_KRAKEN_SELL_GDAX,
                this.isEthBuyKrakenSellGdax);
    }

    public void setEthBuyGdaxSellKraken(boolean ethBuyGdaxSellKraken) {
        isEthBuyGdaxSellKraken = ethBuyGdaxSellKraken;

        CacheManager.getInstance().putBoolean(CacheManager.PREFERENCE_SETTINGS_ETH_BUY_GDAX_SELL_KRAKEN,
                this.isEthBuyGdaxSellKraken);
    }

    public void setBtcBuyKrakenSellGdax(boolean btcBuyKrakenSellGdax) {
        isBtcBuyKrakenSellGdax = btcBuyKrakenSellGdax;


        CacheManager.getInstance().putBoolean(CacheManager.PREFERENCE_SETTINGS_BTC_BUY_KRAKEN_SELL_GDAX,
                this.isBtcBuyKrakenSellGdax);
    }

    public void setBtcBuyGdaxSellKraken(boolean btcBuyGdaxSellKraken) {
        isBtcBuyGdaxSellKraken = btcBuyGdaxSellKraken;

        CacheManager.getInstance().putBoolean(CacheManager.PREFERENCE_SETTINGS_BTC_BUY_GDAX_SELL_KRAKEN,
                this.isBtcBuyGdaxSellKraken);
    }

    public void setLtcBuyKrakenSellGdax(boolean ltcBuyKrakenSellGdax) {
        isLtcBuyKrakenSellGdax = ltcBuyKrakenSellGdax;

        CacheManager.getInstance().putBoolean(CacheManager.PREFERENCE_SETTINGS_LTC_BUY_KRAKEN_SELL_GDAX,
                this.isLtcBuyKrakenSellGdax);
    }

    public void setLtcBuyGdaxSellKraken(boolean ltcBuyGdaxSellKraken) {
        isLtcBuyGdaxSellKraken = ltcBuyGdaxSellKraken;

        CacheManager.getInstance().putBoolean(CacheManager.PREFERENCE_SETTINGS_LTC_BUY_GDAX_SELL_KRAKEN,
                this.isLtcBuyGdaxSellKraken);
    }

    public void setNotification(boolean notification) {
        this.notification = notification;

        CacheManager.getInstance().putBoolean(CacheManager.PREFERENCE_SETTINGS_NOTIFICATION,
                this.notification);
    }

    public double getMinDelta() {
        return 0.65;
    }
}
