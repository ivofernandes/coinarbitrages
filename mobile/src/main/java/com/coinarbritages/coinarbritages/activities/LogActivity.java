package com.coinarbritages.coinarbritages.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.coinarbritages.coinarbritages.MainActivity;
import com.coinarbritages.coinarbritages.R;
import com.coinarbritages.coinarbritages.common.Log;
import com.coinarbritages.coinarbritages.common.SharedResources;
import com.coinarbritages.coinarbritages.common.configuration.LayoutManager;

/**
 * Created by ivofernandes on 26/01/16.
 */
public class LogActivity  extends ActionBarActivity{
    private SharedResources sharedResources = SharedResources.getInstance();
    private LayoutManager layoutManager = LayoutManager.getInstance();
    private LinearLayout panel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        String logList = getIntent().getExtras().getString("logList");
        String log = getIntent().getExtras().getString("log");

        // Change colors of main panel
        ScrollView scroll = (ScrollView) findViewById(R.id.logPanelScroll);
        scroll.setBackgroundColor(layoutManager.getBackgroundColor());

        LinearLayout mainPanel = (LinearLayout) findViewById(R.id.logPanel);
        mainPanel.setBackgroundColor(layoutManager.getBackgroundColor());

        // If it's to create a list
        if(logList != null && logList.equals("all")){
            for(String logString : Log.allLogs()){
                TextView textView = generateLog(logString);

                if(logString.startsWith("E ")){
                    textView.setTextColor(Color.RED);
                } else if(logString.startsWith("I ")){
                    textView.setTextColor(Color.CYAN);
                }else if(logString.startsWith("W ")){
                    textView.setTextColor(Color.YELLOW);
                }

                mainPanel.addView(textView);
            }
        }
        // If only needs to show one log
        else {
            TextView textView = generateLog(log);
            mainPanel.addView(textView);
        }
    }

    private TextView generateLog(String log) {

        TextView textView = new TextView(MainActivity.getInstance().getApplicationContext());
        textView.setTextColor(layoutManager.getForegroundColor());
        textView.setText(log);
        textView.setTextSize(10);
        return textView;
    }
}
