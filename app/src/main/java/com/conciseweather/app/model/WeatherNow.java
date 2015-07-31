package com.conciseweather.app.model;

/**
 * Created by Ken on 2015/7/30.
 */
public class WeatherNow {

    private int id;
    private String cityName;
    private String weatherText;
    private int temp;
    private int flTemp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int getFlTemp() {
        return flTemp;
    }

    public void setFlTemp(int flTemp) {
        this.flTemp = flTemp;
    }
}
