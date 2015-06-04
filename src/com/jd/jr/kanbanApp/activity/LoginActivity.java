package com.jd.jr.kanbanApp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.jd.jr.kanbanApp.R;
import com.jd.jr.kanbanApp.data.RequestManager;
import com.jd.jr.kanbanApp.manage.LoginManage;
import com.jd.jr.kanbanApp.util.CommonUtil;
import com.jd.jr.kanbanApp.util.JDLog;
import com.jd.jr.kanbanApp.view.DialogNetwork;

/**
 * Created by Administrator on 2015/5/24.
 */
public class LoginActivity extends Activity {
    private Handler mHandler;
    private EditText edtUserName;
    private EditText edtUserPwd;
    private String userName;
    private String userPwd;
    private String TAG = "LoginActivity";
    private Button btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        loadBaseData();

        findById();

        setListener();
    }

    private View.OnClickListener loginClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            JDLog.d(TAG, "--login start-->>");
            userName = "" + edtUserName.getText();
            userPwd = "" + edtUserPwd.getText();
            if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPwd)) {
                Toast.makeText(LoginActivity.this, "请输入用户名密码", Toast.LENGTH_LONG).show();
            } else {
                LoginManage loginManage = new LoginManage(LoginActivity.this);

                loginManage.loginToService(userName, userPwd, false, new LoginManage.LoginListener() {

                    @Override
                    public void onLoginError(int errorType, String msg) {

                        JDLog.d(TAG, "--login end-->>");
                        switch (errorType) {
                            case LoginManage.LoginListener.ERROR_CODE_LOGIC:
                                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                break;
                            case LoginManage.LoginListener.ERROR_CODE_NETWORK:
                                DialogNetwork dialogNetwork = new DialogNetwork(LoginActivity.this,R.style.dialog_style,mHandler);
                                Window win = dialogNetwork.getWindow();
                                WindowManager.LayoutParams params = new WindowManager.LayoutParams();
                                params.gravity = Gravity.CENTER;
                                win.setAttributes(params);
                                dialogNetwork.setCanceledOnTouchOutside(true);
                                dialogNetwork.show();
                                break;
                            default:
                                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }

                    @Override
                    public void onLoginSuccess() {
                        Intent homeIntent = new Intent();
                        homeIntent.setClass(LoginActivity.this, HomeActivity.class);
                        startActivity(homeIntent);
                        finish();
                    }

                });
            }
        }
    };

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        RequestManager.cancelAll(this);
    }
    private void findById(){
        edtUserName = (EditText)findViewById(R.id.edt_username);
        edtUserName.setText(userName);
        edtUserName.setSelection(userName.length());
        edtUserPwd = (EditText)findViewById(R.id.edt_userpwd);
        btnLogin = (Button)findViewById(R.id.btn_login);

    }

    /**
     *
     */
    public void loadBaseData(){
        userName = CommonUtil.getUserId();
    }

    /**
     *
     */
    public void setListener() {
        btnLogin.setOnClickListener(loginClickListener);
    }
}
