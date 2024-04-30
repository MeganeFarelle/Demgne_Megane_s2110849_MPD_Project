package com.example.weather.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.weather.WeatherApplication;
import com.example.weather.databinding.ThreeDaysForecastActivityBinding;
import com.example.weather.ui.view_models.ThreeDaysForecastActivityViewModel;

public class ThreeDaysForecastActivity extends AppCompatActivity {

    ThreeDaysForecastActivityBinding binding;
    ThreeDaysForecastActivityViewModel viewModel;
    public int zero = 0;
    public int one = 1;
    public int two = 2;

    WeatherApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ThreeDaysForecastActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(ThreeDaysForecastActivityViewModel.class);

        Intent intent = getIntent();
        viewModel.forecastItem.setValue(intent.getParcelableExtra("WeatherItem"));

        binding.setViewModel(viewModel);
        binding.setForecastActivity(this);

        application = (WeatherApplication) getApplication();
    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        viewModel.forecastItem.setValue(intent.getParcelableExtra("WeatherItem"));
    }

    public void day(int no){
        if(no == 0){
            viewModel.setInterest(viewModel.forecastItem.getValue().day0);
        }else if(no == 1){
            viewModel.setInterest(viewModel.forecastItem.getValue().day1);
        }else if(no == 2){
            viewModel.setInterest(viewModel.forecastItem.getValue().day2);
        }

        Intent intent = new Intent(ThreeDaysForecastActivity.this, WeatherActivity.class);
        intent.putExtra("WeatherItem", viewModel.forecastItem.getValue());
        startActivity(intent);
        finish();
    }

    public void back(){
        finish();
    }

    public void home(){
        Intent intent = new Intent(ThreeDaysForecastActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void map(){
        Intent intent = new Intent(ThreeDaysForecastActivity.this, MapActivity.class);
        startActivity(intent);
    }

    public void weather(){
        Intent intent = new Intent(ThreeDaysForecastActivity.this, WeatherActivity.class);
        intent.putExtra("WeatherItem", application.currentLocationWeather);
        startActivity(intent);
        finish();
    }

}
