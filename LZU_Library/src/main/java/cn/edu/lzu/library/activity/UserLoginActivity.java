package cn.edu.lzu.library.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.lzu.library.R;
import cn.edu.lzu.library.dao.user.User;
import cn.edu.lzu.library.dao.user.UserCenter;
import cn.edu.lzu.library.dao.user.UserDao;
import cn.edu.lzu.library.dao.user.UserSQLiteOpenHelper;
import cn.edu.lzu.library.protocol.UserCenterProtocol;
import cn.edu.lzu.library.utils.UIUtils;
import okhttp3.Call;

/**
 * A login screen that offers login via userName/password.
 *   wtf  登录注册页面
 */
public class UserLoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     * 读取联系人的权限
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * 包含已知用户名和密码的虚拟身份验证存储。
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    // UI references.
    @BindView(R.id.login_progress)
    ProgressBar mProgressView;

    @BindView(R.id.tb_login)
    Toolbar tbLogin;
    @BindView(R.id.user_icon)
    ImageView userIcon;

    @BindView(R.id.user_name)
    AutoCompleteTextView mUserName;
    @BindView(R.id.password)
    EditText mPasswordView;
    @BindView(R.id.user_name_sign_in_button)
    Button mUserNameSignInButton;

    @BindView(R.id.user_name_login_form)
    LinearLayout userNameLoginForm;

    @BindView(R.id.login_form)
    ScrollView mLoginFormView;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     * 跟踪登录任务，以确保我们可以根据需要取消登录。
     */
//    private UserLoginTask mAuthTask = null;
    private UserSQLiteOpenHelper mHelper = new UserSQLiteOpenHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        ButterKnife.bind(this);
        initView();

        // Set up the login form. 设置登录表单
        // populateAutoComplete();

        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == R.id.login || id == EditorInfo.IME_NULL) {
                attemptLogin();
                return true;
            }
            return false;
        });

        mUserNameSignInButton.setOnClickListener(view -> attemptLogin());

    }

    private void initView() {
        tbLogin.setTitle(getString(R.string.user_login));
        setSupportActionBar(tbLogin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        tbLogin.setNavigationOnClickListener(v -> finish());
    }

    private void populateAutoComplete() {
//        if (!mayRequestContacts()) {
//            return;
//        }
//        getLoaderManager().initLoader(0, null, this);
    }

//    /**
//     * 是否需要联系人权限
//     *
//     * @return true为需要
//     */
//    private boolean mayRequestContacts() {
//        return true;
//    }

    /**
     * Callback received when a permissions request has been completed.
     * 当权限请求完成后收到回调。
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * 尝试登录或注册登录表单指定的帐户。
     * If there are form errors (invalid userName, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     * 如果存在表单错误（无效的用户名，缺少的字段等），则会显示错误，并且不会进行实际的登录尝试。
     */
    private void attemptLogin() {
        mPasswordView.clearFocus();
//        if (mAuthTask != null) {
//            return;
//        }

        // Reset errors. 设置错误标志图标
        mUserName.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.获取用户名和密码
        String userName = mUserName.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one. 校验密码
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid userName. 校验用户名
        if (TextUtils.isEmpty(userName)) {
            mUserName.setError(getString(R.string.error_field_required));
            focusView = mUserName;
            cancel = true;
        } else if (!isUserNameValid(userName)) {
            mUserName.setError(getString(R.string.error_invalid_user_name));
            focusView = mUserName;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            // : 2017/4/17 新建子线程并访问网络
            actionLogin(userName,password);
//            mAuthTask = new UserLoginTask(new User(userName, password));
//            mAuthTask.execute((Void) null);
        }
    }

    private void actionLogin(String userName, String password) {
        User mUser = new User(userName,password);
        new Thread(){
            @Override
            public void run() {
                UserCenterProtocol.login(mUser, new StringCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {

//                        UIUtils.runOnUIThread(() -> showProgress(true));

                        UserCenter userCenter =  new Gson().fromJson(response, UserCenter.class);
                        int result = userCenter.result;
                        if (result == 0) {
                            UIUtils.runOnUIThread(() -> {
                                // 验证失败
                                showProgress(false);
                                mPasswordView.setError(getString(R.string.error_incorrect_password));
                                mPasswordView.requestFocus();
                            });

                        } else {
                            // 验证成功
                            saveRecord(mUser, response);
                            // 验证成功，开始给控件赋值
                            // 将 userCenter 返回给上一个 activity
                            Intent intent = getIntent().putExtra("loginResult", response);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                });
            }
        }.start();
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
     * Shows the progress UI and hides the login form.
     * 显示进度条，隐藏表单
     */
    private void showProgress(final boolean show) {
        // 显示进度条
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        //    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
//                show ? 0 : 1)
                /*.setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        })*/

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // 创建 loader 对象
        // todo 从数据库中查询对象并添加
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        // 获取用户名的组对象，在 listView 中展示
        List<String> userName = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            userName.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }
        addUserNameToAutoComplete(userName);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addUserNameToAutoComplete(List<String> userNameCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(UserLoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, userNameCollection);

        mUserName.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
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

