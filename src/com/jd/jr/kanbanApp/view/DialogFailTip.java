package com.jd.jr.kanbanApp.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jd.jr.kanbanApp.R;
import com.jd.jr.kanbanApp.util.JDLog;
import com.jd.jr.kanbanApp.util.MessageID;

/**
 * 失败提示
 * @author liangyanbin
 *
 */
public class DialogFailTip extends Dialog {

	private LinearLayout linearTwo;
	private Button btn_one;
	private Handler handler;
	private Context context;
	private TextView tv_failReason;
    private int messageId;

    /**
     * 设置不同messageId在handler中实现不同功能
     * @param id
     */
    public void setMessageId(int id){
        this.messageId = id;
    }

	public void setFailReason(String msg) {
		tv_failReason.setText(msg);
	}

	public DialogFailTip(Context context) {
		super(context);
		this.context = context;
	}

	public DialogFailTip(Context context, int theme, Handler handler) {
		super(context, theme);
		this.context = context;
		this.handler = handler;
        this.messageId = MessageID.MESSAGE_FAIL_DIALOG_LOGIN;//默认为重新登录
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		JDLog.v("lyb", "dialog oncreate..");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_failtip);
		findView();
	}

	/**
	 * 设置单个按钮的文本
	 * @param buttonText
	 */
	public void setSingleButton(String buttonText){
		linearTwo.setVisibility(View.GONE);
		btn_one.setVisibility(View.VISIBLE);
		btn_one.setText(buttonText);
	}

	private void findView() {
		tv_failReason =  (TextView)findViewById(R.id.tv_failReason);
		linearTwo = (LinearLayout)findViewById(R.id.linearTwo);
//		btn_cancel = (Button) findViewById(R.id.btn_cancel);
//		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		btn_one = (Button) findViewById(R.id.btn_one);
		btn_one.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				handler.sendEmptyMessage(messageId);
				cancel();
				
			}
		});
	}
}
