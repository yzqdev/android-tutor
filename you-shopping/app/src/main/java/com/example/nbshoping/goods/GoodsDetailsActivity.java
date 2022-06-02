package com.example.nbshoping.goods;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nbshoping.R;
import com.example.nbshoping.login.UserBean;
import com.example.nbshoping.utils.BaseActivity;
import com.example.nbshoping.utils.BuyNumDialog;
import com.example.nbshoping.utils.SaveUserUtils;
import com.example.nbshoping.utils.URLUtils;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*展示商品详情*/
public class GoodsDetailsActivity extends BaseActivity {
    GoodsBean.DataBean dataBean;//商品信息
    ImageView goodsIv, back;
    TextView goodsNameTv, goodsCountTv, goodsPriceTv, goodsDetailinfpTv;
    TextView share, addcar, buy;
    LinearLayout store, customer, buycar;

    private UserBean.DataBean userInfo=null;//用户信息
    boolean isLogin=false;

    //猜你喜欢数据
    List<GoodsBean.DataBean> data;
    private LikeAdapter likeAdapter;//
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);
        dataBean = (GoodsBean.DataBean) getIntent().getSerializableExtra("goods");
        initView();
        isLogin=isLogin();

        showInfoInit(dataBean);//联网，获取商品详细信息,初始界面商品信息


    }


    private void showInfoInit(GoodsBean.DataBean dataBean) {
        //展示商品信息
        Log.i("货物", String.valueOf(dataBean));
        goodsNameTv.setText(dataBean.getName());
        goodsCountTv.setText("库存量:" + String.valueOf(dataBean.getCount()) + "件");
        goodsPriceTv.setText("￥" + String.valueOf(dataBean.getPrice()) + "元");
        goodsDetailinfpTv.setText(dataBean.getInfo());
        String photo = dataBean.getPhoto();//设置网络图片,先获取路径
        if (!TextUtils.isEmpty(photo)) {
            String photourl = URLUtils.PUBLIC_URL + photo;
            Picasso.with(getApplicationContext())
                    .load(photourl)
                    .fit()
                    .centerCrop()
                    .into(goodsIv);//加载图片到视图

        }

        //展示推荐数据,猜你喜欢
        data = new ArrayList<>();
        //适配器
        likeAdapter = new LikeAdapter(this, data);
        gridView.setAdapter(likeAdapter);
        //联网
        getNetword(URLUtils.guessYouLike_url);
        //设置点击事件
        setListener();

    }

    /*给girdview每一项设置点击事件*/
    private void setListener() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击事件发生，改变被点击位置position，adapter内部监听处理点击状态颜色事件
                GoodsBean.DataBean dataBean=data.get(position);
                //打开对应商品详情界面
                Intent intent=new Intent(GoodsDetailsActivity.this,GoodsDetailsActivity.class);
                intent.putExtra("goods",dataBean);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "别点了，干赶紧加入购物车吧！" , Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {

        goodsIv = findViewById(R.id.detail_goods_img_iv);//内容
        goodsNameTv = findViewById(R.id.detail_goods_name_tv);
        goodsCountTv = findViewById(R.id.detail_goods_count_tv);
        goodsPriceTv = findViewById(R.id.detail_goods_price_tv);
        goodsDetailinfpTv = findViewById(R.id.detail_goodsinfo_tv);

        back = findViewById(R.id.details_iv_back);//点击
        store = findViewById(R.id.detail_store);
        share = findViewById(R.id.details_tv_share);
        addcar = findViewById(R.id.detail_addcar);
        buy = findViewById(R.id.detail_buy);
        customer = findViewById(R.id.detail_customer);
        buycar = findViewById(R.id.detail_shopingcar);
        gridView = findViewById(R.id.detail_girdview);


        back.setOnClickListener(onClickListener);
        store.setOnClickListener(onClickListener);
        share.setOnClickListener(onClickListener);
        addcar.setOnClickListener(onClickListener);
        buy.setOnClickListener(onClickListener);
        customer.setOnClickListener(onClickListener);
        buycar.setOnClickListener(onClickListener);


    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.details_iv_back:
                    finish();
                    break;
                case R.id.detail_buy:
                    String smg="购买之前之前请先登录！";
                    showBuyDialog(smg,1);
                    break;
                case R.id.detail_addcar:
                    String msg="添加购物车之前请先登录！";
                    showBuyDialog(msg,2);
                    break;
                case R.id.details_tv_share:
                    sharefun(dataBean);

                    break;
                default:
                    Toast.makeText(getApplicationContext(), "该功能正在完善中！", Toast.LENGTH_SHORT).show();
                    break;


            }
        }
    };

    //显示购买数量对话框函数
    private void showBuyDialog(String msg,int flag) {
        if (isLogin ) {
            //弹出对话框
            BuyNumDialog dialog =new BuyNumDialog(GoodsDetailsActivity.this);
            dialog.show();
            dialog.setDialogWidth();
            dialog.setStrorage(dataBean.getCount(),dataBean.getPrice());//向对话框传入数据库存数,单价
            dialog.setOnEnsureListener(new BuyNumDialog.OnEnsureListener() {
                @Override
                public void onEnsure(int num) {
                    if (flag==1)//点击购物车弹出
                    {
                        // 跳转到支付，（传值（数量，商品id），支付界面直接获取用户id）
                        Intent intent=new Intent(getApplicationContext(),BoughtActivity.class);
                        intent.putExtra("count",num);
                        intent.putExtra("commodityId",dataBean.getId());
                        intent.putExtra("price",dataBean.getPrice());
                        startActivity(intent);

                    }
                    else if (flag ==2) {//点击购物车弹出
                        Map<String,String>map=new HashMap<>();
                        map.put("userId",userInfo.getId()+"");
                        map.put("commodityId",dataBean.getId()+"");
                        map.put("count",num+"");
                        postNetwork(URLUtils.insertShoppingCar_url,map);
                    }
                    dialog.cancel();

                }
            });
        }
        else
        {
            Toast.makeText(getApplicationContext(), msg,Toast.LENGTH_SHORT).show();
        }
    }

    //登录判断
    private boolean isLogin(){
        userInfo= SaveUserUtils.getUserInfo(this);//用户信息
        if (dataBean == null) {
            return false;
        }
        return true;
    }

    //分享功能
    private void sharefun(GoodsBean.DataBean dataBean) {

        //由文件得到uri
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        String mes=dataBean.getName()+":"+dataBean.getInfo()+"\n￥:"+String.valueOf(dataBean.getPrice())+"元";
        Log.i("shareMes",mes);
        // 指定发送内容的类型 (MIME type)
        sendIntent.putExtra(Intent.EXTRA_TEXT, mes);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share to..."));

    }

//    public Bitmap GetImageInputStream(String imageurl) {
//        URL url;
//        HttpURLConnection connection = null;
//        Bitmap bitmap = null;
//        try {
//            url = new URL(imageurl);
//            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(6000); //超时设置
//            connection.setDoInput(true);
//            connection.setUseCaches(false); //设置不使用缓存
//            InputStream inputStream = connection.getInputStream();
//            bitmap = BitmapFactory.decodeStream(inputStream);
//            inputStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return bitmap;
//    }

    @Override
    public void onSuccess(String result) {
        super.onSuccess(result);
        GoodsBean bean = new Gson().fromJson(result, GoodsBean.class);
        List<GoodsBean.DataBean> list = bean.getData();//id查找商品信息数据
        switch (bean.getCode()) {
            case 200:
               //连接两次网络，使用同一个onsuccess
                if (bean.getData()==null)
                {
                    Toast.makeText(getApplicationContext(),"添加购物车成功",Toast.LENGTH_SHORT).show();
                }else {
                    /*猜你喜欢*/
                    data.addAll(list);//联网成功，返回数据，存入内存，加载到适配器
                    likeAdapter.notifyDataSetChanged();//通知数据改变了
                }
                break;


            case 201: //
                Toast.makeText(getApplicationContext(), "like查询失败！", Toast.LENGTH_SHORT).show();
                break;
            default: //
                Toast.makeText(getApplicationContext(), "like参数错误！", Toast.LENGTH_SHORT).show();
                break;
        }


    }


}