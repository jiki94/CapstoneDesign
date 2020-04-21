package com.example.expirationdateapp.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.expirationdateapp.MyApplication;
import com.example.expirationdateapp.db.Alarm;
import com.example.expirationdateapp.db.AlarmRepository;

import org.threeten.bp.LocalDate;
import org.threeten.bp.ZoneId;
import org.threeten.bp.temporal.ChronoField;
import org.threeten.bp.temporal.ChronoUnit;

public class AlarmSetter {
    static final String PRODUCT_ID = "product_id";

    private Context context;
    private AlarmManager alarmManager;
    private AlarmRepository alarmRepository;

    public AlarmSetter(Context context){
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmRepository = MyApplication.getInstance().appContainer.getAlarmRepository();
    }

    public void setAlarmBeforeOneWeek(int productId, LocalDate date){
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(PRODUCT_ID, productId);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, productId, intent, 0);

        date.minus(1, ChronoUnit.WEEKS);
        long triggerTime = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        alarmManager.set(AlarmManager.RTC, triggerTime, alarmIntent);

        Alarm alarm = new Alarm(productId);
        alarmRepository.addAlarm(alarm);
    }

    public void cancelAlarm(int productId){
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, productId, intent, 0);

        alarmManager.cancel(alarmIntent);

        Alarm alarm = new Alarm(productId);
        alarmRepository.deleteAlarm(alarm);
    }
}
