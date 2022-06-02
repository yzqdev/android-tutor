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
import com.yzq.rest.model_data.entity.Base;
import com.yzq.rest.R;
import com.yzq.rest.utils.SPDataUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JDK on 2016/8/4.
 */
public class MeiziFragment extends BaseFragment {
    private static Context mContext;
    private List<Base> meiziList;
    private boolean isCache=false;
    public MeiziFragment() {
        super(R.layout.fragment_watch_meizi);
    }
    public static MeiziFragment newInstance(Context context){
        MeiziFragment meiziFragment=new MeiziFragment();
        mContext=context;
        return meiziFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        meiziList=SPDataUtil.getFirstPageGirls(getString(R.string.sharedPreferences_picture),mContext);
        if(meiziList!=null&&meiziList.size()!=0) {
           isCache=true;
        }else{
            meiziList = new ArrayList<>();
        }

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
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void getData(final int page) {
        setSubscriber(page,false);
    }
    public void setSubscriber(final int page,boolean isRefresh){
        if(isRefresh) {
            SPDataUtil.saveFirstPageGirls(mContext, meiziList);
            meiziList.clear();
        }
        RequestData.getInstance(mContext).requestMeiziData(ReturnRetrofit.getInstance().getMyGankApiRetrofit().getWatchMeiziData(page),
                ReturnRetrofit.getInstance().getMyGankApiRetrofit().getWatchRestVideoData(page), getMyRecyclerView(), page, meiziList, isFirst(), false,isCache);
        isCache=false;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            RequestData.getInstance(mContext).setSHProgressinterface(this);
        }

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        StaggeredGridLayoutManager staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        System.out.println("_____________________________on meizifra");
        System.out.println(new LinearLayoutManager(getActivity()));
//        getMyRecyclerView().setLayoutManager(new LinearLayoutManager(getActivity()));
        InitListener();
        binding.swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                binding.swipeRefreshLayout.setRefreshing(true);
                setSubscriber(1, false);
            }
        });
    }
}
