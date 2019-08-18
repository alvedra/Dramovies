package com.example.h.dramoviesvfinal.preferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.example.h.dramoviesvfinal.R;
import com.example.h.dramoviesvfinal.receiver.AlarmReceiver;

public class MyPreferenceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private String dailyTime = "7:00";
    private String releaseTime = "8:00";
    private String DAILY;
    private String RELEASE;

    private SwitchPreferenceCompat dailySwitchPreference;
    private SwitchPreferenceCompat releaseSwitchPreference;

    private AlarmReceiver alarmReceiver;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
        init();
        setSummaries();
    }

    private void init() {
        DAILY = getResources().getString(R.string.key_daily_reminder);
        RELEASE = getResources().getString(R.string.key_release_reminder);

        dailySwitchPreference = (SwitchPreferenceCompat) findPreference(DAILY);
        releaseSwitchPreference = (SwitchPreferenceCompat) findPreference(RELEASE);

        alarmReceiver = new AlarmReceiver();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(DAILY)){
            dailySwitchPreference.setChecked(sharedPreferences.getBoolean(DAILY,false));
            if(sharedPreferences.getBoolean(DAILY,false)){
                String message = getResources().getString(R.string.preferences_daily_message);
                alarmReceiver.setRepeatingAlarm(getActivity(),dailyTime,message, AlarmReceiver.TYPE_DAIlY);
            }else{
                alarmReceiver.cancelAlarm(getActivity(), AlarmReceiver.TYPE_DAIlY);
            }
        }
        if(key.equals(RELEASE)){
           releaseSwitchPreference.setChecked(sharedPreferences.getBoolean(RELEASE,false));
            if(sharedPreferences.getBoolean(RELEASE,false)){
                alarmReceiver.setReleaseAlarm(getActivity(),releaseTime, AlarmReceiver.TYPE_RELEASE);
            }else{
                alarmReceiver.cancelAlarm(getActivity(), AlarmReceiver.TYPE_RELEASE);
            }
        }
    }

    private void setSummaries(){
        SharedPreferences sh = getPreferenceManager().getSharedPreferences();
        dailySwitchPreference.setChecked(sh.getBoolean(DAILY, false));
        releaseSwitchPreference.setChecked(sh.getBoolean(RELEASE,false));
    }
}
