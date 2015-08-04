package com.conciseweather.app.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.conciseweather.app.R;
import com.conciseweather.app.db.ConciseWeatherDB;
import com.conciseweather.app.model.DailyForecast;
import com.conciseweather.app.model.WeatherNow;
import com.conciseweather.app.util.HttpCallbackListener;
import com.conciseweather.app.util.HttpUtil;
import com.conciseweather.app.util.Utility;

import java.util.List;

public class ShowWeatherActivity extends Activity {

    private static String httpURL = "http://apis.baidu.com/heweather/weather/free?city=";

    private TextView titleMain;
    private TextView cityNameText;
    private TextView updateTimeText;
    private TextView tempText;
    private TextView weatherText;
    private DailyAdapter adapter;
    private ListView dailyForecastListView;
    private List<DailyForecast> dailyForecastList;
    private String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show_weather);
        titleMain = (TextView) findViewById(R.id.title_main);
        cityNameText = (TextView) findViewById(R.id.city_name);
        updateTimeText = (TextView) findViewById(R.id.update_time);
        tempText = (TextView) findViewById(R.id.temp_now);
        weatherText = (TextView) findViewById(R.id.weather_text_now);
        adapter = new DailyAdapter(this, R.layout.daily_item, dailyForecastList);
        dailyForecastListView = (ListView) findViewById(R.id.daily_forecast_lv);
        dailyForecastListView.setAdapter(adapter);
        cityName = getIntent().getStringExtra("city_name");
        if (!TextUtils.isEmpty(cityName)){
            titleMain.setText("同步中...");
            queryWeatherInfo(cityName);
        }else {
            showWeather();
        }
    }

    private void queryWeatherInfo(String cityName){
        String address = httpURL + cityName;
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Utility.handleWeatherResponse(ShowWeatherActivity.this, response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWeather();
                    }
                });
            }

            @Override
            public void onErro(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        titleMain.setText("同步失败");
                    }
                });

            }
        });

    }

    private void showWeather(){
        SharedPreferences prefs = PreferenceManager.
                getDefaultSharedPreferences(this);
        ConciseWeatherDB db = ConciseWeatherDB.getInstance(this);
        updateTimeText.setText(prefs.getString("update_time", ""));
        titleMain.setText("Concise Weather");
        WeatherNow weatherNow = db.loadWeatherNow(cityName);
        cityNameText.setText(weatherNow.getCityName());
        tempText.setText(weatherNow.getTemp());
        weatherText.setText(weatherNow.getWeatherText());
        dailyForecastList = db.loadDailyForecast(cityName);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_weather, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
