package com.jd.jr.kanbanApp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jd.jr.kanbanApp.R;
import com.jd.jr.kanbanApp.constant.Constants;
import com.jd.jr.kanbanApp.data.RequestManager;
import com.jd.jr.kanbanApp.util.JDLog;
import com.jd.jr.kanbanApp.util.MessageID;
import com.jd.jr.kanbanApp.util.NetUtils;
import com.jd.jr.kanbanApp.view.DialogNetwork;
import com.jd.mrd.common.http.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * 欢迎界面
 * @author YAOGUBIN
 *
 */
public class WelcomeActivity extends Activity {
	private ImageView appNameImg;
	private SeekBar downloadSeekbar = null;
	private TextView downloadTipTv = null;
	private boolean isAutoLogin = false;
	private Handler mHandler;
	private String version = null;
	private String packageName = null;
	private String TAG ="WelcomeActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_activity);

		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
					case MessageID.MESSAGE_FAIL_DIALOG_NETWORK:
						Intent intent = null;
						if (android.os.Build.VERSION.SDK_INT > 10) {
							intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
						}else {
							intent = new Intent();
							ComponentName component = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
		                    intent.setComponent(component);
		                    intent.setAction("android.intent.action.VIEW");
						}
						startActivity(intent);
						finish();
						//Toast.makeText(WelcomeActivity.this, "您的网络已断开请设置", Toast.LENGTH_LONG).show();
						break;
				}
			}
			
		}; 
		PackageManager manager = this.getPackageManager();
		try {
			PackageInfo info;
			info = manager.getPackageInfo(this.getPackageName(), 0);
			version = info.versionName;
			packageName = info.packageName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//初始化控件
		findViews();
		//判断网络是否正常
		if (NetUtils.isNetworkAvailable()) {
			startAnimationForAppNameImg();
		}else {
			DialogNetwork dialogNetwork = new DialogNetwork(WelcomeActivity.this,R.style.dialog_style,mHandler);
			Window win = dialogNetwork.getWindow();
			LayoutParams params = new LayoutParams();
			params.gravity = Gravity.CENTER;
			win.setAttributes(params);
			dialogNetwork.setCanceledOnTouchOutside(true);
			dialogNetwork.show();
		}
	}
	
	
	/**
	 * 初始化控件
	 */
	public void findViews(){
		appNameImg = (ImageView)findViewById(R.id.img_appname);
		downloadTipTv = (TextView) findViewById(R.id.welcome_download_progress_tip_tv);
	}
	
	/**
	 * 加载设置基础数据
	 */
	public void showControl() {

			
			Intent loginIntent = new Intent();
			loginIntent.setClass(WelcomeActivity.this, LoginActivity.class);
			startActivity(loginIntent);
			finish();
			
	}
	private void startAnimationForAppNameImg(){
		//app名称图片动效
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.welcome_appname);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub 
				if (NetUtils.isWifi()) {
					//检查版本更新
					checkUpdate();
				} else {
					showControl();
				}
			}
		});
		appNameImg.startAnimation(animation);
	}

	private void checkUpdate() {
		//TODO

		showControl();
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//销毁的时候取消网络请求
		RequestManager.cancelAll(this);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
}
