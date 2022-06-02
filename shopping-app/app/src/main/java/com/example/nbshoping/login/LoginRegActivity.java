package com.example.nbshoping.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.nbshoping.R;

import java.util.ArrayList;
import java.util.List;

public class LoginRegActivity extends AppCompatActivity {
    TextView logintv, regtv;//登录，注册
    ViewPager loginvp;//适配器视图滑动，展示fragment
    List<Fragment> fragmentList;//两个

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_reg);
        //接收上一个界面数据,0登录，1注册
        int flag=getIntent().getIntExtra("flag",0);

        initView();
        setListener();
        initPaper();//声明viewerpaper的展示页
        setPagePos(flag);


    }

    /*
    *设置显示位置,注册1或登录0
     */
    private void setPagePos(int flag) {
        if (flag==0)
        {
            //显示登录界面
            logintv.setTextColor(Color.rgb(241,104,173));
            regtv.setTextColor(Color.WHITE);
        }else if (flag==1){
            //显示注册界面
            regtv.setTextColor(Color.rgb(241,104,173));
            logintv.setTextColor(Color.WHITE);
        }
        loginvp.setCurrentItem(flag);

    }

    /*
     *声明viewerpaper的展示页
     */
    private void initPaper() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new LoginFragment());
        fragmentList.add(new RegisterFragment());//添加碎片
        //将fragment放到viewpaper，展示到activity中
        LoginVPAdapter vpAdapter=new LoginVPAdapter(getSupportFragmentManager(),fragmentList);
        loginvp.setAdapter(vpAdapter);//设置适配器

    }

    /*
     *设置监听事件
     */
    private void setListener() {
        logintv.setOnClickListener(onClickListener);
        regtv.setOnClickListener(onClickListener);
        loginvp.addOnPageChangeListener(onPageChangeListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.login_tv_log:
                    setPagePos(0);
                    break;
                case R.id.reg_tv_log:
                    setPagePos(1);
                    break;
            }
        }
    };
    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {//子类创建
        @Override
        public void onPageSelected(int position) {//选中的页面
            super.onPageSelected(position);
            setPagePos(position);

        }
    };


    /*
     * 初始化控件*/
    private void initView() {
        logintv = findViewById(R.id.login_tv_log);
        regtv = findViewById(R.id.reg_tv_log);
        loginvp = findViewById(R.id.loginreg_vp);

    }
}