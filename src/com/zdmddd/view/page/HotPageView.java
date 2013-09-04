package com.zdmddd.view.page;

import com.zdmddd.R;
import com.zdmddd.view.plaza.PlazaView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ScrollView;
import android.widget.TextView;

public class HotPageView extends PageView{
	
	public HotPageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public HotPageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setTitleText(TextView titleText) {
		// TODO Auto-generated method stub
		this.titleText = titleText;
		this.titleText.setText(R.string.discover_hot);
	}

	
	private boolean hasStarted = false;
	@Override
	public void startLoad() {
		// TODO Auto-generated method stub
		if(!hasStarted){
			LayoutInflater inflater = LayoutInflater.from(getContext());
			inflater.inflate(R.layout.hot_page, this);
			
			PlazaView plazaView = new PlazaView((ScrollView)this.getChildAt(0));
			plazaView.startLoadBlogs(PlazaView.PLAZA_HOT, null);
			
			hasStarted = true;
		}
	}

}
