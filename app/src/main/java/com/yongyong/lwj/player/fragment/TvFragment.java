package com.yongyong.lwj.player.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yongyong.lwj.lwjplayer.LwjPlayerView;
import com.yongyong.lwj.lwjplayer.LwjRatioEnum;
import com.yongyong.lwj.lwjplayer.view.LwjChatControllerView;
import com.yongyong.lwj.lwjplayer.view.LwjCommonControllerView;
import com.yongyong.lwj.player.R;
import com.yongyong.lwj.player.adapter.ListAdapter;
import com.yongyong.lwj.player.base.BaseFragment;
import com.yongyong.lwj.player.entity.ListEntity;

/**
 * @author yongyong
 *
 * @email 1947920597@qq.com
 *
 * desc:关于
 *
 * @// TODO: 2020/10/25
 */
public class TvFragment extends BaseFragment {

    private static final String TAG = "TvFragment";

    private FrameLayout frameLayout;

    /**  */
    private String img = "http://yongyong.online/picture/IMG_8618.jpg";
    private String url = "http://yongyong.online/video/IMG_8618.MP4";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.tv_fragment;
    }

    @Override
    protected void initView() {
        frameLayout = mContentView.findViewById(R.id.tv_content);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (frameLayout == null)
            return;
        if (isVisibleToUser){
            if (lwjPlayerView != null){

                /** 释放播放器 */
                lwjPlayerView.onRelease();
                /** 从父布局中删除 */
                ViewParent parent = lwjPlayerView.getParent();
                if (parent instanceof FrameLayout) {
                    ((FrameLayout) parent).removeView(lwjPlayerView);
                }
            }

            lwjPlayerView = new LwjPlayerView(getContext());
            lwjPlayerView.setRatio(LwjRatioEnum.RATIO_CROP);
            lwjPlayerView.setDataSource(url);

            LwjChatControllerView commonControllerView = new LwjChatControllerView(getContext());
            commonControllerView.setThumbnail(img);
            lwjPlayerView.setControllerView(commonControllerView);

            frameLayout.addView(lwjPlayerView);
            lwjPlayerView.onStart();
        }
    }
}
