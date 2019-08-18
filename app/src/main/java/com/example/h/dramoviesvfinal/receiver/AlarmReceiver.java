package com.example.h.dramoviesvfinal.receiver;

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
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.example.h.dramoviesvfinal.R;
import com.example.h.dramoviesvfinal.service.ReleaseMovieIntentService;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    private final static int ID_DAILY = 101;
    private final static int ID_RELEASE = 102;

    public final static String TYPE_DAIlY = "daily";
    public final static String TYPE_RELEASE = "release";
    public static final String EXTRA_TYPE = "extra_type";
    public static final String EXTRA_MESSAGE = "extra_message";

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = context.getResources().getString(R.string.app_name);
        String message = intent.getStringExtra(EXTRA_MESSAGE);

        showAlarmNotification(context,title,message, ID_DAILY);
    }

    public void showAlarmNotification(Context context, String title, String message, int notifId) {
        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "AlarmManager channel";

        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.color.transparent)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        /*
        Untuk android Oreo ke atas perlu menambahkan notification channel
        Materi ini akan dibahas lebih lanjut di modul extended
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            /* Create or update. */
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            builder.setChannelId(CHANNEL_ID);

            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();

        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(notifId, notification);
        }

    }

    public void cancelAlarm(Context context, String type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        int requestCode = type.equalsIgnoreCase(TYPE_DAIlY) ? ID_DAILY : ID_RELEASE;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        pendingIntent.cancel();

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

        if(requestCode == ID_DAILY) {
            Toast.makeText(context, context.getResources().getString(R.string.textview_preferences_daily_off), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, context.getResources().getString(R.string.textview_preferences_release_off), Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isAlarmSet(Context context) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        int requestCode = ID_DAILY;

        return PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_NO_CREATE) != null;
    }

    public void setRepeatingAlarm(Context context, String time, String message, String type) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TYPE, type);

        String timeArray[] = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_DAILY, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }

        Toast.makeText(context, context.getResources().getString(R.string.textview_preferences_daily_on), Toast.LENGTH_SHORT).show();
    }

    public void setReleaseAlarm(Context context, String time, String type){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseMovieIntentService.class);
        intent.putExtra(EXTRA_TYPE, type);

        String timeArray[] = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getService(context, ID_RELEASE, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }

        Toast.makeText(context, context.getResources().getString(R.string.textview_preferences_release_on), Toast.LENGTH_SHORT).show();
    }
}
