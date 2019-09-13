package com.example.alifd.listfilmrecycler.reminder.setting;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.example.alifd.listfilmrecycler.R;
import com.example.alifd.listfilmrecycler.reminder.AlarmReceiver;

import timber.log.Timber;

import static com.example.alifd.listfilmrecycler.reminder.AlarmReceiver.DAILY_REMINDER;
import static com.example.alifd.listfilmrecycler.reminder.AlarmReceiver.NEW_FILMS;

public class PreferenceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{
    private boolean DEFAULT_VALUE = false;
    private String DAILY_NOTIF;
    private String RELEASE_NOTIF;

    private SwitchPreferenceCompat dailySwitch;
    private SwitchPreferenceCompat releaseSwitch;

    private AlarmReceiver alarmReceiver;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.setting_preferences);
        init();
        setSummaries();
    }

    private void init() {
        DAILY_NOTIF = getResources().getString(R.string.key_daily_notif);
        RELEASE_NOTIF = getResources().getString(R.string.key_release_notif);

        dailySwitch = (SwitchPreferenceCompat) findPreference(DAILY_NOTIF);
        releaseSwitch = (SwitchPreferenceCompat) findPreference(RELEASE_NOTIF);
        alarmReceiver = new AlarmReceiver();
    }

    private void setSummaries() {
        SharedPreferences sh = getPreferenceManager().getSharedPreferences();
        dailySwitch.setChecked(sh.getBoolean(DAILY_NOTIF, DEFAULT_VALUE));
        releaseSwitch.setChecked(sh.getBoolean(RELEASE_NOTIF, DEFAULT_VALUE));
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
        if (key.equals(DAILY_NOTIF) && getContext()!=null){
            dailySwitch.setChecked(sharedPreferences.getBoolean(DAILY_NOTIF, DEFAULT_VALUE));
            if(dailySwitch.isChecked()) {
                alarmReceiver.setDailyReminderNotif(getContext(), DAILY_REMINDER, "Mau nonton film? lihat dlu yuk list filmnya");
            }else {
                alarmReceiver.stopNotification(getContext(), DAILY_REMINDER);
            }
            Timber.e("%s", dailySwitch.isChecked());
        }

        if (key.equals(RELEASE_NOTIF) && getContext()!=null){
            releaseSwitch.setChecked(sharedPreferences.getBoolean(RELEASE_NOTIF, DEFAULT_VALUE));
            if(dailySwitch.isChecked()) {
                alarmReceiver.setNewReleaseNotif(getContext(), NEW_FILMS, "Ada film baru nih");
            }else {
                alarmReceiver.stopNotification(getContext(), NEW_FILMS);
            }
            Timber.e("%s", releaseSwitch.isChecked());
        }
    }
}
