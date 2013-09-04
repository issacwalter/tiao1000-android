package com.zdmddd.view.dialog;

import zl.android.log.Logger;

import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class DialogView {
	Context context;
	private Dialog dialog=null;
	public DialogView(Context context){
		this.context = context;
	}
	public void show(){
		
	}
	public void showLoadingDialog(){
		try{
			dialog = new Dialog(context);
			ProgressBar progressBar = new ProgressBar(context);
			progressBar.setBackgroundColor(Color.BLACK);
			progressBar.getBackground().setAlpha(0);
	        dialog.setContentView(progressBar,new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	        dialog.setFeatureDrawableAlpha(Window.FEATURE_OPTIONS_PANEL, 0); 
	        dialog.setCanceledOnTouchOutside(false);
	        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        
	        WindowManager.LayoutParams lp=dialog.getWindow().getAttributes();
	        // 模糊度getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);          dialog.getWindow().setAttributes(lp);
	        lp.alpha=0.5f;//(0.0-1.0)//透明度，黑暗度为lp.dimAmount=1.0f;
	        lp.dimAmount=0;
	        
	        dialog.show();
		}catch(Exception e){
			Logger.error(e);
		}
	}
	public void close(){
		if(dialog == null) return;
		this.dialog.dismiss();
	}
}
