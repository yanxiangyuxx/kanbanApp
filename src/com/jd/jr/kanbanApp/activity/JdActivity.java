package com.jd.jr.kanbanApp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;

import com.jd.jr.kanbanApp.R;
import com.jd.jr.kanbanApp.constant.ActionID;
import com.jd.jr.kanbanApp.data.HttpSetting;
import com.jd.jr.kanbanApp.data.MyJsonRequest;
import com.jd.jr.kanbanApp.constant.ResultCode;
import com.jd.jr.kanbanApp.data.RequestManager;
import com.jd.jr.kanbanApp.util.JDLog;
import com.jd.jr.kanbanApp.util.MessageID;
import com.jd.jr.kanbanApp.view.DialogFailTip;
import com.jd.jr.kanbanApp.view.LoadingDialog;
import org.json.JSONObject;

public abstract class JdActivity extends Activity {
	public Activity activity;
	public Handler mHandler = new Handler();
	protected boolean isLoaded = false;// 是否为已经加载过数据
	private String TAG = "JdActivity";// 日记tag
	private boolean isRestart = false;
	protected boolean needLoadOnStart = true;//onStart时是否需要加载网络数据 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
	}
	
	@Override
	protected void onRestart(){
		super.onRestart();
		isRestart = true;
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		JDLog.i(TAG, "onStart--->>");
		// 加载数据
		if(!isRestart){		
			if(needLoadOnStart){
				loadData();
			}
			
			isRestart = false;
		}
	}

	/**
	 * loading数据
	 */
	protected void loadData() {
		if (isLoaded) {
			Toast.makeText(activity, "已经加载", Toast.LENGTH_LONG).show();
			return;
		}
		// 实例一个请求对象
		MyJsonRequest stringRequest = new MyJsonRequest(Method.GET,
				onCreatHttpSetting().creatFinalUrl(), listener, errorListener);
		// 把请求加入到请求队列里面
		RequestManager.addRequest(stringRequest, this);
		// 显示正在加载对话框
		onShowLoading();
	}

	/**
	 * 请求返回监听
	 */
	Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject jsonObject) {
			// 解析JSON对象
			final Object obj = parser(jsonObject);
			mHandler.postDelayed(new Runnable() {
				public void run() {
					// 绘制界面
					onBindView(obj);
					// 关闭显示正在加载对话框
					onCloseLoading();
				}
			}, 100);
		}
	};
	/**
	 * 请求错误监听
	 */
	ErrorListener errorListener = new Response.ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
			// 显示错误信息
			onShowErrorView(error);
		}
	};

	/**
	 * 显示loading
	 */
	protected void onShowLoading() {
		LoadingDialog.show(this);
	}

	/**
	 * 关闭loading
	 */
	protected void onCloseLoading() {
		LoadingDialog.dismiss(this);
	}

	/**
	 * 显示错误信息
	 */
	protected void onShowErrorView(VolleyError error) {
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				onCloseLoading();
			}
		}, 100);

//		Toast.makeText(activity, TAG + "-->>" + error.getMessage(),
//				Toast.LENGTH_LONG).show();
        showWrongOrNoLoginDialog(ResultCode.VOLLEY_ERROE,"网络不给力，请稍后再试。");
	}

	/**
	 * 创建HttpSetting 组装请求body和URL
	 * 
	 * @return
	 */
	protected abstract HttpSetting onCreatHttpSetting();

	/**
	 * 绑定数据到UI控件
	 * 
	 * @param object
	 */
	protected abstract void onBindView(Object object);

	/**
	 * 把jsonObject转成具体的Object
	 * 
	 * @param jsonObject
	 * @return
	 */
	public abstract Object parser(JSONObject jsonObject);

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 销毁的时候取消网络请求
		RequestManager.cancelAll(this);
	}
	
	/**
	 * 会话失败后，重新登录的 及其他错误后重试的handler
	 */
	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MessageID.MESSAGE_FAIL_DIALOG_LOGIN:
				//关闭home页的广播
				Intent intent = new Intent(ActionID.ACTION_BROADCAST_EXIT);
				activity.sendBroadcast(intent);
				//跳转到登录页
				intent = new Intent(activity, LoginActivity.class);
				activity.startActivity(intent);
				activity.finish();
				break;
            case MessageID.MESSAGE_FAIL_DIALOG_VOLLEY_ERROR:
//                loadData();//重试
//                activity.finish();//返回上一个页面
                //不作处理 直接在DialogFailTip关闭对话框即可
                break;
			}
		}
	};
	/**
	 * 显示会话失败(未登录)对话框或其他错误
	 * @param code
	 * @param msg
	 */
	public void showWrongOrNoLoginDialog(int code, String msg) {
		if (code == ResultCode.CODE_NOT_LOGIN
				|| code == ResultCode.CODE_SESSION_FAIL) {
			DialogFailTip dialogFailTip = new DialogFailTip(activity,
					R.style.dialog_style, handler);
			Window win = dialogFailTip.getWindow();
			LayoutParams params = new LayoutParams();
			params.gravity = Gravity.CENTER;
			win.setAttributes(params);
			dialogFailTip.setCanceledOnTouchOutside(true);
			dialogFailTip.show();
			dialogFailTip.setFailReason(msg);
		}else if (code == ResultCode.VOLLEY_ERROE){
            DialogFailTip dialogFailTip = new DialogFailTip(activity,
                    R.style.dialog_style, handler);
            Window win = dialogFailTip.getWindow();
            LayoutParams params = new LayoutParams();
            params.gravity = Gravity.CENTER;
            win.setAttributes(params);
            dialogFailTip.setCanceledOnTouchOutside(true);
            dialogFailTip.show();
            dialogFailTip.setMessageId(MessageID.MESSAGE_FAIL_DIALOG_VOLLEY_ERROR);
            dialogFailTip.setFailReason(msg);
            dialogFailTip.setSingleButton("取消");
        }else {
			Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
		}

	}

}
