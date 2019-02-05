package com.anie.dara.nontonfilm;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anie.dara.nontonfilm.adapter.GenreAdapter;
import com.anie.dara.nontonfilm.db.DatabaseContract;
import com.anie.dara.nontonfilm.db.FavoritHelper;
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
    private   ArrayList<Genre> genreFilm;
    private    RecyclerView revGenre;

    private    FrameLayout detail_view, detailProses;
    FavoritHelper favoritHelper;
    MenuItem fav;

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
            String url = MainActivity.imageUrl + filmItem.getPoster_path();
            Glide.with(this).load(url).into(poster);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MainActivity.baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            FilmClient client = retrofit.create(FilmClient.class);
            Call<FilmItem> call = client.getFilm(filmItem.getId(), MainActivity.apiKey);
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

        Uri uri = getIntent().getData();

        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);

            if (cursor != null) {

                if (cursor.moveToFirst()) filmItem = new FilmItem(cursor);
                cursor.close();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);

        fav = menu.findItem(R.id.favorit_btn);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.favorit_btn :
                changeFavorit();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void changeFavorit(){
        if(filmItem != null) {
            favoritHelper = new FavoritHelper(this);
            favoritHelper.open();
            String change;
            if (!favoritHelper.cariFav(filmItem.getId())) {
                ContentValues values = new ContentValues();
                values.put("id", filmItem.getId());
                values.put("title", filmItem.getTitle());
                values.put("overview", filmItem.getOverview());
                values.put("poster_path", filmItem.getPoster_path());
                getContentResolver().insert(DatabaseContract.favorit.CONTENT_URI, values);
                finish();

                favoritHelper.insert(filmItem.getId(), filmItem.getTitle(), filmItem.getOverview(), filmItem.getPoster_path());
                change = getString(R.string.addFav);
                fav.setIcon(R.drawable.favorit_fill);
            } else {
                favoritHelper.delete(filmItem.getId());
                change = String.format(DetailActivity.this.getString(R.string.deleteFav));
                fav.setIcon(R.drawable.favorit_fill_gray);
            }
            favoritHelper.close();

            Toast.makeText(DetailActivity.this, change, Toast.LENGTH_SHORT).show();
        }else{
            String pesan = getString(R.string.pesan);
            Toast.makeText(DetailActivity.this, pesan, Toast.LENGTH_SHORT).show();
        }

       }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        super.onPrepareOptionsMenu(menu);

        favoritHelper = new FavoritHelper(this);
        favoritHelper.open();
        Log.d("id_fl", String.valueOf(filmItem.getId()));
        if(favoritHelper.cariFav(filmItem.getId())){
            fav.setIcon(R.drawable.favorit_fill);
            Toast.makeText(DetailActivity.this,"favorit", Toast.LENGTH_SHORT).show();
        }else{
            fav.setIcon(R.drawable.favorit_fill_gray);
            Toast.makeText(DetailActivity.this,"no favorit", Toast.LENGTH_SHORT).show();
        }
        favoritHelper.close();
        return true;
    }
}

