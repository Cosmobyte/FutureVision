package com.example.android.futurevision;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;


public class NotifyService extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {


            Intent alarmIntent = new Intent(context.getApplicationContext(), MainActivity.class);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification n = new NotificationCompat.Builder(context)
                    .setContentTitle("Daily goals")
                    .setContentText("Verify your daily list of goals")
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .addAction(0, "GO TO THE LIST", pendingIntent)
                    .build();

            notificationManager.notify(1, n);

        }
    }

