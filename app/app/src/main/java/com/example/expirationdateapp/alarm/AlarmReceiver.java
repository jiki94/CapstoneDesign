package com.example.expirationdateapp.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.expirationdateapp.MyApplication;
import com.example.expirationdateapp.db.Alarm;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int productId = intent.getIntExtra(AlarmSetter.PRODUCT_ID, -1);

        Log.v("TESTING_ALARM", "received " + productId);

        Alarm alarm = new Alarm(productId);
        MyApplication.getInstance().appContainer.getAlarmRepository().deleteAlarm(alarm);

        // start service to show notification
        // service will show notification and also set next alarm i think
    }
}
