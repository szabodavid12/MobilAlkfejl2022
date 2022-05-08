package com.example.theaterapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHandler {
    private static final String CHANNEL_ID = "theaterapp_notification_channel";
    private static final int NOTIFICATION_ID = 1;

    private NotificationManager mNotificationManager;
    private Context mContext;

    public NotificationHandler(Context context) {
        this.mContext = context;
        this.mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        createChannel();
    }


    private void createChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "TheaterApp Notification", NotificationManager.IMPORTANCE_DEFAULT);

        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(Color.RED);
        channel.setDescription("Nofification from TheaterApp application");
        this.mNotificationManager.createNotificationChannel(channel);
    }

    public void send(String message) {


        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setContentTitle("Sikeres jegyfoglalás!")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_theater);

        this.mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public void cancel(){
        this.mNotificationManager.cancel(NOTIFICATION_ID);
    }

}
