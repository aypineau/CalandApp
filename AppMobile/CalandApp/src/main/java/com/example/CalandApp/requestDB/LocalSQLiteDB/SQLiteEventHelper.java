package com.example.CalandApp.requestDB.LocalSQLiteDB;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class SQLiteEventHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "EventLibrary.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "event";
    private static final String COLUMN_ID = "event_id";
    private static final String COLUMN_ORGANIZER = "organizer_id";
    private static final String COLUMN_GROUP = "group_id";
    private static final String COLUMN_MOMENT = "event_moment";
    private static final String COLUMN_DURATION = "event_duration";
    private static final String COLUMN_DESCRIPTION = "description";


    public SQLiteEventHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ORGANIZER + " INTEGER, " +
                COLUMN_GROUP + " INTEGER, " +
                COLUMN_MOMENT + " TEXT, " +
                COLUMN_DURATION + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT);";
        db.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addEvent(int organizer_id, int group_id, String event_moment, String event_duration, String description){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_ORGANIZER, organizer_id);
        cv.put(COLUMN_GROUP, group_id);
        cv.put(COLUMN_MOMENT, event_moment);
        cv.put(COLUMN_DURATION, event_duration);
        cv.put(COLUMN_DESCRIPTION, description);

        long result = db.insert(TABLE_NAME,null, cv);
        if(result == -1){
            Log.i("SQLiteEventHelper", "Failed to add event");
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Log.i("SQLiteEventHelper", "Event added Successfully");
        }
    }

    public void addEvent(int event_id, int organizer_id, int group_id, String event_moment, String event_duration, String description){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_ID, event_id);
        cv.put(COLUMN_GROUP, group_id);
        cv.put(COLUMN_ORGANIZER, organizer_id);
        cv.put(COLUMN_MOMENT, event_moment);
        cv.put(COLUMN_DURATION, event_duration);
        cv.put(COLUMN_DESCRIPTION, description);

        long result = db.insert(TABLE_NAME,null, cv);
        if(result == -1){
            Log.i("SQLiteEventHelper", "Failed to add event");
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Log.i("SQLiteEventHelper", "Event added Successfully");
        }
    }

    public Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "group_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

}