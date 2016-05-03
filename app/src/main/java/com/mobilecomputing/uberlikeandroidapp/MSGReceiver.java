package com.mobilecomputing.uberlikeandroidapp;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class MSGReceiver  extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        Bundle extras = intent.getExtras();
        Intent msgrcv = new Intent("Msg");
        msgrcv.putExtra("msg", "Ahmed");
        msgrcv.putExtra("fromu", "says");
        msgrcv.putExtra("fromname", "Hello");


        LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
        ComponentName comp = new ComponentName(context.getPackageName(),MSGService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}
