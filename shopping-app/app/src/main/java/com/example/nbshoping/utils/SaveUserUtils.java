package com.example.nbshoping.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.nbshoping.login.UserBean;

/*登录成功后信息保存到本地工具类
 * 1.SharedPreferences共享参数保存
 * 2.file保存
 * 3.数据库存储
 * */
public class SaveUserUtils {
    private static UserBean.DataBean data = null;//保存在内存中数据

    //将登录信息保存到内存中
    public static boolean saveUserToMemory(UserBean.DataBean dataBean) {
        data = dataBean;
        return true;

    }

    //将注册信息保存到文件
    public static boolean saveUserToFile(Context context, UserBean.DataBean dataBean) {
        /*共享共享参数数据保存
         */
        //1获取共享参数对象
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        //2获取编辑对象
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //3开始写数据
        editor.putInt("id", dataBean.getId());
        editor.putString("phone", dataBean.getPhone());
        editor.putString("password", dataBean.getPassword());
        editor.putString("name", dataBean.getName());
        editor.putString("address", dataBean.getAddress());
        editor.putString("nickname", dataBean.getNickname());
        editor.putString("question", dataBean.getQuestion());
        editor.putString("answer", dataBean.getAnswer());
        //4提交结果
        editor.commit();


        //保存到内存
        saveUserToMemory(dataBean);
        return true;
    }

    //获取用户信息数据
    public static UserBean.DataBean getUserDataFromFile(Context context) {
        //获取共享参数数据
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", context.MODE_PRIVATE);
        int id = sharedPreferences.getInt("id", -1);//没有数据默认id为-1
        String phone = sharedPreferences.getString("phone", "");
        String password = sharedPreferences.getString("password", "");
        String name = sharedPreferences.getString("name", "");
        String address = sharedPreferences.getString("address", "");
        String nickname = sharedPreferences.getString("nickname", "");
        String question = sharedPreferences.getString("question", "");
        String answer = sharedPreferences.getString("answer", "");
        UserBean.DataBean dataBean = new UserBean.DataBean();
        dataBean.setId(id);
        dataBean.setPhone(phone);
        dataBean.setPassword(password);
        dataBean.setName(name);
        dataBean.setAddress(address);
        dataBean.setNickname(nickname);
        dataBean.setQuestion(question);
        dataBean.setAnswer(answer);
        return dataBean;
    }

    /*获取用户信息，先从内存获取，没有再从文件回去*/
    public static UserBean.DataBean getUserInfo(Context context) {
        if (data != null) {//内存
            return data;
        }
        UserBean.DataBean user = getUserDataFromFile(context);
        if (user.getId() != -1) {
            data = user;
            return data;//保存到内存，并返回
        }
        return null;
    }

    /*清除内存和sharedpreference保存的user信息*/
    public static void deleteUserInfo(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        data = null;

    }

}
