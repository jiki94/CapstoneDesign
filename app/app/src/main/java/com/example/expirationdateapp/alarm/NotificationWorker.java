package com.example.expirationdateapp.alarm;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class NotificationWorker extends Worker {
    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        // set notificatioon
        int productId = Integer.decode(getTags().iterator().next());
        Log.v("WORK_TEST", "got work: " + productId);
        return Result.success();
    }
}
