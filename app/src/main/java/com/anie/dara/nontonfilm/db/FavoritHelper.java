package com.anie.dara.nontonfilm.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.anie.dara.nontonfilm.model.FilmItem;

import java.util.ArrayList;

public class FavoritHelper {

    private static String DATABASE_TABLE = DatabaseContract.favorit.TABLE_NAME;
    private Context context;
    private DatabaseHelper dbHelper;

    private SQLiteDatabase db;

    public FavoritHelper(Context context) {
        this.context = context;
    }

    public FavoritHelper open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }


    public ArrayList<FilmItem> getData() {
        ArrayList<FilmItem> arrayList = new ArrayList<FilmItem>();
        Cursor cursor = db.query(DATABASE_TABLE
                , null
                , null
                , null
                , null
                , null, DatabaseContract.favorit.ID + " DESC"
                , null);
        cursor.moveToFirst();
        FilmItem filmItem;
        if (cursor.getCount() > 0) {
            do {

                filmItem = new FilmItem();
                filmItem.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.favorit.ID)));
                filmItem.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.favorit.title)));
                filmItem.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.favorit.overview)));
                filmItem.setPoster_path(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.favorit.poster_path)));
                arrayList.add(filmItem);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }



    public long insert(int id, String title, String overview, String poster) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(DatabaseContract.favorit.ID, id);
        initialValues.put(DatabaseContract.favorit.title, title);
        initialValues.put(DatabaseContract.favorit.overview,overview);
        initialValues.put(DatabaseContract.favorit.poster_path,poster);
        return db.insert(DatabaseContract.favorit.TABLE_NAME, null, initialValues);
    }

    public boolean cariFav(int id){
        String query = "select * from " + DatabaseContract.favorit.TABLE_NAME + " WHERE " + DatabaseContract.favorit.ID + "=" + id ;
        Cursor cursor = db.rawQuery(query,new String[] { });
        if(cursor.getCount() >= 1){
            return true;
        }
        else{
            return false;
        }

    }



    public int delete(int id) {
        return db.delete(DatabaseContract.favorit.TABLE_NAME, DatabaseContract.favorit.ID + " = '" + id + "'", null);
    }

    public Cursor queryProvider() {
        return db.query(DatabaseContract.favorit.TABLE_NAME
                , null
                , null
                , null
                , null
                , null
                ,  DatabaseContract.favorit.ID + " DESC");
    }

    public long insertProvider(ContentValues initialValues) {
        return db.insert(DatabaseContract.favorit.TABLE_NAME, null, initialValues);
    }

    public int deleteProvider(String id) {
        return db.delete(DatabaseContract.favorit.TABLE_NAME, DatabaseContract.favorit.ID + " = ?", new String[]{id});
    }

    public int updateProvider(String id, ContentValues initialValues) {
        return db.update(DatabaseContract.favorit.TABLE_NAME, initialValues, DatabaseContract.favorit.ID + " = ?", new String[]{id});
    }



}
