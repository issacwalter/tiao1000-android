package com.zdmddd.view.plaza.component;


import zl.android.http.image_load.BitmapLoader;
import zl.android.http.image_load.LightBitmapLoader;
import zl.android.movie.ZLMovie;
import zl.android.view.imageview.LoadingImageView;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;

public class PlazaListImageView extends LoadingImageView{

	public PlazaListImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public PlazaListImageView(Context context) {
		super(context);
	}
	
	String url=null;
	public String getImageUrl(){
		return this.url;
	}
	public void setImageUrl(String url){
		this.url = url;
		this.setProgressColor(Color.parseColor("#00666666"));
		LightBitmapLoader.instance().load(url, this,new BitmapLoader.OnImageLoadedSuccess() {
			@Override
			public void onImageLoadedSuccess(String url, Bitmap bitmap,ZLMovie movie, int imageType, ImageView imgView) {
				// TODO Auto-generated method stub
				if(bitmap==null && movie!=null){
					bitmap = BitmapFactory.decodeByteArray(movie.getMovieData(), 0, movie.getMovieData().length);
				}
				if(url == PlazaListImageView.this.url){
					imgView.setImageBitmap(bitmap);
				}
			}
		},new BitmapLoader.OnImageLoadedFailed() {
			@Override
			public void onImageLoadedFailed(String url, ImageView imgView,String errorMsg, Exception e) {
			}
		},new BitmapLoader.OnImageLoadProgress() {
			@Override
			public void onImageLoadProgress(String url, int imageType,ImageView imgView, int loaded, int total) {
				// TODO Auto-generated method stub
				drawProgress(loaded,total);
			}
		},false);
	}
}
