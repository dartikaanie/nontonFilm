package com.anie.dara.nontonfilm.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FilmList {
    @SerializedName("results")
    public List<FilmItem> results;

    @SerializedName("genres")
    public List<Genre> genres;

}
