package com.example.aessa.androidserver;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.text.util.Linkify;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.aessa.androidserver.Server.ServerIntentService;


public class MainActivity extends ActionBarActivity
{
    private ServerIntentService mService;
    private boolean mBound;
    // ----->
    private TextView tvState, tvUrl;
    private ToggleButton tBtnState;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvState = (TextView) findViewById(R.id.tv_state);
        tvUrl = (TextView) findViewById(R.id.tv_url);
        tBtnState = (ToggleButton) findViewById(R.id.tbtn_state);
        tBtnState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                toggleService(isChecked);
            }
        });
        tBtnState.setChecked(Utils.isMyServiceRunning(this, ServerIntentService.class));
    }

    // ----------------------------->

    private void toggleService(boolean turnOn)
    {
        Context context = MainActivity.this;
        if(turnOn)
        {
            ServerIntentService.startServerService(context);
            // Bind to Service using service connection
            Intent intent = new Intent(context, ServerIntentService.class);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
        else
        {
            tvUrl.setText("");
            ServerIntentService.stopServerService(context);
            // unbind Service
            if(mBound)
            {
                unbindService(mConnection);
                mBound = false;
            }
        }
    }


    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service)
        {
            ServerIntentService.MyBinder binder = (ServerIntentService.MyBinder) service;
            mService = binder.getService();
            mBound = true;
            tvUrl.setText(mService.getUrl());
            Linkify.addLinks(tvUrl, Linkify.ALL);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0)
        {
            tvUrl.setText("");
            mService = null;
            mBound = false;
        }
    };

}
