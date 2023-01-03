package com.example.alarms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button timerStartButton;
    private EditText editViewHours;
    private EditText editViewMinutes;
    private EditText editViewSeconds;

    private static boolean canSCHEDULE_EXACT_ALARM = true;
    //private static final int SCHEDULE_EXACT_ALARM = 57;
    private static boolean canUSE_EXACT_ALARM = true;
    //private static final int USE_EXACT_ALARM = 1329;

    public static final String MainNotificationChannel = "MainNotificationChannel";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*

        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if (result)
                        {
                            SCHEDULE_EXACT_ALARM = true;
                        }

                    }
                });

         */

        timerStartButton = findViewById(R.id.timerStartButton);
        editViewHours = findViewById(R.id.editViewHours);
        editViewMinutes = findViewById(R.id.editViewMinutes);
        editViewSeconds = findViewById(R.id.editViewSeconds);


        timerStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    long timeInMillis = 0;
                    long hours = 0;
                    long minutes = 0;
                    long seconds = 0;

                    if (!editViewHours.getText().toString().isEmpty())
                        hours = Integer.parseInt(editViewHours.getText().toString());
                    if (!editViewMinutes.getText().toString().isEmpty())
                        minutes = Integer.parseInt(editViewMinutes.getText().toString());
                    if (!editViewSeconds.getText().toString().isEmpty())
                        seconds = Integer.parseInt(editViewSeconds.getText().toString());

                    timeInMillis = (hours * 3600 + minutes * 60 + seconds) * 1000;
                    makeTimer(timeInMillis);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Ошибка постановка таймера: " + e.toString(), Toast.LENGTH_LONG).show();
                }

            }
        });

        createMainNotificationChannel();



    }

    @Override
    protected void onResume() {
        super.onResume();
        AppStatus.setActive();
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppStatus.setInActive();
    }

    private void createMainNotificationChannel()
    {
        try {
            NotificationChannel notificationChannel = new NotificationChannel(MainNotificationChannel, "Таймер", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "Ошибка создания канала уведомлений: " + e.toString(), Toast.LENGTH_LONG).show();
        }

    }



    private void makeTimer(long timeInMillis)
    {
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        long curTime = System.currentTimeMillis();
        //checkPermission();
        if (canSCHEDULE_EXACT_ALARM) {
            try {
                Intent alarmIntent = new Intent(this, AlarmReceiver.class);
                alarmIntent.putExtra(AlarmReceiver.MESSAGE_TYPE, AlarmReceiver.TIMER_INTENT);
                alarmIntent.putExtra(AlarmReceiver.TOTAL_TIME, timeInMillis);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, curTime + timeInMillis, pendingIntent);
                Toast.makeText(getApplicationContext(), "Таймер поставлен", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Ошибка постановка таймера: " + e.toString(), Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Нет необходимого разрешения", Toast.LENGTH_LONG).show();

        }
    }


    /*
    private boolean checkPermission()
    {
        canSCHEDULE_EXACT_ALARM = (ContextCompat.checkSelfPermission(this, Manifest.permission.SCHEDULE_EXACT_ALARM) == PackageManager.PERMISSION_GRANTED);
        if (canSCHEDULE_EXACT_ALARM) return true;
        else
        {
            try {
                ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.SCHEDULE_EXACT_ALARM }, SCHEDULE_EXACT_ALARM);
                /*
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getApplicationContext().startActivity(intent);


            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), "Ошибка постановка таймера: " + e.toString(), Toast.LENGTH_LONG).show();
            }

        }



        canUSE_EXACT_ALARM = (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (canUSE_EXACT_ALARM) return true;
        else
        {
            try {
                ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, USE_EXACT_ALARM);

            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), "Ошибка постановка таймера: " + e.toString(), Toast.LENGTH_LONG).show();
            }

        }

        return false;
    }

     */



    private void makeAlarm()
    {

        /*
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
         */
    }
}

    /*

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SCHEDULE_EXACT_ALARM)
        {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED))
            {
                Toast.makeText(getApplicationContext(), "Программа работать не будет", Toast.LENGTH_LONG).show();
            }
            else
            {
                canSCHEDULE_EXACT_ALARM = true;
            }
        }

        if (requestCode == USE_EXACT_ALARM)
        {

            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(getApplicationContext(), "Программа работать не будет", Toast.LENGTH_LONG).show();
            } else {
                canUSE_EXACT_ALARM = true;
            }

        }
    }
    */
    /*
    private static final int WRITE_STORAGE = 57;
    private static final int READ_STORAGE = 1329;

    private void checkPermission()
    {
        boolean write =
                ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED;
        boolean read =
                ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED;

        if (write == false)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, WRITE_STORAGE);
        }

        if (read == false)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, READ_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == WRITE_STORAGE)
        {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED))
            {
                Toast toast = Toast.makeText(this, "ДЛЯ РАБОТЫ НЕОБХОДИМ ДОСТУП К ПАМЯТИ!!!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }


    };
     */
