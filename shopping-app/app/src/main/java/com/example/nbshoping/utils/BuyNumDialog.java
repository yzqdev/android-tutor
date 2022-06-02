package com.example.nbshoping.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.nbshoping.R;
import com.example.nbshoping.goods.GoodsBean;

/*
 * 自定义Dialog:添加到购物车的对话框
 * */
public class BuyNumDialog extends Dialog  {
    ImageView closeIv;//关闭
    AmountView amountView;//加减数量
    Button ensureTv;//确定


    public BuyNumDialog(@NonNull Context context) {//单价
        super(context);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_buynum_addcar);//绑定布局

        closeIv = findViewById(R.id.addcar_close_iv);
        amountView=findViewById(R.id.addcar_buynum_amount);
        ensureTv = findViewById(R.id.addcar_ensure);

        closeIv.setOnClickListener(onClickListener);
        ensureTv.setOnClickListener(onClickListener);
    }
    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.addcar_close_iv:
                        cancel();//取消
                    break;
                case R.id.addcar_ensure:
                    if (onEnsureListener!=null)
                    {
                        int etNum = amountView.getEtNum();//获取数量编辑框的内容
                        onEnsureListener.onEnsure(etNum);//点击确定，将数据传出去
                    }

                    break;
            }
        }
    };


    /*
    * 接口回调
    * */
    //创建被点击确定执行的新的接口
    public interface OnEnsureListener {
        public void onEnsure(int num);   //填进去的数据，购买数量
    }
    OnEnsureListener onEnsureListener;    //声明接口的变量
    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;//设置接口对象
    }


    /*底部弹出*/
    //设置对话框宽度和屏幕宽度一致
    public void setDialogWidth() {
        Window window = getWindow();   //当前屏幕窗口对象
        WindowManager.LayoutParams wlp = window.getAttributes();  //获取窗口信息参数
        //获取屏幕宽度
        Display d = window.getWindowManager().getDefaultDisplay();
        wlp.width = (int) (d.getWidth());   //对话框窗口宽度为屏幕窗口宽度
        wlp.gravity = Gravity.BOTTOM;    //从底部弹出对话框
        window.setBackgroundDrawableResource(android.R.color.transparent);   //设置窗口背景透明
        window.setAttributes(wlp);
        //自动弹出软键盘
        handler.sendEmptyMessageDelayed(1, 100);
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            InputMethodManager manager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    };



    public void setStrorage(int num,double price)
    {
        amountView.setGoods_storage(num, price);
    }

}
