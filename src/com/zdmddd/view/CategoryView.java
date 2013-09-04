package com.zdmddd.view;

import java.util.ArrayList;

import zl.android.http.ZLHttpParameters;
import zl.android.view.ZLDevice;

import com.zdmddd.R;
import com.zdmddd.activity.PlazaActivity;
import com.zdmddd.application.ZLApplication;
import com.zdmddd.config.UrlConfig;
import com.zdmddd.modal.Category;
import com.zdmddd.service.CategoryService;
import com.zdmddd.service.Service;
import com.zdmddd.service.ServiceFactory;
import com.zdmddd.view.category.CategoryButton;
import com.zdmddd.view.category.EleButton;
import com.zdmddd.view.category.SubCategoryButton;
import com.zdmddd.view.plaza.PlazaView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class CategoryView {
	ScrollView container;
	LinearLayout topCategoryContainer;
	LinearLayout bottomSubCategoryContainer;
	
	ZLApplication app;
	int screenWidth = 0;
	
	/**
	 * 顶部大分类按钮选中时的背景颜色
	 */
	private final int CATEGORY_BUTTON_SELECTED_BACKGROUND_COLOR = Color.parseColor("#666666");
	public CategoryView(ScrollView container){
		this.container = container;
		app = (ZLApplication)this.container.getContext().getApplicationContext();
		
		topCategoryContainer = (LinearLayout)container.findViewById(R.id.topCategoryContainer);
		bottomSubCategoryContainer = (LinearLayout)container.findViewById(R.id.bottomSubCategoryContainer);
		
		screenWidth = ZLDevice.getScreenWidth((Activity)this.container.getContext());
	}
	
	private void _renderCategory(Category category,LinearLayout buttonContainer,boolean fillParent){
		CategoryButton button = new CategoryButton(container.getContext());
		button.setCategory(category);
		
		int layoutWidth = LinearLayout.LayoutParams.WRAP_CONTENT;
		if(fillParent==false) layoutWidth = (int)(screenWidth/4f)-6;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(layoutWidth,LinearLayout.LayoutParams.MATCH_PARENT);
		if(fillParent) {
			params.bottomMargin = 5;
			params.weight=1;
		}
		params.leftMargin = 1;
		params.rightMargin = 1;
		button.setLayoutParams(params);
		buttonContainer.addView(button);
		
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				for(int i=0,il=topCategoryContainer.getChildCount();i<il;i++){
					ViewGroup layout = (ViewGroup)topCategoryContainer.getChildAt(i);
					for(int j=0,jl=layout.getChildCount();j<jl;j++){
						((CategoryButton)layout.getChildAt(j)).setDefaultBackground();
					}
				}
				v.setBackgroundColor(CATEGORY_BUTTON_SELECTED_BACKGROUND_COLOR);
				
				CategoryButton button = (CategoryButton)v;
				_loadSubCategories(button.getCategory());
			}
		});
	}
	
	private LinearLayout _createLayout(){
		LinearLayout layout = new LinearLayout(this.topCategoryContainer.getContext());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		layout.setLayoutParams(params);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		return layout;
	}
	private void _renderCategories(ArrayList<Category> categories){
		this.topCategoryContainer.removeAllViews();
		
		int columns = 4;//一行显示4个
		LinearLayout layout = null;
		int leaveCount = categories.size() % columns;
		for(int i=0,il=categories.size()-leaveCount;i<il;i++){
			Category category = categories.get(i);
			if(i % columns == 0) {
				layout = _createLayout();
				this.topCategoryContainer.addView(layout);
			}
			_renderCategory(category,layout,true);
		}
		layout = _createLayout();
		this.topCategoryContainer.addView(layout);
		for(int i=categories.size()-leaveCount,il=categories.size();i<il;i++){
			Category category = categories.get(i);
			_renderCategory(category,layout,false);
		}
		
		if(topCategoryContainer.getChildCount()>0){
			ViewGroup vg = (ViewGroup)topCategoryContainer.getChildAt(0);
			if(vg.getChildCount()>0){
				vg.getChildAt(0).setBackgroundColor(CATEGORY_BUTTON_SELECTED_BACKGROUND_COLOR);
			}
		}
	}
	public void loadCategory(){
		if(this.topCategoryContainer.getChildCount()==1 && (this.topCategoryContainer.getChildAt(0) instanceof TextView)) {
			ServiceFactory.createService(CategoryService.class).getCategoriesFromServer(UrlConfig.CATEGORY, new CategoryService.GetCategoriesFromServerCallback() {
				@Override
				public void onComplete(int status, String msg, String dataCategories,
						ArrayList<Category> categories) {
					if(Service.noticeExceptSuccess(container.getContext(), status, msg)) return;
					if(categories.size() > 0){
						_loadSubCategories(categories.get(0));
					}
					_renderCategories(categories);
				}
			});
		}
	}
	
	private void _renderSubCategory(Category subCategory,Category category,LinearLayout layout){
		SubCategoryButton button = new SubCategoryButton(container.getContext());
		button.setSubCategory(subCategory,category);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT);
		params.bottomMargin = 5;
		params.leftMargin = 1;
		params.rightMargin = 1;
		params.weight=1;
		button.setLayoutParams(params);
		layout.addView(button);
	}
	private void _renderSubCategories(ArrayList<Category> subCategories,Category category){
		this.bottomSubCategoryContainer.removeAllViews();
		
		LinearLayout topLayout = new LinearLayout(this.container.getContext());
		topLayout.setOrientation(LinearLayout.HORIZONTAL);
		this.bottomSubCategoryContainer.addView(topLayout);
		
		View bottomBorder = new View(this.container.getContext());
		bottomBorder.setBackgroundColor(Color.GRAY);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,1);
		bottomBorder.setLayoutParams(params);
		this.bottomSubCategoryContainer.addView(bottomBorder);
		
		CategoryButton categoryButton = new CategoryButton(container.getContext());
		categoryButton.setCategory(category);
		categoryButton.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		categoryButton.setText("热门");
		categoryButton.setBackgroundColor(Color.parseColor("#00000000"));
		categoryButton.setTextColor(Color.BLACK);
		params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		categoryButton.setLayoutParams(params);
		topLayout.addView(categoryButton);
		categoryButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CategoryButton categoryButton = (CategoryButton)v;
				Intent intent = new Intent();
				intent.setClass(container.getContext(), PlazaActivity.class);
				intent.putExtra("plazaKind", PlazaView.PLAZA_CATEGORY);
				intent.putExtra("uname", categoryButton.getCategory().getUname());
				intent.putExtra("name", categoryButton.getCategory().getName());
				container.getContext().startActivity(intent);
			}
		});
		_loadEles(category, topLayout);
		
		int columns = 4;//一行显示4个
		LinearLayout layout = null;
		for(int i=0,il=subCategories.size();i<il;i++){
			Category subCategory = subCategories.get(i);
			if(i % columns == 0) {
				layout = _createLayout();
				this.bottomSubCategoryContainer.addView(layout);
			}
			_renderSubCategory(subCategory,category,layout);
		}
	}
	private void _loadSubCategories(final Category category){
		this.bottomSubCategoryContainer.removeAllViews();
		
		TextView loadingText = new TextView(container.getContext());
		loadingText.setText(R.string.loading);
		loadingText.setGravity(Gravity.CENTER);
		this.bottomSubCategoryContainer.addView(loadingText);
		ServiceFactory.createService(CategoryService.class).getSubCategory(UrlConfig.SUB_CATEGORY, category.getUname(), new CategoryService.GetSubCategoryCallback() {
			@Override
			public void onComplete(int status, String msg,
					ArrayList<Category> subCategories) {
				
				if(Service.noticeExceptSuccess(container.getContext(), status, msg))return;
				_renderSubCategories(subCategories,category);
			}
		});
	}
	
	
	private void _renderEles(ArrayList<String> eles,Category category,ViewGroup vgContainer){
		int textCount = 0;
		for(int i=0,il=eles.size();i<il&&textCount<9;i++){
			textCount += eles.get(i).length();
			EleButton btn = new EleButton(this.container.getContext());
			btn.setEleName(eles.get(i),category);
			vgContainer.addView(btn);
		}
	}
	private void _loadEles(final Category category,final ViewGroup vgContainer){
		ZLHttpParameters params = new ZLHttpParameters();
		params.put("category", category.getUname());
		ServiceFactory.createService(CategoryService.class).getElesByCategory(UrlConfig.ELES_GET, params, new CategoryService.GetElesByCategoryCallback() {
			@Override
			public void onComplete(int status, String msg, ArrayList<String> eles) {
				if(Service.noticeExceptSuccess(container.getContext(), status, msg)) return;
				if(vgContainer.getChildCount()>0 && (vgContainer.getChildAt(0) instanceof CategoryButton)){
					if(((CategoryButton)vgContainer.getChildAt(0)).getCategory().getUname().equals(category.getUname())){
						_renderEles(eles,category,vgContainer);
					}
				}
			}
		});
	}
}
