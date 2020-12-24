package com.yongyong.lwj.player.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.widget.Adapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yongyong.lwj.lwjplayer.LwjPlayerView;
import com.yongyong.lwj.lwjplayer.LwjRatioEnum;
import com.yongyong.lwj.lwjplayer.view.LwjChatControllerView;
import com.yongyong.lwj.lwjplayer.view.LwjLiveStreamControllerView;
import com.yongyong.lwj.player.R;
import com.yongyong.lwj.player.base.BaseFragment;
import com.yongyong.lwj.player.tv.TvAdapter;
import com.yongyong.lwj.player.tv.TvEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yongyong
 *
 * @email 1947920597@qq.com
 *
 * desc:关于
 *
 * @// TODO: 2020/10/25
 */
public class TvFragment extends BaseFragment {

    private static final String TAG = "TvFragment";

    private FrameLayout frameLayout;

    private String live = "rtmp://58.200.131.2:1935/livetv/cctv1";

    /**  */
    private List<TvEntity> entityList = new ArrayList<>();
    private TvEntity entity;
    /** 弹窗 */
    private AlertDialog mAlertDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.tv_fragment;
    }

    @Override
    protected void initView() {
        frameLayout = mContentView.findViewById(R.id.tv_content);
        initEntity();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (frameLayout == null)
            return;
        if (isVisibleToUser){
            switchTv(entity == null ? live : entity.getUrl());
        }else {
            release();
        }
    }

    /**
     *
     */
    private void switchTv(String url){
        release();

        lwjPlayerView = new LwjPlayerView(getContext());
        lwjPlayerView.setCore(LwjPlayerView.MEDIA_PLAYER_IJK);
        lwjPlayerView.setDataSource(url);

        LwjLiveStreamControllerView controllerView = new LwjLiveStreamControllerView(getContext());
        controllerView.setControllerListener((view, code) -> {
            switch (code){
                case LwjLiveStreamControllerView.LWJ_LIVE_STREAMING_MORE:
                    if (mAlertDialog == null){
                        mAlertDialog = new AlertDialog.Builder(getContext(), R.style.customDialog).create();
                        mAlertDialog.show();
                        mAlertDialog.setContentView(R.layout.tv_window);
                        Window window = mAlertDialog.getWindow();
                        if (window != null){
                            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            window.setGravity(Gravity.BOTTOM);
                        }

                        RecyclerView recyclerView = mAlertDialog.findViewById(R.id.tv_window_item);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
                        recyclerView.setLayoutManager(gridLayoutManager);
                        TvAdapter adapter = new TvAdapter(getContext(),entityList,(position, entity) -> {
                            this.entity = entity;
                            switchTv(entity.getUrl());
                            mAlertDialog.dismiss();
                        });

                        recyclerView.setAdapter(adapter);
                    }else {
                        mAlertDialog.show();
                    }
                    break;
            }
        });
        lwjPlayerView.setControllerView(controllerView);

        frameLayout.addView(lwjPlayerView);
        lwjPlayerView.onStart();
    }

    /**
     *
     */
    private void initEntity(){
        entity = new TvEntity("rtmp://58.200.131.2:1935/livetv/cctv1","CCTV-1 综合",true);
        entityList.add(entity);
        entityList.add(new TvEntity("rtmp://58.200.131.2:1935/livetv/cctv2","CCTV-2 财经"));
        entityList.add(new TvEntity("rtmp://58.200.131.2:1935/livetv/cctv3","CCTV-3 综艺"));
        entityList.add(new TvEntity("rtmp://58.200.131.2:1935/livetv/cctv4","CCTV-4 国际"));
        entityList.add(new TvEntity("rtmp://58.200.131.2:1935/livetv/cctv5","CCTV-5 体育"));
        entityList.add(new TvEntity("rtmp://58.200.131.2:1935/livetv/cctv6","CCTV-6 电影"));
        entityList.add(new TvEntity("rtmp://58.200.131.2:1935/livetv/cctv7","CCTV-7 军事农业"));
        entityList.add(new TvEntity("rtmp://58.200.131.2:1935/livetv/cctv8","CCTV-8 电视剧"));
        entityList.add(new TvEntity("rtmp://58.200.131.2:1935/livetv/cctv9","CCTV-9 记录"));
        entityList.add(new TvEntity("rtmp://58.200.131.2:1935/livetv/cctv10","CCTV-10 科教"));
        entityList.add(new TvEntity("rtmp://58.200.131.2:1935/livetv/cctv11","CCTV-11 戏曲"));
        entityList.add(new TvEntity("rtmp://58.200.131.2:1935/livetv/cctv12","CCTV-12 社会与法"));
        entityList.add(new TvEntity("rtmp://58.200.131.2:1935/livetv/cctv13","CCTV-13 新闻"));
        entityList.add(new TvEntity("rtmp://58.200.131.2:1935/livetv/cctv14","CCTV-14 少儿"));
        entityList.add(new TvEntity("rtmp://58.200.131.2:1935/livetv/cctv15","CCTV-15 音乐"));
        entityList.add(new TvEntity("rtmp://58.200.131.2:1935/livetv/dyjctv","CCTV-第一剧场"));
        entityList.add(new TvEntity("rtmp://58.200.131.2:1935/livetv/gfjstv","CCTV-国防军事"));
        entityList.add(new TvEntity("rtmp://58.200.131.2:1935/livetv/hjjctv","CCTV-怀旧剧场"));
        entityList.add(new TvEntity("rtmp://58.200.131.2:1935/livetv/fyyytv","CCTV-风云音乐"));
        entityList.add(new TvEntity("rtmp://58.200.131.2:1935/livetv/sjdltv","CCTV-世界地理"));
        entityList.add(new TvEntity("rtmp://58.200.131.2:1935/livetv/startv","星空卫视"));
        entityList.add(new TvEntity("rtmp://58.200.131.2:1935/livetv/chctv","CHC家庭影院"));
        entityList.add(new TvEntity("rtmp://58.200.131.2:1935/livetv/chcatv","CHC动作电影"));
        entityList.add(new TvEntity("rtmp://media3.scctv.net/live/scctv_800","美国电视频道"));
        entityList.add(new TvEntity("rtmp://58.200.131.2:1935/livetv/cqtv","重庆卫视"));
        entityList.add(new TvEntity("rtmp://58.200.131.2:1935/livetv/dftv","东方卫视"));
        entityList.add(new TvEntity("rtmp://58.200.131.2:1935/livetv/gztv","贵州卫视"));
        entityList.add(new TvEntity("rtmp://58.200.131.2:1935/livetv/hbtv","湖北卫视"));
        entityList.add(new TvEntity("rtmp://58.200.131.2:1935/livetv/hunantv","湖南卫视"));
        entityList.add(new TvEntity("rtmp://58.200.131.2:1935/livetv/hebtv","河北卫视"));
        entityList.add(new TvEntity("rtmp://58.200.131.2:1935/livetv/hntv","河南卫视"));
        entityList.add(new TvEntity("rtmp://58.200.131.2:1935/livetv/jstv","江苏卫视"));
        entityList.add(new TvEntity("rtmp://58.200.131.2:1935/livetv/jxtv","江西卫视"));
        entityList.add(new TvEntity("rtmp://58.200.131.2:1935/livetv/jltv","吉林卫视"));
        entityList.add(new TvEntity("rtmp://58.200.131.2:1935/livetv/lntv","辽宁卫视"));
        entityList.add(new TvEntity("rtmp://58.200.131.2:1935/livetv/sctv","四川卫视"));
        entityList.add(new TvEntity("rtmp://58.200.131.2:1935/livetv/sdtv","山东卫视"));
    }
}
