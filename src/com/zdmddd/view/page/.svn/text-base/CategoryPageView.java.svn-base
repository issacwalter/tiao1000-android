package com.zdmddd.view.page;

import com.zdmddd.R;
import com.zdmddd.view.CategoryView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ScrollView;
import android.widget.TextView;

public class CategoryPageView extends PageView{
	
	public CategoryPageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public CategoryPageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setTitleText(TextView titleText) {
		// TODO Auto-generated method stub
		this.titleText = titleText;
		this.titleText.setText(R.string.discover_category);
	}

	
	private boolean hasStarted = false;
	private CategoryView categoryView = null;
	@Override
	public void startLoad() {
		// TODO Auto-generated method stub
		if(!hasStarted){
			LayoutInflater inflater = LayoutInflater.from(getContext());
			inflater.inflate(R.layout.category_page, this);
			
			categoryView = new CategoryView((ScrollView)this.getChildAt(0));
			categoryView.loadCategory();
			
			hasStarted = true;
		}
	}

}
