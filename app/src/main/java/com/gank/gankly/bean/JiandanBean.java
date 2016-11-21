package com.gank.gankly.bean;

/**
 * Create by LingYan on 2016-11-21
 * Email:137387869@qq.com
 */

public class JiandanBean {
    private String url;
    private String title;
    private String type;
    private String imgUrl;

    public JiandanBean(String url, String title, String type, String imgUrl) {
        this.url = url;
        this.title = title;
        this.type = type;
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
