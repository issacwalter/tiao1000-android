package com.zdmddd.view.category;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.Button;

import com.zdmddd.R;
import com.zdmddd.modal.Category;

public class CategoryButton extends Button{
	private Category category=null;
	public Category getCategory() {
		return category;
	}
	public CategoryButton(Context context,AttributeSet as) {
		super(context,as);
	}
	public void setCategory(Category category) {
		this.category = category;
		this.setBackgroundResource(R.drawable.category_button);
		this.setTextColor(Color.parseColor("#FFFFFF"));
		this.setGravity(Gravity.CENTER);
		this.setText(category.getName());
	}
	public CategoryButton(Context context) {
		super(context);
	}
	
	public void setDefaultBackground(){
		this.setBackgroundResource(R.drawable.category_button);
	}
}
