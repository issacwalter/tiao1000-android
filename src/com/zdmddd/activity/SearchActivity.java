package com.zdmddd.activity;

import com.zdmddd.R;
import com.zdmddd.config.UrlConfig;
import com.zdmddd.view.plaza.PlazaView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends BaseActivity implements View.OnClickListener,View.OnTouchListener{
	Button searchButton;
	TextView keyWords;
	TextView titleText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.search_activity);
		
		this.titleText.setText("搜索关键字");
	}
	@Override
	protected void _findViews() {
		this.titleText= (TextView)findViewById(R.id.title_text);
		this.searchButton = (Button)findViewById(R.id.search);
		this.keyWords = (TextView)findViewById(R.id.keywords);
	}
	@Override
	protected void _bindEvent() {
		this.searchButton.setOnClickListener(this);
	}
	public String getKeyWordsText(){
		return this.keyWords.getText().toString();
	}
	
	
	
	/*event start*/
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return false;
	}
	@Override
	public void onClick(View v) {
		if(v==this.searchButton){
			String keywords = this.keyWords.getText().toString();
			if(keywords == null || keywords.equals("")){
				Toast.makeText(this, "请输入搜索关键字！", Toast.LENGTH_LONG).show();
				return;
			}
			
			Intent intent = new Intent();
			intent.setClass(this, PlazaActivity.class);
			intent.putExtra("plazaKind", PlazaView.PLAZA_SEARCH);
			intent.putExtra("keywords", keywords);
			
			Bundle bundle = new Bundle();
			//bundle.putInt("plaza_kind", PlazaActivity.PlazaKind.SEARCH);
			bundle.putString("blogListUrl", UrlConfig.SEARCH+"?page_no=0");
			bundle.putString("title", "搜索-"+keywords);
			bundle.putString("keywords", keywords);
			intent.putExtras(bundle);
			
			this.startActivity(intent);
			this.finish();
		}
	}
	/*event end*/
}
