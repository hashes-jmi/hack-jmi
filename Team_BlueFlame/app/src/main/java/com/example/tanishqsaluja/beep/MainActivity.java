package com.example.tanishqsaluja.beep;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String TAG ="WEtheBest" ;
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        Button button= findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ConfigurePButtonActivity.class));
            }
        });


        if(isServiceRunning(BeeperService.class)){
            Log.e(TAG, "onCreate:isserviceRunning:true " );
            stopService(new Intent(this,BeeperService.class));
        }



    }

    public void on_rms_click(View view) {
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.info){
            // do something
            Intent intent=new Intent(MainActivity.this,InfoAct.class);
            startActivity(intent);
        }
        if(id==R.id.scheduler){
            Intent intent=new Intent(MainActivity.this,MedicineActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isServiceRunning(Class<?> serviceClass){
        ActivityManager manager= (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo infos:manager.getRunningServices(Integer.MAX_VALUE)){
            if(serviceClass.getName().equals(infos.service.getClassName())){
                return true;
            }
        }

        return  false;

    }


}

/*

        */
/*AudioManager am = (AudioManager)getSystemService(this.AUDIO_SERVICE);
        if(am.isMusicActive()){

        }*//*

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        if(notificationManager!=null){
            notificationManager.cancelAll();
        }
        //notificationManager.notify(123, notification);
        FloatingActionButton fab=findViewById(R.id.fab);
        View dialogView=getLayoutInflater().inflate(R.layout.alert_dialog,null,false);
        final TimePicker timePicker=dialogView.findViewById(R.id.time);

        final AlertDialog alertDialog=new AlertDialog.Builder(this)
                .setTitle("Schedule your task")
                .setCancelable(false)
                .setView(dialogView)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                         setAlarm(timePicker);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
            }
        });
    }
    private void setAlarm(TimePicker timePicker){
        alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent pendingIntent=new Intent(MainActivity.this,MyReceiver.class);
        Calendar calendar=Calendar.getInstance();
        if(Build.VERSION.SDK_INT>=23) {
            calendar.set(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    timePicker.getHour(),
                    timePicker.getMinute(),
                    0
            );
        }

        else{
            calendar.set(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    timePicker.getCurrentHour(),
                    timePicker.getCurrentMinute(),
                    0
            );
        }

        alarmIntent = PendingIntent.getBroadcast(getBaseContext(),1234,
                pendingIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC,calendar.getTimeInMillis(),alarmIntent);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.settings){
            Intent intent=new Intent(MainActivity.this,MedicineActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
*/
