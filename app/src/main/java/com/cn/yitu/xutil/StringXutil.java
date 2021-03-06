package com.cn.yitu.xutil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringXutil {
	/**
	 * 判断String是否为空或null
	 * 
	 * @param msg
	 * @return true:为空或null
	 */
	public static boolean isEmpty(String msg) {
		if (msg == null || msg.equals("")) {
			return true;
		}
		return false;
	}

	/**
	 * 判断字符串是否是邮箱格式
	 * 
	 * @param strEmail
	 * @return true:字符串是邮箱格式
	 */
	public static boolean isEmail(String strEmail) {
		String strPattern = "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strEmail);
		return m.matches();
	}

	/**
	 * 判断手机号码是否正确
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public static boolean isPhoneNumberValid(String phoneNumber) {
		boolean isValid = false;

		String expression = "((^(13|14|15|17|18)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))";
		CharSequence inputStr = phoneNumber;

		Pattern pattern = Pattern.compile(expression);

		Matcher matcher = pattern.matcher(inputStr);

		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

}
