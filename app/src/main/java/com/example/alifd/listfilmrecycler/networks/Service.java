package com.example.alifd.listfilmrecycler.networks;

import com.example.alifd.listfilmrecycler.model.FilmResponse;
import com.example.alifd.listfilmrecycler.model.TvResponse;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Service {

    //@FormUrlEncoded
    @POST("discover/movie")
    Observable<FilmResponse> getFilmList(@Query("api_key") String api_key,
                                         @Query("language") String type);
    //@FormUrlEncoded
    @POST("discover/movie")
    Observable<FilmResponse> getFilmList(@Query("api_key") String api_key,
                                         @Query("primary_release_date.gte") String date_gte,
                                         @Query("primary_release_date.lte") String date_lte);
    //@FormUrlEncoded
    @POST("discover/tv")
    Observable<TvResponse> getTvList(@Query("api_key") String api_key,
                                     @Query("language") String type);
    //@FormUrlEncoded
    @POST("search/movie")
    Observable<FilmResponse> getFilmListByQuery(@Query("api_key") String api_key,
                                     @Query("language") String type,
                                     @Query("query") String query);
    //@FormUrlEncoded
    @POST("search/tv")
    Observable<TvResponse> getTvListByQuery(@Query("api_key") String api_key,
                                            @Query("language") String type,
                                            @Query("query") String query);

}
