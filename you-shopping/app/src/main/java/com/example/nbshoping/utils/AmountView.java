package com.example.nbshoping.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nbshoping.R;

/**
 * 自定义组件：购买数量，带减少增加按钮
 * Created by hiwhitley on 2016/7/4.
 */
public class AmountView extends LinearLayout implements View.OnClickListener, TextWatcher {

    private static final String TAG = "AmountView";
    private int amount = 1; //购买数量
    private int goods_storage = 999; //商品库存
    double price;//商品单价

    private OnAmountChangeListener mListener;

    private EditText etAmount;
    private Button btnDecrease;
    private Button btnIncrease;
    private TextView countTv,moneyTv;


    public AmountView(Context context) {
        this(context, null);
    }

    public AmountView(Context context, AttributeSet attrs) {
        super(context, attrs);

//        LayoutInflater.from(context).inflate(R.layout.dialog_buynum_addcar, this);//fixme view_amount，获取整个对话框界面
        LayoutInflater.from(context).inflate(R.layout.view_amount, this);//fixme view_amount，获取整个对话框界面


        etAmount = (EditText) findViewById(R.id.etAmount);
        btnDecrease = (Button) findViewById(R.id.btnDecrease);
        btnIncrease = (Button) findViewById(R.id.btnIncrease);

        btnDecrease.setOnClickListener(this);
        btnIncrease.setOnClickListener(this);
        etAmount.addTextChangedListener(this);

        countTv=findViewById(R.id.addcar_count_tv);
        moneyTv=findViewById(R.id.addcar_money_tv);
        moneyTv.setText("￥"+String.valueOf(price*amount));//价格展示
        countTv.setText("已选"+String.valueOf(amount) + "件");


        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.AmountView);
        int btnWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView_btnWidth, LayoutParams.WRAP_CONTENT);
        int tvWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView_tvWidth, 80);
        int tvTextSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView_tvTextSize, 0);
        int btnTextSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView_btnTextSize, 0);
        obtainStyledAttributes.recycle();

        LayoutParams btnParams = new LayoutParams(btnWidth, LayoutParams.MATCH_PARENT);
        btnDecrease.setLayoutParams(btnParams);
        btnIncrease.setLayoutParams(btnParams);
        if (btnTextSize != 0) {
            btnDecrease.setTextSize(TypedValue.COMPLEX_UNIT_PX, btnTextSize);
            btnIncrease.setTextSize(TypedValue.COMPLEX_UNIT_PX, btnTextSize);
        }

        LayoutParams textParams = new LayoutParams(tvWidth, LayoutParams.MATCH_PARENT);
        etAmount.setLayoutParams(textParams);
        if (tvTextSize != 0) {
            etAmount.setTextSize(tvTextSize);
        }
    }

    public void setOnAmountChangeListener(OnAmountChangeListener onAmountChangeListener) {
        this.mListener = onAmountChangeListener;
    }

    public void setGoods_storage(int goods_storage,double price) {
        this.goods_storage = goods_storage;
        this.price=price;
        moneyTv.setText("￥"+String.valueOf(price*amount));//价格展示
        countTv.setText("已选"+String.valueOf(amount) + "件");
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnDecrease) {
            if (amount > 1) {
                amount--;
                moneyTv.setText("￥"+String.valueOf(price*amount));//价格展示
                countTv.setText("已选"+String.valueOf(amount) + "件");
                etAmount.setText(amount + "");
                etAmount.setSelection(String.valueOf(amount).length() );

            }
        } else if (i == R.id.btnIncrease) {
            if (amount < goods_storage) {
                amount++;
                moneyTv.setText("￥"+String.valueOf(price*amount));
                countTv.setText("已选"+String.valueOf(amount) + "件");
                etAmount.setText(amount + "");
                etAmount.setSelection(String.valueOf(amount).length() );
            }
        }

        etAmount.clearFocus();

        if (mListener != null) {
            mListener.onAmountChange(this, amount);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    //同样的设置EditText的数量改变监听
    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().isEmpty())
            return;
        amount = Integer.valueOf(s.toString());
        if (amount > goods_storage) {
            etAmount.setText(goods_storage + "");
            moneyTv.setText("￥"+String.valueOf(price*goods_storage));
            countTv.setText("已选"+String.valueOf(goods_storage) + "件");
            etAmount.setSelection(String.valueOf(goods_storage).length() );

            return;
        }

        if (mListener != null) {
            mListener.onAmountChange(this, amount);
        }
    }


    public interface OnAmountChangeListener {
        void onAmountChange(View view, int amount);
    }

    /*显示在edittext当中的数量*/
    public  int getEtNum(){
        String s = etAmount.getText().toString().toString();
        return Integer.parseInt(s);

    }
}
