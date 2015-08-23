package com.conciseweather.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.conciseweather.app.service.AutoUpdateService;

/**
 * Created by Administrator on 2015/8/23.
 */
public class AutoUpdateReceive extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, AutoUpdateService.class);
        context.startService(i);
    }
}
