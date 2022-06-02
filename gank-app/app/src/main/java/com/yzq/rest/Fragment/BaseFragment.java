package com.yzq.rest.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.yzq.rest.HttpUtils.RequestData;
import com.yzq.rest.R;
import com.yzq.rest.custom_widget.LoadMoreRecyclerView;
import com.yzq.rest.databinding.SwiperefreshlayoutLayoutBinding;


/**
 * Created by JDK on 2016/8/4.
 */
//这个BaseFragment主要实现了下拉刷新和上拉加载
public abstract  class BaseFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, LoadMoreRecyclerView.LoadMoreListener, RequestData.shProgressinterface {
//    @BindView(R.id.swipe_refresh_layout)
//    SwipeRefreshLayout swipeRefreshLayout;
//    @BindView(R.id.LM_RecyclerView)
//     LoadMoreRecyclerView mLMRecyclerView;

     public SwiperefreshlayoutLayoutBinding binding;
    //判断是第一次加载数据还是后面的更新数据
    private boolean isFirst=true;
    private int mLayout_Id;
    private int t=1;
    private View v;

    public BaseFragment(int layout_Id){
        this.mLayout_Id=layout_Id;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setIsFirst(boolean isFirst) {
        this.isFirst = isFirst;
    }

    public LoadMoreRecyclerView getMyRecyclerView() {
        return binding.LMRecyclerView ;
    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

     binding = SwiperefreshlayoutLayoutBinding.inflate(inflater, container, false);
        v=binding.getRoot();
        return  v;
    }
    public abstract void getData(int page);
    public abstract void  setSubscriber(int page,boolean isRefersh);
    public void InitListener(){
        binding.LMRecyclerView.setLoadMoreListener(this);
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.green, R.color.blue);
        binding.swipeRefreshLayout.setOnRefreshListener(this);
    }
    @Override
    public void onRefresh() {
     setIsFirst(false);
     setSubscriber(1,true);
    }

    @Override
    public void loadMore() {
            setIsFirst(false);
                t++;
            setSubscriber(t,false);
    }

    @Override
    public void hideProgress() {
            if (binding.swipeRefreshLayout.isRefreshing())
                binding.swipeRefreshLayout.setRefreshing(false);
    }
    @Override
    public void showProgress() {
        if (!binding.swipeRefreshLayout.isRefreshing())
            binding.swipeRefreshLayout.setRefreshing(true);
    }
}
