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
        int[] measuredSize = LwjMeasureHelper.getInstance().onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measuredSize[0], measuredSize[1]);
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
        LwjMeasureHelper.getInstance().setVideoSize(videoWidth,videoHeight);
        requestLayout();
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
        Log.e(TAG, "screenCapture: SurfaceView No support!");
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
