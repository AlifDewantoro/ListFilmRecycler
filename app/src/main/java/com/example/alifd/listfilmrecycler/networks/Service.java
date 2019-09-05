package com.example.alifd.listfilmrecycler.networks;

import com.example.alifd.listfilmrecycler.model.FilmResponse;
import com.example.alifd.listfilmrecycler.model.TvResponse;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Service {

    @FormUrlEncoded
    @POST("discover/movie")
    Observable<FilmResponse> getFilmList(@Header("Authorization") String api_key,
                                         @Field("language") String type);
    @FormUrlEncoded
    @POST("discover/tv")
    Observable<TvResponse> getTvList(@Header("Authorization") String api_key,
                                     @Field("language") String type);
    @FormUrlEncoded
    @POST("search/movie")
    Observable<FilmResponse> getFilmListByQuery(@Header("Authorization") String api_key,
                                     @Field("language") String type,
                                     @Field("query") String query);
    @FormUrlEncoded
    @POST("search/tv")
    Observable<TvResponse> getTvListByQuery(@Header("Authorization") String api_key,
                                            @Field("language") String type,
                                            @Field("query") String query);

}
