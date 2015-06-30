package com.example.aessa.androidserver;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

/**
 * Created by aessa on 6/28/2015.
 */
public class Utils
{

    public static final String DEBUG = "DEBUG";

    public static void log(String message)
    {
        Log.i(DEBUG, message);
    }

    public static boolean isMyServiceRunning(Context cxt, Class<?> serviceClass)
    {
        ActivityManager manager = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if(serviceClass.getName().equals(service.service.getClassName()))
            {
                return true;
            }
        }
        return false;
    }
}
