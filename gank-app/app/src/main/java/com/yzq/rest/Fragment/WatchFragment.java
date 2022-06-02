package com.yzq.rest.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.material.tabs.TabLayout;
import com.yzq.rest.databinding.PafFragmentLayoutBinding;

import java.util.ArrayList;
import java.util.List;


public class WatchFragment extends Fragment{
//    @BindView(R.id.myViewPager)
//    ViewPager myViewPager;
//    @BindView(R.id.watch_tl)
//    TabLayout tabLayout;
    Fragment meiziFragment;
    Fragment androidFragment;
    Fragment frontFragment;
    List<Fragment> fragmentList;

    private PafFragmentLayoutBinding binding;
    private List<String> mTitleList = new ArrayList<>(3);
    View v;
    static WatchFragment watchFragment;
    public static Fragment getInstance(){
        if( watchFragment ==null){
            watchFragment =new WatchFragment();
        }
        return watchFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       binding=PafFragmentLayoutBinding.inflate(inflater,container,false);
        InitVariable();
        v=binding.getRoot();
        MyAdapter myAdapter = new MyAdapter(getChildFragmentManager());
        binding.myViewPager.setAdapter(myAdapter);
        binding.myViewPager.setOffscreenPageLimit(2);
        binding.watchTl.setTabMode(TabLayout.MODE_FIXED);
        binding.watchTl.setupWithViewPager(binding.myViewPager);
        return v;
    }

    public void InitVariable(){
        mTitleList.add("Picture");
        mTitleList.add("Android");
        mTitleList.add("Front");
        meiziFragment=MeiziFragment.newInstance(getActivity());
        androidFragment=AndroidFragment.newInstance(getActivity());
        frontFragment=FrontFragment.newInstance(getActivity());
        fragmentList=new ArrayList<Fragment>();
        fragmentList.add(meiziFragment);
        fragmentList.add(androidFragment);
        fragmentList.add(frontFragment);
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }


}
