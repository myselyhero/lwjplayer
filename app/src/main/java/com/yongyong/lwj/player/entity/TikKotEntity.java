package com.yongyong.lwj.player.entity;

import java.io.Serializable;

/**
 * @author yongyong
 *
 * @email 1947920597@qq.com
 *
 * desc:抖音实体类
 *
 * @// TODO: 2020/10/25
 */
public class TikKotEntity implements Serializable {

    private String videoThumbnail;
    private String videoUrl;

    public String getVideoThumbnail() {
        return videoThumbnail;
    }

    public void setVideoThumbnail(String videoThumbnail) {
        this.videoThumbnail = videoThumbnail;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Override
    public String toString() {
        return "TikKotEntity{" +
                "videoThumbnail='" + videoThumbnail + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                '}';
    }
}
