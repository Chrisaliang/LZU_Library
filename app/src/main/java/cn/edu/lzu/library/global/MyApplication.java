package cn.edu.lzu.library.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by chris on 2017/1/19.
 */

public class MyApplication extends Application {

    public static Context context;
    public static Handler handler;
    public static Thread mainThread;
    public static long mainThreadId;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        handler = new Handler();
        mainThread = Thread.currentThread();
        mainThreadId = mainThread.getId();
        Fresco.initialize(context);
        initHttpUtil();
        super.onCreate();
    }

    private void initHttpUtil() {
        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(context));
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .cookieJar(cookieJar)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }

    public static Context getContext() {
        return context;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static long getMainThreadId() {
        return mainThreadId;
    }

    public static Thread getMainThread() {
        return mainThread;
    }
}
