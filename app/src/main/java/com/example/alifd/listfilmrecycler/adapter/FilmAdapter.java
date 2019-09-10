package com.example.alifd.listfilmrecycler.adapter;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.alifd.listfilmrecycler.DetailFilmActivity;
import com.example.alifd.listfilmrecycler.R;
import com.example.alifd.listfilmrecycler.helper.MappingHelper;
import com.example.alifd.listfilmrecycler.helper.SessionManager;
import com.example.alifd.listfilmrecycler.model.FilmModel;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static com.example.alifd.listfilmrecycler.db.DatabaseContract.FavColumns.CONTENT_URI;
import static com.example.alifd.listfilmrecycler.db.DatabaseContract.FavColumns.ID;
import static com.example.alifd.listfilmrecycler.db.DatabaseContract.FavColumns.OVERVIEW;
import static com.example.alifd.listfilmrecycler.db.DatabaseContract.FavColumns.POSTER_PATH;
import static com.example.alifd.listfilmrecycler.db.DatabaseContract.FavColumns.RELEASE_DATE;
import static com.example.alifd.listfilmrecycler.db.DatabaseContract.FavColumns.TITLE;
import static com.example.alifd.listfilmrecycler.db.DatabaseContract.FavColumns.VOTE_AVERAGE;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.ViewHolder> {

    private Context context;
    private List<FilmModel> filmModels;
    private ArrayList<FilmModel> filmModelsDB;
    private SessionManager sessionManager;
    private static HandlerThread handlerThread;
    private Cursor cursor;

    public FilmAdapter(Context context, List<FilmModel> filmModels) {
        this.context = context;
        this.filmModels = filmModels;
        this.sessionManager = new SessionManager(context);

        handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver observer = new DataObserver(handler);
        context.getContentResolver().registerContentObserver(CONTENT_URI,true,observer);

        getDataFromDB();
    }

    private void getDataFromDB(){
        cursor = context.getContentResolver().query(CONTENT_URI,null,null,null,null);
        if(cursor != null) {
            this.filmModelsDB = MappingHelper.mapCursorToArrayList(cursor);
        }
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
        //final FilmModel fav = favHelper.getFilmFavorite(filmModel.getId());
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
        if(checkFavFilm(filmModel)){
            viewHolder.ivFav.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_red));
        }else{
            viewHolder.ivFav.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_border_black));
        }

        viewHolder.ivFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FilmModel favInside = favHelper.getFilmFavorite(filmModel.getId());
                if(!checkFavFilm(filmModel)) {
                    Timber.e("do Favorite");
                    //favHelper.insertFilm(filmModel);
                    ContentValues values = new ContentValues(6);
                    values.put(ID, filmModel.getId());
                    values.put(VOTE_AVERAGE, filmModel.getVoteAverage());
                    values.put(TITLE, filmModel.getTitle());
                    values.put(POSTER_PATH, filmModel.getPosterPath());
                    values.put(OVERVIEW, filmModel.getOverview());
                    values.put(RELEASE_DATE, filmModel.getReleaseDate());
                    context.getContentResolver().insert(CONTENT_URI, values);
                    filmModelsDB.clear();
                    getDataFromDB();
                    viewHolder.ivFav.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_red));
                }else{
                    Timber.e("do Unfavorite");
                    //favHelper.deleteFilm(filmModel.getId());
                    Uri uri = Uri.parse(CONTENT_URI+"/"+filmModel.getId().toString());
                    Timber.e(uri.toString());
                    context.getContentResolver().delete(uri, null, null);
                    filmModelsDB.clear();
                    getDataFromDB();
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
    }

    private boolean checkFavFilm(FilmModel filmModel){
        boolean result = false;
        for(int x=0; x<filmModelsDB.size(); x++){
            Timber.e(filmModelsDB.get(x).getId().toString());
            Timber.e(filmModel.getId().toString());
            if(filmModel.getId().toString().equals(filmModelsDB.get(x).getId().toString())){
                result = true;
            }
        }
        Timber.e("%s", result);
        return result;
    }

    public void doRefreshFilmDataDb(){
        filmModelsDB.clear();
        getDataFromDB();
    }

    public static class DataObserver extends ContentObserver {
        public DataObserver(Handler handler) {
            super(handler);
        }
    }
}
