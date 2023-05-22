package com.example.weather.service;

import com.example.weather.repository.WeatherRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class WeatherService implements WeatherRepository {

    @Override
    public List<String> getNetworkData(String city, String key, String urls) throws IOException {

        List<String> list = new LinkedList<>();
        HttpsURLConnection connection = null;
        BufferedReader bufferedReader = null;
        StringBuffer stringBuffer = new StringBuffer();
        URL url = null;//передали в конструктор параметр
        try {
            url = new URL(urls);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        try {
            connection = (HttpsURLConnection) url.openConnection();//открыли соединение
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            connection.connect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        InputStream stream = null;//получаем всю информацию с определенного URL адреса -потоком
        try {
            stream = connection.getInputStream();
        } catch (IOException e) {
            list.add("false");
            throw new IOException();
        }

        if (stream != null) {
            bufferedReader = new BufferedReader(new InputStreamReader(stream));//считываем весь поток в формате строки

            String line = "";

            while (true) {
                try {
                    if (!((line = bufferedReader.readLine()) != null)) break;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                stringBuffer.append(line).append("\n");
            }
        }

        if (stream != null) {
            try {
                JSONObject jsonObject = new JSONObject(stringBuffer.toString());
                    Integer temp = (int) jsonObject.getJSONObject("main").getDouble("temp");
                    list.add(temp.toString());
                    Integer approximately = (int) jsonObject.getJSONObject("main").getDouble("feels_like");
                    list.add(approximately.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("weather");
                    for (int i = 0; i < (jsonArray.length()); i++) {
                        JSONObject json_obj = jsonArray.getJSONObject(i);
                        list.add(json_obj.getString("description"));
                    }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return list;
    }
}
