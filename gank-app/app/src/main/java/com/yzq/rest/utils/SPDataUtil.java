package com.yzq.rest.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.yzq.rest.model_data.entity.Base;
import com.yzq.rest.model_data.entity.Front;
import com.yzq.rest.model_data.entity.MyAndroid;
import com.yzq.rest.model_data.entity.Video;
import com.yzq.rest.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * SharePreferenceDataUtils
 * Created by xybcoder on 2016/3/1.
 */
public class SPDataUtil {
    private static final String RESTAPP = "RESTAPP";
    private static final String PICTURE = "picture";
    private static final String ANDROID = "android";
    private static final String FRONT = "front";
    private static Gson gson = new Gson();

    public static boolean saveFirstPageGirls(Context context, List<Base> data) {
            SharedPreferences   preferences = context.getSharedPreferences(RESTAPP, Context.MODE_PRIVATE);
            if (data.get(0) instanceof Video) {
                return preferences.edit().putString(PICTURE, gson.toJson(data)).commit();
            }
            if(data.get(0) instanceof MyAndroid){
                return preferences.edit().putString(ANDROID, gson.toJson(data)).commit();
            }
            if(data.get(0) instanceof Front) {
                return preferences.edit().putString(FRONT, gson.toJson(data)).commit();
            }
            else{
                return false;
            }

    }

    public static List<Base> getFirstPageGirls(String type,Context context) {
        SharedPreferences preferences = context.getSharedPreferences(RESTAPP, Context.MODE_PRIVATE);
        if (preferences.getString(PICTURE,"").length()!=0||preferences.getString(ANDROID,"")!=null||preferences.getString(FRONT,"")!=null) {
            if (type.equals(context.getString(R.string.sharedPreferences_picture))) {
                return gson.fromJson(preferences.getString(PICTURE, ""), new TypeToken<List<Video>>() {
                }.getType());
            }
            if (type.equals(context.getString(R.string.sharedPreferences_android))) {
                return gson.fromJson(preferences.getString(ANDROID, ""), new TypeToken<List<MyAndroid>>() {
                }.getType());
            }
            if (type.equals(context.getString(R.string.sharedPreferences_front))) {
                return gson.fromJson(preferences.getString(FRONT, ""), new TypeToken<List<Front>>() {
                }.getType());
            } else {
                return null;
            }
        }else{
            return null;
        }
    }
}
