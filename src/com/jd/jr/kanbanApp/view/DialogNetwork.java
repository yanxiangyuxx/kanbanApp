package com.jd.jr.kanbanApp.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import com.jd.jr.kanbanApp.R;
import com.jd.jr.kanbanApp.util.MessageID;


public class DialogNetwork extends Dialog {
	private Button btn_one;
	private Handler handler;
	private Context context;

	public DialogNetwork(Context context) {
		super(context);
		this.context = context;
	}
	public DialogNetwork(Context context, int theme, Handler handler){
		super(context, theme);
		this.context = context;
		this.handler = handler;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_network);
		findView();
	}

	private void findView() {
		btn_one = (Button) findViewById(R.id.btn_one);
		btn_one.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				handler.sendEmptyMessage(MessageID.MESSAGE_FAIL_DIALOG_NETWORK);
				cancel();
			}
		});
	}

}
