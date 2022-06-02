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
import com.yzq.rest.R;
import com.yzq.rest.utils.MyDecoration;
import com.yzq.rest.utils.SnackBarUtils;

/**
 * Created by JDK on 2016/8/4.
 */
public class ShakeAndroidFragment extends BaseFragment{
    static Context mContext;
    public ShakeAndroidFragment() {
        super(R.layout.fragment_shake_android);
    }
    public static ShakeAndroidFragment newInstance(Context context){
        ShakeAndroidFragment androidFragment=new ShakeAndroidFragment();
        mContext = context;
        return androidFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        if(isRefresh)
            binding.swipeRefreshLayout.setRefreshing(false);
        if(page>1) {
            SnackBarUtils.makeLong(getActivity().getWindow().getDecorView(), getResources().getString(R.string.shake_loadmore_footer)).danger();
            return;
        }else if(page==1){
            SnackBarUtils.makeLong(getActivity().getWindow().getDecorView(), getResources().getString(R.string.shake_refresh_header)).danger();
            return;
        }
        RequestData.getInstance(mContext).requestAndroidData(ReturnRetrofit.getInstance().getMyGankApiRetrofit().getShakeAndroidData(), getMyRecyclerView(), page, null, isFirst(), true,false);

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
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getMyRecyclerView().setLayoutManager(new LinearLayoutManager(mContext));
        getMyRecyclerView().addItemDecoration(new MyDecoration(getActivity(),MyDecoration.VERTICAL_LIST));
        getData(0);
        InitListener();
    }

}
