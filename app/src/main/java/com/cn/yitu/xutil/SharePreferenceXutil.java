package com.cn.yitu.xutil;

import com.cn.yitu.config.Constant;
import com.cn.yitu.config.MyApplication;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceXutil {
	private static SharedPreferences sp,hisSp;

	private static SharedPreferences getSp() {
		if (sp == null) {
			sp = MyApplication.getContext().getSharedPreferences(Constant.userIdKey, Context.MODE_PRIVATE);
		}
		return sp;
	}
	
	private static SharedPreferences getHisSp(){
		if (hisSp == null) {
			hisSp = MyApplication.getContext().getSharedPreferences(Constant.history, Context.MODE_PRIVATE);
		}
		return hisSp;
	}

	/**
	 * 保存账号ID
	 * @param userId
	 */
	public static void saveNumberId(int numberId) {
		getSp().edit().putInt("numberId", numberId).commit();
	}

	/**
	 * 获取账号ID
	 * @return
	 */
	public static int getNumberId() {
		return getSp().getInt("numberId", 0);
	}
	
	/**
	 * 保存用户ID
	 * @param userId
	 */
	public static void saveUserId(int userId) {
		getSp().edit().putInt("userId", userId).commit();
	}

	/**
	 * 保存token值
	 * @param token
	 */
	public static void saveToken(String token){
		getSp().edit().putString("token",token).commit();
	}

	/**
	 * 获取token值
	 * @return
	 */
	public static String getToken(){
		return getSp().getString("token","");
	}

}