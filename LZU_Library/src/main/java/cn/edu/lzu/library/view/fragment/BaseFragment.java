package cn.edu.lzu.library.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.edu.lzu.library.view.ui.view.ShowPager;
import cn.edu.lzu.library.utils.UIUtils;

/**
 * Created by chris on 2017/1/20.
 */

public abstract class BaseFragment extends Fragment {

    private ShowPager showPager;
    public FragmentActivity fragmentActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        showPager = new ShowPager(UIUtils.getContext()) {
            @Override
            protected View onCreateSuccessView() {
                return onSubCreateSuccessView();
            }

            @Override
            protected ResultState loadDate() {
                return onSubLoadData();
            }
        };
        return showPager;
    }

    public void getData(){
        //请求网络入口
        if (showPager != null) {
            showPager.show();
        }
    }

    /**
     * 连接成功，创建界面对象view
     * @return
     */
    protected abstract View onSubCreateSuccessView();

    /**
     * 子类请求网络具体实现，再抽象
     * @return
     */
    protected abstract ShowPager.ResultState onSubLoadData() ;
}
