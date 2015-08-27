package com.conciseweather.app.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import com.conciseweather.app.receiver.AutoUpdateReceive;
import com.conciseweather.app.util.HttpCallbackListener;
import com.conciseweather.app.util.HttpUtil;
import com.conciseweather.app.util.Utility;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2015/8/23.
 */
public class AutoUpdateService extends Service {

    String httpURL = "https://api.heweather.com/x3/weather?city=";
    private boolean isCreated;

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(this).edit();
        editor.putBoolean("is_start_service", true);
        editor.putBoolean("first_start_service", false);
        editor.apply();
        isCreated = true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (isCreated){
            isCreated = false;
        }else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    updateWeather();
                }
            }).start();
        }
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int hour = 3600 * 1000;
        long triggerTime = SystemClock.elapsedRealtime() + (hour * 8);
        Intent i = new Intent(this, AutoUpdateReceive.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateWeather(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if (pref.getBoolean("city_selected", false)){
            String cityName = pref.getString("select_city_name","");
            String cityNameURL = null;
            try {
                cityNameURL = URLEncoder.encode(cityName, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            HttpUtil.sendHttpRequest(httpURL + cityNameURL, new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    Utility.handleWeatherResponse(AutoUpdateService.this, response);
                    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd hh:mm:ss", Locale.CHINA);
                    Log.d("AutoUpdateService", "Update success at " + sdf.format(new Date()));
                }
                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(this).edit();
        editor.putBoolean("is_start_service", false);
        editor.apply();
    }
}
