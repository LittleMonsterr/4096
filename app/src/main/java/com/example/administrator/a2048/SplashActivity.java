package com.example.administrator.a2048;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.youmi.android.AdManager;
import net.youmi.android.spot.SpotManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AdManager.getInstance(this).init("6ba7ba85ad2cd1d9", "32093e8f7a765182   ", true);
        //加载广告数据到本地app
        SpotManager.getInstance(this).loadSpotAds();
        //设置插屏竖屏展示
        SpotManager.getInstance(this).setSpotOrientation(SpotManager.ORIENTATION_PORTRAIT);
        //插屏出现动画效果:0: SpotManager.ANIM_NONE 为无动画
        SpotManager.getInstance(this).setAnimationType(SpotManager.ANIM_NONE);
        /*//展示插屏广告
        SpotManager.getInstance(this).showSpotAds(this);

        //如果已经使用了插屏，则不需要再调用开屏的加载。
        SpotManager.getInstance(this).loadSplashSpotAds();*/
        SpotManager.getInstance(this).showSplashSpotAds(this, MainActivity.class);

       /* new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.currentThread().sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //三秒之后跳入到main页面
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                finish();
            }
        }).start();*/
    }
}
