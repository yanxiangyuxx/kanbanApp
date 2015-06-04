package com.jd.jr.kanbanApp.data;

import android.text.TextUtils;
import com.jd.jr.kanbanApp.config.Configuration;
import com.jd.jr.kanbanApp.util.JDLog;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;

/**
 * Copyright 2010 Jingdong Android Mobile Application
 * 
 * @author lijingzuo
 * 
 *         Time: 2010-12-27 下午05:26:55
 * 
 *         Name:
 * 
 *         Description: 连接信息封装
 */
public class HttpSetting implements Cloneable{
	private String host;
	private String functionId;
	private String url;
	private String finalUrl;
	private JSONObject jsonParams;
	private Map<String, String> mapParams;
	private boolean post = false;
	private int attempts;// 重试次数
	private boolean NeedEncrypt;
	private boolean isUseCookies = true; // 是否使用Cookies

	private int readTimeout;
	private String body;
	private String TAG="HttpSetting";
	
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	private int connectTimeout;

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}



	public boolean isUseCookies() {
		return isUseCookies;
	}

	public void setUseCookies(boolean isUseCookies) {
		this.isUseCookies = isUseCookies;
	}


	public boolean isNeedEncrypt() {
		return NeedEncrypt;
	}

	public void setNeedEncrypt(boolean needEncrypt) {
		NeedEncrypt = needEncrypt;
	}




	public int getAttempts() {
		return attempts;
	}

	// 重试次数追加1次
	public void appendOneAttempts() {
		attempts++;
	}

	public void setAttempts(int attempts) {
		this.attempts = attempts;
	}

	public String getFunctionId() {
		return functionId;
	}

	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFinalUrl() {
		return finalUrl;
	}

	public void setFinalUrl(String finalUrl) {
		this.finalUrl = finalUrl;
	}

	public JSONObject getJsonParams() {
		if (null == jsonParams) {
			jsonParams = new JSONObject();
		}
		return jsonParams;
	}

	/**
	 * 批量添加参数
	 */
	@Deprecated
	public void setJsonParams(JSONObject params) {
		if (null == params) {
			return;
		}
		try {
			this.jsonParams = new JSONObject(params.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void putJsonParam(String key, Object value) {
		if (null == this.jsonParams) {
			this.jsonParams = new JSONObject();
		}
		try {
			this.jsonParams.put(key, value);
		} catch (JSONException e) {

		}
	}

	public Map<String, String> getMapParams() {
		return mapParams;
	}

	/**
	 * 批量添加参数
	 */
	@Deprecated
	public void setMapParams(Map<String, String> mapParams) {
		if (null == mapParams) {
			return;
		}
		Set<String> keySet = mapParams.keySet();
		for (String key : keySet) {
			putMapParams(key, mapParams.get(key));
		}
	}

	public void putMapParams(String key, String value) {
		if (null == this.mapParams) {
			this.mapParams = new URLParamMap();
		}
		this.mapParams.put(key, value);
	}

	public boolean isPost() {
		return post;
	}

	public void setPost(boolean post) {
		this.post = post;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
	
	public String creatFinalUrl() {
		if (getHost() == null) {
			this.setHost(Configuration.getProperty(Configuration.HOST));
		}
		if (null != getFunctionId() && getUrl() == null) {
			//开发环境
			//setUrl("http://" + getHost() + "/o2o-smart-web-service/"+getFunctionId());
			//测试环境
			setUrl("http://" + getHost() +"/"+getFunctionId());
		}
			StringBuilder url = new StringBuilder(this.getUrl());
			String body = creatBody();
			// GET
			if (!TextUtils.isEmpty(body)&&!isPost()) {
				url.append("?");
				url.append("body="+body);

			}else {
				//TODO
			}
			JDLog.d(TAG, "--body-->>" + body);
			JDLog.i(TAG, " creatFinalUrl========================="
				+ url.toString());
		return url.toString();
	}
	
	public String creatBody(){
		String parString = "";
		if (getJsonParams() != null) {
			parString = getJsonParams().toString();	
			//putMapParams("body", body);
		}
		
		/*if (null != this.getMapParams()) {
			if (getJsonParams() != null) {
				String body = getJsonParams().toString();	
				putMapParams("body", body);
			}
			
			StringBuilder url = new StringBuilder();
			Map<String, String> mapParams = this.getMapParams();
			Set<String> keySet = mapParams.keySet();
			for (Iterator<String> iterator = keySet.iterator(); iterator
					.hasNext();) {
				String key = iterator.next();
				String value = mapParams.get(key);
				url.append(key).append("=").append(value);
				if (iterator.hasNext()) {
					url.append("&");
				}
			}

		return url.toString();
		}*/
		return parString;	
	}
	
	@Override
	public Object clone() {
		HttpSetting httpSetting = null;

		try {
			httpSetting = (HttpSetting) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		if (httpSetting != null) {
			httpSetting.setJsonParams(this.getJsonParams());
		}
		
		if (httpSetting != null) {
			httpSetting.mapParams = httpSetting.getMapParams();
		}

		return httpSetting;
	}
	
	
}