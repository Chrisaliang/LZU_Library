package cn.edu.lzu.library.factory;

import java.util.HashMap;

import cn.edu.lzu.library.fragment.BaseFragment;
import cn.edu.lzu.library.fragment.ClassificationFragment;
import cn.edu.lzu.library.fragment.HallFragment;
import cn.edu.lzu.library.fragment.NavigationFragment;
import cn.edu.lzu.library.fragment.ServiceFragment;
import cn.edu.lzu.library.fragment.ZXDTFragment;

/**
 * Created by chris on 2017/1/20.
 * fragment的工厂
 */

public class FragmentFactory {

    //hashmap储存使用过的fragment
    public static HashMap<Integer, BaseFragment> hashMap = new HashMap<>();

    public static BaseFragment createFragment(int position) {
        BaseFragment fragment  = hashMap.get(position);
        if (fragment == null) {
            switch (position) {
                case 0:
                    // 堂主模块
                    fragment = new HallFragment();
                    break;
                case 1:
                    // 首页
                    fragment = new NavigationFragment();
                    break;
                case 2:
                    // 最新动态
                    fragment = new ZXDTFragment();
                    break;
                case 3:
                    // 服务
                    fragment = new ServiceFragment();
                    break;
                case 4:
                    // 分类
                    fragment = new ClassificationFragment();
                    break;
                default:
//                    fragment = new HallFragment();
                    break;
            }
            hashMap.put(position, fragment);
        }
        return fragment;
    }
}
