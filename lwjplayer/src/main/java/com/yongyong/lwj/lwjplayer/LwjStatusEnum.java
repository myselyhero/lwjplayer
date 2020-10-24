package com.yongyong.lwj.lwjplayer;

/**
 * @author yongyong
 *
 * @mail 1947920597@qq.com
 *
 * desc:播放器状态
 *
 * @// TODO: 2020/10/17
 */
public enum  LwjStatusEnum {

    /** 空闲 */
    STATUS_IDLE,
    /** 准备中 */
    STATUS_PREPARING,
    /** 缓冲中 */
    STATUS_BUFFERING,
    /** 缓冲结束 */
    STATUS_BUFFEEND,
    /** 停止 */
    STATUS_PAUSED,
    /** 播放中 */
    STATUS_PLAYING,
    /** 播放结束 */
    STATUS_COMPLETED,
    /** 错误 */
    STATUS_ERROR;
}
