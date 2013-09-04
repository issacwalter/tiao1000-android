package com.zdmddd.view.plaza.component;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zdmddd.R;
import com.zdmddd.activity.BlogActivity;
import com.zdmddd.modal.Blog;
import com.zdmddd.view.plaza.Plaza;

public class PlazaListItem extends LinearLayout {

	private PlazaListImageView blogImage = null;
	private TextView blogContent = null;
	private TextView price = null;
	
	private Blog blog = null;
	private int plazaHash = -1;
	
	public PlazaListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	private void _init(){
		if(blogImage == null) this.blogImage = (PlazaListImageView)this.findViewById(R.id.blog_image);
		if(blogContent == null) this.blogContent = (TextView)this.findViewById(R.id.blog_content);
		if(price == null) this.price = (TextView)this.findViewById(R.id.price);
	}
	
	public Blog getBlog(){
		return this.blog;
	}
	
	public void reset(){
		blog = null;
		plazaHash = -1;
		
		blogContent.setText("");
		blogImage.setImageBitmap(null);
		price.setText("");
	}
	
	public void setBlog(Blog blog,int plazaHash){
		_init();
		
		reset();
		
		this.blog = blog;
		
		this.plazaHash = plazaHash;
		
		String title = blog.getTitle();
		if(title.length() > 100) title = title.substring(0, 100)+"...";
		blogContent.setText(title);
		
		ArrayList<String> imgs = blog.getImgs();
		if(imgs!=null && imgs.size()>0){
			String img = imgs.get(0);
			if(img.contains("#")){
				img = img.substring(0,img.indexOf("#"));
			}
			
			String img_url = img+"_80";
			blogImage.setImageUrl(img_url);
		}
		
		
		if(blog.getTips() != null && !blog.getTips().equals("")){
			this.price.setText(blog.getTips());
		}else if(blog.getPrice()>0){
			this.price.setText("￥"+blog.getPrice());
		}
	}
	
	public void startBlogActivity(){
		Intent intent = new Intent();
		intent.setClass(getContext(), BlogActivity.class);
		intent.putExtra("blog_id", this.blog.getId());
		intent.putExtra("plaza_hash", this.plazaHash);
		intent.putExtra("plaza_kind", Plaza.PLAZA_LIST_VIEW);
		((Activity)getContext()).startActivity(intent);
	}
}
