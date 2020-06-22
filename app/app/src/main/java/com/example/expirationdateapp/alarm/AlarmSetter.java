package com.example.expirationdateapp.alarm;

import android.content.Context;
import android.os.Debug;
import android.util.Log;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.expirationdateapp.db.Product;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;
import org.threeten.bp.Period;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.concurrent.TimeUnit;

import static org.threeten.bp.temporal.ChronoUnit.DAYS;

public class AlarmSetter {
    static final String ID_KEY = "id_key";

    private Context context;
    //private AlarmSetRepository alarmSetRepository;

    public AlarmSetter(Context context){
        this.context = context;
        //this.alarmSetRepository = alarmSetRepository;
    }

    public void setAlarm(Product product){
        long daysLeft = product.getExpiryDateInDays();
        Log.v("ALARM_TEST", "Days Left: " + daysLeft);
        int minusAmount = 0;
        if (daysLeft > 7){
            minusAmount = 7;
        }else if (daysLeft > 0){
            minusAmount = 1;
        }else{
            return; // 유통기한 이미 지난 경우에는 알람 세팅 안함
        }

        ZonedDateTime zonedDueDate = product.expiryDate.minusDays(minusAmount-1).atStartOfDay(ZoneId.systemDefault());
        Duration duration = Duration.between(ZonedDateTime.now(), zonedDueDate);

        long durationSeconds = duration.getSeconds();
        if (durationSeconds < 0)
            durationSeconds = 0;

        Log.v("ALARM_TEST", "Duration in hour: " + Duration.ofSeconds(durationSeconds).toHours());

        Data data = new Data.Builder().putInt(ID_KEY, product.id).build();

        // setInitialDelay 함수는 Duration 받는것도 있지만 그건 java.time 라이브러리꺼
        // 현재 사용하는 Duration은 java7에도 호환되는 threeabp 꺼
        OneTimeWorkRequest alarmWorkRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                .setInitialDelay(durationSeconds, TimeUnit.SECONDS) // TODO: 디버깅시에만 코멘트 평소에는 코멘트 처리 없게
                .addTag(String.valueOf(product.id))
                .setInputData(data)
                .build();

        Log.v("WORK_TEST","sec" +  durationSeconds);
        WorkManager.getInstance(context).enqueue(alarmWorkRequest);
    }

    public void cancelAlarm(int productId){
        WorkManager.getInstance(context).cancelAllWorkByTag(String.valueOf(productId));
    }

    private long getExpiryDateInDays(LocalDate date){
        return ChronoUnit.DAYS.between(LocalDate.now(), date.plusDays(1));
    }
}
