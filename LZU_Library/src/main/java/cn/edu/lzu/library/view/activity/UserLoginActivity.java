package cn.edu.lzu.library.view.activity;

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
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.lzu.library.R;
import cn.edu.lzu.library.module.dao.user.User;
import cn.edu.lzu.library.presenter.UserLoginPresenter;

/**
 * A login screen that offers login via userName/password.
 * wtf  登录注册页面
 */
public class UserLoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, IUserLoginView {

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
    private static final String TAG = UserLoginActivity.class.getSimpleName();

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

    UserLoginPresenter presenter;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     * 跟踪登录任务，以确保我们可以根据需要取消登录。
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        ButterKnife.bind(this);
        initView();

        presenter = new UserLoginPresenter(this);

        // Set up the login form. 设置登录表单
        // populateAutoComplete();

        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == R.id.login || id == EditorInfo.IME_NULL) {

                // Store values at the time of the login attempt.获取用户名和密码
                actionAttemptLogin();
                return true;
            }
            return false;
        });

        mUserNameSignInButton.setOnClickListener(view -> actionAttemptLogin());


    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.checkLocal();
    }

    private void actionAttemptLogin() {
        String userName = mUserName.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();
        presenter.attemptLogin(userName, password);
    }

    private void initView() {
        tbLogin.setTitle(R.string.user_login);
        setSupportActionBar(tbLogin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        tbLogin.setNavigationOnClickListener(v -> finish());
    }



    /**
     * Shows the progress UI and hides the login form.
     * 显示进度条，隐藏表单
     */
    public void showProgress(final boolean show) {
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

        Log.e(TAG, "onCreateLoader: hahhah");
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

    @Override
    public void resetUI() {
        mPasswordView.clearFocus();
//        if (mAuthTask != null) {
//            return;
//        }

        // Reset errors. 设置错误标志图标
        mUserName.setError(null);
        mPasswordView.setError(null);

    }

    @Override
    public void passwordErr() {
        mPasswordView.setError(getString(R.string.error_invalid_password));
//        focusView = mPasswordView;
    }

    @Override
    public void userNameErr() {
        mUserName.setError(getString(R.string.error_invalid_user_name));
    }


    @Override
    public void loginErr() {
        // 验证失败
        showProgress(false);
        mPasswordView.setError(getString(R.string.error_incorrect_password));
        mPasswordView.requestFocus();
    }

    @Override
    public void loginSuccess(String response) {
        // 验证成功，开始给控件赋值
        // 将 userCenter 返回给上一个 activity
        Intent intent = getIntent().putExtra("loginResult", response);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void showHistory(ArrayList<User> user) {
//        runOnUiThread(() -> {
//
//        });
        Log.e(TAG, "showHistory: 是否主线程：" + (Looper.myLooper() == Looper.getMainLooper()));

        System.out.println(Thread.currentThread().getName());
        List<String> names = new ArrayList<>();
        if (user.size() > 0) {
            for (int i = 0; i < user.size(); i++) {
                names.add(user.get(i).name);
            }
        }
        mUserName.setText(names.get(0));
        addUserNameToAutoComplete(names);
        mPasswordView.requestFocus();

    }

    @Override
    public void passwordEmpty() {
        mPasswordView.setError(getString(R.string.error_field_required));
    }

    @Override
    public void userNameEmpty() {
        mUserName.setError(getString(R.string.error_field_required));
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

}

