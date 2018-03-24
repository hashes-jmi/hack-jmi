package com.example.tanishqsaluja.beep;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.tanishqsaluja.beep.Receiver.MessageCustomStructure;

public class BeeperService extends IntentService {
    private static final String TAG = ">>UPLOADER>>TASK>>";
    public static final String MESSAGE_OBJ = "messageObject";
    public static final int FOREGROUND_ID = 123;


    public BeeperService(String name) {
        super(name);
    }

    public BeeperService() {
        super("NoNameProvided");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        MediaPlayer player;



        MessageCustomStructure msgObj = null;
        if (intent != null) {
            msgObj = intent.getParcelableExtra(MESSAGE_OBJ);
        }

        startForeground(FOREGROUND_ID, getNotificationObj(msgObj != null ? msgObj.getMessage() : "message"));

        player = MediaPlayer.create(getApplicationContext(), R.raw.ring);
        player.start();


        }


    private Notification getNotificationObj(String message) {
        Log.e("TEST","get notification obj,msg:"+message);


        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent= PendingIntent.getActivity(getApplicationContext(), (int) System.currentTimeMillis(),i,0);
        Notification notification = new NotificationCompat.Builder(getApplicationContext(), "test")
                .setContentTitle(message)
                .setVibrate(new long[] { 0, 5000,1000,5000,1000 })
                .setContentIntent(pendingIntent)

                .setPriority(Notification.PRIORITY_MAX)
                .setAutoCancel(false)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLights(Color.RED,3000,3000)
                .setOngoing(true)
                .build();

        notification.defaults|= Notification.DEFAULT_LIGHTS;
        notification.defaults|= Notification.DEFAULT_VIBRATE;
        notification.defaults|= Notification.FLAG_NO_CLEAR;
        NotificationManager manager= (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        manager.notify(888,notification);


       return notification;
    }

    @Override
    public void onDestroy() {
        NotificationManager manager= (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        if(manager!=null){
            manager.cancelAll();
        }


        super.onDestroy();
    }
}
