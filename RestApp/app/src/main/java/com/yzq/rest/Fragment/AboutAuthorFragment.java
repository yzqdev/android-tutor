package com.yzq.rest.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.yzq.rest.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by JDK on 2016/9/9.
 */
public class AboutAuthorFragment extends Fragment implements View.OnClickListener,AppBarLayout.OnOffsetChangedListener{
    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.music_link_tv)
     TextView music_link_tv;
    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;
    private boolean mIsTheTitleVisible = false;
    private AppCompatActivity appCompatActivity;
    private static AboutAuthorFragment aboutAuthorFragment;
    private View v;

    public interface aboutAuthorDrawerIconListener{
        public void aboutAuthorDrawerIcon();
    }
    public static AboutAuthorFragment newInstance(){
        if( aboutAuthorFragment ==null){
            aboutAuthorFragment =new AboutAuthorFragment();
        }
        return aboutAuthorFragment;
    }
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            int maxScroll = appBarLayout.getTotalScrollRange();
            float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
            if (percentage >= 0.5f) {
                toolbar.setNavigationIcon(R.drawable.ic_navigation_30dp);
            }
            handleToolbarTitleVisibility(percentage);
        }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(v!=null){
            ButterKnife.bind(this,v);
            return v;
        }
         v=inflater.inflate(R.layout.fragment_about_author, container, false);
        ButterKnife.bind(this,v);
        appBarLayout.addOnOffsetChangedListener(this);
        initFabListener();
        initWidget();
        appCompatActivity=((AppCompatActivity) getActivity());
        appCompatActivity.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(this);
        return v;
    }
    // ?????????????????????
    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }
    // ??????ToolBar?????????
    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
            if (!mIsTheTitleVisible) {
                startAlphaAnimation(toolbar, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }
        } else {
            if (mIsTheTitleVisible) {
                startAlphaAnimation(toolbar, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }
    @Override
    public void onClick(View v) {
        if(getActivity() instanceof aboutAuthorDrawerIconListener){
            ((aboutAuthorDrawerIconListener) getActivity()).aboutAuthorDrawerIcon();
        }
    }
    public void initFabListener(){
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri=Uri.parse("https://github.com/sakurajiang");
                Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }

        });
    }
    public void initWidget(){
        String html=getResources().getString(R.string.plus_a_litter_content);
        music_link_tv.setText(Html.fromHtml(html));
//        music_link_tv.setText(getText(R.string.plus_a_litter_content));
        music_link_tv.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
