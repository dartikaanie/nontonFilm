package com.anie.dara.nontonfilm.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.anie.dara.nontonfilm.DetailActivity;
import com.anie.dara.nontonfilm.FilmClient;
import com.anie.dara.nontonfilm.MainActivity;
import com.anie.dara.nontonfilm.R;
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


public class NowPlayingFragment extends Fragment implements FilmAdapter.OnKlikFilm {


    private ArrayList<FilmItem> daftarFilm = new ArrayList<>();
    private  RecyclerView revFilmlist;
    private  FilmAdapter filmAdapter;
    private  ProgressBar progressBar;
    private  Activity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_now_playing, container, false);

        filmAdapter = new FilmAdapter();
        filmAdapter.setDataFilm(daftarFilm);
        filmAdapter.setClickHandler(this);

        revFilmlist = view.findViewById(R.id.list_film);
        revFilmlist.setAdapter(filmAdapter);
        revFilmlist.setLayoutManager(new LinearLayoutManager(getActivity()));

        progressBar = view.findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);
        revFilmlist.setVisibility(View.INVISIBLE);

        getNowPlayingFilms();
        return view;
    }

    private void getNowPlayingFilms() {

        revFilmlist.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FilmClient client = retrofit.create(FilmClient.class);
        Call<FilmList> call = client.getNowPlayingFilms(MainActivity.apiKey);
        call.enqueue(new Callback<FilmList>() {
            @Override
            public void onResponse(Call<FilmList> call, Response<FilmList> response) {

                FilmList  listFilm = response.body();
                List<FilmItem> listFilmItem = listFilm.results;
                String sukses = String.format(activity.getString(R.string.success));
                Toast.makeText(activity,sukses, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                revFilmlist.setVisibility(View.VISIBLE);
                filmAdapter.setDataFilm(new ArrayList<FilmItem>(listFilmItem));
            }

            @Override
            public void onFailure(Call<FilmList> call, Throwable t) {
                String gagal = String.format(activity.getString(R.string.Fail));
                Toast.makeText(activity,gagal, Toast.LENGTH_SHORT).show();
                Log.d("Gagal", t.toString());
            }
        });
        String title = String.format(activity.getString(R.string.now_playing));
        ((AppCompatActivity)activity).getSupportActionBar().setSubtitle(title);

    }

    @Override
    public void filmItemClicked(FilmItem filmItem) {
        Intent moveDetail = new Intent(activity, DetailActivity.class);
        moveDetail.putExtra("data_film_move", filmItem);
        startActivity(moveDetail);
    }
}
