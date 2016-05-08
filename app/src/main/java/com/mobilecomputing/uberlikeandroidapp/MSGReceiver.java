package com.mobilecomputing.uberlikeandroidapp;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class MSGReceiver  extends GcmListenerService {

    NotificationCompat.Builder notification;
    NotificationManager manager;

    public MSGReceiver(){

    }
    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Toast.makeText(getApplicationContext(), "No", Toast.LENGTH_LONG).show();

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notification = new NotificationCompat.Builder(this).setContentTitle("Ahmed").setContentText("Hey !!!!!!").
                setAutoCancel(true)
                .setSound(defaultSound).setContentIntent(pendingIntent);
        manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(0, notification.build());
    }
}
