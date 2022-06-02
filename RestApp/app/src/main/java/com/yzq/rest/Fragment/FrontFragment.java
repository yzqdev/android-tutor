package com.yzq.rest.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.yzq.rest.R;
import com.yzq.rest.HttpUtils.RequestData;
import com.yzq.rest.HttpUtils.ReturnRetrofit;
import com.yzq.rest.utils.MyDecoration;
import com.yzq.rest.utils.SPDataUtil;
import com.yzq.rest.model_data.entity.Base;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JDK on 2016/8/10.
 */
public class FrontFragment extends BaseFragment {
    private List<Base> frontList;
    private static Context mContext;
    private boolean isCache=false;
    public FrontFragment() {
        super(R.layout.fragment_watch_front);
    }
    public static FrontFragment newInstance(Context context){
        FrontFragment frontFragment=new FrontFragment();
        mContext=context;
        return frontFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            RequestData.getInstance(mContext).setSHProgressinterface(this);
        }

    }@Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        frontList =SPDataUtil.getFirstPageGirls(getString(R.string.sharedPreferences_front),mContext);
        if(frontList !=null&& frontList.size()!=0){
            isCache=true;
        }else {
            frontList = new ArrayList<>();
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void getData(int page) {
        setSubscriber(page,false);
    }
    public void setSubscriber(int page,boolean isRefresh){
        if(isRefresh) {
            SPDataUtil.saveFirstPageGirls(mContext, frontList);
            frontList.clear();
        }
        RequestData.getInstance(mContext).requestFrontData(ReturnRetrofit.getInstance().getMyGankApiRetrofit().getWatchFrontData(page), getMyRecyclerView(), page, frontList,isFirst(),false,isCache);
        isCache=false;

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getMyRecyclerView().setLayoutManager(new LinearLayoutManager(mContext));
        getMyRecyclerView().addItemDecoration(new MyDecoration(getActivity(), MyDecoration.VERTICAL_LIST));
        getData(1);
        InitListener();
    }



}

