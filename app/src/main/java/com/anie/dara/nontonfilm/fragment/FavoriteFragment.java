package com.anie.dara.nontonfilm.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.anie.dara.nontonfilm.DetailActivity;
import com.anie.dara.nontonfilm.R;
import com.anie.dara.nontonfilm.adapter.FilmAdapter;
import com.anie.dara.nontonfilm.db.FavoritHelper;
import com.anie.dara.nontonfilm.model.FilmItem;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment implements FilmAdapter.OnKlikFilm {

    private ArrayList<FilmItem> daftarFilm = new ArrayList<>();
    private RecyclerView revFilmlist;
    private FilmAdapter filmAdapter =new FilmAdapter();
    private ProgressBar progressBar;
    private Activity activity;
    private FavoritHelper favoritHelper;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        favoritHelper = new FavoritHelper(activity);
        favoritHelper.open();
        ArrayList<FilmItem> filmlist = favoritHelper.getData();
        favoritHelper.close();


//        filmAdapter = new FilmAdapter();
        filmAdapter.setDataFilm(filmlist);
        filmAdapter.setClickHandler(this);

        revFilmlist = view.findViewById(R.id.list_film);
        revFilmlist.setAdapter(filmAdapter);
        revFilmlist.setLayoutManager(new LinearLayoutManager(getActivity()));


        return view;
    }


    @Override
    public void filmItemClicked(FilmItem filmItem) {
        Intent moveDetail = new Intent(activity, DetailActivity.class);
        moveDetail.putExtra("data_film_move", filmItem);
        startActivity(moveDetail);
    }


    @Override
    public void onResume() {
        super.onResume();
        favoritHelper.open();
        ArrayList<FilmItem> filmlist = favoritHelper.getData();
        favoritHelper.close();
        filmAdapter.setDataFilm(filmlist);
    }
}
