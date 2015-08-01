package com.conciseweather.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.conciseweather.app.db.ConciseWeatherDB;
import com.conciseweather.app.model.DailyForecast;
import com.conciseweather.app.model.WeatherNow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ken on 2015/7/31.
 */
public class Utility {
    public static void handleWeatherResponse(Context context, String response){
        DailyForecast dailyForecast = new DailyForecast();
        WeatherNow weatherNow = new WeatherNow();
        ConciseWeatherDB db = ConciseWeatherDB.getInstance(context);
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray array = jsonObject.getJSONArray("HeWeather data service 3.0");
            JSONObject details = array.getJSONObject(0);
            JSONObject basic = details.getJSONObject("basic");
            JSONObject update = basic.getJSONObject("update");
            String updateTime = update.getString("loc");
            saveWeatherInfo(context, updateTime);
            String cityName = basic.getString("city");
            dailyForecast.setCityName(cityName);
            db.deleteDailyForecast(cityName);
            weatherNow.setCityName(cityName);
            db.deleteWeatherNow(cityName);
            JSONArray dailyForecasts = details.getJSONArray("daily_forecast");
            for (int i = 0; i < dailyForecasts.length(); i++){
                JSONObject forecast = dailyForecasts.getJSONObject(i);
                JSONObject weatherText = forecast.getJSONObject("cond");
                String weatherTxt = weatherText.getString("txt_d");
                dailyForecast.setWeatherText(weatherTxt);
                String date = forecast.getString("date");
                dailyForecast.setDate(date);
                JSONObject temp = forecast.getJSONObject("tmp");
                String tmpMin = temp.getString("min");
                dailyForecast.setMinTemp(tmpMin);
                String tmpMax = temp.getString("max");
                dailyForecast.setMaxTemp(tmpMax);
                JSONObject wind = forecast.getJSONObject("wind");
                String winDir = wind.getString("dir");
                dailyForecast.setWindDir(winDir);
                String winSc = wind.getString("sc");
                dailyForecast.setWindSc(winSc);
                db.saveDailyForecast(dailyForecast);
            }
            JSONObject now = details.getJSONObject("now");
            JSONObject nowText = now.getJSONObject("cond");
            String nowTxt = nowText.getString("txt");
            weatherNow.setWeatherText(nowTxt);
            String tmpFl = now.getString("fl");
            weatherNow.setFlTemp(tmpFl);
            String tmpNow = now.getString("tmp");
            weatherNow.setTemp(tmpNow);
            db.saveWeatherNow(weatherNow);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void saveWeatherInfo(Context context, String updateTime){
        SharedPreferences.Editor  editor = PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        editor.putString("update_time", updateTime);
        editor.apply();
    }
}
