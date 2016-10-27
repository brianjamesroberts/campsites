package com.unfairtools.campsites.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by brianroberts on 10/25/16.
 */

public class OpenHelper extends SQLiteOpenHelper {

    private static OpenHelper mInstance = null;

    private static final String DATABASE_NAME = "DBName";

    private static final int DATABASE_VERSION = 2;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table MyEmployees( _id integer primary key,name text not null);";

    private OpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public static OpenHelper getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new OpenHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    // Method is called during an upgrade of the database,
    @Override
    public void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion){
        database.execSQL("DROP TABLE IF EXISTS MyEmployees");
        onCreate(database);
    }
}

