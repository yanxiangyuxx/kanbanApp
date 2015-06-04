package com.jd.jr.kanbanApp.util;


import android.util.Log;
import com.jd.jr.kanbanApp.config.Configuration;

public class JDLog {

	public static void v(String tag, String message) {
		if (Configuration.LOGMODE_V || Configuration.LOGMODE) {
			if (message != null) {
				Log.v(tag, message);
			}
			;
		}
	}

	public static void i(String tag, String message) {
		if (Configuration.LOGMODE_I || Configuration.LOGMODE) {

			if (message != null) {
				Log.i(tag, message);
			}
		}
	}

	public static void e(String tag, String message) {
		if (Configuration.LOGMODE_E || Configuration.LOGMODE) {
			if (message != null) {
				Log.e(tag, message);
			}

		}
	}

	public static void w(String tag, String message) {
		if (Configuration.LOGMODE_W || Configuration.LOGMODE) {
			if (message != null) {
				Log.w(tag, message);
			}
		}
	}

	public static void d(String tag, String message) {
		if (Configuration.LOGMODE_D || Configuration.LOGMODE) {
			if (message != null) {
				Log.d(tag, message);
			}

		}
	}

}
