package com.yongyong.lwj.lwjplayer.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
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

/**
 * @author yongyong
 *
 * @mail 1947920597@qq.com
 *
 * desc:播放控制器
 *
 * @// TODO: 2020/10/17
 */
public class LwjPlayerControllerView extends LwjPlayerControllerBaseView implements View.OnClickListener {

    /** 顶部 */
    private LinearLayout mTopBackground;
    private ImageView mTopBacktrackImageView;
    private TextView mTopTitleTextView;
    private ImageView mElectricImageView;
    private ImageView mMoreImageView;

    /** 缩略图 */
    private ImageView mThumbnailImageView;

    /** 锁 */
    private ImageView mLockImageView;

    /** 播放暂停按钮 */
    private ImageView mButtonImageView;

    /** 底部 */
    private LinearLayout mBottomBackground;
    private ImageView mBottomVoiceImageView;
    private TextView mCurrentTextView;
    private SeekBar mBottomSeekBar;
    private TextView mBottomTotalTextView;
    private TextView mBottomSpeedTextView;
    private ImageView mBottomScreenImageView;

    public LwjPlayerControllerView(@NonNull Context context) {
        super(context);
    }

    public LwjPlayerControllerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LwjPlayerControllerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.lwj_player_controller_view;
    }

    @Override
    protected void initView() {
        mTopBackground = findViewById(R.id.lwj_player_controller_top);
        mTopBacktrackImageView = findViewById(R.id.lwj_player_controller_top_backtrack);
        mTopTitleTextView = findViewById(R.id.lwj_player_controller_top_title);
        mElectricImageView = findViewById(R.id.lwj_player_controller_top_electric);
        mMoreImageView = findViewById(R.id.lwj_player_controller_top_more);

        mThumbnailImageView = findViewById(R.id.lwj_player_controller_thumbnail);
        mLockImageView = findViewById(R.id.lwj_player_controller_lock);
        mButtonImageView = findViewById(R.id.lwj_player_controller_button);

        mBottomBackground = findViewById(R.id.lwj_player_controller_bottom);
        mBottomVoiceImageView = findViewById(R.id.lwj_player_controller_bottom_voice);
        mCurrentTextView = findViewById(R.id.lwj_player_controller_bottom_current);
        mBottomSeekBar = findViewById(R.id.lwj_player_controller_bottom_seek_bar);
        mBottomTotalTextView = findViewById(R.id.lwj_player_controller_bottom_total);
        mBottomSpeedTextView = findViewById(R.id.lwj_player_controller_bottom_speed);
        mBottomScreenImageView = findViewById(R.id.lwj_player_controller_bottom_screen);

        mButtonImageView.setOnClickListener(this);
    }

    @Override
    protected void onClick() {

    }

    @Override
    protected void onDblClick() {

    }

    @Override
    public void bufferUpdate(int buffering) {

    }

    @Override
    public void changeStatus(LwjStatusEnum statusEnum) {
        switch (statusEnum){
            case STATUS_PLAYING:
                mButtonImageView.setVisibility(View.GONE);
                break;
            default:
                mButtonImageView.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.lwj_player_controller_button) {
            if (mLwjPlayer.isPlayer()){
                mLwjPlayer.onPause();
            }else {
                mLwjPlayer.onStart();
            }
        }
    }

    /**
     *
     * @param thumbnail
     */
    public void setThumbnail(String thumbnail) {
        if (TextUtils.isEmpty(thumbnail))
            return;
        Glide.with(getContext())
                .asBitmap()
                .load(thumbnail)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(mThumbnailImageView);
    }
}
