package com.anie.dara.nontonfilm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.anie.dara.nontonfilm.adapter.GenreAdapter;
import com.anie.dara.nontonfilm.model.FilmItem;
import com.anie.dara.nontonfilm.model.FilmList;
import com.anie.dara.nontonfilm.model.Genre;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity {

    FilmItem filmItem;
    TextView title, rate, date, bahasa, overview, tagline, duration;
    ImageView poster;
    GenreAdapter genreAdapter;
    ArrayList<Integer> genre;
    ArrayList<Genre> genreFilm;
    RecyclerView revGenre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        title = findViewById(R.id.detail_title);
        rate = findViewById(R.id.detail_rate);
        date = findViewById(R.id.detail_date);
        bahasa = findViewById(R.id.detail_bahas);
        overview = findViewById(R.id.detail_over);
        poster = findViewById(R.id.detail_poster);
        tagline = findViewById(R.id.detail_tagline);
        duration = findViewById(R.id.detail_duration);


        genreAdapter = new GenreAdapter();
        genreAdapter.setDaftarGenre(genreFilm);

        revGenre = findViewById(R.id.detail_rvGenres);
        revGenre.setAdapter(genreAdapter);
        revGenre.setLayoutManager(new GridLayoutManager(this,5));


        Intent detailIntent = getIntent();
        if(null != detailIntent) {
            filmItem = detailIntent.getParcelableExtra("data_film_move");
        }

        if(filmItem != null) {
            getSupportActionBar().setTitle(filmItem.getTitle());
//            rate.setText(filmItem.getVote_average());
            String url = "http://image.tmdb.org/t/p/w500" + filmItem.getPoster_path();
            Glide.with(this).load(url).into(poster);

            //dapatin genre
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            FilmClient client = retrofit.create(FilmClient.class);
            Log.d("idFIlm", String.valueOf(filmItem.getId()));
            Call<FilmList> call = client.getFilm(filmItem.getId(), "4c180b85240811f5521423090f06d6cc");
            call.enqueue(new Callback<FilmList>() {
                @Override
                public void onResponse(Call<FilmList> call, Response<FilmList> response) {
                    FilmList list = response.body();
                    List detail =response.body().detail;
                    List<FilmItem> listfilm = list.detail;
                    Log.d("ad", String.valueOf(listfilm.get(0).getVote_average()));
//                    rate.setText(String.valueOf(listfilm.get(0).getVote_average()));
                    date.setText(listfilm.get(0).getRelease_date());
                    overview.setText(listfilm.get(0).getOverview());
                    title.setText(listfilm.get(0).getTitle());
                    bahasa.setText(listfilm.get(0).getOriginal_language());
                    duration.setText(String.valueOf(listfilm.get(0).getRuntime()));
                    tagline.setText(listfilm.get(0).getTagline());

                }

                @Override
                public void onFailure(Call<FilmList> call, Throwable t) {

                }
            });
        }
    }
}

