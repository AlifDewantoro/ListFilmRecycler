package com.example.alifd.listfilmrecycler.reminder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.core.app.NotificationCompat;

import com.example.alifd.listfilmrecycler.BuildConfig;
import com.example.alifd.listfilmrecycler.MainActivity;
import com.example.alifd.listfilmrecycler.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import timber.log.Timber;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String DAILY_REMINDER = "DailyReminder";
    public static final String NEW_FILMS = "NewFils";
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_TITLE = "title";
    String dateFormat = "yyyy-MM-dd";

    private final int ALARM_ID_DAILY_REMINDER = 90;
    private final int ALARM_ID_NEW_FILMS = 91;
    private static final int channel_id_reminder = 100;
    private int channel_id_new_films = 101;
    public static final String DAILY_REMINDER_CH = "Daily Reminder";
    public static final String NEW_FILMS_CH = "New Films";

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent!=null) {
            String title = intent.getStringExtra(EXTRA_TITLE);
            String message = intent.getStringExtra(EXTRA_MESSAGE);

            if(title.equals(DAILY_REMINDER)){
                sendNotificationDaily(context, message);
            }else if(title.equals(NEW_FILMS)){
                getReminderNewFilm(context);
            }else {
                Timber.e("NO DEFINED TYPE");
            }
        }
    }

    public void setNewReleaseNotif(Context context, String title, String message){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_MESSAGE, message);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ALARM_ID_NEW_FILMS, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public void setDailyReminderNotif(Context context, String title, String message){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_MESSAGE, message);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ALARM_ID_DAILY_REMINDER, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    private void sendNotificationDaily(Context context, String messageBody) {
        String channelId  = String.valueOf(channel_id_reminder);
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.ic_panorama_fish_eye_black_24dp)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /* Create or update. */
            NotificationChannel channel = new NotificationChannel(channelId, DAILY_REMINDER_CH, NotificationManager.IMPORTANCE_DEFAULT);
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

    private void getReminderNewFilm(Context context){
        Handler mainHandler = new Handler(Looper.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                AsyncHttpClient client = new AsyncHttpClient();
                String date_gte = new SimpleDateFormat(dateFormat, Locale.getDefault()).format(new Date());
                String date_lte = new SimpleDateFormat (dateFormat, Locale.getDefault()).format(new Date());
                String url = "https://api.themoviedb.org/3/discover/movie?api_key="+ BuildConfig.API_KEY_V3 +"&primary_release_date.gte="+date_gte+"&primary_release_date.lte="+date_lte;
                Timber.e("get New Films: %s", url );
                client.get(url, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String result = new String(responseBody);
                        Timber.e(result);
                        String title="";
                        String message="";
                        try {
                            JSONObject responseObject = new JSONObject(result);
                            JSONArray jsonArray = responseObject.getJSONArray("results");
                            for (int x=0; x<jsonArray.length(); x++) {
                                title = "New Film has been release";
                                message += jsonArray.getJSONObject(x).getString("title");
                                if(x!=jsonArray.length()-1){
                                    message += ", ";
                                }
                                Timber.e(message + " id = "+channel_id_new_films);

                            }
                            sendNotificationNewRelease(context, title, message, channel_id_new_films);
                            //channel_id_new_films++;
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

    private void sendNotificationNewRelease(Context context, String title, String messageBody, int chId) {
        String channelId  = String.valueOf(chId);
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("from_notif", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setContentTitle(title)
                        .setSmallIcon(R.drawable.ic_panorama_fish_eye_black_24dp)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /* Create or update. */
            NotificationChannel channel = new NotificationChannel(channelId, NEW_FILMS_CH, NotificationManager.IMPORTANCE_HIGH);
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

    public void stopNotification(Context context, String type){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        int requestCode = type.equalsIgnoreCase(DAILY_REMINDER) ? ALARM_ID_DAILY_REMINDER : ALARM_ID_NEW_FILMS;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        pendingIntent.cancel();
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

    }
}
