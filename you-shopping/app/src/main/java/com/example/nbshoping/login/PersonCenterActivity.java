package com.example.nbshoping.login;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nbshoping.R;
import com.example.nbshoping.utils.BaseActivity;
import com.example.nbshoping.utils.SaveUserUtils;
import com.example.nbshoping.utils.URLUtils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/*
 *个人中心界面
 */
public class PersonCenterActivity extends BaseActivity {
    private UserBean.DataBean userInfo;//之前登陆时保存的用户信息
    TextView tvidtel, tvnickname, tvname, tvaddress, tvsave;
    ImageView headiv, nicknameiv, nameiv, addressiv, backiv;
    private String nickname,name,address;//修改后数据
    //todo 目前项目仅仅改了三个参数，还有头像未改变


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personcenter);
        userInfo = SaveUserUtils.getUserInfo(this);
        initView();
        showInfoInit(userInfo);//展示之前登录存储的用户信息


    }

    /*获取到之前登录存储的用户信息，然后展示到4个textview上*/
    private void showInfoInit(UserBean.DataBean userInfo) {

        if (userInfo == null) {
            // 跳转到登陆界面，当前内存中没有用户信息
            //跳转登录页面
            Intent intent = new Intent(PersonCenterActivity.this, LoginRegActivity.class);
            intent.putExtra("flag", 0);
            startActivity(intent);
            return;
        }
        Log.i("userInfo.getId():", String.valueOf(userInfo.getId()));
        tvidtel.setText(String.valueOf(userInfo.getPhone()));
        tvnickname.setText(userInfo.getNickname());
        tvname.setText(userInfo.getName());
        tvaddress.setText(userInfo.getAddress());

    }

    private void initView() {

        tvidtel = findViewById(R.id.center_tv_idtel);
        tvnickname = findViewById(R.id.center_tv_nickname);
        tvname = findViewById(R.id.center_tv_name);
        tvaddress = findViewById(R.id.center_tv_address);
        tvsave = findViewById(R.id.center_tv_save);

        headiv = findViewById(R.id.center_iv_head);
        nicknameiv = findViewById(R.id.center_iv_nickname_bt);
        nameiv = findViewById(R.id.center_iv_name_bt);
        addressiv = findViewById(R.id.center_iv_address_bt);
        backiv = findViewById(R.id.center_iv_back);


        //设置监听器
        headiv.setOnClickListener(onClickListener);
        nicknameiv.setOnClickListener(onClickListener);
        nameiv.setOnClickListener(onClickListener);
        addressiv.setOnClickListener(onClickListener);
        backiv.setOnClickListener(onClickListener);
        tvsave.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.center_tv_save:
                    //个人中心保存
                    saveInfo();
                    break;
                case R.id.center_iv_head:
                    //个人中心头像
                    Toast.makeText(getApplicationContext(),"该功能还在完善中！",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.center_iv_nickname_bt:
                    //个人中心名字
                    tvalert_edit(v);

                    break;
                case R.id.center_iv_name_bt:
                    //个人中心昵称
                    tvalert_edit(v);

                    break;
                case R.id.center_iv_address_bt:
                    //个人中心地址
                    tvalert_edit(v);
                    break;
                case R.id.center_iv_back:
                    //个人中心返回
                    issave();

                    break;

            }
        }
    };

    //修改未保存
    private void issave() {
        nickname= String.valueOf(tvnickname.getText()).trim();
        name= String.valueOf(tvname.getText()).trim();
        address= String.valueOf(tvaddress.getText()).trim();
        if (nickname != userInfo.getNickname()||name!=userInfo.getName()||address!=userInfo.getAddress()) {
            //信息设置改变
            new AlertDialog.Builder(this,R.style.UpdateDialogStyle)
                    .setTitle("提示信息")
                    .setMessage("信息已经修改未保存，是否退出？")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //按下确定键后的事件
                            finish();
                        }
                    }).setNegativeButton("取消", null).create().show();
        }
        finish();

    }

    /*保存数据*/
    private void saveInfo() {
        nickname= String.valueOf(tvnickname.getText()).trim();
        name= String.valueOf(tvname.getText()).trim();
        address= String.valueOf(tvaddress.getText()).trim();
        if (false == judgeInput(nickname))
        {
            Toast.makeText(getApplicationContext(),"信息不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if (nickname == userInfo.getNickname()&&name==userInfo.getName()&&address==userInfo.getAddress()) {
            //信息设置未改变
            Toast.makeText(getApplicationContext(),"个人信息未改变......",Toast.LENGTH_SHORT).show();
            return;
        }
        //可执行网络请求,添加网络请求权限，
        Map<String,String>map=new HashMap<>();
        map.put("id", String.valueOf(userInfo.getId()));
        map.put("name",name);
        map.put("address",address);
        map.put("nickname",nickname);
        postNetwork(URLUtils.updateInfo_url,map);

    }

    /*textview信息修改对话框*/
    @SuppressLint("ResourceAsColor")
    public void tvalert_edit(View view) {
        Map<Integer, String> map = new HashMap<>();
        map.put(R.id.center_iv_nickname_bt, "我的昵称:");
        map.put(R.id.center_iv_name_bt, "收件姓名:");
        map.put(R.id.center_iv_address_bt, "收货地址:");

        EditText et = new EditText(this);
        et.setHint("请修改" + map.get(view.getId()));
        et.setTextColor(R.color.black);
        et.setHintTextColor(R.color.nav_normal);
        new AlertDialog.Builder(this,R.style.UpdateDialogStyle)
                .setTitle("修改用户信息")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //按下确定键后的事件
                        if (view.getId() == R.id.center_iv_nickname_bt) {
                            tvnickname.setText(et.getText());
                        } else if (view.getId() == R.id.center_iv_name_bt) {
                            tvname.setText(et.getText());
                        } else if (view.getId() == R.id.center_iv_address_bt) {
                            tvaddress.setText(et.getText());
                        }
                    }
                }).setNegativeButton("取消", null).show();
    }


    /*成功修改反馈*/
    @Override
    public void onSuccess(String result) {
        super.onSuccess(result);
         //请求成功，数据解析，通过解析类，解析工具生成解析类UserBean
        UserBean bean=new Gson().fromJson(result,UserBean.class);
        int code=bean.getCode();
        switch(code)
        {
            case 200:
                // 保存修改信息到本地和内存
                userInfo.setNickname(nickname);
                userInfo.setAddress(address);
                userInfo.setName(name);
                SaveUserUtils.saveUserToFile(getApplicationContext(),userInfo);

                Toast.makeText(getApplicationContext(),"更新用户信息成功！",Toast.LENGTH_SHORT).show();
                break;
            case 201: //
                Toast.makeText(getApplicationContext(),"修改失败！",Toast.LENGTH_SHORT).show();
                break;
            default: //
                Toast.makeText(getApplicationContext(),"参数错误！",Toast.LENGTH_SHORT).show();
                break;
        }
    }


    /*数据格式判断
     * */
    private boolean judgeInput(String nickname) {
        if (nickname.equals(""))
            return false;
        return true;
    }
}
