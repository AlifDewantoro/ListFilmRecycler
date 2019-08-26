package com.example.alifd.listfilmrecycler.presenter;

import com.example.alifd.listfilmrecycler.base.BasePresenter;
import com.example.alifd.listfilmrecycler.model.FilmResponse;
import com.example.alifd.listfilmrecycler.networks.RetrofitInstance;
import com.example.alifd.listfilmrecycler.networks.Service;
import com.example.alifd.listfilmrecycler.view.FilmView;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import timber.log.Timber;

public class FilmPresenter extends BasePresenter {
    private FilmView filmView;
    private Service service;
    public FilmPresenter(FilmView filmView) {
        this.filmView = filmView;
        this.service = RetrofitInstance.getAPIService();
    }

    public void getFilmList(String key, String lang){
        service.getFilmList(key, lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FilmResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(FilmResponse filmResponse) {
                        filmView.onSuccessGetData(filmResponse);
                        Timber.e("success get data");
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            ResponseBody responseBody = ((HttpException)e).response().errorBody();
                            filmView.onFailed(getErrorStatus(responseBody), getErrorMessage(responseBody));
                            Timber.e("failed get data");
                        } else {
                            filmView.onFailed(e.getMessage(), e.getMessage());
                            Timber.e("failed get data : unknown error");
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getFilmListByQuery(String key, String lang, String query){
        service.getFilmListByQuery(key, lang, query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FilmResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(FilmResponse filmResponse) {
                        filmView.onSuccessGetData(filmResponse);
                        Timber.e("success get data");
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            ResponseBody responseBody = ((HttpException)e).response().errorBody();
                            filmView.onFailed(getErrorStatus(responseBody), getErrorMessage(responseBody));
                            Timber.e("failed get data");
                        } else {
                            filmView.onFailed(e.getMessage(), e.getMessage());
                            Timber.e("failed get data : unknown error");
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
