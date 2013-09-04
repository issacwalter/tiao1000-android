package com.zdmddd.modal;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Comment {
	private String comment;
	private long create_timestamp;
	private User user;
	private String id;
	private long target_id;
	private long target_user_id;
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public long getCreate_timestamp() {
		return create_timestamp;
	}
	public void setCreate_timestamp(long create_timestamp) {
		this.create_timestamp = create_timestamp;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public long getTarget_id() {
		return target_id;
	}
	public void setTarget_id(long target_id) {
		this.target_id = target_id;
	}
	public long getTarget_user_id() {
		return target_user_id;
	}
	public void setTarget_user_id(long target_user_id) {
		this.target_user_id = target_user_id;
	}
	
	public static Comment parseFromJson(String jsonStr) throws JSONException{
		Comment ret = new Comment();
		JSONObject jsonObject = new JSONObject(jsonStr);
		if(jsonObject.has("comment")) ret.setComment(jsonObject.getString("comment"));
		if(jsonObject.has("create_timestamp")) ret.setCreate_timestamp(jsonObject.getLong("create_timestamp"));
		if(jsonObject.has("user")) ret.setUser(User.parseFromJSON(jsonObject.getJSONObject("user").toString()));
		if(jsonObject.has("id")) ret.setId(jsonObject.getString("id"));
		if(jsonObject.has("target_id")) ret.setTarget_id(jsonObject.getLong("target_id"));
		if(jsonObject.has("target_user_id")) ret.setTarget_user_id(jsonObject.getLong("target_user_id"));
		return ret;
	}
	public static ArrayList<Comment> parseCommentListFromJson(String jsonStr) throws Exception {
		JSONArray jsonArray = new JSONArray(jsonStr);
		ArrayList<Comment> comments = new ArrayList<Comment>();
		for(int i=0,il=jsonArray.length();i<il;i++){
			JSONObject json = jsonArray.getJSONObject(i);
			comments.add(parseFromJson(json.toString()));
		}
		return comments;
	}
}
