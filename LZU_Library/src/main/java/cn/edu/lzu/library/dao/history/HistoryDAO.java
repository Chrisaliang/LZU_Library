package cn.edu.lzu.library.dao.history;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chris on 2017/3/28.
 * 历史记录数据库操作对象封装
 */

public class HistoryDAO {

    private static HistorySQLiteOpenHelper mHelper;

    /**
     * 查询数据库中是否有指定记录
     *
     * @param history 数据库中的记录
     * @return 是否包含
     */
    public static boolean queryData(HistorySQLiteOpenHelper helper, String history) {
        if (mHelper == null) mHelper = helper;
        Cursor cursor = mHelper.getReadableDatabase().rawQuery(
                "select _id from records where url =?", new String[]{history});
        return cursor.moveToNext();
    }

    /**
     * 更新数据
     *
     * @param helper  指定数据库
     * @param history 要更新的url
     * @return 返回修改的行数
     */
    public static int update(HistorySQLiteOpenHelper helper, String history) {
        if (mHelper == null) mHelper = helper;
        ContentValues values = new ContentValues();
        values.put("url", history);
        values.put("status", 1);
        values.put("time", System.currentTimeMillis());
        return mHelper.getReadableDatabase().update("records", values, "url=?", new String[]{history});
    }

    /**
     * 插入数据
     *
     * @param helper  指定数据库
     * @param history 要更新的url
     * @return 返回新插入行的id，-1表示插入失败
     */
    public static long insert(HistorySQLiteOpenHelper helper, String history) {
        if (mHelper == null) mHelper = helper;
        ContentValues values = new ContentValues();
        values.put("url", history);
        values.put("time", System.currentTimeMillis());
        values.put("status", 1);
        values.put("type", 1);
        return mHelper.getReadableDatabase().insert("records", "", values);
    }

    /**
     * 查询全部的历史记录
     *
     * @param helper 数据库的对象
     * @return 以集合的方式返回历史记录
     */
    public static List<History> queryAll(HistorySQLiteOpenHelper helper) {
        if (mHelper == null) mHelper = helper;
        List<History> records = new ArrayList<>();
        Cursor queryAll = mHelper.getReadableDatabase().rawQuery(
                "select _id, url,time,type from records where status = ? order by time desc", new String[]{"1"});
        while (queryAll.moveToNext()) {
            int id = queryAll.getInt(queryAll.getColumnIndex("_id"));
            String url = queryAll.getString(queryAll.getColumnIndex("url"));
            long time = queryAll.getLong(queryAll.getColumnIndex("time"));
            int type = queryAll.getInt(queryAll.getColumnIndex("type"));
            History history = new History(id, url, time, type);
            records.add(history);
        }
        queryAll.close();
        return records;
    }

    /**
     * 清除指定id的列
     *
     * @param helper 数据库帮助对象
     * @param id     要清除的列
     * @return 返回清除的行数
     */
    public static int delete(HistorySQLiteOpenHelper helper, int id) {
        if (mHelper == null) mHelper = helper;
        ContentValues values = new ContentValues();
        values.put("status", "0");
        return mHelper.getReadableDatabase().update("records", values, "_id = ?", new String[]{id + ""});
        // return mHelper.getReadableDatabase().delete("records", " _id = ?", new String[]{id + ""});
    }

    /**
     * 清除历史记录
     *
     * @param helper 数据库帮助对象
     * @return true 表示清除成功
     */
    public static boolean deleteAll(HistorySQLiteOpenHelper helper) {
        if (mHelper == null) mHelper = helper;
        ContentValues values = new ContentValues();
        values.put("status", "0");
        // 修改数据库的所有条目的状态为不可用
        int update = mHelper.getReadableDatabase().update("records", values, "", new String[]{});
        return update > 0;
    }
}
