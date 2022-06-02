package com.yzq.todo.Settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;


import androidx.preference.CheckBoxPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.yzq.todo.Main.MainFragment;
import com.yzq.todo.R;
import com.yzq.todo.Utility.PreferenceKeys;

/**
 *
 */
public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {




    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences_layout,rootKey);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        PreferenceKeys preferenceKeys = new PreferenceKeys(getResources());
        if (key.equals(preferenceKeys.night_mode_pref_key)) {
            SharedPreferences themePreferences = getActivity().getSharedPreferences(MainFragment.THEME_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor themeEditor = themePreferences.edit();
            //We tell our MainLayout to recreate itself because mode has changed
            themeEditor.putBoolean(MainFragment.RECREATE_ACTIVITY, true);

            CheckBoxPreference checkBoxPreference =   findPreference(preferenceKeys.night_mode_pref_key);
            if (checkBoxPreference.isChecked()) {
                //Comment out this line if not using Google Analytics
                themeEditor.putString(MainFragment.THEME_SAVED, MainFragment.DARKTHEME);
            } else {
                themeEditor.putString(MainFragment.THEME_SAVED, MainFragment.LIGHTTHEME);
            }
            themeEditor.apply();

            getActivity().recreate();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
