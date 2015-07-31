package com.conciseweather.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.conciseweather.app.model.DailyForecast;
import com.conciseweather.app.model.WeatherNow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ken on 2015/7/30.
 */
public class ConciseWeatherDB {

    public static final String DB_NAME = "concise_weather";
    public static final int VERSION = 1;
    private static ConciseWeatherDB conciseWeatherDB;
    private SQLiteDatabase db;

    //Make the constructor to be private.
    private ConciseWeatherDB(Context context){
        ConciseWeatherOpenHelper dbHelper =
                new ConciseWeatherOpenHelper(context,DB_NAME,null,VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public synchronized static ConciseWeatherDB getInstance(Context context){
        if (conciseWeatherDB == null){
            conciseWeatherDB = new ConciseWeatherDB(context);
        }
        return conciseWeatherDB;
    }

    public void saveDailyForecast(DailyForecast dailyForecast){
        if (dailyForecast != null){
            String cityName = dailyForecast.getCityName();
            ContentValues values = new ContentValues();
            values.put("date", dailyForecast.getDate());
            values.put("city_name", dailyForecast.getCityName());
            values.put("weather_txt", dailyForecast.getWeatherText());
            values.put("tmp_min", dailyForecast.getMinTemp());
            values.put("tmp_max", dailyForecast.getMaxTemp());
            values.put("wind_dir", dailyForecast.getWindDir());
            values.put("wind_sc", dailyForecast.getWindSc());
            db.delete("daily_forecast","city_name = ?", new String[] {cityName});
            db.insert("daily_forecast",null,values);
        }
    }

    public void saveWeatherNow(WeatherNow weatherNow){
        if (weatherNow != null){
            String cityName = weatherNow.getCityName();
            ContentValues values = new ContentValues();
            values.put("city_name", weatherNow.getCityName());
            values.put("weather_txt", weatherNow.getWeatherText());
            values.put("temp", weatherNow.getTemp());
            values.put("fl", weatherNow.getFlTemp());
            db.delete("weather_now", "city_name = ?", new String[] {cityName});
            db.insert("weather_now", null, values);
        }
    }

    public List<DailyForecast> loadDailyForecast(String cityName){
        List<DailyForecast> list = new ArrayList<DailyForecast>();
        Cursor cursor = db.query("daily_forecast", null, "city_name = ?",
                new String[] {cityName}, "date", null, null);
        if (cursor.moveToFirst()){
            do {
                DailyForecast dailyForecast = new DailyForecast();
                dailyForecast.setDate(cursor.getString(cursor.getColumnIndex("date")));
                dailyForecast.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                dailyForecast.setMinTemp(cursor.getInt(cursor.getColumnIndex("tmp_min")));
                dailyForecast.setMaxTemp(cursor.getInt(cursor.getColumnIndex("tmp_max")));
                dailyForecast.setWeatherText(cursor.getString(cursor.getColumnIndex("weather_txt")));
                dailyForecast.setWindDir(cursor.getString(cursor.getColumnIndex("win_dir")));
                dailyForecast.setWindSc(cursor.getString(cursor.getColumnIndex("win_sc")));
                list.add(dailyForecast);
            }while (cursor.moveToNext());
        }
        if (cursor != null){
            cursor.close();
        }
        return list;
    }

    public WeatherNow loadWeatherNow(String cityName){
        WeatherNow weatherNow = new WeatherNow();
        Cursor cursor = db.query("weather_now",null,"city_name = ?",
                new String[] {cityName}, null, null, null);
        if (cursor.moveToFirst()){
            do{
                weatherNow.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                weatherNow.setWeatherText(cursor.getString(cursor.getColumnIndex("weather_txt")));
                weatherNow.setTemp(cursor.getInt(cursor.getColumnIndex("temp")));
            }while (cursor.moveToNext());
        }
        if (cursor != null){
            cursor.close();
        }
        return weatherNow;
    }

}
