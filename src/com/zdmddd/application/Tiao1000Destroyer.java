package com.zdmddd.application;

import zl.android.log.Logger;

import zl.android.http.ZLHttpService;
import zl.android.http.image_load.BitmapCacheManager;
import zl.android.local.ZLLocalPreferences;

public class Tiao1000Destroyer {
	public interface OnDestroyComplete{void onComplete();}
	public void destroy(ZLApplication app){
		BitmapCacheManager.getBitmapManager().destroy();
		ZLLocalPreferences.getInstance().saveAttribute("cookies", ZLHttpService.getCookies());
		Logger.info("save cookies:"+ZLHttpService.getCookies());
		app.removeAllAttribute();
	}
}
