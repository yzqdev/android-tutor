package com.example.nbshoping.goods;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.nbshoping.R;
import com.example.nbshoping.utils.BaseFragment;
import com.example.nbshoping.utils.URLUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/*左侧商品分类*/
public class GoodsTypeFragment extends BaseFragment {

    //activity向fragment传值
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;//id
    private String mParam2;
    List<GoodsBean.DataBean> data;
    GoodsTypeAdapter goodsTypeAdapter;//适配器
    ListView goodsTypeLv;


    public GoodsTypeFragment() {
        // Required empty public constructor
    }


    public static GoodsTypeFragment newInstance(String param1, String param2) {
        GoodsTypeFragment fragment = new GoodsTypeFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_goodstype, container, false);
        //当前右侧商品信息fragmment中的list
        goodsTypeLv = view.findViewById(R.id.frag_goodstype_listv);//类型商品listview
        //数据源
        data=new ArrayList<>();
        //适配器
         goodsTypeAdapter = new GoodsTypeAdapter(getContext(), data);
        goodsTypeLv.setAdapter(goodsTypeAdapter);
        //联网
        getNetword(URLUtils.queryCommodityByCateId_url+mParam1);
        //设置点击事件
        setListener();

        return view;
    }
    /*给listview每一项设置点击事件*/
    private void setListener() {

        goodsTypeLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击事件发生，改变被点击位置position，adapter内部监听处理点击状态颜色事件
                Log.i("点右侧击返回id：" , String.valueOf(data.get(position).getId()));//商品id
                GoodsBean.DataBean dataBean=data.get(position);
                //打开对应商品详情界面
                Intent intent=new Intent(getContext(),GoodsDetailsActivity.class);
                intent.putExtra("goods",dataBean);
                startActivity(intent);

            }
        });

    }

    @Override
    public void onSuccess(String result) {
        super.onSuccess(result);
        GoodsBean goodsBean =new Gson().fromJson(result,GoodsBean.class);
        List<GoodsBean.DataBean> list=goodsBean.getData();
        data.addAll(list);
        goodsTypeAdapter.notifyDataSetChanged();
    }
}