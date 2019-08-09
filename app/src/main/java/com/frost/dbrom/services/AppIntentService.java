package com.frost.dbrom.services;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.frost.dbrom.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

/**
 * Created by Gokul Kalagara (Mr. Psycho) on 09-08-2019.
 * <p>
 * Frost
 */
public class AppIntentService extends IntentService {


    public AppIntentService() {
        super(AppIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        ResultReceiver resultReceiver = intent.getParcelableExtra("ResultReceiver");
        if (resultReceiver != null) {
            try {
                Log.d("URL-INSIDE",intent.getStringExtra("Url"));

                URL url = new URL(intent.getStringExtra("Url"));
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));

                if (bufferedReader != null) {
                    String content = null, line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        Log.d("DATA URL",line);
                        content+=line;
                    }
                    bufferedReader.close();

                    Bundle bundle = new Bundle();
                    bundle.putString("Result", content);
                    notifyUrl(intent.getStringExtra("Url"));
                    resultReceiver.send(1, bundle);
                    return;

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            resultReceiver.send(0, null);

        }


    }

    private void notifyUrl(String url)
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"INOTES")
                                            .setColor(Color.parseColor("#000000"))
                                            .setSmallIcon(R.drawable.notification)
                                            .setContentTitle("Download Url")
                                            .setContentText(url);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("INOTES","Notes",NotificationManager.IMPORTANCE_HIGH));
        }
        notificationManager.notify(new Random().nextInt(999)+1,builder.build());


    }
}
