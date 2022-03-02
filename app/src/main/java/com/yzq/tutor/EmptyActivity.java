package com.yzq.tutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yzq.tutor.activity.DisplayMessageActivity;

public class EmptyActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.yzq.tutor.MESSAGE";
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

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}