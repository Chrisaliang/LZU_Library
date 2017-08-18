package cn.edu.lzu.library.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import cn.edu.lzu.library.R;
import cn.edu.lzu.library.activity.hall.CharacteristicResourceActivity;
import cn.edu.lzu.library.activity.hall.ClassVoteActivity;
import cn.edu.lzu.library.activity.hall.HallNotifyActivity;
import cn.edu.lzu.library.activity.hall.HouseMasterAlbumActivity;
import cn.edu.lzu.library.activity.hall.LibraryHallActivity;
import cn.edu.lzu.library.activity.hall.NewBookRecommendActivity;
import cn.edu.lzu.library.adapter.HallFragmentAdapter;
import cn.edu.lzu.library.ui.view.ShowPager;
import cn.edu.lzu.library.utils.UIUtils;

/**
 * 堂主模块的fragment
 * Created by chris on 2017/2/7.
 */
public class HallFragment extends BaseFragment {
    private Context mContext;
    private ConnectivityManager mConnectivityManager;

    @Override
    protected View onSubCreateSuccessView() {
        mContext = getContext();
        RecyclerView view = (RecyclerView) UIUtils.inflate(R.layout.layout_fragment_hall);

        //设置卡片布局管理器
        RecyclerView.LayoutManager layoutManager = getGridManager();
        view.setLayoutManager(layoutManager);

        HallFragmentAdapter adapter = new HallFragmentAdapter();
        adapter.setItemClickListener(position -> {
            switch (position) {
                case 0:
                    // 开启堂主风采activity housemaster_album
                    attemptStartAlbum();
                    break;
                case 1:
                    startLibraryHall();
                    break;
                case 2:
                    startCharacteristicResource();
                    break;
                case 3:
                    startNewBookRecommend();
                    break;
                case 4:
                    startHallNotify();
                    break;
                case 5:
                    startClassVote();
                    break;
            }

        });
        view.setAdapter(adapter);
        return view;
    }

    /**
     * 班级投票
     */
    private void startClassVote() {
        Intent intent = new Intent(mContext, ClassVoteActivity.class);
        startActivity(intent);
    }

    /**
     * 通知公告
     */
    private void startHallNotify() {
        Intent intent = new Intent(mContext, HallNotifyActivity.class);
        startActivity(intent);
    }

    /**
     * 新书推荐
     */
    private void startNewBookRecommend() {
        Intent intent = new Intent(mContext, NewBookRecommendActivity.class);
        startActivity(intent);
    }

    /**
     * 特色及试用资源
     */
    private void startCharacteristicResource() {
        Intent intent = new Intent(mContext, CharacteristicResourceActivity.class);
        startActivity(intent);
    }

    /**
     * 图书馆大厅
     */
    private void startLibraryHall() {
        Intent intent = new Intent(mContext, LibraryHallActivity.class);
        startActivity(intent);
    }

    /**
     * 堂主风采
     */
    private void attemptStartAlbum() {
        boolean flag = false;
        //得到网络连接信息
        if (mConnectivityManager == null)
            mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        //去进行判断网络是否连接
        if (mConnectivityManager.getActiveNetworkInfo() != null) {
            flag = mConnectivityManager.getActiveNetworkInfo().isAvailable();
        }
        if (!flag) {
            setNetwork();
        } else {
            NetworkInfo.State gprs = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
            NetworkInfo.State wifi = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
            if (gprs == NetworkInfo.State.CONNECTED || gprs == NetworkInfo.State.CONNECTING) {
                sendNotice();
            }
            if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
                startAlbum();
            }
        }
    }

    /**
     * 网络未连接时，调用设置方法
     */
    private void setNetwork() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle(R.string.network_notice_msg);
        builder.setMessage(R.string.network_error);
        builder.setPositiveButton(R.string.setting, (dialog, which) -> {
            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
            startActivity(intent);
        });

        builder.setNegativeButton("取消", (dialog, which) -> {
        });
        builder.create();
        builder.show();
    }


    private void startAlbum() {
        Intent intent = new Intent(mContext, HouseMasterAlbumActivity.class);
        startActivity(intent);
    }

    private void sendNotice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle(R.string.network_notice_msg);
        builder.setMessage(R.string.network_onlyGPRS);
        builder.setPositiveButton(R.string.continue_go, (dialog, which) -> {
            // 确定继续
            startAlbum();
        });
        builder.setNegativeButton(R.string.on_cancel, (dialog, which) -> {
        });
        builder.create();
        builder.show();
    }

    //使用卡片布局进行堂主页面的展示
    private RecyclerView.LayoutManager getGridManager() {
        return new GridLayoutManager(fragmentActivity, 2);
    }

    //堂主首页不请求网络，所以返回成功页面
    @Override
    protected ShowPager.ResultState onSubLoadData() {
        return ShowPager.ResultState.ENUM_STATE_SUCCESS;
    }
}
