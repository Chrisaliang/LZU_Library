package cn.edu.lzu.library.presenter;

import android.database.Cursor;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import cn.edu.lzu.library.module.dao.user.User;
import cn.edu.lzu.library.module.dao.user.UserCenter;
import cn.edu.lzu.library.module.dao.user.UserDao;
import cn.edu.lzu.library.module.dao.user.UserSQLiteOpenHelper;
import cn.edu.lzu.library.module.protocol.UserCenterProtocol;
import cn.edu.lzu.library.utils.UIUtils;
import cn.edu.lzu.library.view.activity.IUserCenterView;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;

/**
 * Created by Chrisaliang on 2017/8/18.
 */

public class UserCenterPresenter {

    private IUserCenterView userCenterView;
    private UserSQLiteOpenHelper mHelper = new UserSQLiteOpenHelper(UIUtils.getContext());
    private User mUser;

    public UserCenterPresenter(IUserCenterView userCenterView) {
        this.userCenterView = userCenterView;
    }

    /**
     * 检查是否已经登陆
     * 通过数据库的存储内容进行判断，数据库中有一栏为是否登陆状态
     */
    public void checkLogin() {

        Observable.create((ObservableOnSubscribe<String>) e -> {
            Cursor cursor = UserDao.queryUser(mHelper);
            if (cursor.moveToNext()) {
                // 已经有账号登录过，并保存在本地，不需要重新登录
                // 获取本地的数据，进行赋值
                long time = cursor.getLong(cursor.getColumnIndex("time"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String password = cursor.getString(cursor.getColumnIndex("password"));
                String loginResult = cursor.getString(cursor.getColumnIndex("json"));
                mUser = new User(name, password);
                if ((System.currentTimeMillis() - time) > 5 * 60 * 60 * 1000) {
                    // 重新访问个人中心，获取最新数据
                    UserCenterProtocol.login(mUser, new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
//                                checkLogin();
                        }

                        @Override
                        public void onResponse(String response, int id) {
//                                handleResult(response);
                            e.onNext(response);
                        }
                    });
                }
                e.onNext(loginResult);

            } else {
                e.onError(null);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResult, throwable -> userCenterView.updateUI(null));
    }

    public void handleResult(String loginResult) {
        Observable.create((ObservableOnSubscribe<UserCenter>) e -> {
            Gson gson = new Gson();
            UserCenter userCenter = gson.fromJson(loginResult, UserCenter.class);
            if (userCenter != null) {
                e.onNext(userCenter);
            }
        }).observeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(userCenter -> userCenterView.updateUI(userCenter));

    }

    public void logout() {
        new Thread(() -> UserCenterProtocol.logout(mHelper, mUser)).start();
    }
}
