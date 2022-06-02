package com.yzq.todo.About;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.yzq.todo.R;
import com.yzq.todo.AppDefault.AppDefaultFragment;
public class AboutFragment extends AppDefaultFragment {

    private TextView mVersionTextView;
    private String appVersion = "0.1";
    private Toolbar toolbar;
    private TextView contactMe;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mVersionTextView = (TextView) view.findViewById(R.id.aboutVersionTextView);
        mVersionTextView.setText(String.format(getResources().getString(R.string.app_version), appVersion));
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        contactMe = (TextView) view.findViewById(R.id.aboutContactMe);

        contactMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("点击了");
            }
        });
    }

    @LayoutRes
    protected int layoutRes() {
        return R.layout.fragment_about;
    }

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }
}
