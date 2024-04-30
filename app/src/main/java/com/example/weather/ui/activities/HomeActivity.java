package com.example.weather.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.WeatherApplication;
import com.example.weather.databinding.HomeActivityBinding;
import com.example.weather.helpers.ForecastParser;
import com.example.weather.models.ForecastItem;
import com.example.weather.adapters.WeatherItemsAdapter;
import com.example.weather.ui.view_models.HomeActivityViewModel;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    HomeActivityBinding binding;
    WeatherApplication application;
    WeatherItemsAdapter weatherItemAdapter;
    RecyclerView.LayoutManager layoutManager;
    HomeActivityViewModel homeActivityViewModel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomeActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        homeActivityViewModel = new ViewModelProvider(this).get(HomeActivityViewModel.class);
        binding.setViewModel(homeActivityViewModel);
        binding.setHomeActivity(this);

        application = (WeatherApplication) getApplication();

        weatherItemAdapter = new WeatherItemsAdapter(this, new ArrayList<>());
        weatherItemAdapter.setOnClickListener(data -> {
            Intent intent = new Intent(HomeActivity.this, WeatherActivity.class);
            data.interest = data.current;
            intent.putExtra("WeatherItem", data);
            startActivity(intent);
        });

        homeActivityViewModel.forecastItems.observe(
                this, forecastItems -> weatherItemAdapter.resetData(forecastItems));

        homeActivityViewModel.searchQuery.observe(
                this, s -> weatherItemAdapter.filterData(s));

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.weatherItems.setLayoutManager(layoutManager);
        binding.weatherItems.setAdapter(weatherItemAdapter);
        binding.weatherItems.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = 50;
            }
        });

        if (!isNetworkAvailable(this)){
            homeActivityViewModel.optionsVisibility.setValue(View.GONE);
            homeActivityViewModel.optionsVisibility.setValue(View.GONE);
            homeActivityViewModel.loadingVisibility.setValue(View.GONE);
            homeActivityViewModel.noInternetVisibility.setValue(View.VISIBLE);
            return;
        }

        if(weatherItemAdapter.getItemCount() == 0){
            homeActivityViewModel.loadingVisibility.setValue(View.VISIBLE);
        }else{
            homeActivityViewModel.loadingVisibility.setValue(View.GONE);
        }

        new ForecastParser().ParseData(data -> runOnUiThread(() -> {

            application.forecastItems.clear();
            application.forecastItems.addAll(data);

            homeActivityViewModel.setForecastItems(data);

            homeActivityViewModel.loadingVisibility.setValue(View.GONE);
            homeActivityViewModel.optionsVisibility.setValue(View.VISIBLE);
            homeActivityViewModel.searchVisibility.setValue(View.VISIBLE);

            for(ForecastItem item : data){
                if(item.location.contains("Mauritius")){
                    application.currentLocationWeather = item;
                }
            }
        }));

        binding.setLifecycleOwner(this);
    }

    public void map(){
        Intent intent = new Intent(HomeActivity.this, MapActivity.class);
        startActivity(intent);
    }

    public void weather(){
        Intent intent = new Intent(HomeActivity.this, WeatherActivity.class);
        intent.putExtra("WeatherItem", application.currentLocationWeather);
        startActivity(intent);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

}

