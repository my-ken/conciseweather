package com.conciseweather.app.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.conciseweather.app.R;
import com.conciseweather.app.model.DailyForecast;

import java.util.List;

/**
 * Created by Ken on 2015/8/4.
 */
public class DailyAdapter extends ArrayAdapter<DailyForecast> {

    private int resourceId;

    public DailyAdapter(Context context, int resource, List<DailyForecast> objects) {
        super(context, resource, objects);
        resourceId = resource;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DailyForecast dailyForecast = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView dateText = (TextView) view.findViewById(R.id.date);
        TextView  weatherText = (TextView) view.findViewById(R.id.weather_text);
        TextView tempMin = (TextView) view.findViewById(R.id.temp_min);
        TextView tempMax = (TextView) view.findViewById(R.id.temp_max);
        TextView windDir = (TextView) view.findViewById(R.id.wind_dir);
        TextView windSc = (TextView) view.findViewById(R.id.wind_sc);
        switch (position){
            case 0:
                dateText.setText("今天");
                break;
            case 1:
                dateText.setText("明天");
                break;
            case 2:
                dateText.setText("后天");
                break;
            default:
                dateText.setText(dailyForecast.getDate());
        }
        weatherText.setText(dailyForecast.getWeatherText());
        tempMin.setText(dailyForecast.getMinTemp());
        tempMax.setText(dailyForecast.getMaxTemp());
        windDir.setText(dailyForecast.getWindDir());
        windSc.setText(dailyForecast.getWindSc());
        return view;
    }
}
