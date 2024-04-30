package com.example.weather.interfaces;

import com.example.weather.models.ForecastItem;
import com.example.weather.models.WeatherItem;

import java.util.ArrayList;

public interface ForecastParserListener {
    public void onDataReady(ArrayList<ForecastItem> data);
}
