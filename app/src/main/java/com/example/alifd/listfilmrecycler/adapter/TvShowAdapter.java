package com.example.alifd.listfilmrecycler.adapter;

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
import com.example.alifd.listfilmrecycler.DetailTvShowActivity;
import com.example.alifd.listfilmrecycler.R;
import com.example.alifd.listfilmrecycler.db.FavHelper;
import com.example.alifd.listfilmrecycler.helper.RealmManager;
import com.example.alifd.listfilmrecycler.helper.SessionManager;
import com.example.alifd.listfilmrecycler.model.TvShowModel;

import java.util.List;

import io.realm.Realm;
import timber.log.Timber;

import static com.example.alifd.listfilmrecycler.DetailTvShowActivity.DETAIL_TV;

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.ViewHolder> {

    private Context context;
    private List<TvShowModel> tvShowModels;
    private SessionManager sessionManager;
    private RealmManager realmManager;
    private FavHelper favHelper;

    public TvShowAdapter(Context context, List<TvShowModel> tvShowModels) {
        this.context = context;
        this.tvShowModels = tvShowModels;
        this.sessionManager = new SessionManager(context);

        Realm.init(context);
        Realm realm = Realm.getDefaultInstance();
        realmManager = new RealmManager(realm);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemRow = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tv_show, viewGroup, false);
        return new ViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final TvShowModel tvShowModel = tvShowModels.get(i);
        final TvShowModel fav = realmManager.getFavTvShowById(tvShowModel.getId());

        RequestOptions requestOptions = new RequestOptions()
                .centerCrop();
        String imgPath = String.format("%s/%s",sessionManager.getImgBaseUrl(), tvShowModel.getPosterPath());
        Timber.e("INI LINK FOTO "+ imgPath);
        Glide.with(context)
                .load(imgPath)
                .apply(requestOptions)
                .into(viewHolder.ivTv);
        viewHolder.tvJudulTv.setText(tvShowModel.getName());
        viewHolder.tvDesTv.setText(tvShowModel.getOverview());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailTvShowActivity.class);

                intent.putExtra(DETAIL_TV, tvShowModel);
                context.startActivity(intent);
            }
        });

        if(fav!=null){
            viewHolder.ivFavTv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_red));
        }else{
            viewHolder.ivFavTv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_border_black));
        }

        viewHolder.ivFavTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TvShowModel favInside = realmManager.getFavTvShowById(tvShowModel.getId());
                if(favInside==null) {
                    Timber.e("do Favorite");
                    realmManager.saveTvShow(tvShowModel);
                    viewHolder.ivFavTv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_red));
                }else{
                    Timber.e("do Unfavorite");
                    realmManager.deleteTvShow(tvShowModel.getId());
                    viewHolder.ivFavTv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_border_black));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return tvShowModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFavTv;
        ImageView ivTv;
        TextView tvJudulTv;
        TextView tvDesTv;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFavTv = itemView.findViewById(R.id.iv_fav_tv);
            ivTv = itemView.findViewById(R.id.iv_tv_show);
            tvJudulTv = itemView.findViewById(R.id.tv_judul_tv);
            tvDesTv = itemView.findViewById(R.id.tv_des_tv);
        }
    }
}
