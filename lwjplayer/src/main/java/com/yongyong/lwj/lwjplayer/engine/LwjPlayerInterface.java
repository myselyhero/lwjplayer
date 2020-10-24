package com.yongyong.lwj.lwjplayer.engine;

import android.content.Context;
import android.view.Surface;

import androidx.annotation.NonNull;

import com.yongyong.lwj.lwjplayer.LwjSpeedLevelEnum;

/**
 * @author yongyong
 *
 * @mail 1947920597@qq.com
 *
 * desc:播放器interface
 *
 * @// TODO: 2020/10/17
 */
public interface LwjPlayerInterface {

    /**
     * 初始化
     * @param context
     */
    void initPlayer(Context context);

    /**
     * 数据源
     *
     * @param url 播放地址
     */
    void setDataSource(String url);

    /**
     * 异步准备
     */
    void prepare();

    /**
     * 播放
     */
    void onStart();

    /**
     * 暂停
     */
    void onPause();

    /**
     * 停止
     */
    void onStop();

    /**
     * 重置
     */
    void onReset();

    /**
     * 释放
     */
    void onRelease();

    /**
     *
     * @return 是否正在播放
     */
    boolean isPlaying();

    /**
     *
     * @param time 调整进度
     */
    void seekTo(long time);

    /**
     *
     * @return 当前播放的位置
     */
    long getCurrentPosition();

    /**
     *
     * @return 总时长
     */
    long getDuration();

    /**
     *
     * @param surface 渲染
     */
    void setSurface(Surface surface);

    /**
     * 音量
     * @param v1 左声道
     * @param v2 右声道
     */
    void setVolume(float v1, float v2);

    /**
     * 是否循环播放
     * @param isLooping
     */
    void setLooping(boolean isLooping);

    /**
     *
     * @return 是否循环播放
     */
    boolean isLooping();

    /**
     * 设置播放速度
     */
    void setSpeed(LwjSpeedLevelEnum speed);

    /**
     *
     * @return 宽高
     */
    int[] getVideoSize();

    /**
     * 监听
     * @param listener
     */
    void addPlayerListener(@NonNull LwjPlayerListener listener);
}
