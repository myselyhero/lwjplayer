package com.yongyong.lwj.player.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.yongyong.lwj.player.R;

import java.util.Random;

/**
 * @author yongyong
 *
 * @email 1947920597@qq.com
 *
 * desc:仿抖音点赞视图
 * 
 * @// TODO: 2020/10/25
 */
public class TikTokPraiseView extends ConstraintLayout implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener,
        View.OnTouchListener {

    /** 手势监听 */
    private GestureDetector mGestureDetector;

    /** 随机爱心的旋转角度 */
    private float [] num = new float[]{0f,2f,4f,6f,8f};

    /**  */
    private boolean isDoubleTap;

    public TikTokPraiseView(@NonNull Context context) {
        super(context);
        initView();
    }

    public TikTokPraiseView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TikTokPraiseView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     *
     */
    private void initView(){
        /** 初始化监听 */
        mGestureDetector = new GestureDetector(getContext(), this);
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        /** 双击状态 */
        if (isDoubleTap){
            ImageView imageView = new ImageView(getContext());
            LayoutParams lp = new LayoutParams(300,300);

            /** 点击坐标是父布局的左上角开始的 */
            lp.leftToLeft = 0;
            lp.topToTop = 0;

            /** 点击位置的坐标 */
            lp.leftMargin = (int)(event.getX() - 150F);
            lp.topMargin = (int)(event.getY() - 230F);

            /** 设置图片及params */
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.tik_tok_praise_image));
            imageView.setLayoutParams(lp);
            /** 将图片添加到视图中 */
            addView(imageView);

            /** 设置图片动画 */
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(
                    scaleAni(imageView, "scaleX", 2f, 0.9f, 100, 0))
                    .with(scaleAni(imageView, "scaleY", 2f, 0.9f, 100, 0))
                    .with(rotation(imageView, 0, 0, num[new Random().nextInt(4)]))
                    .with(alphaAni(imageView, 0F, 1F, 100, 0))
                    .with(scaleAni(imageView, "scaleX", 0.9f, 1F, 50, 150))
                    .with(scaleAni(imageView, "scaleY", 0.9f, 1F, 50, 150))
                    .with(translationY(imageView, 0f, -600F, 800, 400))
                    .with(alphaAni(imageView, 1F, 0F, 300, 400))
                    .with(scaleAni(imageView, "scaleX", 1F, 3f, 700, 400))
                    .with(scaleAni(imageView, "scaleY", 1F, 3f, 700, 400));

            /** 启动动画 */
            animatorSet.start();

            /** 动画监听 结束后从父布局删除控件 */
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    removeViewInLayout(imageView);
                }
            });
        }

        return super.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        /** 单击 关闭双击状态 */
        isDoubleTap = false;
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        /** 双击 */
        isDoubleTap = true;
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    /**
     *
     * @param v
     * @param event
     * @return 交由 {@link GestureDetector}处理
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    /* 动画 */

    /**
     * 旋转动画
     * @param view
     * @param time
     * @param delayTime
     * @param values
     * @return
     */
    private ObjectAnimator rotation(View view, long time, long delayTime, final float values){
        ObjectAnimator ani = ObjectAnimator.ofFloat(view,"rotation",values);
        ani.setDuration(time);
        ani.setStartDelay(delayTime);
        ani.setInterpolator(new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                return values;
            }
        });
        return ani;
    }

    /**
     * 透明度渐变
     * @param view
     * @param from
     * @param to
     * @param time
     * @param delayTime
     * @return
     */
    private ObjectAnimator alphaAni(View view,Float from,Float to,long time,long delayTime){
        ObjectAnimator ani = ObjectAnimator.ofFloat(view,"alpha",from,to);
        ani.setInterpolator(new LinearInterpolator());
        ani.setDuration(time);
        ani.setStartDelay(delayTime);
        return ani;
    }

    /**
     *
     * @param view
     * @param from
     * @param to
     * @param time
     * @param delayTime
     * @return
     */
    private ObjectAnimator translationY(View view,float from,float to,long time,long delayTime){
        ObjectAnimator ani = ObjectAnimator.ofFloat(view,"translationY",from,to);
        ani.setInterpolator(new LinearInterpolator());
        ani.setStartDelay(delayTime);
        ani.setDuration(time);
        return ani;
    }

    /**
     *
     * @param view
     * @param from
     * @param time
     * @param to
     * @param delayTime
     * @return
     */
    private ObjectAnimator translationX(View view,float from,long time,float to,long delayTime){
        ObjectAnimator ani = ObjectAnimator.ofFloat(view,"translationX",from,to);
        ani.setStartDelay(delayTime);
        ani.setDuration(time);
        ani.setInterpolator(new LinearInterpolator());
        return ani;
    }

    /**
     *
     * @param View
     * @param propertyName
     * @param from
     * @param to
     * @param time
     * @param delayTime
     * @return
     */
    private ObjectAnimator scaleAni(View View,String propertyName,float from,float to,long time,long delayTime){
        ObjectAnimator ani = ObjectAnimator.ofFloat(View,propertyName,from,to);
        ani.setInterpolator(new LinearInterpolator());
        ani.setStartDelay(delayTime);
        ani.setDuration(time);
        return ani;
    }
}
