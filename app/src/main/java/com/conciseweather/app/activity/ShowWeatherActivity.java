package com.conciseweather.app.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.conciseweather.app.R;
import com.conciseweather.app.db.ConciseWeatherDB;
import com.conciseweather.app.model.DailyForecast;
import com.conciseweather.app.model.WeatherNow;
import com.conciseweather.app.service.AutoUpdateService;
import com.conciseweather.app.util.HttpCallbackListener;
import com.conciseweather.app.util.HttpUtil;
import com.conciseweather.app.util.Utility;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ShowWeatherActivity extends Activity implements View.OnClickListener{

    private TextView titleMain;
    private TextView cityNameText;
    private TextView publishTimeText;
    private TextView updateTimeText;
    private TextView tempText;
    private TextView weatherText;
    private DailyAdapter adapter;
    private Button switchCityBtn;
    private Button updateWeatherBtn;
    private ListView dailyForecastListView;
    private List<DailyForecast> dailyForecastList = new ArrayList<>();
    private ConciseWeatherDB conciseWeatherDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conciseWeatherDB = ConciseWeatherDB.getInstance(this);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if (!pref.getBoolean("city_selected", false)){
            Intent intent = new Intent(ShowWeatherActivity.this, SearchCityActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show_weather);
        switchCityBtn = (Button) findViewById(R.id.switch_city_btn);
        switchCityBtn.setOnClickListener(this);
        updateWeatherBtn = (Button) findViewById(R.id.update_weather_btn);
        updateWeatherBtn.setOnClickListener(this);
        titleMain = (TextView) findViewById(R.id.title_main);
        cityNameText = (TextView) findViewById(R.id.city_name);
        updateTimeText = (TextView) findViewById(R.id.update_time);
        publishTimeText = (TextView) findViewById(R.id.publish_time);
        tempText = (TextView) findViewById(R.id.temp_now);
        weatherText = (TextView) findViewById(R.id.weather_text_now);
        adapter = new DailyAdapter(this, R.layout.daily_item, dailyForecastList);
        dailyForecastListView = (ListView) findViewById(R.id.daily_forecast_lv);
        dailyForecastListView.setAdapter(adapter);
        String selectCity = getIntent().getStringExtra("select_city");
        if (!TextUtils.isEmpty(selectCity)){
            titleMain.setText(getString(R.string.updating));
            queryWeatherInfo(selectCity);
        }else {
            showWeather();
        }
    }

    private void queryWeatherInfo(String cityName){
        String cityNameURL = null;
        try {
            cityNameURL = URLEncoder.encode(cityName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String httpURL = "https://api.heweather.com/x3/weather?city=";
        String address = httpURL + cityNameURL;
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
            public void onError(Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        titleMain.setText(getString(R.string.app_name));
                        SharedPreferences.Editor editor = PreferenceManager
                                .getDefaultSharedPreferences(ShowWeatherActivity.this).edit();
                        editor.putBoolean("city_selected", false);
                        editor.apply();
                        AlertDialog.Builder alertDialog = new AlertDialog
                                .Builder(ShowWeatherActivity.this);
                        alertDialog.setTitle(getString(R.string.update_failed));
                        alertDialog.setMessage(getString(R.string.alert_message));
                        alertDialog.setCancelable(true);
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alertDialog.show();
                    }
                });

            }
        });

    }

    private void showWeather(){
        SharedPreferences prefs = PreferenceManager.
                getDefaultSharedPreferences(this);
        ConciseWeatherDB db = ConciseWeatherDB.getInstance(this);
        String cityName = prefs.getString("city_name", "");
        publishTimeText.setText(prefs.getString("publish_time", ""));
        updateTimeText.setText(prefs.getString("update_time", ""));
        titleMain.setText(getString(R.string.app_name));
        WeatherNow weatherNow = db.loadWeatherNow(cityName);
        cityNameText.setText(weatherNow.getCityName());
        tempText.setText(weatherNow.getTemp() + getString(R.string.celsius));
        weatherText.setText(weatherNow.getWeatherText());
        dailyForecastList.clear();
        dailyForecastList = db.loadDailyForecast(cityName);
        adapter.clear();
        adapter.addAll(dailyForecastList);
        adapter.notifyDataSetChanged();
        if (!(prefs.getBoolean("is_start_service", false))){
            Intent i = new Intent(ShowWeatherActivity.this, AutoUpdateService.class);
            startService(i);
        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.switch_city_btn:
                Intent intent = new Intent(ShowWeatherActivity.this, SearchCityActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.update_weather_btn:
                titleMain.setText(getString(R.string.updating));
                conciseWeatherDB.deleteDailyForecast(cityNameText.getText().toString());
                queryWeatherInfo(cityNameText.getText().toString());
        }
    }
}
