package com.yzq.rest.Activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.yzq.rest.Fragment.AboutAuthorFragment;
import com.yzq.rest.Fragment.AboutProjectFragment;
import com.yzq.rest.Fragment.CollectionFragment;
import com.yzq.rest.Fragment.WatchAndShakeFragment;
import com.yzq.rest.R;
import com.yzq.rest.utils.BackHandlerHelper;
import com.yzq.rest.utils.ShareUtils;
import com.yzq.rest.utils.SnackBarUtils;

import butterknife.ButterKnife;


/**
 * Created by JDK on 2016/8/28.
 */
public class MainActivity extends BaseActivity implements CollectionFragment.collectionDrawerIconListener, WatchAndShakeFragment.watchAndShakeFragmentListener, AboutProjectFragment.aboutProjectDrawerIconListener, AboutAuthorFragment.aboutAuthorDrawerIconListener {
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    private boolean isOpen;
    private WatchAndShakeFragment watchAndShakeFragment;
    private CollectionFragment collectionFragment;
    private AboutProjectFragment aboutProjectFragment;
    private AboutAuthorFragment aboutAuthorFragment;
    private Fragment currentFragment;
    private long lastBackKeyDownTick = 0;
    private static final long MAX_DOUBLE_BACK_DURATION = 1500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        showDefaultFragment();
        System.out.println("可舒服了咖啡店方_______________________________________________");
        System.out.println(mDrawerLayout);
        mDrawerLayout=this.findViewById(R.id.drawer);
        mNavigationView=this.findViewById(R.id.navigation_view);
         mNavigationView.setItemIconTintList(null);
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                isOpen = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                isOpen = false;
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
        initNavigationViewItemSelected();

    }
    public void showDefaultFragment(){
        if(watchAndShakeFragment ==null){
            watchAndShakeFragment = WatchAndShakeFragment.newInstance();
        }
        addFragment(R.id.activity_main, watchAndShakeFragment);
        currentFragment= watchAndShakeFragment;
    }
    public void initNavigationViewItemSelected(){
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_sort:
                        if (watchAndShakeFragment == null) {
                            watchAndShakeFragment = WatchAndShakeFragment.newInstance();
                        }
                        switchFragment(currentFragment, watchAndShakeFragment);
                        break;
                    case R.id.navigation_collection:
                        if (collectionFragment == null) {
                            collectionFragment = CollectionFragment.newInstance(MainActivity.this);
                        }
                        switchFragment(currentFragment, collectionFragment);
                        break;
                    case R.id.navigation_share:
                        ShareUtils.getInstance(MainActivity.this).share(getResources().getString(R.string.share_app_to_friends), "program");
                        break;
                    case R.id.navigation_about_project:
                        if (aboutProjectFragment == null) {
                            aboutProjectFragment = AboutProjectFragment.newInstance();
                        }
                        switchFragment(currentFragment, aboutProjectFragment);
                        break;
                    case R.id.navigation_about_author:
                        if (aboutAuthorFragment == null) {
                            aboutAuthorFragment = new AboutAuthorFragment();
                        }
                        switchFragment(currentFragment, aboutAuthorFragment);
                        break;

                }
                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                return false;
            }
        });
    }
    public void switchFragment(Fragment from,Fragment to){
        if(currentFragment!=to){
            currentFragment=to;
            if(!to.isAdded()){
                getFragmentTransaction().hide(from).add(R.id.activity_main,to).commit();
            }else{
                getFragmentTransaction().hide(from).show(to).commit();
            }
        }
    }
    @Override
    public void collectionDrawerIcon() {
        if (!isOpen) {
            //LEFT和RIGHT指的是现存DrawerLayout的方向
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
    }
    @Override
    public void watchAndShakeFragment() {
        if (!isOpen) {
            //LEFT和RIGHT指的是现存DrawerLayout的方向
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
    }
    @Override
    public void onBackPressed() {
        long currentTick = System.currentTimeMillis();
        if (!BackHandlerHelper.handleBackPress(this)) {
            if (isOpen) {
                mDrawerLayout.closeDrawer(mNavigationView);
                isOpen = false;
            } else {
                if (currentTick - lastBackKeyDownTick > MAX_DOUBLE_BACK_DURATION) {
                    SnackBarUtils.makeShort(getWindow().getDecorView(), "再按一次退出").danger();
                    lastBackKeyDownTick = currentTick;
                } else {
                    finish();
                    System.exit(0);
                }
            }
        }
    }
    @Override
    public void aboutProjectDrawerIcon() {
        if (!isOpen) {
            //LEFT和RIGHT指的是现存DrawerLayout的方向
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
    }

    @Override
    public void aboutAuthorDrawerIcon() {
        if (!isOpen) {
            //LEFT和RIGHT指的是现存DrawerLayout的方向
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
    }
    public void settingOnClick(View v){
        SnackBarUtils.makeShort(v,getResources().getString(R.string.onClick_setting)).warning();
    }
    public void quitOnClick(View v){
        finish();
        System.exit(0);
    }
}
