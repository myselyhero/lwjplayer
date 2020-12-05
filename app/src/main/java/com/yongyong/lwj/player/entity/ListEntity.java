package com.yongyong.lwj.player.entity;

import java.io.Serializable;

/**
 * @author yongyong
 *
 * @email 1947920597@qq.com
 *
 * desc:list实体类
 *
 * @// TODO: 2020/10/25
 */
public class ListEntity implements Serializable {

    private String id;
    /* 用户 */
    private String userId;
    private String userPicture;
    private String userName;
    /*  */
    private String videoThumbnail;
    private String videoUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPicture() {
        return userPicture;
    }

    public void setUserPicture(String userPicture) {
        this.userPicture = userPicture;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

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
        return "ListEntity{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", userPicture='" + userPicture + '\'' +
                ", userName='" + userName + '\'' +
                ", videoThumbnail='" + videoThumbnail + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                '}';
    }
}
