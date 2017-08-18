package cn.edu.lzu.library.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class DpPxUtils {

	public static int dp2Px(Context context, int dp) {
		DisplayMetrics displayMetrics = context.getResources()
				.getDisplayMetrics();
		float density = displayMetrics.density;
		int px = (int) (dp * density + 0.5f);
		return px;
	}

	public static int px2Dp(Context context, int px) {
		DisplayMetrics displayMetrics = context.getResources()
				.getDisplayMetrics();
		float density = displayMetrics.density;
		int dp = (int) (px / density + 0.5f);
		return dp;
	}
}
