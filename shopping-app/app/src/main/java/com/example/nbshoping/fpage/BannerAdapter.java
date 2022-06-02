package com.example.nbshoping.fpage;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;


//
public class BannerAdapter extends PagerAdapter {
    List<ImageView> views;

    public BannerAdapter(List<ImageView> views) {
        this.views = views;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject( View view,  Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem( ViewGroup container, int position) {
        ImageView view=views.get(position);
        container.addView(view);
        return view;



    }

    @Override
    public void destroyItem( ViewGroup container, int position,  Object object) {
        ImageView view=views.get(position);
        container.removeView(view);

    }
}
