package com.example.hana.notifyme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Array;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "notifyme.db";
    public static final String TABLE_MYALARMS = "myalarms";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ALARMID = "_alarmid";
    public static final String COLUMN_INPUTSTRING = "_inputString";
    public static final String COLUMN_IMAGE = "_image";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " +TABLE_MYALARMS + "(" +
             //   COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ALARMID + " INTEGER PRIMARY KEY, " +
                COLUMN_INPUTSTRING + " TEXT, " +
                COLUMN_IMAGE + " TEXT " +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MYALARMS);
        onCreate(db);
    }

    //Method to add new entry in Table
    public void addAlarm(Alarms alarm){

        ContentValues values = new ContentValues();
        values.put(COLUMN_ALARMID,alarm.get_alarmID());
        values.put(COLUMN_INPUTSTRING,alarm.get_inputString());
        values.put(COLUMN_IMAGE, alarm.get_image());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_MYALARMS, null, values);
        db.close();
    }

    public void deleteAlarm(Integer alarmID) {
        int id;
        SQLiteDatabase db = getWritableDatabase();
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_MYINSTA);
        //Toast toast = Toast.makeText(context,"DB deleted",Toast.LENGTH_LONG);
        //toast.show();
        //onUpgrade(db,1,2);


        //timestamp=diaryEntry.get_timestamp();
        db.execSQL("DELETE FROM " + TABLE_MYALARMS + " WHERE " + COLUMN_ALARMID + "=\"" + alarmID + ";");
    }

    public List readDatabase(Integer alarmID){
        List Alarms = new ArrayList<Alarms>();
        Alarms data;
        String query = "SELECT * FROM " + TABLE_MYALARMS + " WHERE "+ COLUMN_ALARMID + "=\"" + alarmID + ";";
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            data = new Alarms();
            data.set_alarmID(c.getInt(1));
            data.set_inputString(c.getString(2));
            data.set_image(c.getString(3));
            Alarms.add(data);
            c.moveToNext();
        }
        return Alarms;
    }
    public List readDatabase(){
        List Alarms = new ArrayList<Alarms>();
        Alarms data;
        String query = "SELECT * FROM " + TABLE_MYALARMS + " WHERE 1;";
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            data = new Alarms();
            data.set_alarmID(c.getInt(1));
            data.set_inputString(c.getString(2));
            data.set_image(c.getString(3));
            Alarms.add(data);
            c.moveToNext();
        }
        return Alarms;
    }
}
