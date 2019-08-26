package com.example.alifd.listfilmrecycler.helper;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.example.alifd.listfilmrecycler.model.FilmModel;
import com.example.alifd.listfilmrecycler.model.TvShowModel;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import timber.log.Timber;

import static android.provider.BaseColumns._ID;

public class RealmManager {
    private Realm realm;

    public RealmManager(Realm realm) {
        this.realm = realm;
    }

    public void saveFilm(final FilmModel filmModel){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                Timber.e("Film Created");
                realm.copyToRealm(filmModel);
            }
        });
    }

    public List<FilmModel> getListFilmFav(){
        Timber.e("get all film local");
        return realm.where(FilmModel.class).findAll();
    }

    public FilmModel getFavFilmById(int id){
        Timber.e("get one film local");
        return realm.where(FilmModel.class).equalTo("id", id).findFirst();
    }

    public void deleteFilm(int id){
        final RealmResults<FilmModel> model = realm.where(FilmModel.class).equalTo("id", id).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                Timber.e("Film Deleted");
                model.deleteFromRealm(0);
            }
        });
    }



    public void saveTvShow(final TvShowModel tvShowModel){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                Timber.e("Tv Created");
                realm.copyToRealm(tvShowModel);
            }
        });
    }

    public List<TvShowModel> getListTvShowFav(){
        Timber.e("get all film local");
        return realm.where(TvShowModel.class).findAll();
    }


    public TvShowModel getFavTvShowById(int id){
        return realm.where(TvShowModel.class).equalTo("id", id).findFirst();
    }

    public void deleteTvShow(int id){
        final RealmResults<TvShowModel> model = realm.where(TvShowModel.class).equalTo("id", id).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                Timber.e("Tv Deleted");
                model.deleteFromRealm(0);
            }
        });
    }

}
