package com.zdmddd.view.plaza.component;

import zl.android.http.image_load.BitmapLoader.OnImageLoadedFailed;
import zl.android.http.image_load.BitmapLoader.OnImageLoadedSuccess;
import zl.android.http.image_load.LightBitmapLoader;
import zl.android.movie.ZLMovie;
import zl.android.utils.bitmap.ZLBitmap;
import zl.android.view.imageview.MemorySafeImageView;

import com.zdmddd.activity.BlogActivity;
import com.zdmddd.modal.Blog;
import com.zdmddd.view.plaza.Plaza;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PlazaBlogImageView extends MemorySafeImageView implements View.OnClickListener{
	private Blog blog = null;
	private String imgUrl=null;
	private int showHeight = 0;
	private int showWidth = 0;
	private int showTop = -1;/*在linearlayout中的top坐标*/
	private boolean isShowingBitmap = false;/*是否正在显示blog的图片*/
	
	private int plazaViewId;
	
	public void setPlazaViewId(int id){
		plazaViewId = id;
	}
	public PlazaBlogImageView(Context context){
		this(context,0);
	}
	public PlazaBlogImageView(Context context,int showWidth) {
		super(context);
		this.showWidth = showWidth-4;
	}
	public void setShowTop(int top){
		this.showTop = top;
	}
	public int getShowTop(){
		return this.showTop;
	}
	public Blog getBlog() {
		return blog;
	}
	public void setBlog(Blog blog) {
		this.blog = blog;
		imgUrl = blog.getImgs().get(0);
		int height = Integer.parseInt(imgUrl.split("#")[1]);
		this.showHeight = (int)(height / 150f * this.showWidth);
		imgUrl = imgUrl.split("#")[0]+"_android150";
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(showWidth, showHeight);
		params.setMargins(2, 0, 2, 3);
		this.showHeight += 3;
		this.setBackgroundColor(Color.WHITE);
		this.setLayoutParams(params);
		
		LightBitmapLoader.instance().load(imgUrl, this);
		
		this.setOnClickListener(this);
		
		isShowingBitmap = true;
	}
	public void setBlog(Blog blog,final OnImageLoadedSuccess onImageLoadSunccess,OnImageLoadedFailed onImageLoadFailed) {
		this.blog = blog;
		imgUrl = blog.getImgs().get(0);
		imgUrl = imgUrl.split("#")[0]+"_android150";
		
		setBackgroundColor(Color.WHITE);
		
		LightBitmapLoader.instance().load(imgUrl, this,new OnImageLoadedSuccess() {
			@Override
			public void onImageLoadedSuccess(String url, Bitmap bitmap,
					ZLMovie movie, int imageType, ImageView imgView) {
				// TODO Auto-generated method stub
				showHeight = bitmap.getHeight()+3;
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(showWidth, showHeight);
				params.setMargins(2, 0, 2, 3);
				setLayoutParams(params);
				onImageLoadSunccess.onImageLoadedSuccess(url, bitmap,null,ZLBitmap.IMAGE_TYPE_JPG, imgView);
			}
		},onImageLoadFailed);
		
		this.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getContext(), BlogActivity.class);
				intent.putExtras(PlazaBlogImageView.this.blog.toBundle());
				intent.putExtra("plazaViewId", plazaViewId);
				getContext().startActivity(intent);
			}
		});
		
		isShowingBitmap = true;
	}
	public void setImageBitmap(Bitmap bmp){
		if(bmp==null) isShowingBitmap = false;
		super.setImageBitmap(bmp);
	}
	public void reloadBitmap(){
		if(imgUrl==null || this.isShowingBitmap) return;
		LightBitmapLoader.instance().load(imgUrl, this);
	}
	public int getShowHeight(){
		return showHeight;
	}
	public boolean isShowingBitmap(){
		return this.isShowingBitmap;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(getContext(), BlogActivity.class);
		intent.putExtra("blog_id",this.blog.getId());
		intent.putExtra("plaza_hash", plazaViewId);
		intent.putExtra("plaza_kind", Plaza.PLAZA_VIEW);
		getContext().startActivity(intent);
	}
}
