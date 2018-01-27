package com.coinarbritages.coinarbritages.activities;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.coinarbritages.coinarbritages.R;
import com.coinarbritages.coinarbritages.common.configuration.LayoutManager;
import com.coinarbritages.coinarbritages.common.configuration.UserPreferencesManager;

public class ChangeLocationActivity extends MenuActivity implements OnMapReadyCallback {

    private LayoutManager layoutManager = LayoutManager.getInstance();

    private LinearLayout panel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_location);

        // Change panel colors
        RelativeLayout mainPanel = (RelativeLayout) findViewById(R.id.changeLocationMainPanel);
        mainPanel.setBackgroundColor(layoutManager.getBackgroundColor());

        this.panel = (LinearLayout) findViewById(R.id.changeLocationPanel);
        panel.setBackgroundColor(layoutManager.getBackgroundColor());

        // Use Gps
        Switch switchUseGps = (Switch) findViewById(R.id.switchUseGps);
        switchUseGps.setTextColor(layoutManager.getForegroundColor());
        switchUseGps.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                UserPreferencesManager.getInstance().setUseGps(isChecked);
            }
        });

        switchUseGps.setChecked(UserPreferencesManager.getInstance().isUseGps());
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
