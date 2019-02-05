package com.anie.dara.nontonfilm.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
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

public class SearchFragment extends Fragment implements FilmAdapter.OnKlikFilm {

    private  ArrayList<FilmItem> daftarFilm = new ArrayList<>();
    private  RecyclerView revFilmlist;
    private  FilmAdapter filmAdapter;
    private   ProgressBar progressBar;
    private CardView cardCari;
//    private   Button cariBtn;
//    private   EditText cariText;
    private   Activity activity;
    private SearchView searchView;
    SearchView.OnQueryTextListener queryTextListener;
    MenuItem set;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's state here
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        filmAdapter = new FilmAdapter();
        filmAdapter.setDataFilm(daftarFilm);
        filmAdapter.setClickHandler(this);

        revFilmlist = view.findViewById(R.id.list_film);
        revFilmlist.setAdapter(filmAdapter);
        revFilmlist.setLayoutManager(new LinearLayoutManager(getActivity()));

        progressBar = view.findViewById(R.id.progressBar);
        cardCari = view.findViewById(R.id.cardCari);
//        cariText = view.findViewById(R.id.cariText);
//        cariBtn = view.findViewById(R.id.cariButton);
//        cariBtn.setOnClickListener(this);
        progressBar.setVisibility(View.VISIBLE);
        revFilmlist.setVisibility(View.INVISIBLE);
        getNowPlayingFilms();
        searchView = view.findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("d", query);
               if(query != null){
                   CariFilm(query);
               }else{
                   getNowPlayingFilms();
               }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

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
                Toast.makeText(activity,getString(R.string.success), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                cardCari.setVisibility(View.VISIBLE);
                revFilmlist.setVisibility(View.VISIBLE);
                filmAdapter.setDataFilm(new ArrayList<FilmItem>(listFilmItem));
            }

            @Override
            public void onFailure(Call<FilmList> call, Throwable t) {
                Toast.makeText(activity,getString(R.string.Fail), Toast.LENGTH_SHORT).show();
                Log.d("Gagal", t.toString());
            }
        });
        String title = getString(R.string.now_playing);
        ((AppCompatActivity)activity).getSupportActionBar().setSubtitle(title);

    }

    @Override
    public void filmItemClicked(FilmItem filmItem) {
        Intent moveDetail = new Intent(activity, DetailActivity.class);
        moveDetail.putExtra("data_film_move", filmItem);
        startActivity(moveDetail);
    }

    public void CariFilm(String filmCari) {

            progressBar.setVisibility(View.VISIBLE);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MainActivity.baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            FilmClient client = retrofit.create(FilmClient.class);

            Call<FilmList> call = client.search(MainActivity.apiKey, filmCari);
            call.enqueue(new Callback<FilmList>() {
                @Override
                public void onResponse(Call<FilmList> call, Response<FilmList> response) {
                    FilmList  listFilm = response.body();
                    List<FilmItem> listFilmItem = listFilm.results;
                    if(listFilmItem.size() == 0){
                        Toast.makeText(activity,getString(R.string.notFound), Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(activity,getString(R.string.success), Toast.LENGTH_SHORT).show();
                    }


                    progressBar.setVisibility(View.GONE);
                    filmAdapter.setDataFilm(new ArrayList<FilmItem>(listFilmItem));
                }

                @Override
                public void onFailure(Call<FilmList> call, Throwable t) {
                    Toast.makeText(activity,getString(R.string.Fail), Toast.LENGTH_SHORT).show();
                }
            });

            ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(getString(R.string.search_film));
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }






}
