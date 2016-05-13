package com.mobilecomputing.uberlikeandroidapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONException;
import org.json.JSONObject;

public class GCMListenerService extends GcmListenerService {
    public GCMListenerService() {

    }

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        try {
            handleNotification(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void handleNotification(Bundle s) throws JSONException {

        if(s.getString("title").equalsIgnoreCase("updateLocation")) {
            Intent i = new Intent("Data_GCM");
            String data = s.getString("driver_location_data");
            JSONObject json = new JSONObject(data);

            i.putExtra("driver_id", json.getString("driver_id"));
            i.putExtra("driver_data", json.getJSONObject("driver_data").getString("ay"));
            sendBroadcast(i);
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle("Ta");
        notificationBuilder.setContentText("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        notificationBuilder.setSound(defaultSoundUri);
        notificationBuilder.setAutoCancel(false);
        notificationBuilder.setSmallIcon(R.drawable.cast_ic_notification_1);
        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
