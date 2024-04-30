package com.example.weather.ui.view_models;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weather.adapters.WeatherItemsAdapter;
import com.example.weather.models.ForecastItem;

import java.util.ArrayList;

public class HomeActivityViewModel extends ViewModel {

    public MutableLiveData<String> searchQuery = new MutableLiveData<>();

    public MutableLiveData<Integer> searchVisibility = new MutableLiveData<>(View.GONE);

    public MutableLiveData<Integer> optionsVisibility = new MutableLiveData<>(View.GONE);

    public MutableLiveData<Integer> loadingVisibility = new MutableLiveData<>(View.GONE);

    public MutableLiveData<Integer> noInternetVisibility = new MutableLiveData<>(View.GONE);

    public MutableLiveData<ArrayList<ForecastItem>> forecastItems = new MutableLiveData<>();

    public void setForecastItems(ArrayList<ForecastItem> items){
        forecastItems.setValue(items);
    }

}
