package com.zdmddd.view.blog;

import zl.android.http.image_load.BitmapLoader;
import zl.android.http.image_load.LightBitmapLoader;
import zl.android.movie.ZLMovie;
import zl.android.view.ZLDevice;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class DetailBlogImageView extends ImageView{
	public DetailBlogImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public DetailBlogImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public DetailBlogImageView(Context context) {
		super(context);
	}
	
	private String url;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url){
		this.url = url;
		_loadSmallImage();
		_loadBigImage();
	}
	
	private void _loadSmallImage(){
		LightBitmapLoader.instance().load(this.url+"_android150", this,new BitmapLoader.OnImageLoadedSuccess() {
			@Override
			public void onImageLoadedSuccess(String url, Bitmap bitmap,
					ZLMovie movie, int imageType, ImageView imgView) {
				// TODO Auto-generated method stub
				if((DetailBlogImageView.this.url+"_android150").equals(url)) _showBlogImage(bitmap,imgView);
				bitmap = null;
			}
		},null,false);
	}
	private void _loadBigImage(){
		LightBitmapLoader.instance().load(this.url+"_440", this,new BitmapLoader.OnImageLoadedSuccess() {
			@Override
			public void onImageLoadedSuccess(String url, Bitmap bitmap,
					ZLMovie movie, int imageType, ImageView imgView) {
				// TODO Auto-generated method stub
				if((DetailBlogImageView.this.url+"_440").equals(url))_showBlogImage(bitmap,imgView);
				bitmap = null;
			}
		},new BitmapLoader.OnImageLoadedFailed() {
			@Override
			public void onImageLoadedFailed(String url, ImageView imgView,String errorMsg, Exception e) {
				Toast.makeText(DetailBlogImageView.this.getContext(), "加载图片失败！"+(errorMsg==null?"":errorMsg), Toast.LENGTH_LONG).show();
			}
		},false);
	}
	
	
	private void _showBlogImage(Bitmap bitmap ,ImageView imgView){
		int imgViewHeight = 0;
		if(bitmap.getWidth() != bitmap.getHeight()){
			imgViewHeight = (int)(ZLDevice.getScreenWidth(getContext()) / (float)bitmap.getWidth() * bitmap.getHeight());
		}else{
			imgViewHeight = ZLDevice.getScreenWidth(getContext());
		}
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ZLDevice.getScreenWidth(getContext()),imgViewHeight);
		imgView.setLayoutParams(params);
		
		imgView.setScaleType(ScaleType.CENTER_CROP);
		imgView.setVisibility(View.VISIBLE);
		imgView.setImageBitmap(bitmap);
	}
}
