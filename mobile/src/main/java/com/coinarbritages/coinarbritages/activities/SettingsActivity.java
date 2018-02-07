package com.coinarbritages.coinarbritages.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.coinarbritages.coinarbritages.R;
import com.coinarbritages.coinarbritages.common.SharedResources;
import com.coinarbritages.coinarbritages.common.configuration.LayoutManager;
import com.coinarbritages.coinarbritages.common.configuration.UserPreferencesManager;

public class SettingsActivity extends ActionBarActivity {

    private LayoutManager layoutManager = LayoutManager.getInstance();
    private UserPreferencesManager userPreferencesManager = UserPreferencesManager.getInstance();

    private LinearLayout panel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedResources.getInstance().setContext(getApplicationContext());

        setContentView(R.layout.activity_settings);

        // Change panel colors
        RelativeLayout mainPanel = (RelativeLayout) findViewById(R.id.SettingsMainPanel);
        mainPanel.setBackgroundColor(layoutManager.getBackgroundColor());

        this.panel = (LinearLayout) findViewById(R.id.settingsPanel);
        panel.setBackgroundColor(layoutManager.getBackgroundColor());

        TextView notificationsText = (TextView) findViewById(R.id.notificationsText);
        notificationsText.setTextColor(layoutManager.getForegroundColor());

        // ETH
        TextView ethText = (TextView) findViewById(R.id.ethText);
        ethText.setTextColor(layoutManager.getForegroundColor());

        TextView ethBuyGdaxSellKrakenText = (TextView) findViewById(R.id.ethBuyGdaxSellKraken);
        ethBuyGdaxSellKrakenText.setTextColor(layoutManager.getForegroundColor());

        TextView ethBuyKrakenSellGdaxText = (TextView) findViewById(R.id.ethBuyKrakenSellGdax);
        ethBuyKrakenSellGdaxText.setTextColor(layoutManager.getForegroundColor());

        // BTC
        TextView btcText = (TextView) findViewById(R.id.btcText);
        btcText.setTextColor(layoutManager.getForegroundColor());

        TextView btcBuyGdaxSellKrakenText = (TextView) findViewById(R.id.btcBuyGdaxSellKraken);
        btcBuyGdaxSellKrakenText.setTextColor(layoutManager.getForegroundColor());

        TextView btcBuyKrakenSellGdaxText = (TextView) findViewById(R.id.btcBuyKrakenSellGdax);
        btcBuyKrakenSellGdaxText.setTextColor(layoutManager.getForegroundColor());

        // LTC
        TextView ltcText = (TextView) findViewById(R.id.ltcText);
        ltcText.setTextColor(layoutManager.getForegroundColor());

        TextView ltcBuyGdaxSellKrakenText = (TextView) findViewById(R.id.ltcBuyGdaxSellKraken);
        ltcBuyGdaxSellKrakenText.setTextColor(layoutManager.getForegroundColor());

        TextView ltcBuyKrakenSellGdaxText = (TextView) findViewById(R.id.ltcBuyKrakenSellGdax);
        ltcBuyKrakenSellGdaxText.setTextColor(layoutManager.getForegroundColor());

        // Notification
        Switch switchNotification = (Switch) findViewById(R.id.switchNotification);
        switchNotification.setTextColor(layoutManager.getForegroundColor());
        switchNotification.setChecked(userPreferencesManager.isNotification());
        switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                UserPreferencesManager.getInstance().setNotification(isChecked);
            }
        });

        // Notification time
        //http://developer.android.com/guide/topics/ui/controls/pickers.html

        // Eth
        Switch ethBuyKrakenSellGdaxSwitch = (Switch) findViewById(R.id.switchEthBuyKrakenSellGdax);
        ethBuyKrakenSellGdaxSwitch.setTextColor(layoutManager.getForegroundColor());
        ethBuyKrakenSellGdaxSwitch.setChecked(userPreferencesManager.isEthBuyKrakenSellGdax());
        ethBuyKrakenSellGdaxSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                UserPreferencesManager.getInstance().setEthBuyKrakenSellGdax(isChecked);
            }
        });

        Switch ethBuyGdaxSellKrakenSwitch = (Switch) findViewById(R.id.switchEthBuyGdaxSellKraken);
        ethBuyGdaxSellKrakenSwitch.setTextColor(layoutManager.getForegroundColor());
        ethBuyGdaxSellKrakenSwitch.setChecked(userPreferencesManager.isEthBuyGdaxSellKraken());
        ethBuyGdaxSellKrakenSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                UserPreferencesManager.getInstance().setEthBuyGdaxSellKraken(isChecked);
            }
        });

        // Btc
        Switch btcBuyKrakenSellGdaxSwitch = (Switch) findViewById(R.id.switchBtcBuyKrakenSellGdax);
        btcBuyKrakenSellGdaxSwitch.setTextColor(layoutManager.getForegroundColor());
        btcBuyKrakenSellGdaxSwitch.setChecked(userPreferencesManager.isBtcBuyKrakenSellGdax());
        btcBuyKrakenSellGdaxSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                UserPreferencesManager.getInstance().setBtcBuyKrakenSellGdax(isChecked);
            }
        });

        Switch btcBuyGdaxSellKrakenSwitch = (Switch) findViewById(R.id.switchBtcBuyGdaxSellKraken);
        btcBuyGdaxSellKrakenSwitch.setTextColor(layoutManager.getForegroundColor());
        btcBuyGdaxSellKrakenSwitch.setChecked(userPreferencesManager.isBtcBuyGdaxSellKraken());
        btcBuyGdaxSellKrakenSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                UserPreferencesManager.getInstance().setBtcBuyGdaxSellKraken(isChecked);
            }
        });

        // Ltc
        Switch ltcBuyKrakenSellGdaxSwitch = (Switch) findViewById(R.id.switchLtcBuyKrakenSellGdax);
        ltcBuyKrakenSellGdaxSwitch.setTextColor(layoutManager.getForegroundColor());
        ltcBuyKrakenSellGdaxSwitch.setChecked(userPreferencesManager.isLtcBuyKrakenSellGdax());
        ltcBuyKrakenSellGdaxSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                UserPreferencesManager.getInstance().setLtcBuyKrakenSellGdax(isChecked);
            }
        });

        Switch ltcBuyGdaxSellKrakenSwitch = (Switch) findViewById(R.id.switchLtcBuyGdaxSellKraken);
        ltcBuyGdaxSellKrakenSwitch.setTextColor(layoutManager.getForegroundColor());
        ltcBuyGdaxSellKrakenSwitch.setChecked(userPreferencesManager.isLtcBuyGdaxSellKraken());
        ltcBuyGdaxSellKrakenSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                UserPreferencesManager.getInstance().setLtcBuyGdaxSellKraken(isChecked);
            }
        });

    }
}
