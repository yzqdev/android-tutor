package com.example.nbshoping.goods;

import java.io.Serializable;
import java.util.List;

//类型的多个货物信息数据格式
public class GoodsBean implements Serializable {

    /**
     * code : 200
     * message : 查询成功
     * data : [{"id":1,"count":9999,"categoryId":1,"name":"奥利奥","info":"奥利奥饼干","photo":"/image/commodity/aoliao.png","price":3.5},{"id":2,"count":500,"categoryId":1,"name":"零食大礼包","info":"网红超大巨型零食大礼包，一整箱猪饲料送女生、男友，休闲小食品","photo":"/image/commodity/lingshidalibao.png","price":148},{"id":3,"count":666,"categoryId":1,"name":"良品铺子麻辣鸭脖","info":"良品铺子鸭脖甜辣鸭脖零食小吃休闲食品锁鲜小包装麻辣小零食解馋","photo":"/image/commodity/yabo.png","price":25.5},{"id":4,"count":550,"categoryId":1,"name":"良品铺子猪肉脯","info":"良品铺子猪肉脯干靖江特产零食网红爆款小零食小吃休闲食品200g","photo":"/image/commodity/zhurou.png","price":45.5},{"id":5,"count":65,"categoryId":1,"name":"乐事薯片（10小包）","info":"乐事薯片小包12g*10袋网红零食小吃休闲食品","photo":"/image/commodity/leshi.png","price":20},{"id":6,"count":650,"categoryId":1,"name":"不二家棒棒糖","info":"不二家糖果原味牛奶棒棒糖280g/盒喜糖儿童糖果安全纸棒28只每盒","photo":"/image/commodity/buerbangbangtang.png","price":10.5},{"id":7,"count":1050,"categoryId":1,"name":"优洽果冻","info":"优洽酵素果冻正品益生元菌酵素粉青汁清排肠孝素果蔬宿便蓝莓果冻","photo":"/image/commodity/youqiaguodong.png","price":49.9},{"id":8,"count":555,"categoryId":1,"name":"淘吉果肉果冻布丁","info":"淘吉 日式樱花味酒冻果味布丁葡萄白桃蜜桔果肉果冻网红零食","photo":"/image/commodity/taoji.png","price":19.9}]
     */

    private int code;
    private String message;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * id : 1
         * count : 9999
         * categoryId : 1
         * name : 奥利奥
         * info : 奥利奥饼干
         * photo : /image/commodity/aoliao.png
         * price : 3.5
         */

        private int id;
        private int count;
        private int categoryId;
        private String name;
        private String info;
        private String photo;
        private double price;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }
}
