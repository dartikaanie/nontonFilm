package com.anie.dara.nontonfilm.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.anie.dara.nontonfilm.DetailActivity;
import com.anie.dara.nontonfilm.LoadCallback;
import com.anie.dara.nontonfilm.R;
import com.anie.dara.nontonfilm.adapter.FilmAdapter;
import com.anie.dara.nontonfilm.db.DatabaseContract;
import com.anie.dara.nontonfilm.db.FavoritHelper;
import com.anie.dara.nontonfilm.model.FilmItem;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.anie.dara.nontonfilm.helper.MappingHelper.mapCursorToArrayList;

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

    private static final String EXTRA_STATE = "EXTRA_STATE";
    private static HandlerThread handlerThread;
    private DataObserver myObserver;


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


        handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        myObserver = new DataObserver(handler, activity);
        activity.getContentResolver().registerContentObserver(DatabaseContract.favorit.CONTENT_URI, true, myObserver);



        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, filmAdapter.getDataFilm());

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

    private void showSnackbarMessage(String message) {
        Snackbar.make(revFilmlist, message, Snackbar.LENGTH_SHORT).show();
    }

//    @Override
    public void postExecute(Cursor notes) {
        ArrayList<FilmItem> listFilm = mapCursorToArrayList(notes);
        if (listFilm.size() > 0) {
            filmAdapter.setDataFilm(listFilm);
        } else {
            filmAdapter.setDataFilm(new ArrayList<FilmItem>());
            showSnackbarMessage("Tidak ada data saat ini");
        }
    }

    private static class LoadNoteAsync extends AsyncTask<Void, Void, Cursor> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadCallback> weakCallback;

        private LoadNoteAsync(Context context, LoadCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            Context context = weakContext.get();
            return context.getContentResolver().query(DatabaseContract.favorit.CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor notes) {
            super.onPostExecute(notes);
            weakCallback.get().postExecute(notes);
        }
    }
    public static class DataObserver extends ContentObserver {

        final Context context;

        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadNoteAsync(context, (LoadCallback) context).execute();

        }
    }
}
