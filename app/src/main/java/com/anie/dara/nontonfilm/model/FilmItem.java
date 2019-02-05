package com.anie.dara.nontonfilm.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.anie.dara.nontonfilm.db.DatabaseContract;

import java.util.ArrayList;

import static com.anie.dara.nontonfilm.db.DatabaseContract.getColumnInt;
import static com.anie.dara.nontonfilm.db.DatabaseContract.getColumnString;

public class FilmItem implements Parcelable {
    private int id;
    private String title;
    private String overview;
    private  double vote_average;
    private  String release_date;
    private   String poster_path;
    private   String original_language;
    private   String tagline;
    private   Integer runtime;
    private   ArrayList<Genre> genres;



    public String getTagline() {
        if(tagline==null){
            tagline="unavaiable";
        }
        return tagline;
    }

    public Integer getRuntime() {
        if(this.runtime == null){
            runtime = 0;
        }
        return runtime;
    }


    public ArrayList<Genre> getGenres() {
        return genres;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getVote_average() {
        return vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOriginal_language() {
        return original_language;
    }



    public FilmItem() {
    }

    public FilmItem(int id, String title, String overview, String poster_path) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.poster_path = poster_path;
    }

    public FilmItem(Cursor cursor) {
        this.id = getColumnInt(cursor, DatabaseContract.favorit.ID);
        this.title = getColumnString(cursor, DatabaseContract.favorit.title);
        this.overview = getColumnString(cursor, DatabaseContract.favorit.overview);
        this.poster_path = getColumnString(cursor, DatabaseContract.favorit.poster_path);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.overview);
        dest.writeDouble(this.vote_average);
        dest.writeString(this.release_date);
        dest.writeString(this.poster_path);
        dest.writeString(this.original_language);


    }


    protected FilmItem(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.overview = in.readString();
        this.vote_average = in.readDouble();
        this.release_date = in.readString();
        this.poster_path = in.readString();
        this.original_language = in.readString();

    }

    public static final Creator<FilmItem> CREATOR = new Creator<FilmItem>() {
        @Override
        public FilmItem createFromParcel(Parcel source) {
            return new FilmItem(source);
        }

        @Override
        public FilmItem[] newArray(int size) {
            return new FilmItem[size];
        }
    };
}
