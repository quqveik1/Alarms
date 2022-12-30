package com.example.alarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


public class AlarmService extends BroadcastReceiver {
    private final String AS_TIMER = "TIMER";

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "ALARM!", Toast.LENGTH_SHORT).show();

    }

    public void setTimer(Context context, int ms, String data)
    {

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmService.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.putExtra(AS_TIMER, data);



        //am.(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1, pendingIntent);
        try {
            //am.setAlarmClock();
        }
        catch (Exception e)
        {
            Toast.makeText(context, "Alarm error", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(context, "Alarm made", Toast.LENGTH_SHORT).show();


    }
}
