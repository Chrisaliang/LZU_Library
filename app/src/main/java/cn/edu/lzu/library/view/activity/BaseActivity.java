package cn.edu.lzu.library.view.activity;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import cn.edu.lzu.library.R;
import cn.edu.lzu.library.view.ui.view.ShowPager;

public abstract class BaseActivity extends AppCompatActivity {

    Toolbar toolbar;
    ConstraintLayout contentFill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        toolbar = findViewById(R.id.toolbar);
        contentFill = findViewById(R.id.content_fill);

        initView();

        ShowPager showPager = new ShowPager(this) {
            @Override
            protected View onCreateSuccessView() {
                return fillLayout();
            }

            @Override
            protected ResultState loadDate() {
                return onSubLoadData();
            }
        };
        contentFill.addView(showPager);

        logic();

    }

    /**
     * 子 View 的数据加载方法
     * @return
     */
    protected abstract ShowPager.ResultState onSubLoadData();

    /**
     * 布局添加结束后的逻辑
     */
    protected abstract void logic();

    /**
     * 填充布局，按照自己想要的布局进行添加
     * @return 布局对象
     */
    protected abstract View fillLayout();


    /**
     * 初始化标题栏 toolbar 布局
     */
    private void initView() {
        toolbar.setTitle(setActivityTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    /**
     * 给每一个页面设置 toolbar 的标题
     * @return 标题文本
     */
    public abstract CharSequence setActivityTitle();
}
