package com.yzq.pianogame;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.yzq.pianogame.R;

/**
 * Description:
 * <br/>site: <a href="http://www.crazyit.org">crazyit.org</a>
 * <br/>Copyright (C), 2001-2014, Yeeku.H.Lee
 * <br/>This program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 *
 * @author Yeeku.H.Lee kongyeeku@163.com
 * @version 1.0
 */
public class Welcome extends Activity {

    MediaPlayer mediaPlayer1 = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        mediaPlayer1 = MediaPlayer.create(this, R.raw.croatianrhapsody);
        mediaPlayer1.start();

        // 获取应用程序中的bn1按钮
        Button bn1 = (Button) findViewById(R.id.bn1);
        // 为bn1按钮绑定事件监听器
        bn1.setOnClickListener(new OnClickListener() {

            public void onClick(View source) {
                // 创建需要启动的Activity对应的Intent
                Intent intent = new Intent(Welcome.this,
                        Main.class);
                // 启动intent对应的Activity
                startActivity(intent);
                mediaPlayer1.stop();
            }
        });

        // 获取应用程序中的bn2按钮
        Button bn2 = (Button) findViewById(R.id.bn2);
        // 为bn2按钮绑定事件监听器
        bn2.setOnClickListener(new OnClickListener() {

            public void onClick(View source) {
                finish();
                mediaPlayer1.stop();

            }
        });
    }
}