package com.anie.dara.nontonfilm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.anie.dara.nontonfilm.adapter.FilmAdapter;
import com.anie.dara.nontonfilm.model.FilmItem;
import com.anie.dara.nontonfilm.model.FilmList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NowPlayingActivity extends AppCompatActivity implements FilmAdapter.OnKlikFilm {

    ArrayList<FilmItem> daftarFilm = new ArrayList<>();
    RecyclerView revFilmlist;
    FilmAdapter filmAdapter;
    ProgressBar progressBar;
    CardView cardCari;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nowplaying);
        filmAdapter = new FilmAdapter();
        filmAdapter.setDataFilm(daftarFilm);
        filmAdapter.setClickHandler(this);

        revFilmlist = findViewById(R.id.list_film);
        revFilmlist.setAdapter(filmAdapter);
        revFilmlist.setLayoutManager(new LinearLayoutManager(this));

        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);
        revFilmlist.setVisibility(View.INVISIBLE);

        getNowPlayingFilms();
    }

    private void getNowPlayingFilms() {

        revFilmlist.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FilmClient client = retrofit.create(FilmClient.class);
        Call<FilmList> call = client.getNowPlayingFilms("4c180b85240811f5521423090f06d6cc");
        call.enqueue(new Callback<FilmList>() {
            @Override
            public void onResponse(Call<FilmList> call, Response<FilmList> response) {

                FilmList  listFilm = response.body();
                List<FilmItem> listFilmItem = listFilm.results;
                Toast.makeText(NowPlayingActivity.this,"Berhasil mengambil Data", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                cardCari.setVisibility(View.VISIBLE);
                revFilmlist.setVisibility(View.VISIBLE);
                filmAdapter.setDataFilm(new ArrayList<FilmItem>(listFilmItem));
            }

            @Override
            public void onFailure(Call<FilmList> call, Throwable t) {
                Toast.makeText(NowPlayingActivity.this,"Gagal mengambil Data", Toast.LENGTH_SHORT).show();
                Log.d("Gagal", t.toString());
            }
        });
        getSupportActionBar().setTitle("Now Playing");

    }

    @Override
    public void filmItemClicked(FilmItem filmItem) {
        Intent moveDetail = new Intent(this, DetailActivity.class);
        moveDetail.putExtra("data_film_move", filmItem);
        startActivity(moveDetail);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent moveMenu = null;
      switch (item.getItemId()){
            case   R.id.menu_upcoming :
                moveMenu = new Intent(this, MainActivity.class);
                break;
            case  R.id.menu_now :
                moveMenu = new Intent(this, NowPlayingActivity.class);
                break;
            case  R.id.menu_search :
                moveMenu = new Intent(this, SearchActivity.class);
                break;
        }
        startActivity(moveMenu);
        return super.onOptionsItemSelected(item);
    }

}
