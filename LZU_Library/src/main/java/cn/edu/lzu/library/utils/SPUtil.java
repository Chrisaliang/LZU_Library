package cn.edu.lzu.library.utils;


import android.content.Context;
import android.content.SharedPreferences;

public class SPUtil {
	private static SharedPreferences mSp;

	private SPUtil() {
	}

	private static void getSP(Context context) {
		if (mSp == null) {
			mSp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
	}

	public static void remove(String key, Context context) {
		getSP(context);
		mSp.edit().remove(key).commit();
	}

	public static void putBoolean(Context context, String key, boolean value) {
		getSP(context);
		mSp.edit().putBoolean(key, value).commit();
	}

	public static boolean getBoolean(Context context, String key,
			boolean defValue) {
		getSP(context);
		return mSp.getBoolean(key, defValue);
	}

	public static void putString(Context context, String key, String value) {
		getSP(context);
		mSp.edit().putString(key, value).commit();
	}

	public static String getString(Context context, String key, String defValue) {
		getSP(context);
		return mSp.getString(key, defValue);
	}

	public static void putInt(Context context, String key, int value) {
		getSP(context);
		mSp.edit().putInt(key, value).commit();
	}

	public static int getInt(Context context, String key, int defValue) {
		getSP(context);
		return mSp.getInt(key, defValue);
	}

}
