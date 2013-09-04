package com.zdmddd.modal;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Group {
	private String id;
	private String name;
	private byte trust;
	private byte type;
	private byte view;
	private User creator;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public byte getTrust() {
		return trust;
	}
	public void setTrust(byte trust) {
		this.trust = trust;
	}
	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
	}
	public byte getView() {
		return view;
	}
	public void setView(byte view) {
		this.view = view;
	}
	public User getCreator() {
		return creator;
	}
	public void setCreator(User creator) {
		this.creator = creator;
	}
	
	public static Group parseFromJSON(String json) throws JSONException{
		Group group = null;
		JSONObject jsonObject = new JSONObject(json);
		group = new Group();
		if(jsonObject.has("id")) group.setId(jsonObject.getString("id"));
		if(jsonObject.has("name")) group.setName(jsonObject.getString("name"));
		if(jsonObject.has("trust")) group.setTrust((byte)jsonObject.getInt("trust"));
		if(jsonObject.has("type")) group.setType((byte)jsonObject.getInt("type"));
		if(jsonObject.has("view")) group.setView((byte)jsonObject.getInt("view"));
		if(jsonObject.has("creator")) group.setCreator(User.parseFromJSON(jsonObject.getString("creator")));
		return group;
	}
	public static ArrayList<Group> parseGroupListFromJSON(String json) throws JSONException{
		ArrayList<Group> groups = new ArrayList<Group>();
		JSONArray jsonArray = new JSONArray(json);
		for(int i=0,il=jsonArray.length();i<il;i++){
			groups.add(parseFromJSON(jsonArray.getString(i)));
		}
		return groups;
	}
}
