package com.coinarbritages.coinarbritages.manager;

import com.coinarbritages.coinarbritages.common.Log;
import com.coinarbritages.coinarbritages.common.configuration.UserPreferencesManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ivofernandes on 28/01/2018.
 */

public class ExchangeDataProcessor {

    private final static String TAG = "ExchangeDataProcessor";

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final JSONObject krakenJsonEth;
    private final JSONArray gdaxJsonEth;

    private final JSONArray gdaxJsonBtc;
    private final JSONObject krakenJsonBtc;

    private final JSONArray gdaxJsonLtc;
    private final JSONObject krakenJsonLtc;

    private double ethDelta;
    private Double ethGdaxPrice = null;
    private Double ethKrakenPrice = null;

    private double btcDelta;
    private Double btcGdaxPrice = null;
    private Double btcKrakenPrice = null;

    private double ltcDelta;
    private Double ltcGdaxPrice = null;
    private Double ltcKrakenPrice = null;

    public ExchangeDataProcessor(JSONArray gdax_ETH, JSONObject kraken_ETH,
                                 JSONArray gdax_BTC, JSONObject kraken_BTC,
                                 JSONArray gdax_LTC, JSONObject kraken_LTC) {

        this.gdaxJsonEth = gdax_ETH;
        this.krakenJsonEth = kraken_ETH;

        this.gdaxJsonBtc = gdax_BTC;
        this.krakenJsonBtc = kraken_BTC;

        this.gdaxJsonLtc = gdax_LTC;
        this.krakenJsonLtc = kraken_LTC;

        init();
    }

    /**
     * Process the exchange data
     */
    private void init() {

        try {
            initArbitrage();
        } catch (JSONException e) {
            Log.e(TAG,"Error processing the arbitrage: " + e.getMessage(),e);
        }

    }

    private void initArbitrage() throws JSONException {
        initEth();

        initBtc();

        initLtc();
    }

    private void initEth() throws JSONException {
        // GDAX_ETH
        JSONObject gdaxPricesJson = gdaxJsonEth.getJSONObject(0);

        ethGdaxPrice = Double.parseDouble(gdaxPricesJson.get("price").toString());

        // KRAKEN_ETH
        JSONObject krakenPricesJson = krakenJsonEth.getJSONObject("result").getJSONObject("XETHZEUR");
        JSONArray krakenAsk = krakenPricesJson.getJSONArray("a");
        ethKrakenPrice = Double.parseDouble(krakenAsk.get(0).toString());

        // ETH Delta
        if(ethKrakenPrice != null && ethGdaxPrice != null){
            this.ethDelta = (Math.abs(ethGdaxPrice / ethKrakenPrice) - 1) * 100;

            String message = "!!! Ethereum is is " + ethDelta + "% above in gdax than kraken\n";
            Log.i(TAG,message);
        }
    }

    private void initBtc() throws JSONException {
        // GDAX_BTC
        JSONObject gdaxPricesJson = gdaxJsonBtc.getJSONObject(0);

        btcGdaxPrice = Double.parseDouble(gdaxPricesJson.get("price").toString());

        // KRAKEN_BTC
        JSONObject krakenPricesJson = krakenJsonBtc.getJSONObject("result").getJSONObject("XXBTZEUR");
        JSONArray krakenAsk = krakenPricesJson.getJSONArray("a");
        btcKrakenPrice = Double.parseDouble(krakenAsk.get(0).toString());

        // BTC Delta
        if(btcKrakenPrice != null && btcGdaxPrice != null){
            this.btcDelta = (Math.abs(btcGdaxPrice / btcKrakenPrice) - 1) * 100;

            String message = "!!! Bitcoin is is " + btcDelta + "% above in gdax than kraken\n";
            Log.i(TAG,message);
        }
    }

    private void initLtc() throws JSONException {
        // GDAX_LTC
        JSONObject gdaxPricesJson = gdaxJsonLtc.getJSONObject(0);

        ltcGdaxPrice = Double.parseDouble(gdaxPricesJson.get("price").toString());

        // KRAKEN_LTC
        JSONObject krakenPricesJson = krakenJsonLtc.getJSONObject("result").getJSONObject("XLTCZEUR");
        JSONArray krakenAsk = krakenPricesJson.getJSONArray("a");
        ltcKrakenPrice = Double.parseDouble(krakenAsk.get(0).toString());

        // ETH Delta
        if(ltcKrakenPrice != null && ltcGdaxPrice != null){
            this.ltcDelta = (Math.abs(ltcGdaxPrice / ltcKrakenPrice) - 1) * 100;

            String message = "!!! Litecoin is is " + ltcDelta + "% above in gdax than kraken\n";
            Log.i(TAG,message);
        }
    }

    public String getArbitrageAlerts() {
        double minDelta = UserPreferencesManager.getInstance().getMinDelta();

        String message = "";

        // ETH
        if(UserPreferencesManager.getInstance().isEthBuyKrakenSellGdax() && ethDelta > minDelta) {
            message += "ETH -> GDAX / Kraken = " + ethDelta + "\n";
        }

        if(UserPreferencesManager.getInstance().isEthBuyGdaxSellKraken() && ethDelta < minDelta * -1) {
            message += "ETH -> GDAX / Kraken = " + ethDelta + "\n";
        }

        // BTC
        if(UserPreferencesManager.getInstance().isBtcBuyKrakenSellGdax() && ethDelta > minDelta) {
            message += "BTC -> GDAX / Kraken = " + btcDelta + "\n";
        }

        if(UserPreferencesManager.getInstance().isBtcBuyGdaxSellKraken() && ethDelta < minDelta * -1) {
            message += "BTC -> GDAX / Kraken = " + btcDelta + "\n";
        }

        // LTC
        if(UserPreferencesManager.getInstance().isLtcBuyKrakenSellGdax() && ethDelta > minDelta) {
            message += "LTC -> GDAX / Kraken = " + ltcDelta + "\n";
        }

        if(UserPreferencesManager.getInstance().isLtcBuyGdaxSellKraken() && ethDelta < minDelta * -1) {
            message += "LTC -> GDAX / Kraken = " + ltcDelta + "\n";
        }

        return message;
    }

    public String getArbitrageMonitor() {

        String date = dateFormat.format(new Date());

        String message = date + "\n"

                + "ETH" + "\n"
                + "GDAX / Kraken = " + ethDelta + "\n"
                + "GDAX: " + ethGdaxPrice + "\n"
                + "Kraken: " + ethKrakenPrice + "\n" + "\n"

                + "BTC" + "\n"
                + "GDAX / Kraken = " + btcDelta + "\n"
                + "GDAX: " + btcGdaxPrice + "\n"
                + "Kraken: " + btcKrakenPrice + "\n" + "\n"

                + "LTC" + "\n"
                + "GDAX / Kraken = " + ltcDelta + "\n"
                + "GDAX: " + ltcGdaxPrice + "\n"
                + "Kraken: " + ltcKrakenPrice
                ;


        return message;
    }
}
