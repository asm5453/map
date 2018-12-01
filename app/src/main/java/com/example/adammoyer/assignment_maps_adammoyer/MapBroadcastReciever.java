package com.example.adammoyer.assignment_maps_adammoyer;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

@TargetApi(26)
public class MapBroadcastReciever extends BroadcastReceiver {
    NotificationManager notificationManager;
    Notification.Builder builder;
    public static final String EXTRA_LATITUDE = "LATITUDE";
    public static final String EXTRA_LONGITUDE = "LONGITUDE";
    public static final String MAP_LOCATION = "LOCATION";

    public static final int CHANNEL_ID = 1;
    public static final String CHANNEL_NAME = "MAPS";
    public static final int CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;
    public static final String CHANNEL_DESCRIPTION = "BROADCAST MAP CHANNEL";

    @Override
    public void onReceive(Context context, Intent intent) {
        Double latitude = intent.getDoubleExtra(EXTRA_LATITUDE, Double.NaN);
        Double longitude = intent.getDoubleExtra(EXTRA_LONGITUDE, Double.NaN);
        String location = intent.getStringExtra(MAP_LOCATION);

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new Notification.Builder(context);

        builder.setContentTitle(location);
        builder.setContentText(Double.isNaN(latitude) || Double.isNaN(longitude)
                ? "Lcation Unknown" :
                "Located at coordinates (lat, lng): "+
                        latitude+", "+longitude);
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_NAME, CHANNEL_DESCRIPTION, CHANNEL_IMPORTANCE);
        notificationChannel.setDescription(CHANNEL_DESCRIPTION);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.BLUE);
        notificationChannel.enableVibration(true);
        notificationChannel.setShowBadge(true);
        notificationManager.createNotificationChannel(notificationChannel);

        notificationManager.notify(CHANNEL_ID, builder.build());
    }


}
