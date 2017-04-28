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
	 * @param numberId
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

	/**
	 * 保存ChannelId
	 * @param channelId
	 */
	public static void saveChannelId(String channelId) {
		getSp().edit().putString("channelId", channelId).commit();
	}

	/**
	 * 获取ChannelId
	 * @return
	 */
	public static String getChannelId() {
		return getSp().getString("channelId", "");
	}

	/**
	 * 设置是否登录成功
	 * @param isSuccess ：true:登录成功
	 */
	public static void setSuccess(boolean isSuccess) {
		getSp().edit().putBoolean("isSuccess", isSuccess).commit();
	}

	/**
	 * 获取是否登录成功
	 * @return
	 * 		true:登录成功
	 */
	public static boolean isSuccess() {
		boolean isAutoLogin = getSp().getBoolean("isSuccess", false);
		return isAutoLogin;
	}

	/**
	 * 保存安保路线id
	 * @param id
	 */
	public static void saveSecurityId(int id){
		getSp().edit().putInt("security_line_id",id).commit();
	}

	/**
	 * 获取安保路线id
	 * @return
	 */
	public static int getSecurityId(){
		return getSp().getInt("security_line_id",0);
	}
	/**
	 * 保存清洁区域id
	 * @param id
	 */
	public static void saveCleanId(int id){
		getSp().edit().putInt("clean_area_id",id).commit();
	}

	/**
	 * 获取清洁区域id
	 * @return
	 */
	public static int getCleanId(){
		return getSp().getInt("clean_area_id",0);
	}
}
