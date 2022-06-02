package com.yzq.rest.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.yzq.rest.Activity.ShowWebViewActivity;
import com.yzq.rest.R;
import com.yzq.rest.custom_widget.CustomClearAndSearechEdittext;
import com.yzq.rest.databinding.FragmentWatchShakeBinding;
import com.yzq.rest.model_data.entity.URLTableData;
import com.yzq.rest.utils.FragmentBackHandler;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by JDK on 2016/8/26.
 */
public class WatchAndShakeFragment extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener, CustomClearAndSearechEdittext.OnClickSearchListener, TextView.OnEditorActionListener, FragmentBackHandler {
//    @BindView(R.id.tool_bar)
//    Toolbar toolbar;
//    @BindView(R.id.drawerIcon)
//    TextView drawerIcon;
//    @BindView(R.id.watch_tv)
//     TextView watch_tv;
//    @BindView(R.id.shake_tv)
//     TextView shake_tv;
//    @BindView(R.id.view_pager)
//    ViewPager view_pager;
//    @BindView(R.id.search_tv)
//   TextView search_tv;
//    @BindView(R.id.search_layout)
//    LinearLayout search_layout;
//    @BindView(R.id.search_til)
//   TextInputLayout textInputLayout;
//    @BindView(R.id.search_et)
//   EditText editText;

    private FragmentWatchShakeBinding binding;
    private ActionBar actionBar;
    //ToolBar三个按钮对应的Fragment
    private List<Fragment> fragmentlist = new ArrayList<>(2);

    private MyFragmentPagerAdapter adapter;
    private static WatchAndShakeFragment watchAndShakeFragment;

    public static WatchAndShakeFragment newInstance() {
        if (watchAndShakeFragment == null) {
            watchAndShakeFragment = new WatchAndShakeFragment();
        }
        return watchAndShakeFragment;
    }

    @Override
    public void onClickSearch() {
        search();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentWatchShakeBinding.inflate(inflater, container, false);
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolBar);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        getActivity().getSupportFragmentManager().popBackStack();
        addFragment();
        initWidgets();
        View v = binding.getRoot();
        return v;
    }

    @Override
    public boolean onBackPressed() {
        if (binding.shakeTv.getVisibility() != View.VISIBLE) {
            binding.shakeTv.setVisibility(View.VISIBLE);
            binding.watchTv.setVisibility(View.VISIBLE);
            binding.drawerIcon.setVisibility(View.VISIBLE);
            actionBar.setDisplayHomeAsUpEnabled(false);
            binding.toolBar.setNavigationIcon(null);
            binding.searchEt.setFocusable(false);
            binding.searchEt.setFocusableInTouchMode(false);
            binding.searchEt.clearFocus();
            binding.searchTil.setVisibility(View.GONE);
            return true;
        } else {
            return false;
        }
    }

    //通过点击软键盘可以搜索
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            search();
        }
        return false;
    }


    public interface watchAndShakeFragmentListener {
        public void watchAndShakeFragment();
    }


    private void initWidgets() {
        adapter = new MyFragmentPagerAdapter(getFragmentManager());
        getFragmentManager();
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.addOnPageChangeListener(this);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "iconfont.ttf");
        binding.drawerIcon.setTypeface(typeface);
        binding.drawerIcon.setOnClickListener(this);
        binding.watchTv.setOnClickListener(this);
        binding.watchTv.setTypeface(typeface);
        binding.shakeTv.setOnClickListener(this);
        binding.shakeTv.setTypeface(typeface);
        binding.searchTv.setTypeface(typeface);
        binding.searchLayout.setOnClickListener(this);
        //初始化显示位置
        binding.watchTv.setSelected(true);
        binding.viewPager.setCurrentItem(0);
    }

    public void search() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ShowWebViewActivity.class);
        String url = "http://gank.io/search?q=" + binding.searchEt.getText().toString();
        URLTableData urlTableData = new URLTableData(url, "", "搜索 [" + binding.searchEt.getText().toString() + "] 的结果", null);
        urlTableData.setType("");
        urlTableData.setIsCollected(false);
        Bundle bundle = new Bundle();
        bundle.putSerializable("urlTableData", urlTableData);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void addFragment() {
        fragmentlist.add(WatchFragment.getInstance());
        fragmentlist.add(ShakeFragment.getInstance(getActivity()));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void getFocus() {
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && binding.shakeTv.getVisibility() != View.VISIBLE) {
                    binding.shakeTv.setVisibility(View.VISIBLE);
                    binding.watchTv.setVisibility(View.VISIBLE);
                    binding.drawerIcon.setVisibility(View.VISIBLE);
                    actionBar.setDisplayHomeAsUpEnabled(false);
                    binding.toolBar.setNavigationIcon(null);
                    binding.searchTil.setVisibility(View.GONE);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.watch_tv:
                binding.watchTv.setSelected(true);
                binding.shakeTv.setSelected(false);
                binding.viewPager.setCurrentItem(0);
                break;
            case R.id.shake_tv:
                binding.shakeTv.setSelected(true);
                binding.watchTv.setSelected(false);
                binding.viewPager.setCurrentItem(1);
                break;
            case R.id.drawerIcon:
                if (getActivity() instanceof watchAndShakeFragmentListener) {
                    ((watchAndShakeFragmentListener) getActivity()).watchAndShakeFragment();
                }
                break;
            case R.id.search_layout:
                binding.shakeTv.setVisibility(View.GONE);
                binding.watchTv.setVisibility(View.GONE);
                binding.drawerIcon.setVisibility(View.GONE);
                binding.searchTil.setVisibility(View.VISIBLE);
                binding.searchEt.setFocusableInTouchMode(true);
                binding.searchEt.setFocusable(true);
                binding.searchEt.requestFocus();
                ((CustomClearAndSearechEdittext) binding.searchEt).setOnClickSearchListener(this);
                binding.searchEt.setOnEditorActionListener(this);
                binding.toolBar.setNavigationIcon(R.drawable.ic_back_28dp);
                binding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.shakeTv.setVisibility(View.VISIBLE);
                        binding.watchTv.setVisibility(View.VISIBLE);
                        binding.drawerIcon.setVisibility(View.VISIBLE);
                        actionBar.setDisplayHomeAsUpEnabled(false);
                        binding.toolBar.setNavigationIcon(null);
                        binding.searchEt.clearFocus();
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(binding.searchEt.getWindowToken(), 0);
                        binding.searchTil.setVisibility(View.GONE);
                    }
                });
            default:
                break;
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                binding.watchTv.setSelected(true);
                binding.shakeTv.setSelected(false);
                binding.viewPager.setCurrentItem(0);
                break;
            case 1:
                binding.shakeTv.setSelected(true);
                binding.watchTv.setSelected(false);
                binding.viewPager.setCurrentItem(1);
                break;

        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentlist.get(position);
        }

        @Override
        public int getCount() {
            return fragmentlist.size();
        }
    }


}
