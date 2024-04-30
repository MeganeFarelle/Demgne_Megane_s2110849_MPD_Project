package com.example.weather.ui.view_models;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weather.models.ForecastItem;
import com.example.weather.models.WeatherItem;

import java.util.Objects;

public class ThreeDaysForecastActivityViewModel extends ViewModel {

    public MutableLiveData<ForecastItem> forecastItem = new MutableLiveData<>();

    public void setInterest(WeatherItem item){
        Objects.requireNonNull(forecastItem.getValue()).interest = item;
    }

}
