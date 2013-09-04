package com.zdmddd.modal;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import zl.android.log.Logger;

public class Category {
	private String _id;
	private String home_page_ele_group;
	private ArrayList<String> home_page_eles;
	private String name;
	private boolean show_on_home_page;
	private String uname;
	private String image;
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getHome_page_ele_group() {
		return home_page_ele_group;
	}
	public void setHome_page_ele_group(String home_page_ele_group) {
		this.home_page_ele_group = home_page_ele_group;
	}
	public ArrayList<String> getHome_page_eles() {
		return home_page_eles;
	}
	public void setHome_page_eles(ArrayList<String> home_page_eles) {
		this.home_page_eles = home_page_eles;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isShow_on_home_page() {
		return show_on_home_page;
	}
	public void setShow_on_home_page(boolean show_on_home_page) {
		this.show_on_home_page = show_on_home_page;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	
	public String toJSONString(){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("_id", this._id);
			jsonObject.put("home_page_ele_group", this.home_page_ele_group);
			JSONArray jsonArray = new JSONArray();
			for(String home_page_ele : home_page_eles){
				jsonArray.put(home_page_ele);
			}
			jsonObject.put("home_page_eles", jsonArray);
			jsonObject.put("name", this.name);
			jsonObject.put("show_on_home_page", this.show_on_home_page);
			jsonObject.put("uname", this.uname);
			jsonObject.put("image", this.image);
		}catch(Exception e){
			Logger.error(e);
		}
		return jsonObject.toString();
	}
	public static Category parseFromJSON(String json) throws Exception{
		Category ret = new Category();
		JSONObject jsonObject = new JSONObject(json);
		if(jsonObject.has("_id")) ret.set_id(jsonObject.getString("_id"));
		if(jsonObject.has("home_page_ele_group")) ret.setHome_page_ele_group(jsonObject.getString("home_page_ele_group"));
		if(jsonObject.has("home_page_eles")) {
			JSONArray jsonArray = jsonObject.getJSONArray("home_page_eles");
			ArrayList<String> home_page_eles = new ArrayList<String>();
			for(int i=0,il=jsonArray.length();i<il;i++){
				home_page_eles.add(jsonArray.getString(i));
			}
			ret.setHome_page_eles(home_page_eles);
		}
		if(jsonObject.has("name")) ret.setName(jsonObject.getString("name"));
		if(jsonObject.has("show_on_home_page")) ret.setShow_on_home_page(jsonObject.getBoolean("show_on_home_page"));
		if(jsonObject.has("uname")) ret.setUname(jsonObject.getString("uname"));
		if(jsonObject.has("image")) ret.setImage(jsonObject.getString("image"));
		return ret;
	}
	public static ArrayList<Category> parseCategoryListFromJson(String json)throws Exception{
		ArrayList<Category> ret = new ArrayList<Category>();
		JSONArray jsonArray = new JSONArray(json);
		for(int i=0,il=jsonArray.length();i<il;i++){
			ret.add(Category.parseFromJSON(jsonArray.getString(i)));
		}
		return ret;
	}
}
