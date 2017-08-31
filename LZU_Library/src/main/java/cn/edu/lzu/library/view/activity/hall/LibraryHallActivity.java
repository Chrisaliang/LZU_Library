package cn.edu.lzu.library.view.activity.hall;

import android.view.View;

import cn.edu.lzu.library.R;
import cn.edu.lzu.library.view.activity.BaseActivity;
import cn.edu.lzu.library.view.ui.view.ShowPager;
import cn.edu.lzu.library.utils.UIUtils;

public class LibraryHallActivity extends BaseActivity {

    @Override
    protected ShowPager.ResultState onSubLoadData() {
        return ShowPager.ResultState.ENUM_STATE_SUCCESS;
    }

    @Override
    protected void logic() {

    }

    @Override
    protected View fillLayout() {
        View view = UIUtils.inflate(R.layout.activity_library_hall);
        return view;
    }

    @Override
    public CharSequence setActivityTitle() {
        return getString(R.string.library_hall);
    }
}
