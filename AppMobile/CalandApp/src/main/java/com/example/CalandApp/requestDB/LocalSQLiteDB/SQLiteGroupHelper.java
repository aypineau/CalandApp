package com.example.CalandApp.requestDB.LocalSQLiteDB;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;

public class SQLiteGroupHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "GroupLibrary.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "grp";
    private static final String COLUMN_ID = "group_id";
    private static final String COLUMN_NAME = "group_name";

    public SQLiteGroupHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT );";
        db.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addGroup(String group_name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, group_name);
        long result = db.insert(TABLE_NAME,null, cv);
        if(result == -1){
            Log.i("SQLiteGroupHelper", "Failed to add group");
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Log.i("SQLiteGroupHelper", "Group added Successfully");
        }
    }

    public void addGroup(int group_id, String group_name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_ID, group_id);
        cv.put(COLUMN_NAME, group_name);
        long result = db.insert(TABLE_NAME,null, cv);
        if(result == -1){
            Log.i("SQLiteGroupHelper", "Failed to add group");
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Log.i("SQLiteGroupHelper", "Group added Successfully");
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

    public void updateData(String row_id, String group_name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, group_name);

        long result = db.update(TABLE_NAME, cv, "group_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }

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