package cn.edu.lzu.library.protocol;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;

import cn.edu.lzu.library.dao.user.User;
import cn.edu.lzu.library.dao.user.UserDao;
import cn.edu.lzu.library.dao.user.UserSQLiteOpenHelper;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by chris on 2017/4/10.
 * 用户中心的联网协议
 */

public class UserCenterProtocol {

    public static void login(User user, StringCallback callback) {
//     String[] result = new String[1];
        String url = "http://mc.m.5read.com/apis/user/userLogin.jspx?"
                + "areaid=274&schoolid=421&userType=0"
                + "&username="
                + user.name
                + "&password="
                + user.password
                + "&encPwd=1";
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(callback);
    }

    public static void logout(UserSQLiteOpenHelper helper, User user) {
        UserDao.logout(helper, user);
    }
}
