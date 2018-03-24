package com.example.tanishqsaluja.beep.Receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.RadialGradient;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.tanishqsaluja.beep.MainActivity;
import com.example.tanishqsaluja.beep.R;

import java.util.Random;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.content.Context.VIBRATOR_SERVICE;

/**
 * Created by tanishqsaluja on 24/3/18.
 */

public class TaskReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("TEST","inside receive method");
        String title=intent.getStringExtra("title");
        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent= PendingIntent.getActivity(context, (int) System.currentTimeMillis(),i,0);
        Notification notification = new NotificationCompat.Builder(context, "test")
                .setContentTitle("Time for the medicine : "+title)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLights(Color.RED,3000,3000)
                .setCategory(Notification.CATEGORY_CALL)
                .setDefaults(Notification.FLAG_INSISTENT)
                .setOngoing(true)
                .setAutoCancel(false)
                .build();
        int number=new Random().nextInt();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(number, notification);
    }
}
