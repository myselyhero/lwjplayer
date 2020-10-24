package com.yongyong.lwj.player;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.yongyong.lwj.lwjplayer.LwjPlayerView;
import com.yongyong.lwj.lwjplayer.LwjRatioEnum;
import com.yongyong.lwj.lwjplayer.view.LwjPlayerChatControllerView;
import com.yongyong.lwj.lwjplayer.view.LwjPlayerCommonControllerView;
import com.yongyong.lwj.lwjplayer.view.LwjPlayerControllerView;

public class MainActivity extends AppCompatActivity {

    private LwjPlayerView lwjPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lwjPlayerView = findViewById(R.id.lwj_palyer);

        /*LwjPlayerControllerView controllerViewView = new LwjPlayerControllerView(this);
        controllerViewView.setThumbnail("http://www.yongyong.online:8090/video/upload/IMG_8626.jpg");
        lwjPlayerView.setControllerView(controllerViewView);*/

        /*LwjPlayerCommonControllerView controllerView = new LwjPlayerCommonControllerView(this);
        controllerView.setThumbnail("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2017279284,4120794229&fm=26&gp=0.jpg");
        lwjPlayerView.setControllerView(controllerView);*/
        LwjPlayerChatControllerView chatControllerView = new LwjPlayerChatControllerView(this);
        chatControllerView.setThumbnail("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2017279284,4120794229&fm=26&gp=0.jpg");
        lwjPlayerView.setControllerView(chatControllerView);

        //"http://www.yongyong.online:8090/video/upload/IMG_8626.jpg",
        //lwjPlayerView.setDataSource("http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4");
        lwjPlayerView.setDataSource("http://www.yongyong.online:8090/video/upload/IMG_8614.MP4");
        //lwjPlayerView.setDataSource("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        lwjPlayerView.setRatio(LwjRatioEnum.RATIO_16_9);
        //lwjPlayerView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        lwjPlayerView.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        lwjPlayerView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lwjPlayerView.onRelease();
    }
}