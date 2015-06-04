package com.jd.jr.kanbanApp.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.jd.jr.kanbanApp.KanbanApp;
import com.jd.jr.kanbanApp.constant.Constants;


public class CommonBase {
	public static SharedPreferences sharedPreferences;
	
	/**
	 * 获取主配置文件
	 */
	public static SharedPreferences getJdSharedPreferences() {
		if (null == sharedPreferences) {
			JDLog.d("CommonUtil", " -->> sharedPreferences:" + sharedPreferences);
			sharedPreferences = KanbanApp.getInstance()//
					.getSharedPreferences(Constants.KANBAN_SHARE_PREFERENCE, Context.MODE_PRIVATE);
		}
		return sharedPreferences;
	}
	/**
	 * 获取SharedPreferences的String值
	 * @param keyString 
	 * @param defaultValue 默认值
	 * @return
	 */
	public static String getSPValueForString(String keyString,String defaultValue){
		return getJdSharedPreferences().getString("globalProvinceID", defaultValue);
	}

	/**
	 * 获取access_token
	 * @return
	 */
	public static String getAccessToken(){
		return getJdSharedPreferences().getString("access_token", "");
	}

	/**
	 * 保存access_token
	 * @param access_token
	 */
	public static void setAccessToken(String access_token){
		String accessToken = getAccessToken();
		if (!TextUtils.isEmpty(access_token) && !access_token.equals(accessToken)) {
			getJdSharedPreferences().edit().putString("access_token", access_token).commit();
		}
	}

	/**
	 * 获取登录账号
	 * @return
	 */
	public static String getUserId(){
		return getJdSharedPreferences().getString("userId", "");
	}

}
