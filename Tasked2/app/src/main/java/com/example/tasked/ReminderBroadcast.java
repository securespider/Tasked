package com.example.tasked;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class ReminderBroadcast extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String body = intent.getStringExtra("body");
        NotificationUtils notificationUtils = new NotificationUtils(context);
        NotificationCompat.Builder builder = notificationUtils.setNotification(title, body);
        notificationUtils.getManager().notify(420, builder.build());
    }
}