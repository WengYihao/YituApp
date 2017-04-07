package com.cn.yitu.xutil;


import com.cn.yitu.config.MyApplication;

import android.widget.Toast;

public class ToastXutil {
	public static void show(String msg) {
		Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
	}
	public static void showL(String msg){
		Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_LONG).show();
	}
}
