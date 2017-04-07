package com.cn.yitu.xutil;

import android.util.Log;

public class L {
	private static boolean D = true;
	private final static String TAG = "aa";

	public static <T> void i(T msg) {
		if (D) {
			Log.i(TAG, msg + "");
		}
	}

	public static <T> void i(T msg, T msg2) {
		if (D) {
			Log.i(TAG, msg + "--->" + msg2);
		}
	}

	public static <T> void d(T msg) {
		if (D) {
			Log.d(TAG, msg + "");
		}
	}

	public static <T> void d(T msg, T msg2) {
		if (D) {
			Log.d(TAG, msg + "--->" + msg2);
		}
	}

	public static <T> void e(T msg) {
		if (D) {
			Log.e(TAG, msg + "");
		}
	}

	public static <T> void e(T msg, T msg2) {
		if (D) {
			Log.e(TAG, msg + "--->" + msg2);
		}
	}
}
