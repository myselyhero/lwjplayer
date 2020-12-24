package com.yongyong.lwj.lwjplayer.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
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
import com.yongyong.lwj.lwjplayer.LwjStatusView;
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
public class LwjChatControllerView extends LwjControllerBaseView implements View.OnClickListener {

    private static final String TAG = "LwjPlayerChatController";

    private ImageView mThumbnailImageView;

    private LwjStatusView statusView;

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
    private OnLwjPlayerControllerListener controllerListener;

    public LwjChatControllerView(@NonNull Context context) {
        super(context);
    }

    public LwjChatControllerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LwjChatControllerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.lwj_chat_controller_view;
    }

    @Override
    protected void initView() {
        mThumbnailImageView = findViewById(R.id.lwj_player_chat_thumbnail);
        statusView = findViewById(R.id.lwj_player_chat_status);
        mControllerBackground = findViewById(R.id.lwj_player_chat_controller_background);
        mCurrentTextView = findViewById(R.id.lwj_player_chat_controller_current);
        mSeekBar = findViewById(R.id.lwj_player_chat_controller_seek);
        mDurationTextView = findViewById(R.id.lwj_player_chat_controller_duration);
        mFunctionBackground = findViewById(R.id.lwj_player_chat_function_background);
        mBacktrackImageView = findViewById(R.id.lwj_player_chat_function_backtrack);
        mShareImageView = findViewById(R.id.lwj_player_chat_function_share);
        mCollectImageView = findViewById(R.id.lwj_player_chat_function_collect);
        downloadImageView = findViewById(R.id.lwj_player_chat_function_download);

        statusView.setOnClickListener(this);
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
                seekTo(position);
            }
        });
    }

    @Override
    protected void onClick(MotionEvent event) {
        startController();
    }

    @Override
    protected void onDblClick(MotionEvent event) {
        startAndStop();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.lwj_player_chat_status) {
            switch (statusView.getStatusEnum()){
                case STOP:
                    /**
                     * 播放暂停
                     */
                    startAndStop();
                    break;
            }
        }else if (v.getId() == R.id.lwj_player_chat_function_backtrack){
            if (controllerListener != null)
                controllerListener.onClick(v,LWJ_PLAYER_CHAT_CONTROLLER_BACKTRACK);
        }else if (v.getId() == R.id.lwj_player_chat_function_share){
            if (controllerListener != null)
                controllerListener.onClick(v,LWJ_PLAYER_CHAT_CONTROLLER_SHARE);
        }else if (v.getId() == R.id.lwj_player_chat_function_collect){
            if (controllerListener != null)
                controllerListener.onClick(v,LWJ_PLAYER_CHAT_CONTROLLER_COLLECT);
        }else if (v.getId() == R.id.lwj_player_chat_function_download){
            if (controllerListener != null)
                controllerListener.onClick(v,LWJ_PLAYER_CHAT_CONTROLLER_DOWNLOAD);
        }
    }

    @Override
    public void bufferUpdate(int buffering) {
        //解决缓冲进度不能100%问题
        if (buffering >= 95) {
            mSeekBar.setSecondaryProgress((int) getDuration());
        } else {
            mSeekBar.setSecondaryProgress(buffering * 10);
        }
    }

    @Override
    public void currencyPosition(long currencyPosition) {
        mSeekBar.setProgress((int) getCurrentPosition());
        mCurrentTextView.setText(longTimeToString(getCurrentPosition()));
    }

    @Override
    public void statusListener(LwjStatusEnum statusEnum) {
        switch (statusEnum){
            case STATUS_PLAYING:
                mDurationTextView.setText(longTimeToString(getDuration()));
                mSeekBar.setMax((int) getDuration());
                statusView.onIdle();
                hideControllerAnim(mThumbnailImageView);
                break;
            case STATUS_BUFFERING:
                statusView.onLoader();
                break;
            case STATUS_PAUSED:
                statusView.onStop();
                break;
            case STATUS_COMPLETED:
                statusView.onIdle();
                showControllerAnim(mThumbnailImageView);
                break;
            case STATUS_PREPARING:
            case STATUS_IDLE:
            case STATUS_BUFFEEND:
                statusView.onIdle();
                break;
            case STATUS_ERROR:
                statusView.onFail();
                break;
        }
    }

    /**
     *
     * @param controllerListener
     */
    public void setControllerListener(OnLwjPlayerControllerListener controllerListener) {
        this.controllerListener = controllerListener;
    }

    /**
     * 设置缩略图
     * @param path
     */
    public void setThumbnail(String path){
        if (TextUtils.isEmpty(path))
            return;
        showControllerAnim(mThumbnailImageView);
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
}
