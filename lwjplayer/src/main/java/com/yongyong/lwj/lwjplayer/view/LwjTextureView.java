package com.yongyong.lwj.lwjplayer.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.view.Surface;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yongyong.lwj.lwjplayer.LwjRatioEnum;
import com.yongyong.lwj.lwjplayer.engine.LwjPlayer;

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
public class LwjTextureView extends android.view.TextureView implements android.view.TextureView.SurfaceTextureListener {

    /**  */
    private SurfaceTexture mSurfaceTexture;

    /**  */
    @Nullable
    private LwjPlayer mLwjPlayer;
    private Surface mSurface;

    /** 视频旋转 */
    private int mRotationDegree;

    /**  */
    private int mVideoWidth,mVideoHeight;

    /** 屏幕尺寸 */
    private LwjRatioEnum mRatioEnum = LwjRatioEnum.RATIO_DEFAULT;

    public LwjTextureView(Context context) {
        super(context);
        setSurfaceTextureListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int[] measuredSize = doMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measuredSize[0], measuredSize[1]);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        if (mSurfaceTexture != null) {
            setSurfaceTexture(mSurfaceTexture);
        } else {
            mSurfaceTexture = surfaceTexture;
            mSurface = new Surface(surfaceTexture);
            if (mLwjPlayer != null) {
                mLwjPlayer.setSurface(mSurface);
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

    /**
     * 绑定播放器
     * @param player
     */
    public void attachToPlayer(@NonNull LwjPlayer player) {
        mLwjPlayer = player;
    }

    /**
     *
     * @param videoWidth
     * @param videoHeight
     */
    public void setVideoSize(int videoWidth, int videoHeight) {
        if (videoWidth > 0 && videoHeight > 0) {
            mVideoWidth = videoWidth;
            mVideoHeight = videoHeight;
            /** 竖屏 */
            if (videoHeight > videoWidth) {
                //竖屏视频，使用居中裁剪
                mRatioEnum = LwjRatioEnum.RATIO_CROP;
            } else {
                /** 横屏 */
                mRatioEnum = LwjRatioEnum.RATIO_DEFAULT;
            }
            requestLayout();
        }
    }

    /**
     * 尺寸
     * @param ratioEnum
     */
    public void setScaleType(LwjRatioEnum ratioEnum) {
        mRatioEnum = ratioEnum;
        requestLayout();
    }

    /**
     *
     * @param rotationDegree
     */
    public void setRotationDegree(int rotationDegree) {
        mRotationDegree = rotationDegree;
    }

    /**
     * 屏幕截图
     * @return
     */
    public Bitmap screenCapture(){
        return getBitmap();
    }

    /**
     * 释放
     */
    public void release() {
        if (mSurface != null)
            mSurface.release();
        if (mSurfaceTexture != null)
            mSurfaceTexture.release();
    }

    /**
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     * @return
     */
    private int[] doMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /** 软解码时处理旋转信息，交换宽高 */
        if (mRotationDegree == 90 || mRotationDegree == 270) {
            widthMeasureSpec = widthMeasureSpec + heightMeasureSpec;
            heightMeasureSpec = widthMeasureSpec - heightMeasureSpec;
            widthMeasureSpec = widthMeasureSpec - heightMeasureSpec;
        }

        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        int height = View.MeasureSpec.getSize(heightMeasureSpec);

        if (mVideoHeight == 0 || mVideoWidth == 0) {
            return new int[]{width, height};
        }

        /** 设置了宽高比例 */
        switch (mRatioEnum){
            case RATIO_DEFAULT://默认使用原视频高宽
            default:
                if (mVideoWidth * height < width * mVideoHeight) {
                    width = height * mVideoWidth / mVideoHeight;
                } else if (mVideoWidth * height > width * mVideoHeight) {
                    height = width * mVideoHeight / mVideoWidth;
                }
                break;
            case RATIO_CROP://居中
                if (mVideoWidth * height > width * mVideoHeight) {
                    width = height * mVideoWidth / mVideoHeight;
                } else {
                    height = width * mVideoHeight / mVideoWidth;
                }
                break;
            case RATIO_FULL://全屏
            case RATIO_1_1:
                width = widthMeasureSpec;
                height = heightMeasureSpec;
                break;
            case RATIO_16_9://
                if (height > width / 16 * 9) {
                    height = width / 16 * 9;
                } else {
                    width = height / 9 * 16;
                }
                break;
            case RATIO_4_3://
                if (height > width / 4 * 3) {
                    height = width / 4 * 3;
                } else {
                    width = height / 3 * 4;
                }
                break;
        }
        return new int[]{width, height};
    }
}