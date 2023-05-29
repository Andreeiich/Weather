package com.example.weather.repository;


import com.example.weather.WeatherModelData.MainData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetDataAPI {
    @GET("data/2.5/weather?")
    Call<MainData> getDataWeather(@Query("q") String city,@Query("appid") String appid,@Query("units") String units,@Query("lang") String lang);
}
