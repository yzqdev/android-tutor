package com.example.nbshoping.utils;

/*
*网络部分url
 */
public class URLUtils {

//    public  static String PUBLIC_URL="http://192.168.10.13:8080/beikeMarke";    //内网公共部分
    public  static String PUBLIC_URL="http://47.100.26.130:8085/beikeMarke";//外网公共部分
//    public  static String PUBLIC_URL="http://192.168.13.34:8080/beikeMarket";//内网公共部分2

    //个人相关
    //注册接口
    public static String register_url=PUBLIC_URL+"/user/register";
    //登录接口
    public static String login_url=PUBLIC_URL+"/user/login";
    //更新完善信息接口
    public static String updateInfo_url=PUBLIC_URL+"/user/updateInfo";
    //示密保问题
    public static String showQuestion_url=PUBLIC_URL+"/user/showQuestion";
    //证密保答案
    public static String verifyAnswer_url=PUBLIC_URL+"/user/verifyAnswer";
    //修改密码
    public static String updatePwd_url=PUBLIC_URL+"/user/updatePwd";


    //业务相关
    //查询所有的分类
    public static String queryAllCategory_url=PUBLIC_URL+"/biz/queryAllCategory";
    //通过分类查询对应的产品
    public static String queryCommodityByCateId_url=PUBLIC_URL+"/biz/queryCommodityByCateId?categoryId=";
    //商品模糊查询get
    public static String queryCommodityByName_url=PUBLIC_URL+"/biz/queryCommodityByName";
    //商品详情
    public static String queryCommodityInfo_url=PUBLIC_URL+"/biz/queryCommodityInfo?commodityId=";
    //热销产品
    public static String hotCommodity_url=PUBLIC_URL+"/biz/hotCommodity";
    // 产品推荐
    public static String recommendCommodity_url=PUBLIC_URL+"/biz/recommendCommodity";
    // 猜你喜欢
    public static String guessYouLike_url=PUBLIC_URL+"/biz/guessYouLike";

    //购物相关
    //添加购物车 post
    public static String insertShoppingCar_url=PUBLIC_URL+"/trade/insertShoppingCar";
    // 查询购物车
    public static String queryShoppingCar_url=PUBLIC_URL+"/trade/queryShoppingCar";
    // 购物车中商品下单支付
    public static String orderShopping_url=PUBLIC_URL+"/trade/orderShopping";
    //直接购买（不进入购物车） post
    public static String insertBought_url=PUBLIC_URL+"/trade/insertBought";
    // 查询已购买商品,内网
    public static String queryBougth_url=PUBLIC_URL+"/trade/querybougth";


}
