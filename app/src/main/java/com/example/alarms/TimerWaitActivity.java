package com.example.alarms;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TimerWaitActivity extends AppCompatActivity {

    class CDTimer extends CountDownTimer
    {

        public CDTimer(long finishTime, long countDownInterval) {
            super(finishTime, countDownInterval);
        }

        @Override
        public void onTick(long untilFinishTime) {
            setTimeView(untilFinishTime);
        }

        @Override
        public void onFinish() {
            Intent finishIntent = AlarmReceiver.createAlarmIntent(getApplicationContext(), timerTime);
            startActivity(finishIntent);

        }
    }

    private long timerTime = 0;
    private final long deltaTime = 1000;

    private TextView RestTimeView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);

        RestTimeView = findViewById(R.id.RestTimeView);

        try {
            timerTime = getIntent().getLongExtra(AlarmReceiver.TOTAL_TIME, 0);
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "Ошибка получения времени таймера в TimerWaitActivity: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        CDTimer cdTimer = new CDTimer(timerTime, deltaTime);
        cdTimer.start();
    }

    private void setTimeView(long restTime)
    {
        String restTimeText = AlarmReceiver.stringTime(restTime + 1000);

        RestTimeView.setText(restTimeText);

    }

    @Override
    public void onBackPressed() {
    }
}
