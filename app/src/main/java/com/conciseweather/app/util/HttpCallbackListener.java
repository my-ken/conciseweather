package com.conciseweather.app.util;

/**
 * Created by Administrator on 2015/7/31.
 */
public interface HttpCallbackListener {

    void onFinish(String response);

    void onErro(Exception e);
}
