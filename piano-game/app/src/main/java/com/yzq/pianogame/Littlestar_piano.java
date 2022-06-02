package com.yzq.pianogame;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;


public class Littlestar_piano extends AppCompatActivity {


    private ImageButton imageButton_white1;
    private ImageButton imageButton_white2;
    private ImageButton imageButton_white3;
    private ImageButton imageButton_white4;
    private ImageButton imageButton_white5;
    private ImageButton imageButton_white6;
    private ImageButton imageButton_white7;
    private ImageButton imageButton_white21;
    private ImageButton imageButton_white22;
    private ImageButton imageButton_white23;
    private ImageButton imageButton_white24;
    private ImageButton imageButton_white25;
    private ImageButton imageButton_white26;
    private ImageButton imageButton_white27;

    private ImageButton imageButton_black1;
    private ImageButton imageButton_black2;
    private ImageButton imageButton_black3;
    private ImageButton imageButton_black4;
    private ImageButton imageButton_black5;
    private ImageButton imageButton_black21;
    private ImageButton imageButton_black22;
    private ImageButton imageButton_black23;
    private ImageButton imageButton_black24;
    private ImageButton imageButton_black25;

    SoundPool soundPool;
    HashMap<Integer, Integer> soundMap =new HashMap<Integer, Integer>();



    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.main);


        LinearLayout root = (LinearLayout) findViewById(R.id.root);
        // 创建DrawView组件
        final Littlestar_picture draw = new Littlestar_picture(this);
        root.addView(draw);


        soundPool = new SoundPool(25, AudioManager.STREAM_SYSTEM, 5);

        soundMap.put(1, soundPool.load(this, R.raw.white1, 1));
        soundMap.put(2, soundPool.load(this, R.raw.white2, 1));
        soundMap.put(3, soundPool.load(this, R.raw.white3, 1));
        soundMap.put(4, soundPool.load(this, R.raw.white4, 1));
        soundMap.put(5, soundPool.load(this, R.raw.white5, 1));
        soundMap.put(6, soundPool.load(this, R.raw.white6, 1));
        soundMap.put(7, soundPool.load(this, R.raw.white7, 1));
        soundMap.put(14, soundPool.load(this, R.raw.white21, 1));
        soundMap.put(15, soundPool.load(this, R.raw.white22, 1));
        soundMap.put(16, soundPool.load(this, R.raw.white23, 1));
        soundMap.put(17, soundPool.load(this, R.raw.white24, 1));
        soundMap.put(18, soundPool.load(this, R.raw.white25, 1));
        soundMap.put(19, soundPool.load(this, R.raw.white26, 1));
        soundMap.put(20, soundPool.load(this, R.raw.white27, 1));
        soundMap.put(9, soundPool.load(this, R.raw.black1, 1));
        soundMap.put(10, soundPool.load(this, R.raw.black2, 1));
        soundMap.put(11, soundPool.load(this, R.raw.black3, 1));
        soundMap.put(12, soundPool.load(this, R.raw.black4, 1));
        soundMap.put(13, soundPool.load(this, R.raw.black5, 1));
        soundMap.put(21, soundPool.load(this, R.raw.black21, 1));
        soundMap.put(22, soundPool.load(this, R.raw.black22, 1));
        soundMap.put(23, soundPool.load(this, R.raw.black23, 1));
        soundMap.put(24, soundPool.load(this, R.raw.black24, 1));
        soundMap.put(25, soundPool.load(this, R.raw.black25, 1));


        imageButton_white1 = (ImageButton) findViewById(R.id.white1);
        imageButton_white2 = (ImageButton) findViewById(R.id.white2);
        imageButton_white3 = (ImageButton) findViewById(R.id.white3);
        imageButton_white4 = (ImageButton) findViewById(R.id.white4);
        imageButton_white5 = (ImageButton) findViewById(R.id.white5);
        imageButton_white6 = (ImageButton) findViewById(R.id.white6);
        imageButton_white7 = (ImageButton) findViewById(R.id.white7);
        imageButton_white21 = (ImageButton) findViewById(R.id.white21);
        imageButton_white22 = (ImageButton) findViewById(R.id.white22);
        imageButton_white23 = (ImageButton) findViewById(R.id.white23);
        imageButton_white24 = (ImageButton) findViewById(R.id.white24);
        imageButton_white25 = (ImageButton) findViewById(R.id.white25);
        imageButton_white26 = (ImageButton) findViewById(R.id.white26);
        imageButton_white27 = (ImageButton) findViewById(R.id.white27);


        imageButton_black1 = (ImageButton) findViewById(R.id.black1);
        imageButton_black2 = (ImageButton) findViewById(R.id.black2);
        imageButton_black3 = (ImageButton) findViewById(R.id.black3);
        imageButton_black4 = (ImageButton) findViewById(R.id.black4);
        imageButton_black5 = (ImageButton) findViewById(R.id.black5);
        imageButton_black21 = (ImageButton) findViewById(R.id.black21);
        imageButton_black22 = (ImageButton) findViewById(R.id.black22);
        imageButton_black23 = (ImageButton) findViewById(R.id.black23);
        imageButton_black24 = (ImageButton) findViewById(R.id.black24);
        imageButton_black25 = (ImageButton) findViewById(R.id.black25);

        imageButton_white1.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    soundPool.play(soundMap.get(1),1,1,0,0,1);
                    imageButton_white1.setImageResource(R.drawable.whiteback1);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageButton_white1.setImageResource(R.drawable.white1);
                }
                return false;
            }
        });


        imageButton_white2.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    soundPool.play(soundMap.get(2),1,1,0,0,1);
                    imageButton_white2.setImageResource(R.drawable.whiteback2);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageButton_white2.setImageResource(R.drawable.white2);
                }
                return false;
            }
        });
//
        imageButton_white3.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    soundPool.play(soundMap.get(3),1,1,0,0,1);
                    imageButton_white3.setImageResource(R.drawable.whiteback3);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageButton_white3.setImageResource(R.drawable.white3);
                }
                return false;
            }
        });

        imageButton_white4.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    soundPool.play(soundMap.get(4),1,1,0,0,1);
                    imageButton_white4.setImageResource(R.drawable.whiteback4);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageButton_white4.setImageResource(R.drawable.white4);
                }
                return false;
            }
        });

        imageButton_white5.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    soundPool.play(soundMap.get(5),1,1,0,0,1);
                    imageButton_white5.setImageResource(R.drawable.whiteback5);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageButton_white5.setImageResource(R.drawable.white5);
                }
                return false;
            }
        });

        imageButton_white6.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    soundPool.play(soundMap.get(6),1,1,0,0,1);
                    imageButton_white6.setImageResource(R.drawable.whiteback6);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageButton_white6.setImageResource(R.drawable.white6);
                }
                return false;
            }
        });

        imageButton_white7.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    soundPool.play(soundMap.get(7),1,1,0,0,1);
                    imageButton_white7.setImageResource(R.drawable.whiteback7);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageButton_white7.setImageResource(R.drawable.white7);
                }
                return false;
            }
        });

        imageButton_white21.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    soundPool.play(soundMap.get(14),1,1,0,0,1);
                    imageButton_white21.setImageResource(R.drawable.whiteback21);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageButton_white21.setImageResource(R.drawable.white21);
                }
                return false;
            }
        });

        imageButton_white22.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    soundPool.play(soundMap.get(15),1,1,0,0,1);
                    imageButton_white22.setImageResource(R.drawable.whiteback22);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageButton_white22.setImageResource(R.drawable.white22);
                }
                return false;
            }
        });

        imageButton_white23.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    soundPool.play(soundMap.get(16),1,1,0,0,1);
                    imageButton_white23.setImageResource(R.drawable.whiteback23);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageButton_white23.setImageResource(R.drawable.white23);
                }
                return false;
            }
        });

        imageButton_white24.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    soundPool.play(soundMap.get(17),1,1,0,0,1);
                    imageButton_white24.setImageResource(R.drawable.whiteback24);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageButton_white24.setImageResource(R.drawable.white24);
                }
                return false;
            }
        });

        imageButton_white25.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    soundPool.play(soundMap.get(18),1,1,0,0,1);
                    imageButton_white25.setImageResource(R.drawable.whiteback25);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageButton_white25.setImageResource(R.drawable.white25);
                }
                return false;
            }
        });

        imageButton_white26.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    soundPool.play(soundMap.get(19),1,1,0,0,1);
                    imageButton_white26.setImageResource(R.drawable.whiteback26);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageButton_white26.setImageResource(R.drawable.white26);
                }
                return false;
            }
        });

        imageButton_white27.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    soundPool.play(soundMap.get(20),1,1,0,0,1);
                    imageButton_white27.setImageResource(R.drawable.whiteback27);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageButton_white27.setImageResource(R.drawable.white27);
                }
                return false;
            }
        });



        imageButton_black1.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    soundPool.play(soundMap.get(9),1,1,0,0,1);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageButton_black1.setImageResource(R.drawable.black1);
                }
                return false;
            }
        });

        imageButton_black2.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    soundPool.play(soundMap.get(10),1,1,0,0,1);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageButton_black2.setImageResource(R.drawable.black2);
                }
                return false;
            }
        });

        imageButton_black3.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    soundPool.play(soundMap.get(11),1,1,0,0,1);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageButton_black3.setImageResource(R.drawable.black3);
                }
                return false;
            }
        });

        imageButton_black4.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    soundPool.play(soundMap.get(12),1,1,0,0,1);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageButton_black4.setImageResource(R.drawable.black4);
                }
                return false;
            }
        });

        imageButton_black5.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    soundPool.play(soundMap.get(13),1,1,0,0,1);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageButton_black5.setImageResource(R.drawable.black5);
                }
                return false;
            }
        });

        imageButton_black21.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    soundPool.play(soundMap.get(21),1,1,0,0,1);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageButton_black21.setImageResource(R.drawable.black21);
                }
                return false;
            }
        });

        imageButton_black22.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    soundPool.play(soundMap.get(22),1,1,0,0,1);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageButton_black22.setImageResource(R.drawable.black22);
                }
                return false;
            }
        });

        imageButton_black23.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    soundPool.play(soundMap.get(23),1,1,0,0,1);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageButton_black23.setImageResource(R.drawable.black23);
                }
                return false;
            }
        });

        imageButton_black24.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    soundPool.play(soundMap.get(24),1,1,0,0,1);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageButton_black24.setImageResource(R.drawable.black24);
                }
                return false;
            }
        });

        imageButton_black25.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    soundPool.play(soundMap.get(25),1,1,0,0,1);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageButton_black25.setImageResource(R.drawable.black25);
                }
                return false;
            }
        });


        // 获取应用程序中的bn11按钮
        Button bn11 = (Button) findViewById(R.id.bn11);
        // 为bn11按钮绑定事件监听器
        bn11.setOnClickListener(new OnClickListener()
        {

            public void onClick(View source)
            {
                //结束当前Activity
                finish();
            }
        });


    }




}

