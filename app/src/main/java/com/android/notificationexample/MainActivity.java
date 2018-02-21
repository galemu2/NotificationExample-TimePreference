package com.android.notificationexample;
/*
 * Copyright (C) 2018
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private boolean notificationIsOn;
    private int hourOfTheDay;
    private int hour;
    private int minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buttonNotification(View view) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        notificationIsOn = sharedPreferences.getBoolean(getResources().getString(R.string.daily_notification_select), getResources().getBoolean(R.bool.daily_notification_default_value));
        /*            int hours = minutesAfterMidnight/60;
            int minutes = minutesAfterMidnight%60;
            */

        if (notificationIsOn) {
            hourOfTheDay = sharedPreferences.getInt(getResources().getString(R.string.time_preference_key), Integer.valueOf(getResources().getString(R.string.time_preference_default_val)));
            hour = hourOfTheDay / 60;
            minute = hourOfTheDay % 60;


            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR, hour);
            calendar.set(Calendar.MINUTE, minute);
//            calendar.set(Calendar.SECOND, minute);

            Intent intent = new Intent(getApplicationContext(), NotificationBroadcastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            if (alarmManager != null) {//AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),10000, pendingIntent
                alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 60000, pendingIntent);
            }
        } else {
            Toast.makeText(this, "Notification is OFF", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            Intent intent = new Intent(this, SettingsActivity.class);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, new View(this), "");
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        String switchPrefKey = getResources().getString(R.string.daily_notification_select);
        String timePrefKey = getResources().getString(R.string.time_preference_key);
        if (key.equals(switchPrefKey)) {
            notificationIsOn = sharedPreferences.getBoolean(key, getResources().getBoolean(R.bool.daily_notification_default_value));
        } else if (key.equals(timePrefKey)) {
            hourOfTheDay = sharedPreferences.getInt(key, Integer.valueOf(getResources().getString(R.string.time_preference_default_val)));
            Toast.makeText(this, hour + ":" + minute, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
}
