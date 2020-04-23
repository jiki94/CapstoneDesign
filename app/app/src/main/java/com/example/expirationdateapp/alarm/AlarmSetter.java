package com.example.expirationdateapp.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.expirationdateapp.MyApplication;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;
import org.threeten.bp.Period;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.temporal.ChronoField;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.concurrent.TimeUnit;

public class AlarmSetter {
    static final String ID_KEY = "id_key";

    private Context context;

    public AlarmSetter(Context context){
        this.context = context;
    }

    public void setAlarm(int productId, LocalDate date){
        LocalDate alarmDate = LocalDate.from(date);
        alarmDate.minus(1, ChronoUnit.WEEKS);
        Duration duration = Duration.between(ZonedDateTime.now(), alarmDate.atStartOfDay(ZoneId.systemDefault()));
        if (duration.isNegative()){
            alarmDate = LocalDate.from(date);
            alarmDate.minus(3, ChronoUnit.DAYS);
            duration = Duration.between(ZonedDateTime.now(), alarmDate.atStartOfDay(ZoneId.systemDefault()));

            if (duration.isNegative()){
                alarmDate = LocalDate.from(date);
                alarmDate.minus(1, ChronoUnit.DAYS);
                duration = Duration.between(ZonedDateTime.now(), alarmDate.atStartOfDay(ZoneId.systemDefault()));

                if (duration.isNegative()){
                    duration = Duration.ZERO;
                }
            }
        }

        long durationSeconds = duration.getSeconds();
        Data data = new Data.Builder().putInt(ID_KEY, productId).build();

        // setInitialDelay 함수는 Duration 받는것도 있지만 그건 java.time 라이브러리꺼
        // 현재 사용하는 Duration은 java7에도 호환되는 threeabp 꺼
        OneTimeWorkRequest alarmWorkRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                //.setInitialDelay(durationSeconds, TimeUnit.SECONDS) // TODO: 디버깅시에만 코멘트 평소에는 코멘트 처리 없게
                .addTag(String.valueOf(productId))
                .setInputData(data)
                .build();

        Log.v("WORK_TEST","sec" +  durationSeconds);
        WorkManager.getInstance(context).enqueue(alarmWorkRequest);
    }

    public void cancelAlarm(int productId){
        WorkManager.getInstance(context).cancelAllWorkByTag(String.valueOf(productId));
    }
}
