package com.example.alarms;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AlarmActivity extends AppCompatActivity {
    TextView timeView;
    Button okButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        Toast.makeText(getApplicationContext(), "Таймер сработал", Toast.LENGTH_LONG).show();

        timeView = findViewById(R.id.timeView);
        okButton = findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlarmReceiver.cancelAlarmNotification(getApplicationContext());
                returnToMainActivity();
            }
        });
        Intent previousIntent = getIntent();

        long totalTime = 0;

        try {
            totalTime = previousIntent.getLongExtra("Total time", 0);
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "Проблема с получением времени таймера:  " + e.toString(), Toast.LENGTH_LONG).show();    
        }

        /*
        long hours = totalTime / (3600 * 1000);
        long minutes = (totalTime -(hours * 3600 * 1000)) / (60 * 1000);
        long seconds = (totalTime - (hours * 3600 * 1000) - (minutes * 60 * 1000)) / 1000;

         */

        String outputStr = AlarmReceiver.stringTime(totalTime);

        timeView.setText(outputStr);

    }

    private void returnToMainActivity()
    {
        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
        mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(mainActivity);
    }

    @Override
    public void onBackPressed() {
        returnToMainActivity();

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
}
