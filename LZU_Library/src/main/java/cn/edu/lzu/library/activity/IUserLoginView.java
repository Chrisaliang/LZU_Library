package cn.edu.lzu.library.activity;

/**
 * Created by Chrisaliang on 2017/8/18.
 */

public interface IUserLoginView {
    void resetUI();

    void passwordErr();

    void userNameErr();

    void passwordEmpty();

    void userNameEmpty();

    void showProgress(boolean show);

    void loginErr();

    void loginSuccess(String response);

//    void requestFocus();
}
