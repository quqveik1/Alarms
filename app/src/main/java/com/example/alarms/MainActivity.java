package com.example.alarms;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private AlarmService alarmService = new AlarmService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        makeAlarm();
    }

    private void makeAlarm()
    {

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.putExtra(AS_TIMER, data);
        PendingIntent pendingIntent = null;

        try {


            pendingIntent = PendingIntent.getActivity(this, 1329, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }

        AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(System.currentTimeMillis() + 2000, pendingIntent);

        Intent amIntent = new Intent(this, AlarmActivity.class);
        amIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent amPendingIntent = PendingIntent.getActivity(this,57, amIntent, PendingIntent.FLAG_UPDATE_CURRENT| PendingIntent.FLAG_MUTABLE);

        try
        {
            am.setAlarmClock(alarmClockInfo, amPendingIntent);
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }

        Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();


        //am.setAlarmClock(alarmClockInfo, );
    }
}