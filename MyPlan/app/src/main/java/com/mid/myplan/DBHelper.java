package com.mid.myplan;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by OptimusV5 on 2014/12/8.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "plans.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS todos"
                + "(id INTEGER PRIMARY KEY AUTOINCREMENT,subject TEXT, class INTEGER, time TEXT, content TEXT, alarm INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS done"
                + "(id INTEGER PRIMARY KEY AUTOINCREMENT,subject TEXT, class INTEGER, time TEXT, content TEXT, alarm INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
