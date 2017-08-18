package cn.edu.lzu.library.dao.history;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chris on 2017/3/28.
 */

public class HistorySQLiteOpenHelper extends SQLiteOpenHelper {
    private static String name = "history.db";
    private static Integer version = 1;
    public HistorySQLiteOpenHelper(Context context) {
        this(context, name, null, version, null);
    }

    public HistorySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                                   int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table records(_id integer primary key,url text,time text,status text," +
                "type text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
