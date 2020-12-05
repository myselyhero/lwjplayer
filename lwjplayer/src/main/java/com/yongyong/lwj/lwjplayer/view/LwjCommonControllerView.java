package com.yongyong.lwj.lwjplayer.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
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
import com.yongyong.lwj.lwjplayer.R;

import java.util.Timer;
import java.util.TimerTask;

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
    private ImageView playerButtonImageView;

    /** 缓冲窗口 */
    private LinearLayout windowBackground;

    /** 底部 */
    private LinearLayout bottomBackground;
    private ImageView voiceImageView;
    private TextView currentTextView;
    private SeekBar progressSeekBar;
    private TextView totalTextView;

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
        return R.layout.lwj_player_common_controller_view;
    }

    @Override
    protected void initView() {
        thumbnailImageView = findViewById(R.id.lwj_player_common_controller_thumbnail);
        playerButtonImageView = findViewById(R.id.lwj_player_common_controller_button);

        windowBackground = findViewById(R.id.lwj_player_common_controller_window);

        bottomBackground = findViewById(R.id.lwj_player_common_controller_bottom);
        voiceImageView = findViewById(R.id.lwj_player_common_controller_bottom_voice);
        currentTextView = findViewById(R.id.lwj_player_common_controller_bottom_current);
        progressSeekBar = findViewById(R.id.lwj_player_common_controller_bottom_seek_bar);
        totalTextView = findViewById(R.id.lwj_player_common_controller_bottom_total);

        playerButtonImageView.setOnClickListener(this);
        voiceImageView.setOnClickListener(this);

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
                mLwjPlayer.seekTo(position);
            }
        });
    }

    @Override
    protected void onClick() {

    }

    @Override
    protected void onDblClick() {

    }

    /**
     * 更新缓冲值
     * @param buffering
     */
    @Override
    public void bufferUpdate(int buffering) {

        //解决缓冲进度不能100%问题
        if (buffering >= 95) {
            progressSeekBar.setSecondaryProgress((int) mLwjPlayer.getDuration());
        } else {
            progressSeekBar.setSecondaryProgress(buffering * 10);
        }
    }

    /**
     * 更新进度条
     * @param currencyPosition
     */
    @Override
    public void currencyPosition(int currencyPosition) {
        progressSeekBar.setProgress((int) mLwjPlayer.getCurrentPosition());
        currentTextView.setText(longTimeToString(mLwjPlayer.getCurrentPosition()));
    }

    /**
     * 刷新了状态
     * @param statusEnum
     */
    @Override
    public void changeStatus(LwjStatusEnum statusEnum) {
        switch (statusEnum){
            case STATUS_PLAYING:
                totalTextView.setText(longTimeToString(mLwjPlayer.getDuration()));
                progressSeekBar.setMax((int) mLwjPlayer.getDuration());
                thumbnailImageView.setVisibility(View.GONE);
                playerButtonImageView.setVisibility(View.GONE);
                break;
            case STATUS_BUFFERING:
                windowBackground.setVisibility(View.VISIBLE);
                break;
            case STATUS_BUFFEEND:
                windowBackground.setVisibility(View.GONE);
                break;
            case STATUS_PAUSED:
            case STATUS_IDLE:
                playerButtonImageView.setVisibility(View.VISIBLE);
                break;
            case STATUS_COMPLETED:
                thumbnailImageView.setVisibility(View.VISIBLE);
                break;
            case STATUS_PREPARING:
                //thumbnailImageView.setVisibility(View.GONE);
                break;
            case STATUS_ERROR:
                Log.e(TAG, "changeStatus: 呀 发生错误了");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.lwj_player_common_controller_button) {
            /**
             * 播放暂停
             */
            if (mLwjPlayer.isPlayer()){
                mLwjPlayer.onPause();
            }else {
                mLwjPlayer.onStart();
            }
        }else if (v.getId() == R.id.lwj_player_common_controller_bottom_voice){
            /**
             * 音量开启与关闭
             */
            mLwjPlayer.setVoice(mLwjPlayer.isVoice() ? false : true);
            if (mLwjPlayer.isVoice()){
                voiceImageView.setImageResource(R.drawable.lwj_player_voice_off);
            }else {
                voiceImageView.setImageResource(R.drawable.lwj_player_voice_on);
            }
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
}
