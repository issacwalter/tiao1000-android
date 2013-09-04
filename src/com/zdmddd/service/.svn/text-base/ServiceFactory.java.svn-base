package com.zdmddd.service;

import java.util.Hashtable;

import zl.android.log.Logger;

public class ServiceFactory {
	private static Hashtable<String, Service> services = new Hashtable<String, Service>();
	
	@SuppressWarnings("unchecked")
	public static <T extends Service> T createService(Class<T> cls){
		String key = cls.getClass().getName();
		T service = (T)services.get(key);
		if(service == null){
			try {
				service = cls.newInstance();
			} catch (InstantiationException e) {
				Logger.error(e);
			} catch (IllegalAccessException e) {
				Logger.error(e);
			}
		}
		return service;
	}
}
