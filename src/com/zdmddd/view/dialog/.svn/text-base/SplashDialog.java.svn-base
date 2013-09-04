package com.zdmddd.view.dialog;


import zl.android.log.Logger;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.zdmddd.R;


class Delay extends AsyncTask<Object, Object, Object>{
	
	public interface Callback{void onComplete();}
	
	private long duration = 0;
	private Callback cb = null;
	
	public Delay(long duration , Callback cb){
		this.duration = duration;
		this.cb = cb;
	}
	
	@Override
	protected byte[] doInBackground(Object... arg0) {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(duration);
		} catch (InterruptedException e) {
			Logger.error(e);
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		
		if(cb!=null) cb.onComplete();
	}
	
}
public class SplashDialog {
	private Dialog dialog;
	
	private long showTime = 0;
	private long duration = 3000;
	public void show(Context context){
		this.showTime = System.currentTimeMillis();
		dialog = new Dialog(context,R.style.Transparent);
		ImageView imgView = new ImageView(context);
		imgView.setImageResource(R.drawable.splash);
		imgView.setScaleType(ScaleType.CENTER_CROP);
		dialog.setContentView(imgView);
		dialog.show();
	}
	
	public void close(){
		long now = System.currentTimeMillis();
        if(now - this.showTime < duration){
        	new Delay(duration - (now - this.showTime),new Delay.Callback() {
				@Override
				public void onComplete() {
					if(dialog != null){
						dialog.dismiss();
					}
				}
			}).execute(new Object[]{});
        }else{
			if(dialog != null){
				dialog.dismiss();
			}
        }
	}
}
