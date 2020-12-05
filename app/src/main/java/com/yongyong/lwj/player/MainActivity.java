package com.yongyong.lwj.player;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yongyong.lwj.player.bar.StatusBarUtil;
import com.yongyong.lwj.player.base.BaseActivity;
import com.yongyong.lwj.player.fragment.TvFragment;
import com.yongyong.lwj.player.fragment.ListFragment;
import com.yongyong.lwj.player.fragment.TikTokFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yongyong
 *
 * @email 1947920597@qq.com
 *
 * desc:主页面
 * 
 * @// TODO: 2020/10/25
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout tikBackground;
    private TextView tikTextView;
    private ImageView tikImageView;

    private LinearLayout listBackground;
    private TextView listTextView;
    private ImageView listImageView;

    private LinearLayout tvBackground;
    private TextView tvTextView;
    private ImageView tvImageView;

    private int oldPosition = -1;

    /**
     *
     */
    private ViewPager viewPager;
    private List<ViewPagerEntity> dataSource = new ArrayList<>();

    /* 退出时间 */
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 设置状态栏透明和沉浸式
         */
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setRootViewFitsSystemWindows(this,false);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        viewPager = findViewById(R.id.main_pager);
        tikBackground = findViewById(R.id.main_tab_tik);
        tikTextView = findViewById(R.id.main_tab_tik_tv);
        tikImageView = findViewById(R.id.main_tab_tik_iv);

        listBackground = findViewById(R.id.main_tab_list);
        listTextView = findViewById(R.id.main_tab_list_tv);
        listImageView = findViewById(R.id.main_tab_list_iv);

        tvBackground = findViewById(R.id.main_tab_stream);
        tvTextView = findViewById(R.id.main_tab_stream_tv);
        tvImageView = findViewById(R.id.main_tab_stream_iv);

        onSelected(0);

        tikBackground.setOnClickListener(this);
        listBackground.setOnClickListener(this);
        tvBackground.setOnClickListener(this);

        ViewPagerEntity entity = new ViewPagerEntity();
        entity.setTitle(getString(R.string.main_tab_tik_tok));
        entity.setFragment(new TikTokFragment());
        dataSource.add(entity);

        entity = new ViewPagerEntity();
        entity.setTitle(getString(R.string.main_tab_list));
        entity.setFragment(new ListFragment());
        dataSource.add(entity);

        entity = new ViewPagerEntity();
        entity.setTitle(getString(R.string.main_tab_tv));
        entity.setFragment(new TvFragment());
        dataSource.add(entity);

        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(dataSource.size());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                onSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if (System.currentTimeMillis() - exitTime > 1000){
                exitTime = System.currentTimeMillis();
                Toast.makeText(this,"再按一次退出APP",Toast.LENGTH_SHORT).show();
                return false;
            }else {
                System.exit(0);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     *
     * @param position
     */
    private void onSelected(int position){

        if (position == oldPosition)
            return;

        clear();
        switch (position){
            case 0:
                tikTextView.setTextColor(getResources().getColor(R.color.theme_color));
                tikImageView.setImageResource(R.drawable.main_tik_tok_press);
                break;
            case 1:
                listTextView.setTextColor(getResources().getColor(R.color.theme_color));
                listImageView.setImageResource(R.drawable.main_list_press);
                break;
            case 2:
                tvTextView.setTextColor(getResources().getColor(R.color.theme_color));
                tvImageView.setImageResource(R.drawable.main_tv_press);
                break;
        }
        viewPager.setCurrentItem(position);
        oldPosition = position;
    }

    /**
     *
     */
    private void clear(){
        tikTextView.setTextColor(getResources().getColor(R.color.white));
        tikImageView.setImageResource(R.drawable.main_tik_tok_normal);

        listTextView.setTextColor(getResources().getColor(R.color.white));
        listImageView.setImageResource(R.drawable.main_list_normal);

        tvTextView.setTextColor(getResources().getColor(R.color.white));
        tvImageView.setImageResource(R.drawable.main_tv_normal);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_tab_tik:
                onSelected(0);
                break;
            case R.id.main_tab_list:
                onSelected(1);
                break;
            case R.id.main_tab_stream:
                onSelected(2);
                break;
        }
    }

    /**
     *
     */
    class ViewPagerEntity {

        private String title;
        private Fragment fragment;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Fragment getFragment() {
            return fragment;
        }

        public void setFragment(Fragment fragment) {
            this.fragment = fragment;
        }

        @Override
        public String toString() {
            return "ViewPagerEntity{" +
                    "title='" + title + '\'' +
                    ", fragment=" + fragment +
                    '}';
        }
    }

    /**
     *
     */
    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }


        @Override
        public int getCount() {
            return dataSource.size();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return dataSource.get(position).getFragment();
        }
        /**
         *
         * @param position
         * @return
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return dataSource.get(position).getTitle();
        }
    }
}