package com.yzq.rest.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.yzq.rest.HttpUtils.RequestData;
import com.yzq.rest.HttpUtils.ReturnRetrofit;
import com.yzq.rest.R;
import com.yzq.rest.utils.SnackBarUtils;

/**
 * Created by JDK on 2016/8/4.
 */
public class ShakeMeiziFragment extends BaseFragment implements RequestData.shProgressinterfaceofShake {
    private static Context mContext;
    public ShakeMeiziFragment() {
        super(R.layout.fragment_shake_meizi);
    }
    public static ShakeMeiziFragment newInstance(Context context){
        ShakeMeiziFragment meiziFragment=new ShakeMeiziFragment();
        mContext=context;
        return meiziFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return super.onCreateView(inflater, container, savedInstanceState);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void getData(final int page) {
        setSubscriber(page,false);
    }

    /**
     * 这里的page的作用是用来判断snackbar的显示与否
     * @param page
     */
    public void setSubscriber(final int page,boolean isRefresh){
        if(isRefresh)
        binding.swipeRefreshLayout.setRefreshing(false);
        //这里设置return是因为当在摇一摇界面的时候刷新的话直接跳出，不请求数据，节省流量。
        if(page>1) {
            SnackBarUtils.makeLong(getActivity().getWindow().getDecorView(), getResources().getString(R.string.shake_loadmore_footer)).danger();
            return;
        }else if(page==1){
            SnackBarUtils.makeLong(getActivity().getWindow().getDecorView(), getResources().getString(R.string.shake_refresh_header)).danger();
            return;
        }
        RequestData.getInstance(mContext).requestMeiziData(ReturnRetrofit.getInstance().getMyGankApiRetrofit().getShakeMeiziData(),
                ReturnRetrofit.getInstance().getMyGankApiRetrofit().getShakeRestVideoData(),getMyRecyclerView(),page,null,isFirst(),true,false);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)
            RequestData.getInstance(mContext).setSHProgressinterfaceofShake(this);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        StaggeredGridLayoutManager staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        getMyRecyclerView().setLayoutManager(new LinearLayoutManager(getActivity()));
        InitListener();
        binding.swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                binding.swipeRefreshLayout.setRefreshing(true);
                setSubscriber(0, false);
            }
        });
    }

}
