package com.zdmddd.service;

import org.json.JSONObject;


import zl.android.http.ZLHttpCallback;
import zl.android.http.ZLHttpParameters;
import zl.android.http.ZLHttpService;
import zl.android.log.Logger;
import com.zdmddd.application.ZLApplication;
import com.zdmddd.config.UrlConfig;
import com.zdmddd.modal.User;

public class UserService extends Service{
	public interface LikeItCallback{void onComplete(int status,String msg);}
	public void likeIt(String url,ZLHttpParameters ap,final LikeItCallback cb){
		ZLHttpService.post(url,ap, new ZLHttpCallback() {
			@Override
			public void onComplete(int status, String msg, Throwable t,
					byte[] data, String responseCookie) {
				super.onComplete(status, msg, t, data, responseCookie);
				
				if(cb==null)return;
				if(status==ZLHttpCallback.STATUS_CONNECT_ERROR || status==ZLHttpCallback.STATUS_REQUEST_TIMEOUT
						|| status==ZLHttpCallback.STATUS_INVALIDE_URL || status==ZLHttpCallback.STATUS_PROTOCOL_ERROR){
					cb.onComplete(Service.STATUS_NET_FAIL, msg);
				}else if(status==ZLHttpCallback.STATUS_SERVER_ERROR){
					cb.onComplete(Service.STATUS_SERVER_ERROR, msg);
				}else if(status==ZLHttpCallback.STATUS_FAIL){
					cb.onComplete(Service.STATUS_NET_FAIL, msg);
				}else if(status==ZLHttpCallback.STATUS_SUCCESS){
					int cbStatus = Service.STATUS_UNKNOWN_ERROR;
					String cbMsg = null;
					try{
						JSONObject jsonObject = new JSONObject(new String(data));
						if(jsonObject.getInt("status") != 1){
							cbStatus = Service.STATUS_DATA_FAIL;
							cbMsg = jsonObject.getString("msg");
						}else{
							cbStatus = Service.STATUS_SUCCESS;
						}
					}catch(Exception e){
						Logger.error(e);
						cbStatus = Service.STATUS_DATA_PARSE_FAIL;
						cbMsg = e.getMessage();
					}
					cb.onComplete(cbStatus, cbMsg);
				}
			}
		});
	}
	
	public interface ForwardCallback{void onComplete(int status,String msg);}
	public void forward(String url,ZLHttpParameters ap,final ForwardCallback cb){
		ZLHttpService.post(url, ap ,new ZLHttpCallback() {
			@Override
			public void onComplete(int status, String msg, Throwable t,
					byte[] data, String responseCookie) {
				super.onComplete(status, msg, t, data, responseCookie);
				
				if(cb==null)return;
				if(status==ZLHttpCallback.STATUS_CONNECT_ERROR || status==ZLHttpCallback.STATUS_REQUEST_TIMEOUT
						|| status==ZLHttpCallback.STATUS_INVALIDE_URL || status==ZLHttpCallback.STATUS_PROTOCOL_ERROR){
					cb.onComplete(Service.STATUS_NET_FAIL, msg);
				}else if(status==ZLHttpCallback.STATUS_SERVER_ERROR){
					cb.onComplete(Service.STATUS_SERVER_ERROR, msg);
				}else if(status==ZLHttpCallback.STATUS_FAIL){
					cb.onComplete(Service.STATUS_NET_FAIL, msg);
				}else if(status==ZLHttpCallback.STATUS_SUCCESS){
					int cbStatus = Service.STATUS_UNKNOWN_ERROR;
					String cbMsg = null;
					try{
						JSONObject jsonObject = new JSONObject(parseServerByteData(data, "{status:0,msg:\"错误的服务器数据\"}"));
						if(jsonObject.getInt("status") != 1){
							cbStatus = Service.STATUS_DATA_FAIL;
							cbMsg = jsonObject.getString("msg");
						}else{
							cbStatus = Service.STATUS_SUCCESS;
						}
					}catch(Exception e){
						Logger.error(e);
						cbStatus = Service.STATUS_DATA_PARSE_FAIL;
						cbMsg = e.getMessage();
					}
					cb.onComplete(cbStatus, cbMsg);
				}
			}
		});
	}

	public interface LoginCallback{void onComplete(int status,String msg,User self);}
	public void login(String url,ZLHttpParameters ap,final LoginCallback cb){
		ZLHttpService.post(url, ap, new ZLHttpCallback() {
			@Override
			public void onComplete(int status, String msg, Throwable t,
					byte[] data, String responseCookie) {
				super.onComplete(status, msg, t, data, responseCookie);
				
				if(cb==null)return;
				if(status==ZLHttpCallback.STATUS_CONNECT_ERROR || status==ZLHttpCallback.STATUS_REQUEST_TIMEOUT
						|| status==ZLHttpCallback.STATUS_INVALIDE_URL || status==ZLHttpCallback.STATUS_PROTOCOL_ERROR){
					cb.onComplete(Service.STATUS_NET_FAIL, msg,null);
				}else if(status==ZLHttpCallback.STATUS_SERVER_ERROR){
					cb.onComplete(Service.STATUS_SERVER_ERROR, msg,null);
				}else if(status==ZLHttpCallback.STATUS_FAIL){
					cb.onComplete(Service.STATUS_NET_FAIL, msg,null);
				}else if(status==ZLHttpCallback.STATUS_SUCCESS){
					int cbStatus = Service.STATUS_UNKNOWN_ERROR;
					String cbMsg = null;
					User cbSelf = null;
					try{
						JSONObject jsonObject = new JSONObject(new String(data));
						if(jsonObject.getInt("status")!=1) {
							cbStatus = Service.STATUS_DATA_FAIL;
							cbMsg = jsonObject.getString("msg");
						}else{
							cbStatus = Service.STATUS_SUCCESS;
							cbSelf = User.parseFromJSON(jsonObject.getString("data"));
						}
					}catch(Exception e){
						cbStatus = Service.STATUS_DATA_PARSE_FAIL;
						cbMsg = e.getMessage();
					}
					cb.onComplete(cbStatus, cbMsg, cbSelf);
				}
			}
		});
	}

	public interface GetSelfCallback{void onComplete(int status,String msg,User self);}
	public void getSelf(String url,final GetSelfCallback cb){
		ZLHttpService.post(url, new ZLHttpCallback() {
			@Override
			public void onComplete(int status, String msg, Throwable t,
					byte[] data, String responseCookie) {
				super.onComplete(status, msg, t, data, responseCookie);
				
				if(cb==null)return;
				if(status==ZLHttpCallback.STATUS_CONNECT_ERROR || status==ZLHttpCallback.STATUS_REQUEST_TIMEOUT
						|| status==ZLHttpCallback.STATUS_INVALIDE_URL || status==ZLHttpCallback.STATUS_PROTOCOL_ERROR){
					cb.onComplete(Service.STATUS_NET_FAIL, msg,null);
				}else if(status==ZLHttpCallback.STATUS_SERVER_ERROR){
					cb.onComplete(Service.STATUS_SERVER_ERROR, msg,null);
				}else if(status==ZLHttpCallback.STATUS_FAIL){
					cb.onComplete(Service.STATUS_NET_FAIL, msg,null);
				}else if(status==ZLHttpCallback.STATUS_SUCCESS){
					int cbStatus = Service.STATUS_UNKNOWN_ERROR;
					String cbMsg = null;
					User cbSelf = null;
					try{
						JSONObject jsonObject = new JSONObject(new String(data));
						if(jsonObject.getInt("status") == 1){
							cbStatus = Service.STATUS_SUCCESS;
							cbSelf = User.parseFromJSON(jsonObject.getString("data"));
						}else{
							cbStatus = Service.STATUS_DATA_FAIL;
							cbMsg = jsonObject.getString("msg");
						}
					}catch(Exception e){
						Logger.error(e);
						cbStatus = Service.STATUS_DATA_PARSE_FAIL;
						cbMsg = e.getMessage();
					}
					cb.onComplete(cbStatus, cbMsg, cbSelf);
				}
			}
		});
	}

	public interface ReplyCallback{void onComplete(int status,String msg);}
	public void reply(String url,ZLHttpParameters ap,final ReplyCallback cb){
		ZLHttpService.post(url, ap, new ZLHttpCallback() {
			@Override
			public void onComplete(int status, String msg, Throwable t,
					byte[] data, String responseCookie) {
				super.onComplete(status, msg, t, data, responseCookie);
				
				if(cb==null)return;
				if(status==ZLHttpCallback.STATUS_CONNECT_ERROR || status==ZLHttpCallback.STATUS_REQUEST_TIMEOUT
						|| status==ZLHttpCallback.STATUS_INVALIDE_URL || status==ZLHttpCallback.STATUS_PROTOCOL_ERROR){
					cb.onComplete(Service.STATUS_NET_FAIL, msg);
				}else if(status==ZLHttpCallback.STATUS_SERVER_ERROR){
					cb.onComplete(Service.STATUS_SERVER_ERROR, msg);
				}else if(status==ZLHttpCallback.STATUS_FAIL){
					cb.onComplete(Service.STATUS_NET_FAIL, msg);
				}else if(status==ZLHttpCallback.STATUS_SUCCESS){
					int cbStatus = Service.STATUS_UNKNOWN_ERROR;
					String cbMsg = null;
					try{
						JSONObject jsonObject = new JSONObject(new String(data));
						if(jsonObject.getInt("status") != 1){
							cbStatus = Service.STATUS_DATA_FAIL;
							cbMsg = jsonObject.getString("msg");
						}else{
							cbStatus = Service.STATUS_SUCCESS;
						}
					}catch(Exception e){
						Logger.error(e);
						cbStatus = Service.STATUS_DATA_PARSE_FAIL;
						cbMsg = e.getMessage();
					}
					cb.onComplete(cbStatus, cbMsg);
				}
			}
		});
	}
	
	public interface RegisteCallback{void onComplete(int status,String msg);}
	public void registe(String url,ZLHttpParameters ap,final RegisteCallback cb){
		ZLHttpService.post(url, ap,new ZLHttpCallback() {
			@Override
			public void onComplete(int status, String msg, Throwable t,
					byte[] data, String responseCookie) {
				super.onComplete(status, msg, t, data, responseCookie);
				if(cb==null)return;
				if(status==ZLHttpCallback.STATUS_CONNECT_ERROR || status==ZLHttpCallback.STATUS_REQUEST_TIMEOUT
						|| status==ZLHttpCallback.STATUS_INVALIDE_URL || status==ZLHttpCallback.STATUS_PROTOCOL_ERROR){
					cb.onComplete(Service.STATUS_NET_FAIL, msg);
				}else if(status==ZLHttpCallback.STATUS_SERVER_ERROR){
					cb.onComplete(Service.STATUS_SERVER_ERROR, msg);
				}else if(status==ZLHttpCallback.STATUS_FAIL){
					cb.onComplete(Service.STATUS_NET_FAIL, msg);
				}else if(status==ZLHttpCallback.STATUS_SUCCESS){
					int cbStatus = Service.STATUS_UNKNOWN_ERROR;
					String cbMsg = null;
					try{
						JSONObject jsonObject = new JSONObject(new String(data));
						if(jsonObject.getInt("status") != 1){
							cbStatus = Service.STATUS_DATA_FAIL;
							cbMsg = jsonObject.getString("msg");
						}else{
							cbStatus = Service.STATUS_SUCCESS;
						}
					}catch(Exception e){
						Logger.error(e);
						cbStatus=Service.STATUS_DATA_PARSE_FAIL;
						cbMsg = e.getMessage();
					}
					cb.onComplete(cbStatus, cbMsg);
				}
			}
		});
	}
	
	public interface HelpLoginCallback{void onComplete(int status, String msg, User self);}
	public void helpLogin(final ZLApplication app,final HelpLoginCallback cb){
		getSelf(UrlConfig.GET_SELF, new UserService.GetSelfCallback() {
			@Override
			public void onComplete(int status, String msg, User self) {
				if(status==UserService.STATUS_SUCCESS){
					app.setAttribute("self", self);
					app.saveInfoToDb("self", self);
				}else{
					app.removeAttribute("self");
					app.removeInfoFromDb("self");
				}
				if(cb!=null) cb.onComplete(status,msg,self);
			}
		});
	}
}
