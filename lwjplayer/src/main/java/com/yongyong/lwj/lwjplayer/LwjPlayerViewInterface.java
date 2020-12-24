package com.yongyong.lwj.lwjplayer;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.yongyong.lwj.lwjplayer.view.LwjControllerBaseView;

/**
 * @author yongyong
 * 
 * @desc:播放器interface
 * 
 * @// TODO: 2020/12/5
 */
public interface LwjPlayerViewInterface {

    /**
     *
     * @param core
     */
    void setCore(int core);

    /**
     *
     */
    void setOptions();

    /**
     * 设置数据源
     * @param dataSource
     */
    void setDataSource(String dataSource);

    /**
     * 设置播放器比例
     * @param ratioEnum
     */
    void setRatio(LwjRatioEnum ratioEnum);

    /**
     * 设置音量
     * @param l
     * @param r
     */
    void setVolume(float l, float r);

    /**
     * 设置播放进度
     * @param to
     */
    void seekTo(long to);

    /**
     *
     * @return 返回视频总时长
     */
    long getDuration();

    /**
     *
     * @return 当前播放进度
     */
    long getCurrentPosition();

    /**
     *
     * @return
     */
    long getTcpSpeed();

    /**
     * 是否获取音频焦点、默认获取
     * @param focus
     */
    void setFocus(boolean focus);

    /**
     *
     * @return
     */
    boolean isFocus();

    /**
     * 是否循环播放、默认循环
     * @param looping
     */
    void setLooping(boolean looping);

    /**
     *
     * @return
     */
    boolean isLooping();

    /**
     * 是否静音
     * @param voice
     */
    void setVoice(boolean voice);

    /**
     *
     * @return
     */
    boolean isVoice();

    /**
     * 是否全屏
     * @param fullScreen
     */
    void setFullScreen(boolean fullScreen);

    /**
     *
     * @return
     */
    boolean isFullScreen();

    /**
     * 设置控制器
     * @param controllerBaseView
     */
    void setControllerView(@NonNull LwjControllerBaseView controllerBaseView);

    /**
     * 设置播放器状态监听 用于为设置控制器时使用
     * @param listener
     */
    void addStatusChangeListener(@NonNull LwjStatusChangeListener listener);

    /**
     * 截图
     * @return
     */
    Bitmap screenCapture();

    /**
     * 是否是拉流
     * @return
     */
    boolean isLive();

    /**
     * 是否正在播放
     * @return
     */
    boolean isPlayer();

    /**
     * 开始播放
     */
    void onStart();

    /**
     * 暂停
     */
    void onPause();

    /**
     * 释放
     */
    void onRelease();
}
