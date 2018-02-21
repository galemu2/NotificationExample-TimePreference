package com.android.notificationexample.perference;
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
import android.os.Build;
import android.os.Bundle;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.view.View;
import android.widget.TimePicker;

import com.android.notificationexample.R;

public class TimePreferenceFragment extends PreferenceDialogFragmentCompat {


    private TimePicker mTimePicker;

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if(positiveResult){
            int minute, hour, minutesAfterMidnight;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                 minute = mTimePicker.getMinute();
                 hour = mTimePicker.getHour();

            }else {
                minute=mTimePicker.getCurrentMinute();
                hour=mTimePicker.getCurrentHour();
            }
            minutesAfterMidnight = (hour *60)+minute;
            DialogPreference preference = getPreference();
            if (preference instanceof TimePreference){
                TimePreference timePickerPreference = (TimePreference) preference;
                if (timePickerPreference.callChangeListener(minutesAfterMidnight)) {

                    timePickerPreference.setmTime(minutesAfterMidnight  );

                }

            }

        }

    }

    public static TimePreferenceFragment newInstance(String key){
        final TimePreferenceFragment fragment = new TimePreferenceFragment();
        final Bundle bundle = new Bundle(1);
        bundle.putString(ARG_KEY, key);
        fragment.setArguments(bundle);

        return fragment;

    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        mTimePicker = view.findViewById(R.id.edit);

        if(mTimePicker==null)
            throw  new IllegalStateException("Dialog view must contain a TimePicker with id edit");

        Integer minutesAfterMidnight = null;
        DialogPreference dialogPreference = getPreference();
        if(dialogPreference instanceof TimePreference){
            minutesAfterMidnight = ((TimePreference) dialogPreference).getmTime();


        }

        if(minutesAfterMidnight!=null ){
            int hours = minutesAfterMidnight/60;
            int minutes = minutesAfterMidnight%60;
            boolean is24hrs = android.text.format.DateFormat.is24HourFormat(getContext());

            mTimePicker.setIs24HourView(is24hrs);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mTimePicker.setHour(hours);
                mTimePicker.setMinute(minutes);
            } else {
                mTimePicker.setCurrentHour(hours);
                mTimePicker.setCurrentMinute(minutes);
            }

        }

    }




}
