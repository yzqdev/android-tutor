package com.example.nbshoping.goods;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.nbshoping.R;
import com.example.nbshoping.login.UserBean;
import com.example.nbshoping.utils.BaseActivity;
import com.example.nbshoping.utils.SaveUserUtils;
import com.example.nbshoping.utils.URLUtils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* 付钱购买界面
* */
public class BoughtActivity extends BaseActivity {
    private UserBean.DataBean userInfo=null;//用户信息
    private int count,commodityId;//商品数量，id,价格
    private double price;
    EditText addressEt,nameEt,phoneEt,moneyEt;
    Button sureBtn;
    RadioGroup rg;//支付方式
    ImageView backBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bought);
        userInfo= SaveUserUtils.getUserInfo(this);//用户信息
        count=getIntent().getIntExtra("count",-1);
        price=getIntent().getDoubleExtra("price",0.0);
        commodityId=getIntent().getIntExtra("commodityId",-1);

        initView();



    }

    private void initView() {
        addressEt=findViewById(R.id.bought_address);
        nameEt=findViewById(R.id.bought_name);
        phoneEt=findViewById(R.id.bought_phone);
        moneyEt=findViewById(R.id.bought_money);
        sureBtn=findViewById(R.id.bought_ensure);
        rg=findViewById(R.id.bought_rg);
        backBtn=findViewById(R.id.bought_iv_back);

        addressEt.setText(userInfo.getAddress());
        nameEt.setText(userInfo.getName());
        phoneEt.setText(userInfo.getPhone());
        moneyEt.setText(String.valueOf(count*price)+"元");

        sureBtn.setOnClickListener(onClickListener);
        backBtn.setOnClickListener(onClickListener);
        rg.setOnCheckedChangeListener(onCheckedChangeListener);



    }

    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bought_iv_back:
                    finish();
                    break;

                case  R.id.bought_ensure:
                    // 支付点击
                    if (false==judgeInput(phoneEt.getText().toString().trim(),nameEt.getText().toString().trim(),addressEt.getText().toString().trim()))
                       break;
                    Map<String,String> map=new HashMap<>();
                    map.put("count",count+"");
                    map.put("commodityId",commodityId+"");
                    map.put("userId",userInfo.getId()+"");
                    postNetwork(URLUtils.insertBought_url,map);
                    //todo 打开购买详情，关闭finish
                    break;
            }

        }
    };
    //支付方式
    RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId)
            {
                case R.id.bought_zhifubao:
                    break;
                case R.id.bought_weixin:
                    break;


            }
        }
    };

    @Override
    public void onSuccess(String result) {
        super.onSuccess(result);
        GoodsBean bean = new Gson().fromJson(result, GoodsBean.class);//格式？？
        switch (bean.getCode()) {
            case 200:
                Toast.makeText(getApplicationContext(), "购买成功！", Toast.LENGTH_SHORT).show();
                break;

            default: //
                Toast.makeText(getApplicationContext(), "发生错误！", Toast.LENGTH_SHORT).show();
                break;
        }


    }

    /*数据格式判断
     * */
    private boolean judgeInput(String tel, String name, String address) {

        if (TextUtils.isEmpty(tel) || TextUtils.isEmpty(name) || TextUtils.isEmpty(address)) {   //输入为空判断
            Toast.makeText(getApplicationContext(), "输入内容不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        //大陆电话号码正则表达式
        String regex = "^1(?:3\\d|4[4-9]|5[0-35-9]|6[67]|7[013-8]|8\\d|9\\d)\\d{8}$";
        if (!tel.matches(regex)) {   //输入电话号码格式判断
            Toast.makeText(getApplicationContext(), "手机号码不合法！", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}