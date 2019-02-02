package com.anie.dara.nontonfilm.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "dbmovie";

    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_FAVORIT = String.format(
            "CREATE TABLE " + DatabaseContract.favorit.TABLE_NAME + " (" +
                    DatabaseContract.favorit.ID + " INTEGER PRIMARY KEY," +
                    DatabaseContract.favorit.title + " TEXT," +
                    DatabaseContract.favorit.overview + " TEXT," +
                    DatabaseContract.favorit.poster_path + " TEXT)"
    );


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_FAVORIT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.favorit.TABLE_NAME);
        onCreate(db);
    }


}
