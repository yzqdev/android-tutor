package com.yzq.rest.HttpUtils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.yzq.rest.Activity.ShowBigBitmapActivity;
import com.yzq.rest.Activity.ShowWebViewActivity;
import com.yzq.rest.Adapter.RecyclerViewDataAdapter;
import com.yzq.rest.BR;
import com.yzq.rest.Fragment.AndroidFragment;
import com.yzq.rest.Fragment.FrontFragment;
import com.yzq.rest.Fragment.MeiziFragment;
import com.yzq.rest.Fragment.ShakeMeiziFragment;
import com.yzq.rest.model_data.FrontData;
import com.yzq.rest.model_data.MeiziData;
import com.yzq.rest.model_data.MyAndroidData;
import com.yzq.rest.model_data.RestVideoData;
import com.yzq.rest.model_data.entity.URLTableData;
import com.yzq.rest.R;
import com.yzq.rest.utils.ChangeTimeFormat;
import com.yzq.rest.utils.SPDataUtil;
import com.yzq.rest.utils.SnackBarUtils;
import com.yzq.rest.utils.SplitUtils;

import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by JDK on 2016/10/8.
 */
public class RequestData {
    private RecyclerViewDataAdapter mRecyclerViewDataAdapter;
    private static Context mContext;
    private shProgressinterface mSHProgressinterface;
    private shProgressinterfaceofShake mShProgressinterfaceofShake;
    private final int meiziLayoutIdArray[] = {R.layout.meizi_item};
    private final int androidLayoutIdArray[] = {R.layout.android_item};
    private final int frontLayoutIdArray[] = {R.layout.front_item};

    public interface shProgressinterface {
        void showProgress();

        void hideProgress();
    }

    public interface shProgressinterfaceofShake {
    }

    public void setSHProgressinterface(shProgressinterface shProgressinterface) {
        this.mSHProgressinterface = shProgressinterface;
    }

    public void setSHProgressinterfaceofShake(shProgressinterfaceofShake shProgressinterfaceofShake) {
        this.mShProgressinterfaceofShake = shProgressinterfaceofShake;
    }

    public static class StaticInsideRequestData {
        private static final RequestData requestData = new RequestData();
    }

    public static RequestData getInstance(Context context) {
        mContext = context;
        return StaticInsideRequestData.requestData;
    }

    public void requestMeiziData(final Observable o1, final Observable o2, final RecyclerView mRecyclerView, final int page, final List meiziList, final boolean isFirst, final boolean isShake, boolean isCache) {
        if (mSHProgressinterface != null && mSHProgressinterface instanceof MeiziFragment) {
            mSHProgressinterface.showProgress();
        }
        if (isCache) {
            mRecyclerViewDataAdapter = new RecyclerViewDataAdapter(mContext, meiziList, meiziLayoutIdArray, BR.video);
            mRecyclerView.setAdapter(mRecyclerViewDataAdapter);
            if ((mSHProgressinterface != null && mSHProgressinterface instanceof MeiziFragment))
                mSHProgressinterface.hideProgress();
        } else {
            Observable.zip(o1, o2, (Func2<MeiziData, RestVideoData, RestVideoData>) (meiziData, restVideoData) -> combineMeiziDataWithRestVideoData(meiziData, restVideoData)).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<RestVideoData>() {
                        @Override
                        public void onCompleted() {
                            if ((mSHProgressinterface != null && mSHProgressinterface instanceof MeiziFragment))
                                mSHProgressinterface.hideProgress();
                            if (mShProgressinterfaceofShake != null && mShProgressinterfaceofShake instanceof ShakeMeiziFragment)
                                ((ShakeMeiziFragment) mShProgressinterfaceofShake).hideProgress();
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (mSHProgressinterface != null && mSHProgressinterface instanceof MeiziFragment)
                                mSHProgressinterface.hideProgress();
                            if (mShProgressinterfaceofShake != null && mShProgressinterfaceofShake instanceof ShakeMeiziFragment)
                                ((ShakeMeiziFragment) mShProgressinterfaceofShake).hideProgress();
                            SnackBarUtils.makeLong(((Activity) mContext).getWindow().getDecorView(), mContext.getResources().getString(R.string.network_error)).danger();
                        }

                        @Override
                        public void onNext(RestVideoData restVideoData) {
                            if (meiziList != null)
                                meiziList.addAll(restVideoData.results);
                            if (isShake) {
                                setListWise(restVideoData.results, 2);
                                mRecyclerViewDataAdapter = new RecyclerViewDataAdapter(mContext, restVideoData.results, meiziLayoutIdArray, BR.video);
                                mRecyclerView.setAdapter(mRecyclerViewDataAdapter);
                            }
                            if (isFirst && !isShake) {
                                SPDataUtil.saveFirstPageGirls(mContext, meiziList);
                                mRecyclerViewDataAdapter = new RecyclerViewDataAdapter(mContext, meiziList, meiziLayoutIdArray, BR.video);
                                mRecyclerView.setAdapter(mRecyclerViewDataAdapter);
                            }
                            initListener(mRecyclerViewDataAdapter);

                        }
                    });
        }
        if (mRecyclerViewDataAdapter != null) {
            initListener(mRecyclerViewDataAdapter);
        }

    }

    public void requestAndroidData(final Observable o1, final RecyclerView mRecyclerView, final int page, final List androidList, final boolean isFirst, final boolean isShake, boolean isCache) {
        if (mSHProgressinterface != null && mSHProgressinterface instanceof AndroidFragment) {
            mSHProgressinterface.showProgress();
        }
        if (isCache) {
            mRecyclerViewDataAdapter = new RecyclerViewDataAdapter(mContext, androidList, androidLayoutIdArray, BR.myAndroid);
            mRecyclerView.setAdapter(mRecyclerViewDataAdapter);
            if ((mSHProgressinterface != null && mSHProgressinterface instanceof AndroidFragment))
                mSHProgressinterface.hideProgress();
        } else {
            o1
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<MyAndroidData>() {
                        @Override
                        public void onCompleted() {
                            if (mSHProgressinterface != null && mSHProgressinterface instanceof AndroidFragment)
                                mSHProgressinterface.hideProgress();
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (mSHProgressinterface != null && mSHProgressinterface instanceof AndroidFragment)
                                mSHProgressinterface.hideProgress();
                            SnackBarUtils.makeLong(((Activity) mContext).getWindow().getDecorView(), mContext.getResources().getString(R.string.network_error)).danger();
                        }

                        @Override
                        public void onNext(MyAndroidData myAndroidData) {
                            if (androidList != null)
                                androidList.addAll(myAndroidData.results);
                            //在这里设置这个标志位是为了加载后是直接在尾部加载数据，不会造成加载后整个数据又滑到了最开始。
                            if (isShake) {
                                mRecyclerViewDataAdapter = new RecyclerViewDataAdapter(mContext, myAndroidData.results, androidLayoutIdArray, BR.myAndroid);
                                mRecyclerView.setAdapter(mRecyclerViewDataAdapter);
                            }
                            if (isFirst && !isShake) {
                                SPDataUtil.saveFirstPageGirls(mContext, androidList);
                                mRecyclerViewDataAdapter = new RecyclerViewDataAdapter(mContext, androidList, androidLayoutIdArray, BR.myAndroid);
                                mRecyclerView.setAdapter(mRecyclerViewDataAdapter);
                            }
                            initListener(mRecyclerViewDataAdapter);
                        }
                    });
        }
        if (mRecyclerViewDataAdapter != null) {
            initListener(mRecyclerViewDataAdapter);
        }

    }

    public void requestFrontData(final Observable o1, final RecyclerView mRecyclerView, final int page, final List frontList, final boolean isFirst, final boolean isShake, boolean isCache) {
        if (mSHProgressinterface != null && mSHProgressinterface instanceof FrontFragment) {
            mSHProgressinterface.showProgress();
        }
        if (isCache) {
            mRecyclerViewDataAdapter = new RecyclerViewDataAdapter(mContext, frontList, frontLayoutIdArray, BR.front);
            mRecyclerView.setAdapter(mRecyclerViewDataAdapter);
            if ((mSHProgressinterface != null && mSHProgressinterface instanceof FrontFragment))
                mSHProgressinterface.hideProgress();
        } else {
            o1
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<FrontData>() {
                        @Override
                        public void onCompleted() {
                            if (mSHProgressinterface != null && mSHProgressinterface instanceof FrontFragment)
                                mSHProgressinterface.hideProgress();
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (mSHProgressinterface != null && mSHProgressinterface instanceof FrontFragment)
                                mSHProgressinterface.hideProgress();
                        }

                        @Override
                        public void onNext(FrontData frontData) {
                            if (frontList != null)
                                frontList.addAll(frontData.results);
                            if (isShake) {
                                mRecyclerViewDataAdapter = new RecyclerViewDataAdapter(mContext, frontData.results, frontLayoutIdArray, BR.front);
                                mRecyclerView.setAdapter(mRecyclerViewDataAdapter);
                            }
                            if (isFirst && !isShake) {
                                SPDataUtil.saveFirstPageGirls(mContext, frontList);
                                mRecyclerViewDataAdapter = new RecyclerViewDataAdapter(mContext, frontList, frontLayoutIdArray, BR.front);
                                mRecyclerView.setAdapter(mRecyclerViewDataAdapter);
                            }
                            initListener(mRecyclerViewDataAdapter);
                        }
                    });
        }
        if (mRecyclerViewDataAdapter != null) {
            initListener(mRecyclerViewDataAdapter);
        }
    }

    /**
     * 使用zip将Video和Meizi数据放在一起
     *
     * @param meiziData
     * @param restVideoData
     * @return
     */
    public RestVideoData combineMeiziDataWithRestVideoData(MeiziData meiziData, RestVideoData restVideoData) {
        int size = Math.min(meiziData.results.size(), restVideoData.results.size());
        for (int i = 0; i < size; i++) {
            restVideoData.results.get(i).desc = restVideoData.results.get(i).desc + ",./" + meiziData.results.get(i).desc;
            restVideoData.results.get(i).url = restVideoData.results.get(i).url + ",./" + meiziData.results.get(i).url;
            restVideoData.results.get(i).publishedAt = meiziData.results.get(i).publishedAt;
        }
        restVideoData.results.size();
        return restVideoData;
    }

    /**
     * 设置list的规格,若有超出，则从头开始抹去，直到满足条件的size
     *
     * @param listWise
     * @param size
     */
    public void setListWise(List listWise, int size) {
        if (listWise.size() > size) {
            for (int i = 0; i < listWise.size() - size; i++) {
                listWise.remove(i);
            }
        }
    }

    public void initListener(RecyclerViewDataAdapter mRecyclerViewDataAdapter) {
        mRecyclerViewDataAdapter.notifyDataSetChanged();
        mRecyclerViewDataAdapter.setRecyclerViewItemOnClickListener(new RecyclerViewDataAdapter.recyclerViewDataBindingItemOnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void recyclerViewDataBindingItemOnClick(String url, String desc, String who, Date PublishedAt, String type, int position, int clickPosition) {
                if (clickPosition == 0) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, ShowWebViewActivity.class);
                    URLTableData urlTableData = new URLTableData(SplitUtils.splitWithComma(url)[0], who, SplitUtils.splitWithComma(desc)[0], PublishedAt);
                    urlTableData.setType(type);
                    urlTableData.setIsCollected(false);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("urlTableData", urlTableData);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(mContext, ShowBigBitmapActivity.class);
                    intent.putExtra("URL", SplitUtils.splitWithComma(url)[1]);
                    intent.putExtra("DESC", SplitUtils.splitWithComma(desc)[1]);
                    intent.putExtra("PUBLISHEDAT", ChangeTimeFormat.changeToYearMonthDay(PublishedAt));
                    ImageView imageView = (ImageView) LayoutInflater.from(mContext).inflate(R.layout.meizi_item, null).findViewById(R.id.meizi_imageView);
                    mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(((Activity) mContext), imageView, mContext.getString(R.string.jump_meizi)).toBundle());
                }
            }
        });

    }
}
