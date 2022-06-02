package com.example.nbshoping.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nbshoping.R;
import com.example.nbshoping.utils.BaseFragment;
import com.example.nbshoping.utils.SaveUserUtils;
import com.example.nbshoping.utils.URLUtils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
/*
*登录页面
 */

public class LoginFragment extends BaseFragment {

    EditText telEt,pwdEt;//电话号码，，密码，
    TextView updatepwd;//修改密码
    Button ensurebutton;
    private UserBean.DataBean userInfo;//之前登陆时保存的用户信息


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_login, container, false);
        userInfo = SaveUserUtils.getUserInfo(getContext());

        initView(view);
        return view;
    }

    private void initView(View view) {
        telEt=view.findViewById(R.id.frag_login_te_tel1);
        pwdEt=view.findViewById(R.id.frag_login_te_pwd);
        ensurebutton=view.findViewById(R.id.frag_login_btn);
        updatepwd=view.findViewById(R.id.frag_login_tv_updatepwd);
        ensurebutton.setOnClickListener(onClickListener);
        updatepwd.setOnClickListener(onClickListener);
    }
    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.frag_login_btn:
                    //登录
                    String tel=telEt.getText().toString();
                    String pwd=pwdEt.getText().toString();
                    //数据格式判断
                    boolean flag=judgeInput( tel, pwd);
                    if (flag) {
                        //可执行网络请求,添加网络请求权限，
                        Map<String,String> map=new HashMap<>();
                        map.put("phone",tel);
                        map.put("password",pwd);
                        postNetwork(URLUtils.login_url,map);

                    }
                    break;
                case R.id.frag_login_tv_updatepwd:
                    //修改密码
//                    if (telEt.getText().equals("")) {
//                        Toast.makeText(getContext(), "请先输入注册电话号码", Toast.LENGTH_SHORT).show();
//                        break;
//                    }
//                    Intent intent=new Intent(getContext(),UpPwdVaActivity.class);
//                    startActivity(intent);

                    break;
            }

        }
    };

    @Override
    public void onSuccess(String result) {
        super.onSuccess(result);
        //请求成功，数据解析，通过解析类，解析工具生成解析类UserBean
        UserBean bean=new Gson().fromJson(result,UserBean.class);
        int code=bean.getCode();
        switch(code)
        {
            case 200: //成功
                Toast.makeText(getContext(),"登录成功！",Toast.LENGTH_SHORT).show();
                // 保存登录信息
                UserBean.DataBean dataBean=bean.getData();//获取用户信息
                SaveUserUtils.saveUserToFile(getContext(),dataBean);



                // 跳转到个人中心
                Intent intent=new Intent(getContext(),PersonCenterActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case 201: //用户名或密码错误
                Toast.makeText(getContext(),"用户名或密码错误！",Toast.LENGTH_SHORT).show();

                break;
            default: //其他失败
                Toast.makeText(getContext(),"登录失败！",Toast.LENGTH_SHORT).show();
                break;
        }

    }

    /*数据格式判断
     * */
    private boolean judgeInput(String tel,String pwd) {

        //大陆电话号码正则表达式
        String regex="^1(?:3\\d|4[4-9]|5[0-35-9]|6[67]|7[013-8]|8\\d|9\\d)\\d{8}$";
        if (TextUtils.isEmpty(tel)||TextUtils.isEmpty(pwd))
        {   //输入为空判断
            Toast.makeText(getContext(),"输入内容不能为空！",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!tel.matches(regex))
        {   //输入电话号码格式判断
            Toast.makeText(getContext(),"手机号码不合法！",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}