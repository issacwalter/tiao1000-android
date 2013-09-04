package com.zdmddd.activity;


import zl.android.http.ZLHttpParameters;
import zl.android.http.image_load.BitmapLoader;
import zl.android.utils.MyMath;
import zl.android.utils.StringHelper;

import com.zdmddd.R;
import com.zdmddd.config.UrlConfig;
import com.zdmddd.service.Service;
import com.zdmddd.service.ServiceFactory;
import com.zdmddd.service.UserService;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

public class RegisteActivity extends Activity {
	Button backButton;
	Button registeButton;
	Button cancelButton;
	
	EditText emailEditText;
	EditText nickNameEditText;
	EditText passwordEditText;
	EditText confirmEditText;
	EditText vcodeEditText;
	RadioButton manRadioButton;
	RadioButton womanRadioButton;
	RadioButton secretRadioButton;
	ImageView vcodeImgView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.registe_activity);
		
		_findViews();
		_bindEvent();
		_loadVcodeImg();
	}
	
	private void _findViews(){
		this.backButton = (Button)findViewById(R.id.back_button);
		this.registeButton = (Button)findViewById(R.id.registe);
		this.cancelButton = (Button)findViewById(R.id.cancel);
		
		this.emailEditText = (EditText)findViewById(R.id.email);
		this.nickNameEditText = (EditText)findViewById(R.id.nick_name);
		this.passwordEditText = (EditText)findViewById(R.id.password);
		this.confirmEditText = (EditText)findViewById(R.id.confirm_password);
		this.manRadioButton = (RadioButton)findViewById(R.id.radio_man);
		this.womanRadioButton = (RadioButton)findViewById(R.id.radio_woman);
		this.secretRadioButton = (RadioButton)findViewById(R.id.radio_secret);
		this.vcodeEditText = (EditText)findViewById(R.id.vcode);
		this.vcodeImgView = (ImageView)findViewById(R.id.vcode_img);
	}
	private void _bindEvent(){
		View.OnClickListener closeListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_closeRegisteActivity(REGISTE_STATUS_CANCEL);
			}
		};
		this.backButton.setOnClickListener(closeListener);
		this.cancelButton.setOnClickListener(closeListener);
		this.registeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String email = emailEditText.getText().toString();
				String nickName = nickNameEditText.getText().toString();
				String sex=null;
				if(manRadioButton.isChecked()){
					sex = SEX_MAN;
				}else if(womanRadioButton.isChecked()){
					sex=SEX_WOMAN;
				}else if(secretRadioButton.isChecked()){
					sex=SEX_SECRET;
				}
				String password = passwordEditText.getText().toString();
				String confirmPassword = confirmEditText.getText().toString();
				String vcode = vcodeEditText.getText().toString();
				String msg = _validate(email,nickName,sex,password,confirmPassword,vcode);
				if(msg!=null){
					Toast.makeText(RegisteActivity.this, msg, Toast.LENGTH_LONG).show();
					return;
				}
				
				ZLHttpParameters ap = new ZLHttpParameters();
				ap.put("email",email);
				ap.put("pwd", MyMath.encryptionWithMD5(password));
				ap.put("gender", sex);
				ap.put("vcode", vcode);
				ap.put("nick_name", nickName);
				ap.put("agree", "on");
				ServiceFactory.createService(UserService.class).registe(UrlConfig.REGISTE_URL, ap, new UserService.RegisteCallback() {
					@Override
					public void onComplete(int status, String msg) {
						if(Service.noticeExceptSuccess(getApplicationContext(), status, msg)) {
							_loadVcodeImg();
							return;
						}
						Toast.makeText(getApplicationContext(), "注册成功！", Toast.LENGTH_SHORT).show();
						_closeRegisteActivity(REGISTE_STATUS_SUCCESS);
					}
				});
			}
		});
		this.vcodeImgView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_loadVcodeImg();
			}
		});
	}
	
	public static final String SEX_MAN="m";
	public static final String SEX_WOMAN="f";
	public static final String SEX_SECRET="n";
	private String _validate(String email,String nick_name,String sex,String password,String confirmPassword,String vcode){
		String ret=null;
		if(email==null||email.equals("")){
			ret = "请输入邮箱地址！";
		}else if(!StringHelper.isEmail(email)){
			ret = "邮箱格式不正确！";
		}else if(nick_name==null||nick_name.trim().equals("")){
			ret = "请输入昵称！";
		}else if(!sex.equals(SEX_MAN)&&!sex.equals(SEX_SECRET)&&!sex.equals(SEX_WOMAN)){
			ret = "请正确选择性别！";
		}else if(password==null||password.equals("")){
			ret = "请输入密码！";
		}else if(!password.equals(confirmPassword)){
			ret = "两次密码输入不匹配！";
		}else if(vcode==null||vcode.equals("")){
			ret = "请输入验证码";
		}else if(vcode.trim().length()!=4){
			ret = "验证码输入错误";
		}
		return ret;
	}
	
	private void _loadVcodeImg(){
		new BitmapLoader(UrlConfig.VCODE_URL+"?d="+System.currentTimeMillis(),this.vcodeImgView).execute();
	}
	
	public static final int REGISTE_STATUS_SUCCESS=1;
	public static final int REGISTE_STATUS_CANCEL=2;
	private void _closeRegisteActivity(int registeStatus){
		this.setResult(registeStatus);
		this.finish();
	}
}
