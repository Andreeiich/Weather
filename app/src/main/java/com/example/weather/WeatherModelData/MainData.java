package com.example.weather.WeatherModelData;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MainData {

    @SerializedName("coord")
    private Coord coord;
    @SerializedName("weather")
    private ArrayList<Weather> arrayList = new ArrayList<>();
    @SerializedName("main")
    private Main main;
    @SerializedName("wind")
    private Wind wind;
    @SerializedName("clouds")
    private Clouds clouds;
    @SerializedName("sys")
    private Sys sys;

    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDt_txt() {
        return dt_txt;
    }

    public void setDt_txt(String dt_txt) {
        this.dt_txt = dt_txt;
    }

    @SerializedName("dt_txt")
    private String dt_txt;

    private boolean status;
    private boolean errorRequest;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    private String error;

    public MainData() {
        this.status = true;
        this.errorRequest = false;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public boolean isErrorRequest() {
        return errorRequest;
    }

    public void setErrorRequest(boolean errorRequest) {
        this.errorRequest = errorRequest;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public ArrayList<Weather> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<Weather> arrayList) {
        this.arrayList = arrayList;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

}



