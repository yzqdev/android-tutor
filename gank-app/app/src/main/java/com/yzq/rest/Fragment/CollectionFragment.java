package com.yzq.rest.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.yzq.rest.Activity.ShowWebViewActivity;
import com.yzq.rest.Adapter.RecyclerViewDataAdapter;
import com.yzq.rest.BR;
import com.yzq.rest.DataBase.MySQLiteWebViewTextBussiness;
import com.yzq.rest.R;
import com.yzq.rest.databinding.FragmentWatchShakeBinding;
import com.yzq.rest.model_data.entity.Base;
import com.yzq.rest.model_data.entity.URLTableData;
import com.yzq.rest.utils.SnackBarUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by JDK on 2016/8/13.
 */
public class CollectionFragment extends BaseFragment implements View.OnClickListener{
//    @BindView(R.id.drawerIcon)
//    TextView drawerIcon_tv;
    private static Context mContext;
    private List<Base> myList;
    private MySQLiteWebViewTextBussiness mySQLiteWebViewTextBussiness;
    private View v;
    private RecyclerViewDataAdapter recyclerViewDataAdapter;
    private final int collectionLayoutIdArray[]={R.layout.collection_item};
    public FragmentWatchShakeBinding binding2;
    public interface collectionDrawerIconListener{
        public void collectionDrawerIcon();
    }
    public CollectionFragment() {
        super(R.layout.fragment_my_collection);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       binding2=FragmentWatchShakeBinding.inflate(inflater,container,false);
         v=binding2.getRoot();

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "iconfont.ttf");
        binding2.drawerIcon.setTypeface(typeface);
        return v;
    }

    public static CollectionFragment newInstance(Context context){
        CollectionFragment collectionFragment=new CollectionFragment();
        mContext=context;
        return collectionFragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mySQLiteWebViewTextBussiness=new MySQLiteWebViewTextBussiness(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        getData(1);
      binding2.drawerIcon.setOnClickListener(this);
    }

    @Override
    public void getData(int page) {
        List<URLTableData> urlTableDataList=mySQLiteWebViewTextBussiness.queryAllFromTable();
        myList=new ArrayList<>();
       for(int i=0;i<urlTableDataList.size();i++){
           myList.add(urlTableDataList.get(i));
       }
        if(myList.size()==0){
            URLTableData urlTableData=new URLTableData(null,null,null,new Date());
            myList.add(urlTableData);
        }
        recyclerViewDataAdapter=new RecyclerViewDataAdapter(mContext,myList,collectionLayoutIdArray, BR.urlTableData);
        getMyRecyclerView().setAdapter(recyclerViewDataAdapter);
        recyclerViewDataAdapter.setRecyclerViewItemOnClickListener(new RecyclerViewDataAdapter.recyclerViewDataBindingItemOnClickListener() {
            @Override
            public void recyclerViewDataBindingItemOnClick(String url, String desc, String who, Date CreateAt, String type, int position, int clickPosition) {
                Intent intent = new Intent();
                intent.setClass(mContext, ShowWebViewActivity.class);
                View v = getMyRecyclerView().getChildAt(position);
                TextView textView = (TextView) v.findViewById(R.id.id_tv);
                int _id = Integer.valueOf(textView.getText().toString());
                URLTableData urlTableData = new URLTableData(url, who, desc, CreateAt);
                urlTableData.setType(type);
                urlTableData.set_id(_id);
                urlTableData.setIsCollected(true);
                Bundle bundle = new Bundle();
                bundle.putSerializable("urlTableData", urlTableData);
                intent.putExtras(bundle);
                if(url==null){
                    SnackBarUtils.makeShort(getActivity().getWindow().getDecorView(), getResources().getString(R.string.collection_no_item)).danger();
                }else {
                    startActivity(intent);
                }
            }
        });
        recyclerViewDataAdapter.notifyDataSetChanged();
    }

    @Override
    public void setSubscriber(int page,boolean isRefresh) {
        if(page>1) {
            SnackBarUtils.makeShort(getActivity().getWindow().getDecorView(), getResources().getString(R.string.collection_loadmore_footer)).danger();
        }else{
            SnackBarUtils.makeShort(getActivity().getWindow().getDecorView(), getResources().getString(R.string.collection_refresh_header)).danger();
            binding.swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        getMyRecyclerView().setLayoutManager(new LinearLayoutManager(mContext));
//        getMyRecyclerView().addItemDecoration(new MyDecoration(getActivity(), MyDecoration.VERTICAL_LIST));
        InitListener();
    }

    @Override
    public void onClick(View v) {
        if(getActivity() instanceof collectionDrawerIconListener){
            ((collectionDrawerIconListener) getActivity()).collectionDrawerIcon();
        }
    }
}
