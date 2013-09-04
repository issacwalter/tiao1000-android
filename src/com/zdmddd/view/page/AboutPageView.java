package com.zdmddd.view.page;

import zl.android.log.Logger;

import com.zdmddd.R;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

public class AboutPageView extends PageView {

	public AboutPageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setTitleText(TextView titleText) {
		// TODO Auto-generated method stub
		titleText.setText("关于");
	}

	private String _getVersion(){
		String version = "";
		try {
			PackageInfo info = this.getContext().getPackageManager().getPackageInfo(this.getContext().getPackageName(), 0);  
			version = info.versionName;  
		} catch (NameNotFoundException e) {  
		    Logger.error(e);
		}
		return version;
	}
	boolean isStarted = false;
	@Override
	public void startLoad() {
		// TODO Auto-generated method stub
		if(!isStarted){
			isStarted = true;
			
			LayoutInflater inflater = LayoutInflater.from(getContext());
			inflater.inflate(R.layout.about_page, this);
			
			TextView version = (TextView)this.findViewById(R.id.version);
			version.setText("版本"+_getVersion());
		}
	}

}
