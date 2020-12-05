package com.yongyong.lwj.player.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yongyong.lwj.lwjplayer.util.GlideEngine;
import com.yongyong.lwj.player.R;
import com.yongyong.lwj.player.entity.TikKotEntity;

import java.util.List;

/**
 * @author yongyong
 *
 * desc:抖音适配器
 *
 * @// TODO: 2020/10/25
 */
public class TikTokAdapter extends RecyclerView.Adapter<TikTokAdapter.TikTokViewHolder> {

    private Context mContext;
    private List<TikKotEntity> dataSource;

    public TikTokAdapter(Context mContext, List<TikKotEntity> dataSource) {
        this.mContext = mContext;
        this.dataSource = dataSource;
    }

    @NonNull
    @Override
    public TikTokViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tik_tok_fragment_item,parent,false);
        return new TikTokViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TikTokViewHolder holder, int position) {
        TikKotEntity entity = dataSource.get(position);
        if (!TextUtils.isEmpty(entity.getVideoThumbnail())){
            GlideEngine.loadThumbnail(mContext,entity.getVideoThumbnail(),holder.imageView);
        }else {
            if (!TextUtils.isEmpty(entity.getVideoUrl())){
                GlideEngine.loadThumbnail(mContext,entity.getVideoUrl(),holder.imageView);
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataSource == null ? 0 : dataSource.size();
    }

    /**
     *
     */
    public static class TikTokViewHolder extends RecyclerView.ViewHolder {

        public FrameLayout frameLayout;
        ImageView imageView;

        public TikTokViewHolder(@NonNull View itemView) {
            super(itemView);
            frameLayout = itemView.findViewById(R.id.tik_tok_fragment_item_content);
            imageView = itemView.findViewById(R.id.tik_tok_fragment_item_iv);
            itemView.setTag(this);
        }
    }
}
