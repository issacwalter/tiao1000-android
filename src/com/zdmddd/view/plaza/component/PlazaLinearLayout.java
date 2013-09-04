package com.zdmddd.view.plaza.component;

import java.util.ArrayList;
import java.util.Collections;

import zl.android.http.image_load.BitmapLoader.OnImageLoadedFailed;
import zl.android.http.image_load.BitmapLoader.OnImageLoadedSuccess;
import zl.android.log.Logger;
import zl.android.movie.ZLMovie;

import com.zdmddd.modal.Blog;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PlazaLinearLayout extends LinearLayout {
	private int showHeight=0;
	private int showWidth = 0;
	
	private int plazaViewId;
	
	public void setPlazaViewId(int id){
		this.plazaViewId = id;
	}
	public PlazaLinearLayout(Context context) {
		super(context);
		this.setOrientation(LinearLayout.VERTICAL);
	}
	public PlazaLinearLayout(Context context,AttributeSet attrs) {
		super(context,attrs);
		this.setOrientation(LinearLayout.VERTICAL);
	}
	
	public void setShowWidth(int showWidth){
		this.showWidth = showWidth;
	}
	public int getShowWidth(){
		return this.showWidth;
	}
	public void addBlog(Blog blog){
		PlazaBlogImageView imgView = new PlazaBlogImageView(this.getContext(),showWidth);
		imgView.setBlog(blog);
		imgView.setPlazaViewId(this.plazaViewId);
		this.addView(imgView);
		
		imgView.setShowTop(this.showHeight);
		showHeight += imgView.getShowHeight();
		
		this.showingImageIndexes.add(this.getChildCount()-1);
		Collections.sort(this.showingImageIndexes);
	}
	
	public void addBlogAsyn(Blog blog,PlazaBlogImageView imgView){
		imgView.setPlazaViewId(this.plazaViewId);
		this.addView(imgView);
		
		imgView.setShowTop(this.showHeight);
		showHeight += imgView.getShowHeight();
		
		this.showingImageIndexes.add(this.getChildCount()-1);
		Collections.sort(this.showingImageIndexes);
	}
	
	public void addBlog(Blog blog,final OnImageLoadedSuccess onImageLoadSunccess,OnImageLoadedFailed onImageLoadFailed){
		PlazaBlogImageView imgView = new PlazaBlogImageView(this.getContext(),showWidth);
		imgView.setBlog(blog,new OnImageLoadedSuccess() {
			@Override
			public void onImageLoadedSuccess(String url, Bitmap bitmap,
					ZLMovie movie, int imageType, ImageView v) {
				// TODO Auto-generated method stub
				PlazaBlogImageView imgView = (PlazaBlogImageView)v;
				imgView.setPlazaViewId(plazaViewId);
				addView(imgView);
				
				imgView.setShowTop(showHeight);
				showHeight += imgView.getShowHeight();
				
				showingImageIndexes.add(getChildCount()-1);
				Collections.sort(showingImageIndexes);
			}
		},onImageLoadFailed);
	}
	
	public int getShowHeight(){
		return showHeight;
	}
	
	/**
	 * 显示与隐藏，防止内存溢出
	 * @param scrollTop 滚动条的top
	 * @param screenHeight 手机屏幕的高度
	 * @param action 手指滑动的方式，-1表示向上滑动，1表示向下滑动，0表示没有滑动
	 */
	private ArrayList<Integer> showingImageIndexes = new ArrayList<Integer>();
	public void avoidMemoryLeak(int scrollTop,int screenHeight,int action){
		if(action==3) return;
		if(action==-1){
			/*向下滚动*/
			ArrayList<Integer> temp = new ArrayList<Integer>();
			for(int i=0,il=this.showingImageIndexes.size();i<il;i++){
				PlazaBlogImageView view = (PlazaBlogImageView)this.getChildAt(this.showingImageIndexes.get(i));
				if(view.getShowTop()+view.getShowHeight() < scrollTop){
					view.setImageBitmap(null);
					temp.add(this.showingImageIndexes.get(i));
				}else{
					break;
				}
			}
			if(this.showingImageIndexes.size() > 0){
				int lastIndex = this.showingImageIndexes.get(this.showingImageIndexes.size()-1);
				for(int i=lastIndex+1,il=this.getChildCount();i<il;i++){
					PlazaBlogImageView view = (PlazaBlogImageView)this.getChildAt(i);
					if(view.getShowTop() < scrollTop+screenHeight && !view.isShowingBitmap()){
						view.reloadBitmap();
						this.showingImageIndexes.add(i);
					}else{
						break;
					}
				};
			}
			for(int i=0,il=temp.size();i<il;i++){
				this.showingImageIndexes.remove(Integer.valueOf((temp.get(i))));
			}
			Collections.sort(showingImageIndexes);
		}else if(action==1){
			/*向上滚动*/
			ArrayList<Integer> temp = new ArrayList<Integer>();
			for(int i=this.showingImageIndexes.size()-1;i>=0;i--){
				PlazaBlogImageView view = (PlazaBlogImageView)this.getChildAt(this.showingImageIndexes.get(i));
				if(view.getShowTop() > scrollTop + screenHeight){
					view.setImageBitmap(null);
					temp.add(this.showingImageIndexes.get(i));
				}else{
					break;
				}
			}
			if(this.showingImageIndexes.size() > 0){
				int firstIndex = this.showingImageIndexes.get(0);
				for(int i=firstIndex-1;i>=0;i--){
					PlazaBlogImageView view = (PlazaBlogImageView)this.getChildAt(i);
					Logger.info("scrollTop:"+scrollTop);
					if(view.getShowTop()+view.getShowHeight() > scrollTop && !view.isShowingBitmap()){
						view.reloadBitmap();
						this.showingImageIndexes.add(i);
					}else{
						break;
					}
				}
			}
			for(int i=0,il=temp.size();i<il;i++){
				this.showingImageIndexes.remove(Integer.valueOf((temp.get(i))));
			}
			Collections.sort(showingImageIndexes);
		}
	}
	
	public void reset(){
		this.removeAllViews();
		this.showingImageIndexes.clear();
		this.showHeight = 0;
	}
}
