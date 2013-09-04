package com.zdmddd.view.dialog;

import java.util.ArrayList;
import java.util.HashMap;

import zl.android.http.ZLHttpParameters;
import zl.android.utils.ObjectConvertor;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ScrollView;

import com.zdmddd.R;
import com.zdmddd.config.UrlConfig;
import com.zdmddd.modal.Category;
import com.zdmddd.service.CategoryService;
import com.zdmddd.service.ServiceFactory;

public class CategoryDialog implements CategoryService.GetCallback,View.OnClickListener{
	
	private ProgressDialog progressDlg = null;
	private PopupWindow popupWindow = null;
	private View parent = null;
	
	private ArrayList<Category> categories = null;
	private HashMap<View,Category> view_cate_map = new HashMap<View,Category>();
	private Category selectedCate = null;
	
	private String currentUname = null;
	private OnDismissListener dismissListener;
	
	private static CategoryDialog categoryDialog = null;
	private CategoryDialog(){}
	public static CategoryDialog getInstance(){
		if(categoryDialog == null) categoryDialog = new CategoryDialog();
		return categoryDialog;
	}
	
	
	public void setDismissListener(OnDismissListener dismissListener) {
		this.dismissListener = dismissListener;
	}
	
	private View _buildCategoryView(Category category){
		Button button = new Button(this.parent.getContext());
		button.setText(category.getName());
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		button.setLayoutParams(params);
		button.setBackgroundResource(R.drawable.menu_button);
		button.setTextColor(Color.rgb(200, 200, 200));
		
		button.setOnClickListener(this);
		return button;
	}
	private View _buildContentView(){
		LinearLayout container = new LinearLayout(this.parent.getContext());
		container.setOrientation(LinearLayout.VERTICAL);
		
		ScrollView scroll = new ScrollView(this.parent.getContext());
		LinearLayout buttonContainer = new LinearLayout(this.parent.getContext());
		buttonContainer.setOrientation(LinearLayout.VERTICAL);
		
		for(Category category : categories){
			if(category.getUname().equals(currentUname)){
				continue;
			}
			View v = _buildCategoryView(category);
			view_cate_map.put(v, category);
			buttonContainer.addView(v);
		}
		
		scroll.setPadding(10, 20, 10, 20);
		scroll.setBackgroundResource(R.drawable.category_dlg_scroll);
		scroll.addView(buttonContainer);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0);
		params.weight = 1;
		scroll.setLayoutParams(params);
		
		ImageView arrow = new ImageView(this.parent.getContext());
		arrow.setScaleType(ScaleType.CENTER_CROP);
		arrow.setImageResource(R.drawable.up);
		params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER_HORIZONTAL;
		arrow.setLayoutParams(params);
		
		container.addView(arrow);
		container.addView(scroll);
		
		return container;
	}
	private void _showCateogries(){
        this.popupWindow = new PopupWindow(this.parent.getContext());
        this.popupWindow.setContentView(_buildContentView());
        
        this.popupWindow.setWidth(ObjectConvertor.dip2px(this.parent.getContext(), 200));  
        this.popupWindow.setHeight(ObjectConvertor.dip2px(this.parent.getContext(), 400));  
        
        // 设置PopupWindow外部区域是否可触摸
        this.popupWindow.setFocusable(true); //设置PopupWindow可获得焦点
        this.popupWindow.setTouchable(true); //设置PopupWindow可触摸
        this.popupWindow.setOutsideTouchable(true); //设置非PopupWindow区域可触摸 
        
        this.popupWindow.setBackgroundDrawable(parent.getResources().getDrawable(R.drawable.touming));

        this.popupWindow.showAtLocation(parent, Gravity.TOP, 0, ObjectConvertor.dip2px(this.parent.getContext(), 65));
        
        this.popupWindow.setOnDismissListener(this.dismissListener);
	}
	
	public CategoryDialog show(View parent,String currentUname){
		this.parent = parent;
		this.currentUname = currentUname;
		
		if(this.categories == null){
			ZLHttpParameters params = new ZLHttpParameters();
			params.setUrl(UrlConfig.SCATE);
			ServiceFactory.createService(CategoryService.class).get(params, this);
			progressDlg = ProgressDialog.show(this.parent.getContext(), null, "加载中...",false,false);
		}else{
			_showCateogries();
		}
		
		return this;
	}
	
	
	public Category getSelectedCategory(){
		return this.selectedCate;
	}

	@Override
	public void onComplete(int status, String msg,ArrayList<Category> categories) {
		// TODO Auto-generated method stub
		this.categories = categories;
		Category cate = new Category();
		cate.setUname("plaza_recommand");
		cate.setName("值得买的东东");
		this.categories.add(0,cate);
		this._showCateogries();
		progressDlg.dismiss();
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		this.selectedCate = this.view_cate_map.get(v);
		this.popupWindow.dismiss();
	}
}
