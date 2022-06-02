package com.yzq.rest.Activity;

import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.yzq.rest.R;


/**
 * Created by JDK on 2016/8/8.
 */
public class BaseActivity extends AppCompatActivity {
    public void setFullScreen(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
        ,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    public void setWebViewAdaptScreen(int layout_id,int id){
        setContentView(layout_id);
        LinearLayout linearLayout= (LinearLayout) findViewById(R.id.webview_linearlayout);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;   // 屏幕高度（像素）
        float density = metric.density;      // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(width,height-260);
        linearLayout.setLayoutParams(params);
    }

    public FragmentTransaction getFragmentTransaction(){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        return fragmentTransaction;
    }
    public void addFragment(int id,Fragment fragment){
       getFragmentTransaction().add(id, fragment).commit();
    }
    public void addFragmentWithTag(int id,Fragment fragment,String TAG){
        getFragmentTransaction().add(id, fragment,TAG).commit();
    }
    public void replaceFragment(int id,Fragment fragment){
        getFragmentTransaction().replace(id, fragment).addToBackStack(null).commit();
    }
    public void replaceFragmentWithTag(int id,Fragment fragment,String TAG){
        getFragmentTransaction().replace(id,fragment,TAG).addToBackStack(null).commit();
    }
}
