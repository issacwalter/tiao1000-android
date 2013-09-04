package com.zdmddd.view.page;

import java.util.HashMap;
import java.util.Map;

import com.zdmddd.R;
import com.zdmddd.modal.Category;
import com.zdmddd.view.dialog.CategoryDialog;
import com.zdmddd.view.plaza.PlazaListView;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.ViewGroup;

public class FirstPageView extends PageView implements View.OnClickListener{

	public FirstPageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		_init();
	}
	public FirstPageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		_init();
	}
	
	//String currentCategoryUname = "plaza_recommand";
	Category category = null;
	HashMap<String,PlazaListView> plazaListViews = new HashMap<String,PlazaListView>();
	
	private void _init(){
		this.category = new Category();
		category.setUname("plaza_recommand");
		category.setName("值得买的东东");
	}
	
	private PlazaListView _createPlazaListView(int plazaKind,String uname,Bundle params){
		PlazaListView plazaListView = this.plazaListViews.get(uname);
		if(plazaListView == null){
			plazaListView = new PlazaListView(this.getContext());
			plazaListView.startLoadBlogs(plazaKind, params);
			this.plazaListViews.put(uname,plazaListView);
			this.addView(plazaListView);
		}
		for(Map.Entry<String, PlazaListView> entry:this.plazaListViews.entrySet()){
			entry.getValue().setVisibility(View.GONE);
		}
		plazaListView.setVisibility(View.VISIBLE);
		return plazaListView;
	}
	
	private void _switchUpDown(boolean isUp){
		ImageView imgView = (ImageView)((ViewGroup)this.titleText.getParent()).findViewById(R.id.up_down);
		if(isUp){
			imgView.setImageResource(R.drawable.up);
		}else{
			imgView.setImageResource(R.drawable.down);
		}
		imgView.setVisibility(View.VISIBLE);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(this.getVisibility() != View.VISIBLE) return;
		
		_switchUpDown(true);
		if(v == this.titleText){
			final CategoryDialog dlg = CategoryDialog.getInstance();
			dlg.setDismissListener(new android.widget.PopupWindow.OnDismissListener() {
				@Override
				public void onDismiss() {
					// TODO Auto-generated method stub
					_switchUpDown(false);
					
					Category cate = dlg.getSelectedCategory();
					if(cate == null) return;
					if(!cate.getUname().equals(FirstPageView.this.category.getUname())){
						FirstPageView.this.category = cate;
						
						if(cate.getUname().equals("plaza_recommand")) {
							_createPlazaListView(PlazaListView.PLAZA_RECOMMAND,cate.getUname(),null);
						}else{
							Bundle params = new Bundle();
							params.putString("scate", cate.getUname());
							_createPlazaListView(PlazaListView.PLAZA_SHOW_SCATE,cate.getUname(),params);
						}
						
						titleText.setText(cate.getName());
					}
				}
			});
			dlg.show(this.titleText,FirstPageView.this.category.getUname());
		}
	}
	@Override
	public void setTitleText(TextView titleText) {
		// TODO Auto-generated method stub
		this.titleText = titleText;
		this.titleText.setText(this.category.getName());
		this.titleText.setOnClickListener(this);
	}
	
	private boolean hasStarted = false;
	@Override
	public void startLoad() {
		// TODO Auto-generated method stub
		if(hasStarted) return;
		_createPlazaListView(PlazaListView.PLAZA_RECOMMAND,"plaza_recommand",null);
		hasStarted = true;
	}

}
