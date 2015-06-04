package com.jd.jr.kanbanApp.constant;
/**
 * 请求的code
 * @author liangyanbin
 *
 */
public class ResultCode {
	/**
	 * 成功
	 */
	public static final int CODE_SUCCESS = 0;
	/**
	 * 参数错误
	 */
	public static final int CODE_PARAM_ERROR = 1;
	/**
	 * 方法不存在
	 */
	public static final int CODE_METHOD_NOTEXIST = 2;
	/**
	 * 用户未登陆
	 */
	public static final int CODE_NOT_LOGIN = 3;
	/**
	 * 失败
	 */
	public static final int CODE_FAIL = -1;
	/**
	 * 批处理的部分失败
	 */
	public static final int CODE_PART_FAIL = -2;
	/**
	 * 系统异常
	 */
	public static final int CODE_SYSTEM_ABNORMAL = -3;
	/**
	 * 会话失效
	 */
	public static final int CODE_SESSION_FAIL = 400;
    /**
     * volley Response返回的失败
     */
    public static final int VOLLEY_ERROE = 500;
}
