package com.yongyong.lwj.lwjplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yongyong.lwj.lwjplayer.engine.LwjPlayerBase;

/**
 * @author yongyong
 *
 * @mail 1947920597@qq.com
 *
 * desc:渲染视图
 *
 * @// TODO: 2020/10/17
 */
@SuppressLint("ViewConstructor")
public class LwjTextureView extends TextureView implements LwjDrawingInterface, TextureView.SurfaceTextureListener {

    /**  */
    private SurfaceTexture mSurfaceTexture;

    /**  */
    @Nullable
    private LwjPlayerBase mPlayer;
    private Surface mSurface;

    public LwjTextureView(Context context) {
        super(context);
        setSurfaceTextureListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int[] measuredSize = LwjMeasureHelper.getInstance().onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measuredSize[0], measuredSize[1]);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        if (mSurfaceTexture != null) {
            setSurfaceTexture(mSurfaceTexture);
        } else {
            mSurfaceTexture = surfaceTexture;
            mSurface = new Surface(surfaceTexture);
            if (mPlayer != null) {
                mPlayer.setSurface(mSurface);
            }
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public void attach(@NonNull LwjPlayerBase player) {
        mPlayer = player;
    }

    /**
     *
     * @param videoWidth
     * @param videoHeight
     */
    public void setVideoSize(int videoWidth, int videoHeight) {
        if (videoWidth > 0 && videoHeight > 0) {
            LwjMeasureHelper.getInstance().setVideoSize(videoWidth,videoHeight);
            /** 竖屏 */
            /*if (videoHeight > videoWidth) {
                LwjMeasureHelper.getInstance().setRatioEnum(LwjRatioEnum.RATIO_CROP);
            } else {
                *//** 横屏 *//*
                LwjMeasureHelper.getInstance().setRatioEnum(LwjRatioEnum.RATIO_DEFAULT);
            }*/
            requestLayout();
        }
    }

    @Override
    public void setRotationDegree(int degree) {
        LwjMeasureHelper.getInstance().setRotationDegree(degree);
        requestLayout();
    }

    @Override
    public void setRatio(LwjRatioEnum ratioEnum) {
        LwjMeasureHelper.getInstance().setRatioEnum(ratioEnum);
        requestLayout();
    }

    @Override
    public Bitmap screenCapture() {
        return getBitmap();
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void release() {
        if (mSurface != null)
            mSurface.release();
        if (mSurfaceTexture != null)
            mSurfaceTexture.release();
    }
}