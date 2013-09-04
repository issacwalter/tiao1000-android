package com.zdmddd.activity;

import java.util.ArrayList;

import com.zdmddd.R;

import zl.android.http.ZLHttpParameters;
import zl.android.log.Logger;
import com.zdmddd.config.UrlConfig;
import com.zdmddd.modal.Group;
import com.zdmddd.service.GroupService;
import com.zdmddd.service.Service;
import com.zdmddd.service.ServiceFactory;
import com.zdmddd.service.UserService;
import com.zdmddd.view.category.GroupItem;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ForwardActivity extends BaseActivity implements View.OnClickListener{
	LinearLayout groupItemContainer;
	Button done;
	Button cancel;
	TextView titleText;
	
	private Bundle blog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.forward_activity);
		
		this.blog = this.getIntent().getExtras();
		this.titleText.setText("收入专辑");
		_init();
	}
	
	@Override
	protected void _findViews() {
		this.groupItemContainer = (LinearLayout)findViewById(R.id.group_item_container);
		this.done = (Button)findViewById(R.id.done);
		this.cancel = (Button)findViewById(R.id.cancel);
		this.titleText = (TextView)findViewById(R.id.title_text);
	}


	@Override
	protected void _bindEvent() {
		this.done.setOnClickListener(this);
		this.cancel.setOnClickListener(this);
	}
	
	/*event start*/
	@Override
	public void onClick(View v) {
		if(v==this.done){
			_onDoneClick(v);
		}else if(v==this.cancel){
			finish();
		}
	}
	private void _onDoneClick(View v){
		GroupItem selectedItem = null;
		for(int i=0,il=groupItemContainer.getChildCount();i<il;i++){
			GroupItem item = (GroupItem)groupItemContainer.getChildAt(i);
			if(item.isSelected()) {selectedItem = item;break;}
		}
		if(selectedItem == null){
			Toast.makeText(this, "请选择要收入的专辑！", Toast.LENGTH_SHORT).show();
			return;
		}
		
		Group group = selectedItem.getGroupInfo();
		ZLHttpParameters params = null;
		try{
			params = new ZLHttpParameters();
			params.put("type", String.valueOf(5));
			params.put("target_id", String.valueOf(blog.getLong("id")));
			params.put("target_user_id", String.valueOf(blog.getLong("uid")));
			params.put("refer_id", String.valueOf(blog.getLong("id")));
			params.put("refer_user_id", String.valueOf(blog.getLong("uid")));
			params.put("qid", group.getId());
		}catch(Exception e){
			Logger.error(e);
			return;
		}
		
		Logger.info(params.toString());
		ServiceFactory.createService(UserService.class).forward(UrlConfig.REPLY, params, new UserService.ForwardCallback() {
			@Override
			public void onComplete(int status, String msg) {
				if(Service.noticeExceptSuccess(ForwardActivity.this, status, msg))return;
				Toast.makeText(ForwardActivity.this, "收入成功！", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
	}
	/*event end*/
	
	private void _renderGroupItems(ArrayList<Group> groups){
		for(Group group : groups){
			GroupItem groupItem = new GroupItem(ForwardActivity.this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,60);
			groupItem.setLayoutParams(params);
			groupItem.setGroupInfo(group);
			groupItemContainer.addView(groupItem);
			
			groupItem.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					for(int i=0,il=groupItemContainer.getChildCount();i<il;i++){
						GroupItem item = (GroupItem)groupItemContainer.getChildAt(i);
						try{
							item.getBackground().setAlpha(0);
						}catch(Exception e){
							Logger.error(e);
						}
						item.setSelected(false);
					}
					((GroupItem)v).setBackgroundColor(Color.parseColor("#FFA200"));
					((GroupItem)v).setSelected(true);
				}
			});
		}
	}
	private void _init(){
		/*请求group数据*/
		ServiceFactory.createService(GroupService.class).getMyGroups(UrlConfig.PUB_INFO, new GroupService.GetMyGroupsCallback() {
			@Override
			public void onComplete(int status, String msg, ArrayList<Group> groups) {
				if(Service.noticeExceptSuccess(getApplicationContext(), status, msg)) return;
				_renderGroupItems(groups);
			}
		});
	}
}
