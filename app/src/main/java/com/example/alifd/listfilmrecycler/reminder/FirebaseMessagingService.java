package com.example.alifd.listfilmrecycler.reminder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.alifd.listfilmrecycler.BuildConfig;
import com.example.alifd.listfilmrecycler.MainActivity;
import com.example.alifd.listfilmrecycler.R;
import com.google.firebase.messaging.RemoteMessage;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import timber.log.Timber;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    final String Api_key = BuildConfig.API_KEY_V3;
    String dateFormat = "yyyy-MM-dd";
    int notifId = 100;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Timber.e("Token fcm : %s", s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);
        if(remoteMessage.getNotification()!=null){
            if(remoteMessage.getNotification().getTitle().equals("Daily Reminder")) {
                sendNotification(remoteMessage.getNotification().getBody());
            }else{
                getReminderNewFilm();
            }
        }
    }


    private void sendNotification(String messageBody) {
        String channelId  = getString(R.string.default_notification_channel_id);
        String channelName = getString(R.string.default_notification_channel_name);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_panorama_fish_eye_black_24dp)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /* Create or update. */
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationBuilder.setChannelId(channelId);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(channel);
            }
        }
        Notification notification = notificationBuilder.build();
        if (mNotificationManager != null) {
            mNotificationManager.notify(0, notification);
        }
    }

    private void getReminderNewFilm(){
        Handler mainHandler = new Handler(Looper.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                AsyncHttpClient client = new AsyncHttpClient();
                String date_gte = new SimpleDateFormat(dateFormat, Locale.getDefault()).format(new Date());
                String date_lte = new SimpleDateFormat (dateFormat, Locale.getDefault()).format(new Date());
                String url = "https://api.themoviedb.org/3/discover/movie?api_key="+Api_key+"&primary_release_date.gte="+date_gte+"&primary_release_date.lte="+date_lte;
                Timber.e("get New Films: %s", url );
                client.get(url, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String result = new String(responseBody);
                        Timber.e(result);
                        try {
                            JSONObject responseObject = new JSONObject(result);
                            JSONArray jsonArray = responseObject.getJSONArray("results");
                            for (int x=0; x<jsonArray.length(); x++) {
                                String title = "New Film has been release";
                                String message = jsonArray.getJSONObject(x).getString("title");
                                Timber.e(message + " id = "+notifId);

                                sendNotificationNewRelease(title, message, notifId);
                                notifId++;
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    }
                });
            }
        };
        mainHandler.post(myRunnable);

    }

    private void sendNotificationNewRelease(String title, String messageBody, int chId) {
        String channelId  = String.valueOf(chId);
        String channelName = channelId;
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setContentTitle(title)
                        .setSmallIcon(R.drawable.ic_panorama_fish_eye_black_24dp)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /* Create or update. */
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationBuilder.setChannelId(channelId);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(channel);
            }
        }
        Notification notification = notificationBuilder.build();
        if (mNotificationManager != null) {
            mNotificationManager.notify(0, notification);
        }
    }
}
