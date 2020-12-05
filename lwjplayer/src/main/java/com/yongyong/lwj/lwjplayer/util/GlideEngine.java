package com.yongyong.lwj.lwjplayer.util;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

/**
 * @author yongyong
 *
 * @email 1947920597@qq.com
 *
 * desc:图片加载
 *
 * @// TODO: 2020/10/25
 */
public class GlideEngine {

    /**
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadImage(@NonNull Context context,@NonNull String url, @NonNull ImageView imageView) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(imageView);

    }

    /**
     * 加载GIF
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadAsGifImage(@NonNull Context context,@NonNull String url, @NonNull ImageView imageView) {
        Glide.with(context)
                .asGif()
                .load(url)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(imageView);
    }

    /**
     * 加载圆形图片
     * @param context
     * @param url
     * @param imageView
     */
    public static void loaderCircle(@NonNull Context context,@NonNull String url, @NonNull final ImageView imageView){
        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .bitmapTransform(new CircleCrop()))
                .into(imageView);
    }

    /**
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadThumbnail(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView){
        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(imageView);
    }
}