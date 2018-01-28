package com.coinarbritages.coinarbritages.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coinarbritages.coinarbritages.R;
import com.coinarbritages.coinarbritages.common.SharedResources;
import com.coinarbritages.coinarbritages.common.configuration.LayoutManager;

public class HelpActivity extends ActionBarActivity {

    private LayoutManager layoutManager = LayoutManager.getInstance();
    private LinearLayout panel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // Change colors
        RelativeLayout mainPanel = (RelativeLayout) findViewById(R.id.helpMainPanel);
        mainPanel.setBackgroundColor(layoutManager.getBackgroundColor());

        this.panel = (LinearLayout) findViewById(R.id.helpPanel);
        panel.setBackgroundColor(layoutManager.getBackgroundColor());

        TextView aboutThunderwarn = (TextView) findViewById(R.id.aboutThunderwarn);
        aboutThunderwarn.setTextColor(layoutManager.getForegroundColor());

        TextView aboutThunderwarnDescription = (TextView) findViewById(R.id.aboutThunderwarnDescription);
        aboutThunderwarnDescription.setTextColor(layoutManager.getForegroundColor());

        TextView colorMeaning = (TextView) findViewById(R.id.colorMeaning);
        colorMeaning.setTextColor(layoutManager.getForegroundColor());


        TextView colorMeaningDescription = (TextView) findViewById(R.id.colorMeaningDescription);
        colorMeaningDescription.setTextColor(layoutManager.getForegroundColor());

        LinearLayout colorMeaningDescriptionPanel = (LinearLayout) findViewById(R.id.colorMeaningDescriptionPanel);
        colorMeaningDescriptionPanel.setBackgroundColor(layoutManager.getBackgroundColor());

        TextView textDirectTrade = (TextView) findViewById(R.id.directTrade);
        textDirectTrade.setTextColor(layoutManager.getForegroundColor());

        TextView apparentTemperatureDescription = (TextView) findViewById(R.id.apparentTemperatureDescription);
        apparentTemperatureDescription.setTextColor(layoutManager.getForegroundColor());


    }


}
