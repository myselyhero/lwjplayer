package com.yongyong.lwj.player.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yongyong.lwj.player.R;
import com.yongyong.lwj.player.bar.StatusBarUtil;

/**
 * @author yongyong
 *
 * @email 1947920597@qq.com
 *
 * desc:主页面
 *
 * @// TODO: 2020/10/25
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        /**
         * 设置状态栏颜色
         */
        StatusBarUtil.setStatusBarColor(this, R.color.theme_background);

        initView();
    }

    /**
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     *
     */
    protected abstract void initView ();
}
