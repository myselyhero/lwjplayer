package com.yongyong.lwj.lwjplayer.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.yongyong.lwj.lwjplayer.LwjStatusEnum;
import com.yongyong.lwj.lwjplayer.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author yongyong
 *
 * @email 1947920597@qq.com
 *
 * desc:该控制器在本人项目中实际引用
 * 
 * @// TODO: 2020/10/23
 */
public class LwjPlayerChatControllerView extends LwjPlayerControllerBaseView implements View.OnClickListener {

    private static final String TAG = "LwjPlayerChatController";

    private ImageView mThumbnailImageView;
    private ImageView mPlayerButtonImageView;

    private LinearLayout mControllerBackground;
    private TextView mCurrentTextView;
    private SeekBar mSeekBar;
    private TextView mDurationTextView;

    private RelativeLayout mFunctionBackground;
    private ImageView mBacktrackImageView;
    private ImageView mShareImageView;
    private ImageView mCollectImageView;
    private ImageView downloadImageView;

    /**  */
    public static final int LWJ_PLAYER_CHAT_CONTROLLER_BACKTRACK = 0;
    public static final int LWJ_PLAYER_CHAT_CONTROLLER_SHARE = 1;
    public static final int LWJ_PLAYER_CHAT_CONTROLLER_COLLECT = 2;
    public static final int LWJ_PLAYER_CHAT_CONTROLLER_DOWNLOAD = 3;
    private OnLwjPlayerChatControllerListener controllerListener;

    public LwjPlayerChatControllerView(@NonNull Context context) {
        super(context);
    }

    public LwjPlayerChatControllerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LwjPlayerChatControllerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.lwj_chat_controller_view;
    }

    @Override
    protected void initView() {
        mThumbnailImageView = findViewById(R.id.lwj_player_chat_thumbnail);
        mPlayerButtonImageView = findViewById(R.id.lwj_player_chat_btn);
        mControllerBackground = findViewById(R.id.lwj_player_chat_controller_background);
        mCurrentTextView = findViewById(R.id.lwj_player_chat_controller_current);
        mSeekBar = findViewById(R.id.lwj_player_chat_controller_seek);
        mDurationTextView = findViewById(R.id.lwj_player_chat_controller_duration);
        mFunctionBackground = findViewById(R.id.lwj_player_chat_function_background);
        mBacktrackImageView = findViewById(R.id.lwj_player_chat_function_backtrack);
        mShareImageView = findViewById(R.id.lwj_player_chat_function_share);
        mCollectImageView = findViewById(R.id.lwj_player_chat_function_collect);
        downloadImageView = findViewById(R.id.lwj_player_chat_function_download);

        mPlayerButtonImageView.setOnClickListener(this);
        mBacktrackImageView.setOnClickListener(this);
        mShareImageView.setOnClickListener(this);
        mCollectImageView.setOnClickListener(this);
        downloadImageView.setOnClickListener(this);

        /**
         *
         */
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCurrentTextView.setText(longTimeToString(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                /**
                 *不能滑动到最后
                 */
                int maxCanSeekTo = seekBar.getMax() - 5 * 1000;
                int position;
                if (seekBar.getProgress() < maxCanSeekTo) {
                    position = seekBar.getProgress();
                } else {
                    position = maxCanSeekTo;
                }
                mLwjPlayer.seekTo(position);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.lwj_player_chat_btn) {
            /**
             * 播放暂停
             */
            if (mLwjPlayer.isPlayer()){
                mLwjPlayer.onPause();
            }else {
                mLwjPlayer.onStart();
            }
        }else if (v.getId() == R.id.lwj_player_chat_function_backtrack){
            if (controllerListener != null)
                controllerListener.onCall(LWJ_PLAYER_CHAT_CONTROLLER_BACKTRACK);
        }else if (v.getId() == R.id.lwj_player_chat_function_share){
            if (controllerListener != null)
                controllerListener.onCall(LWJ_PLAYER_CHAT_CONTROLLER_SHARE);
        }else if (v.getId() == R.id.lwj_player_chat_function_collect){
            if (controllerListener != null)
                controllerListener.onCall(LWJ_PLAYER_CHAT_CONTROLLER_COLLECT);
        }else if (v.getId() == R.id.lwj_player_chat_function_download){
            if (controllerListener != null)
                controllerListener.onCall(LWJ_PLAYER_CHAT_CONTROLLER_DOWNLOAD);
        }
    }

    @Override
    protected void onClick() {
        startController();
    }

    @Override
    protected void onDblClick() {
        if (mLwjPlayer.isPlayer()){
            mLwjPlayer.onPause();
        }else {
            mLwjPlayer.onStart();
        }
    }

    @Override
    public void bufferUpdate(int buffering) {
        //解决缓冲进度不能100%问题
        if (buffering >= 95) {
            mSeekBar.setSecondaryProgress((int) mLwjPlayer.getDuration());
        } else {
            mSeekBar.setSecondaryProgress(buffering * 10);
        }
    }

    @Override
    public void changeStatus(LwjStatusEnum statusEnum) {
        //startController();
        switch (statusEnum){
            case STATUS_PLAYING:
                mDurationTextView.setText(longTimeToString(mLwjPlayer.getDuration()));
                mSeekBar.setMax((int) mLwjPlayer.getDuration());
                startProgress();
                mThumbnailImageView.setVisibility(View.GONE);
                mPlayerButtonImageView.setVisibility(View.GONE);
                break;
            case STATUS_BUFFERING:

                break;
            case STATUS_BUFFEEND:

                break;
            case STATUS_PAUSED:
            case STATUS_IDLE:
                mPlayerButtonImageView.setVisibility(View.VISIBLE);
                break;
            case STATUS_COMPLETED:
                mThumbnailImageView.setVisibility(View.VISIBLE);
                break;
            case STATUS_PREPARING:
                //thumbnailImageView.setVisibility(View.GONE);
                break;
            case STATUS_ERROR:
                Log.e(TAG, "changeStatus: 呀 发生错误了");
                break;
        }
    }

    /**
     *
     * @param controllerListener
     */
    public void setControllerListener(OnLwjPlayerChatControllerListener controllerListener) {
        this.controllerListener = controllerListener;
    }

    /**
     * 设置缩略图
     * @param path
     */
    public void setThumbnail(String path){
        if (TextUtils.isEmpty(path))
            return;
        Glide.with(getContext())
                .asBitmap()
                .load(path)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(mThumbnailImageView);
    }

    /**
     *
     */
    private void startController(){
        if (isVisible){
            stopController();
            stopProgress();
        }else {
            showControllerAnim(mControllerBackground);
            showControllerAnim(mFunctionBackground);
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    stopController();
                }
            };
            mHandler.postDelayed(mRunnable,mControllerTime);
            startProgress();
            isVisible = true;
        }
    }

    /**
     *
     */
    private void stopController(){
        if (mHandler != null){
            mHandler.removeCallbacks(mRunnable);
            mRunnable = null;
        }
        hideControllerAnim(mControllerBackground);
        hideControllerAnim(mFunctionBackground);
        isVisible = false;
    }

    /**
     * 开始计时刷新进度s
     */
    private void startProgress(){
        stopProgress();
        progressTimer = new Timer();
        progressTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                post(new Runnable() {
                    @Override
                    public void run() {
                        mSeekBar.setProgress((int) mLwjPlayer.getCurrentPosition());
                        mCurrentTextView.setText(longTimeToString(mLwjPlayer.getCurrentPosition()));
                    }
                });
            }
        },0,progressDefault);
    }

    /**
     * 停止刷新进度的线程
     */
    private void stopProgress(){
        if (progressTimer != null){
            progressTimer.cancel();
            progressTimer = null;
        }
    }

    /**
     *
     */
    public interface OnLwjPlayerChatControllerListener {

        /**
         *
         * @param code
         */
        void onCall(int code);
    }
}
