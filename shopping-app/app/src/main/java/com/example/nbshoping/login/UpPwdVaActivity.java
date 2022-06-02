package com.example.nbshoping.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nbshoping.R;
import com.example.nbshoping.utils.BaseActivity;
import com.example.nbshoping.utils.SaveUserUtils;
import com.example.nbshoping.utils.URLUtils;
import com.google.gson.Gson;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

/*密保：
*1显示
*2验证
* */
public class UpPwdVaActivity extends BaseActivity {
    EditText qEv,aEv;
    Button surebtn;
    ImageView backbt;
    private UserBean.DataBean userInfo = null;//之前登陆时保存的用户信息


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uppwdva);
        userInfo = SaveUserUtils.getUserInfo(this);

        initView();
        initshowquestion(userInfo);


    }

    //显示密保问题
    private void initshowquestion(UserBean.DataBean userInfo) {

        if (userInfo == null) {
            Toast.makeText(getApplicationContext(), "没有获取到账户信息,请先登录(密码设置界面)", Toast.LENGTH_SHORT).show();
            return;
        }
        // 显示密保问题
        //将url封装到请求参数中
        RequestParams requestParams = new RequestParams(URLUtils.showQuestion_url);
        //提交的键值对放到请求参数中
        requestParams.addParameter("id", userInfo.getId());
        requestParams.setAsJsonContent(true);//设置内容，形式
        requestParams.setBodyContentType("application/json;charset=utf-8");

        x.http().get(requestParams, new CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //请求成功，数据解析，通过解析类，解析工具生成解析类UserBean
                UserBean bean = new Gson().fromJson(result, UserBean.class);
                int code = bean.getCode();
                /*显示密保问题*/
                switch (code) {
                    case 200: //显示密保问题成功
                        qEv.setText(bean.getMessage());
                        break;
                    case 201: //未设安全问题
                        Toast.makeText(getApplicationContext(), "未设置安全问题！", Toast.LENGTH_SHORT).show();
                        break;
                    default: //其他失败
                        Toast.makeText(getApplicationContext(), "参数错误！", Toast.LENGTH_SHORT).show();
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
        qEv = findViewById(R.id.uppwd_q_ev);
        aEv = findViewById(R.id.uppwd_a_ev);
        backbt=findViewById(R.id.uppwdva_iv_back);

        surebtn = findViewById(R.id.uppwd_sure_btn);

        surebtn.setOnClickListener(onClickListener);
        backbt.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId()==R.id.uppwdva_iv_back)
            {
                finish();
                return;
            }

            if (judgeInput(String.valueOf(aEv.getText()).trim()) == true) {
                Toast.makeText(getApplicationContext(), "密保答案不能为空！", Toast.LENGTH_SHORT).show();
                return;
            }
//            验证密保问题
            Map<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(userInfo.getId()));
            map.put("answer", aEv.getText().toString().trim());
            postNetwork(URLUtils.verifyAnswer_url, map);

        }
    };


    @Override
    public void onSuccess(String result) {
        super.onSuccess(result);
        //请求成功，数据解析，通过解析类，解析工具生成解析类UserBean
        UserBean bean = new Gson().fromJson(result, UserBean.class);
        int code = bean.getCode();

        /* 验证密保答案*/

        switch (code) {
            case 200: //显示密保问题成功
                Toast.makeText(getApplicationContext(), "验证通过！", Toast.LENGTH_SHORT).show();
                fixpwd();
                break;
            case 201: //未设安全问题
                Toast.makeText(getApplicationContext(), "答案错误！", Toast.LENGTH_SHORT).show();
                break;
            default: //其他失败
                Toast.makeText(getApplicationContext(), "参数错误！", Toast.LENGTH_SHORT).show();
                break;
        }




    }



    /*密保验证成功，进入密码修改*/
    private void fixpwd() {
        Intent intent =new Intent(getApplicationContext(),UpPwdActivity.class);
        startActivity(intent);
        finish();
    }


    /*数据格式判断
     * */
    private boolean judgeInput(String string) {
        if (string.equals(""))//空判断
            return true;
        return false;
    }

}