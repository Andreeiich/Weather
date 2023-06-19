package com.example.weather;


import com.example.weather.WeatherModelData.DataHours;
import com.example.weather.WeatherModelData.MainData;
import com.example.weather.service.ServiceWeather;
import com.example.weather.repository.GetDataAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;

import java.text.ParseException;

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
                } else {
                    MainData errorData = new MainData();
                    errorData.setStatus(false);
                    assert response.errorBody() != null;
                    errorData.setError(response.errorBody().toString());
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


    public void sendRequestHours(String city,String units,String language,onResultHours onResultHours) throws RuntimeException {
        ServiceWeather serviceWeather = new ServiceWeather();
        GetDataAPI getDataAPIHours = serviceWeather.getRetrofit().create(GetDataAPI.class);

        getDataAPIHours.getDataWeatherDevidedToHours(city,getKey(),units,language,8).enqueue(new Callback<DataHours>() {
            @Override
            public void onResponse(Call<DataHours> call,Response<DataHours> response) {
                DataHours dataHours = response.body();

                if (response.isSuccessful()) {
                    try {
                        onResultHours.funcHours(dataHours);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    DataHours errorData = new DataHours();
                    errorData.setStatus(false);
                    assert response.errorBody() != null;
                    errorData.setError(response.errorBody().toString());
                    try {
                        onResultHours.funcHours(errorData);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }


            }

            @Override
            public void onFailure(Call<DataHours> call,Throwable t) {

            }
        });


    }

    interface onResult {
        void func(MainData weatherData);

    }

    interface onResultHours {

        void funcHours(DataHours dataHours) throws ParseException;
    }

}