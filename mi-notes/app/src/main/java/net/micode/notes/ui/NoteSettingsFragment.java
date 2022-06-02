package net.micode.notes.ui;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import net.micode.notes.R;

public class NoteSettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}