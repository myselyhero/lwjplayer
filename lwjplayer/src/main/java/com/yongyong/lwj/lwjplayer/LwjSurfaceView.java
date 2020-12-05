package com.yongyong.lwj.lwjplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yongyong.lwj.lwjplayer.engine.LwjPlayerBase;

/**
 * @author yongyong
 * 
 * @desc:
 * 
 * @// TODO: 2020/12/5
 */
@SuppressLint("ViewConstructor")
public class LwjSurfaceView extends SurfaceView implements LwjDrawingInterface, SurfaceHolder.Callback {

    private static final String TAG = "LwjSurfaceView";
    
    /** 播放器 */
    @Nullable
    private LwjPlayerBase mPlayer;

    /** 视频旋转 */
    private int mRotationDegree;

    /**
     * 视频宽高
     */
    private int mVideoWidth,mVideoHeight;

    /**
     * 比例
     */
    private LwjRatioEnum mRatioEnum = LwjRatioEnum.RATIO_DEFAULT;

    public LwjSurfaceView(Context context) {
        super(context);
        init();
    }

    public LwjSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LwjSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /** 软解码时处理旋转信息，交换宽高 */
        if (mRotationDegree == 90 || mRotationDegree == 270) {
            widthMeasureSpec = widthMeasureSpec + heightMeasureSpec;
            heightMeasureSpec = widthMeasureSpec - heightMeasureSpec;
            widthMeasureSpec = widthMeasureSpec - heightMeasureSpec;
        }

        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        int height = View.MeasureSpec.getSize(heightMeasureSpec);

        /** 设置了宽高比例 */
        if (mVideoHeight != 0 || mVideoWidth != 0) {
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
        }

        setMeasuredDimension(width, height);
    }

    /**
     *
     */
    private void init(){
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        if (mPlayer != null) {
            mPlayer.setSurface(holder.getSurface());
        }
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    @Override
    public void attach(@NonNull LwjPlayerBase player) {
        mPlayer = player;
    }

    @Override
    public void setVideoSize(int videoWidth, int videoHeight) {
        mVideoWidth = videoWidth;
        mVideoHeight = videoHeight;
        requestLayout();
    }

    @Override
    public void setRotationDegree(int degree) {
        mRotationDegree = degree;
        requestLayout();
    }

    @Override
    public void setRatio(LwjRatioEnum ratioEnum) {
        mRatioEnum = ratioEnum;
        requestLayout();
    }

    @Override
    public Bitmap screenCapture() {
        return null;
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void release() {

    }
}
