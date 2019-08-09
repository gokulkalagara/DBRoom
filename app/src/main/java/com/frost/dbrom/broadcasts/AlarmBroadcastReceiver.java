package com.frost.dbrom.broadcasts;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.frost.dbrom.R;

import java.util.Random;

/**
 * Created by Gokul Kalagara (Mr. Psycho) on 09-08-2019.
 * <p>
 * Frost
 */
public class AlarmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"INOTES")
                                            .setContentTitle("Alarm Receive")
                                            .setContentText(""+ intent.getStringExtra("text"))
                                            .setSmallIcon(R.drawable.notification)
                                            .setColor(Color.parseColor("#228899"));


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        notificationManager.createNotificationChannel(new NotificationChannel("INOTES","Notes",NotificationManager.IMPORTANCE_HIGH));
        notificationManager.notify(new Random().nextInt(9999)+1,builder.build());
    }
}
