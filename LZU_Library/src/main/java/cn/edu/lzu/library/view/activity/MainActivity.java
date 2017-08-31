package cn.edu.lzu.library.view.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.lzu.library.R;
import cn.edu.lzu.library.view.adapter.MainFragmentAdapter;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tb)
    Toolbar toolbar;
    @BindView(R.id.indicate)
    TabLayout indicate;
    @BindView(R.id.activity_main)
    CoordinatorLayout activityMain;
    @BindView(R.id.vp_main)
    ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initToolBar();

        initViewPager();

        initTablayout();
    }

    private void initToolBar() {
//        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(Color.WHITE);
//        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
    }

    private void initViewPager() {
        MainFragmentAdapter adapter = new MainFragmentAdapter(getSupportFragmentManager());
        viewpager.setAdapter(adapter);
    }

    private void initTablayout() {
        //初始化
        indicate.setupWithViewPager(viewpager);
        //样式
        indicate.setTabTextColors(Color.WHITE, Color.CYAN);
        indicate.setSelectedTabIndicatorHeight(7);
        indicate.setSelectedTabIndicatorColor(Color.CYAN);
    }

    /**
     * 搜索框
     *
     * @param view 搜索框
     */
    public void WantFind(View view) {
        Intent intent = new Intent(this, SearchBookActivity.class);
        startActivity(intent);
    }

    /**
     * 个人中心
     *
     * @param view 按钮
     */
    public void UserInfo(View view) {
        Intent intent = new Intent(this, UserCenterActivity.class);
        startActivity(intent);
    }

//    private long mPressedTime = 0;

    /**
     * 退出按钮
     */
    @Override
    public void onBackPressed() {
        PackageManager pm = getPackageManager();
        ResolveInfo homeInfo = pm.resolveActivity(new Intent(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_HOME), 0);
        ActivityInfo ai = homeInfo.activityInfo;
        Intent startIntent = new Intent(Intent.ACTION_MAIN);
        startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        startIntent
                .setComponent(new ComponentName(ai.packageName, ai.name));
        startActivity(startIntent);

//        long mNowTime = System.currentTimeMillis();//获取第一次按键时间
//        if ((mNowTime - mPressedTime) > 1000) {//比较两次按键时间差
//            ToastUtils.show(this, getString(R.string.doubleClick2Exit));
//            mPressedTime = mNowTime;
//        } else {//退出程序
//            this.finish();
//            System.exit(0);
//        }
    }

}
