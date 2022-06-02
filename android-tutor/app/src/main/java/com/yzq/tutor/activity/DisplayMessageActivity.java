package com.yzq.tutor.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.yzq.tutor.EmptyActivity;
import com.yzq.tutor.MainActivity;
import com.yzq.tutor.R;

public class DisplayMessageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(EmptyActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        Button runNotify = findViewById(R.id.runNotify);
        runNotify.setOnClickListener(v -> {
                showNotify( v);
                runNotify.setText("设置字体");

        });

    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showNotify(View view) {


        // 1. 创建一个通知(必须设置channelId)
        Context context = getApplicationContext();
        String channelId = "ChannelId"; // 通知渠道
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, channelId)

                .setSmallIcon(R.drawable.ic_user)
                .setContentTitle("通知标题")
                .setContentText("通知内容");
// 2. 获取系统的通知管理器(必须设置channelId)
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        NotificationChannel channel = new NotificationChannel(
                channelId,
                "通知的渠道名称",
                NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(channel);
// 3. 发送通知(Notification与NotificationManager的channelId必须对应)
        notificationManager.notify(11, notification.build());

    }

//    @RequiresApi(Build.VERSION_CODES.N_MR1)
//    public void showNotifyV5(View view ) {
//        //获取NotificationManager实例
//        NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        //实例化NotificationCompat.Builde并设置相关属性
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"string")
//                //设置小图标
//                .setSmallIcon(R.drawable.ic_user)
//                //设置通知标题
//                .setContentTitle("最简单的Notification")
//                //设置通知内容
//                .setContentText("只有小图标、标题、内容");
//        //设置通知时间，默认为系统发出通知的时间，通常不用设置
//        //.setWhen(System.currentTimeMillis());
//        //通过builder.build()方法生成Notification对象,并发送通知,id=1
//        notifyManager.notify(1, builder.build());
//    }
}
