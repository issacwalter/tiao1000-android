package com.zdmddd.view;

import java.util.ArrayList;

import com.zdmddd.R;
import com.zdmddd.application.ZLApplication;
import com.zdmddd.config.UrlConfig;
import com.zdmddd.modal.Group;
import com.zdmddd.modal.User;
import com.zdmddd.service.GroupService;
import com.zdmddd.service.Service;
import com.zdmddd.service.ServiceFactory;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class GroupListView extends ListView {

	public GroupListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	
	public interface OnGroupLoadComplete{void onComplete();}
	OnGroupLoadComplete completeListener=null;
	public void setOnGroupLoadComplete(OnGroupLoadComplete completeListener){
		this.completeListener = completeListener;
	}
	/**
	 * load the groups which belong to current user
	 */
	public void load(){
		ServiceFactory.createService(GroupService.class).getMyGroups(UrlConfig.PUB_INFO, new GroupService.GetMyGroupsCallback() {
			@Override
			public void onComplete(int status, String msg,ArrayList<Group> groups) {
				// TODO Auto-generated method stub
				if(!Service.noticeExceptSuccess(GroupListView.this.getContext(), status, msg)) {
					_renderGroups(groups);
				}
				if(completeListener!=null) completeListener.onComplete();
			}
		});
		this.setDivider(new ColorDrawable(Color.parseColor("#aaaaaa")));
	}
	
	ArrayList<Group> groups;
	public Group getGroupByIndex(int index){
		return groups.get(index);
	}
	private void _renderGroups(ArrayList<Group> groups){
		this.groups = groups;
		this.setAdapter(new ListViewAdapter());
	}
	
	private final class ListViewAdapter extends BaseAdapter{
		private LayoutInflater inflater = LayoutInflater.from(getContext());
		
		private View _renderGroup(View item,Group group){
			TextView groupName = (TextView)item.findViewById(R.id.group_name);
			TextView nickName = (TextView)item.findViewById(R.id.nick_name);
			
			if(group.getType()==2){
				groupName.setText("问答咨询");
			}else if(group.getName()==null||group.getName().equals("")){
				groupName.setText("默认组");
			}else{
				groupName.setText(group.getName());
			}
			
			User self = (User)((ZLApplication)getContext().getApplicationContext()).getAttribute("self");
			if(self.getId()!=group.getCreator().getId()){
				nickName.setText(group.getCreator().getNick_name());
			}else{
				nickName.setText("");
			}
			
			return item;
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView==null){
				convertView = inflater.inflate(R.layout.group_list_item, GroupListView.this,false);
			}
			_renderGroup(convertView,groups.get(position));
			return convertView;
		}

		public long getItemId(int position) {
			//return position;
			return 0;
		}

		public Object getItem(int position) {
			//return allBlogs.get(position);
			return null;
		}

		public int getCount() {
			return groups.size();
		}
	}
}
