package com.zdmddd.service;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONObject;

import zl.android.http.ZLHttpCallback;
import zl.android.http.ZLHttpParameters;
import zl.android.http.ZLHttpService;
import zl.android.log.Logger;

import com.zdmddd.modal.Blog;

public class BlogService extends Service{
	public interface GetBlogsCallback{void onComplete(int status,String msg,ArrayList<Blog> blogs);}
	public void getBlogs(ZLHttpParameters params,final GetBlogsCallback cb){
		ZLHttpService.get(params.getUrl(),params, new ZLHttpCallback() {
			@Override
			public void onComplete(int status, String msg, Throwable t,
					byte[] data, String responseCookie) {
				super.onComplete(status, msg, t, data, responseCookie);
				
				if(cb==null)return;
				Integer serviceStatus = httpStatusToServiceStatus(status);
				if(serviceStatus!=null){
					cb.onComplete(serviceStatus.intValue(), msg, null);
				}else{
					int cbStatus=Service.STATUS_UNKNOWN_ERROR;
					String cbMsg=null;
					ArrayList<Blog> cbBlogs = null;
					try{
						JSONObject jsonObject = new JSONObject(parseServerByteData(data, "{status:0,msg:\"服务器数据返回错误\"}"));
						if(jsonObject.getInt("status")==0){
							cbStatus = Service.STATUS_DATA_FAIL;
							cbMsg = jsonObject.getString("msg");
						}else{
							cbStatus = Service.STATUS_SUCCESS;
							String jsonBlogs = null;
							if(jsonObject.has("data")){
								jsonBlogs = jsonObject.getString("data");
							}else if(jsonObject.has("blogs")){
								jsonBlogs = jsonObject.getString("blogs");
							}
							cbBlogs = Blog.parseBlogListFromJson(jsonBlogs);
						}
					}catch(Exception e){
						Logger.error(e);
						cbStatus = Service.STATUS_DATA_PARSE_FAIL;
						cbMsg = e.getMessage();
					}
					cb.onComplete(cbStatus, cbMsg, cbBlogs);
				}
			}
		});
	}
	
	public interface GetBlogListBySkipCallback{void onComplete(int status,String msg,ArrayList<Blog> blogs);}
	public void getBlogListBySkip(String url,int skip,final GetBlogListBySkipCallback cb){
		ZLHttpParameters params = new ZLHttpParameters();
		params.put("skip", String.valueOf(skip));
		params.setUrl(url);
		this.getBlogs(params, new GetBlogsCallback() {
			@Override
			public void onComplete(int status, String msg, ArrayList<Blog> blogs) {
				if(cb!=null)cb.onComplete(status, msg, blogs);
			}
		});
	}
	
	public interface GetBlogByIdCallback{void onComplete(int status,String msg,Blog blog);}
	public void getBlogById(String url,long blogId,final GetBlogByIdCallback cb){
		ZLHttpParameters params = new ZLHttpParameters();
		params.put("id",String.valueOf(blogId));
		ZLHttpService.get(url, params, new ZLHttpCallback(){
			@Override
			public void onComplete(int status, String msg, Throwable t,
					byte[] data, String responseCookie) {
				super.onComplete(status, msg, t, data, responseCookie);
				
				if(cb==null)return;
				Integer serviceStatus = httpStatusToServiceStatus(status);
				if(serviceStatus!=null){
					cb.onComplete(serviceStatus.intValue(), msg, null);
				}else{
					int cbStatus=Service.STATUS_UNKNOWN_ERROR;
					String cbMsg=null;
					Blog cbBlog = null;
					try{
						JSONObject jsonObject = new JSONObject(parseServerByteData(data, "{status:0,msg:\"服务器数据返回错误\"}"));
						if(jsonObject.getInt("status")==0){
							cbStatus = Service.STATUS_DATA_FAIL;
							cbMsg = jsonObject.getString("msg");
						}else{
							cbStatus = Service.STATUS_SUCCESS;
							cbBlog = Blog.parseFromJSON(jsonObject.getJSONObject("data").getString("blog"));
						}
					}catch(Exception e){
						Logger.error(e);
						cbStatus = Service.STATUS_DATA_PARSE_FAIL;
						cbMsg = e.getMessage();
					}
					cb.onComplete(cbStatus, cbMsg, cbBlog);
				}
			}
		});
	}
	
	public interface PublishCallback{void onComplete(int status,String msg);}
	public void publish(String url,ZLHttpParameters params,final PublishCallback cb){
		ZLHttpService.post(url, params, new ZLHttpCallback(){
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
					int cbStatus=Service.STATUS_UNKNOWN_ERROR;
					String cbMsg=null;
					try{
						JSONObject jsonObject = new JSONObject(parseServerByteData(data, "{status:0,msg:\"服务器数据返回错误\"}"));
						if(jsonObject.getInt("status")==0){
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

	public interface UploadImageCallback{void onComplete(int status,String msg,String href);}
	public void uploadImage(String url,File file,final UploadImageCallback cb){
		try{
			ZLHttpParameters params = new ZLHttpParameters();
			params.put("f", "t");
			ZLHttpService.postBitmap(url, file,"filename",params, new ZLHttpCallback() {
				@Override
				public void onComplete(int status, String msg, Throwable t,
						byte[] data, String responseCookie) {
					super.onComplete(status, msg, t, data, responseCookie);
					
					Integer serviceStatus = httpStatusToServiceStatus(status);
					if(serviceStatus==null){
						int cbStatus=Service.STATUS_UNKNOWN_ERROR;
						String cbMsg = null;
						String cbHref = null;
						try{
							JSONObject jsonObject = new JSONObject(parseServerByteData(data, "{status0,msg:\"错误的服务器数据！\"}"));
							if(jsonObject.getInt("status")==1){
								cbStatus = Service.STATUS_SUCCESS;
								cbHref = jsonObject.getString("href");
							}else{
								cbStatus = Service.STATUS_DATA_FAIL;
								cbMsg = jsonObject.getString("msg");
							}
						}catch(Exception e){
							cbStatus = Service.STATUS_DATA_PARSE_FAIL;
							cbMsg = "解析服务器数据错误！"+e.getMessage();
						}
						cb.onComplete(cbStatus, cbMsg, cbHref);
					}else{
						cb.onComplete(serviceStatus, msg, null);
					}
				}
			});
		}catch(Exception e){
			
		}
	}
}
