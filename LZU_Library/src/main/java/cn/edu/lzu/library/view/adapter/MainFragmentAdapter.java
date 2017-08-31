package cn.edu.lzu.library.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cn.edu.lzu.library.R;
import cn.edu.lzu.library.module.factory.FragmentFactory;
import cn.edu.lzu.library.view.fragment.BaseFragment;
import cn.edu.lzu.library.utils.UIUtils;

/**
 * Created by chris on 2017/1/20.
 */
public class MainFragmentAdapter extends FragmentPagerAdapter {

    String[] titles = UIUtils.getStringArray(R.array.tab_names);

    public MainFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        BaseFragment fragment = FragmentFactory.createFragment(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}
