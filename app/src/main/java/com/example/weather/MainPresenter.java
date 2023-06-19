package com.example.weather;

import android.content.Context;
import android.os.Build;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MainPresenter {
    private MainActivity activity;
    private final MainModel model;

    public void attachView(MainActivity mainActivity) {
        activity = mainActivity;
    }

    @Inject
    public MainPresenter(MainModel model) {
        this.model = model;
    }

    public void handleSendRequest(String city,String key,String units,String language) {
        model.setKey(key);

        model.sendRequest(city,units,language,(weatherData) -> {
            MainActivity activity1 = this.activity;

            if (!weatherData.isStatus()) {
                weatherData.setStatus(true);
                activity.wrongData();
            } else if (weatherData.isErrorRequest()) {
                activity.showError(weatherData.getError());
            } else {
                this.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity1.updateWeatherInfo((int) weatherData.getMain().getTemp(),(int) weatherData.getMain().getFeels_like(),weatherData.getArrayList().get(0).getDescription(),
                                weatherData.getArrayList().get(0).getDescription(),weatherData.getName());
                    }
                });
            }

        });


        model.sendRequestHours(city,units,language,(dataHours) -> {

            MainActivity activity1 = this.activity;

            if (!dataHours.isStatus()) {
                dataHours.setStatus(true);
                activity.wrongData();
            } else if (dataHours.isErrorRequest()) {
                activity.showError(dataHours.getError());
            } else {
                this.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0;i < 5;i++) {
                            activity1.setDataTime(i,(int) dataHours.getArrayMainList().get(i).getMain().getTemp(),dataHours.getArrayMainList().get(i).getDt_txt().substring(11,16),dataHours.getArrayMainList().get(i).getArrayList().get(0).getDescription());

                        }
                    }
                });
            }
        });

    }

    public interface View {
        void updateWeatherInfo(int temperature,int approximatelyTemperature,String conditionSky,String image,String city);

        void wrongData();

        void showError(String error);

        void setDataTime(int index,int temp,String time,String image);
    }


}
