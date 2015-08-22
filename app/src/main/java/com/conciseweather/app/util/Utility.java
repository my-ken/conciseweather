package com.conciseweather.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.conciseweather.app.R;
import com.conciseweather.app.db.ConciseWeatherDB;
import com.conciseweather.app.model.DailyForecast;
import com.conciseweather.app.model.WeatherNow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by Ken on 2015/7/31.
 */
public class Utility {
    public static void handleWeatherResponse(Context context, String response){

        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

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
            String cityName;
            cityName = basic.getString("city");
            Log.d("City Name", cityName);
            saveWeatherInfo(context, updateTime, cityName);
            dailyForecast.setCityName(cityName);
            db.deleteDailyForecast(cityName);
            weatherNow.setCityName(cityName);
            db.deleteWeatherNow(cityName);
            JSONArray dailyForecasts = details.getJSONArray("daily_forecast");
            for (int i = 0; i < dailyForecasts.length(); i++){
                dailyForecast.setDate(month + context.getString(R.string.month)
                        + (day + i) + context.getString(R.string.day_of_month));
                JSONObject forecast = dailyForecasts.getJSONObject(i);
                JSONObject weatherText = forecast.getJSONObject("cond");
                String weatherTxt = weatherText.getString("txt_d");
                dailyForecast.setWeatherText(weatherTxt);
                JSONObject temp = forecast.getJSONObject("tmp");
                String tmpMin = temp.getString("min");
                dailyForecast.setMinTemp(tmpMin);
                String tmpMax = temp.getString("max");
                dailyForecast.setMaxTemp(tmpMax);
                JSONObject wind = forecast.getJSONObject("wind");
                String windDir = wind.getString("dir");
                dailyForecast.setWindDir(windDir);
                String windSc = wind.getString("sc");
                dailyForecast.setWindSc(windSc);
                Log.d("Day " + (i + 1), dailyForecast.getDate() + ";"
                        + dailyForecast.getWeatherText() + ";"
                        + dailyForecast.getMinTemp() + ";"
                        + dailyForecast.getMaxTemp() + ";"
                        + dailyForecast.getWindDir() + ";"
                        + dailyForecast.getWindSc());
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
            Log.d("WeatherNow ", nowTxt + ";" + tmpNow);
            db.saveWeatherNow(weatherNow);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void saveWeatherInfo(Context context, String updateTime, String cityName){
        SharedPreferences.Editor  editor = PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        editor.putString("update_time", updateTime);
        editor.putString("city_name", cityName);
//        editor.putBoolean("city_selected", true);
        editor.apply();
    }
}
