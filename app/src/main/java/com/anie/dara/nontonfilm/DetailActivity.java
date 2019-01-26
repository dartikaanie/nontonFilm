package com.anie.dara.nontonfilm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.anie.dara.nontonfilm.adapter.GenreAdapter;
import com.anie.dara.nontonfilm.model.FilmItem;
import com.anie.dara.nontonfilm.model.Genre;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity {

    private  FilmItem filmItem;
    private TextView title, rate, date, bahasa, overview, tagline, duration;
    private  ImageView poster;
    private  GenreAdapter genreAdapter;
    private   ArrayList<Integer> genre;
    private   ArrayList<Genre> genreFilm;
    private    RecyclerView revGenre;
    private    FrameLayout detail_view, detailProses;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = findViewById(R.id.detail_title);
        rate = findViewById(R.id.detail_rate);
        date = findViewById(R.id.detail_date);
        bahasa = findViewById(R.id.detail_bahas);
        overview = findViewById(R.id.detail_over);
        poster = findViewById(R.id.detail_poster);
        tagline = findViewById(R.id.detail_tagline);
        duration = findViewById(R.id.detail_duration);
        detail_view = findViewById(R.id.isiDetail);
        detailProses = findViewById(R.id.progresDetail);

        genreAdapter = new GenreAdapter();
        genreAdapter.setDaftarGenre(genreFilm);

        revGenre = findViewById(R.id.detail_rvGenres);
        revGenre.setAdapter(genreAdapter);

        StaggeredGridLayoutManager layoutGrid = new StaggeredGridLayoutManager(4, LinearLayoutManager.VERTICAL);
        revGenre.setLayoutManager(layoutGrid);
        revGenre.setHasFixedSize(true);

        Intent detailIntent = getIntent();
        if(null != detailIntent) {
            filmItem = detailIntent.getParcelableExtra("data_film_move");
        }

        if(filmItem != null) {
            getSupportActionBar().setTitle(filmItem.getTitle());
            detail_view.setVisibility(View.INVISIBLE);
            String url = "http://image.tmdb.org/t/p/w500" + filmItem.getPoster_path();
            Glide.with(this).load(url).into(poster);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            FilmClient client = retrofit.create(FilmClient.class);
            Call<FilmItem> call = client.getFilm(filmItem.getId(), "4c180b85240811f5521423090f06d6cc");
            call.enqueue(new Callback<FilmItem>() {
                @Override
                public void onResponse(Call<FilmItem> call, Response<FilmItem> response) {
                    FilmItem list = response.body();
                    detail_view.setVisibility(View.VISIBLE);
                    detailProses.setVisibility(View.INVISIBLE);

                    rate.setText(String.valueOf(list.getVote_average()));
                    date.setText(list.getRelease_date());
                    overview.setText(list.getOverview());
                    title.setText(list.getTitle());
                    bahasa.setText(list.getOriginal_language());
                    duration.setText(String.valueOf(list.getRuntime()));
                    tagline.setText(list.getTagline());
                    genreAdapter.setDaftarGenre(new ArrayList<Genre>(list.getGenres()));
                }

                @Override
                public void onFailure(Call<FilmItem> call, Throwable t) {
                    Log.d("gagalDetail", t.toString());
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

