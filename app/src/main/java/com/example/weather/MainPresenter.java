package com.example.weather;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MainPresenter {
    private MainActivity activity;
    private final MainModel model;
    private View view;
    public void attachView(MainActivity mainActivity) {
        activity = mainActivity;
    }
    @Inject
    public MainPresenter(MainModel model) {
        this.model = model;
    }

    public MainPresenter(View view) {
        this.model = new MainModel();
        this.view = view; //TODO Использовать ли View?
    }

    public void handleSendRequest(String city, String key, Context context) {
        model.setKey(key);
        model.setContext(context);

        model.sendRequest(city, (weatherData) -> {
            MainActivity activity1 = this.activity;

            if (!weatherData.isStatus()) {
                weatherData.setStatus(true);
                this.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity1.wrongData();
                    }
                });
            } else if (weatherData.isErrorRequest()) {
                this.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity1.showError(weatherData.getError());
                    }
                });

            } else {
                this.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity1.updateWeatherInfo((int) weatherData.getMain().getTemp(), (int) weatherData.getMain().getFeels_like(), weatherData.getArrayList().get(0).getDescription(),
                                weatherData.getArrayList().get(0).getDescription());
                    }
                });
            }

        });

    }

    public interface View {
        void updateWeatherInfo(int temperature, int approximatelyTemperature, String conditionSky, String image);

        void wrongData();

        void showError(String error);
    }


}
