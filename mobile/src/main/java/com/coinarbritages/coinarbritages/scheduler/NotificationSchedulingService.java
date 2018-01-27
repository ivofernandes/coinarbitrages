package com.coinarbritages.coinarbritages.scheduler;

import android.app.IntentService;
import android.content.Intent;

import com.coinarbritages.coinarbritages.common.Log;

import com.coinarbritages.coinarbritages.common.SharedResources;
import com.coinarbritages.coinarbritages.manager.DataManager;

import java.util.HashSet;
import java.util.Set;

/**
 * This {@code IntentService} does the app's actual work.
 * {@code NotificationAlarmReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class NotificationSchedulingService extends IntentService {

    private static NotificationSchedulingService instance;

    public static NotificationSchedulingService getInstance(){
        return instance;
    }

    public static final String TAG = "NotificationSchedulingService";

    // Fields
    private Intent intent;

    private DataManager weatherDataManager = DataManager.getInstance();

    private Set<String> updateRequestType = new HashSet<String>();

    public NotificationSchedulingService() {
        super("NotificationSchedulingService");

        instance = this;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            this.intent = intent;

            SharedResources.getInstance().init(this.getApplicationContext());

            Log.d(TAG, "start update/notification");

            clear();

            sendNotification();

            Thread.sleep(2 * 60 * 1000); // Wait two minutes until it's done

            Log.d(TAG, "end start update/notification");
        }catch (Exception e){
            Log.e(TAG,"Error processing scheduling event");
        }

        done();
    }

    private void clear() {
        updateRequestType.clear();
    }

    public void sendNotification(){
        try {

            DataManager.getInstance().requestAllData(DataManager.WeatherRequestType.NOTIFICATION);
        }catch (Exception e) {
            Log.e(TAG, "error trying to generate a weather notification: " + e.getMessage(), e);
            done();
        }
    }

    public void done() {
        try {
            // Release the wake lock provided by the BroadcastReceiver.
            NotificationAlarmReceiver.completeWakefulIntent(intent);
            // END_INCLUDE(service_onhandle)
        }catch (Exception e){
            Log.e(TAG,"Error completing wakeup: "+ e.getMessage(),e);
        }
    }

    public void updated(String requestType) {
        updateRequestType.add(requestType);

        if(updateRequestType.size() == 3){
            done();
        }
    }


    public void noInternetError() {
        done();
        // Alert the user that can't get data...enable 3g data??
        Log.e(TAG,"no internet error");
    }
}
