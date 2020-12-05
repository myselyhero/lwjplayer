package com.yongyong.lwj.lwjplayer.engine;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.Surface;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;

import com.yongyong.lwj.lwjplayer.LwjSpeedLevelEnum;

import java.io.IOException;

/**
 * @author yongyong
 * 
 * @desc:系统播放器
 * 
 * @// TODO: 2020/12/4
 */
public class LwjMediaPlayer extends LwjPlayerBase {

    /**
     * 上下文
     */
    private Context mContext;

    /**
     * 播放器实例
     */
    private MediaPlayer mMediaPlayer;

    /**
     * 监听
     */
    private LwjPlayerListener mPlayerListener;

    @Override
    public void init(Context context) {
        mContext = context;
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        /** 播放时屏幕常亮 */
        //mMediaPlayer.setScreenOnWhilePlaying(true);

        mMediaPlayer.setOnBufferingUpdateListener(bufferingUpdateListener);
        mMediaPlayer.setOnCompletionListener(completionListener);
        mMediaPlayer.setOnErrorListener(errorListener);
        mMediaPlayer.setOnInfoListener(infoListener);
        mMediaPlayer.setOnPreparedListener(preparedListener);
        mMediaPlayer.setOnVideoSizeChangedListener(videoSizeChangedListener);
    }

    @Override
    public void setDataSource(String url) {
        if (mMediaPlayer == null || TextUtils.isEmpty(url))
            return;
        try {
            mMediaPlayer.setDataSource(mContext, Uri.parse(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void prepare() {
        if (mMediaPlayer == null)
            return;
        mMediaPlayer.prepareAsync();
    }

    @Override
    public void onStart() {
        if (mMediaPlayer == null || mMediaPlayer.isPlaying())
            return;
        mMediaPlayer.start();
    }

    @Override
    public void onPause() {
        if (mMediaPlayer == null || !mMediaPlayer.isPlaying())
            return;
        mMediaPlayer.pause();
    }

    @Override
    public void onStop() {
        if (mMediaPlayer == null || !mMediaPlayer.isPlaying())
            return;
        onPause();
        mMediaPlayer.stop();
    }

    @Override
    public void onReset() {
        if (mMediaPlayer == null)
            return;
        if (mMediaPlayer.isPlaying()){
            onStop();
        }
        mMediaPlayer.setSurface(null);
        mMediaPlayer.setDisplay(null);
        mMediaPlayer.reset();
    }

    @Override
    public void onRelease() {
        if (mMediaPlayer == null)
            return;
        if (mMediaPlayer.isPlaying()){
            onStop();
        }
        mMediaPlayer.setOnErrorListener(null);
        mMediaPlayer.setOnCompletionListener(null);
        mMediaPlayer.setOnInfoListener(null);
        mMediaPlayer.setOnBufferingUpdateListener(null);
        mMediaPlayer.setOnPreparedListener(null);
        mMediaPlayer.setOnVideoSizeChangedListener(null);
        new Thread() {
            @Override
            public void run() {
                try {
                    mMediaPlayer.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayer != null && mMediaPlayer.isPlaying();
    }

    @Override
    public void seekTo(long time) {
        if (mMediaPlayer == null)
            return;
        mMediaPlayer.seekTo((int) time);
    }

    @Override
    public long getCurrentPosition() {
        return mMediaPlayer != null ? mMediaPlayer.getCurrentPosition() : 0;
    }

    @Override
    public long getDuration() {
        return mMediaPlayer != null ? mMediaPlayer.getDuration() : 0;
    }

    @Override
    public void setSurface(Surface surface) {
        if (mMediaPlayer != null)
            mMediaPlayer.setSurface(surface);
    }

    @Override
    public void setDisplay(SurfaceHolder holder) {
        if (mMediaPlayer != null)
            mMediaPlayer.setDisplay(holder);
    }

    @Override
    public void setVolume(float v1, float v2) {
        if (mMediaPlayer != null)
            mMediaPlayer.setVolume(v1,v2);
    }

    @Override
    public void setLooping(boolean isLooping) {
        if (mMediaPlayer != null)
            mMediaPlayer.setLooping(isLooping);
    }

    @Override
    public boolean isLooping() {
        return mMediaPlayer != null && mMediaPlayer.isLooping();
    }

    @Override
    public void setSpeed(LwjSpeedLevelEnum speed) {
        if (mMediaPlayer == null)
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PlaybackParams params = mMediaPlayer.getPlaybackParams();
            params.setSpeed(speed.getSpeed());
            mMediaPlayer.setPlaybackParams(params);
        }
    }

    @Override
    public int[] getVideoSize() {
        if (mMediaPlayer == null)
            return new int[]{0,0};
        return new int[]{mMediaPlayer.getVideoWidth(),mMediaPlayer.getVideoHeight()};
    }

    @Override
    public void addPlayerListener(@NonNull LwjPlayerListener listener) {
        mPlayerListener = listener;
    }

    /* 播放器监听 */

    /**
     * 准备监听
     */
    private MediaPlayer.OnPreparedListener preparedListener = mp -> {
        if (mPlayerListener != null)
            mPlayerListener.onPreparedEnd();
    };

    /**
     * 缓存监听
     */
    private MediaPlayer.OnBufferingUpdateListener bufferingUpdateListener = (mp, percent) -> {
        if (mPlayerListener != null)
            mPlayerListener.onBuffering(percent,-1);
    };

    /**
     * 加载、渲染监听
     */
    private MediaPlayer.OnInfoListener infoListener = (mp, what, extra) -> {
        if (mPlayerListener != null)
            mPlayerListener.onInfo(what,extra);
        return false;
    };

    /**
     * 错误监听
     */
    private MediaPlayer.OnErrorListener errorListener = (mp, what, extra) -> {
        if (what != -38) {
            if (mPlayerListener != null)
                mPlayerListener.onError();
            return false;
        }else {
            return true;
        }
    };

    /**
     * 播放结束
     */
    private MediaPlayer.OnCompletionListener completionListener = mp -> {
        if (mPlayerListener != null)
            mPlayerListener.onCompletion();
    };

    /**
     * 视频大小监听
     */
    private MediaPlayer.OnVideoSizeChangedListener videoSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            if (mPlayerListener != null)
                mPlayerListener.onSizeChanged(mp.getVideoWidth(),mp.getVideoHeight());
        }
    };
}
