package com.anie.dara.nontonfilm.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class FilmItem implements Parcelable {
    int id;
    String title;
    String overview;
    double vote_average;
    String release_date;
    String poster_path;
    String original_language;
    String tagline;
    Integer runtime;

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

    ArrayList<Integer> genre_ids;

    public ArrayList<Integer> getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(ArrayList<Integer> genre_ids) {
        this.genre_ids = genre_ids;
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

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
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

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
        }

    public FilmItem() {
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
        dest.writeList(this.genre_ids);

    }


    protected FilmItem(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.overview = in.readString();
        this.vote_average = in.readDouble();
        this.release_date = in.readString();
        this.poster_path = in.readString();
        this.original_language = in.readString();
        this.genre_ids = in.readArrayList(null);

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
