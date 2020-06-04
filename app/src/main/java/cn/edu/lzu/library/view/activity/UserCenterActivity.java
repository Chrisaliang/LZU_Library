package cn.edu.lzu.library.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import cn.edu.lzu.library.R;
import cn.edu.lzu.library.module.dao.user.UserCenter;
import cn.edu.lzu.library.presenter.UserCenterPresenter;
import cn.edu.lzu.library.utils.ToastUtils;

/**
 * 用户中心
 */
public class UserCenterActivity extends AppCompatActivity implements IUserCenterView {

    Toolbar tbMe;
    TextView tvUserName;
    TextView tvDisplayName;
    TextView tvEmail;
    TextView tvPhoneNumber;
    TextView tvDepartment;
    Button btnLogout;
    SimpleDraweeView ivUserIcon;

    UserCenterPresenter presenter;


    private final int REQUEST_LOGIN = 0x000000000089;
    private boolean isLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);
        tbMe = findViewById(R.id.tb_me);
        tvUserName = findViewById(R.id.tv_user_name);
        tvDisplayName = findViewById(R.id.tv_display_name);
        tvEmail = findViewById(R.id.tv_email);
        tvPhoneNumber = findViewById(R.id.tv_phone_number);
        tvDepartment = findViewById(R.id.tv_department);
        btnLogout = findViewById(R.id.btn_logout);
        ivUserIcon = findViewById(R.id.iv_user_icon);

        presenter = new UserCenterPresenter(this);
        initView();
        presenter.checkLogin();
    }


    @Override
    public void startLogin() {
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
                    presenter.handleResult(loginResult);
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

    public void onLogoutClicked() {
        if (isLogin) {
            // 1. 首先修改数据库中的内容
            presenter.logout();
            // 重新设置界面
            reSetUI();
            // 重新检查登录状态
        }else{
            // 开始登录
            startLogin();
        }
    }

    private void reSetUI() {
        isLogin = false;
        ivUserIcon.setImageResource(R.mipmap.ic_launcher);
        tvUserName.setText("");
        tvDisplayName.setText("");
        tvEmail.setText("");
        tvPhoneNumber.setText("");
        tvDepartment.setText("");
        btnLogout.setBackgroundResource(R.drawable.btn_login);
        btnLogout.setText(R.string.action_sign_in);
    }

    public void onEditClicked() {
        // TODO: 2017/4/18 修改个人信息
        ToastUtils.show(this, "修改个人信息");
    }

    @Override
    public void updateUI(UserCenter userCenter) {
        if (userCenter == null) {
            reSetUI();
        } else {
            isLogin = true;
            Uri uri = Uri.parse(userCenter.msg.headpic);
            ivUserIcon.setImageURI(uri);
            tvUserName.setText(TextUtils.equals(userCenter.msg.displayname, "") ?
                    userCenter.msg.showName + "\r\n" + userCenter.msg.uname
                    : userCenter.msg.displayname + "\r\n" + userCenter.msg.uname);
            tvDisplayName.setText(userCenter.msg.displayname);
            tvEmail.setText(userCenter.msg.email);
            tvPhoneNumber.setText(userCenter.msg.phone);
            tvDepartment.setText(userCenter.msg.department);
            btnLogout.setBackgroundResource(R.drawable.btn_red_selector);
        }
    }
}