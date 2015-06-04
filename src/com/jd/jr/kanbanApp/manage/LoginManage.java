package com.jd.jr.kanbanApp.manage;

import android.content.Context;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jd.jr.kanbanApp.KanbanApp;
import com.jd.jr.kanbanApp.data.MyJsonRequest;
import com.jd.jr.kanbanApp.data.RequestManager;
import com.jd.jr.kanbanApp.util.CommonUtil;
import com.jd.jr.kanbanApp.util.JDLog;
import com.jd.mrd.common.http.HttpError;
import com.jd.mrd.common.http.HttpRequestSetting;
import com.jd.mrd.common.http.HttpRequestSetting.AGREEMENT;
import com.jd.mrd.common.http.HttpRequestSetting.METHOD;
import com.jd.mrd.common.http.HttpResponse;
import com.jd.mrd.common.http.JDHttpRequest;
import com.jd.mrd.common.http.JDHttpRequest.IHttpTaskListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 登录管理类
 * @author YAOGUBIN
 *
 */
public class LoginManage {
	private String TAG = "LoginManage";
	private String name;
	private String pwd;
	private Context mContext;
	private boolean autoLogin;
	private LoginListener loginListener;
	
	public LoginManage(Context context){
		this.mContext = context;
	}
	/**
	 * 请求https登录
	 */
	public void loginToServiceHttps(String userName,String userPwd,boolean isAutoLogin,LoginListener listener){
		loginListener = listener;
		name = userName;
		pwd = userPwd;
		autoLogin = isAutoLogin;
		String loginUrlString = "http://jrrisk.jd.com:8080/KanbanAppController/login.do?username="+userName+"&password="+userPwd;
		HttpRequestSetting httpRequestSetting = new HttpRequestSetting(loginUrlString.toString(),false, new IHttpTaskListener(){

			@Override
			public void onSuccess(HttpResponse response) {
				// TODO Auto-generated method stub
				JDLog.d(TAG, "loginRequest--response-->>" + response.getJsonObject().toString());
				if(response.getJsonObject() != null){
					try {
						JSONObject loginObject = response.getJsonObject().getJSONObject("data");
						//保存登录成功信息
						saveLoginInfo(loginObject);
						loginListener.onLoginSuccess();
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						JDLog.e(TAG, "loginRequest.onSuccess()-->>"+e.getMessage());
					}
				}
				
			}

			@Override
			public void onError(HttpError error) {
				// TODO Auto-generated method stub
				JDLog.d(TAG, "Login error-->> " + error);
				loginListener.onLoginError(LoginListener.ERROR_CODE_LOGIC, error.getMessage());
			}

			@Override
			public void onProgress(int currProgress, int currDataLength,
					int maxDataLength) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				
			}
			
		});
		httpRequestSetting.setMethod(METHOD.GET);
		httpRequestSetting.setAgreement(AGREEMENT.HTTPS);
		httpRequestSetting.setVerificationCodeEnable(true);
		//加到任务里面
		JDHttpRequest.addTask(httpRequestSetting);
	}
	
	public void loginToService(String userName,String userPwd,boolean isAutoLogin,LoginListener listener){
		loginListener = listener;
		name = userName;
		pwd = userPwd;
		autoLogin = isAutoLogin;
		String urlStr = "http://10.8.4.73/KanbanAppController/login.do?username="+userName+"&password="+userPwd;
		JDLog.d(TAG, "login--Url-->>"+urlStr);
		MyJsonRequest myJsonRequest = new MyJsonRequest(Method.GET, urlStr, responseListener(), errorListener());
		RequestManager.addRequest(myJsonRequest, mContext);
	}
	private Response.Listener<JSONObject> responseListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					int code = response.getInt("code");
					if (code == 0) {
//						JSONObject dataJsonObject = response.getJSONObject("data");
						//保存登录成功信息
//						saveLoginInfo(dataJsonObject);
						loginListener.onLoginSuccess();
					}else {
						JDLog.e(TAG, "登录失败，信息："+response.getString("msg"));
						loginListener.onLoginError(LoginListener.ERROR_CODE_LOGIC, response.getString("msg"));
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					loginListener.onLoginError(LoginListener.ERROR_CODE_OTHER, e.getMessage());
				}
			}
		};
	}
	protected Response.ErrorListener errorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				JDLog.e(TAG, "登录异常，信息："+error.getMessage());
				loginListener.onLoginError(LoginListener.ERROR_CODE_NETWORK, error.getMessage());
			}
		};
	}

	 /**
     * 登录监听器，用于登录完成后的通知
     *
     * @author yaogubin
     */
    public interface LoginListener {
    	int ERROR_CODE_NETWORK = 1;// 登录失败代码：网络原因
        int ERROR_CODE_LOGIC = 2;// 登录失败代码：逻辑错误，服务器返回错误描述，如密码错误等
        int ERROR_CODE_OTHER = 3;// 登录失败代码：其它，如服务器未知错误，等
        
    	void onLoginError(int errorType, String msg);

        void onLoginSuccess();
    }
    /**
     * 保存登录成功信息
     * @param jsonObject
     */
    private void saveLoginInfo(JSONObject jsonObject){
    	try {
    		JDLog.i(TAG, "--response-->>ticket:"+jsonObject.getString("ticket"));
			KanbanApp.getUserInfo().setTicket(""+jsonObject.getString("ticket"));
//			CommonUtil.setUserId(name);
//			CommonUtil.setUserPwd(pwd);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
}
