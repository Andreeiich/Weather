package com.example.weather.WeatherModelData;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataHours {
    private boolean status;
    private boolean errorRequest;
    private String error;

    public DataHours() {
        this.status = true;
        this.errorRequest = false;
    }

    @SerializedName("list")
    private ArrayList<MainData> arrayMainList = new ArrayList<>();

    public ArrayList<MainData> getArrayMainList() {
        return arrayMainList;
    }

    public void setArrayMainList(ArrayList<MainData> arrayMainList) {
        this.arrayMainList = arrayMainList;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isErrorRequest() {
        return errorRequest;
    }

    public void setErrorRequest(boolean errorRequest) {
        this.errorRequest = errorRequest;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
