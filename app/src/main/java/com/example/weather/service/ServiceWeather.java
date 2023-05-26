package com.example.weather.service;

import com.example.weather.repository.GetDataAPI;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceWeather {
    String body;
    static final String BASE_URL = "https://api.openweathermap.org/";
    private static ServiceWeather mInstance;
    private Retrofit retrofit;

    public ServiceWeather(){
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();//без RxJava
    }

   public GetDataAPI init(){
        GetDataAPI getDataAPI=retrofit.create(GetDataAPI.class);
        return getDataAPI;
    }
    public static ServiceWeather getInstance() {
        if (mInstance == null) {
            mInstance = new ServiceWeather();
        }
        return mInstance;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public GetDataAPI getJSONApi() {
        return retrofit.create(GetDataAPI.class);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


}
