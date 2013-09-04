package com.zdmddd.application;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import org.json.JSONObject;

import com.zdmddd.config.UrlConfig;
import com.zdmddd.modal.User;
import com.zdmddd.service.Service;
import com.zdmddd.service.ServiceFactory;
import com.zdmddd.service.UserService;

import zl.android.log.Logger;

import zl.android.exception.ZLGlobalExceptionCatch;
import zl.android.http.ZLHttpService;
import zl.android.local.ZLLocalPreferences;

/**
 * 趣味组app初始化
 * @author zhangli
 *
 */
public class Tiao1000Initializer {
	private interface _OnAsyncComplete{void onComplete();}
	private Hashtable<String,Boolean> asyncInitializeReadyTable = new Hashtable<String,Boolean>();
	private boolean _checkReady(){
		for(Enumeration<String> en=asyncInitializeReadyTable.keys();en.hasMoreElements();){
			String key = en.nextElement();
			if(!asyncInitializeReadyTable.get(key)){
				return false;
			}
		}
		asyncInitializeReadyTable.clear();
		return true;
	}
	
	public interface OnInitializeComplete{void onComplete();}
	public void initialize(ZLApplication app,final OnInitializeComplete onComplete){
		_appGlobalExceptionCatcher(app);
		_initializeCookies(app);
		_initializeUserInfo(app,onComplete==null?null:new _OnAsyncComplete() {
			@Override
			public void onComplete() {
				// TODO Auto-generated method stub
				if(_checkReady()){
					onComplete.onComplete();
				}
			}
		});
		
		if(_checkReady()){
			onComplete.onComplete();
		}
	}
	
	private void _initializeUserInfo(final ZLApplication app,final _OnAsyncComplete onComplete){
		User self = null;
//		try{
//			self = User.parseFromJSON(app.getInfoFromDb("self"));
//			app.setAttribute("self", self);
//		}catch(Exception e){
//			Logger.error(e);
//		}
		if(self == null){
			this.asyncInitializeReadyTable.put("_initializeUserInfo", false);
			ServiceFactory.createService(UserService.class).getSelf(UrlConfig.GET_SELF, new UserService.GetSelfCallback() {
				@Override
				public void onComplete(int status, String msg, User self) {
					// TODO Auto-generated method stub
					if(status==Service.STATUS_SUCCESS){
						app.setAttribute("self", self);
						app.saveInfoToDb("self", self);
					}else{
						app.removeAttribute("self");
						app.removeInfoFromDb("self");
					}
					asyncInitializeReadyTable.put("_initializeUserInfo", true);
					if(onComplete!=null) onComplete.onComplete();
				}
			});
		}
	}
	private void _appGlobalExceptionCatcher(ZLApplication app){
		ZLGlobalExceptionCatch globalExceptionCatch = ZLGlobalExceptionCatch.getInstance();
		globalExceptionCatch.init(app);
		Thread.setDefaultUncaughtExceptionHandler(globalExceptionCatch);
	}
	
	private void _initializeCookies(ZLApplication app){
		ZLLocalPreferences.createInstance(app);
		try{
			JSONObject jsonObject = new JSONObject(ZLLocalPreferences.getInstance().getAttribute("cookies", "{}"));
			Logger.info("get cookies:"+jsonObject.toString());
			if(jsonObject != null){
				@SuppressWarnings("unchecked")
				Iterator<String> it = jsonObject.keys();
				while(it.hasNext()){
					String key = it.next();
					ZLHttpService.addCookie(key,jsonObject.getString(key));
				}
			}
		}catch(Exception e){
			Logger.error(e);
		}
	}
}
