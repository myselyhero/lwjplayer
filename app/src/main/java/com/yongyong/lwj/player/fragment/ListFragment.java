package com.yongyong.lwj.player.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yongyong.lwj.lwjplayer.LwjPlayerView;
import com.yongyong.lwj.lwjplayer.LwjRatioEnum;
import com.yongyong.lwj.player.R;
import com.yongyong.lwj.player.adapter.ListAdapter;
import com.yongyong.lwj.player.base.BaseFragment;
import com.yongyong.lwj.player.entity.ListEntity;

import java.util.ArrayList;
import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 * @author yongyong
 *
 * @email 1947920597@qq.com
 *
 * desc:recyclerViewList
 *
 * @// TODO: 2020/10/25
 */
public class ListFragment extends BaseFragment {

    private static final String TAG = "ListFragment";

    private RecyclerView recyclerView;
    private List<ListEntity> dataSource = new ArrayList<>();
    private ListAdapter mAdapter;

    private LinearLayoutManager linearLayoutManager;
    private int currentPosition = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.list_fragment;
    }

    @Override
    protected void initView() {
        recyclerView = mContentView.findViewById(R.id.list_fragment_item);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new ListAdapter(getContext(),dataSource);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setAdapterListener((position, entity) -> {
            startPlay(position);
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == SCROLL_STATE_IDLE) { //滚动停止
                    autoPlayVideo(recyclerView);
                }
            }

            private void autoPlayVideo(RecyclerView view) {
                if (view == null)
                    return;
                for (int i = 0; i < view.getChildCount(); i++) {
                    View itemView = view.getChildAt(i);
                    if (itemView == null)
                        continue;
                    ListAdapter.ListViewHolder holder = (ListAdapter.ListViewHolder) itemView.getTag();
                    Rect rect = new Rect();
                    holder.frameLayout.getLocalVisibleRect(rect);
                    int height = holder.frameLayout.getHeight();
                    if (rect.top == 0 && rect.bottom == height) {
                        startPlay(holder.position);
                        break;
                    }
                }
            }
        });

        ListEntity entity = new ListEntity();
        entity.setVideoThumbnail("http://yongyong.online/picture/IMG_8614.jpg");
        entity.setVideoUrl("http://yongyong.online/video/IMG_8614.MP4");
        dataSource.add(entity);

        entity = new ListEntity();
        entity.setVideoThumbnail("http://yongyong.online/picture/IMG_8615.jpg");
        entity.setVideoUrl("http://yongyong.online/video/IMG_8615.MP4");
        dataSource.add(entity);

        entity = new ListEntity();
        entity.setVideoThumbnail("http://yongyong.online/picture/IMG_8618.jpg");
        entity.setVideoUrl("http://yongyong.online/video/IMG_8618.MP4");
        dataSource.add(entity);

        entity = new ListEntity();
        entity.setVideoThumbnail("http://yongyong.online/picture/IMG_8621.jpg");
        entity.setVideoUrl("http://yongyong.online/video/IMG_8621.MP4");
        dataSource.add(entity);

        entity = new ListEntity();
        entity.setVideoThumbnail("http://yongyong.online/picture/IMG_8626.jpg");
        entity.setVideoUrl("http://yongyong.online/video/IMG_8626.MP4");
        dataSource.add(entity);

        entity = new ListEntity();
        entity.setVideoThumbnail("http://yongyong.online/picture/IMG_8627.jpg");
        entity.setVideoUrl("http://yongyong.online/video/IMG_8627.MP4");
        dataSource.add(entity);

        entity = new ListEntity();
        entity.setVideoThumbnail("http://yongyong.online/picture/IMG_8629.jpg");
        entity.setVideoUrl("http://yongyong.online/video/IMG_8629.MP4");
        dataSource.add(entity);

        entity = new ListEntity();
        entity.setVideoThumbnail("http://yongyong.online/picture/IMG_8630.jpg");
        entity.setVideoUrl("http://yongyong.online/video/IMG_8630.MP4");
        dataSource.add(entity);

        entity = new ListEntity();
        entity.setVideoThumbnail("http://yongyong.online/picture/IMG_8634.jpg");
        entity.setVideoUrl("http://yongyong.online/video/IMG_8634.MP4");
        dataSource.add(entity);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    startPlay(0);
                }
            });
        }
    }

    /**
     *
     * @param position
     */
    protected void startPlay(int position) {
        if (currentPosition == position)
            return;

        if (lwjPlayerView != null){
            lwjPlayerView.onRelease();
            /** 从父布局中删除 */
            ViewParent parent = lwjPlayerView.getParent();
            if (parent instanceof FrameLayout) {
                ((FrameLayout) parent).removeView(lwjPlayerView);
            }
        }

        ListEntity entity = dataSource.get(position);
        lwjPlayerView = new LwjPlayerView(getContext());
        lwjPlayerView.setRatio(LwjRatioEnum.RATIO_16_9);
        lwjPlayerView.setDataSource(entity.getVideoUrl());

        View itemView = recyclerView.getChildAt(position);
        if (itemView == null)
            return;
        ListAdapter.ListViewHolder viewHolder = (ListAdapter.ListViewHolder) itemView.getTag();
        viewHolder.frameLayout.addView(lwjPlayerView);
        lwjPlayerView.onStart();

        currentPosition = position;
    }
}
