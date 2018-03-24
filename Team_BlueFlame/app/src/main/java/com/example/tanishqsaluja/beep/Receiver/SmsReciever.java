package com.example.tanishqsaluja.beep.Receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.tanishqsaluja.beep.BeeperService;
import com.example.tanishqsaluja.beep.MainActivity;
import com.example.tanishqsaluja.beep.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by ansh on 11/2/18.
 */

/**
 * Created by ansh on 10/2/18.
 */

public class SmsReciever extends BroadcastReceiver {
    public static final String TAG = ">>BROADCAST>>MANAGER>>";
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";



    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(SMS_RECEIVED)) {
            Log.e(TAG, "onReceive: message recieved!" );
            MessageCustomStructure recievedMessage=getRecievedMessage(context,intent);

            if(recievedMessage!=null && isMessageUseful(recievedMessage)) {
                startUploaderService(context,recievedMessage);

            }

        }
    }
    private MessageCustomStructure getRecievedMessage(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            // get sms objects
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus.length == 0) {
                return null;
            }
            // large message might be broken into many
            SmsMessage[] messages = new SmsMessage[pdus.length];
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < pdus.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                sb.append(messages[i].getMessageBody());
            }
            String sender = messages[0].getOriginatingAddress();
            String message = sb.toString();
            return new MessageCustomStructure(sender,message);

        }
        return  null;

    }

    public boolean isMessageUseful(MessageCustomStructure recievedMessage) {
        Boolean isValid=recievedMessage.getMessage().startsWith("CLEnT"); //change its working here
        Log.e(TAG, "isMessageUseful: "+isValid );
        return isValid;

    }


    private void startUploaderService(Context context, MessageCustomStructure recievedMessage) {


        Intent i=new Intent(context,BeeperService.class);

        i.putExtra(BeeperService.MESSAGE_OBJ,recievedMessage);
        context.startService(i);

    }

    private void startNotification(Context context, MessageCustomStructure recievedMessage) {
             Log.e("TEST","inside receive method");


            Intent i = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent= PendingIntent.getActivity(context, (int) System.currentTimeMillis(),i,0);
            Notification notification = new NotificationCompat.Builder(context, "test")
                    .setContentTitle(recievedMessage.getMessage())
                    .setVibrate(new long[] { 0, 5000,1000,5000,1000 })
                    .setContentIntent(pendingIntent)

                    .setPriority(Notification.PRIORITY_MAX)
                    .setAutoCancel(false)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setLights(Color.RED,3000,3000)
                    .setOngoing(true)
                    .build();
            notification.sound = Uri.parse("android.resource://"+context.getPackageName()+"/"+R.raw.ring);


            notification.defaults|= Notification.DEFAULT_LIGHTS;
        notification.defaults|= Notification.DEFAULT_VIBRATE;
        notification.defaults|= Notification.FLAG_NO_CLEAR;


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(123, notification);
        }


    }



}
