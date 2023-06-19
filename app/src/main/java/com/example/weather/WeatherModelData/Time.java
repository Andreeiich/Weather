package com.example.weather.WeatherModelData;

import com.google.gson.annotations.SerializedName;

public class Time {

    @SerializedName("dt_txt")
    private String dt_txt;

    public String getDt_txt() {
        return dt_txt;
    }

    public void setDt_txt(String dt_txt) {
        this.dt_txt = dt_txt;
    }
}
