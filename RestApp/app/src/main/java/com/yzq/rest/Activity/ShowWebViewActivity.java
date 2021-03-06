package com.yzq.rest.Activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.yzq.rest.DataBase.MySQLiteWebViewTextBussiness;
import com.yzq.rest.model_data.entity.URLTableData;
import com.yzq.rest.R;
import com.yzq.rest.utils.ShareUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JDK on 2016/8/8.
 */
public class ShowWebViewActivity extends BaseActivity {
    @BindView(R.id.show_webview_wv)
    WebView showWebView;
    @BindView(R.id.count_tv)
    TextView count_tv;
    @BindView(R.id.interest_ib)
    ImageButton interest_ib;
    @BindView(R.id.show_webview_pb)
    ProgressBar show_pb;
    @BindView(R.id.webview_toolbar)
    Toolbar webview_toolbar;
    @BindView(R.id.desc_tv_showwebview)
    TextView desc_tv_showwebview;
    private Intent intent;
    private URLTableData urlTableData;
    private MySQLiteWebViewTextBussiness mySQLiteWebViewTextBussiness;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_webview);
        mySQLiteWebViewTextBussiness=new MySQLiteWebViewTextBussiness(this);
        intent=getIntent();
        urlTableData= (URLTableData) intent.getSerializableExtra("urlTableData");
        ButterKnife.bind(this);
        setSupportActionBar(webview_toolbar);
        webview_toolbar.setNavigationIcon(R.drawable.ic_back_24dp);
        setMyShowWebView(urlTableData.getUrl());
        setTextViewValue();
        initListener();
        initTitle();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (showWebView != null) {
            showWebView.setWebViewClient(null);
            showWebView.setWebChromeClient(null);
            showWebView.destroy();
            showWebView = null;
        }
    }
    @Override
    protected void onPause() {
        if (showWebView!=null) {
            showWebView.onPause();
            showWebView.pauseTimers();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if(showWebView!=null) {
            showWebView.resumeTimers();
            showWebView.onResume();
        }
        super.onResume();
    }

    public void setMyShowWebView(String url){
        WebSettings webSettings=showWebView.getSettings();
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setSupportZoom(true);
        if (Build.VERSION.SDK_INT >= 19) {
            showWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else {
            showWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        showWebView.setWebChromeClient(new Chrome());
        showWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        showWebView.loadUrl(url);
    }
    private class Chrome extends WebChromeClient
            implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer player) {
            if (player != null) {
                if (player.isPlaying()) player.stop();
                player.reset();
                player.release();
                player = null;
            }
        }
            @Override
            public void onProgressChanged (WebView view,int newProgress){
                if (newProgress == 100) {
                    show_pb.setVisibility(View.GONE);
                } else {
                    if (View.VISIBLE == show_pb.getVisibility()) {
                        show_pb.setVisibility(View.VISIBLE);
                    }
                    show_pb.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        }
    public void setTextViewValue(){
        int count=mySQLiteWebViewTextBussiness.getCount();
        count_tv.setText("??????????????????" + count);
    }
    public void initListener(){
        judgeURLTableData(urlTableData);
        interest_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (urlTableData.isCollected()) {
                    mySQLiteWebViewTextBussiness.deleteFromWebViewTable(urlTableData.get_id());
                    interest_ib.setBackgroundResource(R.mipmap.uncollected);
                    urlTableData.setIsCollected(false);
                } else {
                    mySQLiteWebViewTextBussiness.insertToWebViewURLTable(urlTableData);
                    interest_ib.setBackgroundResource(R.mipmap.collected);
                    urlTableData.setIsCollected(true);
                }
                setTextViewValue();
            }
        });
        webview_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showWebView.canGoBack()) {
                    showWebView.goBack();
                } else {
                    onBackPressed();
                }

            }
        });
    }
    public void judgeURLTableData(URLTableData urlTableData) {
        //???????????????urlTableData
        List<URLTableData> urlTableDataList = mySQLiteWebViewTextBussiness.queryAllFromTable();
        //??????????????????????????????????????????urlTableData????????????????????????IsCollected??????true
        if (urlTableDataList.size() > 0) {
            for (int i = 0; i < urlTableDataList.size(); i++) {
                if (urlTableData.getUrl().equals(urlTableDataList.get(i).getUrl())) {
                    urlTableData.setIsCollected(true);
                    //??????urlTableData???urlTableDataList.get(i)??????????????????????????????????????????????????????
                    urlTableData.set_id(urlTableDataList.get(i).get_id());
                }
            }
        }
        //????????????isCollected?????????????????????
        if(urlTableData.isCollected()){
            interest_ib.setBackgroundResource(R.mipmap.collected);
        }else {
            interest_ib.setBackgroundResource(R.mipmap.uncollected);
        }
    }
    public void initTitle(){
        desc_tv_showwebview.setText(urlTableData.getDesc().toString());

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.webview_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
         if (id == R.id.action_share) {
             ShareUtils.getInstance(this).share(urlTableData.getUrl(),"textvideo");
        }
        return super.onOptionsItemSelected(item);
    }
}
