package com.yongyong.lwj.player.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yongyong.lwj.lwjplayer.LwjPlayerView;

/**
 * @author yongyong
 *
 * @email 1947920597@qq.com
 *
 * desc:frament父类
 * 
 * @// TODO: 2020/10/25
 */
public abstract class BaseFragment extends Fragment {

    /**
     *
     */
    protected View mContentView;

    /**  */
    protected LwjPlayerView lwjPlayerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getLayoutId() != 0){
            mContentView = inflater.inflate(getLayoutId(),container,false);
            initView();
            return mContentView;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (lwjPlayerView != null)
            lwjPlayerView.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (lwjPlayerView != null)
            lwjPlayerView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (lwjPlayerView != null)
            lwjPlayerView.onRelease();
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
}
