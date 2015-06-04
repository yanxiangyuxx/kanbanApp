/*
 * Created by Storm Zhang, Feb 11, 2014.
 */

package com.jd.jr.kanbanApp.data;

import android.app.ActivityManager;
import android.content.Context;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class RequestManager {
	private static RequestQueue mRequestQueue;
	private static ImageLoader mImageLoader;
	private static String TAG = "RequestManager";

	private RequestManager() {
		// no instances
	}
	/**
	 * 初始化请求对象队列
	 * @param context
	 */
	public static void init(Context context) {
		mRequestQueue = Volley.newRequestQueue(context);

		int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE))
				.getMemoryClass();
		// Use 1/8th of the available memory for this memory cache.
		int cacheSize = 1024 * 1024 * memClass / 8;
		mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(cacheSize));
	}

	public static RequestQueue getRequestQueue() {
		if (mRequestQueue != null) {
			return mRequestQueue;
		} else {
			throw new IllegalStateException("RequestQueue not initialized");
		}
	}
	/**
	 * 把请求对象加到队列中
	 * @param request
	 * @param tag
	 */
	public static void addRequest(Request<?> request, Object tag) {
        if (tag != null) {
        	/** 
        	* Constructs a new retry policy. 构建一个新的重试策略 
        	* @param initialTimeoutMs The initial timeout for the policy. 超时时间 
        	* @param maxNumRetries The maximum number of retries. 最多重试次数 
        	* @param backoffMultiplier Backoff multiplier for the policy. 
        	*/ 
        	request.setRetryPolicy(new DefaultRetryPolicy(5000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        	//设置网络请求排序
        	//request.setSequence(0);
//        	JDLog.i(TAG, "addRequest-->>initialTimeoutMs--"+DefaultRetryPolicy.DEFAULT_TIMEOUT_MS);
//        	JDLog.i(TAG, "addRequest-->>maxNumRetries--"+DefaultRetryPolicy.DEFAULT_MAX_RETRIES);
//        	JDLog.i(TAG, "addRequest-->>backoffMultiplier--"+DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setTag(tag);
        }
        mRequestQueue.add(request);
    }
	/**
	 * 取消请求
	 * @param tag
	 */
	public static void cancelAll(Object tag) {
        mRequestQueue.cancelAll(tag);
    }

	/**
	 * Returns instance of ImageLoader initialized with {@see FakeImageCache}
	 * which effectively means that no memory caching is used. This is useful
	 * for images that you know that will be show only once.
	 * 
	 * @return
	 */
	public static ImageLoader getImageLoader() {
		if (mImageLoader != null) {
			return mImageLoader;
		} else {
			throw new IllegalStateException("ImageLoader not initialized");
		}
	}
}
