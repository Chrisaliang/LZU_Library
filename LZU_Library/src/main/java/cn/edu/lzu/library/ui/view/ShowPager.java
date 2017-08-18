package cn.edu.lzu.library.ui.view;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import cn.edu.lzu.library.R;
import cn.edu.lzu.library.utils.UIUtils;

/**
 * Created by chris on 2017/1/20.
 */

public abstract class ShowPager extends FrameLayout {

    private View loadingView;   //加载过程中view
    private View netErrorView;  //请求网络失败展示的view
    private View emptyView;     //网络内容为空view
    private View successView;   //成功view

    //提供四种类型的网络访问状态
    private static final int STATE_NONE = 0;
    private static final int STATE_LOADING = 1;
    private static final int STATE_ERROR = 2;
    private static final int STATE_EMPTY = 3;
    private static final int STATE_SUCCESS = 4;

    //当前页面展示状态
    private int currentState = STATE_NONE;

    public ShowPager(Context context) {
        super(context);
        //初始化成功以外的界面状态
        loadingView = UIUtils.inflate(R.layout.layout_loading);
        if (loadingView!=null){
            addView(loadingView);
        }
        netErrorView = UIUtils.inflate(R.layout.layout_error);
        if (netErrorView!=null){
            addView(netErrorView);
        }
        emptyView = UIUtils.inflate(R.layout.layout_empty);
        if (emptyView != null) {
            addView(emptyView);
        }

        show();
    }

    /**
     * 请求网络并根据结果展示
     */
    public void show() {
        //请求状态归位
        //每一个模块请求都有可能出现多次
        //假设第一次请求，请求成功，currentState = STATE_SUCCESS
        //发送第二次请求，currentState = STATE_NONE
        if (currentState == STATE_ERROR||currentState == STATE_EMPTY||currentState == STATE_SUCCESS) {
            currentState = STATE_NONE;
        }
        new Thread(){
            @Override
            public void run() {
                //在此请求网络
                ResultState resultState = loadDate();
                if (resultState != null) {
                    //返回状态码
                    currentState = resultState.getState();
                    UIUtils.runOnUIThread(() -> showPage());
                }
            }
        }.start();
    }

    /**
     * 根据网络返回状态显示页面结果
     */
    private void showPage() {
        //当前处于正在请求网络或者初始化状态，显示进度圈
        if (loadingView != null) {
            loadingView.setVisibility
                    ((currentState == STATE_LOADING || currentState == STATE_NONE) ?
                            View.VISIBLE : View.GONE);
        }
        //请求网络失败状态
        if (netErrorView != null) {
            netErrorView.setVisibility(currentState == STATE_ERROR ? View.VISIBLE : View.GONE);
        }
        //请求数据为空
        if (emptyView != null) {
            emptyView.setVisibility(currentState == STATE_EMPTY ? View.VISIBLE : View.GONE);
        }
        //网络请求成功
        if (currentState == STATE_SUCCESS) {
            //移除已有页面
            if (successView !=null){
                removeView(successView);
            }
            //添加成功页面
            successView = onCreateSuccessView();
            addView(successView);
        }
        if (successView != null) {
            // 如果成功页面不为空，根据状态显示页面
            successView.setVisibility(currentState == STATE_SUCCESS ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 返回成功获取数据的对应页面对象
     * 不同的fragment有不同的显示效果，所以不能用布局文件指定
     * @return
     */
    protected abstract View onCreateSuccessView();

    /**
     * 模块单独网络访问方法
     * @return
     */
    protected abstract ResultState loadDate() ;


    public enum ResultState {
        ENUM_STATE_ERROR(2),
        ENUM_STATE_EMPTY(3),
        ENUM_STATE_SUCCESS(4);
        private int mState;
        //枚举,私有构造方法的类
        private ResultState(int state) {
            this.mState = state;
        }

        public int getState() {
            return mState;
        }
    }
}
