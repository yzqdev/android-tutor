package com.yzq.rest.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.yzq.rest.HttpUtils.RequestData;
import com.yzq.rest.HttpUtils.ReturnRetrofit;
import com.yzq.rest.model_data.entity.Base;
import com.yzq.rest.R;
import com.yzq.rest.utils.MyDecoration;
import com.yzq.rest.utils.SPDataUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JDK on 2016/8/4.
 */
public class AndroidFragment extends BaseFragment {
    private List<Base> androidList;
    static Context mContext;
    private boolean isCache=false;

    public AndroidFragment() {
        super(R.layout.fragment_watch_android);
    }
    public static AndroidFragment newInstance(Context context){
        AndroidFragment androidFragment=new AndroidFragment();
        mContext = context;
        return androidFragment;
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
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        androidList=SPDataUtil.getFirstPageGirls(getString(R.string.sharedPreferences_android),mContext);
        if(androidList!=null&&androidList.size()!=0){
            isCache=true;
        }else {
            androidList = new ArrayList<>();
        }

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
    @Override
    public void setSubscriber(int page,boolean isRefresh){
        if(isRefresh) {
            SPDataUtil.saveFirstPageGirls(mContext,androidList);
            androidList.clear();
        }
        RequestData.getInstance(mContext).requestAndroidData(ReturnRetrofit.getInstance().getMyGankApiRetrofit().getWatchAndroidData(page),getMyRecyclerView(),page,androidList,isFirst(),false,isCache);
        isCache=false;
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
        getMyRecyclerView().setLayoutManager(new LinearLayoutManager(mContext));
        getMyRecyclerView().addItemDecoration(new MyDecoration(getActivity(), MyDecoration.VERTICAL_LIST));
        getData(1);
        InitListener();
}

}
