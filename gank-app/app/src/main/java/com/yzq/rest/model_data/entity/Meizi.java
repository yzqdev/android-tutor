package com.yzq.rest.model_data.entity;

import android.graphics.Color;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.yzq.rest.R;
import com.squareup.picasso.Picasso;

import java.util.Date;

/**
 * Created by JDK on 2016/8/2.
 */
public class Meizi extends Base{
    public String type;//干货类型，如Android，iOS，福利等
    public String url;//链接地址
    public String who;//作者
    public String desc;//干货内容的描述
    public boolean isCollected;
    public Date createdAt;
    public Date updatedAt;
    public Date publishedAt;
    @BindingAdapter("imageURL")
    public static void loadImage(ImageView imageView,String url){
        imageView.setBackgroundColor(Color.argb(204,161,136,127));
        Picasso.get( ).load(url).error(R.mipmap.error).resize(1000, 1000).centerCrop().into(imageView);
    }
}
