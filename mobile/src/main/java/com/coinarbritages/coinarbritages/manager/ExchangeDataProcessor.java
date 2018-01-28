package com.coinarbritages.coinarbritages.manager;

import com.coinarbritages.coinarbritages.common.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ivofernandes on 28/01/2018.
 */

public class ExchangeDataProcessor {

    private final static String TAG = "ExchangeDataProcessor";

    private final JSONObject krakenJson;
    private final JSONArray gdaxJson;
    private double delta;

    public ExchangeDataProcessor(JSONArray gdax, JSONObject kraken) {
        this.gdaxJson = gdax;
        this.krakenJson = kraken;

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
        // GDAX
        JSONObject gdaxPricesJson = gdaxJson.getJSONObject(0);

        Double priceInGdax = Double.parseDouble(gdaxPricesJson.get("price").toString());

        // KRAKEN
        JSONObject krakenPricesJson = krakenJson.getJSONObject("result").getJSONObject("XETHZEUR");
        JSONArray krakenAsk = krakenPricesJson.getJSONArray("a");
        Double priceInKraken = Double.parseDouble(krakenAsk.get(0).toString());

        if(priceInKraken != null && priceInGdax != null){
            this.delta = (Math.abs(priceInGdax / priceInKraken) - 1) * 100;

            String message = "!!! Ethereum is is " + delta + "% above in gdax than kraken\n";

            Log.i(TAG,message);


        }

    }

    public String getArbitrageAlerts() {
        String message = "GDAX / Kraken = " + delta;
        return message;
    }
}
