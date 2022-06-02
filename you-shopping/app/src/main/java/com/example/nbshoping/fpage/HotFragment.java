package com.example.nbshoping.fpage;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.nbshoping.R;
import com.example.nbshoping.goods.GoodsBean;
import com.example.nbshoping.goods.GoodsDetailsActivity;
import com.example.nbshoping.utils.BaseFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class HotFragment extends BaseFragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    GridView gv;
    List<GoodsBean.DataBean> data;//数据源
    private HotAdapter adapter;

    public HotFragment() {
        // Required empty public constructor
    }


    public static HotFragment newInstance(String param1, String param2) {
        HotFragment fragment = new HotFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot, container, false);
        gv=view.findViewById(R.id.grag_hot_gv);
        //数据源
        data=new ArrayList<>();
        //适配器
        adapter=new HotAdapter(getContext(),data);
        gv.setAdapter(adapter);
        //联网
        getNetword(mParam1);
        //设置监听
        gv.setOnItemClickListener(listener);

        return view;
    }
    AdapterView.OnItemClickListener listener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent=new Intent(getContext(), GoodsDetailsActivity.class);
            intent.putExtra("goods",data.get(position));
            startActivity(intent);
        }
    };

    @Override
    public void onSuccess(String result) {
        super.onSuccess(result);
        GoodsBean goodsBean=new Gson().fromJson(result,GoodsBean.class);
        List<GoodsBean.DataBean> list=goodsBean.getData();
        data.addAll(list);
        adapter.notifyDataSetChanged();
    }
}