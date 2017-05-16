package com.cn.yitu.view.show;

import com.amap.api.maps.model.LatLng;
import com.cn.yitu.config.base.CallBack;
import com.cn.yitu.server.QueryHTTP;
import com.cn.yitu.ui.LoginActivity;
import com.cn.yitu.view.CustomDialog;
import com.cn.yitu.xutil.ActivityCollector;
import com.cn.yitu.xutil.SharePreferenceXutil;
import com.cn.yitu.xutil.ToastXutil;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import org.json.JSONObject;

public class ToastDialog {
	private static String token = SharePreferenceXutil.getToken();
	static QueryHTTP server = new QueryHTTP();

	/**
	 * 绑定打扫区域
	 * @param context
	 */
	public static void bindClean(final Context context,final int id) {
		// 自定义对话框
		CustomDialog.Builder builder = new CustomDialog.Builder(context);
		builder.setTitle("提示");
		builder.setMessage("是否绑定"+id+"打扫区域?");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				server.bingClean(token, id, new CallBack() {
					@Override
					public void onResponse(String response) {
						try{
							Log.i("123",response+"");
							JSONObject jsonObject = new JSONObject(response);
							int resultnumber = jsonObject.getInt("resultnumber");
							if (resultnumber == 200){
								ToastXutil.show("绑定成功");
							}
						}catch (Exception e){
							Log.i("123",e.getMessage()+"报错");
						}
					}
				});

			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				ToastXutil.show("取消");
			}
		});
		builder.create().show();
	}

	/**
	 * 退出登录
	 *
	 * @param context
	 */
	public static void exit(final Context context) {
		CustomDialog.Builder builder = new CustomDialog.Builder(context);
		builder.setTitle("提示");
		builder.setMessage("确定退出吗？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 设置你的操作事项
				SharePreferenceXutil.setExit(true);
				context.startActivity(new Intent(context, LoginActivity.class));
				ActivityCollector.finishAll();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
}
