package com.zdmddd.view.blog;


import zl.android.http.image_load.BitmapLoader;
import zl.android.http.image_load.LightBitmapLoader;
import zl.android.movie.ZLMovie;
import zl.android.utils.bitmap.ZLBitmap;
import zl.android.view.ZLDevice;
import zl.android.view.imageview.gif.GifView;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class BlogImageView extends GifView {
	private int default_image_max_height=3500;
	public BlogImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public BlogImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	String url;
	public void setBitmap(String url,int max_height){
		this.url = url;
		drawProgress(0, 100);
		this.default_image_max_height = max_height;
		this.setMinimumHeight(100);
		this.setProgressColor(Color.parseColor("#0078B6"));
		LightBitmapLoader.instance().load(url, this,new BitmapLoader.OnImageLoadedSuccess() {
			@Override
			public void onImageLoadedSuccess(String url, Bitmap bitmap,ZLMovie movie, int imageType, ImageView imgView) {
				// TODO Auto-generated method stub
				drawProgress(0, 100);
				if(url!=BlogImageView.this.url) return;
				
				imgView.setMinimumHeight(0);
				if(imageType!=ZLBitmap.IMAGE_TYPE_UNKNOWN){
					if(imageType==ZLBitmap.IMAGE_TYPE_GIF){
						if(movie.getWidth()<=ZLDevice.getScreenWidth(getContext())){
							LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(movie.getWidth(),movie.getHeight());
							params.gravity = Gravity.CENTER_HORIZONTAL;
							imgView.setLayoutParams(params);
							((BlogImageView)imgView).setGifImage(movie.getMovieData());
						}else{
							Toast.makeText(getContext(), "过大的gif图片！", Toast.LENGTH_SHORT).show();
						}
					}else{
						imgView.setScaleType(ScaleType.FIT_XY);
						int width = ZLDevice.getScreenWidth(BlogImageView.this.getContext());
						if(bitmap.getHeight()>default_image_max_height){
							bitmap = ZLBitmap.instance().bitmapCutBySize(bitmap, bitmap.getWidth(), default_image_max_height);
						}
						int height = (int)(width / (float)bitmap.getWidth() * bitmap.getHeight());
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,height);
						imgView.setLayoutParams(params);
						imgView.setImageBitmap(bitmap);
					}
				}
			}
		},null,new BitmapLoader.OnImageLoadProgress() {
			@Override
			public void onImageLoadProgress(String url, int imageType,
					ImageView imgView, int loaded, int total) {
				// TODO Auto-generated method stub
				if(url.equals(BlogImageView.this.url))drawProgress(loaded, total);
				else drawProgress(0, total);
			}
		},false);
	}
	public void setBitmap(String url){
		this.setBitmap(url, 4000);
	}
}
