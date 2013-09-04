package com.zdmddd.service;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import zl.android.http.ZLHttpCallback;
import zl.android.http.ZLHttpParameters;
import zl.android.http.ZLHttpService;
import zl.android.log.Logger;

import com.zdmddd.modal.Category;

public class CategoryService extends Service{
	
	private ArrayList<Category> categories = null;//cache
	private String dataCategories = null;
	public interface GetCategoriesFromServerCallback{void onComplete(int status,String msg,String dataCategories,ArrayList<Category> categories);}
	public void getCategoriesFromServer(String url,final GetCategoriesFromServerCallback cb){
		if(categories != null){
			if(cb!=null) cb.onComplete(Service.STATUS_SUCCESS, null, dataCategories, categories);
			return;
		}
		ZLHttpService.get(url,new ZLHttpCallback() {
			@Override
			public void onComplete(int status, String msg, Throwable t,byte[] data, String responseCookie) {
				super.onComplete(status, msg, t, data, responseCookie);
				
				if(cb==null)return;
				if(status==ZLHttpCallback.STATUS_CONNECT_ERROR || status==ZLHttpCallback.STATUS_REQUEST_TIMEOUT
						|| status==ZLHttpCallback.STATUS_INVALIDE_URL || status==ZLHttpCallback.STATUS_PROTOCOL_ERROR){
					cb.onComplete(Service.STATUS_NET_FAIL, msg, null,null);
				}else if(status==ZLHttpCallback.STATUS_SERVER_ERROR){
					cb.onComplete(Service.STATUS_SERVER_ERROR, msg, null,null);
				}else if(status==ZLHttpCallback.STATUS_FAIL){
					cb.onComplete(Service.STATUS_NET_FAIL, msg, null,null);
				}else if(status==ZLHttpCallback.STATUS_SUCCESS){
					int cbStatus = Service.STATUS_UNKNOWN_ERROR;
					String cbMsg = null;
					String cbDataCategories = null;
					ArrayList<Category> cbCategories = null;
					try{
						JSONObject jsonObject = new JSONObject(new String(data));
						if(jsonObject.getInt("status") != 1){
							cbStatus = Service.STATUS_DATA_FAIL;
							cbMsg = jsonObject.getString("msg");
						}else{
							cbStatus = Service.STATUS_SUCCESS;
							cbDataCategories = jsonObject.getString("data");
							cbCategories = Category.parseCategoryListFromJson(jsonObject.getString("data"));
							
							categories = cbCategories;
							CategoryService.this.dataCategories = cbDataCategories;
						}
					}catch(Exception e){
						Logger.error(e);
						cbStatus = Service.STATUS_DATA_PARSE_FAIL;
						cbMsg = e.getMessage();
					}
					cb.onComplete(cbStatus, cbMsg, cbDataCategories, cbCategories);
				}
			}
		});
	}
	
	public interface GetSubCategoryCallback{void onComplete(int status,String msg,ArrayList<Category> subCategories);}
	public void getSubCategory(String url,String categoryUname,final GetSubCategoryCallback cb){
		ZLHttpParameters params = new ZLHttpParameters();
		params.put("category", categoryUname);
		ZLHttpService.get(url,params, new ZLHttpCallback() {
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
					ArrayList<Category> cbSubCategories = null;
					try{
						JSONObject jsonObject = new JSONObject(new String(data));
						if(jsonObject.getInt("status") != 1){
							cbStatus = Service.STATUS_DATA_FAIL;
							cbMsg = jsonObject.getString("msg");
						}else{
							cbStatus = Service.STATUS_SUCCESS;
							cbSubCategories = Category.parseCategoryListFromJson(jsonObject.getString("data"));
						}
					}catch(Exception e){
						Logger.error(e);
						cbStatus = Service.STATUS_DATA_PARSE_FAIL;
						cbMsg = e.getMessage();
					}
					cb.onComplete(cbStatus, cbMsg, cbSubCategories);
				}
			}
		});
	}

	public interface GetElesByCategoryCallback{void onComplete(int status,String msg,ArrayList<String> eles);}
	public void getElesByCategory(String url,ZLHttpParameters params,final GetElesByCategoryCallback cb){
		ZLHttpService.get(url, params, new ZLHttpCallback() {
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
					ArrayList<String> eles = null;
					try{
						JSONObject jsonObject = new JSONObject(new String(data));
						if(jsonObject.getInt("status") != 1){
							cbStatus = Service.STATUS_DATA_FAIL;
							cbMsg = jsonObject.getString("msg");
						}else{
							cbStatus = Service.STATUS_SUCCESS;
							JSONArray jsonArray = jsonObject.getJSONArray("data");
							eles = new ArrayList<String>();
							for(int i=0,il=jsonArray.length();i<il;i++){
								eles.add(jsonArray.getString(i));
							}
						}
					}catch(Exception e){
						Logger.error(e);
						cbStatus = Service.STATUS_DATA_PARSE_FAIL;
						cbMsg = e.getMessage();
					}
					cb.onComplete(cbStatus, cbMsg, eles);
				}
			}
		});
	}

	public interface GetCallback{void onComplete(int status,String msg,ArrayList<Category> categories);}
	public void get(ZLHttpParameters params,final GetCallback cb){
		ZLHttpService.get(params.getUrl(), params, new ZLHttpCallback() {
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
					ArrayList<Category> cbCategories = null;
					try{
						JSONObject jsonObject = new JSONObject(new String(data));
						if(jsonObject.getInt("status") != 1){
							cbStatus = Service.STATUS_DATA_FAIL;
							cbMsg = jsonObject.getString("msg");
						}else{
							cbStatus = Service.STATUS_SUCCESS;
							String cate = null;
							if(jsonObject.has("categories")){
								cate = jsonObject.getString("categories");
							}else if(jsonObject.has("scates")){
								cate = jsonObject.getString("scates");
							}else if(jsonObject.has("ssubcates")){
								cate = jsonObject.getString("ssubcates");
							}
							cbCategories = Category.parseCategoryListFromJson(cate);
						}
					}catch(Exception e){
						Logger.error(e);
						cbStatus = Service.STATUS_DATA_PARSE_FAIL;
						cbMsg = e.getMessage();
					}
					cb.onComplete(cbStatus, cbMsg, cbCategories);
				}
			}
		});
	}
}
