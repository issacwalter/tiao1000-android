package com.zdmddd.service;

import java.util.ArrayList;

import org.json.JSONObject;

import zl.android.http.ZLHttpCallback;
import zl.android.http.ZLHttpParameters;
import zl.android.http.ZLHttpService;
import zl.android.log.Logger;

import com.zdmddd.modal.Comment;

public class CommentService extends Service{
	public interface GetCommentListByPageAndBlogIdCallback{void onComplete(int status,String msg,ArrayList<Comment> comments,int replyNo);}
	public void getCommentListByPageAndBlogId(String url, int page,long blog_id, final GetCommentListByPageAndBlogIdCallback cb) {
		ZLHttpParameters params = new ZLHttpParameters();
		params.put("pager_offset", String.valueOf(page));
		params.put("blog_id", String.valueOf(blog_id));
		
		ZLHttpService.post(url,params,new ZLHttpCallback() {
			@Override
			public void onComplete(int status, String msg, Throwable t,
					byte[] data, String responseCookie) {
				super.onComplete(status, msg, t, data, responseCookie);
				
				if(cb==null)return;
				Integer serviceStatus = httpStatusToServiceStatus(status);
				if(serviceStatus != null){
					cb.onComplete(serviceStatus.intValue(), msg, null,0);
				}else{
					int cbStatus=Service.STATUS_DATA_FAIL;
					String cbMsg=null;
					ArrayList<Comment> cbComments = null;
					int cbReplyNo=0;
					try{
						JSONObject jsonObject = new JSONObject(new String(data));
						if(jsonObject.getInt("status") != 1){
							cbStatus=Service.STATUS_DATA_FAIL;
							cbMsg = jsonObject.getString("msg");
						}else{
							cbStatus=Service.STATUS_SUCCESS;
							cbComments = Comment.parseCommentListFromJson(jsonObject.getJSONObject("data").getString("comments"));
							cbReplyNo = jsonObject.getJSONObject("data").getInt("reply_no");
						}
					}catch(Exception e){
						Logger.error(e);
						cbStatus=Service.STATUS_DATA_PARSE_FAIL;
						cbMsg = e.getMessage();
					}
					cb.onComplete(cbStatus, cbMsg, cbComments, cbReplyNo);
				}
			}
		});
	}
}
