package cn.edu.lzu.library.module.dao.user;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chris on 2017/4/6.
 * 用户中心数据库帮助对象
 */

public class UserSQLiteOpenHelper extends SQLiteOpenHelper {

    private static String name = "userCenter.db";
    private static Integer version = 1;

    public UserSQLiteOpenHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建个人中心数据库，该数据库包含了用户的登录状态，登录名和登录密码，
        // 还有登录成功的 json 字符串 以及插入时间
        db.execSQL("create table userCenter(_id integer primary key,status text,name text," +
                "password text,json text,time text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
