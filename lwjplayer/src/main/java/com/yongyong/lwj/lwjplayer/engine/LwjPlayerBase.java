package com.yongyong.lwj.lwjplayer.engine;

import android.content.Context;
import android.view.Surface;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;

import com.yongyong.lwj.lwjplayer.LwjSpeedLevelEnum;

/**
 * @author yongyong
 *
 * @desc:播放器父类
 * 
 * @// TODO: 2020/12/4
 */
public abstract class LwjPlayerBase {

    /**
     *
     * @param context
     */
    public abstract void init(Context context);

    /**
     *
     */
    public abstract void setOptions();

    /**
     * 数据源
     *
     * @param url 播放地址
     */
    public abstract void setDataSource(String url);

    /**
     * 异步准备
     */
    public abstract void prepare();

    /**
     * 播放
     */
    public abstract void onStart();

    /**
     * 暂停
     */
    public abstract void onPause();

    /**
     * 停止
     */
    public abstract void onStop();

    /**
     * 重置
     */
    public abstract void onReset();

    /**
     * 释放
     */
    public abstract void onRelease();

    /**
     *
     * @return 是否正在播放
     */
    public abstract boolean isPlaying();

    /**
     *
     * @param time 调整进度
     */
    public abstract void seekTo(long time);

    /**
     *
     * @return 当前播放的位置
     */
    public abstract long getCurrentPosition();

    /**
     *
     * @return 总时长
     */
    public abstract long getDuration();

    /**
     *
     * @param surface 渲染
     */
    public abstract void setSurface(Surface surface);

    /**
     *
     * @param holder
     */
    public abstract void setDisplay(SurfaceHolder holder);

    /**
     * 音量
     * @param v1 左声道
     * @param v2 右声道
     */
    public abstract void setVolume(float v1, float v2);

    /**
     * 是否循环播放
     * @param isLooping
     */
    public abstract void setLooping(boolean isLooping);

    /**
     *
     * @return 是否循环播放
     */
    public abstract boolean isLooping();

    /**
     * 设置播放速度
     */
    public abstract void setSpeed(LwjSpeedLevelEnum speed);

    /**
     *
     * @return
     */
    public abstract long getTcpSpeed();

    /**
     *
     * @return 宽高
     */
    public abstract int[] getVideoSize();

    /**
     * 监听
     * @param listener
     */
    public abstract void addPlayerListener(@NonNull LwjPlayerListener listener);
}
