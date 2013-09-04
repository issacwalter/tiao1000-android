package com.zdmddd.activity;

import com.zdmddd.R;
import com.zdmddd.application.ZLApplication;
import com.zdmddd.modal.User;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public abstract class BaseActivity extends Activity {
	protected ZLApplication app;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (ZLApplication)getApplication();
	}
	
	@Override
	public void setContentView(int layoutResID) {
		// TODO Auto-generated method stub
		super.setContentView(layoutResID);
		_findViews();
		_bindEvent();
		_init();
	}
	@Override
	public void setContentView(View view) {
		// TODO Auto-generated method stub
		super.setContentView(view);
		_findViews();
		_bindEvent();
		_init();
	}
	@Override
	public void setContentView(View view, LayoutParams params) {
		// TODO Auto-generated method stub
		super.setContentView(view, params);
		_findViews();
		_bindEvent();
		_init();
	}
	
	private void _init(){
		View v = this.findViewById(R.id.back_button);
		if(v!=null){
			v.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}
	}
	/**找到view*/
	protected abstract void _findViews();
	/**给view绑定事件*/
	protected abstract void _bindEvent();
	
	public View getRootView(){
		return ((ViewGroup)findViewById(android.R.id.content)).getChildAt(0);
	}
	
	public boolean isUserInfoCached(){
		Object selfObj = app.getAttribute("self");
		if(selfObj==null || !(selfObj instanceof User)){
			return false;
		}
		return true;
	}
	public void userExit(){
		app.removeAttribute("self");
	}
}
