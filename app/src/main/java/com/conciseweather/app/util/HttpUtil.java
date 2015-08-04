package com.conciseweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ken on 2015/7/31.
 */
public class HttpUtil {

    private static String apiKey = "3c5ee96fd6f53ce53da0b6b945834842";

    public static void sendHttpRequest(final String address,
                                       final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("apikey", apiKey);
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null){
                        response.append(line);
                    }
                    if (listener != null){
                        listener.onFinish(response.toString());
                    }

                }catch (Exception e){
                    if (listener != null){
                        listener.onErro(e);
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
