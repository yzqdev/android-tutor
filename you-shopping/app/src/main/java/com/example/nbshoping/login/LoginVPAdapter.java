package com.example.nbshoping.login;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/*
 *viewpaper适配器
 * 辅助将fragment填充到viewpaper中
 */
public class LoginVPAdapter extends FragmentPagerAdapter {
    List<Fragment> fragmentList;//两个

    public LoginVPAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;

    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
