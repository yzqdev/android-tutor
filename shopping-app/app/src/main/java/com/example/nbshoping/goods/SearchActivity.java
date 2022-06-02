package com.example.nbshoping.goods;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nbshoping.R;
import com.example.nbshoping.login.UserBean;
import com.example.nbshoping.utils.BaseActivity;
import com.example.nbshoping.utils.URLUtils;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/*首页搜索商品功能
* */
public class SearchActivity extends BaseActivity  {


    TabLayout tabLayout;
    ImageView back;
    private ListView listView;
    private CustomAdapter adapter;
    List<GoodsBean.DataBean> data;//数据源
    String searchname;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchname=getIntent().getExtras().getString("goodsname").trim();
        initView();

        //1.确定数据源
        data = new LinkedList<>();
        //3.初始联网
        initNet();
        //2.设置适配器
        adapter = new CustomAdapter(getApplicationContext(), data);
        listView.setAdapter(adapter);
        //4.设置点击事件
        setListener();

        initTabLayout();




    }

    /*给listview每一项设置点击事件*/
    private void setListener() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击事件发生
                Toast.makeText(getApplicationContext(),"点击了："+data.get(position).getName(),Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void initTabLayout(){

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        //默认/排序
                        break;
                    default:
                        break;
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }



    //初始联网
    private void initNet() {

        //将url封装到请求参数中
        RequestParams requestParams = new RequestParams(URLUtils.queryCommodityByName_url);
        //提交的键值对放到请求参数中
        requestParams.addParameter("name", searchname);
        requestParams.setAsJsonContent(true);//设置内容，形式
        requestParams.setBodyContentType("application/json;charset=utf-8");
        x.http().get(requestParams, new CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //请求成功，数据解析，通过解析类，解析工具生成解析类UserBean
                GoodsBean bean = new Gson().fromJson(result, GoodsBean.class);
                int code = bean.getCode();
                switch (code) {
                    case 200:
                        List<GoodsBean.DataBean> list=bean.getData();
                        data.addAll(list);
                        adapter.notifyDataSetChanged();
                        break;
                    case 400: //
                        Toast.makeText(getApplicationContext(), "参数错误！", Toast.LENGTH_SHORT).show();
                        break;
                    default: //其他失败
                        Toast.makeText(getApplicationContext(), "未知错误！", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getApplicationContext(), "未知问题，检查网络等！", Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
            }
        });
    }

    private void initView() {
        tabLayout = findViewById(R.id.tab_search_layout);
        back=findViewById(R.id.search_back_iv);
        listView=findViewById(R.id.tab_search_list);



        //返回
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}