package com.coinarbritages.coinarbritages.manager;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by ivofernandes on 28/01/2018.
 */

public class ExchangeDataProcessor {

    private final JSONObject kraken;
    private final JSONArray gdax;

    public ExchangeDataProcessor(JSONArray gdax, JSONObject kraken) {
        this.gdax = gdax;
        this.kraken = kraken;

        init();
    }

    /**
     * Process the exchange data
     */
    private void init() {

        initArbitrage();

    }

    private void initArbitrage() {

    }
}
