package com.zdmddd.activity;

import com.zdmddd.R;
import com.zdmddd.view.plaza.PlazaView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

public class PlazaActivity extends BaseActivity {
	PlazaView plazaView;
	ScrollView plazaContainer;
	
	TextView titleText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.plaza_activity);
		
		Intent intent = getIntent();
		int plazaKind = intent.getIntExtra("plazaKind", 0);
		if(plazaKind==0){
			this.finish();
			return;
		}
		
		plazaView = new PlazaView(plazaContainer);
		Bundle bundleParams = new Bundle();
		if(plazaKind==PlazaView.PLAZA_CATEGORY || plazaKind==PlazaView.PLAZA_SUB_CATEGORY){
			bundleParams.putString("uname", intent.getStringExtra("uname"));
			bundleParams.putString("sub_uname", intent.getStringExtra("sub_uname"));
			
			StringBuilder titleBuilder = new StringBuilder();
			String name = intent.getStringExtra("name");
			String sub_name = intent.getStringExtra("sub_name");
			if(name!=null && !name.equals("")){
				titleBuilder.append(name);
				if(sub_name!=null && !sub_name.equals("")){
					titleBuilder.append("-"+sub_name);
				}
			}
			this.titleText.setText(titleBuilder.toString()+(plazaKind==PlazaView.PLAZA_CATEGORY?"-热门":""));
		}else if(plazaKind==PlazaView.PLAZA_SEARCH){
			String keywords = intent.getStringExtra("keywords");
			this.titleText.setText("搜索-"+keywords);
			bundleParams.putString("keywords", keywords);
		}else if(plazaKind==PlazaView.PLAZA_ELE){
			String ele = intent.getStringExtra("ele");
			String categoryName = intent.getStringExtra("categoryName");
			this.titleText.setText(categoryName+"-"+ele+"风格");
			
			String categoryUname = intent.getStringExtra("categoryUname");
			bundleParams.putString("uname", categoryUname);
			bundleParams.putString("ele", ele);
		}
		plazaView.startLoadBlogs(plazaKind, bundleParams);
	}
	
	protected void _findViews(){
		this.plazaContainer = (ScrollView)findViewById(R.id.plazaContainer);
		this.titleText = (TextView)findViewById(R.id.title_text);
	}
	
	protected void _bindEvent(){
	}
}
