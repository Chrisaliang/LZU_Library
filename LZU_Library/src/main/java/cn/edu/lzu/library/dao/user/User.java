package cn.edu.lzu.library.dao.user;

/**
 * Created by chris on 2017/4/6.
 * 用户登录类
 */

public class User {

    public String name;
    public String password;

    public User() {
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
