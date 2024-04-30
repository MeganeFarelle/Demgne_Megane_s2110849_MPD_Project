package com.example.weather.ui.view_models;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weather.models.ForecastItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class MapActivityViewModel extends ViewModel {

    float mapZoom = 0.1f;

    public MutableLiveData<String> searchQuery = new MutableLiveData<>();

    HashMap<Marker, ForecastItem> makerDataMap = new HashMap<>();

    public MutableLiveData<ForecastItem> infoItem = new MutableLiveData<>();

    public MutableLiveData<GoogleMap> googleMap = new MutableLiveData<>();

    public MutableLiveData<Integer> detailsOn = new MutableLiveData<>(View.GONE);

    public void setMap(GoogleMap map, ArrayList<ForecastItem> items){

        googleMap.setValue(map);

        map.animateCamera(CameraUpdateFactory.zoomTo(mapZoom));

        for(ForecastItem item : items){
            LatLng location = new LatLng(item.latitude, item.longitude);
            Marker marker = map.addMarker(new MarkerOptions().position(location).title(item.location));
            makerDataMap.put(marker, item);
        }

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {

                map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                //map.animateCamera(CameraUpdateFactory.zoomTo(5f));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 10f));

                infoItem.setValue(makerDataMap.get(marker));
                detailsOn.setValue(View.VISIBLE);

                return true;
            }
        });
    }
}
