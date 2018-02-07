package com.coinarbritages.coinarbritages.manager;

import com.coinarbritages.coinarbritages.common.Log;
import com.coinarbritages.coinarbritages.common.configuration.UserPreferencesManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ivofernandes on 28/01/2018.
 */

public class ExchangeDataProcessor {

    private final static String TAG = "ExchangeDataProcessor";

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final NumberFormat numberFormat = NumberFormat.getNumberInstance();

    private JSONArray gdaxJsonBtc;
    private JSONArray gdaxJsonEth;
    private JSONArray gdaxJsonLtc;

    private JSONObject krakenJsonBtc;
    private JSONObject krakenJsonEth;
    private JSONObject krakenJsonLtc;
    private JSONObject krakenJsonXrp;

    private JSONObject bistampBtc;
    private JSONObject bistampEth;
    private JSONObject bistampXrp;

    private double ethGdaxKrakenDelta;
    private double ethGdaxBitstampDelta;
    private Double ethGdaxPrice = null;
    private Double ethKrakenPrice = null;

    private double btcGdaxKrakenDelta;
    private double btcGdaxBitstampDelta;
    private Double btcGdaxPrice = null;
    private Double btcKrakenPrice = null;
    private Double xrpKrakenPrice;

    private double ltcDelta;
    private Double ltcGdaxPrice = null;
    private Double ltcKrakenPrice = null;

    private Double ethBitstampPrice;
    private Double btcBitstampPrice;
    private Double xrpBitstampPrice;
    private double xrpBitstampKrakenDelta;

    public ExchangeDataProcessor() {
        numberFormat.setMinimumFractionDigits(2);
    }

    public void loadGdax(JSONArray gdax_BTC, JSONArray gdax_ETH, JSONArray gdax_LTC) {
        this.gdaxJsonBtc = gdax_BTC;
        this.gdaxJsonEth = gdax_ETH;
        this.gdaxJsonLtc = gdax_LTC;
    }

    public void loadKraken(JSONObject kraken_BTC, JSONObject kraken_ETH, JSONObject kraken_LTC, JSONObject kraken_XRP) {
        this.krakenJsonBtc = kraken_BTC;
        this.krakenJsonEth = kraken_ETH;
        this.krakenJsonLtc = kraken_LTC;
        this.krakenJsonXrp = kraken_XRP;
    }

    public void loadBitstamp(JSONObject bistamp_btc, JSONObject bistamp_eth, JSONObject bistamp_xrp) {
        this.bistampBtc = bistamp_btc;
        this.bistampEth = bistamp_eth;
        this.bistampXrp = bistamp_xrp;
    }

    /**
     * Process the exchange data
     */
    public void init() {

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

        initXrp();
    }

    private void initXrp() throws JSONException {
        // KRAKEN_XRP
        JSONObject krakenPricesJson = krakenJsonXrp.getJSONObject("result").getJSONObject("XXRPZEUR");
        JSONArray krakenAsk = krakenPricesJson.getJSONArray("a");
        xrpKrakenPrice = Double.parseDouble(krakenAsk.get(0).toString());

        // Bitstamp xrp
        xrpBitstampPrice = Double.parseDouble(bistampXrp.get("last").toString());

        // XRP Delta
        if(xrpKrakenPrice != null && xrpBitstampPrice != null){
            this.xrpBitstampKrakenDelta = (Math.abs(xrpBitstampPrice / xrpKrakenPrice) - 1) * 100;

            String message = "!!! Bitcoin is is " + btcGdaxKrakenDelta + "% above in bitstamp than kraken\n";
            Log.i(TAG,message);
        }
    }

    private void initEth() throws JSONException {
        // GDAX_ETH
        JSONObject gdaxPricesJson = gdaxJsonEth.getJSONObject(0);

        ethGdaxPrice = Double.parseDouble(gdaxPricesJson.get("price").toString());

        // KRAKEN_ETH
        JSONObject krakenPricesJson = krakenJsonEth.getJSONObject("result").getJSONObject("XETHZEUR");
        JSONArray krakenAsk = krakenPricesJson.getJSONArray("a");
        ethKrakenPrice = Double.parseDouble(krakenAsk.get(0).toString());

        // Bitstamp ETH
        ethBitstampPrice = Double.parseDouble(bistampEth.get("last").toString());

        // ETH Delta
        if(ethKrakenPrice != null && ethGdaxPrice != null){
            this.ethGdaxKrakenDelta = (Math.abs(ethGdaxPrice / ethKrakenPrice) - 1) * 100;

            String message = "!!! Ethereum is is " + ethGdaxKrakenDelta + "% above in gdax than kraken\n";
            Log.i(TAG,message);
        }

        if(ethBitstampPrice != null && ethGdaxPrice != null){
            this.ethGdaxBitstampDelta = (Math.abs(ethGdaxPrice / ethBitstampPrice) - 1) * 100;

            String message = "!!! Ethereum is is " + ethGdaxBitstampDelta + "% above in gdax than bitstamp\n";
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

        // Bitstamp BTC
        btcBitstampPrice = Double.parseDouble(bistampBtc.get("last").toString());

        // BTC Delta
        if(btcKrakenPrice != null && btcGdaxPrice != null){
            this.btcGdaxKrakenDelta = (Math.abs(btcGdaxPrice / btcKrakenPrice) - 1) * 100;

            String message = "!!! Bitcoin is is " + btcGdaxKrakenDelta + "% above in gdax than kraken\n";
            Log.i(TAG,message);
        }

        if(btcBitstampPrice != null && btcGdaxPrice != null){
            this.btcGdaxBitstampDelta = (Math.abs(btcGdaxPrice / btcBitstampPrice) - 1) * 100;

            String message = "!!! Bitcoin is is " + btcGdaxBitstampDelta + "% above in gdax than bitstamp\n";
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
        if(UserPreferencesManager.getInstance().isEthBuyKrakenSellGdax() && ethGdaxKrakenDelta > minDelta) {
            message += "ETH -> GDAX / Kraken = " + ethGdaxKrakenDelta + "\n";
        }

        if(UserPreferencesManager.getInstance().isEthBuyGdaxSellKraken() && ethGdaxKrakenDelta < minDelta * -1) {
            message += "ETH -> GDAX / Kraken = " + ethGdaxKrakenDelta + "\n";
        }

        // BTC
        if(UserPreferencesManager.getInstance().isBtcBuyKrakenSellGdax() && btcGdaxKrakenDelta > minDelta) {
            message += "BTC -> GDAX / Kraken = " + btcGdaxKrakenDelta + "\n";
        }

        if(UserPreferencesManager.getInstance().isBtcBuyGdaxSellKraken() && btcGdaxKrakenDelta < minDelta * -1) {
            message += "BTC -> GDAX / Kraken = " + btcGdaxKrakenDelta + "\n";
        }

        // LTC
        if(UserPreferencesManager.getInstance().isLtcBuyKrakenSellGdax() && ltcDelta > minDelta) {
            message += "LTC -> GDAX / Kraken = " + ltcDelta + "\n";
        }

        if(UserPreferencesManager.getInstance().isLtcBuyGdaxSellKraken() && ltcDelta < minDelta * -1) {
            message += "LTC -> GDAX / Kraken = " + ltcDelta + "\n";
        }

        return message;
    }

    public String getArbitrageMonitor() {
        String date = dateFormat.format(new Date());

        String message = date + "\n"

                + "ETH" + "\n"
                + "GDAX / Kraken = " + numberFormat.format(ethGdaxKrakenDelta) + "%\n"
                + "GDAX / Bitstamp = " + numberFormat.format(ethGdaxBitstampDelta) + "%\n"
                + "GDAX: " + ethGdaxPrice + "\n"
                + "Kraken: " + ethKrakenPrice + "\n"
                + "Bitstamp: " + ethBitstampPrice + "\n" + "\n"

                + "BTC" + "\n"
                + "GDAX / Kraken = " + numberFormat.format(btcGdaxKrakenDelta) + "%\n"
                + "GDAX / Bitstamp = " + numberFormat.format(btcGdaxBitstampDelta) + "%\n"
                + "GDAX: " + btcGdaxPrice + "\n"
                + "Kraken: " + btcKrakenPrice + "\n"
                + "Bitstamp: " + btcBitstampPrice + "\n" + "\n"

                + "XRP" + "\n"
                + "Bitstamp / Kraken = " + numberFormat.format(xrpBitstampKrakenDelta) + "%\n"
                + "Kraken: " + xrpKrakenPrice + "\n"
                + "Bitstamp: " + xrpBitstampPrice + "\n" + "\n"

                + "LTC" + "\n"
                + "GDAX / Kraken = " + numberFormat.format(ltcDelta) + "%\n"
                + "GDAX: " + ltcGdaxPrice + "\n"
                + "Kraken: " + ltcKrakenPrice
                ;


        return message;
    }
}
