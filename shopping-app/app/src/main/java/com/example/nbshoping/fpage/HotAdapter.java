package com.example.nbshoping.fpage;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nbshoping.R;
import com.example.nbshoping.goods.GoodsBean;
import com.example.nbshoping.utils.URLUtils;
import com.squareup.picasso.Picasso;

import java.util.List;


/*分类中的商品 适配器
 *编写listview和dridview适配器
 * 继承BaseAdapter
 * 重写4个方法
 * 定义两个变量：context，集合
 * 创建构造方法，变量赋值
 * 前三个方法按套路写，最后一个
 * 创建内部内，item中控件，构造方法传入View，findvi控件
 * */
public class HotAdapter extends BaseAdapter {
    Context context;
    List<GoodsBean.DataBean> data;

    public HotAdapter(Context context, List<GoodsBean.DataBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VH vh=null;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_hot,parent,false);
            vh=new VH(convertView);
            convertView.setTag(vh);

        }else {
            vh=(VH) convertView.getTag();
        }
        GoodsBean.DataBean dataBean=data.get(position);
        vh.titleTv.setText(dataBean.getName());
        vh.desTv.setText(dataBean.getInfo());
        vh.priceTv.setText("￥"+dataBean.getPrice()+"元");
        String photo=dataBean.getPhoto();//设置网络图片,先获取路径
        if (!TextUtils.isEmpty(photo)) {
            String photourl= URLUtils.PUBLIC_URL+photo;
            Picasso.with(context).load(photourl).into(vh.iv);//加载图片到视图

        }


        return convertView;
    }

    class VH{
        TextView titleTv,priceTv,desTv;
        ImageView iv;
        public VH(View v)
        {
            titleTv=v.findViewById(R.id.item_hot_tv_title);
            priceTv=v.findViewById(R.id.item_hot_price);
            desTv=v.findViewById(R.id.item_hot_tv_des);
            iv=v.findViewById(R.id.item_hot_iv);
        }
    }
}
