package com.example.nbshoping;

import android.app.Application;

import org.xutils.x;

/*
* 自定义application
* 注册
* 初始化工作,数据库，xutils，...
* */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //对xutils架构的初始化
        x.Ext.init(this);





    }
}
