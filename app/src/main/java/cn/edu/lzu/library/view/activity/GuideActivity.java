package cn.edu.lzu.library.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import cn.edu.lzu.library.R;
import cn.edu.lzu.library.utils.Constant;
import cn.edu.lzu.library.utils.DpPxUtils;
import cn.edu.lzu.library.utils.SPUtil;

public class GuideActivity extends AppCompatActivity {

    private ViewPager mVp;
    private int[] bgIds = {R.mipmap.guide_1, R.mipmap.guide_2//, R.mipmap.guide_3
    };
    private ArrayList<ImageView> mBgs;
    private LinearLayout llRound;
    private ImageView ivRed;
    private Button btnStart;
    private int allDis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initView();
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //页面滑动的 时候执行
                int redPos = (int) (allDis * positionOffset + allDis * position);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) ivRed.getLayoutParams();
                layoutParams.leftMargin = redPos;
                ivRed.setLayoutParams(layoutParams);
            }

            @Override
            public void onPageSelected(int position) {
                //页面被选择后执行
                if (position == mBgs.size() - 1) {
                    btnStart.setVisibility(View.VISIBLE);
                } else {
                    btnStart.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //滑动状态改变的时候执行
            }
        });

        llRound.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int left0 = llRound.getChildAt(0).getLeft();
                int left1 = llRound.getChildAt(1).getLeft();
                llRound.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                allDis = left1 - left0;
            }
        });
        btnStart.setOnClickListener(view -> {
            startActivity(new Intent(GuideActivity.this, MainActivity.class));
            SPUtil.putBoolean(getApplicationContext(), Constant.IS_FIRST, false);
            SPUtil.putBoolean(getApplicationContext(), Constant.ENTER_GUIDE, false);
            finish();
        });
    }

    private void initView() {
        mVp = (ViewPager) findViewById(R.id.vp_guide);
        llRound = (LinearLayout) findViewById(R.id.ll_round);
        ivRed = (ImageView) findViewById(R.id.iv_red);
        btnStart = (Button) findViewById(R.id.btn_start);

        mBgs = new ArrayList<>();
        for (int i = 0; i < bgIds.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setBackgroundResource(bgIds[i]);
            mBgs.add(iv);

            ImageView ivGray = new ImageView(this);
            ivGray.setImageResource(R.drawable.gray_dot);
            if (i != 0) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.leftMargin = DpPxUtils.dp2Px(getApplicationContext(), 10);
                ivGray.setLayoutParams(layoutParams);
            }
            llRound.addView(ivGray);
        }
        mVp.setAdapter(new GuideAdapter());
    }

    private class GuideAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mBgs.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = mBgs.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
