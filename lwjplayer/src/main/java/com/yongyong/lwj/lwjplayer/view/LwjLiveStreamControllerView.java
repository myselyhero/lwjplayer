package com.yongyong.lwj.lwjplayer.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yongyong.lwj.lwjplayer.LwjStatusEnum;
import com.yongyong.lwj.lwjplayer.LwjStatusView;
import com.yongyong.lwj.lwjplayer.R;
import com.yongyong.lwj.lwjplayer.util.NetWorkUtils;

/**
 * @author yongyong
 * 
 * @desc:直播流控制器
 * 
 * @// TODO: 2020/12/8
 */
public class LwjLiveStreamControllerView extends LwjControllerBaseView implements View.OnClickListener {

    private String TAG = "LwjLiveStreamControllerView";

    public static final int LWJ_LIVE_STREAMING_GIFT = 1;
    public static final int LWJ_LIVE_STREAMING_SHOP = 2;
    public static final int LWJ_LIVE_STREAMING_SHARE = 3;
    public static final int LWJ_LIVE_STREAMING_MORE = 4;

    private LinearLayout linearLayout;
    private LwjStatusView statusView;

    private ImageView giftImageView;
    private ImageView shareImageView;
    private ImageView shopImageView;
    private ImageView moreImageView;

    private OnLwjPlayerControllerListener controllerListener;

    public LwjLiveStreamControllerView(@NonNull Context context) {
        super(context);
    }

    public LwjLiveStreamControllerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LwjLiveStreamControllerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.lwj_live_stream_controller_view;
    }

    @Override
    protected void initView() {
        linearLayout = findViewById(R.id.lwj_live_streaming_controller_bar);
        statusView = findViewById(R.id.lwj_live_streaming_status);
        giftImageView = findViewById(R.id.lwj_live_streaming_gift);
        shareImageView = findViewById(R.id.lwj_live_streaming_share);
        shopImageView = findViewById(R.id.lwj_live_streaming_shop);
        moreImageView = findViewById(R.id.lwj_live_streaming_more);

        /** 用于沉浸式撑起bar高度 */
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        int barHeight = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if(resourceId > 0) {
                barHeight = getResources().getDimensionPixelSize(resourceId);
            }
        }else{
            barHeight = 0;
        }
        setPadding(linearLayout.getPaddingLeft(), barHeight, linearLayout.getPaddingRight(), linearLayout.getPaddingBottom());

        giftImageView.setOnClickListener(this);
        shareImageView.setOnClickListener(this);
        shopImageView.setOnClickListener(this);
        moreImageView.setOnClickListener(this);
    }

    @Override
    protected void onClick(MotionEvent event) {

    }

    @Override
    protected void onDblClick(MotionEvent event) {

    }

    @Override
    public void bufferUpdate(int buffering) {
        if (getTcpSpeed() != -1){
            statusView.setTcpSpeed(NetWorkUtils.formatSizeBySecond(getTcpSpeed()));
        }
    }

    @Override
    public void currencyPosition(long position) {
    }

    @Override
    public void statusListener(LwjStatusEnum statusEnum) {
        switch (statusEnum){
            case STATUS_PLAYING:
            case STATUS_BUFFEEND:
            case STATUS_COMPLETED:
            case STATUS_IDLE:
            case STATUS_PAUSED:
                statusView.onIdle();
                break;
            case STATUS_BUFFERING:
            case STATUS_PREPARING:
                statusView.onLoader();
                break;
            case STATUS_ERROR:
                statusView.onFail();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.lwj_live_streaming_gift) {
            if (controllerListener != null)
                controllerListener.onClick(v,LWJ_LIVE_STREAMING_GIFT);
        } else if (id == R.id.lwj_live_streaming_shop) {
            if (controllerListener != null)
                controllerListener.onClick(v,LWJ_LIVE_STREAMING_SHOP);
        } else if (id == R.id.lwj_live_streaming_share) {
            if (controllerListener != null)
                controllerListener.onClick(v,LWJ_LIVE_STREAMING_SHARE);
        } else if (id == R.id.lwj_live_streaming_more) {
            if (controllerListener != null)
                controllerListener.onClick(v,LWJ_LIVE_STREAMING_MORE);
        }
    }

    /**
     *
     * @param controllerListener
     */
    public void setControllerListener(OnLwjPlayerControllerListener controllerListener) {
        this.controllerListener = controllerListener;
    }
}
