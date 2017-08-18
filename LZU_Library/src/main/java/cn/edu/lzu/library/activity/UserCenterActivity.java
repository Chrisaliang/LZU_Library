package cn.edu.lzu.library.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.edu.lzu.library.R;
import cn.edu.lzu.library.dao.user.User;
import cn.edu.lzu.library.dao.user.UserCenter;
import cn.edu.lzu.library.dao.user.UserDao;
import cn.edu.lzu.library.dao.user.UserSQLiteOpenHelper;
import cn.edu.lzu.library.protocol.UserCenterProtocol;
import cn.edu.lzu.library.utils.ToastUtils;
import cn.edu.lzu.library.utils.UIUtils;
import okhttp3.Call;

/**
 * 用户中心
 */
public class UserCenterActivity extends AppCompatActivity {

    @BindView(R.id.tb_me)
    Toolbar tbMe;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_display_name)
    TextView tvDisplayName;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.tv_phone_number)
    TextView tvPhoneNumber;
    @BindView(R.id.tv_department)
    TextView tvDepartment;
    @BindView(R.id.tv_edit)
    TextView tvEdit;
    @BindView(R.id.btn_logout)
    Button btnLogout;
    @BindView(R.id.iv_user_icon)
    SimpleDraweeView ivUserIcon;

    private UserSQLiteOpenHelper mHelper = new UserSQLiteOpenHelper(this);
    private final int REQUEST_LOGIN = 0x000000000089;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);
        ButterKnife.bind(this);

        initView();
        checkLogin();
    }

    /**
     * 检查是否已经登陆
     * 通过数据库的存储内容进行判断，数据库中有一栏为是否登陆状态
     */
    private void checkLogin() {
        new Thread() {
            @Override
            public void run() {
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
                                handleResult(response);
                            }
                        });
                    }
                    handleResult(loginResult);
                } else {
                    startLogin();
                }
            }
        }.start();
    }

    private void handleResult(String loginResult) {
        Gson gson = new Gson();
        UserCenter userCenter = gson.fromJson(loginResult, UserCenter.class);

        if (userCenter != null) {
            UIUtils.runOnUIThread(() -> {
                Uri uri = Uri.parse(userCenter.msg.headpic);
                ivUserIcon.setImageURI(uri);
//                Glide.with(UserCenterActivity.this)
//                        .load(userCenter.msg.headpic)
//                        .into(ivUserIcon);
                tvUserName.setText(TextUtils.equals(userCenter.msg.displayname, "") ?
                        userCenter.msg.showName + "\r\n" + userCenter.msg.uname
                        : userCenter.msg.displayname + "\r\n" + userCenter.msg.uname);
                tvDisplayName.setText(userCenter.msg.displayname);
                tvEmail.setText(userCenter.msg.email);
                tvPhoneNumber.setText(userCenter.msg.phone);
                tvDepartment.setText(userCenter.msg.department);
            });

        }
    }

    private void startLogin() {
        Intent intent = new Intent(this, UserLoginActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_LOGIN:
                if (resultCode == RESULT_OK) {
                    // 从登录页面返回的成功结果 处理数据
                    String loginResult = data.getStringExtra("loginResult");
                    handleResult(loginResult);
                }
                break;
        }
    }

    /**
     * 初始化布局
     */
    private void initView() {
        tbMe.setTitle(getString(R.string.user_center));
        setSupportActionBar(tbMe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        tbMe.setNavigationOnClickListener(v -> finish());
    }

    @OnClick(R.id.btn_logout)
    public void onLogoutClicked() {
        // 1. 首先修改数据库中的内容
        new Thread(() -> UserCenterProtocol.logout(mHelper, mUser)).start();
        // 重新设置界面
        reSetUI();
        // 重新检查登录状态
        checkLogin();
    }

    private void reSetUI() {
        ivUserIcon.setImageResource(R.mipmap.ic_launcher);
        tvUserName.setText("");
        tvDisplayName.setText("");
        tvEmail.setText("");
        tvPhoneNumber.setText("");
        tvDepartment.setText("");
    }

    @OnClick(R.id.tv_edit)
    public void onEditClicked() {
        // TODO: 2017/4/18 修改个人信息
        ToastUtils.show(this, "修改个人信息");
    }
}
