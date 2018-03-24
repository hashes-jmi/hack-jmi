package com.example.tanishqsaluja.beep.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by tanishqsaluja on 16/3/18.
 */

public class NotesDB extends SQLiteOpenHelper {

    //SETTING UP DATABASE


    public static final String DB_NAME="notesdb";
    public static final int DB_VERSION=5;
    public static final String DROP=" DROP TABLE IF EXISTS "+ TableContract.TABLE_NAME;

    public NotesDB(Context context) {
        super(context, DB_NAME, null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL(TableContract.CREATE_TABLE_ALTER);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public ArrayList<Note> getAllNotes(){
        Cursor c=getReadableDatabase().query(
                TableContract.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        ArrayList<Note> notes=new ArrayList<>();
        //convert cursor to arraylist
        while(c.moveToNext()){
            int id=c.getInt(0);
            String title=c.getString(1);
            String desc=c.getString(2);
            Boolean isDone=c.getInt(3)==1;
            Integer hour=c.getInt(4);
            Integer minute=c.getInt(5);
            Note note=new Note(id,title,desc,isDone,hour,minute);
            notes.add(note);
        }
        c.close();//essential step
        return notes;
    }

    public Note getNoteFromId(int id){
        SQLiteDatabase sqLiteDatabase=getReadableDatabase();
        Cursor c=sqLiteDatabase.query(
                TableContract.TABLE_NAME,
                null,
                TableContract.ID=" =? ",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );
        c.moveToNext();
        int resId=c.getInt(0);
        String title=c.getString(1);
        String desc=c.getString(2);
        Boolean isDone=c.getInt(3)==1;
        Integer hour=c.getInt(4);
        Integer minute=c.getInt(5);
        Note note=new Note(resId,title,desc,isDone,hour,minute);
        c.close();

        return note;
    }

    public int deleteRow(String title){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        int count=sqLiteDatabase.delete(TableContract.TABLE_NAME, TableContract.TITLE+"=?", new String[]{title});
        return count;
    }

    public void insertNote(Note note){
        ContentValues contentValues=new ContentValues();
        contentValues.put(TableContract.ID,note.getId());
        contentValues.put(TableContract.TITLE,note.getTitle());
        contentValues.put(TableContract.DESCRIPTION,note.getDesc());
        contentValues.put(TableContract.ISDONE,note.getIsdone());
        contentValues.put(TableContract.HOUR,note.getHour());
        contentValues.put(TableContract.MINUTE,note.getMinute());
        getWritableDatabase().insert(TableContract.TABLE_NAME,null,contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try{
            db.execSQL(DROP);
            onCreate(db);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
