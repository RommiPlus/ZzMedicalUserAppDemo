package com.androidobservablescrollviewdemo;

import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void setTime(View view) {
        EditText hourEdt = (EditText) findViewById(R.id.hour_edt);
        EditText minuteEdt = (EditText) findViewById(R.id.minute_edt);
        EditText messageEdt = (EditText) findViewById(R.id.message_edt);

        ArrayList<Integer> repeatWeekDay = new ArrayList<>();
        repeatWeekDay.add(Calendar.MONDAY);
        repeatWeekDay.add(Calendar.TUESDAY);
        repeatWeekDay.add(Calendar.WEDNESDAY);
        repeatWeekDay.add(Calendar.THURSDAY);
        repeatWeekDay.add(Calendar.FRIDAY);
        repeatWeekDay.add(Calendar.SATURDAY);
        repeatWeekDay.add(Calendar.SUNDAY);

        createAlarm(messageEdt.getText().toString(),
                repeatWeekDay,
                Integer.valueOf(hourEdt.getText().toString()),
                Integer.valueOf(minuteEdt.getText().toString()));
    }

    public void createAlarm(String message,  ArrayList<Integer> repeatWeekDay, int hour, int minutes) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                .putExtra(AlarmClock.EXTRA_DAYS, repeatWeekDay)
                .putExtra(AlarmClock.EXTRA_HOUR, hour)
                .putExtra(AlarmClock.EXTRA_MINUTES, minutes);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void showAllAlarm(View view) {
        showAllAlarm();
    }

    public void showAllAlarm() {
        Intent intent = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
