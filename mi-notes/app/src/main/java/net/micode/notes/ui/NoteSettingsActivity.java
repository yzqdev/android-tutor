package net.micode.notes.ui;

import android.accounts.Account;
import android.os.Bundle;
import android.preference.PreferenceCategory;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import net.micode.notes.R;

public class NoteSettingsActivity extends AppCompatActivity {
    public static final String PREFERENCE_NAME = "notes_preferences";

    public static final String PREFERENCE_SYNC_ACCOUNT_NAME = "pref_key_account_name";

    public static final String PREFERENCE_LAST_SYNC_TIME = "pref_last_sync_time";

    public static final String PREFERENCE_SET_BG_COLOR_KEY = "pref_key_bg_random_appear";

    private static final String PREFERENCE_SYNC_ACCOUNT_KEY = "pref_sync_account_key";

    private static final String AUTHORITIES_FILTER_KEY = "authorities";

    private PreferenceCategory mAccountCategory;

//    private GTaskReceiver mReceiver;

    private Account[] mOriAccounts;

    private boolean mHasAddedAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new NoteSettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // need to set sync account automatically if user has added a new
        // account
//        if (mHasAddedAccount) {
//            Account[] accounts = getGoogleAccounts();
//            if (mOriAccounts != null && accounts.length > mOriAccounts.length) {
//                for (Account accountNew : accounts) {
//                    boolean found = false;
//                    for (Account accountOld : mOriAccounts) {
//                        if (TextUtils.equals(accountOld.name, accountNew.name)) {
//                            found = true;
//                            break;
//                        }
//                    }
//                    if (!found) {
//                        setSyncAccount(accountNew.name);
//                        break;
//                    }
//                }
//            }
//        }

//        refreshUI();
    }
}