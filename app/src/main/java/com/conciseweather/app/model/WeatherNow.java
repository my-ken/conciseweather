package com.conciseweather.app.model;

/**
 * Created by Ken on 2015/7/30.
 */
public class WeatherNow {

    private String cityName;
    private String weatherText;
    private String temp;
    private String flTemp;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getWeatherText() {
        return weatherText;
    }

    public void setWeatherText(String weatherText) {
        this.weatherText = weatherText;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getFlTemp() {
        return flTemp;
    }

    public void setFlTemp(String flTemp) {
        this.flTemp = flTemp;
    }
}
