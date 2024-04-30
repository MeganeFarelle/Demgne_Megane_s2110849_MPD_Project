package com.example.weather;

import android.app.Application;
import com.example.weather.models.ForecastItem;
import java.util.ArrayList;

public class WeatherApplication extends Application {

    public ForecastItem currentLocationWeather = new ForecastItem();

    public ArrayList<ForecastItem> forecastItems = new ArrayList<>();

}
