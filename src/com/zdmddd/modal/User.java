package com.zdmddd.modal;

import org.json.JSONException;
import org.json.JSONObject;

import zl.android.log.Logger;

public class User {
	private long id;
	private String nick_name;
	private String email;
	private String head;
	private int email_verified;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNick_name() {
		return nick_name;
	}
	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public int getEmail_verified() {
		return email_verified;
	}
	public void setEmail_verified(int email_verified) {
		this.email_verified = email_verified;
	}
	
	public static User parseFromJSON(String json) throws JSONException{
		if(json == null || json.equals("")) return null;
		User user = null;
		JSONObject jsonObject = new JSONObject(json);
		user = new User();
		if(jsonObject.has("id")) user.setId(jsonObject.getInt("id"));
		if(jsonObject.has("nick_name")) user.setNick_name(jsonObject.getString("nick_name"));
		if(jsonObject.has("email")) user.setEmail(jsonObject.getString("email"));
		if(jsonObject.has("head")) user.setHead(jsonObject.getString("head"));
		if(jsonObject.has("email_verified")) user.setEmail_verified(jsonObject.getInt("email_verified"));
		return user;
	}
	
	public JSONObject toJSONObject() throws JSONException{
		JSONObject ret = new JSONObject();
		ret.put("id", this.id);
		ret.put("nick_name", this.nick_name);
		ret.put("email", this.email);
		ret.put("head", this.head);
		ret.put("email_verified", this.email_verified);
		return ret;
	}
	public String toString(){
		try {
			return this.toJSONObject().toString();
		} catch (JSONException e) {
			Logger.error(e);
		}
		return "";
	}
}
