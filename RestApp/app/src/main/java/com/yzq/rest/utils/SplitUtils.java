package com.yzq.rest.utils;

/**
 * Created by JDK on 2016/10/8.
 */
public class SplitUtils {
    public static String[] splitWithComma(String s){
        String [] arrayString=s.split(",./");
        return arrayString;
    }
}
