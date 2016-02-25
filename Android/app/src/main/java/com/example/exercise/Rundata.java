package com.example.exercise;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Rundata extends SQLiteOpenHelper {


    public final static String RUN_ID = "id";
    public final static String RUN_USER = "user";
    public final static String RUN_TIME = "time";
    public final static String RUN_SEQ = "seq";
    public final static String RUN_LONGITUDE = "longitude";
    public final static String RUN_LATITUDE = "latitude";

    private  String TABLE_NAME="Run_table";
    public static final String CREATE_RUN = "create table Run_table ("
            + "id integer primary key autoincrement, "
            + "user text, "
            + "time text, "
            + "seq integer, "
            + "longitude real, "
            + "latitude real)";

    private Context mContext;

    public Rundata(Context context, String name, CursorFactory factory, int version)
    {
        super(context,name,factory,version);
        mContext = context;
    }

    public void onCreate(SQLiteDatabase db) {
        Log.d("myDB", CREATE_RUN);
        db.execSQL(CREATE_RUN);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    public long insert(String userName,String time,int seq,double longitude,double latitude)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(RUN_USER, userName);
        cv.put(RUN_TIME, time);
        cv.put(RUN_SEQ, seq);
        cv.put(RUN_LONGITUDE, longitude);
        cv.put(RUN_LATITUDE, latitude);
        Log.d("myDB", "before insert");
        long row = db.insert(TABLE_NAME, null, cv);

        return row;
    }
    public Cursor select() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db
                .query(TABLE_NAME, null, null, null, null, null, null);
        return cursor;
    }

    public void update(int id, String userName,String time,int seq,double longitude,double latitude)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = RUN_ID + " = ?";
        String[] whereValue = { Integer.toString(id) };

        ContentValues cv = new ContentValues();
        cv.put(RUN_USER, userName);
        cv.put(RUN_TIME, time);
        cv.put(RUN_SEQ, seq);
        cv.put(RUN_LONGITUDE, longitude);
        cv.put(RUN_LATITUDE, latitude);
        db.update(TABLE_NAME, cv, where, whereValue);
    }
}
