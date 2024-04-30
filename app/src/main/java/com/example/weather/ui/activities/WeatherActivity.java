package com.example.weather.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.weather.WeatherApplication;
import com.example.weather.databinding.WeatherActivityBinding;
import com.example.weather.models.ForecastItem;
import com.example.weather.ui.view_models.WeatherActivityViewModel;

public class WeatherActivity extends AppCompatActivity {

    WeatherActivityBinding binding;
    WeatherApplication application;
    WeatherActivityViewModel weatherActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = WeatherActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        weatherActivityViewModel = new ViewModelProvider(this).get(WeatherActivityViewModel.class);
        binding.setViewModel(weatherActivityViewModel);
        binding.setWeatherActivity(this);

        application = (WeatherApplication) getApplication();

        Intent intent = getIntent();
        weatherActivityViewModel.forecastItem.setValue(intent.getParcelableExtra("WeatherItem"));

        binding.setLifecycleOwner(this);
    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        ForecastItem received = intent.getParcelableExtra("WeatherItem");
        weatherActivityViewModel.forecastItem.setValue(received);
    }

    public void back(){
        finish();
    }

    public void map(){
        Intent intent = new Intent(WeatherActivity.this, MapActivity.class);
        startActivity(intent);
    }

    public void home(){
        Intent intent = new Intent(WeatherActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void weather(){
        weatherActivityViewModel.forecastItem.setValue(application.currentLocationWeather);
    }

    public void threeDayForecast(){
        Intent tdfIntent = new Intent(WeatherActivity.this, ThreeDaysForecastActivity.class);
        tdfIntent.putExtra("WeatherItem",weatherActivityViewModel.forecastItem.getValue());
        startActivity(tdfIntent);
    }

}
