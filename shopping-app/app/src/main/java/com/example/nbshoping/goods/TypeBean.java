package com.example.nbshoping.goods;

import java.io.Serializable;
import java.util.List;

//货物类型数据格式
public class TypeBean implements Serializable {

    /**
     * code : 200
     * message : 查询成功
     * data : [{"id":1,"name":"食品","icon":"/image/type/type_food.png"},{"id":2,"name":"饮品","icon":"/image/type/type_drink.png"},{"id":3,"name":"3C数码","icon":"/image/type/type_3c.png"},{"id":4,"name":"生活家居","icon":"/image/type/type_fitment.png"},{"id":5,"name":"服装服饰","icon":"/image/type/type_clothing.png"},{"id":6,"name":"美妆洗护","icon":"/image/type/type_cosmetics.png"},{"id":7,"name":"箱包","icon":"/image/type/type_bag.png"},{"id":8,"name":"母婴","icon":"/image/type/type_baby.png"},{"id":9,"name":"图书","icon":"/image/type/type_book.png"},{"id":10,"name":"宠物","icon":"/image/type/type_pet.png"}]
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
         * name : 食品
         * icon : /image/type/type_food.png
         */

        private int id;
        private String name;
        private String icon;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}
