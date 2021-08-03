package com.yzq.tutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        Button button=findViewById(R.id.button);
        button.setOnClickListener(view->{
            button.setText("这是按钮");
            startActivity(new Intent(this,MainActivity.class));
        });
    }
}