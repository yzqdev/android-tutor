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
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

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
        TextView textView = findViewById(R.id.textView2);
        textView.setText(message);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showNotify(View view){



        // 1. 创建一个通知(必须设置channelId)
        Context context = getApplicationContext();
        String channelId = "ChannelId"; // 通知渠道
        Notification notification = new Notification.Builder(context)
                .setChannelId(channelId)
                .setSmallIcon(R.drawable.ic_user)
                .setContentTitle("通知标题")
                .setContentText("通知内容")
                .build();
// 2. 获取系统的通知管理器(必须设置channelId)
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(
                channelId,
                "通知的渠道名称",
                NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(channel);
// 3. 发送通知(Notification与NotificationManager的channelId必须对应)
        notificationManager.notify(11, notification);

    }
}
