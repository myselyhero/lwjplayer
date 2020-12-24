package com.yongyong.lwj.lwjplayer.engine;

import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;

import com.yongyong.lwj.lwjplayer.LwjSpeedLevelEnum;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * @author yongyong
 *
 * @mail 1947920597@qq.com
 *
 * desc:播放器
 *
 * @// TODO: 2020/10/17
 */
public class LwjIjkPlayer extends LwjPlayerBase {

    /**
     * 上下文
     */
    private Context mContext;

    /**
     * 播放器实例
     */
    private IjkMediaPlayer mIjkPlayer;

    /**
     * 监听
     */
    private LwjPlayerListener mPlayerListener;

    /**
     * 初始化播放器
     * @param context
     */
    @Override
    public void init(Context context) {
        mContext = context;

        mIjkPlayer = new IjkMediaPlayer();
        /*mIjkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "dns_cache_clear", 1);
        mIjkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT,"analyzeduration",1);
        mIjkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT,"probesize",1024*10);*/

        mIjkPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mIjkPlayer.setOnErrorListener(errorListener);
        mIjkPlayer.setOnCompletionListener(completionListener);
        mIjkPlayer.setOnInfoListener(infoListener);
        mIjkPlayer.setOnBufferingUpdateListener(updateListener);
        mIjkPlayer.setOnPreparedListener(preparedListener);
        mIjkPlayer.setOnVideoSizeChangedListener(videoSizeChangedListener);
        mIjkPlayer.setOnNativeInvokeListener(nativeInvokeListener);

        /**
         * 设置日志级别
         */
        IjkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_ERROR);
    }

    /**
     * 设置直播拉流优化项
     */
    @Override
    public void setOptions() {
        /*if (mIjkPlayer == null)
            return;

        mIjkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-hevc", 1);
        // 设置播放前的探测时间 1,达到首屏秒开效果
        mIjkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzeduration", 1);
        // 如果是rtsp协议，可以优先用tcp(默认是用udp)
        mIjkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "rtsp_transport", "tcp");
        // 设置播放前的最大探测时间 （100未测试是否是最佳值）
        mIjkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzemaxduration", 100L);
        // 每处理一个packet之后刷新io上下文
        mIjkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "flush_packets", 1L);
        // 需要准备好后自动播放
        mIjkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 1);
        // 不额外优化（使能非规范兼容优化，默认值0 ）
        mIjkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "fast", 1);
        // 是否开启预缓冲，一般直播项目会开启，达到秒开的效果，不过带来了播放丢帧卡顿的体验
        mIjkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", 0);
        // 自动旋屏
        mIjkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 0);
        // 处理分辨率变化
        mIjkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 0);
        // 最大缓冲大小,单位kb
        mIjkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "max-buffer-size", 0);
        // 默认最小帧数2
        mIjkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "min-frames", 2);
        // 最大缓存时长
        mIjkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max_cached_duration", 3); //300
        // 是否限制输入缓存数
        mIjkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "infbuf", 1);
        // 缩短播放的rtmp视频延迟在1s内
        mIjkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "fflags", "nobuffer");
        // 播放前的探测Size，默认是1M, 改小一点会出画面更快
        mIjkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "probesize", 200); //1024L)
        // 播放重连次数
        mIjkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "reconnect", 3);
        // TODO:
        mIjkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);
        // 设置是否开启环路过滤: 0开启，画面质量高，解码开销大，48关闭，画面质量差点，解码开销小
        mIjkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48L);
        // 跳过帧 ？？
        mIjkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_frame", 0);
        // 视频帧处理不过来的时候丢弃一些帧达到同步的效果
        mIjkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 5);
        //最大fps
        mIjkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER,"max-fps",30);*/

    }

    /**
     * 设置播放源
     * @param url 播放地址
     */
    @Override
    public void setDataSource(String url) {
        if (!isUsable())
            return;
        try {
            mIjkPlayer.setDataSource(mContext, Uri.parse(url));
        } catch (Exception e) {
            if (mPlayerListener != null)
                mPlayerListener.onError();
        }
    }

    /**
     * 准备
     */
    @Override
    public void prepare() {
        if (!isUsable())
            return;
        try {
            mIjkPlayer.prepareAsync();
        } catch (IllegalStateException e) {
            if (mPlayerListener != null)
                mPlayerListener.onError();
        }
    }

    /**
     * 开始播放
     */
    @Override
    public void onStart() {
        if (!isUsable())
            return;
        try {
            mIjkPlayer.start();
        } catch (IllegalStateException e) {
            if (mPlayerListener != null)
                mPlayerListener.onError();
        }
    }

    /**
     * 暂停
     */
    @Override
    public void onPause() {
        if (!isUsable())
            return;
        try {
            mIjkPlayer.pause();
        } catch (IllegalStateException e) {
            if (mPlayerListener != null)
                mPlayerListener.onError();
        }
    }

    /**
     * 停止
     */
    @Override
    public void onStop() {
        if (!isUsable())
            return;
        try {
            mIjkPlayer.stop();
        } catch (IllegalStateException e) {
            if (mPlayerListener != null)
                mPlayerListener.onError();
        }
    }

    /**
     * 重置播放器
     */
    @Override
    public void onReset() {
        if (!isUsable())
            return;
        mIjkPlayer.reset();
    }

    /**
     * 释放
     */
    @Override
    public void onRelease() {
        if (!isUsable())
            return;

        mIjkPlayer.setOnErrorListener(null);
        mIjkPlayer.setOnCompletionListener(null);
        mIjkPlayer.setOnInfoListener(null);
        mIjkPlayer.setOnBufferingUpdateListener(null);
        mIjkPlayer.setOnPreparedListener(null);
        mIjkPlayer.setOnVideoSizeChangedListener(null);
        new Thread() {
            @Override
            public void run() {
                try {
                    mIjkPlayer.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 是否正在播放
     * @return
     */
    @Override
    public boolean isPlaying() {
        return isUsable() && mIjkPlayer.isPlaying();
    }

    /**
     *
     * @param time 调整进度
     */
    @Override
    public void seekTo(long time) {
        if (!isUsable())
            return;
        try {
            mIjkPlayer.seekTo((int) time);
        } catch (IllegalStateException e) {
            if (mPlayerListener != null)
                mPlayerListener.onError();
        }
    }

    /**
     * 获取当前播放进度
     * @return
     */
    @Override
    public long getCurrentPosition() {
        return isUsable() ? mIjkPlayer.getCurrentPosition() : 0;
    }

    /**
     * 总时长
     * @return
     */
    @Override
    public long getDuration() {
        return isUsable() ? mIjkPlayer.getDuration() : 0;
    }

    /**
     * 渲染视图
     * @param surface 渲染
     */
    @Override
    public void setSurface(Surface surface) {
        if (!isUsable())
            return;
        mIjkPlayer.setSurface(surface);
    }

    @Override
    public void setDisplay(SurfaceHolder holder) {
        if (!isUsable())
            return;
        mIjkPlayer.setDisplay(holder);
    }

    /**
     * 音量
     * @param v1 左声道
     * @param v2 右声道
     */
    @Override
    public void setVolume(float v1, float v2) {
        if (!isUsable())
            return;
        mIjkPlayer.setVolume(v1, v2);
    }

    /**
     * 循环播放
     * @param isLooping
     */
    @Override
    public void setLooping(boolean isLooping) {
        if (!isUsable())
            return;
        mIjkPlayer.setLooping(isLooping);
    }

    /**
     * 是否循环播放
     * @return
     */
    @Override
    public boolean isLooping() {
        return isUsable() && mIjkPlayer.isLooping();
    }

    /**
     * 设置播放速度
     * @param speed
     */
    @Override
    public void setSpeed(LwjSpeedLevelEnum speed) {
        if (!isUsable())
            return;
        mIjkPlayer.setSpeed(speed.getSpeed());
    }

    @Override
    public long getTcpSpeed() {
        return mIjkPlayer != null ? mIjkPlayer.getTcpSpeed() : -1;
    }

    /**
     * 获取视频宽高
     * @return
     */
    @Override
    public int[] getVideoSize() {
        if (!isUsable())
            return new int[]{0,0};
        return new int[]{mIjkPlayer.getVideoWidth(),mIjkPlayer.getVideoHeight()};
    }

    /**
     * 设置监听
     * @param listener
     */
    @Override
    public void addPlayerListener(@NonNull LwjPlayerListener listener) {
        mPlayerListener = listener;
    }

    /**
     * 是否可用
     * @return
     */
    private boolean isUsable(){
        return mIjkPlayer != null;
    }

    /* 播放器监听 */

    /**
     * 准备结束
     */
    private IjkMediaPlayer.OnPreparedListener preparedListener = iMediaPlayer -> {
        if (mPlayerListener != null)
            mPlayerListener.onPreparedEnd();
    };

    /**
     *
     */
    private IjkMediaPlayer.OnInfoListener infoListener = (iMediaPlayer, i, i1) -> {
        if (mPlayerListener != null)
            mPlayerListener.onInfo(i,i1);
        return false;
    };

    /**
     * 缓冲监听
     */
    private IjkMediaPlayer.OnBufferingUpdateListener updateListener = (iMediaPlayer, i) -> {
        if (mPlayerListener != null)
            mPlayerListener.onBuffering(i,isUsable() ? mIjkPlayer.getTcpSpeed() : 0);
    };

    /**
     * 宽高
     */
    private IjkMediaPlayer.OnVideoSizeChangedListener videoSizeChangedListener = (iMediaPlayer, i, i1, i2, i3) -> {
        if (mPlayerListener != null)
            mPlayerListener.onSizeChanged(mIjkPlayer.getVideoWidth(),mIjkPlayer.getVideoHeight());
    };

    /**
     * 错误监听
     */
    private IjkMediaPlayer.OnErrorListener errorListener = (iMediaPlayer, i, i1) -> {
        if (mPlayerListener != null)
            mPlayerListener.onError();
        return false;
    };

    /**
     *
     */
    private IjkMediaPlayer.OnNativeInvokeListener nativeInvokeListener = (i, bundle) -> {
        return true;
    };

    /**
     * 播放结束
     */
    private IjkMediaPlayer.OnCompletionListener completionListener = iMediaPlayer -> {
        if (mPlayerListener != null)
            mPlayerListener.onCompletion();
    };
}
