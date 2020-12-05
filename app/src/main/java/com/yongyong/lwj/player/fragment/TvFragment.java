package com.yongyong.lwj.player.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yongyong.lwj.player.R;
import com.yongyong.lwj.player.base.BaseFragment;

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

    }
}
