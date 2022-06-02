package com.example.nbshoping.me;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.nbshoping.R;
import com.example.nbshoping.login.UpPwdVaActivity;
import com.example.nbshoping.utils.SaveUserUtils;

public class SetActivity extends AppCompatActivity {

    ImageView setbackiv;
    LinearLayout pwdlayout,l2,l3,l4,l5,l6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setmain);
        initView();
    }

    private void initView() {
        setbackiv = findViewById(R.id.set_iv_back);
        pwdlayout = findViewById(R.id.set1_pwd_layuot);
        l2 = findViewById(R.id.set2);
        l3 = findViewById(R.id.set3);
        l4 = findViewById(R.id.set4);
        l5 = findViewById(R.id.set5);
        l6 = findViewById(R.id.set6);

        setbackiv.setOnClickListener(onClickListener);
        pwdlayout.setOnClickListener(onClickListener);
        l2.setOnClickListener(onClickListener);
        l3.setOnClickListener(onClickListener);
        l4.setOnClickListener(onClickListener);
        l5.setOnClickListener(onClickListener);
        l6.setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.set_iv_back:
                    finish();
                    break;
                case R.id.set1_pwd_layuot:
                    //密码设置
                    if (SaveUserUtils.getUserInfo(getApplicationContext()) == null) {
                        Toast.makeText(getApplicationContext(), "没有获取到账户信息,请先登录!", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    Intent intent = new Intent(SetActivity.this, UpPwdVaActivity.class);
                    startActivity(intent);
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "当前功能还在完善中！", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };


}