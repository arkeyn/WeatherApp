package com.omoskalchuk.weatherapp.WeatherApp.controller;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WeatherService {

    private OkHttpClient client;
    private Response response;
    private String CityName;
    private String unit;

String cityName = "Stamford";

    public JSONObject getWeather() throws JSONException {

        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://api.openweathermap.org/data/2.5/weather?q=" +
                        getCityName() + "&units=" + getUnit() + "&APPID=64bf2eb75290605e54391e33d6e50454")
                .build();

        try {
            response = client.newCall(request).execute();

            return new JSONObject(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public JSONArray returnWeatherArray() throws JSONException {

            JSONArray weatherJsonArray = getWeather().getJSONArray("weather");

        return weatherJsonArray;

    }


    public JSONObject returnMainObject() throws JSONException {

        JSONObject mainObject = getWeather().getJSONObject("main");

        return mainObject;
    }

    public JSONObject returnWindObject() throws JSONException {

        JSONObject windObject = getWeather().getJSONObject("wind");

        return windObject;

    }

    public JSONObject returnSunSetObject() throws JSONException {

        JSONObject sunSetObj = getWeather().getJSONObject("sys");

        return sunSetObj;
    }



    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
