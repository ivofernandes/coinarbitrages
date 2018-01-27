package com.coinarbritages.coinarbritages;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.coinarbritages.coinarbritages.activities.MenuActivity;
import com.coinarbritages.coinarbritages.common.Log;
import com.coinarbritages.coinarbritages.common.SharedResources;
import com.coinarbritages.coinarbritages.common.configuration.ConfigurationManager;
import com.coinarbritages.coinarbritages.common.configuration.LayoutManager;
import com.coinarbritages.coinarbritages.manager.ForecastInterfaceManager;
import com.coinarbritages.coinarbritages.manager.DataManager;
import com.coinarbritages.coinarbritages.scheduler.SendNotificationManager;

/**
 * MainActivity 
 *
 * Send reboot intent:
 * cd /Users/ivofernandes/Library/Android/sdk/platform-tools/
 * ./adb shell am broadcast -a android.intent.action.BOOT_COMPLETED
 */
public class MainActivity extends MenuActivity {

    private static final String TAG = "MainActivity";

    private SharedResources sharedResources = SharedResources.getInstance();

    // Data manager
    private DataManager dataManager = DataManager.getInstance();

    //  User Interface
    private ForecastInterfaceManager forecastInterfaceManager = ForecastInterfaceManager.getInstance();
    private LayoutManager layoutManager = LayoutManager.getInstance();
    private ConfigurationManager configurationManager = ConfigurationManager.getInstance();
    private SendNotificationManager sendNotificationManager = SendNotificationManager.getInstance();

    // Singleton
    private static MainActivity instance = null;
    private LinearLayout mainView;

    public static MainActivity getInstance(){
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate START >");
        super.onCreate(savedInstanceState);

        init();

        Log.d(TAG, "onCreate END <");
    }

    @Override
    protected void onResume() {

        Log.d(TAG, "onResume START >");
        super.onResume();
        SharedResources.getInstance().setContext(getApplicationContext());

        // Start Forecast UI manager
        final LinearLayout forecastPanel = (LinearLayout) findViewById(R.id.forecastView);
        forecastInterfaceManager.setForecastView(forecastPanel);

        dataManager.requestAllData(DataManager.WeatherRequestType.UPDATE_VIEWS);

        initViews();

        // Init activity

        configureOrientation(getResources().getConfiguration());

        // Analytics
        try {
            ((AnalyticsApplication) getApplication()).startTracking();

            AdView mAdView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
            mAdView.loadAd(adRequest);
        }catch(Exception e){
            Log.e(TAG,"error initing ads and analytics",e);
        }

        Log.d(TAG, "onResume END <");
    }

    private void init() {
        instance = this;

        final SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        sharedResources.init(getApplicationContext());
        FragmentManager fragmentManager = getSupportFragmentManager();
        sharedResources.setFragmentManager(fragmentManager);

        setContentView(R.layout.activity_main);
    }

    public void initLabels() {
        TextView text3hours = (TextView) findViewById(R.id.text3hours);
        TextView textDaily = (TextView) findViewById(R.id.textDaily);

        text3hours.setTextColor(layoutManager.getSmoothForegroundColor());
        textDaily.setTextColor(layoutManager.getSmoothForegroundColor());
    }

    private void initViews() {
        this.mainView = (LinearLayout) findViewById(R.id.mainView);
        this.mainView.setBackgroundColor(layoutManager.getBackgroundColor());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    public void loadingComplete(){
        RelativeLayout image = (RelativeLayout) findViewById(R.id.loadingImage);
        image.setVisibility(View.GONE);

        // Init labels
        initLabels();
    }

    public void setLastUpdate(String lastUpd,int color){
        // show last update
        TextView textView = (TextView) findViewById(R.id.lastUpd);

        if(lastUpd != null) {
            String lastUpdateDescription = sharedResources.resolveString(R.string.last_update);
            textView.setText(lastUpdateDescription + " " + lastUpd);
            textView.setVisibility(View.VISIBLE);
            textView.setTextColor(color);
            textView.setGravity(Gravity.CENTER);
        }else{
            textView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        configureOrientation(newConfig);

    }

    private void configureOrientation(Configuration config) {

        LinearLayout forecastView = (LinearLayout) findViewById(R.id.forecastView);

        FrameLayout id3hours = (FrameLayout) findViewById(R.id.id3hours);
        FrameLayout idDaily = (FrameLayout) findViewById(R.id.idDaily);

        // Checks the orientation of the screen
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            forecastView.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams id3hoursLayoutParams = (LinearLayout.LayoutParams) id3hours.getLayoutParams();
            id3hoursLayoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            id3hoursLayoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
            id3hours.setLayoutParams(id3hoursLayoutParams);


            LinearLayout.LayoutParams idDailyLayoutParams = (LinearLayout.LayoutParams) id3hours.getLayoutParams();
            idDailyLayoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            idDailyLayoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
            idDaily.setLayoutParams(idDailyLayoutParams);

        } else if (config.orientation == Configuration.ORIENTATION_PORTRAIT){
            forecastView.setOrientation(LinearLayout.VERTICAL);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) id3hours.getLayoutParams();
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            params.width = LinearLayout.LayoutParams.MATCH_PARENT;
            id3hours.setLayoutParams(params);

            LinearLayout.LayoutParams idDailyLayoutParams = (LinearLayout.LayoutParams) id3hours.getLayoutParams();
            idDailyLayoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            idDailyLayoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
            idDaily.setLayoutParams(idDailyLayoutParams);
        }
    }

    public void setAppTitle(String appTitle) {
        if(appTitle != null && !appTitle.equals("")) {
            setTitle(appTitle);
        }
    }

}
