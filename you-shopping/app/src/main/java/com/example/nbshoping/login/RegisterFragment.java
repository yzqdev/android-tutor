package com.example.nbshoping.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nbshoping.R;
import com.example.nbshoping.utils.BaseFragment;
import com.example.nbshoping.utils.URLUtils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;


public class RegisterFragment extends BaseFragment {
    EditText telEt, nicknameEt, pwdEt, repwdEt,questionEt,answerEt;//电话号码，昵称，密码确认密码，
    Button ensurebutton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        telEt = view.findViewById(R.id.frag_reg_et_tel1);
        nicknameEt = view.findViewById(R.id.frag_reg_te_name);
        pwdEt = view.findViewById(R.id.frag_reg_et_pwd);
        repwdEt = view.findViewById(R.id.frag_reg_et_repwd);
        ensurebutton = view.findViewById(R.id.frag_reg_btn);
        questionEt=view.findViewById(R.id.frag_reg_et_question);
        answerEt=view.findViewById(R.id.frag_reg_et_answer);

        ensurebutton.setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String tel = telEt.getText().toString();
            String nickname = nicknameEt.getText().toString();
            String pwd = pwdEt.getText().toString();
            String repwd = repwdEt.getText().toString();
            String question = questionEt.getText().toString();
            String answer = answerEt.getText().toString();

            //数据格式判断
            boolean flag = judgeInput(tel, nickname, pwd, repwd,question,answer);
            if (flag) {
                //可执行网络请求,添加网络请求权限，
                Map<String, String> map = new HashMap<>();
                map.put("phone", tel);
                map.put("password", pwd);
                map.put("name", "");
                map.put("address", "");
                map.put("nickname", nickname);
                map.put("question", question);
                map.put("answer", answer);
                postNetwork(URLUtils.register_url, map);

            }
        }
    };

    @Override
    public void onSuccess(String result) {
        super.onSuccess(result);
        //请求成功，数据解析，通过解析类，解析工具生成解析类UserBean
        UserBean bean = new Gson().fromJson(result, UserBean.class);
        int code = bean.getCode();
        switch (code) {
            case 200: //注册成功
                Toast.makeText(getContext(), "注册成功！", Toast.LENGTH_SHORT).show();
                //是否跳转到登录界面
                new AlertDialog.Builder(getContext(), R.style.UpdateDialogStyle)
                        .setTitle("提示信息")
                        .setIcon(R.mipmap.icon)
                        .setMessage("注册成功，是否切换到登陆界面？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //切换到登录界面
                                //显示登录界面

                                ((LoginRegActivity)getActivity()).loginvp.setCurrentItem(0);

                            }
                        })
                        .create()
                        .show();


                break;
            case 201: //手机号码已经被注册
                Toast.makeText(getContext(), "手机号码已经注册，请直接注册！", Toast.LENGTH_SHORT).show();
                //todo是否跳转到登录界面
                new AlertDialog.Builder(getContext(), R.style.UpdateDialogStyle)
                        .setTitle("提示信息")
                        .setIcon(R.mipmap.icon)
                        .setMessage("该号码已经注册，是否切换到登陆界面？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //切换到登录界面
                                //显示登录界面
                                ((LoginRegActivity)getActivity()).loginvp.setCurrentItem(0);

                            }
                        })
                        .create()
                        .show();
                break;
            default: //其他注册失败
                Toast.makeText(getContext(), "注册失败！", Toast.LENGTH_SHORT).show();

                break;
        }

    }

    /*数据格式判断
     * */
    private boolean judgeInput(String tel, String nickname, String pwd, String repwd,String question,String answer) {


        if (TextUtils.isEmpty(tel) || TextUtils.isEmpty(nickname) || TextUtils.isEmpty(pwd)
                || TextUtils.isEmpty(repwd)||TextUtils.isEmpty(question)||TextUtils.isEmpty(answer)) {   //输入为空判断
            Toast.makeText(getContext(), "输入内容不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!pwd.equals(repwd)) {
            Toast.makeText(getContext(), "两次输入密码不同！", Toast.LENGTH_SHORT).show();
            return false;
        }
        //大陆电话号码正则表达式
        String regex = "^1(?:3\\d|4[4-9]|5[0-35-9]|6[67]|7[013-8]|8\\d|9\\d)\\d{8}$";
        if (!tel.matches(regex)) {   //输入电话号码格式判断
            Toast.makeText(getContext(), "手机号码不合法！", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


}



