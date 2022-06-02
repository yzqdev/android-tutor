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
import com.yzq.rest.utils.SnackBarUtils;
import com.yzq.rest.model_data.entity.Base;

import java.util.List;

/**
 * Created by JDK on 2016/8/10.
 */
public class ShakeFrontFragment extends BaseFragment {
    private List<Base> myList;
    private static Context mContext;
    public ShakeFrontFragment() {
        super(R.layout.fragment_shake_front);
    }
    public static ShakeFrontFragment newInstance(Context context){
        ShakeFrontFragment frontFragment=new ShakeFrontFragment();
        mContext=context;
        return frontFragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
//        if(isRefresh)
            binding.swipeRefreshLayout.setRefreshing(false);
        if(page>1) {
            SnackBarUtils.makeLong(getActivity().getWindow().getDecorView(), getResources().getString(R.string.shake_loadmore_footer)).danger();
            return;
        }else if(page==1){
            SnackBarUtils.makeLong(getActivity().getWindow().getDecorView(), getResources().getString(R.string.shake_refresh_header)).danger();
            return;
        }
        RequestData.getInstance(mContext).requestFrontData( ReturnRetrofit.getInstance().getMyGankApiRetrofit().getShakeFrontData(),getMyRecyclerView(),page,null,isFirst(),true,false);
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getMyRecyclerView().setLayoutManager(new LinearLayoutManager(mContext));
        getMyRecyclerView().addItemDecoration(new MyDecoration(getActivity(), MyDecoration.VERTICAL_LIST));
        getData(0);
        InitListener();
    }



}

