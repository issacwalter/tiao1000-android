package com.zdmddd.view;

import com.zdmddd.R;
import com.zdmddd.application.ZLApplication;
import com.zdmddd.modal.User;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;

public class MenuView extends LinearLayout implements View.OnClickListener{
	
	public MenuView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		_init();
	}
	public MenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		_init();
	}
	
	private ZLApplication app = null;
	private int animateDuration = 300;
	
	private Button zdmdddButton = null;
	private Button mylikeButton = null;
	private Button myfavorButton = null;
	private Button discoverHotButton = null;
	private Button discoverCategoryButton = null;
	private Button photoButton = null;
	private Button loginButton = null;
	private Button registeButton = null;
	private Button exitButton = null;
	private Button searchButton = null;
	private Button myshareButton = null;
	private Button aboutButton = null;
	
	private LinearLayout buttons_container = null;
	
	private MenuButtonClickListener clickListener = null;
	
	public void setClickListener(MenuButtonClickListener clickListener) {
		this.clickListener = clickListener;
	}
	private void _init(){
		LayoutInflater inflater = LayoutInflater.from(getContext());
		inflater.inflate(R.layout.menu, this);
		this.setVisibility(View.GONE);
		
		this.zdmdddButton = (Button)findViewById(R.id.zdmddd);
		this.mylikeButton = (Button)findViewById(R.id.mylike);
		this.myfavorButton = (Button)findViewById(R.id.myfavor);
		this.discoverHotButton = (Button)findViewById(R.id.discover_hot);
		this.discoverCategoryButton = (Button)findViewById(R.id.discover_category);
		this.photoButton = (Button)findViewById(R.id.photo);
		this.loginButton = (Button)findViewById(R.id.login);
		this.registeButton = (Button)findViewById(R.id.registe);
		this.exitButton = (Button)findViewById(R.id.exit);
		this.searchButton = (Button)findViewById(R.id.menu_search);
		this.myshareButton = (Button)findViewById(R.id.myshare);
		this.aboutButton = (Button)findViewById(R.id.about);
		this.buttons_container = (LinearLayout)findViewById(R.id.buttons_container);
		
		this.zdmdddButton.setOnClickListener(this);
		this.mylikeButton.setOnClickListener(this);
		this.myfavorButton.setOnClickListener(this);
		this.discoverHotButton.setOnClickListener(this);
		this.discoverCategoryButton.setOnClickListener(this);
		this.photoButton.setOnClickListener(this);
		this.loginButton.setOnClickListener(this);
		this.registeButton.setOnClickListener(this);
		this.exitButton.setOnClickListener(this);
		this.searchButton.setOnClickListener(this);
		this.myshareButton.setOnClickListener(this);
		this.aboutButton.setOnClickListener(this);
		
		app = (ZLApplication)this.getContext().getApplicationContext();
	}
	
	private void _render(){
		Object selfObj = app.getAttribute("self");
		if(selfObj == null || !(selfObj instanceof User)){
			this.loginButton.setVisibility(View.VISIBLE);
			this.registeButton.setVisibility(View.VISIBLE);
			this.exitButton.setVisibility(View.GONE);
		}else{
			this.loginButton.setVisibility(View.GONE);
			this.registeButton.setVisibility(View.GONE);
			this.exitButton.setVisibility(View.VISIBLE);
		}
	}
	
	public void slideIn(){
		_render();
		
		AnimationSet animationSet = new AnimationSet(true);
		TranslateAnimation translateAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 1f,
				Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f);
		translateAnimation.setDuration(animateDuration);
		animationSet.addAnimation(translateAnimation);
		this.startAnimation(animationSet);
		this.setVisibility(View.VISIBLE);
	}
	public void slideOut(){
		AnimationSet animationSet = new AnimationSet(true);
		TranslateAnimation translateAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 1f,
				Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f);
		translateAnimation.setDuration(animateDuration);
		animationSet.addAnimation(translateAnimation);
		animationSet.setAnimationListener(new AnimationListener(){
			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				MenuView.this.setVisibility(View.GONE);
			}
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		this.startAnimation(animationSet);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(this.clickListener != null){
			int index = this.buttons_container.indexOfChild(v);
			this.clickListener.onClick(v, index);
		}
	}
	
	public interface MenuButtonClickListener{void onClick(View v,int btnIndex);}
}
