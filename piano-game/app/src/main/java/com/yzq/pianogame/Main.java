package com.yzq.pianogame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class Main extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.premain);


        // 获取应用程序中的bn1按钮
        Button bn3 = (Button) findViewById(R.id.bn3);
        // 为bn3按钮绑定事件监听器
        bn3.setOnClickListener(new OnClickListener() {

            public void onClick(View source) {
                // 创建需要启动的Activity对应的Intent
                Intent intent = new Intent(Main.this,
                        Searchlist.class);
                // 启动intent对应的Activity
                startActivity(intent);
            }
        });

        // 获取应用程序中的bn4按钮
        Button bn4 = this.findViewById(R.id.bn4);
        // 为bn4按钮绑定事件监听器
        bn4.setOnClickListener(new OnClickListener() {

            public void onClick(View source) {
                // 创建需要启动的Activity对应的Intent
                Intent intent = new Intent(Main.this,
                        Freeplay.class);
                // 启动intent对应的Activity
                startActivity(intent);
            }
        });
        // 获取应用程序中的bn5按钮
        Button bn5 = (Button) findViewById(R.id.bn5);
        // 为bn5按钮绑定事件监听器
        bn5.setOnClickListener(new OnClickListener() {

            public void onClick(View source) {
                //结束当前Activity
                finish();
            }
        });
    }
}