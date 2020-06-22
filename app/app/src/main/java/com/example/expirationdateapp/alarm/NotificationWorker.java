package com.example.expirationdateapp.alarm;

import android.app.Notification;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.expirationdateapp.MyApplication;
import com.example.expirationdateapp.db.Product;

import org.threeten.bp.LocalDate;

public class NotificationWorker extends Worker {
    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        int productId = getInputData().getInt(AlarmSetter.ID_KEY, -1);
        if (productId == -1)
            throw new IllegalArgumentException();
        Log.v("WORK_TEST", "got work: " + productId);

        Product product = MyApplication.getInstance().appContainer.getProductRepository().getItem(productId);
        Log.v("WORK_TEST", "got product: " + productId);

        long daysLeft = product.getExpiryDateInDays();
        if (daysLeft > 0){
            NotificationSetter notificationSetter = new NotificationSetter(getApplicationContext());
            notificationSetter.setNotification(product);

            if (daysLeft > 1){
                AlarmSetter alarmSetter = new AlarmSetter(getApplicationContext());
                alarmSetter.setAlarm(product);
            }
        }

        Log.v("WORK_TEST", "done work: " + productId);

        return Result.success();
    }
}
