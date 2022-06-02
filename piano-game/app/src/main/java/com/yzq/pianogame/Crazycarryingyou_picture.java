package com.yzq.pianogame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;


public class Crazycarryingyou_picture extends View {
    // 记录背景位图的实际高度
    final int BACK_HEIGHT = 1500;
    // 背景图片
    private Bitmap back;
    final int WIDTH = 413;
    final int HEIGHT = 135;
    private int startY = BACK_HEIGHT - HEIGHT;

    public Crazycarryingyou_picture(Context context) {
        super(context);
        back = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.carryingyou);
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 0x123 && startY >= 10) {


                    startY -= 3;

                }
                invalidate();
            }
        };
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0x123);
            }

        }, 0, 100);
    }


    @Override
    public void onDraw(Canvas canvas) {
        // 根据原始位图和Matrix创建新图片
        Bitmap bitmap2 = Bitmap
                .createBitmap(back, 0, startY, WIDTH, HEIGHT); // ①
        // 绘制新位图
        canvas.drawBitmap(bitmap2, 0, 0, null);
    }

}
    


