package com.example.nbshoping.goods;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.nbshoping.R;

import java.util.List;

/*分类适配器
 *编写listview和dridview适配器
 * 继承BaseAdapter
 * 重写4个方法
 * 定义两个变量：context，集合
 * 创建构造方法，变量赋值
 * 前三个方法按套路写，最后一个
 * 创建内部内，item中控件，构造方法传入View，findvi控件
 * */
public class TypeAdapter extends BaseAdapter {
    Context context;
    List<TypeBean.DataBean> data;//数据源,放到每一个item
    int clickPos = 0;

    public TypeAdapter(Context context, List<TypeBean.DataBean> data) {
        this.context = context;
        this.data = data;
    }


    //设置点击的位置
    public void setClickPoos(int clickPos) {
        this.clickPos = clickPos;
//        重新绘制页面
        notifyDataSetChanged();
    }


    //显示数据长度，集合长度
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    //返回指定位置viewid
    @Override
    public long getItemId(int position) {

        return position;
    }

    //返回每个item的view对象
    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VH vh = null;
        if (convertView == null) {
            //convertView不能复用，创建view
            convertView = LayoutInflater.from(context).inflate(R.layout.item_type, parent, false);
            vh = new VH(convertView);
            convertView.setTag(vh);
        } else {
            vh=(VH) convertView.getTag();
        }
        TypeBean.DataBean dataBean = data.get(position);
        vh.type.setText(dataBean.getName());

        //点击背景颜色，文字颜色，
        if (position == clickPos) {
            convertView.setBackgroundResource(R.color.white);
            vh.type.setTextColor(R.color.black);
        } else {
            convertView.setBackgroundResource(R.color.whitesmoke);
            vh.type.setTextColor(R.color.black);
        }

        return convertView;
    }

    class VH {
        TextView type, line;

        public VH(View v) {
            this.type = v.findViewById(R.id.item_type_tv);
            this.line = v.findViewById(R.id.item_type_line);
        }
    }
}
