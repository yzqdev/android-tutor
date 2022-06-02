package com.yzq.geekweather.activity;

import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.yzq.geekweather.R;
import com.google.gson.Gson;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.yzq.geekweather.activity.SelectCity;
import com.yzq.geekweather.bean.Bean;
import com.yzq.geekweather.bean.City;

/*
 *  极客天气 v1.1
 *
 *  更新：
 *  API接口替换：从百度API Store换成了和风天气，更快、数据更准确
 *  改进一些细节：沉浸式标题栏处理；背景固定
 */

public class MainActivity extends Activity implements OnClickListener {

	private TextView selectCityName, publishTime, temperature;
	private TextView weatherStatus, wind, wind_1, pm, pm_1;
	private TextView gm, cy, yd;
	private TextView one_type, one_warm;
	private TextView two_type, two_warm;
	private TextView three_type, three_warm;
	private ImageButton selectCity, location, update, more;
	private ImageView main_pic, one_pic, two_pic, three_pic;

	private LinearLayout background;

	private ProgressDialog updateDialog, searchDialog, locationDialog;
	private String cityValue;

	Gson gson;

	// 百度地图定位相关参数
	public LocationClient mLocationClient = null;
	// 侧滑栏相关
	private DrawerLayout drawerlayout;
	// 监听网络
	private ConnectionChangeReceiver myReceiver;
	// 再按一次退出的提示
	private long exitTime = 0;

	// Volley准备
	RequestQueue mQueue;
	// 天气接口URL
	String url = "https://free-api.heweather.com/v5/weather?city=";
	// TODO 和风天气 个人开发者key (去官网注册会生成，这个请你记得替换成自己的)
	String key = "0e235183d43544afb0999ed6ca72d412";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mQueue = Volley.newRequestQueue(MainActivity.this);
		setContentView(R.layout.activity_main);
		initTop();
		initView();
		// 先判断是否有网络
		haveWifi();

	}

	// 如果MainActivity的launchMode是singleTask则只能在这里拿到B页面回传过来的值
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		cityValue = intent.getStringExtra("selectCity");
		if (cityValue == null) {
			// 每次按home回到主页后，再点击会执行这个方法
		} else {
			searchDialog = new ProgressDialog(this);
			searchDialog.setMessage("正在为您查询天气中...");
			searchDialog.show();
			getCity(cityValue);
		}

	}

	// 初始化所有控件
	private void initView() {
		selectCity = (ImageButton) findViewById(R.id.select_city);
		more = (ImageButton) findViewById(R.id.more);
		update = (ImageButton) findViewById(R.id.update);
		location = (ImageButton) findViewById(R.id.location);
		selectCityName = (TextView) findViewById(R.id.selectCityName);
		publishTime = (TextView) findViewById(R.id.publishTime);
		temperature = (TextView) findViewById(R.id.temperature);
		// 调用了一下自定义字体，显示温度、时间
		Typeface typeface = Typeface.createFromAsset(getAssets(),
				"fonnts/Gadugi.ttf");
		temperature.setTypeface(typeface);
		publishTime.setTypeface(typeface);

		weatherStatus = (TextView) findViewById(R.id.weatherStatus);
		wind = (TextView) findViewById(R.id.wind);
		wind_1 = (TextView) findViewById(R.id.wind_1);
		pm = (TextView) findViewById(R.id.pm);
		pm_1 = (TextView) findViewById(R.id.pm_1);
		gm = (TextView) findViewById(R.id.gm);
		cy = (TextView) findViewById(R.id.cy);
		yd = (TextView) findViewById(R.id.yd);
		one_type = (TextView) findViewById(R.id.one_type);
		two_type = (TextView) findViewById(R.id.two_type);
		three_type = (TextView) findViewById(R.id.three_type);
		one_warm = (TextView) findViewById(R.id.one_warm);
		two_warm = (TextView) findViewById(R.id.two_warm);
		three_warm = (TextView) findViewById(R.id.three_warm);
		main_pic = (ImageView) findViewById(R.id.main_pic);
		one_pic = (ImageView) findViewById(R.id.one_pic);
		two_pic = (ImageView) findViewById(R.id.two_pic);
		three_pic = (ImageView) findViewById(R.id.three_pic);
		background = (LinearLayout) findViewById(R.id.background);
		update.setOnClickListener(this);
		selectCity.setOnClickListener(this);
		location.setOnClickListener(this);
		drawerlayout = (DrawerLayout) findViewById(R.id.drawerlayout);
		gson = new Gson();

		more.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				drawerlayout.openDrawer(Gravity.LEFT);
			}
		});

	}

	// 根据城市调接口查天气
	public void getCity(final String cityValue) {
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url
				+ cityValue + "&" + "key=" + key, null,
				new Response.Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						try {
							JSONObject data = response.getJSONArray(
									"HeWeather5").getJSONObject(0);
							String status = data.getString("status");
							// 如果状态为ok，则用Gson解析拿数据
							if (status.equals("ok")) {
								// 利用Gson解析把json数据映射给对象
								Bean bean = gson.fromJson(data.toString(),
										Bean.class);
								City.CityBean cb = bean.getAqi().getCity();
								// 拿aqi，转换成int，并根据指数自行分污染等级
								String aqi = cb.getAqi();
								pm.setText("aqi " + aqi);
								int value = Integer.valueOf(aqi);
								judgeAqi(value);
								// 开始显示数据
								selectCityName.setText(cityValue); // 城市名
								publishTime.setText(DataString.StringData()); // 时间
								// 星期几
								String statusString = bean.getNow().getCond()
										.getTxt(); // 天气状态
								weatherStatus.setText(statusString);
								temperature.setText(bean.getNow().getTmp());// 温度

								wind.setText(bean.getNow().getWind().getDir());// 风向
								wind_1.setText(bean.getNow().getWind().getSc()
										+ "级");// 风力
								gm.setText(bean.getSuggestion().getFlu()
										.getTxt());// 感冒
								cy.setText(bean.getSuggestion().getDrsg()
										.getTxt());// 穿衣
								yd.setText(bean.getSuggestion().getSport()
										.getTxt());// 出行
								one_type.setText(bean.getDaily_forecast()
										.get(0).getCond().getTxt_d());// 预报天气
								two_type.setText(bean.getDaily_forecast()
										.get(1).getCond().getTxt_d());
								three_type.setText(bean.getDaily_forecast()
										.get(2).getCond().getTxt_d());
								one_warm.setText(bean.getDaily_forecast()
										.get(0).getTmp().getMin()
										+ "~"
										+ bean.getDaily_forecast().get(0)
										.getTmp().getMax() + "°C");// 预报温度
								two_warm.setText(bean.getDaily_forecast()
										.get(1).getTmp().getMin()
										+ "~"
										+ bean.getDaily_forecast().get(1)
										.getTmp().getMax() + "°C");
								three_warm.setText(bean.getDaily_forecast()
										.get(2).getTmp().getMin()
										+ "~"
										+ bean.getDaily_forecast().get(2)
										.getTmp().getMax() + "°C");
								// 判断天气状态，不同的天气配不同的图标
								int mainpic = judgeWeather(statusString); // 主天气图标
								int onepic = judgeWeather(bean
										.getDaily_forecast().get(0).getCond()
										.getTxt_d());// 三天预报的图标
								int twopic = judgeWeather(bean
										.getDaily_forecast().get(1).getCond()
										.getTxt_d());
								int threepic = judgeWeather(bean
										.getDaily_forecast().get(2).getCond()
										.getTxt_d());
								main_pic.setImageResource(mainpic);
								one_pic.setImageResource(onepic);
								two_pic.setImageResource(twopic);
								three_pic.setImageResource(threepic);
								// 关闭提示框
								if (updateDialog != null
										&& updateDialog.isShowing()) {
									updateDialog.dismiss();
									Toast.makeText(MainActivity.this, "更新成功!",
											Toast.LENGTH_SHORT).show();
								}
								if (searchDialog != null
										&& searchDialog.isShowing()) {
									searchDialog.dismiss();
								}
								if (locationDialog != null
										&& locationDialog.isShowing()) {
									Toast.makeText(MainActivity.this, "定位成功!",
											Toast.LENGTH_SHORT).show();
									locationDialog.dismiss();
								}

							} else {
								Toast.makeText(MainActivity.this,
										"抱歉！没有这个城市的天气信息 (。﹏。*)",
										Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(MainActivity.this,
						"服务器异常，请检查您的网络是否已连接.", Toast.LENGTH_SHORT)
						.show();
			}
		});

		mQueue.add(jsonObjectRequest);
	}

	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.select_city:
				Intent intent = new Intent(this, SelectCity.class);
				startActivity(intent);
				break;
			case R.id.update:
				updateDialog = new ProgressDialog(this);
				updateDialog.setMessage("正在为您更新天气中...");
				updateDialog.show();
				if (cityValue != null) {
					getCity(cityValue);
				} else { // 默认进去，就点刷新，则默认查询长沙的天气
					getCity(selectCityName.getText().toString());
				}
				break;
			case R.id.location:
				locationDialog = new ProgressDialog(this);
				locationDialog.setMessage("正在定位您的城市...");
				locationDialog.show();
				getLocation();
				break;
		}
	}

	// 当请求网络返回的json数据，中文显示为unicode码时候调用即可转换成中文
	public static String decodeUnicode(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
							case '0':
							case '1':
							case '2':
							case '3':
							case '4':
							case '5':
							case '6':
							case '7':
							case '8':
							case '9':
								value = (value << 4) + aChar - '0';
								break;
							case 'a':
							case 'b':
							case 'c':
							case 'd':
							case 'e':
							case 'f':
								value = (value << 4) + 10 + aChar - 'a';
								break;
							case 'A':
							case 'B':
							case 'C':
							case 'D':
							case 'E':
							case 'F':
								value = (value << 4) + 10 + aChar - 'A';
								break;
							default:
								throw new IllegalArgumentException(
										"Malformed   \\uxxxx   encoding.");
						}

					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					outBuffer.append(aChar);
				}
			} else
				outBuffer.append(aChar);
		}
		return outBuffer.toString();
	}

	// 拿到aqi字符串转换成整型，(根据网上百度到空气质量图，自行判断)
	private void judgeAqi(int value) {
		if (value >= 0 && value <= 50) {
			pm_1.setText("空气优");
		} else if (value >= 51 && value <= 100) {
			pm_1.setText("空气良");
		} else if (value >= 101 && value <= 150) {
			pm_1.setText("轻度污染");
		} else if (value >= 151 && value <= 200) {
			pm_1.setText("中度污染");
		} else if (value >= 201 && value <= 300) {
			pm_1.setText("重度污染");
		} else if (value > 300) {
			pm_1.setText("严重污染");
		}

	}

	// 不同天气换不同图片
	private int judgeWeather(String name) {
		if (name.equals("晴") || name.equals("热") ) {
			return R.drawable.qing;
		} else if (name.equals("晴间多云")) {
			return R.drawable.qingjianduoyun;
		} else if (name.equals("多云")) {
			return R.drawable.duoyun;
		} else if (name.equals("少云")) {
			return R.drawable.shaoyun;
		} else if (name.equals("阴")) {
			return R.drawable.yin;
		} else if (name.equals("有风") || name.equals("平静") || name.equals("微风")
				|| name.equals("和风") || name.equals("清风")) {
			return R.drawable.weifeng;
		} else if (name.equals("强风/劲风") || name.equals("疾风") || name.equals("大风")
				|| name.equals("烈风")) {
			return R.drawable.qiangfeng;
		} else if (name.equals("风暴") || name.equals("狂爆风") || name.equals("飓风")
				|| name.equals("龙卷风") || name.equals("热带风暴")) {
			return R.drawable.longjuanfeng;
		} else if (name.equals("阵雨") || name.equals("强阵雨")) {
			return R.drawable.zhenyu;
		} else if (name.equals("雷阵雨") || name.equals("强雷阵雨")
				|| name.equals("雷阵雨伴有冰雹")) {
			return R.drawable.qiangleizhenyu;
		} else if (name.equals("小雨") || name.equals("毛毛雨/细雨")) {
			return R.drawable.xiaoyu;
		} else if (name.equals("中雨")) {
			return R.drawable.zhongyu;
		} else if (name.equals("大雨")) {
			return R.drawable.dayu;
		} else if (name.equals("暴雨") || name.equals("极端降雨")
				|| name.equals("暴雨") || name.equals("大暴雨")
				|| name.equals("特大暴雨") || name.equals("冻雨")) {
			return R.drawable.baoyu;
		} else if (name.equals("小雪") || name.equals("冷")) {
			return R.drawable.xiaoxue;
		} else if (name.equals("中雪") || name.equals("阵雪")) {
			return R.drawable.zhongxue;
		} else if (name.equals("大雪") || name.equals("暴雪")) {
			return R.drawable.daxue;
		} else if (name.equals("雨夹雪") || name.equals("雨雪天气")
				|| name.equals("阵雨夹雪")) {
			return R.drawable.yujiaxue;
		} else if (name.equals("雾霾")) {
			return R.drawable.wu;
		} else if (name.equals("霾") || name.equals("扬沙") || name.equals("浮尘")
				|| name.equals("沙尘暴") || name.equals("强沙尘暴")) {
			return R.drawable.mai;
		} else {
			return R.drawable.unknow;
		}
	}

	private void getLocation() {
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		MyLocationListenner myListener = new MyLocationListenner();
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		// 定位SDK参数设置
		LocationClientOption option = new LocationClientOption();
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
		mLocationClient.start();

	}

	// 关闭定位
	// TODO 会报线性池错误，因为一直在界面定位，定位服务每次都开启一遍.
	public void onDestroy() {
		mLocationClient.stop();
		super.onDestroy();
	}

	// 百度地图定位：实现接口
	private class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			try {
				// 当前设备位置所在的市，这里定位只提供查询市的天气
				String city = location.getCity();
				// 小县城需要选，因为这里县和区都有，接口限制不方便查询
				// 比如长沙市天心区,天心不是县肯定查不到;北京市朝阳区,朝阳又可以查到天气
				// String county = location.getDistrict();
				// 去掉最后一个字符串"市"
				String cityName = city.substring(0, city.length() - 1);
				getCity(cityName);
				// TODO 每次定位完后就关闭，不会造成上限5个线程的堵塞异常
				mLocationClient.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	// 注册广播接收器监听网络
	public class ConnectionChangeReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mobNetInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			NetworkInfo wifiNetInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

			if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
				Toast.makeText(MainActivity.this, "请检查您的网络是否正常",
						Toast.LENGTH_LONG).show();
			} else {
				getLocation();
			}
		}
	}

	// 监听是否有网络
	private void haveWifi() {
		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		myReceiver = new ConnectionChangeReceiver();
		this.registerReceiver(myReceiver, filter);
	}

	// 再按一次退出
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// 定义一个内部类，用于获取当前时间和星期
	public static class DataString {
		private static String mYear;
		private static String mMonth;
		private static String mDay;
		private static String mWay;

		public static String StringData() {
			final Calendar c = Calendar.getInstance();
			c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
			mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
			mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
			mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
			mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
			if ("1".equals(mWay)) {
				mWay = "天";
			} else if ("2".equals(mWay)) {
				mWay = "一";
			} else if ("3".equals(mWay)) {
				mWay = "二";
			} else if ("4".equals(mWay)) {
				mWay = "三";
			} else if ("5".equals(mWay)) {
				mWay = "四";
			} else if ("6".equals(mWay)) {
				mWay = "五";
			} else if ("7".equals(mWay)) {
				mWay = "六";
			}
			return mYear + "年" + mMonth + "月" + mDay + "日   " + "星期" + mWay;
		}
	}

	// 加沉浸式标题栏判断 ，这两个方法是引用了开源框架SystemBarTint来处理标题栏颜色
	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	// 加沉浸式标题栏判断，这两个方法是引用了开源框架SystemBarTint来处理标题栏颜色
	public void initTop() {
		// 4.4及以上版本开启
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setNavigationBarTintEnabled(true);
		// 自定义颜色
		tintManager.setTintColor(getResources().getColor(R.color.title_color));

	}

}
