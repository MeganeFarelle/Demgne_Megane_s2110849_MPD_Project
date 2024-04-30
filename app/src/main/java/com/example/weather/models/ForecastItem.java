package com.example.weather.models;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import org.apache.commons.lang3.StringUtils;

public class ForecastItem implements Parcelable {

    public String location = "NA";

    public String id = "NA";
    public double latitude;
    public double longitude;
    public WeatherItem interest;

    public WeatherItem current;
    public WeatherItem day0;
    public WeatherItem day1;
    public WeatherItem day2;

    public ForecastItem(){
        interest = new WeatherItem();
        current = new WeatherItem();
        day0 = new WeatherItem();
        day1 = new WeatherItem();
        day2 = new WeatherItem();
    };

    protected ForecastItem(Parcel in) {
        interest = in.readParcelable(WeatherItem.class.getClassLoader());
        current = in.readParcelable(WeatherItem.class.getClassLoader());
        day0 = in.readParcelable(WeatherItem.class.getClassLoader());
        day1 = in.readParcelable(WeatherItem.class.getClassLoader());
        day2 = in.readParcelable(WeatherItem.class.getClassLoader());
        location = in.readString();
        id = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
    }

    public String getMinTemp(){
        int[] values = {
                Integer.parseInt(StringUtils.substringBefore(current.getMinTemperature(), "°")),
                Integer.parseInt(StringUtils.substringBefore(day0.getMinTemperature(), "°")),
                Integer.parseInt(StringUtils.substringBefore(day1.getMinTemperature(), "°")),
                Integer.parseInt(StringUtils.substringBefore(day2.getMinTemperature(), "°")),
        };

        int min = values[0];
        for(int val : values){
            if(val < min){
                min = val;
            }
        }

        return  min + "°";
    }

    public String getMaxTemp(){
        int[] values = {
                Integer.parseInt(StringUtils.substringBefore(current.getMaxTemperature(), "°")),
                Integer.parseInt(StringUtils.substringBefore(day0.getMaxTemperature(), "°")),
                Integer.parseInt(StringUtils.substringBefore(day1.getMaxTemperature(), "°")),
                Integer.parseInt(StringUtils.substringBefore(day2.getMaxTemperature(), "°")),
        };

        int max = values[0];
        for(int val : values){
            if(val > max ){
                max = val;
            }
        }

        return  max + "°";
    }

    public void setInterest(WeatherItem item){
        this.interest = item;
    }

    public static final Creator<ForecastItem> CREATOR = new Creator<ForecastItem>() {
        @Override
        public ForecastItem createFromParcel(Parcel in) {
            return new ForecastItem(in);
        }

        @Override
        public ForecastItem[] newArray(int size) {
            return new ForecastItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeParcelable(interest, flags);
        dest.writeParcelable(current, flags);
        dest.writeParcelable(day0, flags);
        dest.writeParcelable(day1, flags);
        dest.writeParcelable(day2, flags);
        dest.writeString(location);
        dest.writeString(id);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
    }
}
