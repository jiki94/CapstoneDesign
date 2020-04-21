package com.example.expirationdateapp.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.expirationdateapp.MyApplication;
import com.example.expirationdateapp.db.Alarm;
import com.example.expirationdateapp.db.AlarmRepository;

import org.threeten.bp.LocalDate;
import org.threeten.bp.temporal.ChronoField;
import org.threeten.bp.temporal.ChronoUnit;
import org.threeten.bp.temporal.TemporalUnit;

public class AlarmSetter {
    static final String PRODUCT_ID = "product_id";

    private Context context;
    private AlarmRepository alarmRepository;

    public AlarmSetter(Context context){
        this.context = context;
        alarmRepository = MyApplication.getInstance().appContainer.getAlarmRepository();
    }

    public void setAlarmBeforeOneWeek(int productId, LocalDate date){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(PRODUCT_ID, productId);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, productId, intent, 0);

        date.minus(1, ChronoUnit.WEEKS);
        long triggerTime = date.getLong(ChronoField.MILLI_OF_SECOND);
        alarmManager.set(AlarmManager.RTC, triggerTime, alarmIntent);

        Alarm alarm = new Alarm(productId);
        alarmRepository.addAlarm(alarm);
    }

    public void cancelAlarm(int productId){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, productId, intent, 0);

        alarmManager.cancel(alarmIntent);

        Alarm alarm = new Alarm(productId);
        alarmRepository.deleteAlarm(alarm);
    }
}
