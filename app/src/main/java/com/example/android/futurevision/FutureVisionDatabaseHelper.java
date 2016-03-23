package com.example.android.futurevision;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;


class FutureVisionDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "FutureVision";
    private static final int DB_VERSION = 1;
    public static final Uri URI_TABLE = Uri.parse("content://com.example.android.futurevision.FutureVision/DAY_GOALS");
    public static final Uri URI_TABLE_MONTH = Uri.parse("content://com.example.android.futurevision.FutureVision/MONTH_GOALS");
    public static final Uri URI_TABLE_YEAR = Uri.parse("content://com.example.android.futurevision.FutureVision/YEAR_GOALS");
    public FutureVisionDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE DAY_GOALS ("
                +"_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "GOAL TEXT);");
        db.execSQL("CREATE TABLE MONTH_GOALS ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "GOAL TEXT);");
        db.execSQL("CREATE TABLE YEAR_GOALS ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "GOAL TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
