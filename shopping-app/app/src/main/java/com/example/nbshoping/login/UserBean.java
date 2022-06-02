package com.example.nbshoping.login;

import java.io.Serializable;

public class UserBean implements Serializable {

    /**
     * code : 200
     * message : login success
     * data : {"id":6,"phone":"18855556666","password":"123456","name":"张三","address":"江西九江","nickname":"张三丰","question":"我的母校","answer":"九江学院"}
     */

    private int code;
    private String message;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * id : 6
         * phone : 18855556666
         * password : 123456
         * name : 张三
         * address : 江西九江
         * nickname : 张三丰
         * question : 我的母校
         * answer : 九江学院
         */

        private int id;
        private String phone;
        private String password;
        private String name;
        private String address;
        private String nickname;
        private String question;
        private String answer;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }
    }
}
