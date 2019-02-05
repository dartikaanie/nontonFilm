package com.anie.dara.nontonfilm.Provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.anie.dara.nontonfilm.db.DatabaseContract;
import com.anie.dara.nontonfilm.db.FavoritHelper;

public class FavoritProvider extends ContentProvider {

    private static final int FAV = 1;
    private static final int FAV_ID = 2;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private FavoritHelper favoritHelper;

    static {
        // content://com.dicoding.picodiploma.mynotesapp/note
        sUriMatcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.favorit.TABLE_NAME, FAV);
        // content://com.dicoding.picodiploma.mynotesapp/note/id
        sUriMatcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.favorit.TABLE_NAME + "/#", FAV_ID);
    }

    @Override
    public boolean onCreate() {
        favoritHelper = new FavoritHelper(getContext());
        favoritHelper.open();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        favoritHelper.open();
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case FAV:
                cursor = favoritHelper.queryProvider();
                break;
            default:
                cursor = null;
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

            favoritHelper.open();
        long added;
        switch (sUriMatcher.match(uri)) {
            case FAV:
                added = favoritHelper.insertProvider(contentValues);
                break;
            default:
                added = 0;
                break;
        }
        if(getContext() != null) {
            getContext().getContentResolver().notifyChange(DatabaseContract.favorit.CONTENT_URI, null);
        }


        return Uri.parse(DatabaseContract.favorit.CONTENT_URI + "/" + added);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        favoritHelper.open();
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case FAV_ID:
                deleted = favoritHelper.deleteProvider(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }

        if(getContext() != null) {
            getContext().getContentResolver().notifyChange(DatabaseContract.favorit.CONTENT_URI, null);
        }

        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        favoritHelper.open();
        int updated;
        switch (sUriMatcher.match(uri)) {
            case FAV_ID:
                updated = favoritHelper.updateProvider(uri.getLastPathSegment(), contentValues);
                break;
            default:
                updated = 0;
                break;
        }
        if(getContext() != null) {
            getContext().getContentResolver().notifyChange(DatabaseContract.favorit.CONTENT_URI, null);
        }

        return updated;
    }


}
