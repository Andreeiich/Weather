package com.example.weather.repository;

import java.io.IOException;
import java.util.List;

public interface WeatherRepository {

    public List<String> getNetworkData(String city, String key, String urls) throws IOException;

}
