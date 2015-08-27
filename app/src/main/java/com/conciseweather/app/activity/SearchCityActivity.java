package com.conciseweather.app.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;

import com.conciseweather.app.R;
import com.conciseweather.app.db.ConciseWeatherDB;
import com.conciseweather.app.model.City;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ken on 2015/8/4.
 */
public class SearchCityActivity extends Activity implements SearchView.OnQueryTextListener{

    private SearchView searchView;
    private ListView listView;
    private ConciseWeatherDB conciseWeatherDB;
    private ProgressDialog progressDialog;
    private List<Map<String, Object>> queryResults = new ArrayList<>();
    private int[] presetCityArrID = new int[]{
            R.array.preset_city_info_China,
            R.array.preset_city_info_China_1,
            R.array.preset_city_info_China_2,
            R.array.preset_city_info_China_3,
            R.array.preset_city_info_China_4};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search_city);
        listView = (ListView) findViewById(R.id.city_list);
        searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(false);
        searchView.setQueryHint(Html.fromHtml("<font><small>"
                + getString(R.string.search_hint) + "</small></fonts>"));
        conciseWeatherDB = ConciseWeatherDB.getInstance(this);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if (pref.getBoolean("first_run", true)){
            showProgressDialog();

            new Thread(new Runnable() {

                @Override
                public void run() {
                    conciseWeatherDB.clearCity();
                    for (int cityArrID : presetCityArrID) {
                        loadCityData(cityArrID);
                    }
                    SharedPreferences.Editor editor = PreferenceManager
                            .getDefaultSharedPreferences(SearchCityActivity.this)
                            .edit();
                    editor.putBoolean("first_run", false);
                    editor.apply();
                    closeProgressDialog();
                }
            }).start();
        }


    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if (TextUtils.isEmpty(newText)){
            listView.setVisibility(View.GONE);
        }else{
            showResult(newText + "*");
            listView.setVisibility(View.VISIBLE);
        }
        return true;
    }

    public void showResult(String query){
        queryResults.clear();
        List<City> cityResult = conciseWeatherDB.queryCities(query);
        for (City city : cityResult){
            Map<String, Object> result = new HashMap<>();
            result.put("cityNameCN", city.getCityNameCN());
            result.put("cityNamePY", city.getCityNamePY());
            queryResults.add(result);
        }
        SimpleAdapter resultAdapter = new SimpleAdapter(this, queryResults,
                R.layout.query_result_item,
                new String[]{"cityNameCN", "cityNamePY"},
                new int[]{R.id.city_name_CN, R.id.city_name_PY});
        listView.setAdapter(resultAdapter);
        resultAdapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Map<String, Object> citySelectMap = queryResults.get(position);
                String citySelect = (String) citySelectMap.get("cityNamePY");
                Log.d("SearchCityActivity", citySelect);
                conciseWeatherDB.deleteWeatherNow(citySelect);
                conciseWeatherDB.deleteDailyForecast(citySelect);
                SharedPreferences.Editor editor = PreferenceManager
                        .getDefaultSharedPreferences(SearchCityActivity.this)
                        .edit();
                editor.putBoolean("city_selected", true);
                editor.putString("select_city_name",citySelect);
                editor.apply();
                Intent intent = new Intent(SearchCityActivity.this, ShowWeatherActivity.class);
                intent.putExtra("select_city", citySelect);
                startActivity(intent);
                finish();
            }
        });
    }

    public void loadCityData(int cityArrID){
        String[] cityArr = getResources().getStringArray(cityArrID);
        List<City> cities = new ArrayList<>();
        for (String array : cityArr) {
            City cityTemp = new City();
            String[] temp = array.split("\\|\\|");
            cityTemp.setCityNameCN(temp[0]);
            cityTemp.setCityNamePY(temp[1]);
            cityTemp.setCityNameShort(temp[3]);
            cities.add(cityTemp);
        }
        conciseWeatherDB.saveCity(cities);
        cities.clear();
    }

    public void showProgressDialog(){
        if (progressDialog == null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle(getString(R.string.load_city_progress_title));
            progressDialog.setMessage(getString(R.string.load_city_progress_message));
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    public void closeProgressDialog(){
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if (pref.getBoolean("city_selected", false)){
            Intent intent = new Intent(SearchCityActivity.this, ShowWeatherActivity.class);
            startActivity(intent);
        }

    }
}
