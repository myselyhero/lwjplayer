package com.yongyong.lwj.lwjplayer;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yongyong.lwj.lwjplayer.engine.LwjPlayer;
import com.yongyong.lwj.lwjplayer.engine.LwjPlayerListener;
import com.yongyong.lwj.lwjplayer.util.LwjAudioFocusManager;
import com.yongyong.lwj.lwjplayer.view.LwjPlayerControllerBaseView;
import com.yongyong.lwj.lwjplayer.view.LwjTextureView;

/**
 * @author yongyong
 *
 * @mail 1947920597@qq.com
 *
 * desc:自定义播放器
 * 
 * @// TODO: 2020/10/17
 */
public class LwjPlayerView extends FrameLayout {

    private static final String TAG = "LwjPlayerView";

    /** 开始渲染视频画面 */
    public static final int MEDIA_INFO_VIDEO_RENDERING_START = 3;
    /** 缓冲开始 */
    public static final int MEDIA_INFO_BUFFERING_START = 701;
    /** 缓冲结束 */
    public static final int MEDIA_INFO_BUFFERING_END = 702;
    /** 视频旋转信息 */
    public static final int MEDIA_INFO_VIDEO_ROTATION_CHANGED = 10001;

    /**  */
    private Activity mActivity;

    /** 播放器实例 */
    private LwjPlayer mLwjPlayer;

    /** 渲染 */
    private LwjTextureView mTextureView;

    /** 控制器 */
    private LwjPlayerControllerBaseView mControllerView;

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
    private LwjAudioFocusManager mFocusManager;

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

    /**
     * 设置数据源
     * @param dataSource
     */
    public void setDataSource(String dataSource){
        if (TextUtils.isEmpty(dataSource))
            return;
        mDataSource = dataSource;
    }

    /**
     *
     * @param ratioEnum
     */
    public void setRatio(LwjRatioEnum ratioEnum) {
        mRatioEnum = ratioEnum;
        if (mTextureView != null) {
            mTextureView.setScaleType(ratioEnum);
        }
    }

    /**
     * 设置音量
     * @param l 左声道
     * @param r 右声道
     */
    private void setVolume(float l, float r){
        if (!isUsable()){
            return;
        }
        mLwjPlayer.setVolume(l,r);
    }

    /**
     *
     * @return
     */
    public boolean isPlayer(){
        return mLwjPlayer != null && mLwjPlayer.isPlaying();
    }

    /**
     * 开始播放
     */
    public void onStart(){

        if (TextUtils.isEmpty(mDataSource)){
            Log.e(LwjPlayerView.class.getSimpleName(), "onStart: url not null!!!");
            return;
        }

        switch (mStatusEnum){
            case STATUS_IDLE:
                changeStatus(LwjStatusEnum.STATUS_PREPARING);
                mLwjPlayer = new LwjPlayer();
                mLwjPlayer.addPlayerListener(mPlayerListener);
                mLwjPlayer.initPlayer(getContext());
                mLwjPlayer.setLooping(isLooping);
                mLwjPlayer.setDataSource(mDataSource);
                mLwjPlayer.prepare();
                mFrameLayout.setKeepScreenOn(true);
                onInitTextureView();
                break;
            case STATUS_PAUSED:
            case STATUS_PLAYING:
                onInitAudioManager();
                mLwjPlayer.onStart();
                changeStatus(LwjStatusEnum.STATUS_PLAYING);
                mFrameLayout.setKeepScreenOn(true);
                break;
        }
    }

    /**
     * 暂停
     */
    public void onPause(){
        if (!isUsable())
            return;

        if (mStatusEnum != LwjStatusEnum.STATUS_PLAYING && !isPlayer())
            return;
        mCurrentPosition = mLwjPlayer.getCurrentPosition();
        mLwjPlayer.onPause();
        changeStatus(LwjStatusEnum.STATUS_PAUSED);
        onReleaseAudioManager();
        mFrameLayout.setKeepScreenOn(false);
    }

    /**
     * 释放
     */
    public void onRelease(){

        if (mStatusEnum == LwjStatusEnum.STATUS_IDLE)
            return;
        onPause();
        //释放播放器
        if (mLwjPlayer != null) {
            mLwjPlayer.onRelease();
            mLwjPlayer = null;
        }
        //释放TextureView
        onRemoveTextureView();
        //关闭AudioFocus监听
        onReleaseAudioManager();
        //关闭屏幕常亮
        mFrameLayout.setKeepScreenOn(false);
        //重置播放进度
        mCurrentPosition = 0;
        //切换转态
        changeStatus(LwjStatusEnum.STATUS_IDLE);
    }

    /**
     *
     * @return
     */
    public long getDuration(){
        return isUsable() ? mLwjPlayer.getDuration() : 0;
    }

    /**
     *
     * @param to
     */
    public void seekTo(long to){
        if (!isUsable())
            return;
        mLwjPlayer.seekTo(to);
    }

    /**
     *
     * @return
     */
    public long getCurrentPosition(){
        return isUsable() ? mLwjPlayer.getCurrentPosition() : 0;
    }

    public boolean isLooping() {
        return isLooping;
    }

    public void setLooping(boolean looping) {
        isLooping = looping;
    }

    public boolean isVoice() {
        return isVoice;
    }

    /**
     * 设置是否静音
     * @param voice
     */
    public void setVoice(boolean voice) {
        isVoice = voice;
        float volume = isVoice ? 0.0f : 1.0f;
       setVolume(volume,volume);
    }

    public boolean isFullScreen() {
        return isFullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        isFullScreen = fullScreen;
    }

    /* ---- 内部API ---- */

    /**
     *
     * @return
     */
    private boolean isUsable() {
        return mLwjPlayer != null;
    }

    /**
     *
     */
    private void onInitTextureView(){

        onRemoveTextureView();

        mTextureView = new LwjTextureView(getContext());
        mTextureView.attachToPlayer(mLwjPlayer);
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        mFrameLayout.addView(mTextureView, 0, params);
    }

    /**
     *
     */
    private void onRemoveTextureView(){
        if (mTextureView != null) {
            mFrameLayout.removeView(mTextureView);
            mTextureView.release();
            mTextureView = null;
        }
    }

    /**
     *
     * @param controllerBaseView
     */
    public void setControllerView(@NonNull LwjPlayerControllerBaseView controllerBaseView){
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

    /**
     *
     */
    private void removeControllerView(){
        if (mControllerView != null) {
            mFrameLayout.removeView(mControllerView);
            mControllerView = null;
        }
    }

    /**
     *
     * @param statusEnum
     */
    private void changeStatus(@NonNull LwjStatusEnum statusEnum){
        mStatusEnum = statusEnum;
        if (mControllerView != null)
            mControllerView.changeStatus(mStatusEnum);
    }

    /**
     * 初始化音频焦点
     */
    private void onInitAudioManager(){
        if (mFocusManager == null) {
            mFocusManager = new LwjAudioFocusManager(getContext(), mFocusListener);
        }
        mFocusManager.requestFocus();
    }

    /**
     * 释放音频焦点
     */
    private void onReleaseAudioManager(){
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
            onStart();
            /**
             * 已禁音则不恢复音量
             */
            if (isVoice){
                setVolume(1,1);
            }
        }

        @Override
        public void onLose() {
            onPause();
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
            onInitAudioManager();
            mLwjPlayer.onStart();
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
                    if (mTextureView != null)
                        mTextureView.setRotationDegree(extra);
                    break;
            }
        }

        @Override
        public void onSizeChanged(int width, int height) {
            if (mTextureView != null) {
                mTextureView.setScaleType(mRatioEnum);
                mTextureView.setVideoSize(width, height);
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
