package com.conciseweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ken on 2015/7/30.
 */
public class ConciseWeatherOpenHelper extends SQLiteOpenHelper {

    // Create table for daily weather forecast.
    public static final String CREATE_DAILY_FORECAST = "create table daily_forecast ("
            + "_id integer primary key autoincrement, "
            + "date text, "
            + "city_name, "
            + "weather_txt text, "
            + "tmp_min integer, "
            + "tmp_max integer, "
            + "wind_dir text, "
            + "wind_sc text)";

    // Create table for weather now.
    public static final String CREATE_WEATHER_NOW = "create table weather_now ("
            + "_id integer primary key autoincrement, "
            + "city_name, "
            + "weather_txt text, "
            + "temp integer, "
            + "fl integer)";

    //Create virtual table for city data.
    public static final String CREATE_CITY = "create virtual table City using fts3("
            + "city_name_CN, "
            + "city_name_PY, "
            + "city_name_short)";

    public ConciseWeatherOpenHelper(
            Context context,
            String name,
            SQLiteDatabase.CursorFactory factory,
            int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DAILY_FORECAST);
        db.execSQL(CREATE_WEATHER_NOW);
        db.execSQL(CREATE_CITY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
