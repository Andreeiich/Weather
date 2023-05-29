package com.example.weather;


import com.example.weather.WeatherModelData.MainData;
import com.example.weather.service.ServiceWeather;
import com.example.weather.repository.GetDataAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;

import javax.inject.Inject;

public class MainModel {
    private String key;

    @Inject
    public MainModel() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void sendRequest(String city,String units,String language,onResult onResult) throws RuntimeException {

        ServiceWeather serviceWeather = new ServiceWeather();

        GetDataAPI getDataAPI = serviceWeather.getRetrofit().create(GetDataAPI.class);
        getDataAPI.getDataWeather(city,getKey(),units,
                language).enqueue(new Callback<MainData>() {
            @Override
            public void onResponse(Call<MainData> call,Response<MainData> response) {
                MainData dataWeather = response.body();
                if (response.isSuccessful()) {
                    onResult.func(dataWeather);
                }
                if (response.code() >= 404) {
                    MainData errorData = new MainData();
                    errorData.setStatus(false);
                    onResult.func(errorData);
                }
            }

            @Override
            public void onFailure(Call<MainData> call,Throwable t) {
                MainData mainData = new MainData();
                mainData.setErrorRequest(true);
                mainData.setError(t.getMessage());
                onResult.func(mainData);
            }
        });

    }

    interface onResult {
        void func(MainData weatherData);

    }

}