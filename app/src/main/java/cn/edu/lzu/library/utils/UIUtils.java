package cn.edu.lzu.library.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;

import cn.edu.lzu.library.global.MyApplication;


public class UIUtils {
    private static String string;

    /**
     * 获取全局上下文
     *
     * @return
     */
    public static Context getContext() {
        return MyApplication.getContext();
    }

    /**
     * 获取handler对象
     *
     * @return
     */
    public static Handler getHandler() {
        return MyApplication.getHandler();
    }

    /**
     * 获取主线程id
     *
     * @return
     */
    public static long getMainThreadId() {
        return MyApplication.getMainThreadId();
    }

    /**
     * 获取主线程
     *
     * @return
     */
    public static Thread getMainThread() {
        return MyApplication.getMainThread();
    }

    public static int dip2px(int dip) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (density * dip + 0.5f);
    }

    public static int px2dip(int px) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5f);
    }

    /**
     * 获取资源
     *
     * @return
     */
    private static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 获取 drawable 对象
     *
     * @param drawableId 对象id
     * @return drawable 对象
     */
    public static Drawable getDrawable(int drawableId) {
        return getResources().getDrawable(drawableId);
    }

    /**
     * 主线程运行
     *
     * @param runnable
     */
    public static void runOnUIThread(Runnable runnable) {
        if (getMainThread() == Thread.currentThread()) {
            //是主线程，直接运行
            runnable.run();
        } else {
            //不是主线程
            getHandler().post(runnable);
        }
    }

    /**
     * 获取 string 文件中 string 类型数组
     *
     * @param tabIds 数组 id
     * @return 数组
     */
    public static String[] getStringArray(int tabIds) {
        return getResources().getStringArray(tabIds);
    }

    /**
     * 获取 string 文件中 int 类型的数组
     *
     * @param tabIds 数组 id
     * @return 数组
     */
    public static int[] getIntArray(int tabIds) {
        return getResources().getIntArray(tabIds);
    }

    public static int getResourceId(String name) {
        return getResources().getIdentifier(name, "mipmap", getContext().getPackageName());
    }

    /**
     * 颜色的状态选择器
     *
     * @param mTabTextColorResId 资源 id
     * @return 返回 id 指定的资源文件选择器对象
     */
    public static ColorStateList getColorStateList(int mTabTextColorResId) {
        return getResources().getColorStateList(mTabTextColorResId);
    }

    /**
     * 将布局xml转换成view对象
     *
     * @param layoutId
     * @return
     */
    public static View inflate(int layoutId) {
        return View.inflate(getContext(), layoutId, null);
    }


}
