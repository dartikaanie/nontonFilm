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
import com.anie.dara.nontonfilm.model.Genre;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    FilmItem filmItem;
    TextView title, rate, date, bahasa, overview;
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
            rate.setText(String.valueOf(filmItem.getVote_average()));
            date.setText(filmItem.getRelease_date());
            overview.setText(filmItem.getOverview());
            title.setText(filmItem.getTitle());
            genre = filmItem.getGenre_ids();
            Log.d("arrayGenre", String.valueOf(genre.size()));
            String url = "http://image.tmdb.org/t/p/w500" + filmItem.getPoster_path();
            Glide.with(this).load(url).into(poster);

//            //dapatin genre
//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl("https://api.themoviedb.org")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//            FilmClient client = retrofit.create(FilmClient.class);
//            Call<FilmList> call = client.getGenre("4c180b85240811f5521423090f06d6cc");
//            call.enqueue(new Callback<FilmList>() {
//                @Override
//                public void onResponse(Call<FilmList> call, Response<FilmList> response) {
//                    FilmList list = response.body();
//                    List<Genre> listGenre = list.genres;
//                    Log.d("noGenre", String.valueOf(list));
//                    genreFilm = new ArrayList<>();
//                    for(int i=0; i<genre.size();i++){
//                        for(Genre item : listGenre)
//                        {
//                            Log.d("id", String.valueOf(item.getId()));
//                            Log.d("idDia", String.valueOf(genre.get(i)));
//                            if(item.getId() == genre.get(i) ){
//                                Log.d("sama", item.getName());
//                                genreFilm.add(item);
//                            }
//                        }
//                        i++;
//                    }
//                    Log.d("genreArray", String.valueOf(genreFilm.size()));
//
//                    genreAdapter.setDaftarGenre(new ArrayList<Genre>(genreFilm));
//                }
//
//                @Override
//                public void onFailure(Call<FilmList> call, Throwable t) {
//
//                }
//            });
        }
    }
}

