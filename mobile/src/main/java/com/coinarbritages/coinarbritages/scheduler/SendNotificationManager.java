package com.coinarbritages.coinarbritages.scheduler;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import com.coinarbritages.coinarbritages.R;
import com.coinarbritages.coinarbritages.common.Log;

import com.coinarbritages.coinarbritages.MainActivity;
import com.coinarbritages.coinarbritages.common.SharedResources;
import com.coinarbritages.coinarbritages.common.configuration.ConfigurationManager;
import com.coinarbritages.coinarbritages.manager.ExchangeDataProcessor;

/**
 * Created by ivofernandes on 18/10/15.
 */
public class SendNotificationManager {

    private static final String TAG = "SendNotificationManager";

    // Singleton
    private static SendNotificationManager instance = new SendNotificationManager();

    private SendNotificationManager(){}

    public static SendNotificationManager getInstance(){
        return instance;
    }

    // Vibration pattern
    private long[] mVibratePattern = { 0, 200,
            1000,1000,1000,1000,1000,1000,1000,1000,1000,
            1000,1000,1000,1000,1000,1000,1000,1000,1000,
            1000,1000,1000,1000,1000,1000,1000,1000,1000
    };

    // Fields
    private SharedResources sharedResources = SharedResources.getInstance();

    private ConfigurationManager configurationManager = ConfigurationManager.getInstance();

    /**
     * Receives a 3h json and generate the notifications that it should produce
     * @param processor
     */
    public void fireNotications(ExchangeDataProcessor processor){


        generatePriceNotification();

        NotificationSchedulingService.getInstance().done();

    }

    private boolean generatePriceNotification() {

        // Generate the notification
        String title = sharedResources.resolveString(R.string.notification_title);

        String description = "HODL";

        fireNotication(title, description);

        return true;


    }

    public void fireNotication(String title, String description) {
        Log.d(TAG,"notification: " + description);
        Intent notificationIntent = new Intent(sharedResources.getContext(), MainActivity.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent intent = PendingIntent.getActivity(sharedResources.getContext(), 0,
                notificationIntent, 0);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification.Builder notificationBuilder = new Notification.Builder(
                sharedResources.getContext())
                .setTicker(title)
                .setSmallIcon(R.drawable.notification_icon)
                .setAutoCancel(true)
                .setVibrate(mVibratePattern)
                .setContentTitle(title)
                .setContentIntent(intent)
                .setContentText(description)
                .setSound(alarmSound);

        SharedResources.getInstance().getNotificationManager().notify(SharedResources.RAIN_NOTIFICATION_ID,
                notificationBuilder.build());

    }

    public int getNotificationHour(){
        return 18;
    }

    public int getNotificationMinute(){
        return 0;
    }
}
