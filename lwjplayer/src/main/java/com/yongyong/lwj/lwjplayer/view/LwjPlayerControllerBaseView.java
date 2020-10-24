package com.yongyong.lwj.lwjplayer.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yongyong.lwj.lwjplayer.LwjPlayerView;
import com.yongyong.lwj.lwjplayer.LwjStatusEnum;

import java.util.Timer;

/**
 * @author yongyong
 *
 * @mail 1947920597@qq.com
 *
 * desc:控制器父类
 *
 * @// TODO: 2020/10/17
 */
public abstract class LwjPlayerControllerBaseView extends FrameLayout implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener,
        View.OnTouchListener{

    private String TAG = "LwjPlayerControllerBaseView";

    /**  */
    protected Activity mActivity;

    /** 是否锁定 */
    protected boolean isLock;

    /** 控制器是否可见 */
    protected boolean isVisible;

    /** 当前音量 */
    protected int mVolume;

    /** 多长时间后隐藏控制器 */
    protected int mControllerTime = 3000;

    /** 音频 */
    private AudioManager mAudioManager;

    /** 播放器实例 */
    protected LwjPlayerView mLwjPlayer;

    /** 刷新进度 */
    protected int progressDefault = 1000;
    protected Timer progressTimer;

    /**  */
    private GestureDetector mGestureDetector;
    /** 是否关闭手势 */
    protected boolean isGesture;

    /** 控制器隐藏线程 */
    protected Handler mHandler = new Handler(Looper.getMainLooper());
    protected Runnable mRunnable;

    public LwjPlayerControllerBaseView(@NonNull Context context) {
        super(context);
        init();
    }

    public LwjPlayerControllerBaseView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LwjPlayerControllerBaseView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     *
     */
    private void init(){
        mActivity = (Activity) getContext();

        mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        mGestureDetector = new GestureDetector(getContext(), this);
        setOnTouchListener(this);

        if (getLayoutId() != 0){
            LayoutInflater.from(getContext()).inflate(getLayoutId(),this);
            initView();
        }
    }



    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        onClick();
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        onDblClick();
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        if (isGesture)
            return false;
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mGestureDetector.onTouchEvent(event) && event.getAction() == MotionEvent.ACTION_UP) {

        }
        return super.onTouchEvent(event);
    }

    /**
     * 获取布局ID
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化视图
     */
    protected abstract void initView();


    /**
     * 绑定播放器
     * @param playerView
     */
    public void bindPlayer(@NonNull LwjPlayerView playerView){
        mLwjPlayer = playerView;
    }

    /**
     * 单击
     */
    protected abstract void onClick();

    /**
     * 双击
     */
    protected abstract void onDblClick();

    /**
     * 更新缓冲值
     * @param buffering
     */
    public abstract void bufferUpdate(int buffering);

    /**
     * 改变播放器状态
     * @param statusEnum
     */
    public abstract void changeStatus(LwjStatusEnum statusEnum);

    /**
     * 转换毫秒数成“分、秒”，如“01:53”。若超过60分钟则显示“时、分、秒”，如“01:01:30
     * @param time
     * @return
     */
    protected String longTimeToString(long time) {
        int second = 1000;
        int minute = second * 60;
        int hour = minute * 60;

        long hourTime = (time) / hour;
        long minuteTime = (time - hourTime * hour) / minute;
        long secondTime = (time - hourTime * hour - minuteTime * minute) / second;

        String strHour = hourTime < 10 ? "0" + hourTime : "" + hourTime;
        String strMinute = minuteTime < 10 ? "0" + minuteTime : "" + minuteTime;
        String strSecond = secondTime < 10 ? "0" + secondTime : "" + secondTime;
        if (hourTime > 0) {
            return strHour + ":" + strMinute + ":" + strSecond;
        } else {
            return strMinute + ":" + strSecond;
        }
    }

    /**
     *
     */
    protected void showControllerAnim(View view){
        if (view.getVisibility() == View.VISIBLE)
            return;

        view.setVisibility(View.VISIBLE);
        view.animate()
                .alpha(1f)
                .setDuration(500)
                .setListener(null);
    }

    /**
     *
     * @param view
     */
    protected void hideControllerAnim(View view){
        if (view.getVisibility() == View.GONE)
            return;

        view.animate()
                .alpha(0f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                    }
                });
    }
}
