package com.zdmddd.application;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;

import zl.android.log.Logger;

/**
 * 自定义的 异常处理类 , 实现了 UncaughtExceptionHandler接口 
 * @author Administrator
 *
 */
public class GlobalExceptionCatch implements UncaughtExceptionHandler {
	// 需求是 整个应用程序 只有一个 MyCrash-Handler 
	private static GlobalExceptionCatch myCrashHandler ;
	private Context context;
	
	//1.私有化构造方法
	private GlobalExceptionCatch(){
		
	}
	
	public static synchronized GlobalExceptionCatch getInstance(){
		if(myCrashHandler!=null){
			return myCrashHandler;
		}else {
			myCrashHandler  = new GlobalExceptionCatch();
			return myCrashHandler;
		}
	}
	public void init(Context context){
		this.context = context;
	}
	

	public void uncaughtException(Thread arg0, Throwable arg1) {
		Logger.error("程序挂掉了 ");
		
		// 3.把错误的堆栈信息 获取出来 
		String errorinfo = getErrorInfo(arg1);
		
		Logger.error(errorinfo);
	
		//干掉当前的程序 
		//android.os.Process.killProcess(android.os.Process.myPid());
	}

	/**
	 * 获取错误的信息 
	 * @param arg1
	 * @return
	 */
	private String getErrorInfo(Throwable arg1) {
		Writer writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		arg1.printStackTrace(pw);
		pw.close();
		String error= writer.toString();
		return error;
	}

}

