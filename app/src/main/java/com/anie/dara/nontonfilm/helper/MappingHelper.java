package com.anie.dara.nontonfilm.helper;

import android.database.Cursor;

import com.anie.dara.nontonfilm.db.DatabaseContract;
import com.anie.dara.nontonfilm.model.FilmItem;

import java.util.ArrayList;

public class MappingHelper {

    public static ArrayList<FilmItem> mapCursorToArrayList(Cursor filmCursor) {
        ArrayList<FilmItem> filmList = new ArrayList<>();
        while (filmCursor.moveToNext()) {
            int id = filmCursor.getInt(filmCursor.getColumnIndexOrThrow(DatabaseContract.favorit.ID));
            String title = filmCursor.getString(filmCursor.getColumnIndexOrThrow(DatabaseContract.favorit.title));
            String overview = filmCursor.getString(filmCursor.getColumnIndexOrThrow(DatabaseContract.favorit.overview));
            String poster = filmCursor.getString(filmCursor.getColumnIndexOrThrow(DatabaseContract.favorit.poster_path));
            filmList.add(new FilmItem(id, title, overview, poster));
        }
        return filmList;
    }

}
