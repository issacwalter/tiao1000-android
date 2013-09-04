package com.zdmddd.service;

import zl.android.http.ZLHttpCallback;
import android.content.Context;
import android.widget.Toast;

public abstract class Service {
	/**
	 * 成功
	 */
	public static final int STATUS_SUCCESS=2;
	/**
	 * 因为网络故障造成的失败
	 * */
	public static final int STATUS_NET_FAIL=0;
	/**
	 * 返回的数据中指明本次操作失败
	 * */
	public static final int STATUS_DATA_FAIL=1;
	/**
	 * 解析服务器返回的数据异常
	 * */
	public static final int STATUS_DATA_PARSE_FAIL=3;
	/**
	 * 服务器错误
	 */
	public static final int STATUS_SERVER_ERROR=4;
	/**
	 * 未知错误
	 */
	public static final int STATUS_UNKNOWN_ERROR=5;
	
	
	/**
	 * 帮助处理除了STATUS_SUCCESS状态之外的所有状态
	 */
	public static boolean noticeExceptSuccess(Context context,int status,String msg){
		boolean ret=false;
		if(status==STATUS_NET_FAIL){
			ret = true;
			Toast.makeText(context, "操作失败，请检查您的网络！"+(msg==null?"":msg), Toast.LENGTH_LONG).show();
		}else if(status==STATUS_DATA_FAIL){
			ret = true;
			Toast.makeText(context, "操作失败！"+(msg==null?"":msg), Toast.LENGTH_LONG).show();
		}else if(status==STATUS_DATA_PARSE_FAIL){
			ret = true;
			Toast.makeText(context, "解析服务器数据错误！"+(msg==null?"":msg), Toast.LENGTH_LONG).show();
		}else if(status==STATUS_SERVER_ERROR){
			ret = true;
			Toast.makeText(context, "服务器错误！"+(msg==null?"":msg), Toast.LENGTH_LONG).show();
		}else if(status==STATUS_UNKNOWN_ERROR){
			ret = true;
			Toast.makeText(context, "未知错误！"+(msg==null?"":msg), Toast.LENGTH_LONG).show();
		}
		return ret;
	}
	
	protected Integer httpStatusToServiceStatus(int httpStatus){
		Integer ret = null;
		if(httpStatus==ZLHttpCallback.STATUS_CONNECT_ERROR || httpStatus==ZLHttpCallback.STATUS_REQUEST_TIMEOUT
				|| httpStatus==ZLHttpCallback.STATUS_INVALIDE_URL || httpStatus==ZLHttpCallback.STATUS_PROTOCOL_ERROR){
			ret = Integer.valueOf(Service.STATUS_NET_FAIL);
		}else if(httpStatus==ZLHttpCallback.STATUS_SERVER_ERROR){
			ret = Integer.valueOf(Service.STATUS_SERVER_ERROR);
		}else if(httpStatus==ZLHttpCallback.STATUS_FAIL){
			ret = Integer.valueOf(Service.STATUS_NET_FAIL);
		}
		return ret;
	}
	
	protected String parseServerByteData(byte[] data,String defaultValue){
		String ret = new String(data);
		if(ret==null||ret.trim().equals("")){
			return defaultValue;
		}
		return ret;
	}
	protected String parseServerByteDataJsonp(byte[] data,String defaultJsonStr){
		String jsonStr = parseServerByteData(data,"("+defaultJsonStr+")");
		jsonStr = jsonStr.substring(jsonStr.indexOf('(')+1,jsonStr.lastIndexOf(')'));
		return jsonStr;
	}
}
