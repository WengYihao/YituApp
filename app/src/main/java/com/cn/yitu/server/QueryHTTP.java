package com.cn.yitu.server;

import android.telecom.Call;

import com.cn.yitu.config.Constant;
import com.cn.yitu.config.base.BaseServer;
import com.cn.yitu.config.base.CallBack;
import com.cn.yitu.xutil.SharePreferenceXutil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class QueryHTTP extends BaseServer{

	/**
	 * 注册
	 * @param staff_name  姓名
	 * @param staff_age   年龄
	 * @param type_of_work_id 工种
	 * @param staff_sex 性别
	 * @param staff_phone 电话
	 * @param staff_password 密码
	 * @param registration_code 注册码
	 * @param code 短信验证码
	 * @param callBack 回调接口
	 */
	public void register(String staff_name,String staff_age,String type_of_work_id,String staff_sex,String staff_phone,String staff_password,String registration_code,String code,CallBack callBack){
		Map<String,String> map = new HashMap<String,String>();
		map.put("staff_name",staff_name);
		map.put("staff_age",staff_age);
		map.put("type_of_work_id",type_of_work_id);
		map.put("staff_sex",staff_sex);
		map.put("staff_phone",staff_phone);
		map.put("staff_password",staff_password);
		map.put("registration_code",registration_code);
		map.put("code",code);
		map.put("appKey", Constant.APPKEY);
		map.put("regid",SharePreferenceXutil.getChannelId());    //推送id
		post3("interface/mobile/update/register.do",map,callBack);
	}

	/**
	 * 登录
	 * @param staff_phone
	 * @param staff_password
	 * @param callBack
	 */
	public void login(String staff_phone,String staff_password,CallBack callBack){
		Map<String,String> map = new HashMap<>();
		map.put("staff_phone",staff_phone);
		map.put("staff_password",staff_password);
		map.put("regid", SharePreferenceXutil.getChannelId()); //推送id
		post3("interface/mobile/query/login.do",map,callBack);
	}

	/**
	 * 签到
	 * @param account_token
	 * @param lat
	 * @param lng
	 * @param callBack
	 */
	public void sign(String account_token, double lat, double lng, CallBack callBack){
		Map<String,String> map = new HashMap<>();
		map.put("account_token",account_token);
		map.put("lat",lat+"");
		map.put("lng",lng+"");
		post3("interface/mobile/update/sign_in.do",map,callBack);
	}

	/**
	 * 获取本月签到
	 * @param account_token
	 * @param callBack
	 */
	public void getSign(String account_token,CallBack callBack){
		Map<String,String> map = new HashMap<>();
		map.put("account_token",account_token);
		post3("interface/mobile/query/accessToSignIn.do",map,callBack);
	}

	/**
	 * 快速登录
	 * @param account_token
	 * @param callBack
	 */
	public void automaticLogin(String account_token,CallBack callBack){
		Map<String,String> map = new HashMap<>();
		map.put("account_token",account_token);
		post3("interface/mobile/query/automaticLogin.do",map,callBack);
	}


	/**
	 * 获取所有安保路线
	 * @param account_token
	 * @param callBack
	 */
	public void getSecurity(String account_token,CallBack callBack){
		Map<String,String> map = new HashMap<>();
		map.put("account_token",account_token);
		post3("interface/mobile/query/queryAllTheSecurityLine.do",map,callBack);
	}
	/**
	 * 获取所有保洁区域
	 * @param account_token
	 * @param callBack
	 */
	public void getClean(String account_token,CallBack callBack){
		Map<String,String> map = new HashMap<>();
		map.put("account_token",account_token);
		post3("interface/mobile/query/queryAllCleaningArea.do",map,callBack);
	}

	/**
	 * 绑定打扫区域
	 * @param account_token
	 * @param cleaning_area_id
	 * @param callBack
	 */
	public void bingClean(String account_token,int cleaning_area_id,CallBack callBack){
		Map<String,String> map = new HashMap<>();
		map.put("account_token",account_token);
		map.put("cleaning_area_id",cleaning_area_id+"");
		post3("interface/mobile/update/saveCleaningRecords.do",map,callBack);
	}

	/**
	 * 绑定安保路线
	 * @param account_token
	 * @param the_security_line_id
	 * @param callBack
	 */
	public void bingSecurity(String account_token,int the_security_line_id,CallBack callBack){
		Map<String,String> map = new HashMap<>();
		map.put("account_token",account_token);
		map.put("the_security_line_id",the_security_line_id+"");
		post3("interface/mobile/update/saveSecurityPatrolRecord.do",map,callBack);
	}

	/**
	 * 获取路线坐标集合
	 * @param account_token
	 * @param the_security_line_id
	 * @param callBack
	 */
	public void getLine(String account_token,int the_security_line_id,CallBack callBack){
		Map<String,String> map = new HashMap<>();
		map.put("account_token",account_token);
		map.put("the_security_line_id",the_security_line_id+"");
		post3("interface/mobile/query/tidQueryTheSecurityLine.do",map,callBack);
	}

	/**
	 * 获取所有游船
	 * @param account_token
	 * @param callBack
	 */
	public void getBoat(String account_token,CallBack callBack){
		Map<String,String> map = new HashMap<>();
		map.put("account_token",account_token);
		post3("interface/mobile/query/queryAllPleasureBoat.do",map,callBack);
	}

	/**
	 * 获取摆渡车
	 * @param account_token
	 * @param callBack
	 */
	public void getFerryCar(String account_token,CallBack callBack){
		Map<String,Object> map = new HashMap<>();
		map.put("account_token",account_token);
		get("interface/mobile/query/queryAllFerryPush.do",map,callBack);
	}

	/**
	 * 上传打扫完的图片
	 * @param callBack
	 */
	public void sendPhoto(Map<String,String> map, CallBack callBack){
		post3("interface/mobile/update/endCleaningRecordsBate64Upload.do",map,callBack);
	}

	/**
	 * 获取工种任务
	 * @param account_token
	 * @param callBack
	 */
	public void getTask(String account_token,CallBack callBack){
		Map<String,String> map = new HashMap<>();
		map.put("account_token",account_token);
		post3("interface/mobile/query/queryCraft.do",map,callBack);
	}

	/**
	 * 巡逻点报告
	 * @param map
	 * @param callBack
	 */
	public void sendReport(Map<String,String> map, CallBack callBack){
		post3("interface/mobile/update/updateNowLocationdsByte64.do",map,callBack);
	}

	/**
	 * 签退
	 * @param account_token
	 * @param callBack
	 */
	public void signOut(String account_token,double lat,double lng,CallBack callBack){
		Map<String,String> map = new HashMap<>();
		map.put("account_token",account_token);
		map.put("lat",lat+"");
		map.put("lng",lng+"");
		post3("interface/mobile/update/sign_out.do",map,callBack);
	}

	/*
	测试
	 */
	public void test(Map<String,String> map, CallBack callBack){
		post3("error/parameterTest.do",map,callBack);
	}
}
