package com.coinarbritages.coinarbritages.scheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.coinarbritages.coinarbritages.common.Log;
import com.coinarbritages.coinarbritages.common.SharedResources;
import com.coinarbritages.coinarbritages.scheduler.NotificationAlarmReceiver;

/**
 * This BroadcastReceiver automatically (re)starts the alarm when the device is
 * rebooted. This receiver is set to be disabled (android:enabled="false") in the
 * application's manifest file. When the user sets the alarm, the receiver is enabled.
 * When the user cancels the alarm, the receiver is disabled, so that rebooting the
 * device will not trigger this receiver.
 */
// BEGIN_INCLUDE(autostart)
public class NotificationBootReceiver extends BroadcastReceiver {

    private static String TAG = "NotificationBootReceiver";

    NotificationAlarmReceiver alarm = new NotificationAlarmReceiver();
    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            Log.i(TAG, "reboot receiver called");

            SharedResources.getInstance().init(context);

            Log.i(TAG, "alarm setted");
        }catch (Exception e){
            Log.e(TAG,"Error initing boot alarm");
        }
    }
}
//END_INCLUDE(autostart)
