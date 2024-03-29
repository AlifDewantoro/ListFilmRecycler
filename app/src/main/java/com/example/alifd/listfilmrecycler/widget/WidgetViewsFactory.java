package com.example.alifd.listfilmrecycler.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.example.alifd.listfilmrecycler.R;
import com.example.alifd.listfilmrecycler.helper.RealmManager;
import com.example.alifd.listfilmrecycler.helper.SessionManager;
import com.example.alifd.listfilmrecycler.model.FilmModel;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;


public class WidgetViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private List<String> widgetItems = new ArrayList<>();
    private final Context context;
    private SessionManager sessionManager;
    private Realm realm;

    public WidgetViewsFactory(Context context) {
        this.context = context;

        this.sessionManager = new SessionManager(context);

        Realm.init(context);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("fav_catalogue.db")
                .schemaVersion(0)
                .build();
        Realm.setDefaultConfiguration(configuration);
        this.realm = Realm.getDefaultInstance();
    }

    @Override
    public void onCreate() {
        List<FilmModel> filmModels = new ArrayList<>(realm.where(FilmModel.class).findAll());
        for(int x=0; x<filmModels.size(); x++) {
            widgetItems.add(filmModels.get(x).getPosterPath());
            Timber.e("create factory %s", widgetItems.get(x));
        }
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return widgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.item_widget_fav);
        String imgPath = String.format("%s/%s", sessionManager.getImgBaseUrl(), widgetItems.get(position));
        Timber.e("link di widget %s", imgPath);
        Uri uri = Uri.parse(imgPath);
        //remoteViews.setImageViewUri(R.id.imageViewWidget, uri);

        try {
            Bitmap bitmap = Glide.with(context)
                    .asBitmap()
                    .load(uri)
                    .submit(512, 512)
                    .get();

            remoteViews.setImageViewBitmap(R.id.imageViewWidget, bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bundle extras = new Bundle();
        extras.putInt(ListFilmAppWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        remoteViews.setOnClickFillInIntent(R.id.imageViewWidget, fillInIntent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
