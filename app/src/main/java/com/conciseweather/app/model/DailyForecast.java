package com.conciseweather.app.model;

/**
 * Created by Ken on 2015/7/30.
 */
public class DailyForecast {

    private String date;
    private String cityName;
    private String weatherText;
    private String MinTemp;
    private String MaxTemp;
    private String windDir;
    private String windSc;

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

    public String getMinTemp() {
        return MinTemp;
    }

    public void setMinTemp(String minTemp) {
        MinTemp = minTemp;
    }

    public String getMaxTemp() {
        return MaxTemp;
    }

    public void setMaxTemp(String maxTemp) {
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

    @Override
    public String toString() {
        return "DailyForecast{" +
                "date='" + date + '\'' +
                ", cityName='" + cityName + '\'' +
                ", weatherText='" + weatherText + '\'' +
                ", MinTemp='" + MinTemp + '\'' +
                ", MaxTemp='" + MaxTemp + '\'' +
                ", windDir='" + windDir + '\'' +
                ", windSc='" + windSc + '\'' +
                '}';
    }
}
