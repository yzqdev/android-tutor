package com.yzq.geekweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

import com.yzq.geekweather.R;


public class WelCome extends Activity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏以及状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /**标题是属于View的，所以窗口所有的修饰部分被隐藏后标题依然有效,需要去掉标题**/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.welcome);
        //延迟三秒再发送
        handler.sendEmptyMessageDelayed(0,3000);
    }

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent(WelCome.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };



}
