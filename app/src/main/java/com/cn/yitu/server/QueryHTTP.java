package com.cn.yitu.server;

import com.cn.yitu.config.base.BaseServer;
import com.cn.yitu.config.base.CallBack;
import com.cn.yitu.xutil.SharePreferenceXutil;

import java.util.HashMap;
import java.util.Map;


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
}
