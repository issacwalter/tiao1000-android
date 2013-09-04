package com.zdmddd.application;

import java.util.Calendar;

import zl.android.http.ZLHttpService;
import zl.android.http.image_load.BitmapCacheManager;
import zl.android.local.ZLGlobalAttributes;
import zl.android.local.ZLLocalPreferences;
import android.app.Application;

public class ZLApplication extends Application{
	public ZLApplication(){
		
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	
	public void destroy(){
		BitmapCacheManager.getBitmapManager().destroy();
		ZLLocalPreferences.getInstance().saveAttribute("cookies", ZLHttpService.getCookies());
	}
	
	public void saveInfoToDb(String k,Object v){
		ZLLocalPreferences.getInstance().saveAttribute(k, v.toString());
	}
	/**
	 * 
	 * @param k
	 * @param v
	 * @param field calender的field
	 * @param expireTimeFromNow 距离现在的时间
	 */
	public void saveInfoToDb(String k,Object v,int field,int expireTimeFromNow){
		Calendar cld = Calendar.getInstance();
		cld.setTimeInMillis(System.currentTimeMillis());
		cld.add(field, expireTimeFromNow);
		ZLLocalPreferences.getInstance().saveAttribute(k, v.toString(),cld.getTimeInMillis());
	}
	public String getInfoFromDb(String k){
		return ZLLocalPreferences.getInstance().getAttribute(k, null);
	}
	public void removeInfoFromDb(String k){
		ZLLocalPreferences.getInstance().removeAttribute(k);
	}
	
	public void setAttribute(String key,Object value){
		ZLGlobalAttributes.instance().setAttribute(key, value);
	}
	public Object getAttribute(String key){
		return ZLGlobalAttributes.instance().getAttribute(key);
	}
	public void removeAttribute(String key){
		ZLGlobalAttributes.instance().removeAttribute(key);
	}
	public void removeAllAttribute(){
		ZLGlobalAttributes.instance().removeAllAttributes();
	}
}
