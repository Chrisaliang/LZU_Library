package cn.edu.lzu.library.view.activity;

import cn.edu.lzu.library.module.dao.user.UserCenter;

/**
 * Created by Chrisaliang on 2017/8/18.
 */

public interface IUserCenterView {
    void updateUI(UserCenter userCenter);

    void startLogin();
}
