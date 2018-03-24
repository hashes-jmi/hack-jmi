package com.example.tanishqsaluja.beep;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.tanishqsaluja.beep.DB.Note;
import com.example.tanishqsaluja.beep.DB.NotesDB;
import com.example.tanishqsaluja.beep.Receiver.TaskReceiver;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by tanishqsaluja on 24/3/18.
 */

public class MedicineActivity extends AppCompatActivity {//implements SearchView.OnQueryTextListener{
    NotesDB notesDB;
    NotesAdapter notesAdapter;
    ArrayList<Note> arrayList;
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicine_activity);
        RecyclerView recyclerView=findViewById(R.id.rv);
        notesDB=new NotesDB(this);
        arrayList=new ArrayList<>();
        arrayList=notesDB.getAllNotes();
        Log.e("TEST",arrayList.size()+"");
        notesAdapter=new NotesAdapter(this,arrayList,notesDB);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(notesAdapter);
        View dialogView=getLayoutInflater().inflate(R.layout.dialog_layout,null,false);
        final EditText title = dialogView.findViewById(R.id.editTextTitle);
        final EditText description = dialogView.findViewById(R.id.editTextDescriotion);
        final TimePicker timePicker=dialogView.findViewById(R.id.time);

        final AlertDialog alertDialog=new AlertDialog.Builder(this)
                .setTitle("Schedule your task")
                .setCancelable(false)
                .setView(dialogView)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int hour=timePicker.getCurrentHour();
                        int minute=timePicker.getCurrentMinute();
                        int id= (int) System.currentTimeMillis();
                        if(title.getText().toString().equals("")){
                            Toast.makeText(MedicineActivity.this,"Enter the task's title",Toast.LENGTH_SHORT).show();
                        }else {
                            Note note = new Note(id, title.getText().toString(), description.getText().toString(), false, hour, minute);
                            notesDB.insertNote(note);
                            notesAdapter.addItem(note, title.getText().toString(), hour, minute);
                            //  clockAdapter.addItem(clock,title.getText().toString(),hour,minute);
                            setAlarm(timePicker, title.getText().toString(), id, notesDB);                       // String am=timePicker.get
                            title.setText("");
                            description.setText("");
                            update();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        FloatingActionButton floatingActionButton=findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
            }
        });

    }

    private void update() {
        arrayList.clear();
        arrayList.addAll(notesDB.getAllNotes());
        notesAdapter.notifyDataSetChanged();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.search);
        final android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(MedicineActivity.this);
        return true;
    }



    //The default methods that we have to implement since we are implementing SearchView interface
    @Override
    public boolean onQueryTextSubmit(String query) {
        notesAdapter.filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        notesAdapter.filter(newText);
        return false;
    }*/
    private void setAlarm(TimePicker timePicker,String title,Integer id,NotesDB ndb){
        alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent pendingIntent=new Intent(MedicineActivity.this,TaskReceiver.class);
        pendingIntent.putExtra("title",title);
        pendingIntent.putExtra("noteid",id);
        // pendingIntent.putExtra("notesdb", (Parcelable) ndb);
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
}
