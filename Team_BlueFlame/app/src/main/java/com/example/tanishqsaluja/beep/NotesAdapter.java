package com.example.tanishqsaluja.beep;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tanishqsaluja.beep.DB.Note;
import com.example.tanishqsaluja.beep.DB.NotesDB;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.content.ContentValues.TAG;

/**
 * Created by tanishqsaluja on 16/3/18.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesHolder> {
    ArrayList<Note> arrayList,arrayListCopy;
    Context context;
    String title;int hour,minutes;Boolean color;
    NotesDB ndb;

    NotesAdapter(Context c, ArrayList<Note> list, NotesDB notesDB){
        this.context=c;
        this.arrayList=list;
        this.ndb=notesDB;
        arrayListCopy = new ArrayList<>(arrayList); // create a new List that contains all the elements of `list`.
    }

    public void addItem(Note item,String title,int hour,int minutes){
        arrayList.add(item);
        arrayListCopy.add(item);
        this.hour=hour;
        this.minutes=minutes;
        this.title=title;
    }

    @Override
    public NotesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NotesHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row,parent,false));
    }

    @Override
    public void onBindViewHolder(final NotesHolder holder, int position) {
        final Note note=arrayList.get(position);
        holder.title.setText(note.getTitle());
        holder.time.setText(note.getHour()+":"+note.getMinute());


        if(note.getHour()>12 && note.getMinute()<10){
            holder.time.setText((note.getHour()-12)+":0"+note.getMinute()+" PM");
        }else if(note.getHour()>12 && note.getMinute()>=10){
            holder.time.setText((note.getHour()-12)+":"+note.getMinute()+" PM");
        }else if(note.getHour()<=12 && note.getMinute()<10){
            holder.time.setText(note.getHour()+":0"+note.getMinute()+" AM");
        }else{
            holder.time.setText(note.getHour()+":"+note.getMinute()+" AM");
        }


        Calendar c = Calendar.getInstance();
        SimpleDateFormat df;

        df = new SimpleDateFormat("kk:mm");
        String currTime = df.format(c.getTime());

        String check=""+note.getHour()+":"+note.getMinute();
        Log.e(TAG, "onBindViewHolder: check:"+check );
        Log.e(TAG, "onBindViewHolder: formattedTime:"+currTime );
        try {

            if(df.parse(currTime).compareTo(df.parse(check))>=0) {
                holder.time.setTextColor(Color.GRAY);
                holder.title.setTextColor(Color.GRAY);
            }

            else{

                holder.time.setTextColor(Color.BLACK);
                holder.title.setTextColor(Color.BLACK);

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        holder.title.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ndb.deleteRow(holder.title.getText().toString());
                arrayList.remove(holder.getAdapterPosition());
                arrayListCopy.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
                return true;
            }
        });
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,DescActivity.class);
                intent.putExtra("desc",note.getDesc());
                context.startActivity(intent);
            }
        });
    }

    //We here define the filter method which contains our logic for searching
    public void filter(String text){
        arrayList.clear();
        if(TextUtils.isEmpty(text)){
            arrayList.addAll(arrayListCopy);
        }
        else{
            text=text.toLowerCase();
            for(Note note:arrayListCopy){
                if(note.getTitle().toLowerCase().contains(text)){
                    arrayList.add(note);
                }
            }
        }
        notifyDataSetChanged();//we update the data set attached with our Adapter
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class NotesHolder extends RecyclerView.ViewHolder {
        TextView title,time,done;
        public NotesHolder(View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
          //  title=itemView.findViewById(R.id.title);
            time=itemView.findViewById(R.id.time);
            done=itemView.findViewById(R.id.done);
            Log.e("TEST","VIEWHOLDER");
        }
    }
}