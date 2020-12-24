package com.yongyong.lwj.lwjplayer.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

/**
 * @author yongyong
 *
 * @email 1947920597@qq.com
 *
 * desc:通用控制器
 * 
 * @// TODO: 2020/10/23
 */
public class LwjCommonControllerView extends LwjControllerBaseView implements View.OnClickListener {

    private String TAG = "LwjPlayerCommonControllerView";

    /**  */
    private ImageView thumbnailImageView;

    private LwjStatusView statusView;

    /** 底部 */
    private ImageView voiceImageView;
    private TextView currentTextView;
    private SeekBar progressSeekBar;
    private TextView totalTextView;
    private ImageView fullImageView;

    private OnLwjPlayerDynamicControllerVoiceListener controllerVoiceListener;

    public LwjCommonControllerView(@NonNull Context context) {
        super(context);
    }

    public LwjCommonControllerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LwjCommonControllerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.lwj_common_controller_view;
    }

    @Override
    protected void initView() {
        thumbnailImageView = findViewById(R.id.lwj_common_controller_thumbnail);
        statusView = findViewById(R.id.lwj_common_controller_status);

        voiceImageView = findViewById(R.id.lwj_common_controller_bottom_voice);
        currentTextView = findViewById(R.id.lwj_common_controller_bottom_current);
        progressSeekBar = findViewById(R.id.lwj_common_controller_bottom_seek_bar);
        totalTextView = findViewById(R.id.lwj_common_controller_bottom_total);
        fullImageView = findViewById(R.id.lwj_common_controller_bottom_full);

        statusView.setOnClickListener(this);
        voiceImageView.setOnClickListener(this);
        fullImageView.setOnClickListener(this);

        /**
         * 拖动监听
         */
        progressSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentTextView.setText(longTimeToString(progress));
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

    }

    @Override
    protected void onDblClick(MotionEvent event) {
        startAndStop();
    }

    /**
     * 更新缓冲值
     * @param buffering
     */
    @Override
    public void bufferUpdate(int buffering) {

        //解决缓冲进度不能100%问题
        if (buffering >= 95) {
            progressSeekBar.setSecondaryProgress((int) getDuration());
        } else {
            progressSeekBar.setSecondaryProgress(buffering * 10);
        }
    }

    /**
     * 更新进度条
     * @param currencyPosition
     */
    @Override
    public void currencyPosition(long currencyPosition) {
        progressSeekBar.setProgress((int) getCurrentPosition());
        currentTextView.setText(longTimeToString(getCurrentPosition()));
    }

    /**
     * 刷新了状态
     * @param statusEnum
     */
    @Override
    public void statusListener(LwjStatusEnum statusEnum) {
        switch (statusEnum){
            case STATUS_PLAYING:
                totalTextView.setText(longTimeToString(getDuration()));
                progressSeekBar.setMax((int) getDuration());
                thumbnailImageView.setVisibility(View.GONE);
                statusView.onIdle();
                break;
            case STATUS_BUFFERING:
                statusView.onLoader();
                break;
            case STATUS_PAUSED:
                statusView.onStop();
                break;
            case STATUS_BUFFEEND:
            case STATUS_IDLE:
            case STATUS_PREPARING:
                statusView.onIdle();
                break;
            case STATUS_COMPLETED:
                thumbnailImageView.setVisibility(View.VISIBLE);
                break;
            case STATUS_ERROR:
                statusView.onFail();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.lwj_common_controller_status) {

            switch (statusView.getStatusEnum()){
                case STOP:
                    /**
                     * 播放暂停
                     */
                    startAndStop();
                    break;
                case FAIL:
                    Log.e(TAG, "onClick: 点击重试");
                    break;
            }
        }else if (v.getId() == R.id.lwj_common_controller_bottom_voice){
            /**
             * 音量开启与关闭
             */
            setMute(!isVoice());

            if (controllerVoiceListener != null)
                controllerVoiceListener.onVoice(isVoice());
        }else if (v.getId() == R.id.lwj_common_controller_bottom_full){

        }
    }

    /**
     *
     * @param mute
     */
    public void setMute(boolean mute){
        if (setVoice(mute)){
            voiceImageView.setImageResource(R.drawable.lwj_player_voice_off);
        }else {
            voiceImageView.setImageResource(R.drawable.lwj_player_voice_on);
        }
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
                .into(thumbnailImageView);
    }

    /**
     *
     * @param controllerVoiceListener
     */
    public void setControllerVoiceListener(OnLwjPlayerDynamicControllerVoiceListener controllerVoiceListener) {
        this.controllerVoiceListener = controllerVoiceListener;
    }

    /**
     * 监听语音的状态后保存
     */
    public interface OnLwjPlayerDynamicControllerVoiceListener {

        /**
         *
         * @param voice
         */
        void onVoice(boolean voice);
    }
}
