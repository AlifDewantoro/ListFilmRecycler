package com.example.alifd.listfilmrecycler.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.alifd.listfilmrecycler.DetailFilmActivity;
import com.example.alifd.listfilmrecycler.R;
import com.example.alifd.listfilmrecycler.db.FavHelper;
import com.example.alifd.listfilmrecycler.helper.SessionManager;
import com.example.alifd.listfilmrecycler.model.FilmModel;

import java.util.List;

import timber.log.Timber;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.ViewHolder> {

    private Context context;
    private List<FilmModel> filmModels;
    private SessionManager sessionManager;
    private FavHelper favHelper;

    public FilmAdapter(Context context, List<FilmModel> filmModels) {
        this.context = context;
        this.filmModels = filmModels;
        this.sessionManager = new SessionManager(context);
        this.favHelper = FavHelper.getInstance(context);
        favHelper.open();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemRow = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_film, viewGroup, false);
        return new ViewHolder(itemRow);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final FilmModel filmModel = filmModels.get(i);
        final FilmModel fav = favHelper.getFilmFavorite(filmModel.getId());
        //final FilmModel fav = realmManager.getFavFilmById(filmModel.getId());

        RequestOptions requestOptions = new RequestOptions()
                .centerCrop();
        String imgPath = String.format("%s/%s", sessionManager.getImgBaseUrl(), filmModel.getPosterPath());
        Timber.e("INI LINK FOTO %s", imgPath);
        Glide.with(context)
                .load(imgPath)
                .apply(requestOptions)
                .into(viewHolder.ivFilm);
        viewHolder.tvJudul.setText((i+1)+". "+filmModel.getTitle());
        viewHolder.tvTahun.setText("("+filmModel.getReleaseDate()+")");

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailFilmActivity.class);

                intent.putExtra(DetailFilmActivity.DETAIL_FILM, filmModel);
                context.startActivity(intent);
            }
        });
        if(fav.getId()!=null){
            viewHolder.ivFav.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_red));
        }else{
            viewHolder.ivFav.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_border_black));
        }

        viewHolder.ivFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilmModel favInside = favHelper.getFilmFavorite(filmModel.getId());
                if(favInside.getId()==null) {
                    Timber.e("do Favorite");
                    favHelper.insertFilm(filmModel);
                    viewHolder.ivFav.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_red));
                }else{
                    Timber.e("do Unfavorite");
                    favHelper.deleteFilm(filmModel.getId());
                    viewHolder.ivFav.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_border_black));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return filmModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFilm;
        TextView tvJudul;
        TextView tvTahun;
        ImageView ivFav;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFilm = itemView.findViewById(R.id.iv_film);
            tvJudul = itemView.findViewById(R.id.tv_judul);
            tvTahun = itemView.findViewById(R.id.tv_tahun);
            ivFav = itemView.findViewById(R.id.iv_fav_film);
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        favHelper.close();
    }
}
