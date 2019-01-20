package com.anie.dara.nontonfilm.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anie.dara.nontonfilm.R;
import com.anie.dara.nontonfilm.model.Genre;

import java.util.ArrayList;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreHolder> {

    ArrayList<Genre> daftarGenre = new ArrayList<>();

    public void setDaftarGenre(ArrayList<Genre> genre) {
        daftarGenre = genre;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GenreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.list_genres, parent, false);
        return new GenreHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreHolder holder, int position) {
        Genre genreItem = daftarGenre.get(position);
        holder.namaGenre.setText(genreItem.getName());
    }

    @Override
    public int getItemCount() {
        if(daftarGenre != null){
            return daftarGenre.size();
        }
        return 0;
    }

    public class GenreHolder extends RecyclerView.ViewHolder {
        TextView namaGenre;

        public GenreHolder(View v){
            super(v);
            namaGenre = v.findViewById(R.id.genre_name);
        }
    }
}
