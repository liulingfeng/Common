package com.llf.basemodel.image;

import java.io.Serializable;

/**
 * Created by llf on 2017/3/9.
 */

public class Info implements Serializable {
    private String url;
    private String title;

    public Info(String title, String url) {
        this.url = url;
        this.title = title;
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
}
