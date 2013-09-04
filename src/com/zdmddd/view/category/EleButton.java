package com.zdmddd.view.category;

import com.zdmddd.activity.PlazaActivity;
import com.zdmddd.modal.Category;
import com.zdmddd.view.plaza.PlazaView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

public class EleButton extends Button {
	private String eleName;
	private Category category;
	
	public EleButton(Context context) {
		super(context);
	}
	public EleButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setEleName(String eleName,Category category){
		this.eleName = eleName;
		this.category = category;
		
		this.setText(this.eleName);
		this.setBackgroundColor(Color.parseColor("#00000000"));
		
		this.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getContext(), PlazaActivity.class);
				intent.putExtra("plazaKind", PlazaView.PLAZA_ELE);
				intent.putExtra("ele", EleButton.this.eleName);
				intent.putExtra("categoryName", EleButton.this.category.getName());
				intent.putExtra("categoryUname", EleButton.this.category.getUname());
				getContext().startActivity(intent);
			}
		});
		
	}
}
