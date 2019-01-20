package com.anie.dara.nontonfilm;

import com.anie.dara.nontonfilm.model.FilmList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FilmClient {
    @GET("/3/movie/upcoming")
    Call<FilmList> getUpcomingFilms(@Query("api_key") String api_key);

    @GET("/3/movie/{id}")
    Call<FilmList> getFilm(@Path("id") int id, @Query("api_key") String api_key);

    @GET("/3/search/movie")
    Call<FilmList> search(
            @Query("api_key") String api_key,
            @Query("query") String query
    );

}
