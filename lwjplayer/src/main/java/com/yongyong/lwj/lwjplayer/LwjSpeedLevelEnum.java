package com.yongyong.lwj.lwjplayer;

/**
 * @author yongyong
 *
 * @mail 1947920597@qq.com
 *
 * desc:播放速度
 *
 * @// TODO: 2020/10/17
 */
public enum LwjSpeedLevelEnum {

    /** 极慢 */
    SPEED_SLOW2(0.5f),
    /** 慢 */
    SPEED_SLOW1(0.7f),
    /** 正常 */
    SPEED_NORMAL(1.0f),
    /** 快 */
    SPEED_FAST1(1.25f),
    /** 极快 */
    SPEED_FAST2(1.5f);

    float speed;

    LwjSpeedLevelEnum(float v) {
        speed = v;
    }

    public float getSpeed() {
        return speed;
    }
}
