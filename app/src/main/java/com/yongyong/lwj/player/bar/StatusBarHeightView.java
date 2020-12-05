package com.yongyong.lwj.player.bar;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 * @author 王永勇
 * 状态栏高度、用于沉浸式时撑起高度，避免视图与状态栏重合
 * @// TODO: 2020/7/22
 */
public class StatusBarHeightView extends LinearLayout {

    private int barHeight;

    public StatusBarHeightView(Context context) {
        super(context);
        init();
    }

    public StatusBarHeightView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StatusBarHeightView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    /**
     *
     */
    private void init() {

        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if(resourceId>0) {
                barHeight = getResources().getDimensionPixelSize(resourceId);
            }
        }else{
            barHeight = 0;
        }
        setPadding(getPaddingLeft(), barHeight, getPaddingRight(), getPaddingBottom());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec), barHeight);
    }
}
