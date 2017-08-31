package cn.edu.lzu.library.presenter;

import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;

import cn.edu.lzu.library.module.dao.user.User;
import cn.edu.lzu.library.module.dao.user.UserCenter;
import cn.edu.lzu.library.module.dao.user.UserDao;
import cn.edu.lzu.library.module.dao.user.UserSQLiteOpenHelper;
import cn.edu.lzu.library.module.protocol.UserCenterProtocol;
import cn.edu.lzu.library.utils.UIUtils;
import cn.edu.lzu.library.view.activity.IUserLoginView;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;

/**
 * Created by Chrisaliang on 2017/8/18.
 */

public class UserLoginPresenter {

    IUserLoginView userLoginView;
    private String TAG = UserLoginPresenter.class.getSimpleName();

    public UserLoginPresenter(IUserLoginView userLoginView) {
        this.userLoginView = userLoginView;
    }

    private UserSQLiteOpenHelper mHelper = new UserSQLiteOpenHelper(UIUtils.getContext());

    public void checkLocal() {
        // 从数据库读取记录，将记录
        Observable.create((ObservableOnSubscribe<ArrayList<User>>) e -> {
            Cursor cursor = UserDao.queryAllUser(mHelper);
            Log.e(TAG, "subscribe: cursor  " + cursor);
            ArrayList<User> users = new ArrayList<>();
            while (cursor.moveToNext()) {
                // "name", "password", "json", "time"
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String password = cursor.getString(cursor.getColumnIndex("password"));
                //                String json = cursor.getString(cursor.getColumnIndex("json"));
//                long time = cursor.getLong(cursor.getColumnIndex("time"));
                User user = new User(name, password);
                users.add(user);
            }
            cursor.close();
            if (users.size() > 0) e.onNext(users);
        }).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(users -> userLoginView.showHistory(users));
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * 尝试登录或注册登录表单指定的帐户。
     * If there are form errors (invalid userName, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     * 如果存在表单错误（无效的用户名，缺少的字段等），则会显示错误，并且不会进行实际的登录尝试。
     */
    public void attemptLogin(String userName, String password) {

        userLoginView.resetUI();

        boolean cancel = false;
//        View focusView = null;

        // Check for a valid password, if the user entered one. 校验密码
        if (TextUtils.isEmpty(password)) {
//            focusView = mPasswordView;
            userLoginView.passwordEmpty();
            cancel = true;
        } else if (!isPasswordValid(password)) {
            userLoginView.passwordErr();
//            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid userName. 校验用户名
        if (TextUtils.isEmpty(userName)) {
            userLoginView.userNameEmpty();
//            focusView = mUserName;
            cancel = true;
        } else if (!isUserNameValid(userName)) {
            userLoginView.userNameEmpty();
//            focusView = mUserName;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
//            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            userLoginView.showProgress(true);
            // : 2017/4/17 新建子线程并访问网络
            actionLogin(userName, password);
//            mAuthTask = new UserLoginTask(new User(userName, password));
//            mAuthTask.execute((Void) null);
        }
    }

    private void actionLogin(String userName, String password) {
        User mUser = new User(userName, password);

        Observable.create((ObservableOnSubscribe<String>) e ->
                UserCenterProtocol.login(mUser, new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {

//                        UIUtils.runOnUIThread(() -> showProgress(true));

                        UserCenter userCenter = new Gson().fromJson(response, UserCenter.class);
                        int result = userCenter.result;
                        if (result == 0) {
                            e.onError(null);
//                            UIUtils.runOnUIThread(() -> userLoginView.loginErr());
                        } else {
                            // 验证成功
                            saveRecord(mUser, response);
                            e.onNext(response);
                        }
                    }
                })).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> userLoginView.loginSuccess(response),
                        throwable -> userLoginView.loginErr());


    }

    private boolean isUserNameValid(String userName) {
        // 校验用户名的逻辑
        String reg = "[0-9]{13}";
        return userName.matches(reg);
    }

    private boolean isPasswordValid(String password) {
        // 个人校验密码的逻辑
        return password.length() >= 4;
    }

    /**
     * 保存本地记录，将用户和 json 结果保存起来
     *
     * @param user            用户
     * @param loginResultJson 登录成功的 json 结果
     */
    private void saveRecord(User user, String loginResultJson) {
        UserDao.saveUser(mHelper, user, loginResultJson);
    }


}
