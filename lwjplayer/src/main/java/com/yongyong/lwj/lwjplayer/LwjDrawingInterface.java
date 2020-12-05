package com.yongyong.lwj.lwjplayer;

import android.graphics.Bitmap;
import android.view.View;

import androidx.annotation.NonNull;

import com.yongyong.lwj.lwjplayer.engine.LwjPlayerBase;

/**
 * @author yongyong
 * 
 * desc:
 * 
 * @// TODO: 2020/12/5
 */
public interface LwjDrawingInterface {

    /**
     * 绑定
     * @param player
     */
    void attach(@NonNull LwjPlayerBase player);

    /**
     * 视频大小
     * @param videoWidth
     * @param videoHeight
     */
    void setVideoSize(int videoWidth, int videoHeight);

    /**
     * 旋转
     * @param degree
     */
    void setRotationDegree(int degree);

    /**
     * 缩放比例
     * @param ratioEnum
     */
    void setRatio(LwjRatioEnum ratioEnum);

    /**
     * 屏幕截图
     * @return
     */
    Bitmap screenCapture();

    /**
     *
     * @return
     */
    View getView();

    /**
     * 释放
     */
    void release();
}
