package com.zdmddd.activity;

import zl.android.http.ZLHttpParameters;
import zl.android.http.ZLHttpService;
import zl.android.utils.MyMath;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zdmddd.R;
import com.zdmddd.config.UrlConfig;
import com.zdmddd.modal.User;
import com.zdmddd.service.Service;
import com.zdmddd.service.ServiceFactory;
import com.zdmddd.service.UserService;

public class LoginActivity extends BaseActivity{
	Button loginButton;
	Button cancelButton ;
	EditText account;
	EditText password;
	Button registeButton;
	TextView titleText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		
		this.titleText.setText("登陆");
	}
	@Override
	protected void _findViews() {
		this.loginButton = (Button)findViewById(R.id.login);
		this.cancelButton = (Button)findViewById(R.id.cancel);
		this.account = (EditText)findViewById(R.id.account);
		this.password = (EditText)findViewById(R.id.password);
		this.registeButton = (Button)findViewById(R.id.registe);
		this.titleText = (TextView)findViewById(R.id.title_text);
	}
	@Override
	protected void _bindEvent() {
		this.loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onLoginButtonClick(v);
			}
		});
		this.cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		this.registeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onRegisteButtonClick(v);
			}
		});
	}
	
	public void onLoginButtonClick(View v){
		String email = account.getText().toString();
		String pwd = password.getText().toString();
		if(email.equals("")|| pwd.equals("")){
			Toast.makeText(LoginActivity.this, "请输入用户名或密码！", Toast.LENGTH_LONG).show();
			return;
		}
		
		ZLHttpParameters ajaxParams = new ZLHttpParameters();
		ajaxParams.put("email", email);
		ajaxParams.put("pwd", MyMath.encryptionWithMD5(pwd));
		ServiceFactory.createService(UserService.class).login(UrlConfig.LOGIN_URL, ajaxParams, new UserService.LoginCallback() {
			@Override
			public void onComplete(int status, String msg, User self) {
				if(Service.noticeExceptSuccess(LoginActivity.this, status, msg)) return;
				app.setAttribute("self", self);
				app.saveInfoToDb("self", self);
				ZLHttpService.addCookie("un", account.getText().toString());
				Toast.makeText(LoginActivity.this, "登陆成功！", Toast.LENGTH_SHORT).show();
				_finishLoginActivity(LOGIN_STATUS_SUCCESS);
			}
		});
	}
	public void onRegisteButtonClick(View v){
		Intent intent = new Intent();
		intent.setClass(LoginActivity.this, RegisteActivity.class);
		this.startActivityForResult(intent, 1);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==1){
			/*RegisteActivity*/
			if(resultCode == RegisteActivity.REGISTE_STATUS_SUCCESS){
				final ProgressDialog progressDlg = new ProgressDialog(LoginActivity.this);
				progressDlg.setCancelable(false);
				progressDlg.setTitle("登录中");
				progressDlg.show();
				
				ServiceFactory.createService(UserService.class).helpLogin(app,new UserService.HelpLoginCallback() {
					@Override
					public void onComplete(int status, String msg, User self) {
						progressDlg.dismiss();
						if(Service.noticeExceptSuccess(LoginActivity.this, status, msg))return;
						_finishLoginActivity(LOGIN_STATUS_SUCCESS);
					}
				});
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * 用户登录成功
	 */
	public static final int LOGIN_STATUS_SUCCESS=1;
	/**
	 * 用户登录失败
	 */
	public static final int LOGIN_STATUS_FAILED=2;
	/**
	 * 用户点击了取消按钮
	 */
	public static final int LOGIN_STATUS_CANCEL=3;
	private void _finishLoginActivity(int loginStatus){
		this.setResult(loginStatus);
		this.finish();
	}
	public void onBackButtonClick(View v){
		_finishLoginActivity(LOGIN_STATUS_CANCEL);
	}
}
