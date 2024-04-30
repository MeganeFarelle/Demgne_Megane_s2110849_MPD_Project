package com.example.weather.models;

import android.os.Parcel;
import java.util.Locale;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import com.example.weather.R;

import org.apache.commons.lang3.StringUtils;

public class WeatherItem implements Parcelable {

    public int number;

    public String day = "NA";

    public String date = "NA";

    public String sunrise = "NA";

    public String sunset = "NA";

    public String condition = "Clear Sky";

    public String tips = "You can go out!";

    public int conditionImageRes;

    public double latitude;

    public double longitude;

    public int temperature = -404;

    public int minTemperature = -404;

    public int maxTemperature = -404;

    public String windDirection = "NA";

    public String windSpeed = "NA";

    public String humidity = "NA";

    public String pressure ="NA";

    public String visibility = "NA";

    public String uv_risk = "NA";

    public String pubDate = "NA";

    public String HPString = String.format(Locale.ENGLISH,"RH : %s, hPa : %s", humidity, pressure);

    public WeatherItem(){
        conditionImageRes = R.drawable.sunny;
        setConditionDetails();
    }

    protected WeatherItem(Parcel in) {
        day = in.readString();
        condition = in.readString();
        conditionImageRes = in.readInt();
        temperature = in.readInt();
        maxTemperature = in.readInt();
        minTemperature = in.readInt();
        windDirection = in.readString();
        windSpeed = in.readString();
        humidity = in.readString();
        pressure = in.readString();
        visibility = in.readString();
        pubDate = in.readString();
        HPString = in.readString();
        uv_risk = in.readString();
        date = in.readString();
        sunrise = in.readString();
        sunset = in.readString();

        setConditionDetails();
    }

    public static final Creator<WeatherItem> CREATOR = new Creator<WeatherItem>() {
        @Override
        public WeatherItem createFromParcel(Parcel in) {
            return new WeatherItem(in);
        }

        @Override
        public WeatherItem[] newArray(int size) {
            return new WeatherItem[size];
        }
    };

    public void setConditionDetails(){
        if (condition.equalsIgnoreCase("Hazy")){
            conditionImageRes = R.drawable.light_cloud;
            tips = "Limit outdoor activities!";
        }else if (condition.equalsIgnoreCase("Light Rain")){
            conditionImageRes = R.drawable.rain;
            tips = "Carry an umbrella!";
        } else if (condition.equalsIgnoreCase("Light Rain Showers")){
            conditionImageRes = R.drawable.rain;
            tips = "Drive carefully!";
        }else if (condition.equalsIgnoreCase("Heavy Rain")){
            conditionImageRes = R.drawable.heavy_rain;
            tips = "Stay indoors!";
        }else if (condition.equalsIgnoreCase("Sunny Intervals")){
            conditionImageRes = R.drawable.sun;
            tips = "Wear sunscreen!";
        }else if (condition.equalsIgnoreCase("Sunny")){
            conditionImageRes = R.drawable.sun;
            tips = "Enjoy the sunshine!";
        }else if (condition.equalsIgnoreCase("Drizzle")){
            conditionImageRes = R.drawable.nn;
            tips = "Spend sometime indoors!";
        } else if (condition.equalsIgnoreCase("Mostly Cloudy")){
            conditionImageRes = R.drawable.mostly_cloudy;
            tips = "Go for a nature walk!";
        }else if (condition.equalsIgnoreCase("Partly Cloudy")){
            conditionImageRes = R.drawable.partly_cloudy;
            tips = "Appreciate the beauty!";
        }else if (condition.equalsIgnoreCase("Light Cloud")){
            conditionImageRes = R.drawable.light_cloud;
            tips = "Enjoy a meal outdoors!";
        } else if (condition.equalsIgnoreCase("Mist")){
            conditionImageRes = R.drawable.image;
            tips = "Embrace the cozy atmosphere!";
        }else if (condition.equalsIgnoreCase("Thundering")){
            conditionImageRes = R.drawable.thunder;
            tips = "Move indoors!";
        } else if (condition.equalsIgnoreCase("Thundery Showers")){
            tips = "Close windows and doors!";
            conditionImageRes = R.drawable.pluvieuse;
        }else if (condition.equalsIgnoreCase("Snow")){
            tips = "Wear multiple layers of clothing!";
            conditionImageRes = R.drawable.snow;
        }
    }

    public void setCondition(String value){
        if(!value.equalsIgnoreCase("not available")
                && !value.equalsIgnoreCase("null")
                && !value.startsWith("--")
                && !value.isEmpty()){
            condition = value;
        }

        setConditionDetails();
    }

    public void setSunrise(String value){
        if(!value.equalsIgnoreCase("not available")
                && !value.equalsIgnoreCase("null")
                && !value.startsWith("--")
                && !value.isEmpty()){
            sunrise = value;
        }
    }

    public void setSunset(String value){
        if(!value.equalsIgnoreCase("not available")
                && !value.equalsIgnoreCase("null")
                && !value.startsWith("--")
                && !value.isEmpty()){
            sunset = value;
        }
    }

    public void setTemperature(String value){
        if(!value.equalsIgnoreCase("not available")
                && !value.equalsIgnoreCase("null")
                && !value.startsWith("--")
                && !value.isEmpty()){
            temperature = Integer.parseInt(StringUtils.substringBefore(value, "°"));
        }
    }

    public void setMaxTemperature(String value){
        if(!value.equalsIgnoreCase("not available")
                && !value.equalsIgnoreCase("null")
                && !value.startsWith("--")
                && !value.isEmpty()){
            maxTemperature = Integer.parseInt(StringUtils.substringBefore(value, "°"));
        }
    }

    public void setMinTemperature(String value){
        if(!value.equalsIgnoreCase("not available")
                && !value.equalsIgnoreCase("null")
                && !value.startsWith("--")
                && !value.isEmpty()){
            minTemperature = Integer.parseInt(StringUtils.substringBefore(value, "°"));
        }
    }

    public void setWindDirection(String value){
        if(!value.equalsIgnoreCase("Direction not available")
                && !value.equalsIgnoreCase("null")
                && !value.startsWith("--")
                && !value.isEmpty()){
            windDirection = extractLeadingCharacters(value);
        }
    }

    public void setWindSpeed(String value){
        if(!value.equalsIgnoreCase("not available")
                && !value.equalsIgnoreCase("null")
                && !value.startsWith("--")
                && !value.isEmpty()){
            windSpeed = value;
        }
    }

    public void setHumidity(String value){
        if(!value.equalsIgnoreCase("not available")
                && !value.equalsIgnoreCase("null")
                && !value.startsWith("--")
                && !value.isEmpty()){
            humidity = value;
        }

        HPString = String.format(Locale.ENGLISH,"RH : %s, hPa : %s", humidity, pressure);
    }

    public void setPressure(String value){
        if(!value.equalsIgnoreCase("not available")
                && !value.equalsIgnoreCase("null")
                && !value.startsWith("--")
                && !value.isEmpty()){
            pressure = value;
        }

        HPString = String.format(Locale.ENGLISH,"RH : %s, hPa : %s", humidity, pressure);
    }

    public void setVisibility(String value){
        if(!value.equalsIgnoreCase("not available")
                && !value.equalsIgnoreCase("null")
                && !value.startsWith("--")
                && !value.isEmpty()){

            visibility = value;

        }
    }

    public String getTemperature(){

        int temp = 0;
        if(temperature != -404){
            temp = temperature;
        }else if(minTemperature != -404 && maxTemperature != -404){
            temp = (Integer) ((minTemperature + maxTemperature)/2);
        }else if(maxTemperature != -404){
            temp = maxTemperature - 5;
        }else if(minTemperature != -404){
            temp = minTemperature + 2;
        }

        return Integer.toString(temp) + "°";
    }

    public String getMinTemperature(){

        int temp = 0;
        if(minTemperature != -404){
            temp = minTemperature;
        }else if(temperature != -404){
            temp = temperature - 2;
        }else if(maxTemperature != -404){
            temp = maxTemperature - 5;
        }

        return Integer.toString(temp) + "°";
    }

    public String getMaxTemperature(){

        int temp = 0;
        if(maxTemperature != -404){
            temp = maxTemperature;
        }else if(temperature != -404){
            temp = temperature + 3;
        }else if(minTemperature != -404){
            temp = minTemperature + 5;
        }

        return Integer.toString(temp) + "°";
    }

    @NonNull
    @Override
    public String toString(){
        return "WeatherItem {" +
                "day='" + day + '\'' +
                ", condition='" + condition + '\'' +
                ", temperature='" + temperature + '\'' +
                ", windDirection='" + windDirection + '\'' +
                ", windSpeed='" + windSpeed + '\'' +
                ", visibility='" + visibility + '\'' +
                ", Pressure='" + pressure + '\'' +
                ", humidity='" + humidity + '\'' +
                ", pubDate='" + pubDate + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(day);
        dest.writeString(condition);
        dest.writeInt(conditionImageRes);
        dest.writeInt(temperature);
        dest.writeInt(maxTemperature);
        dest.writeInt(minTemperature);
        dest.writeString(windDirection);
        dest.writeString(windSpeed);
        dest.writeString(humidity);
        dest.writeString(pressure);
        dest.writeString(visibility);
        dest.writeString(pubDate);
        dest.writeString(HPString);
        dest.writeString(uv_risk);
        dest.writeString(date);
        dest.writeString(sunrise);
        dest.writeString(sunset);
    }

    public static String extractLeadingCharacters(String input) {
        StringBuilder resultBuilder = new StringBuilder();

        // Split the input string into individual words
        String[] words = input.split("\\s+"); // Split by whitespace

        // Iterate through each word and extract its leading character
        for (String word : words) {
            if (!word.isEmpty()) { // Skip empty words
                // Extract the leading character
                char leadingChar = word.charAt(0);
                // Append the leading character to the result
                resultBuilder.append(leadingChar);
            }
        }

        // Convert the StringBuilder to a String
        return resultBuilder.toString();
    }
}
