package com.jd.jr.kanbanApp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Proxy;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.jd.jr.kanbanApp.KanbanApp;



public class NetUtils {

	private static final String TAG = "NetUtils";

	private static final int NO_NET = 2147483647;
	private static final int UNKNOWN = 2147483646;
	private static final int WIFI = 2147483645;
	private static final int ROAMING = 2147483644;
	
	public static final String NETWORK_TYPE_2G = "2g";// 提交到服务器的网络类型：2g
	public static final String NETWORK_TYPE_3G = "3g";// 提交到服务器的网络类型：3g
	public static final String NETWORK_TYPE_WIFI = "wifi";// 提交到服务器的网络类型：wifi


	/**
	 * 网络连接使用代理：默认，优先判断用户是否用代理</br> false:使用直连方式连接网络</br> true:使用当前代理IP与PORT方式连接网
	 * 
	 */
	public static boolean isProxy = true;

	public static NetType getNetType() {

		// if (netInfo.isRoaming()) {
		// // 漫游
		// }

		NetType result = new NetType();//默认为空内容

		ConnectivityManager connectivityManager = null;
		try {
			connectivityManager = (ConnectivityManager) KanbanApp.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
		} catch (Exception e) {
		}

		if (null == connectivityManager) {
			return result;
		}

		if (!isNetworkAvailable(connectivityManager)) {
			return result;
		}

		int summaryType = getSummaryType(connectivityManager);

		NetworkInfo networkInfo = null;
		try {
			networkInfo = connectivityManager.getActiveNetworkInfo();
		} catch (Throwable e) {
		}

		String extraInfo = getExtraInfo(networkInfo);

		result = new NetType(summaryType, extraInfo);

		return result;

		// NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
		//
		// if (netInfo != null && netInfo.isConnected()) {
		// }

	}

	/**
	 * 判断是否有网络（遍历NetworkInfo）
	 */
	public static boolean isNetworkAvailable(ConnectivityManager connectivityManager) {

		NetworkInfo[] networkInfos = null;
		try {
			networkInfos = connectivityManager.getAllNetworkInfo();
		} catch (Throwable e) {
		}
		if (networkInfos != null) {
			final int length = networkInfos.length;
			for (int i = 0; i < length; i++) {
				boolean connected = false;
				try {
					connected = networkInfos[i].getState() == State.CONNECTED;
				} catch (Throwable e) {
				}
				if (connected) {
					return true;
				}
			}
		}

		return false;
	}


	/**
	 * 是否有网络
	 *
	 * @return
	 */
	public static boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = null;
		try {
			connectivityManager = (ConnectivityManager) KanbanApp.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
		} catch (Exception e) {
			JDLog.d(TAG, " isNetworkAvailable -->> " + e.getMessage());
			e.printStackTrace();
		}

		if (null == connectivityManager) {
			return false;
		}

		return isNetworkAvailable(connectivityManager);
	}


	/**
	 * 判断是“手机网络”还是“无线网络”
	 */
	public static int getSummaryType(ConnectivityManager connectivityManager) {

		int result = NetType.SUMMARY_TYPE_OTHER;

		// mobile
		State mobile = null;
		try {
			mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
		} catch (Throwable e) {
		}
		// wifi
		State wifi = null;
		try {
			wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		} catch (Throwable e) {
		}

		if (mobile == State.CONNECTED || mobile == State.CONNECTING) {
			// mobile
			result = NetType.SUMMARY_TYPE_MOBILE;
		} else if (wifi == State.CONNECTED || wifi == State.CONNECTING) {
			// wifi
			result = NetType.SUMMARY_TYPE_WIFI;
		}

		return result;
	}

	/**
	 * 网络额外信息
	 */
	public static String getExtraInfo(NetworkInfo networkInfo) {
		String result = "";
		try {
			result = networkInfo.getExtraInfo();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return result;
	}

	public static class NetType {

		public static final int NSP_CHINA_MOBILE = 1;// 移动
		public static final int NSP_CHINA_UNICOM = 2;// 联通
		public static final int NSP_CHINA_TELECOM = 3;// 电信
		public static final int NSP_OTHER = 0;// 其它
		public static final int NSP_NO = -1;// 不可用

		public static final int SUMMARY_TYPE_WIFI = 1;// WIFI
		public static final int SUMMARY_TYPE_MOBILE = 2;// MOBILE
		public static final int SUMMARY_TYPE_OTHER = 0;// 其它

		private String extraInfo;

		private int summaryType = SUMMARY_TYPE_OTHER;
		private String detailType;

		Integer simState;
		String networkType;
		String networkTypeName;
		String networkOperator;
		String networkOperatorName;

		String proxyHost;
		Integer proxyPort;

		public NetType(int summaryType, String extraInfo) {
			this.summaryType = summaryType;
			this.extraInfo = extraInfo;
			getSimAndOperatorInfo();
		}
		
		public NetType() {
		}

		private void getSimAndOperatorInfo() {
			TelephonyManager telephonyManager = (TelephonyManager) KanbanApp.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
			try {
				simState = telephonyManager.getSimState();
			} catch (Throwable e) {
			}
			try {
				networkOperatorName = telephonyManager.getNetworkOperatorName();
			} catch (Throwable e) {
			}
			try {
				networkOperator = telephonyManager.getNetworkOperator();
			} catch (Throwable e) {
			}
			try {
				int temp = telephonyManager.getNetworkType();
				networkType = "" + temp;
				networkTypeName = getNetworkTypeName(temp);
			} catch (Throwable e) {
			}

		}

		public int getNSP() {

			if (null == simState || simState == TelephonyManager.SIM_STATE_UNKNOWN) {
				return NSP_NO;
			}
			if (TextUtils.isEmpty(networkOperatorName) && TextUtils.isEmpty(networkOperator)) {
				return NSP_NO;
			}

			if (("中国移动".equalsIgnoreCase(networkOperatorName))//
					|| ("CMCC".equalsIgnoreCase(networkOperatorName)) //
					|| ("46000".equalsIgnoreCase(networkOperator))//
					|| ("China Mobile".equalsIgnoreCase(networkOperatorName))) {
				// 中国移动
				return NSP_CHINA_MOBILE;
			}
			if (("中国电信".equalsIgnoreCase(networkOperatorName))//
					|| ("China Telecom".equalsIgnoreCase(networkOperatorName))//
					|| ("46003".equalsIgnoreCase(networkOperator))) {
				// 中国电信
				return NSP_CHINA_TELECOM;
			}
			if (("中国联通".equalsIgnoreCase(networkOperatorName))//
					|| ("China Unicom".equalsIgnoreCase(networkOperatorName))//
					|| ("46001".equalsIgnoreCase(networkOperator))//
					|| ("CU-GSM".equalsIgnoreCase(networkOperatorName))) {
				// 中国联通
				return NSP_CHINA_UNICOM;
			}

			return NSP_OTHER;
		}

		public String getNetworkTypeName(int code) {
			switch (code) {
			case TelephonyManager.NETWORK_TYPE_GPRS:
				return "GPRS";
			case TelephonyManager.NETWORK_TYPE_EDGE:
				return "EDGE";
			case TelephonyManager.NETWORK_TYPE_UMTS:
				return "UMTS";
			case TelephonyManager.NETWORK_TYPE_HSDPA:
				return "HSDPA";
			case TelephonyManager.NETWORK_TYPE_HSUPA:
				return "HSUPA";
			case TelephonyManager.NETWORK_TYPE_HSPA:
				return "HSPA";
			case TelephonyManager.NETWORK_TYPE_CDMA:
				return "CDMA";
			case TelephonyManager.NETWORK_TYPE_EVDO_0:
				return "CDMA - EvDo rev. 0";
			case TelephonyManager.NETWORK_TYPE_EVDO_A:
				return "CDMA - EvDo rev. A";
			case TelephonyManager.NETWORK_TYPE_1xRTT:
				return "CDMA - 1xRTT";
			default:
				return "UNKNOWN";
			}
		}

		public String getDetailType() {
			// StringBuilder sb = new StringBuilder();
			return "";
		}

		public String getUploadType() {
			return networkType;
		}

		public String getProxyHost() {
			String proxyHost = Proxy.getDefaultHost();
			JDLog.d(TAG, "getProxyHost() proxyHost -->> " + proxyHost);
			if (SUMMARY_TYPE_WIFI == summaryType) {// 魅族切换到WIFI时proxyHost仍然按切换前返回，导致需要如此处理。
				JDLog.d(TAG, "getProxyHost() WIFI -->> ");
				return null;
			} else {
				JDLog.d(TAG, "getProxyHost() else -->> ");
				this.proxyHost = proxyHost;
				this.proxyPort = Proxy.getDefaultPort();
			}
			JDLog.d(TAG, "getProxyHost() proxyHost -->> " + proxyHost);
			return this.proxyHost;
		}

		public Integer getProxyPort() {
			return proxyPort;
		}

	}

	/**
	 * 
	 *    public static final int NETWORK_TYPE_UNKNOWN = 0;
	 *    public static final int NETWORK_TYPE_GPRS = 1;	// 2.5G
	 *    public static final int NETWORK_TYPE_EDGE = 2;	// 2.75G （模拟器中的网络类型就是这一种）
	 *    public static final int NETWORK_TYPE_UMTS = 3;	// 3G
	 *    public static final int NETWORK_TYPE_CDMA = 4;	// 2G
	 *    public static final int NETWORK_TYPE_EVDO_0 = 5;	// 3G
	 *    public static final int NETWORK_TYPE_EVDO_A = 6;	// 3G
	 *    public static final int NETWORK_TYPE_1xRTT = 7;	// 3G
	 *    public static final int NETWORK_TYPE_HSDPA = 8;	// 3.5G
	 *    public static final int NETWORK_TYPE_HSUPA = 9;	// 3G
	 *    public static final int NETWORK_TYPE_HSPA = 10;	// 3G
	 *    public static final int NETWORK_TYPE_EVDO_B = 12;	// 3G
	 */
	public static boolean is2GNetwork(Context context) {
		
		NetType netType = getNetType();
		if(netType.summaryType == NetType.SUMMARY_TYPE_WIFI) {
			return false;
		}
		
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		int type = telephonyManager.getNetworkType();
		JDLog.d(TAG, "Net work type:" + type);
		if(TelephonyManager.NETWORK_TYPE_CDMA == type || TelephonyManager.NETWORK_TYPE_GPRS == type || TelephonyManager.NETWORK_TYPE_EDGE == type) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否是wifi环境
	 * @return
	 */
	public static boolean isWifi() {
		NetType netType = getNetType();
		if(netType.summaryType == NetType.SUMMARY_TYPE_WIFI) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * 获取网络类型，用于传给服务器2g/3g/wifi
	 * @return
	 */
	public static String getNetworkType() {
		NetType netType = getNetType();
		if (netType.summaryType == NetType.SUMMARY_TYPE_WIFI) {
			return NETWORK_TYPE_WIFI;
		} else if (netType.summaryType == NetType.SUMMARY_TYPE_MOBILE) {

			final TelephonyManager telephonyManager = (TelephonyManager) KanbanApp.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
			final int type = telephonyManager.getNetworkType();
			if (TelephonyManager.NETWORK_TYPE_CDMA == type || TelephonyManager.NETWORK_TYPE_GPRS == type || TelephonyManager.NETWORK_TYPE_EDGE == type) {
				return NETWORK_TYPE_2G;
			} else {
				return NETWORK_TYPE_3G;
			}
		}

		return "";
	}

	
}
