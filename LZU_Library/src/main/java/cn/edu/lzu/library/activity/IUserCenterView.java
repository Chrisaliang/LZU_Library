package cn.edu.lzu.library.activity;

import cn.edu.lzu.library.dao.user.UserCenter;

/**
 * Created by Chrisaliang on 2017/8/18.
 */

public interface IUserCenterView {
    void updateUI(UserCenter userCenter);

    void startLogin();
}
