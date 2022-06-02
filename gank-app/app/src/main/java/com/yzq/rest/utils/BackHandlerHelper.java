package com.yzq.rest.utils;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.List;

/**
 * Created by JDK on 2016/11/18.
 */
public class BackHandlerHelper {
    public static boolean handleBackPress(FragmentManager fragmentManager){
        List<Fragment> fragmentList=fragmentManager.getFragments();
        if(fragmentList==null) return false;
        for(int i=fragmentList.size()-1;i>=0;i--){
            Fragment fragment=fragmentList.get(i);
            if(isFragmentBackHandled(fragment))
                return true;
        }
        if(fragmentManager.getBackStackEntryCount()>0){
            fragmentManager.popBackStack();
            return true;
        }
        return false;

    }
    public static boolean handleBackPress(FragmentActivity fragmentActivity) {
        return handleBackPress(fragmentActivity.getSupportFragmentManager());
    }
    public static boolean isFragmentBackHandled(Fragment fragment) {
        return fragment != null
                && fragment.isVisible()
                && fragment instanceof FragmentBackHandler
                && ((FragmentBackHandler) fragment).onBackPressed();
    }
}
