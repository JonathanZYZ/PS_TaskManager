package com.myapplicationdev.android.ps_taskmanager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class ScheduledNotificationReceiver extends BroadcastReceiver {

    int reqCode = 12345;


    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getExtras().getString("name");
        String description = intent.getExtras().getString("description");
        DBHelper dbh = new DBHelper(context);
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new
                    NotificationChannel("default", "Default Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.setDescription("This is for default notification");
            notificationManager.createNotificationChannel(channel);
        }

        Intent i = new Intent(context,MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity
                ( context, reqCode, i,
                        PendingIntent.FLAG_CANCEL_CURRENT);
        //Big picture
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.gpang);
        // bigPicture
        NotificationCompat.BigPictureStyle bigPicture = new NotificationCompat.BigPictureStyle();
        bigPicture.bigPicture(bitmap);
        bigPicture.bigLargeIcon(null);
        bigPicture.setBigContentTitle("Task: " + name);
        bigPicture.setSummaryText("Description: " + description + "\n");

        // Build notification
        NotificationCompat.Builder builder = new
                NotificationCompat.Builder(context, "default");

        int num = dbh.getTasks().size();

        Log.d("TEST","TEST: "+dbh.getTasks().get(num-1).getTitle());

        builder.setContentTitle(dbh.getTasks().get(num-1).getTitle());
        builder.setContentText(dbh.getTasks().get(num-1).getDescription());
        builder.setSmallIcon(android.R.drawable.ic_dialog_info);
        builder.setContentIntent(pIntent);
        // bigPicture
        builder.setStyle(bigPicture);
        // LED
        builder.setLights(0xFFFF0000, 50, 50);
        // Sound Effect
        builder.setDefaults(Notification.DEFAULT_ALL);
        // Vibration
        builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        builder.setAutoCancel(true);

        Notification n = builder.build();
        notificationManager.notify(123, n);
    }
}