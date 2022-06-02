package com.yzq.pianogame;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

/**
 * Description:
 * <br/>website: <a href="http://www.crazyit.org">crazyit.org</a>
 * <br/>Copyright (C), 2001-2014, Yeeku.H.Lee
 * <br/>This program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 *
 * @author Yeeku.H.Lee kongyeeku@163.com
 * @version 1.0
 */
public class SearchList extends AppCompatActivity implements
        SearchView.OnQueryTextListener {
    int n;
    private SearchView sv;
    private ListView lv;
    // 自动完成的列表
    private final String[] mStrings = {"小星星     easy", "天空之城    normal", "天空之城 hard"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchlist);
        lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mStrings));
        lv.setTextFilterEnabled(true);
        sv = (SearchView) findViewById(R.id.sv);
        // 设置该SearchView默认是否自动缩小为图标
        sv.setIconifiedByDefault(false);
        // 为该SearchView组件设置事件监听器
        sv.setOnQueryTextListener(this);
        // 获取应用程序中的button1按钮
        Button button1 =  findViewById(R.id.button1);
        // 为button1按钮绑定事件监听器
        button1.setOnClickListener(new OnClickListener() {

            public void onClick(View source) {
                n = 1;
                onQueryTextChange("easy");
            }
        });

        Button button2 = (Button) findViewById(R.id.button2);
        // 为button2按钮绑定事件监听器
        button2.setOnClickListener(source -> {
            n = 2;
            onQueryTextChange("normal");
        });


        Button button3 =  findViewById(R.id.button3);
        // 为burron3按钮绑定事件监听器
        button3.setOnClickListener(new OnClickListener() {

            public void onClick(View source) {
                n = 3;
                onQueryTextChange("hard");
            }
        });

        Button button4 = findViewById(R.id.button4);
        // 为button4按钮绑定事件监听器
        button4.setOnClickListener(source -> {
            if (n == 1) {// 创建需要启动的Activity对应的Intent
                Intent intent = new Intent(SearchList.this,
                        Littlestar_piano.class);
                // 启动intent对应的Activity
                startActivity(intent);
            }

            if (n == 2) {// 创建需要启动的Activity对应的Intent
                Intent intent = new Intent(SearchList.this,
                        Carryingyou_piano.class);
                // 启动intent对应的Activity
                startActivity(intent);
            }

            if (n == 3) {// 创建需要启动的Activity对应的Intent
                Intent intent = new Intent(SearchList.this,
                        Crazycarryingyou_piano.class);
                // 启动intent对应的Activity
                startActivity(intent);
            }

        });

        Button button5 = (Button) findViewById(R.id.button5);
        // 为button5按钮绑定事件监听器
        button5.setOnClickListener(new OnClickListener() {

            public void onClick(View source) {
                finish();
            }
        });


        // 设置该SearchView显示搜索按钮
        sv.setSubmitButtonEnabled(true);
        // 设置该SearchView内默认显示的提示文本
        sv.setQueryHint("曲目列表");
    }

    // 用户输入字符时激发该方法
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            // 清除ListView的过滤
            lv.clearTextFilter();
        } else {
            // 使用用户输入的内容对ListView的列表项进行过滤
            lv.setFilterText(newText);
        }
        return true;
    }

    // 单击搜索按钮时激发该方法
    public boolean onQueryTextSubmit(String query) {
        // 实际应用中应该在该方法内执行实际查询
        // 此处仅使用Toast显示用户输入的查询内容
        Toast.makeText(this, "您的选择是:" + query
                , Toast.LENGTH_SHORT).show();
        return false;
    }
}
