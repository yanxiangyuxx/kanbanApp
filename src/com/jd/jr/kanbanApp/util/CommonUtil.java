package com.jd.jr.kanbanApp.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.jd.jr.kanbanApp.R;


import java.io.ByteArrayOutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 工具类
 * 
 * @author liangyanbin
 * 
 */
public class CommonUtil extends CommonBase {

	// 上一次的联网方式
	public static String preNetApn;

	public static int dip2px(Context context, float dipValue) {
		return (int) (0.5F + dipValue
				* context.getResources().getDisplayMetrics().density);
	}

	public static int px2dip(Context context, float pxValue) {
		return (int) (0.5F + pxValue
				/ context.getResources().getDisplayMetrics().density);
	}

	public static float getDensity(Context context) {
		float desity = context.getResources().getDisplayMetrics().density;
		JDLog.v("lyb", "desity =" + desity);
		return desity;
	}

	/**
	 * 根据用户的数据类型打开相应的Activity。比如 tel:13400010001打开拨号程序，http://www.g.cn则会打开浏览器等。
	 * 
	 * @param activity
	 * @param url
	 */
	public static void openURL(Activity activity, String url) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		Uri content_url = Uri.parse(url);
		intent.setData(content_url);
		activity.startActivity(intent);
	}

	/**
	 * 调用系统中的打电话功能 (需要添加打电话权限：<uses-permission
	 * android:name="android.permission.CALL_PHONE" />)
	 * 
	 * @param activity
	 * @param phoneNumber
	 *            你要拨打的电话号码
	 */
	public static void call(Activity activity, String phoneNumber) {
		if (activity != null && phoneNumber != null && !phoneNumber.equals("")) {
			Intent intent = new Intent();
			intent.setAction("android.intent.action.CALL");
			intent.setData(Uri.parse("tel:" + phoneNumber));
			activity.startActivity(intent);
		}
	}

	/**
	 * 调用系统中的打电话功能
	 * 
	 * @param context
	 * @param phoneNumber
	 *            你要拨打的电话号码
	 */
	public static void call(Context context, String phoneNumber) {
		if (context != null && phoneNumber != null && !phoneNumber.equals("")) {
			Intent intent = new Intent();
			intent.setAction("android.intent.action.CALL");
			intent.setData(Uri.parse("tel:" + phoneNumber));
			context.startActivity(intent);
		}
	}

	/**
	 * 隐藏键盘
	 * 
	 * @param v
	 * @param context
	 */
	public static void hideSoftInput(View v, Context context) {
		final InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
	}

	/**
	 * 判断字符串是否是纯数字串
	 * 
	 * @param content
	 * @return
	 */
	public static boolean isAllNum(String content) {
		for (int i = 0; i < content.length(); i++) {
			int cAscii = (int) content.charAt(i);
			char charZero = '0';
			char char9 = '9';
			if (cAscii < (int) charZero || cAscii > (int) char9) {
				return false;
			}
		}
		return true;
	}

	// 转码
	public static String toUrlEncode(String str) {
		try {
			if (str == null)
				return null;
			return URLEncoder.encode(str, "UTF-8");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	// 解码
	public static String formUrlEncode(String urlEncodeStr) {
		try {
			if (urlEncodeStr == null)
				return null;
			return URLDecoder.decode(urlEncodeStr, "UTF-8");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	/**
	 * 加载本地图片
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getLoacalBitmap(String url) {
		try {
			java.io.FileInputStream fis = new java.io.FileInputStream(url);
			return BitmapFactory.decodeStream(fis);
		} catch (java.io.FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 用于将给定的内容生成成一维码 注：目前生成内容为中文的话将直接报错，要修改底层jar包的内容
	 * 
	 * @param content
	 *            将要生成一维码的内容
	 * @return 返回生成好的一维码bitmap
	 * @throws WriterException
	 *             WriterException异常
	 */
	public static Bitmap createOneDCode(String content) throws WriterException {
		// 生成一维条码,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
		BitMatrix matrix = new MultiFormatWriter().encode(content,
				BarcodeFormat.CODE_128, 500, 100);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = 0xff000000;
				}
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		// 通过像素数组生成bitmap,具体参考api
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	/**
	 * 将指定的内容生成成二维码
	 * 
	 * @param content
	 *            将要生成二维码的内容
	 * @return 返回生成好的二维码事件
	 * @throws WriterException
	 *             WriterException异常
	 */
	public static Bitmap createTwoDCode(String content) throws WriterException {
		// 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
		BitMatrix matrix = new MultiFormatWriter().encode(content,
				BarcodeFormat.QR_CODE, 300, 300);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		// 二维矩阵转为一维像素数组,也就是一直横着排了
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = 0xff000000;
				}
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		// 通过像素数组生成bitmap,具体参考api
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	/**
	 * 自定义toast
	 * 
	 * @param context
	 * @param showText
	 * @param isLong
	 */
	public static void showToast(Context context, String showText,
			boolean isLong) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View layout = inflater.inflate(R.layout.toast, null);
		TextView text = (TextView) layout.findViewById(R.id.toastText);
		text.setText(showText);
		Toast toast = new Toast(context);
		toast.setGravity(Gravity.CENTER, 0, 0);
		if (isLong) {
			toast.setDuration(Toast.LENGTH_LONG);
		}
		toast.setView(layout);
		toast.show();

	}

	/**
	 * 网络是否是打开的(WIFI\cmwap\cmnet)
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetWorkOpen(Context context) {
		boolean isOpen = false;
		try {
			ConnectivityManager cwjManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			isOpen = cwjManager.getActiveNetworkInfo().isAvailable();
		} catch (Exception ex) {
			JDLog.v("isNetWorkOpen", ex.toString());
			// 如果出异常，那么就是电信3G卡
			isOpen = false;
		}

		return isOpen;
	}

	/**
	 * 获取连接类型
	 * 
	 * @param context
	 * @return
	 */
	public static String getApnType(Context context) {
		String result = null;
		String proxy = null;
		try {
			if (isNetWorkOpen(context)) {
				ConnectivityManager mag = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				String type = mag.getActiveNetworkInfo().getTypeName();
				if (type.toLowerCase().equals("wifi")) {
					result = "wifi";
				} else {
					proxy = APNUtil.getCurrentAPNFromSetting(context).proxy;
					NetworkInfo mobInfo = mag
							.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
					result = mobInfo.getExtraInfo();
				}
			} else {
				return preNetApn == null ? "wifi" : preNetApn;
			}
		} catch (Exception e) {
			JDLog.v("tag", "CommonUtil:getApnType apn Error");
		} finally {
			// 如果出异常或者为null，则是电信3G卡
			if (result == null || result.indexOf("#") >= 0) {
				// if(proxy != null){
				// if(proxy.length() > 0){
				// result = "ctwap";
				// }else{
				// result = "ctnet";
				// }
				// }else{
				// result = "ctnet";
				// }
				result = preNetApn == null ? "wifi" : preNetApn;
			}
		}
		return result.toLowerCase();
	}

	// 地球半径（米）
	private static final Integer Radius = 6370856;

	/**
	 * 用于计算两个点之间的距离公式
	 * 
	 * @param lat1
	 * @param lng1
	 * @param lat2
	 * @param lng2
	 * @return
	 */
	public static float getDistance(float lat1, float lng1, float lat2,
			float lng2) {
		float x, y, distance = 0;
		try {
			x = (float) ((lng2 - lng1) * Math.PI * Radius
					* Math.cos(((lat1 + lat2) / 2) * Math.PI / 180) / 180);
			y = (float) ((lat2 - lat1) * Math.PI * Radius / 180);
			distance = (float) Math.hypot(x, y);
		} catch (Exception e) {

		}
		return distance;
	}

	/**
	 * 用于计算两个点之间的距离公式
	 * 
	 * @param strLat1
	 * @param strLng1
	 * @param strLat2
	 * @param strLng2
	 * @return
	 */
	public static int getDistance(String strLat1, String strLng1,
			String strLat2, String strLng2) {
		float distance = 0;
		try {
			float lat1 = Float.parseFloat(strLat1);
			float lng1 = Float.parseFloat(strLng1);
			float lat2 = Float.parseFloat(strLat2);
			float lng2 = Float.parseFloat(strLng2);
			float x, y;
			x = (float) ((lng2 - lng1) * Math.PI * Radius
					* Math.cos(((lat1 + lat2) / 2) * Math.PI / 180) / 180);
			y = (float) ((lat2 - lat1) * Math.PI * Radius / 180);
			distance = (float) Math.hypot(x, y);
		} catch (Exception e) {

		}
		return (int) distance;
	}

	public static byte[] bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}
	
	public static Bitmap bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}
	
	public static String bitmap2Base64String(Bitmap bm){
		return Base64.encodeToString(bitmap2Bytes(bm), Base64.DEFAULT);
	}

	public static Bitmap base64StringToBitmap(String base64String) {
		// 将字符串转换成Bitmap类型
		Bitmap bitmap = null;
		try {
			byte[] bitmapArray;
			bitmapArray = Base64.decode(base64String, Base64.DEFAULT);
			bitmap = bytes2Bimap(bitmapArray);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 消息提示框 
	 * 
	 * @param context
	 * @param showText
	 */
//	public static void showDialogComfirm(Context context, String showText) {
//		Handler handler = new Handler();
//		DialogFailTip dialogFailTip = new DialogFailTip(context,
//				R.style.dialog_style, handler);
//		Window win = dialogFailTip.getWindow();
//		LayoutParams params = new LayoutParams();
//		params.gravity = Gravity.CENTER;
//		win.setAttributes(params);
//		dialogFailTip.setCanceledOnTouchOutside(true);
//		dialogFailTip.show();
//
//		dialogFailTip.setFailReason(showText);
//        dialogFailTip.setMessageId(MessageID.MESSAGE_CONFIRM_DIALOG_SINGLE);
//        dialogFailTip.setSingleButton("确定");
//	}
	
	// *********************图片尺寸
	// n0: 608x608
	// n1: 350x350
	// n2： 160x160
	// n3: 130x130
	// n4: 100x100
	// n5: 50x50
	// 图片前缀。用于订单列表跟pic字段拼接 (如需要不同的尺寸,替换n2)
	public static String PIC_PREFIX = "http://img10.360buyimg.com/n2";

	// 屏幕的宽度
	public static int screen_width;
	// 屏幕的高度
	public static int screen_height;

}
