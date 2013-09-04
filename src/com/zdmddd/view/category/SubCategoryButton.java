package com.zdmddd.view.category;

import zl.android.http.image_load.BitmapLoader;

import com.zdmddd.R;
import com.zdmddd.activity.PlazaActivity;
import com.zdmddd.modal.Category;
import com.zdmddd.view.plaza.PlazaView;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SubCategoryButton extends LinearLayout{
	private Category subCategory;
	private Category category;
	public SubCategoryButton(Context context) {
		super(context);
		this.setOrientation(LinearLayout.VERTICAL);
		this.setMinimumHeight(60);
		this.setGravity(Gravity.CENTER);
	}
	
	public void setText(String text){
		TextView textView = new TextView(this.getContext());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		textView.setLayoutParams(params);
		textView.setText(text);
		textView.setGravity(Gravity.CENTER);
		this.addView(textView);
	}
	public void setImage(String url){
		if(url==null||url.equals("")) return;
		
		ImageView imgView = new ImageView(this.getContext());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(80,80);
		params.gravity = Gravity.CENTER_HORIZONTAL;
		params.topMargin = 5;
		imgView.setLayoutParams(params);
		this.addView(imgView, 0);
		url = url.split("#")[0]+"_80";
		new BitmapLoader(url,imgView).execute();
	}
	public void setSubCategory(Category subCategory,Category category){
		this.subCategory = subCategory;
		this.category = category;
		
		this.setImage(this.subCategory.getImage());
		this.setText(this.subCategory.getName());
		this.setBackgroundResource(R.drawable.sub_category_button);
		
		this.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getContext(), PlazaActivity.class);
				intent.putExtra("plazaKind", PlazaView.PLAZA_SUB_CATEGORY);
				intent.putExtra("uname", SubCategoryButton.this.category.getUname());
				intent.putExtra("sub_uname", SubCategoryButton.this.subCategory.getUname());
				intent.putExtra("name", SubCategoryButton.this.category.getName());
				intent.putExtra("sub_name", SubCategoryButton.this.subCategory.getName());
				getContext().startActivity(intent);
			}
		});
	}
	public Category getSubCategory(){
		return this.subCategory;
	}
}
