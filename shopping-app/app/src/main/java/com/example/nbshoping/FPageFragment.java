package com.example.nbshoping;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nbshoping.fpage.BannerAdapter;
import com.example.nbshoping.fpage.HotFragment;
import com.example.nbshoping.goods.SearchActivity;
import com.example.nbshoping.utils.URLUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页碎片
 */
public class FPageFragment extends Fragment {

    private boolean ischange = false;//刷新fragment标志

    EditText searchEt;//
    ViewPager typeVp, bannerVp;
    LinearLayout pointLayout;//小点点布局
    List<ImageView> bannerList;
    List<ImageView> pointList;//小圆点
    int resId[] = {R.mipmap.ad1, R.mipmap.ad2, R.mipmap.ad3, R.mipmap.ad4, R.mipmap.ad5};//数据源加载图片


    //定时器，滑动banner条目
    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what ==1) {
                int currentItem=bannerVp.getCurrentItem();//当前滑到的页
                if (currentItem == bannerList.size()-1) {//滑倒最后页，滑倒第一页
                    bannerVp.setCurrentItem(0);
                }else {
                    currentItem++;
                    bannerVp.setCurrentItem(currentItem);//自动下一页
                }
                handler.sendEmptyMessageDelayed(1,5000);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fpage, container, false);
        initView(view);
        searchEt.setText("");

        loadFragment();
        setBannerPager();//设置中间左右滑动的viewpager

        return view;
    }


    //设置中间左右滑动的viewpager
    private void setBannerPager() {
        handler.sendEmptyMessageDelayed(1,5000);//第一次发生消息
        //数据源
        bannerList = new ArrayList<>();
        pointList = new ArrayList<>();
        //填充数据
        for (int i = 0; i < resId.length; i++) {
            //图片
            ImageView imageView = new ImageView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(params);
            imageView.setImageResource(resId[i]);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);//设置显示
            bannerList.add(imageView);
            //小点点
            ImageView piv = new ImageView(getContext());
            LinearLayout.LayoutParams pparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            pparams.setMargins(0,0,8,0);//外边距
            piv.setLayoutParams(pparams);
            piv.setImageResource(R.mipmap.page__normal_indicator);
            pointList.add(piv);//放到集合中，统一管理
            pointLayout.addView(piv);//加载到布局
        }

        //设置适配器
        BannerAdapter bannerAdapter=new BannerAdapter(bannerList);
        bannerVp.setAdapter(bannerAdapter);
        //小点点第一个选中
        pointList.get(0).setImageResource(R.mipmap.page__selected_indicator);
        //滑动颜色变化
        bannerVp.addOnPageChangeListener(listener);



    }
    ViewPager.OnPageChangeListener listener=new ViewPager.SimpleOnPageChangeListener(){

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < pointList.size(); i++) {
                pointList.get(i).setImageResource(R.mipmap.page__normal_indicator);
            }
            pointList.get(position).setImageResource(R.mipmap.page__selected_indicator);

        }
    };

    /*将热销产品和产品推荐的产品进行加载*/
    private void loadFragment() {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.fpage_hot_layput, HotFragment.newInstance(URLUtils.hotCommodity_url, ""));
        transaction.add(R.id.fpage_hot_recommendt, HotFragment.newInstance(URLUtils.recommendCommodity_url, ""));
        transaction.commit();


    }


    /*切换刷新*/
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            //隐藏时所作的事情
            searchEt.setText("");//搜索框恢复
        } else {
            //显示时所作的事情

        }


    }

    private void initView(View view) {
        searchEt = view.findViewById(R.id.fpage_sc_et);
        typeVp = view.findViewById(R.id.fpage_type_vp);
        bannerVp = view.findViewById(R.id.fpage_banner_vp);
        pointLayout = view.findViewById(R.id.fpage_point_layout);

        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {

//                    if ((actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_SEARCH) && keyEvent.getKeyCode() == KeyEvent.ACTION_UP) {
                    //点击搜索要做的操作
                    search();
                    return true;
                }
                return false;
            }
        });

    }

    //点击搜索要做的操作
    private void search() {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        intent.putExtra("goodsname", searchEt.getText().toString().trim());
        startActivity(intent);
        Toast.makeText(getContext(), "首页", Toast.LENGTH_SHORT).show();

    }
}