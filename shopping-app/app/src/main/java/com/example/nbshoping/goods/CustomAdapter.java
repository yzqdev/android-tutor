package com.example.nbshoping.goods;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nbshoping.R;
import com.example.nbshoping.utils.URLUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/*TabLayout的一些样式
* searchactivity*/
public class CustomAdapter extends BaseAdapter {
    private Context context;
    List<GoodsBean.DataBean> data;



    public CustomAdapter(Context context,List<GoodsBean.DataBean> data){
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

    //每一条
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        VH vh=null;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_goodstype,parent,false);
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
            titleTv=v.findViewById(R.id.item_goodstype_tv_title);
            priceTv=v.findViewById(R.id.item_goodstype_price);
            desTv=v.findViewById(R.id.item_goodstype_tv_des);
            iv=v.findViewById(R.id.item_goodstype_iv);
        }
    }
}