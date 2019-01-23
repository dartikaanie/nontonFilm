package com.anie.dara.nontonfilm.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anie.dara.nontonfilm.R;
import com.anie.dara.nontonfilm.model.FilmItem;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.FilmHolder>{
    ArrayList<FilmItem> daftarFilm = new ArrayList<>();
    OnKlikFilm klikFilm;


    public void setDataFilm(ArrayList<FilmItem> films) {
        daftarFilm = films;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public FilmHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.list_film, viewGroup, false);
        return new FilmHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmHolder holder, int position) {
        FilmItem filmItem = daftarFilm.get(position);
        String subdes;
        if(filmItem.getOverview().length()<100){
            subdes=filmItem.getOverview();
        } else{
            subdes=filmItem.getOverview().substring(0,100)+"...";
        }
        holder.title.setText(filmItem.getTitle());
        holder.overview.setText(subdes);

        String url = "https://image.tmdb.org/t/p/w92/" + filmItem.getPoster_path();
        Glide.with(holder.itemView)
                .load(url)
                .into(holder.poster_path);
    }

    @Override
    public int getItemCount() {
        if(daftarFilm != null){
            return daftarFilm.size();
        }
        return 0;
    }

    public class FilmHolder extends RecyclerView.ViewHolder{
        TextView title, overview;
        ImageView poster_path;

        public FilmHolder(View v){
            super(v);
            title = v.findViewById(R.id.judul);
            overview = v.findViewById(R.id.deskripsi);
            poster_path = v.findViewById(R.id.poster);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FilmItem filmItem = daftarFilm.get(getAdapterPosition());
                    klikFilm.filmItemClicked(filmItem);
                }
            });
        }
    }

    public interface OnKlikFilm{
        void filmItemClicked (FilmItem filmItem);

    }

    public void setClickHandler(OnKlikFilm clickHandler) {
        this.klikFilm = clickHandler;
    }

}
