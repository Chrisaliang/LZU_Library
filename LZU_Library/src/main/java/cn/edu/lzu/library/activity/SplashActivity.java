package cn.edu.lzu.library.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import cn.edu.lzu.library.R;
import cn.edu.lzu.library.utils.Constant;
import cn.edu.lzu.library.utils.SPUtil;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        RelativeLayout rlSplash = (RelativeLayout) findViewById(R.id.rl_splash);
        //旋转动画
        RotateAnimation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        //渐变动画
        AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
        //缩放动画
        ScaleAnimation sa = new ScaleAnimation(0, 1.0f, 0, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        // 动画集合
        AnimationSet as = new AnimationSet(true);

        as.setAnimationListener(new Animation.AnimationListener() {
            //动画开始
            @Override
            public void onAnimationStart(Animation animation) {}

            //动画结束
            @Override
            public void onAnimationEnd(Animation animation) {
                boolean enterGuide = SPUtil.getBoolean(getApplicationContext(), Constant.ENTER_GUIDE, true);
                if (enterGuide) {
                    startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                }else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                finish();
            }

            //动画重复
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

//        as.addAnimation(ra);
        as.addAnimation(aa);
//        as.addAnimation(sa);
        as.setDuration(450);
        //开启动画
        rlSplash.startAnimation(as);
    }
}
