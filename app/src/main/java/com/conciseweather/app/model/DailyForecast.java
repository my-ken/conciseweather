package com.conciseweather.app.model;

/**
 * Created by Ken on 2015/7/30.
 */
public class DailyForecast {

    private int id;
    private String date;
    private String cityName;
    private String weatherText;
    private int MinTemp;
    private int MaxTemp;
    private String windDir;
    private String windSc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public int getMinTemp() {
        return MinTemp;
    }

    public void setMinTemp(int minTemp) {
        MinTemp = minTemp;
    }

    public int getMaxTemp() {
        return MaxTemp;
    }

    public void setMaxTemp(int maxTemp) {
        MaxTemp = maxTemp;
    }

    public String getWindDir() {
        return windDir;
    }

    public void setWindDir(String windDir) {
        this.windDir = windDir;
    }

    public String getWindSc() {
        return windSc;
    }

    public void setWindSc(String windSc) {
        this.windSc = windSc;
    }
}
