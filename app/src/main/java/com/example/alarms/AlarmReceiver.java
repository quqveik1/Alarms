package com.example.alarms;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String TIMER_INTENT = "Timer";
    public static final String OK_INTENT = "ButtonOk";
    public static final String TOTAL_TIME = "Total time";
    public static final String MESSAGE_TYPE = "Type";



    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String type = intent.getStringExtra(MESSAGE_TYPE);
            if (type.compareTo(TIMER_INTENT) == 0)
            {
                timerReceive(context, intent);
            }
            if (type.compareTo(OK_INTENT) == 0)
            {
                okReceive(context, intent);
            }
        }
        catch (Exception e)
        {
            Toast.makeText(context, "Ошибка AlarmReceiver: " + e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    private void okReceive(Context context, Intent intent)
    {
        try
        {
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.cancel(timerNotificationId);
        }
        catch (Exception e)
        {
            Toast.makeText(context, "Ошибка AlarmReceiver: " + e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    private void timerReceive(Context context, Intent intent)
    {
        try
        {
            long time = intent.getLongExtra(TOTAL_TIME, 0);

            if (AppStatus.getStatus() == 0) {
                makeNotification(time, context);
            } else {
                context.startActivity(createAlarmIntent(context, time));
            }
        }
        catch (Exception e)
        {
            Toast.makeText(context, "Ошибка AlarmReceiver: " + e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    public static Intent createAlarmIntent (Context context, long time)
    {
        Intent alarmActivityIntent = new Intent(context, AlarmActivity.class);
        alarmActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        alarmActivityIntent.putExtra("Total time", time);
        return  alarmActivityIntent;
    }

    public final int timerNotificationId = 1;

    private void makeNotification(long totalTime, Context context)
    {
        try {
            String textTime = AlarmReceiver.stringTime(totalTime);
            Intent alarmIntent = createAlarmIntent(context, totalTime);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, alarmIntent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

            Intent okIntent = new Intent(context, AlarmReceiver.class);
            okIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            okIntent.putExtra(MESSAGE_TYPE, OK_INTENT);
            PendingIntent okPendingIntent = PendingIntent.getBroadcast(context, 0, okIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);


            @SuppressLint("NotificationTrampoline") NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MainActivity.MainNotificationChannel)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("Сработал таймер")
                    .setContentText(textTime)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .addAction(R.drawable.ic_launcher_background, "OK", okPendingIntent);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.notify(timerNotificationId, builder.build());
        }
        catch (Exception e)
        {
            Toast.makeText(context, "Ошибка AlarmReceiver в создании уведомления: " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public static String stringTime(long timeInMilliseconds)
    {
        long[] time = AlarmReceiver.normalizeTime(timeInMilliseconds);
        long hours = time[0];
        long minutes = time[1];
        long seconds = time[2];
        String outputStr = "";
        if (hours == 0)
        {
            if (minutes == 0)
            {
                outputStr = String.format("%d секунд", seconds);
            }
            else
            {
                outputStr = String.format("%d:%02d", minutes, seconds);
            }
        }
        else
        {
            outputStr = String.format("%d:%02d:%02d", hours, minutes, seconds);
        }
        /*
        if (hours != 0)
        {
            outputStr += hours + " часов ";
        }
        if (minutes != 0)
        {
            outputStr += minutes + " минут ";
        }
        if (seconds != 0)
        {
            outputStr += seconds + " cекунд ";
        }*/
        return outputStr;
    }

    public static long[] normalizeTime(long timeInMilliseconds)
    {
        long hours = timeInMilliseconds / (3600 * 1000);
        long minutes = (timeInMilliseconds -(hours * 3600 * 1000)) / (60 * 1000);
        long seconds = (timeInMilliseconds - (hours * 3600 * 1000) - (minutes * 60 * 1000)) / 1000;
        long[] returnable = new long[3];
        returnable[0] = hours;
        returnable[1] = minutes;
        returnable[2] = seconds;
        return returnable;
    }

}
