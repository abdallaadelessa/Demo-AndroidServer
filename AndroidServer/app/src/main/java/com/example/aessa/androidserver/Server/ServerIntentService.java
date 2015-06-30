package com.example.aessa.androidserver.Server;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.example.aessa.androidserver.MainActivity;
import com.example.aessa.androidserver.R;
import com.example.aessa.androidserver.Utils;

import java.io.IOException;

public class ServerIntentService extends Service
{
    private static final int NOTIFICATION_ID = 25;
    private static final int PORT = 10252;
    private static final String KEY_SHOW_NOTIFICATION = "key_show_notification";
    // ---->
    private final IBinder mBinder = new MyBinder();
    private HttpLocalServer server;
    // ---->
    private int startCommandCounter, numOfRunsCounter;
    private String url;

    public static void startServerService(Context context)
    {
        Intent intent = new Intent(context, ServerIntentService.class);
        intent.putExtra(KEY_SHOW_NOTIFICATION, true);
        context.startService(intent);
    }

    public static void stopServerService(Context context)
    {
        Intent intent = new Intent(context, ServerIntentService.class);
        intent.putExtra(KEY_SHOW_NOTIFICATION, false);
        context.stopService(intent);
    }

    // ------------------------------------------>

    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if(intent != null && intent.getExtras() != null && intent.hasExtra(KEY_SHOW_NOTIFICATION))
        {
            if(intent.getBooleanExtra(KEY_SHOW_NOTIFICATION, false))
            {
                startForeground(NOTIFICATION_ID, prepareNotificationObject());
            }
            else
            {
                stopForeground(true);
            }
        }
        if(server == null || !server.isAlive())
        {
            Utils.log("Running Server Counter: " + (++numOfRunsCounter));
            try
            {
                if(server != null)
                {
                    server.closeAllConnections();
                }
                server = new HttpLocalServer(PORT);
                server.start();
                printUrl();
            }
            catch(IOException e)
            {
                Utils.log(e.getMessage());
            }
        }
        Utils.log("Server onStartCommand Counter: " + (++startCommandCounter));
        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        stopForeground(true);
        if(server != null)
        {
            server.stop();
        }
        super.onDestroy();
    }

    // ------------------------------------------>

    public class MyBinder extends Binder
    {
        public ServerIntentService getService()
        {
            return ServerIntentService.this;
        }
    }

    private Notification prepareNotificationObject()
    {
//        Uri defaultUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Mobile Banking Server");
        builder.setContentText("simulating mobile banking local server");
        builder.setContentIntent(contentIntent);
//        builder.setSound(defaultUri);
        return builder.build();
    }

    private void printUrl()
    {
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
        final String formatedIpAddress = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
        url = "http://" + formatedIpAddress + ":" + PORT;
        Utils.log("Please access! " + url);
    }

    public String getUrl()
    {
        return url;
    }
}
