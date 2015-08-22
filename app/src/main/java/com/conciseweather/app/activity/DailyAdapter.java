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
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.dateText = (TextView) view.findViewById(R.id.date);
            viewHolder.dailyWeatherText = (TextView) view.findViewById(R.id.weather_text);
            viewHolder.dailyWeatherMaxTemp = (TextView) view.findViewById(R.id.temp_max);
            viewHolder.dailyWeatherMinTemp = (TextView) view.findViewById(R.id.temp_min);
            viewHolder.dailyWeatherWindDir = (TextView) view.findViewById(R.id.wind_dir);
            viewHolder.dailyWeatherWindSc = (TextView) view.findViewById(R.id.wind_sc);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.dateText.setText(dailyForecast.getDate());
        viewHolder.dailyWeatherText.setText(dailyForecast.getWeatherText());
        viewHolder.dailyWeatherMinTemp.setText(dailyForecast.getMinTemp()
                + getContext().getString(R.string.celsius));
        viewHolder.dailyWeatherMaxTemp.setText(dailyForecast.getMaxTemp()
                + getContext().getString(R.string.celsius));
        viewHolder.dailyWeatherWindDir.setText(dailyForecast.getWindDir());
        viewHolder.dailyWeatherWindSc.setText(dailyForecast.getWindSc());
        return view;
    }

    class ViewHolder {
        TextView dateText;
        TextView dailyWeatherText;
        TextView dailyWeatherMinTemp;
        TextView dailyWeatherMaxTemp;
        TextView dailyWeatherWindDir;
        TextView dailyWeatherWindSc;
    }
}
