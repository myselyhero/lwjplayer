package com.yongyong.lwj.player.tv;

import java.io.Serializable;

/**
 * @author yongyong
 * 
 * @desc:
 * 
 * @// TODO: 2020/12/8
 */
public class TvEntity implements Serializable {

    private String url;
    private String name;
    private boolean sel;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSel() {
        return sel;
    }

    public void setSel(boolean sel) {
        this.sel = sel;
    }

    public TvEntity(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public TvEntity(String url, String name, boolean sel) {
        this.url = url;
        this.name = name;
        this.sel = sel;
    }

    @Override
    public String toString() {
        return "TvEntity{" +
                "url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", sel=" + sel +
                '}';
    }
}
