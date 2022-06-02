package com.example.nbshoping.me;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.nbshoping.R;

public class AboutActivity extends AppCompatActivity {
    ImageView aboutbackiv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
    }

    private void initView() {
        aboutbackiv=findViewById(R.id.about_iv_back);

        aboutbackiv.setOnClickListener(onClickListener);
    }
    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.about_iv_back:
                    finish();
                    break;
            }
        }
    };
}