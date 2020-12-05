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
import com.yongyong.lwj.player.entity.ListEntity;

import java.util.List;

/**
 * @author yongyong
 *
 * desc:列表适配器
 * 
 * @// TODO: 2020/10/25
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    private Context mContext;
    private List<ListEntity> dataSource;

    private OnListAdapterListener adapterListener;

    public ListAdapter(Context mContext, List<ListEntity> dataSource) {
        this.mContext = mContext;
        this.dataSource = dataSource;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_fragment_item,parent,false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        ListEntity entity = dataSource.get(position);

        holder.position = position;
        if (!TextUtils.isEmpty(entity.getVideoThumbnail())){
            GlideEngine.loadThumbnail(mContext,entity.getVideoThumbnail(),holder.imageView);
        }else {
            if (!TextUtils.isEmpty(entity.getVideoUrl())){
                GlideEngine.loadThumbnail(mContext,entity.getVideoUrl(),holder.imageView);
            }
        }

        if (adapterListener != null)
            holder.itemView.setOnClickListener(v -> {
                adapterListener.onClick(position,entity);
            });
    }

    @Override
    public int getItemCount() {
        return dataSource == null ? 0 : dataSource.size();
    }

    /**
     *
     * @param adapterListener
     */
    public void setAdapterListener(OnListAdapterListener adapterListener) {
        this.adapterListener = adapterListener;
    }

    /**
     *
     */
    public static class ListViewHolder extends RecyclerView.ViewHolder {

        public FrameLayout frameLayout;
        public int position;
        ImageView imageView;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            frameLayout = itemView.findViewById(R.id.list_fragment_item_content);
            imageView = itemView.findViewById(R.id.list_fragment_item_iv);
            itemView.setTag(this);
        }
    }

    /**
     *
     */
    public interface OnListAdapterListener {
        /**
         *
         * @param position
         * @param entity
         */
        void onClick(int position,ListEntity entity);
    }
}
