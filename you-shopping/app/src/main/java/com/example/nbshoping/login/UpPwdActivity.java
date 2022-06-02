package com.example.nbshoping.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nbshoping.MainActivity;
import com.example.nbshoping.R;
import com.example.nbshoping.utils.BaseActivity;
import com.example.nbshoping.utils.SaveUserUtils;
import com.example.nbshoping.utils.URLUtils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/*修改密码*/
public class UpPwdActivity extends BaseActivity {

    EditText oldEt, newEt, renewEt;//密码框
    Button ensureBtn;
    ImageView backbt;
    private UserBean.DataBean userInfo = null;//之前登陆时保存的用户信息


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uppwd);
        userInfo = SaveUserUtils.getUserInfo(this);

        initView();

    }

    private void initView() {
        oldEt = findViewById(R.id.uppwd_old_ev);
        newEt = findViewById(R.id.uppwd_new_ev);
        renewEt = findViewById(R.id.uppwd_renew_ev);
        ensureBtn = findViewById(R.id.uppwd_sure_btn);
        backbt = findViewById(R.id.uppwd_iv_back);

        ensureBtn.setOnClickListener(onClickListener);
        backbt.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.uppwd_iv_back:
                    finish();
                    break;
                case R.id.uppwd_sure_btn:
                    isfix();
                    break;
            }

        }
    };

    /*修改密码*/
    private void isfix() {
        if (judgeInput(oldEt.getText().toString().trim(), newEt.getText().toString().trim(), renewEt.getText().toString().trim()) == false) {
            return;
        }
        new AlertDialog.Builder(this, R.style.UpdateDialogStyle)
                .setTitle("提示信息")
                .setMessage("是否确定修改密码？")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //按下确定键后的事件
                        Map<String, String> map = new HashMap<>();
                        map.put("userId", String.valueOf(userInfo.getId()));
                        map.put("oldPwd", oldEt.getText().toString().trim());
                        map.put("newPwd", newEt.getText().toString().trim());
                        postNetwork(URLUtils.updatePwd_url, map);

                    }
                }).setNegativeButton("取消", null).create().show();
        //
    }


    @Override
    public void onSuccess(String result) {
        super.onSuccess(result);
        //请求成功，数据解析，通过解析类，解析工具生成解析类UserBean
        UserBean bean = new Gson().fromJson(result, UserBean.class);
        int code = bean.getCode();
        switch (code) {
            case 200:
                Toast.makeText(getApplicationContext(), "修改成功！", Toast.LENGTH_SHORT).show();
                exitLogin();//修改密码后注销重新登录
                finish();
                break;
            case 201:
                Toast.makeText(getApplicationContext(), "原始密码错误！", Toast.LENGTH_SHORT).show();
                break;
            default: //其他失败
                Toast.makeText(getApplicationContext(), "参数错误！", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    /*修改密码后注销重新登录*/
    private void exitLogin() {

        //删除本地及内存的用户信息,并跳转到登录界面
        Toast.makeText(getApplicationContext(), "修改密码后请重新登录！", Toast.LENGTH_SHORT).show();
        SaveUserUtils.deleteUserInfo(getApplicationContext());
        userInfo = null;
        Intent intent = new Intent(UpPwdActivity.this, LoginRegActivity.class);
        intent.putExtra("flag", 0);
        startActivity(intent);
    }

    /*数据格式判断
     * */
    private boolean judgeInput(String oldpwd, String pwd, String repwd) {
        if (oldpwd.equals(pwd) && pwd.equals(repwd)) {
            Toast.makeText(getApplicationContext(), "填入的旧密码和新密码相同！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(oldpwd) || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(repwd)) {   //输入为空判断
            Toast.makeText(getApplicationContext(), "输入内容不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!pwd.equals(repwd)) {
            Toast.makeText(getApplicationContext(), "两次输入密码不同！", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}