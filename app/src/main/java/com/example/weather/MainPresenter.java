package com.example.weather;

import android.widget.ImageView;

import java.io.IOException;

public class MainPresenter {
    private MainActivity activity;
    private final MainModel model;
    private View view;

    public void attachView(MainActivity mainActivity) {
        activity = mainActivity;
    }

    public MainPresenter(MainModel model) {
        this.model = model;
    }

    public MainPresenter(View view) {
        this.model = new MainModel();
        this.view = view;
    }

    public void handleSendRequest(String city, String key, String url) {
        model.setKey(key);
        model.setUrl(url);
        model.sendRequest(city);
        if (!model.isStatusCity()) {
            model.setStatusCity(true);
            this.activity.wrongData();
        } else {
            this.activity.updateWeatherInfo(model.getTemperature(), model.getApproximatelyTemperature(), model.getConditionSky(), model.image);
        }

    }

    public interface View {
        void updateWeatherInfo(int temperature, int approximatelyTemperature, String conditionSky, String image);

        void wrongData();
    }


}
