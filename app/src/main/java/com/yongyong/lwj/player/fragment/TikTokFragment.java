package com.yongyong.lwj.player.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.yongyong.lwj.lwjplayer.LwjPlayerView;
import com.yongyong.lwj.lwjplayer.LwjRatioEnum;
import com.yongyong.lwj.player.R;
import com.yongyong.lwj.player.adapter.TikTokAdapter;
import com.yongyong.lwj.player.base.BaseFragment;
import com.yongyong.lwj.player.entity.TikKotEntity;
import com.yongyong.lwj.player.util.OnViewPagerListener;
import com.yongyong.lwj.player.util.ViewPagerLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yongyong
 *
 * @email 1947920597@qq.com
 *
 * desc:抖音
 *
 * @// TODO: 2020/10/25
 */
public class TikTokFragment extends BaseFragment {

    private static final String TAG = "TikTokFragment";

    private RecyclerView recyclerView;
    private TikTokAdapter mAdapter;
    private List<TikKotEntity> dataSource = new ArrayList<>();

    private int currentPosition = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.tik_tok_fragment;
    }

    @Override
    protected void initView() {
        recyclerView = mContentView.findViewById(R.id.tik_tok_item);

        ViewPagerLayoutManager layoutManager = new ViewPagerLayoutManager(getContext());
        layoutManager.setOnViewPagerListener(new OnViewPagerListener() {
            @Override
            public void onInitComplete() {
                startPlay(0);
            }

            @Override
            public void onPageRelease(boolean isNext, int position) {
                Log.e(TAG, "onPageRelease: next:"+isNext+"position:"+position);
            }

            @Override
            public void onPageSelected(int position, boolean isBottom) {
                Log.e(TAG, "onPageSelected: position:"+position+"bottom:"+isBottom);
                startPlay(position);
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new TikTokAdapter(getContext(),dataSource);
        recyclerView.setAdapter(mAdapter);

        TikKotEntity entity = new TikKotEntity();
        entity.setVideoThumbnail("http://www.yongyong.online/picture/IMG_8614.jpg");
        entity.setVideoUrl("http://www.yongyong.online/video/IMG_8614.MP4");
        dataSource.add(entity);

        entity = new TikKotEntity();
        entity.setVideoThumbnail("http://www.yongyong.online/picture/IMG_8615.jpg");
        entity.setVideoUrl("http://www.yongyong.online/video/IMG_8615.MP4");
        dataSource.add(entity);

        entity = new TikKotEntity();
        entity.setVideoThumbnail("http://www.yongyong.online/picture/IMG_8618.jpg");
        entity.setVideoUrl("http://www.yongyong.online/video/IMG_8618.MP4");
        dataSource.add(entity);

        entity = new TikKotEntity();
        entity.setVideoThumbnail("http://www.yongyong.online/picture/IMG_8621.jpg");
        entity.setVideoUrl("http://www.yongyong.online/video/IMG_8621.MP4");
        dataSource.add(entity);

        entity = new TikKotEntity();
        entity.setVideoThumbnail("http://www.yongyong.online/picture/IMG_8626.jpg");
        entity.setVideoUrl("http://www.yongyong.online/video/IMG_8626.MP4");
        dataSource.add(entity);

        entity = new TikKotEntity();
        entity.setVideoThumbnail("http://www.yongyong.online/picture/IMG_8627.jpg");
        entity.setVideoUrl("http://www.yongyong.online/video/IMG_8627.MP4");
        dataSource.add(entity);

        entity = new TikKotEntity();
        entity.setVideoThumbnail("http://www.yongyong.online/picture/IMG_8629.jpg");
        entity.setVideoUrl("http://www.yongyong.online/video/IMG_8629.MP4");
        dataSource.add(entity);

        entity = new TikKotEntity();
        entity.setVideoThumbnail("http://www.yongyong.online/picture/IMG_8630.jpg");
        entity.setVideoUrl("http://www.yongyong.online/video/IMG_8630.MP4");
        dataSource.add(entity);

        entity = new TikKotEntity();
        entity.setVideoThumbnail("http://www.yongyong.online/picture/IMG_8634.jpg");
        entity.setVideoUrl("http://www.yongyong.online/video/IMG_8634.MP4");
        dataSource.add(entity);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser){
            if (lwjPlayerView != null)
                lwjPlayerView.onRelease();
        }else {
            /*if (lwjPlayerView != null)
                lwjPlayerView.onStart();*/
        }
    }

    /**
     *
     *
     * @param position
     */
    private void startPlay(int position) {
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

        TikKotEntity entity = dataSource.get(position);

        View itemView = recyclerView.getChildAt(0);
        if (itemView == null)
            return;
        TikTokAdapter.TikTokViewHolder viewHolder = (TikTokAdapter.TikTokViewHolder) itemView.getTag();
        lwjPlayerView = new LwjPlayerView(getContext());
        lwjPlayerView.setRatio(LwjRatioEnum.RATIO_CROP);
        lwjPlayerView.setCore(LwjPlayerView.MEDIA_PLAYER_IJK);
        lwjPlayerView.setDataSource(entity.getVideoUrl());
        viewHolder.frameLayout.addView(lwjPlayerView);
        lwjPlayerView.onStart();

        currentPosition = position;
    }
}
