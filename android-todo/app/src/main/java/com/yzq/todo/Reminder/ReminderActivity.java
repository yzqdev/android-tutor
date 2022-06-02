package com.yzq.todo.Reminder;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.yzq.todo.AppDefault.AppDefaultActivity;
import com.yzq.todo.R;

public class ReminderActivity extends AppDefaultActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int contentViewLayoutRes() {
        return R.layout.reminder_layout;
    }

    @NonNull
    @Override
    protected ReminderFragment createInitialFragment() {
        return ReminderFragment.newInstance();
    }


}
