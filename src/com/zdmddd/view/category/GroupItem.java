package com.zdmddd.view.category;

import zl.android.log.Logger;
import com.zdmddd.application.ZLApplication;
import com.zdmddd.modal.Group;
import com.zdmddd.modal.User;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GroupItem extends LinearLayout{
	
	private TextView groupNameView;
	private TextView creatorView;
	
	private Group group;
	
	private boolean isSelected = false;
	
	public GroupItem(Context context) {
		super(context);
		_init();
	}
	
	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	private void _init(){
		this.setOrientation(LinearLayout.HORIZONTAL);
		
		this.groupNameView = new TextView(this.getContext());
		this.creatorView = new TextView(this.getContext());
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,1);
		this.groupNameView.setLayoutParams(params);
		this.groupNameView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		this.groupNameView.setTextSize(20);
		this.addView(groupNameView);
		
		params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT);
		this.creatorView.setLayoutParams(params);
		this.creatorView.setGravity(Gravity.CENTER);
		this.addView(this.creatorView);
	}
	
	public void setGroupName(String groupName){
		this.groupNameView.setText(groupName==null || groupName.equals("") ? "默认组":groupName);
	}
	public void setCreatorName(String creatorName){
		ZLApplication app = (ZLApplication)this.getContext().getApplicationContext();
		Object selfObj = app.getAttribute("self");
		if(selfObj==null) return;
		User self = (User)selfObj;
		if(!creatorName.equals(self.getNick_name()))this.creatorView.setText(creatorName);
	}
	public void setGroupInfo(Group group){
		this.group = group;
		
		if(this.group.getType() != 0){
			this.setVisibility(View.GONE);
			return;
		}
		
		setGroupName(group.getName());
		try{
			if(group.getCreator()!=null){
				setCreatorName(group.getCreator().getNick_name());
			}
		}catch(Exception e){
			Logger.error(e);
		}
	}
	public Group getGroupInfo(){
		return this.group;
	}
}
