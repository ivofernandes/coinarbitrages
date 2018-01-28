package com.coinarbritages.coinarbritages.scheduler;

import android.app.IntentService;
import android.content.Intent;

import com.coinarbritages.coinarbritages.common.Log;
import com.coinarbritages.coinarbritages.common.SharedResources;
import com.coinarbritages.coinarbritages.manager.DataManager;
import com.coinarbritages.coinarbritages.manager.ExchangeDataProcessor;

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

    private Set<DataManager.RequestSource> updateRequestType = new HashSet<>();

    public NotificationSchedulingService() {
        super("NotificationSchedulingService");

        instance = this;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        this.intent = intent;
        start();
    }

    public void start() {
        try {

            Thread.sleep(30 * 1000); // Wait 30 seconds to repeat
            SharedResources.getInstance().init(this.getApplicationContext());

            Log.d(TAG, "start update/notification");

            clear();

            DataManager.getInstance().requestAllData(DataManager.RequestType.NOTIFICATION);

            Log.d(TAG, "end start update/notification");
        }catch (Exception e){
            Log.e(TAG,"Error processing scheduling event");
        }

        done();
    }

    private void clear() {
        updateRequestType.clear();
    }

    public void sendNotification(ExchangeDataProcessor processor){
        try {

            SendNotificationManager.getInstance().fireNotications(processor);
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

    public void updated(ExchangeDataProcessor processor) {

        sendNotification(processor);

        done();
    }
}
