package com.conciseweather.app.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ken on 2015/7/31.
 */
public class HttpUtil {

    private final static String apiKey = "&key=20f6b1845279488f9add22048dba9c93";

    public static void sendHttpRequest(final String address,
                                       final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address + apiKey);
                    Log.d("Address", address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null){
                        response.append(line);
                    }
                    Log.d("Http Response", response.toString());
                    if (listener != null){
                        listener.onFinish(response.toString());
                    }
                }catch (Exception e){
                    if (listener != null){
                        listener.onError(e);
                    }
                }finally {
                    if (connection != null){
                        connection.disconnect();
                    }
                }
            }
        }).start();

    }
}
