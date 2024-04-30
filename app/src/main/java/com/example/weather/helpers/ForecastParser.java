package com.example.weather.helpers;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.util.Xml;

import com.example.weather.interfaces.ForecastParserListener;
import com.example.weather.models.ForecastItem;
import com.example.weather.models.WeatherItem;

import org.apache.commons.lang3.StringUtils;
import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ForecastParser {

    // Method to fetch weather data from BBC and parse it

    ForecastParserListener dataListener;

    public void ParseData(ForecastParserListener listener){
        dataListener = listener;

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                ArrayList<ForecastItem> weatherItems = new ArrayList<>();

                String[][] listOfPlaces = {
                        {"Glasgow, Scotland", "2648579"},
                        {"London, England", "2643743"},
                        {"New York, USA", "5128581"},
                        {"Muscat, Oman", "287286"},
                        {"Pamplemouses, Mauritius", "934154"},
                        {"Dhaka, Bangladesh","1185241"},
                };

                for(String[] place : listOfPlaces){
                    weatherItems.add(getForecastData(place));
                }

                if(dataListener != null){
                    dataListener.onDataReady(weatherItems);
                }
            }
        });
    }

    public ForecastItem getForecastData(String[] place){
        ForecastItem forecastItem = new ForecastItem();
        forecastItem.location = place[0];
        forecastItem.id = place[1];

        WeatherItem current = getCurrentWeatherData(forecastItem.id);

        for(WeatherItem item : getThreeDaysWeatherData(forecastItem.id)){

            if(item.number == 0){
                forecastItem.day0 = item;
                forecastItem.day0.date = getDate(current.pubDate, 0);

                forecastItem.current = forecastItem.day0;
                forecastItem.current.setTemperature(item.getTemperature());

                forecastItem.setInterest(forecastItem.current);

                forecastItem.latitude = item.latitude;
                forecastItem.longitude = item.longitude;

            }else if(item.number == 1){
                forecastItem.day1 = item;
                forecastItem.day1.date = getDate(current.pubDate, 1);

            }else if(item.number == 2){
                forecastItem.day2 = item;
                forecastItem.day2.date = getDate(current.pubDate, 2);

            }
        }

        return forecastItem;
    }

    public WeatherItem getCurrentWeatherData(String id){
        String urlString = "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/";
        ArrayList<WeatherItem> data = getWeatherData(urlString, id);

        if(data.size() > 0){
            return data.get(0);
        }else{
            return new WeatherItem();
        }
    }

    public ArrayList<WeatherItem> getThreeDaysWeatherData(String id){
        String urlString = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/";
        return getWeatherData(urlString, id);
    }

    public ArrayList<WeatherItem> getWeatherData(String url_string, String id) {

        ArrayList<WeatherItem> data = new ArrayList<>();

        int count = 0;
        WeatherItem item = new WeatherItem();

        // URL of the BBC weather XML feed
        String urlString = url_string + id;

        //System.out.println("url: " + urlString);

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            // Starts the query
            conn.connect();
            InputStream stream = conn.getInputStream();

            // Initialize XMLPullParser
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            parser.setInput(stream, null);

            // Parse XML data
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {

                //Handle start tags
                String tagName = parser.getName();
                //System.out.println("Tag name: " + tagName);

                switch (eventType) {

                    case XmlPullParser.END_TAG:
                        if(tagName.equals("item")) {
                            data.add(item);
                            count = count + 1;

                            //System.out.println("Item: " + item.toString());
                            //System.out.println("End Item");
                        }

                        break;

                    case XmlPullParser.START_TAG:

                        if(tagName.equals("item")){
                            //System.out.println("Start Item");
                            item = new WeatherItem();
                            item.number = count;

                        } else if(tagName.equals("title")) {
                            // Parse title tag
                            String title = parser.nextText();
                            //System.out.println("Title: " + title);

                            String[] titleArray = title.split(",");
                            String[] dayConditionArray = titleArray[0].trim().replace(" - ", ":").split(":");
                            //System.out.println("DayCondition: " + Arrays.toString(dayConditionArray));

                            if(dayConditionArray.length == 2){
                                item.day = dayConditionArray[0].trim();
                                item.setCondition(dayConditionArray[1].trim());

                            }else if (dayConditionArray.length > 2){
                                item.day = dayConditionArray[0].trim();
                                item.setCondition(dayConditionArray[3].trim());

                            }
                            //System.out.println("Condition: " + item.condition);

                        }else if(tagName.equals("pubDate")){
                            // Parse publication date tag

                            String pubDate = parser.nextText();
                            //System.out.println("pubDate: " + pubDate);
                            item.pubDate = pubDate;

                            String[] split = pubDate.split(" ");
                            item.date = split[2] + ", " + split[1];

                        }else if(tagName.equals("point")){
                            String[] geo = parser.nextText().trim().split(" ");
                            item.latitude = Double.parseDouble(geo[0]);
                            item.longitude = Double.parseDouble(geo[1]);

                        } else if (tagName.equals("description")) {
                            // Parse description tag

                            String description = parser.nextText();
                            //System.out.println("Description: " + description);

                            // Extract weather parameters from the description
                            String[] params = description.split(",");
                            for (String param : params) {
                                param = param.trim();

                                if (param.startsWith("Temperature:")) {
                                    item.setTemperature(StringUtils.substringBefore(param.split(":")[1].trim(), "("));
                                    //System.out.println("Temperature: " + item.temperature);

                                }else if (param.startsWith("Maximum Temperature:")) {
                                    item.setMaxTemperature(StringUtils.substringBefore(param.split(":")[1].trim(), "("));
                                    //System.out.println("Max Temperature: " + item.maxTemperature);

                                } else if (param.startsWith("Minimum Temperature:")) {
                                    item.setMinTemperature(StringUtils.substringBefore(param.split(":")[1].trim(), "("));
                                    //System.out.println("Min Temperature: " + item.minTemperature);

                                } else if (param.startsWith("Humidity:")) {
                                    item.setHumidity(param.split(":")[1].trim());
                                    //System.out.println("Humidity: " + item.humidity);

                                } else if (param.startsWith("Wind Direction:")) {
                                    item.setWindDirection(param.split(":")[1].trim());
                                    //System.out.println("Wind Direction: " + item.windDirection);

                                } else if (param.startsWith("Wind Speed: ")) {
                                    item.setWindSpeed(param.split(":")[1].trim());
                                    //System.out.println("Wind Speed: " + item.windSpeed);

                                } else if (param.startsWith("Pressure: ")) {
                                    item.setPressure(param.split(":")[1].trim());
                                    //System.out.println("Pressure: " + item.pressure);

                                }else if (param.startsWith("Visibility: ")) {
                                    item.setVisibility(param.split(":")[1].trim());
                                    //System.out.println("Visibility: " + item.visibility);

                                }else if (param.startsWith("UV Risk: ")) {
                                    item.uv_risk = param.split(":")[1].trim();
                                    //System.out.println("UV Risk: " + item.uv_risk);

                                }else if (param.startsWith("Sunset: ")) {
                                    String[] dta = param.split(":");
                                    String time = dta[1].trim() + ":" + dta[2].trim();
                                    item.setSunset(time);
                                    //System.out.println("Sunset: " + item.sunset);

                                }else if (param.startsWith("Sunrise: ")) {
                                    String[] dta = param.split(":");
                                    String time = dta[1].trim() + ":" + dta[2].trim();
                                    item.setSunrise(time);
                                    //System.out.println("Sunrise: " + item.sunrise);
                                }

                            }

                        }

                        break;
                }

                eventType = parser.next();
            }
            // Close the InputStream and disconnect the HttpURLConnection

            stream.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    public String getDate(String now, int days) {

        try {
            SimpleDateFormat inputDf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.getDefault());
            SimpleDateFormat outputDf = new SimpleDateFormat("EEE, MMM dd", Locale.getDefault());

            Calendar calendar = Calendar.getInstance();
            Date currentDate = inputDf.parse(now);
            calendar.setTime(currentDate);

            // Add one day to the current date

            calendar.add(Calendar.DAY_OF_YEAR, days);

            Date nextDayDate = calendar.getTime();
            return outputDf.format(nextDayDate);

        }catch (ParseException e){
            e.printStackTrace();
            return now;
        }

    }

}
