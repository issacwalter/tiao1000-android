package com.zdmddd.service;

import java.util.ArrayList;

import org.json.JSONObject;

import zl.android.http.ZLHttpCallback;
import zl.android.http.ZLHttpService;
import zl.android.log.Logger;

import com.zdmddd.modal.Group;

public class GroupService extends Service{
	public interface GetMyGroupsCallback{void onComplete(int status,String msg,ArrayList<Group> groups);}
	public void getMyGroups(String url,final GetMyGroupsCallback cb){
		ZLHttpService.get(url, new ZLHttpCallback(){
			@Override
			public void onComplete(int status, String msg, Throwable t,
					byte[] data, String responseCookie) {
				super.onComplete(status, msg, t, data, responseCookie);
				
				if(cb==null) return;
				if(status==ZLHttpCallback.STATUS_CONNECT_ERROR || status==ZLHttpCallback.STATUS_REQUEST_TIMEOUT
						|| status==ZLHttpCallback.STATUS_INVALIDE_URL || status==ZLHttpCallback.STATUS_PROTOCOL_ERROR){
					cb.onComplete(Service.STATUS_NET_FAIL, msg, null);
				}else if(status==ZLHttpCallback.STATUS_SERVER_ERROR){
					cb.onComplete(Service.STATUS_SERVER_ERROR, msg, null);
				}else if(status==ZLHttpCallback.STATUS_FAIL){
					cb.onComplete(Service.STATUS_NET_FAIL, msg, null);
				}else if(status==ZLHttpCallback.STATUS_SUCCESS){
					int cbStatus = Service.STATUS_UNKNOWN_ERROR;
					String cbMsg = null;
					ArrayList<Group> cbGroups = null;
					try{
						JSONObject jsonObject = new JSONObject(parseServerByteDataJsonp(data, "{status:0,\"错误的服务器数据\"}"));
						Logger.info("-----"+jsonObject);
						if(jsonObject.getInt("status")!=1){
							cbStatus = Service.STATUS_DATA_FAIL;
							cbMsg = jsonObject.getString("msg");
						}else{
							cbStatus = Service.STATUS_SUCCESS;
							cbGroups = Group.parseGroupListFromJSON(jsonObject.getString("pub_zu"));
						}
					}catch(Exception e){
						Logger.error(e);
						cbStatus = Service.STATUS_DATA_PARSE_FAIL;
						cbMsg = e.getMessage();
					}
					cb.onComplete(cbStatus, cbMsg, cbGroups);
				}
			}
		});
	}
}
