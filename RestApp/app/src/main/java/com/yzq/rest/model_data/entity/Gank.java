package com.yzq.rest.model_data.entity;

import java.util.Date;

/**
 * Created by JDK on 2016/8/2.
 */
public class Gank extends Base{
    public boolean used;
    public String type;//干货类型，如Android，iOS，福利等
    public String url;//链接地址
    public String who;//作者
    public String desc;//干货内容的描述
    public Date updatedAt;
    public Date createdAt;
    public Date publishedAt;
}
