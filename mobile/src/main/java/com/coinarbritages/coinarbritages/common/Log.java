package com.coinarbritages.coinarbritages.common;

import android.view.Gravity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ivofernandes on 17/12/15.
 */
public class Log {
    private static final String TAG = "Log";
    private static final String ERROR_LOG_CACHE = "ERROR_LOG";
    private static final String LOG_CACHE = "LOG";

    private static List<Date> clickCounter = new ArrayList<Date>();
    private static boolean LOG = true;
    private static boolean errorVisualLog = false;
    private static boolean canEnableLog = true;
    private static boolean log;
    private static ArrayList<String> allLogs = new ArrayList<>();

    public static void i(String tag, String string) {
        try {
            if (LOG){
                android.util.Log.i(tag, string);
                allLogs.add("I " + tag + " " + string);
            }
        }catch(Exception ex){
            // If can't log, don't break the app...
        }
    }

    public static void d(String tag, String string) {
        try {
            if (LOG){
                android.util.Log.d(tag, string);
                allLogs.add("D " + tag + " " + string);
            }
        }catch(Exception ex){
            // If can't log, don't break the app...
        }
    }

    public static void v(String tag, String string) {
        try {
            if (LOG){
                android.util.Log.v(tag, string);
            }
        }catch(Exception ex){
            // If can't log, don't break the app...
        }
    }

    public static void w(String tag, String string) {
        try {
            if (LOG){
                android.util.Log.w(tag, string);
                allLogs.add("W " + tag + " " + string);
            }
        }catch(Exception ex){
            // If can't log, don't break the app...
        }
    }

    public static void e(String tag, String string, Exception e) {
        try {
            if (LOG) {
                if (e == null) {
                    android.util.Log.e(tag, string);
                    allLogs.add("E " + tag + " " + string);
                } else {
                    android.util.Log.e(tag, string, e);
                    String stack = "";
                    if (e != null) {
                        stack = e.getMessage();

                        if (e.getStackTrace() != null) {
                            if (e.getStackTrace().length > 0) {
                                StackTraceElement traceLine = e.getStackTrace()[0];
                                stack += traceLine.toString();
                            }
                        }
                    }
                    allLogs.add("E " + tag + " " + string + " " + stack);
                }
            }
        }catch(Exception ex){
            // If can't log an exception, don't break the app...
        }
    }

    public static void e(String tag, String string) {
        e(tag, string, null);
    }

}
