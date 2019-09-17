package com.example.alifd.listfilmrecycler.presenter;

import com.example.alifd.listfilmrecycler.base.BasePresenter;
import com.example.alifd.listfilmrecycler.model.TvResponse;
import com.example.alifd.listfilmrecycler.networks.RetrofitInstance;
import com.example.alifd.listfilmrecycler.networks.Service;
import com.example.alifd.listfilmrecycler.view.TvShowView;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import timber.log.Timber;

public class TvShowPresenter extends BasePresenter {

    private TvShowView tvView;
    private Service service;

    public TvShowPresenter(TvShowView tvView) {
        this.tvView = tvView;
        this.service = RetrofitInstance.getAPIService();
    }

    public void getTvList(String key, String lang){
        service.getTvList(key, lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TvResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(TvResponse tvResponse) {
                        tvView.onSuccessGetData(tvResponse);
                        Timber.e("success get data");
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            ResponseBody responseBody = ((HttpException)e).response().errorBody();
                            try {
                                assert responseBody != null;
                                String body = responseBody.string();
                                Timber.e("ini message throwable : %s", body);
                                tvView.onFailed(getErrorStatus(body), getErrorMessage(body));
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            Timber.e("failed get data");
                        } else {
                            tvView.onFailed(e.getMessage(), e.getMessage());
                            Timber.e("failed get data : unknown error");
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getTvListByQuery(String key, String lang, String query){
        service.getTvListByQuery(key, lang, query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TvResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(TvResponse tvResponse) {
                        tvView.onSuccessGetData(tvResponse);
                        Timber.e("success get data");
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            ResponseBody responseBody = ((HttpException)e).response().errorBody();
                            try {
                                assert responseBody != null;
                                String body = responseBody.string();
                                Timber.e("ini message throwable : %s", body);
                                tvView.onFailed(getErrorStatus(body), getErrorMessage(body));
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            Timber.e("failed get data");
                        } else {
                            tvView.onFailed(e.getMessage(), e.getMessage());
                            Timber.e("failed get data : unknown error");
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
