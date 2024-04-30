package com.example.weather.ui.activities;


import android.content.Intent;
import android.os.Bundle;

import com.example.weather.R;
import com.example.weather.WeatherApplication;
import com.example.weather.databinding.MapActivityBinding;
import com.example.weather.models.ForecastItem;
import com.example.weather.ui.view_models.MapActivityViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    MapActivityBinding binding;

    WeatherApplication application;

    ArrayList<ForecastItem> forecastItems;

    MapActivityViewModel viewModel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MapActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MapActivityViewModel.class);

        binding.setMapActivity(this);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        application = (WeatherApplication) getApplication();
        forecastItems = application.forecastItems;

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

//        MapView mapView = findViewById(R.id.mapView);
//        mapView.onCreate(savedInstanceState);
//        mapView.getMapAsync(this);

        viewModel.searchQuery.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {

                for(ForecastItem item : forecastItems){
                    if(item.location.toLowerCase().contains(s.toString().toLowerCase())){
                        LatLng loc = new LatLng(item.latitude, item.longitude);
                        viewModel.googleMap.getValue().moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 10f));
                        break;
                    }
                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        viewModel.setMap(googleMap, forecastItems);
    }

    public void back(){
        finish();
    }

    public void home(){
        Intent intent = new Intent(MapActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void weather(){
        Intent intent = new Intent(MapActivity.this, WeatherActivity.class);
        intent.putExtra("WeatherItem", application.currentLocationWeather);
        startActivity(intent);
        finish();
    }

    public void weather2(){
        Intent intent = new Intent(MapActivity.this, WeatherActivity.class);
        intent.putExtra("WeatherItem", viewModel.infoItem.getValue());
        startActivity(intent);
        finish();
    }

}
