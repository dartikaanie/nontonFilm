package com.anie.dara.nontonfilm.db;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String AUTHORITY = "com.anie.dara.nontonfilm";
    public static final String SCHEME = "content";

    private DatabaseContract(){}

    public static class favorit implements BaseColumns {
        public static final String TABLE_NAME = "favorit";
        public static final String ID = "id";
        public static final String title= "title";
        public static final String overview= "overview";
        public static final String poster_path= "poster_path";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();

    }


    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }
}
