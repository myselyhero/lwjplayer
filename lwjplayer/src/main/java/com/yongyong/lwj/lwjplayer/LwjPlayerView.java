package com.yongyong.lwj.lwjplayer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yongyong.lwj.lwjplayer.engine.LwjIjkPlayer;
import com.yongyong.lwj.lwjplayer.engine.LwjMediaPlayer;
import com.yongyong.lwj.lwjplayer.engine.LwjPlayerBase;
import com.yongyong.lwj.lwjplayer.engine.LwjPlayerListener;
import com.yongyong.lwj.lwjplayer.util.LwjAudioFocusManager;
import com.yongyong.lwj.lwjplayer.view.LwjControllerBaseView;

/**
 * @author yongyong
 *
 * @mail 1947920597@qq.com
 *
 * desc:自定义播放器
 * 
 * @// TODO: 2020/10/17
 */
public class LwjPlayerView extends FrameLayout implements LwjPlayerViewInterface {

    private static final String TAG = "LwjPlayerView";

    /** 开始渲染视频画面 */
    public static final int MEDIA_INFO_VIDEO_RENDERING_START = 3;
    /** 缓冲开始 */
    public static final int MEDIA_INFO_BUFFERING_START = 701;
    /** 缓冲结束 */
    public static final int MEDIA_INFO_BUFFERING_END = 702;
    /** 视频旋转信息 */
    public static final int MEDIA_INFO_VIDEO_ROTATION_CHANGED = 10001;

    /** 使用系统播放器 */
    public static final int MEDIA_PLAYER = 0;
    /** 使用ijk播放器 */
    public static final int MEDIA_PLAYER_IJK = 1;

    /**  */
    private Activity mActivity;

    /** 播放器实例 */
    private LwjPlayerBase mPlayer;

    /** 渲染 */
    private LwjDrawingInterface mDrawingInterface;

    /** 控制器 */
    private LwjControllerBaseView mControllerView;

    /** 容器 */
    private FrameLayout mFrameLayout;

    /** 播放进度 */
    private long mCurrentPosition;

    /** 是否循环播放 */
    private boolean isLooping = true;

    /** 是否禁音 */
    private boolean isVoice;

    /** 是否全屏 */
    private boolean isFullScreen;

    /** 数据源 */
    private String mDataSource;

    /** 状态 */
    private LwjStatusEnum mStatusEnum = LwjStatusEnum.STATUS_IDLE;

    /** 尺寸 */
    private LwjRatioEnum mRatioEnum = LwjRatioEnum.RATIO_DEFAULT;

    /** 音频焦点 */
    private boolean isFocus = true;
    private LwjAudioFocusManager mFocusManager;

    /** 播放器核心 */
    private int playerCore = MEDIA_PLAYER;

    /**  */
    private LwjStatusChangeListener statusChangeListener;

    public LwjPlayerView(@NonNull Context context) {
        super(context);
        init();
    }

    public LwjPlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LwjPlayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     *
     */
    private void init(){
        mActivity = (Activity) getContext();
        /** 初始化容器 */
        mFrameLayout = new FrameLayout(getContext());
        mFrameLayout.setBackgroundColor(getResources().getColor(R.color.lwj_transparency));
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mFrameLayout, params);
    }

    @Override
    public void setCore(int core) {
        playerCore = core;
    }

    @Override
    public void setOptions() {
        if (mPlayer != null)
            mPlayer.setOptions();
    }

    @Override
    public void setDataSource(String dataSource) {
        if (TextUtils.isEmpty(dataSource))
            return;
        mDataSource = dataSource;
    }

    @Override
    public void setRatio(LwjRatioEnum ratioEnum) {
        mRatioEnum = ratioEnum;
        if (mDrawingInterface != null) {
            mDrawingInterface.setRatio(ratioEnum);
        }
    }

    @Override
    public void setVolume(float l, float r) {
        if (!isUsable()){
            return;
        }
        mPlayer.setVolume(l,r);
    }

    @Override
    public void seekTo(long to) {
        if (!isUsable())
            return;
        mPlayer.seekTo(to);
    }

    @Override
    public long getDuration() {
        return isUsable() ? mPlayer.getDuration() : 0;
    }

    @Override
    public long getCurrentPosition() {
        return isUsable() ? mPlayer.getCurrentPosition() : 0;
    }

    @Override
    public long getTcpSpeed() {
        return isUsable() ? mPlayer.getTcpSpeed() : -1;
    }

    @Override
    public void setFocus(boolean focus) {
        isFocus = focus;
        /** 设置了取消并且如果以获取则释放 */
        if (!isFocus()){

        }
    }

    @Override
    public boolean isFocus() {
        return isFocus;
    }

    @Override
    public void setLooping(boolean looping) {
        isLooping = looping;
    }

    @Override
    public boolean isLooping() {
        return isLooping;
    }

    @Override
    public void setVoice(boolean voice) {
        isVoice = voice;
        float volume = isVoice ? 0.0f : 1.0f;
        setVolume(volume,volume);
    }

    @Override
    public boolean isVoice() {
        return isVoice;
    }

    @Override
    public void setFullScreen(boolean fullScreen) {
        isFullScreen = fullScreen;
    }

    @Override
    public boolean isFullScreen() {
        return isFullScreen;
    }

    @Override
    public void setControllerView(@NonNull LwjControllerBaseView controllerBaseView) {
        removeControllerView();
        mControllerView = controllerBaseView;
        if (mControllerView != null){
            LayoutParams params = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            mFrameLayout.addView(mControllerView, params);
            mControllerView.bindPlayer(this);
        }
    }

    @Override
    public void addStatusChangeListener(@NonNull LwjStatusChangeListener listener) {
        statusChangeListener = listener;
    }

    @Override
    public Bitmap screenCapture() {
        return mDrawingInterface == null ? null : mDrawingInterface.screenCapture();
    }

    @Override
    public boolean isLive() {
        if (TextUtils.isEmpty(mDataSource) || playerCore == MEDIA_PLAYER)
            return false;
        if (mDataSource.startsWith("rtmp") || mDataSource.startsWith("rtsp") || mDataSource.endsWith(".mov") || mDataSource.endsWith(".m3u8"))
            return true;
        else
            return false;
    }

    @Override
    public boolean isPlayer() {
        return mPlayer != null && mPlayer.isPlaying();
    }

    @Override
    public void onStart() {

        if (TextUtils.isEmpty(mDataSource)){
            Log.e(TAG, "onStart: url not null!!!");
            return;
        }

        switch (mStatusEnum){
            case STATUS_IDLE:
                initPlayer();
                mFrameLayout.setKeepScreenOn(true);
                changeStatus(LwjStatusEnum.STATUS_PREPARING);
                break;
            case STATUS_PAUSED:
            case STATUS_PLAYING:
            case STATUS_PREPARING:
                initAudioManager();
                if (mCurrentPosition > 0)
                    mPlayer.seekTo(mCurrentPosition);
                mPlayer.onStart();
                changeStatus(LwjStatusEnum.STATUS_PLAYING);
                mFrameLayout.setKeepScreenOn(true);
                break;
        }
    }

    @Override
    public void onPause() {
        if (!isUsable())
            return;

        if (mStatusEnum != LwjStatusEnum.STATUS_PLAYING && !isPlayer())
            return;

        mCurrentPosition = mPlayer.getCurrentPosition();
        mPlayer.onPause();
        changeStatus(LwjStatusEnum.STATUS_PAUSED);
        releaseAudioManager();
        mFrameLayout.setKeepScreenOn(false);
    }

    @Override
    public void onRelease() {
        if (mStatusEnum == LwjStatusEnum.STATUS_IDLE)
            return;
        /** 先暂停播放器 */
        onPause();
        /** 释放播放器 */
        if (mPlayer != null) {
            mPlayer.onStop();
            mPlayer.onRelease();
            mPlayer = null;
        }
        /** 释放TextureView */
        removeTextureView();
        /** 关闭AudioFocus监听 */
        releaseAudioManager();
        /** 关闭屏幕常亮 */
        mFrameLayout.setKeepScreenOn(false);
        /** 重置播放进度 */
        mCurrentPosition = 0;
        /** 切换转态 */
        changeStatus(LwjStatusEnum.STATUS_IDLE);
    }

    /* ---- 内部方法 ---- */

    /**
     * 播放器是否可用
     * @return
     */
    private boolean isUsable(){
        return mPlayer != null;
    }

    /**
     * 初始化播放器核心
     */
    private void initPlayer(){
        if (MEDIA_PLAYER_IJK == playerCore){
            mPlayer = new LwjIjkPlayer();
        }else {
            mPlayer = new LwjMediaPlayer();
        }

        mPlayer.addPlayerListener(mPlayerListener);
        mPlayer.init(getContext());
        mPlayer.setLooping(isLooping);
        mPlayer.setDataSource(mDataSource);
        if (isVoice)
            setVoice(true);
        mPlayer.prepare();
    }

    /**
     *
     */
    private void initTextureView(){

        removeTextureView();

        if (MEDIA_PLAYER_IJK == playerCore){
            mDrawingInterface = new LwjTextureView(getContext());
        }else {
            mDrawingInterface = new LwjSurfaceView(getContext());
        }

        mDrawingInterface.attach(mPlayer);
        mDrawingInterface.setRatio(mRatioEnum);
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        mFrameLayout.addView(mDrawingInterface.getView(), 0, params);
    }

    /**
     *
     */
    private void removeTextureView(){
        if (mDrawingInterface != null) {
            mFrameLayout.removeView(mDrawingInterface.getView());
            mDrawingInterface.release();
            mDrawingInterface = null;
        }
    }

    /**
     * 删除已添加的控制器
     */
    private void removeControllerView(){
        if (mControllerView != null) {
            mFrameLayout.removeView(mControllerView);
            mControllerView = null;
        }
    }

    /**
     * 播放器状态改变了
     * @param statusEnum
     */
    private void changeStatus(@NonNull LwjStatusEnum statusEnum){
        mStatusEnum = statusEnum;
        if (mControllerView != null)
            mControllerView.changeStatus(mStatusEnum);
        if (statusChangeListener != null)
            statusChangeListener.onChangeStatus(mStatusEnum);
    }

    /**
     * 初始化音频焦点
     */
    private void initAudioManager(){
        /** 已禁用则不获取 */
        if (!isFocus())
            return;
        if (mFocusManager == null) {
            mFocusManager = new LwjAudioFocusManager(getContext(), mFocusListener);
        }
        mFocusManager.requestFocus();
    }

    /**
     * 释放音频焦点
     */
    private void releaseAudioManager(){
        if (mFocusManager != null){
            mFocusManager.abandonFocus();
            mFocusManager = null;
        }
    }

    /* ---- 回调 ---- */

    /**
     * 音频焦点回调
     */
    private LwjAudioFocusManager.LwjAudioFocusListener mFocusListener = new LwjAudioFocusManager.LwjAudioFocusListener() {
        @Override
        public void onAcquire() {
            post(new Runnable() {
                @Override
                public void run() {
                    onStart();
                }
            });
            /**
             * 已禁音则不恢复音量
             */
            if (isVoice){
                setVolume(1,1);
            }
        }

        @Override
        public void onLose() {
            post(new Runnable() {
                @Override
                public void run() {
                    onPause();
                }
            });
        }

        @Override
        public void onFlat() {
            setVolume(0.1f, 0.1f);
        }
    };

    /**
     * 播放器回调
     */
    private LwjPlayerListener mPlayerListener = new LwjPlayerListener() {
        @Override
        public void onPreparedEnd() {
            initTextureView();
            initAudioManager();
            onStart();
            changeStatus(LwjStatusEnum.STATUS_PLAYING);
        }

        @Override
        public void onBuffering(int buffer, long speed) {
            if (mControllerView != null)
                mControllerView.bufferUpdate(buffer);
        }

        @Override
        public void onInfo(int what, int extra) {
            switch (what) {
                case MEDIA_INFO_BUFFERING_START://缓冲
                    changeStatus(LwjStatusEnum.STATUS_BUFFERING);
                    break;
                case MEDIA_INFO_BUFFERING_END://缓冲结束
                    changeStatus(LwjStatusEnum.STATUS_BUFFEEND);
                    break;
                case MEDIA_INFO_VIDEO_RENDERING_START: // 视频开始渲染
                    changeStatus(LwjStatusEnum.STATUS_PLAYING);
                    if (getWindowVisibility() != VISIBLE)
                        onPause();
                    break;
                case MEDIA_INFO_VIDEO_ROTATION_CHANGED:
                    if (mDrawingInterface != null)
                        mDrawingInterface.setRotationDegree(extra);
                    break;
            }
        }

        @Override
        public void onSizeChanged(int width, int height) {
            if (mDrawingInterface != null) {
                mDrawingInterface.setVideoSize(width, height);
            }
        }

        @Override
        public void onError() {
            changeStatus(LwjStatusEnum.STATUS_ERROR);
            setKeepScreenOn(false);
        }

        @Override
        public void onCompletion() {
            mCurrentPosition = 0;
            setKeepScreenOn(false);
            changeStatus(LwjStatusEnum.STATUS_COMPLETED);
        }
    };
}