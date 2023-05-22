package com.example.weather;


import android.annotation.SuppressLint;

import com.example.weather.service.WeatherService;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class MainModel {


    WeatherService weatherService = new WeatherService();

    private int temperature;
    private int approximatelyTemperature;
    private String conditionSky;
    private String key;
    private String url;
    String image;

    private boolean statusCity = true;

    public boolean isStatusCity() {
        return statusCity;
    }

    public void setStatusCity(boolean statusCity) {
        this.statusCity = statusCity;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getApproximatelyTemperature() {
        return approximatelyTemperature;
    }

    public void setApproximatelyTemperature(int approximatelyTemperature) {
        this.approximatelyTemperature = approximatelyTemperature;
    }

    public String getConditionSky() {
        return conditionSky;
    }

    public void setConditionSky(String conditionSky) {
        this.conditionSky = conditionSky;
    }


    public void sendRequest(String city) throws RuntimeException {

        try {
            Thread thread = new Thread(new FetchWeatherData(city, key, url), /*getResources().getString(R.string.Connect)*/"Connect");
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                thread.interrupt();
            }
        } catch (RuntimeException e) {
            throw new RuntimeException();
        }
    }

    private class FetchWeatherData implements Runnable {

        private String city = "";
        private String key = "";
        private String urls = "";

        public FetchWeatherData(String city, String key, String urls) {
            this.city = city;
            this.key = key;
            this.urls = urls;
        }


        @SuppressLint("SetTextI18n")
        @Override
        public void run() {

            List<String> list = new LinkedList<>();
            try {
                list = weatherService.getNetworkData(city, key, urls);
                setTemperature(Integer.parseInt(list.get(0)));
                setApproximatelyTemperature(Integer.parseInt(list.get(1)));
                setConditionSky(list.get(2));
                image = list.get(2);
            } catch (IOException e) {
                setStatusCity(false);
            }
        }

    }
}