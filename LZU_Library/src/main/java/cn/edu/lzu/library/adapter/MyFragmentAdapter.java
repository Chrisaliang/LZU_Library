package cn.edu.lzu.library.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cn.edu.lzu.library.R;
import cn.edu.lzu.library.fragment.MyFragment;
import cn.edu.lzu.library.utils.UIUtils;

/**
 * Created by chris on 2017/1/19.
 */
public class MyFragmentAdapter extends FragmentPagerAdapter {

    String[] titles =
            UIUtils.getStringArray(R.array.tab_names);
//            new String[]{"堂主","首页","最新动态","服务","分类"};



    public MyFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public Fragment getItem(int position) {
        return new MyFragment();
    }

    @Override
    public int getCount() {
        return titles.length;
    }
}
