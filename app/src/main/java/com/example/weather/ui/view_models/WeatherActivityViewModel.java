package com.example.weather.ui.view_models;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weather.models.ForecastItem;

public class WeatherActivityViewModel extends ViewModel {

    public MutableLiveData<ForecastItem> forecastItem = new MutableLiveData<>();

}
