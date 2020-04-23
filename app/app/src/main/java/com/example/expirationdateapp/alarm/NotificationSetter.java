package com.example.expirationdateapp.alarm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.expirationdateapp.R;
import com.example.expirationdateapp.db.Product;
import com.example.expirationdateapp.viewing.ViewFragment;

import org.threeten.bp.LocalDate;
import org.threeten.bp.Period;

// 백그라운드에서 작동한다고 판단함
public class NotificationSetter {
    static final String ALARM_CHANNEL_ID = "alarm_channel_id";
    public static final String ALARM_EXTRA_NAME = "alarm_extra_id";

    private Context context;

    public NotificationSetter(Context context){
        this.context = context;
    }

    // 모듈내에서 호출
    void setNotification(Product product){
        Period period = Period.between(LocalDate.now(), product.expiryDate);
        String title = context.getString(R.string.text_notification_title);
        String content;
        if (period.getDays() == 0){
            content = context.getString(R.string.text_notification_zero_content, product.name);
        }else{
           content = context.getString(R.string.text_notification_content, product.name, period.getDays());
        }

        // TODO: 아이콘 바꾸기
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ALARM_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(product.id, builder.build());
    }

    // mainActivity에서 호출
    public void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String name = context.getString(R.string.text_channel_name);
            String desc = context.getString(R.string.text_channel_desc);
            NotificationChannel channel = new NotificationChannel(ALARM_CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(desc);

            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }
    }
}
