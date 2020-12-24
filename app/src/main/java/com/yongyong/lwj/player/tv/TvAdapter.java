package com.yongyong.lwj.player.tv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yongyong.lwj.player.R;

import java.util.List;

/**
 * @author yongyong
 * 
 * @desc:
 * 
 * @// TODO: 2020/12/8
 */
public class TvAdapter extends RecyclerView.Adapter<TvAdapter.TvHolder> {

    private Context mContext;
    private List<TvEntity> dataSource;

    private OnTvAdapterListener listener;

    public TvAdapter(Context mContext, List<TvEntity> dataSource,OnTvAdapterListener listener) {
        this.mContext = mContext;
        this.dataSource = dataSource;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tv_item,parent,false);
        return new TvHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TvHolder holder, int position) {
        TvEntity entity = dataSource.get(position);

        holder.textView.setText(entity.getName());

        if (entity.isSel()){
            holder.textView.setTextColor(mContext.getResources().getColor(R.color.theme_color));
        }else {
            holder.textView.setTextColor(mContext.getResources().getColor(R.color.white));
        }

        if (listener != null){
            holder.itemView.setOnClickListener(v -> {
                if (entity.isSel()){
                    entity.setSel(false);
                }else {
                    for (int i = 0; i < dataSource.size(); i++) {
                        if (dataSource.get(i).isSel()){
                            dataSource.get(i).setSel(false);
                            notifyItemChanged(i);
                        }
                    }
                    entity.setSel(true);
                }
                notifyItemChanged(position);
                if (entity.isSel())
                    listener.onItemClick(position,entity);
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataSource == null ? 0 : dataSource.size();
    }

    /**
     *
     */
    class TvHolder extends RecyclerView.ViewHolder{

        TextView textView;

        public TvHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_holder_tv);
        }
    }

    /**
     *
     */
    public interface OnTvAdapterListener {


        /**
         *
         * @param position
         * @param entity
         */
        void onItemClick(int position,TvEntity entity);
    }
}
