package com.anie.dara.nontonfilm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
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

public class MainActivity extends AppCompatActivity implements FilmAdapter.OnKlikFilm, View.OnClickListener {

    ArrayList<FilmItem> daftarFilm = new ArrayList<>();
    RecyclerView revFilmlist;
    FilmAdapter filmAdapter;
    ProgressBar progressBar;
    CardView cardCari;
    Button cariBtn;
    SearchView cariText;
    String filmCari;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filmAdapter = new FilmAdapter();
        filmAdapter.setDataFilm(daftarFilm);
        filmAdapter.setClickHandler(this);

        revFilmlist = findViewById(R.id.list_film);
        revFilmlist.setAdapter(filmAdapter);
        revFilmlist.setLayoutManager(new LinearLayoutManager(this));

        progressBar = findViewById(R.id.progressBar);
        cardCari = findViewById(R.id.cardCari);
        cariText = findViewById(R.id.cariText);
        cariBtn = findViewById(R.id.cariButton);
        cariBtn.setOnClickListener(this);
        progressBar.setVisibility(View.VISIBLE);
        revFilmlist.setVisibility(View.INVISIBLE);

        getUpcomingFilms();;
    }

    private void getUpcomingFilms() {

        revFilmlist.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FilmClient client = retrofit.create(FilmClient.class);
        Log.d("client", client.toString());
        Call<FilmList> call = client.getUpcomingFilms("4c180b85240811f5521423090f06d6cc");
        call.enqueue(new Callback<FilmList>() {
            @Override
            public void onResponse(Call<FilmList> call, Response<FilmList> response) {

                FilmList  listFilm = response.body();
                List<FilmItem> listFilmItem = listFilm.results;
                Toast.makeText(MainActivity.this,"Berhasil mengambil Data", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                cardCari.setVisibility(View.VISIBLE);
                revFilmlist.setVisibility(View.VISIBLE);
                filmAdapter.setDataFilm(new ArrayList<FilmItem>(listFilmItem));
            }

            @Override
            public void onFailure(Call<FilmList> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Gagal mengambil Data", Toast.LENGTH_SHORT).show();
                Log.d("Gagal", t.toString());
            }
        });

    }

    @Override
    public void filmItemClicked(FilmItem filmItem) {
        Toast.makeText(
                this,
                "Item yang diklik adalah : " + filmItem.getTitle(),
                Toast.LENGTH_SHORT).show();
        Intent moveDetail = new Intent(this, DetailActivity.class);
        moveDetail.putExtra("data_film_move", filmItem);
        startActivity(moveDetail);
    }

    public void CariFilm() {
        revFilmlist.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        String API_BASE_URL = "https://api.themoviedb.org";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FilmClient client = retrofit.create(FilmClient.class);

        Call<FilmList> call = client.search("4c180b85240811f5521423090f06d6cc", filmCari);
        call.enqueue(new Callback<FilmList>() {
            @Override
            public void onResponse(Call<FilmList> call, Response<FilmList> response) {
                FilmList  listFilm = response.body();
                List<FilmItem> listFilmItem = listFilm.results;
                Toast.makeText(MainActivity.this,"Berhasil mengambil Data", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                cardCari.setVisibility(View.VISIBLE);
                revFilmlist.setVisibility(View.VISIBLE);
                filmAdapter.setDataFilm(new ArrayList<FilmItem>(listFilmItem));
            }

            @Override
            public void onFailure(Call<FilmList> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case   R.id.cariButton :
                filmCari = cariText.getQuery().toString();
                CariFilm();
                break;
        }
    }
}

