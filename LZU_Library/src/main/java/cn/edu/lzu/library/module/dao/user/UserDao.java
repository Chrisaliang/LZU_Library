package cn.edu.lzu.library.module.dao.user;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by chris on 2017/4/7.
 * 用户中心存储数据库操作对象封装
 */

public class UserDao {

    private static UserSQLiteOpenHelper mHelper;

    public static Cursor queryUser(UserSQLiteOpenHelper helper) {
        if (mHelper == null) mHelper = helper;
        return mHelper.getReadableDatabase().rawQuery(
                "select name,password,json,time from userCenter where status =?",
                new String[]{1 + ""});
    }

    public static Cursor queryAllUser(UserSQLiteOpenHelper helper) {
        if (mHelper == null) mHelper = helper;
        Cursor cursor = mHelper.getReadableDatabase().query("userCenter",
                new String[]{"name", "password", "json", "time"},
                "", new String[]{}, null, null, null);
        return cursor;
    }

    /**
     * 查询数据库中是否有指定用户的列
     *
     * @param helper 数据库帮助对象
     * @param user   用户
     * @return 返回 true 表示已存在该用户
     */
    public static boolean query(UserSQLiteOpenHelper helper, User user) {
        if (mHelper == null) mHelper = helper;
        Cursor cursor = mHelper.getReadableDatabase().rawQuery(
                "select _id from userCenter where name =?",
                new String[]{user.name});
        return cursor.moveToNext();
    }

    /**
     * 保存用户登录信息
     *
     * @param helper          数据库帮助对象
     * @param user            用户
     * @param loginResultJson 用户登录成功的 json
     */
    public static void saveUser(UserSQLiteOpenHelper helper, User user, String loginResultJson) {
        if (mHelper == null) mHelper = helper;
        // 1. 查询是否已经存在该用户
        if (query(mHelper, user)) {
            // 2. 是
            // 更新记录
            updateUser(mHelper, user, loginResultJson);
        } else {
            // 3. 否
            // 添加记录
            insertUser(mHelper, user, loginResultJson);
        }
    }

    /**
     * 插入用户数据
     *
     * @param helper          数据库帮助对象
     * @param user            用户
     * @param loginResultJson 用户登录成功的 json
     * @return 返回插入的行数
     */
    private static long insertUser(UserSQLiteOpenHelper helper, User user, String loginResultJson) {
        if (mHelper == null) mHelper = helper;
        ContentValues values = new ContentValues();
        values.put("status", 1);
        values.put("name", user.name);
        values.put("password", user.password);
        values.put("json", loginResultJson);
        values.put("time", System.currentTimeMillis());
        return mHelper.getReadableDatabase().insert("userCenter", "", values);
    }

    /**
     * 更新用户数据
     *
     * @param helper          数据库帮助对象
     * @param user            用户
     * @param loginResultJson 用户登录成功的 json
     * @return 返回更新的行数
     */
    private static int updateUser(UserSQLiteOpenHelper helper, User user, String loginResultJson) {
        if (mHelper == null) mHelper = helper;
        ContentValues values = new ContentValues();
        values.put("status", 1);
        values.put("name", user.name);
        values.put("password", user.password);
        values.put("json", loginResultJson);
        values.put("time", System.currentTimeMillis());
        return mHelper.getReadableDatabase().update("userCenter", values, "name=?", new String[]{user.name});
    }

    public static int logout(UserSQLiteOpenHelper helper, User user) {
        if (mHelper == null) mHelper = helper;
        ContentValues values = new ContentValues();
        values.put("status", 0);
        values.put("name", user.name);
        values.put("password", user.password);
        return mHelper.getReadableDatabase().update("userCenter", values, "name=?", new String[]{user.name});
    }
}
