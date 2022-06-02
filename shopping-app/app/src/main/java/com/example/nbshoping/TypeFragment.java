package com.example.nbshoping;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nbshoping.goods.GoodsTypeFragment;
import com.example.nbshoping.goods.SearchActivity;
import com.example.nbshoping.goods.TypeAdapter;
import com.example.nbshoping.goods.TypeBean;
import com.example.nbshoping.utils.BaseFragment;
import com.example.nbshoping.utils.URLUtils;
import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.List;

/*分类
 * */
public class TypeFragment extends BaseFragment {
    ListView typeLv;
    EditText searchEt;//todo
    List<TypeBean.DataBean> data;//数据源
    private TypeAdapter typeAdapter;
    private FragmentManager fm;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_type, container, false);
        initView(view);
        fm = getChildFragmentManager();

        //1.确定数据源
        data = new LinkedList<>();
        //2.设置适配器
        typeAdapter = new TypeAdapter(getContext(), data);
        typeLv.setAdapter(typeAdapter);
        //3.联网
        getNetword(URLUtils.queryAllCategory_url);
        //4.设置点击事件
        setListener();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            //隐藏时所作的事情
            searchEt.setText("");//搜索框恢复
        } else {
            //显示时所作的事情
        }
    }

    /*点击右侧item，切换右侧的页面*/
    private void changeRightPage(int position) {
        //得到切换的数据
        TypeBean.DataBean dataBean = data.get(position);
        int id = dataBean.getId();
        FragmentTransaction transaction = fm.beginTransaction();
        GoodsTypeFragment gtfrag = GoodsTypeFragment.newInstance(id + "", "");
        transaction.replace(R.id.frag_type_layout, gtfrag);
        transaction.commit();

    }

    /*给listview每一项设置点击事件*/
    private void setListener() {
        typeLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                typeAdapter.setClickPoos(position);//点击事件发生，改变被点击位置，adapter内部监听处理点击状态颜色事件
                changeRightPage(position);//点击切换商品页面， 切换右侧的页面

            }
        });
    }

    @Override
    public void onSuccess(String result) {
        super.onSuccess(result);
        //解析数据
        TypeBean typeBean = new Gson().fromJson(result, TypeBean.class);
        List<TypeBean.DataBean> list = typeBean.getData();//获取网络数据
        data.addAll(list);//将网络数据添加到listview的数据源中
        typeAdapter.notifyDataSetChanged();//提示适配器更新数据源
        changeRightPage(0);

    }

    private void initView(View view) {
        typeLv = view.findViewById(R.id.frag_type_listv);
        searchEt = view.findViewById(R.id.frag_type_sc_et);

        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    //点击搜索要做的操作
                    search();
                    return true;
                }
                return false;
            }
        });

    }
    //点击搜索要做的操作
    private void search() {
        Intent intent=new Intent(getActivity(), SearchActivity.class);
        intent.putExtra("goodsname",searchEt.getText().toString().trim());
        startActivity(intent);
        Toast.makeText(getContext(),"分类",Toast.LENGTH_SHORT).show();


    }

}