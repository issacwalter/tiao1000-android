package com.zdmddd.view.plaza;

import java.util.ArrayList;
import java.util.Hashtable;

import zl.android.http.ZLHttpParameters;
import zl.android.log.Logger;
import zl.android.view.PushDownRefreshView;
import zl.android.view.ZLDevice;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.zdmddd.R;
import com.zdmddd.application.ZLApplication;
import com.zdmddd.config.UrlConfig;
import com.zdmddd.helper.blog.Blogs;
import com.zdmddd.helper.blog.BlogsLoader;
import com.zdmddd.modal.Blog;
import com.zdmddd.modal.User;
import com.zdmddd.service.BlogService;
import com.zdmddd.service.Service;
import com.zdmddd.view.event.ScrollViewStopDetector;
import com.zdmddd.view.plaza.component.PlazaLinearLayout;

public class PlazaView implements PushDownRefreshView.RefreshListener,Plaza{
	private ScrollView plazaContainer;
	private ZLApplication app;
	
	private PlazaLinearLayout leftLayout;
	private PlazaLinearLayout centerLayout;
	private PlazaLinearLayout rightLayout;
	
	private int screenHeight=0;
	private int screenWidth=0;
	
	private TextView loadText;
	
	private Blogs blogs = Blogs.getInstance();
	
	private static Hashtable<Integer,PlazaView> plazaViews = new Hashtable<Integer,PlazaView>();
	public PlazaView(ScrollView plazaContainer){
		_init(plazaContainer);
		plazaViews.put(this.hashCode(), this);
	}
	public static PlazaView getPlazaViewById(int id){
		return plazaViews.get(id);
	}
	private void _init(ScrollView plazaContainer){
		this.plazaContainer = plazaContainer;
		app = (ZLApplication)this.plazaContainer.getContext().getApplicationContext();
		
		this.leftLayout = (PlazaLinearLayout)this.plazaContainer.findViewById(R.id.left);
		this.centerLayout = (PlazaLinearLayout)this.plazaContainer.findViewById(R.id.center);
		this.rightLayout = (PlazaLinearLayout)this.plazaContainer.findViewById(R.id.right);
		loadText = (TextView)this.plazaContainer.findViewById(R.id.load_more);
		
		screenWidth = ZLDevice.getScreenWidth(plazaContainer.getContext());
		screenHeight = ZLDevice.getScreenHeight(plazaContainer.getContext());
		int layoutWidth = (int)(screenWidth / 3f);
		leftLayout.setShowWidth(layoutWidth);
		centerLayout.setShowWidth(layoutWidth);
		rightLayout.setShowWidth(layoutWidth);
		
		leftLayout.setPlazaViewId(this.hashCode());
		centerLayout.setPlazaViewId(this.hashCode());
		rightLayout.setPlazaViewId(this.hashCode());
	}	
	
	private int _getContainerShowHeight(){
		int leftHeight = leftLayout.getShowHeight();
		int centerHeight = centerLayout.getShowHeight();
		int rightHeight = rightLayout.getShowHeight();
		if(leftHeight >= centerHeight && leftHeight >= rightHeight){
			return leftHeight;
		}else if(centerHeight >= leftHeight && centerHeight >= rightHeight){
			return centerHeight;
		}else{
			return rightHeight;
		}
	}
	private int plazaKind;
	/**
	 * 开始加载blog
	 */
	ZLHttpParameters httpParams=null;
	public void startLoadBlogs(int plazaKind,Bundle bundleParams){
		this.plazaKind = plazaKind;
		_constructHttpParams(bundleParams);
		if (httpParams == null) return;
		
		_load(null);
		_bindEvent();
	}
	
	private void _addBlog(Blog blog){
		int leftHeight = leftLayout.getShowHeight();
		int centerHeight = centerLayout.getShowHeight();
		int rightHeight = rightLayout.getShowHeight();
		if(leftHeight <= centerHeight && leftHeight <= rightHeight){
			leftLayout.addBlog(blog);
		}else if(centerHeight <= leftHeight && centerHeight <= rightHeight){
			centerLayout.addBlog(blog);
		}else{
			rightLayout.addBlog(blog);
		}
	}
	private void _addBlogs(ArrayList<Blog> blogs){
		for(Blog blog : blogs){
			_addBlog(blog);
		}
	}
	
	private boolean isRefresh = false;
	private boolean isLoading = false;
	private void _load(final BlogsLoader.Callback cb){
		if(isLoading) return;
		
		isLoading = true;
		loadText.setVisibility(View.VISIBLE);
		BlogsLoader.getInstance().addTask(httpParams, new BlogsLoader.Callback() {
			@Override
			public void onComplete(int status, String msg, ArrayList<Blog> blogs) {
				loadText.setVisibility(View.GONE);
				
				if(cb!=null) cb.onComplete(status, msg, blogs);
				
				if(BlogService.noticeExceptSuccess(app, status, msg)) {
					loadText.setVisibility(View.GONE);
					isLoading = false;
					return;
				}
				
				_constructHttpParams(null);
				
				if(blogs==null || blogs.size()==0){
					Toast.makeText(plazaContainer.getContext(), "没有更多了", Toast.LENGTH_SHORT).show();
					loadText.setVisibility(View.GONE);
					isLoading = false;
					return;
				}else{
					if(isRefresh){
						if(blogs.size()>0&&PlazaView.this.blogs.getPlazaBlogSize(PlazaView.this.hashCode())>0
								&&blogs.get(0).getId()==PlazaView.this.blogs.getByIndex(PlazaView.this.hashCode(), 0).getId()){
							Toast.makeText(plazaContainer.getContext(), "已经是最新", Toast.LENGTH_SHORT).show();
						}else{
							PlazaView.this.blogs.clearPlazaBlogs(PlazaView.this.hashCode());
							leftLayout.reset();
							centerLayout.reset();
							rightLayout.reset();
						}
						downRefresh.setViewHeight(0);
						isRefresh = false;
					}
					
					_addBlogs(blogs);
				}
				
				loadText.setVisibility(View.GONE);
				isLoading = false;
			}
		}, this.hashCode());
		
	}
	
	private void _constructHttpParams(Bundle bundleParams) {
		if (httpParams != null) {
			switch(this.plazaKind){
			case PLAZA_NEW:
			case PLAZA_HOT:
			case PLAZA_CATEGORY:
			case PLAZA_SUB_CATEGORY:
			case PLAZA_ELE:{
				int skip = 1+blogs.getPlazaBlogSize(this.hashCode());
				httpParams.put("skip", String.valueOf(skip));
				break;
			}
			case PLAZA_LIKE:
			case PLAZA_MY_SHARE:
			case PLAZA_FAVOR:{
				if(blogs.getPlazaBlogSize(this.hashCode())>0){
					Blog blog = blogs.getByIndex(this.hashCode(), blogs.getPlazaBlogSize(this.hashCode())-1);
					httpParams.put("create_timestamp", String.valueOf(blog.getTime()));
				}
				break;
			}
			case PLAZA_SEARCH:{
				if(blogs.getPlazaBlogSize(this.hashCode())>0) 
					httpParams.put("page_no", String.valueOf(Integer.parseInt(httpParams.get("page_no"))+1));
				break;
			}
			}
		} else {
			httpParams = new ZLHttpParameters();
			switch (this.plazaKind) {
			case PLAZA_NEW:{
				httpParams.setUrl(UrlConfig.PLAZA_NEW);
				httpParams.put("skip", "1");
				break;
			}
			case PLAZA_HOT:{
				httpParams.setUrl(UrlConfig.PLAZA_HOT);
				httpParams.put("skip", "1");
				break;
			}
			case PLAZA_CATEGORY:{
				httpParams.setUrl(UrlConfig.CATEGORY_HOT + "/"
						+ bundleParams.getString("uname"));
				httpParams.put("skip", "1");
				break;
			}
			case PLAZA_SUB_CATEGORY:{
				httpParams.setUrl(UrlConfig.CATEGORY_HOT + "/"
						+ bundleParams.getString("uname") + "/"
						+ bundleParams.getString("sub_uname"));
				httpParams.put("skip", "1");
				break;
			}
			case PLAZA_LIKE: {
				httpParams.setUrl(UrlConfig.PLAZA_LIKE);
				Object selfObj = app.getAttribute("self");
				if (selfObj == null || !(selfObj instanceof User))
					return;
				User self = (User) selfObj;
				httpParams.put("user_id", String.valueOf(self.getId()));
				httpParams.put("create_timestamp",
						String.valueOf(System.currentTimeMillis()));
				httpParams.put("type", "3");
				break;
			}
			case PLAZA_FAVOR: {
				httpParams.setUrl(UrlConfig.PLAZA_FAVOR);
				Object selfObj = app.getAttribute("self");
				if (selfObj == null || !(selfObj instanceof User))
					return;
				httpParams.put("create_timestamp",
						String.valueOf(System.currentTimeMillis()));
				break;
			}
			case PLAZA_SEARCH: {
				httpParams.setUrl(UrlConfig.PLAZA_SEARCH);
				httpParams.put("k", bundleParams.getString("keywords"));
				httpParams.put("page_no", "1");
				break;
			}
			case PLAZA_ELE: {
				httpParams.setUrl(UrlConfig.CATEGORY_HOT + "/"
						+ bundleParams.getString("uname"));
				httpParams.put("skip", "1");
				httpParams.put("ele", bundleParams.getString("ele"));
				break;
			}
			case PLAZA_MY_SHARE:{
				httpParams.setUrl(UrlConfig.PLAZA_LIKE);
				Object selfObj = app.getAttribute("self");
				if (selfObj == null || !(selfObj instanceof User))
					return;
				User self = (User) selfObj;
				httpParams.put("user_id", String.valueOf(self.getId()));
				httpParams.put("create_timestamp",
						String.valueOf(System.currentTimeMillis()));
				httpParams.put("type", "0");
				break;
			}
			}
		}
	}
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if(this.plazaKind == PLAZA_NEW){
			httpParams.put("skip", "1");
		}else if(this.plazaKind == PLAZA_HOT){
			httpParams.put("skip", "1");
		}else if(this.plazaKind == PLAZA_CATEGORY){
			httpParams.put("skip", "1");
		}else if(this.plazaKind == PLAZA_SUB_CATEGORY){
			httpParams.put("skip", "1");
		}else if(this.plazaKind==PLAZA_LIKE){
			Object selfObj = app.getAttribute("self");
			if(selfObj == null || !(selfObj instanceof User)) return;
			User self = (User)selfObj;
			httpParams.put("user_id", String.valueOf(self.getId()));
			httpParams.put("create_timestamp", String.valueOf(System.currentTimeMillis()));
			httpParams.put("type", "3");
		}else if(this.plazaKind==PLAZA_FAVOR){
			Object selfObj = app.getAttribute("self");
			if(selfObj == null || !(selfObj instanceof User)) return;
			httpParams.put("create_timestamp",String.valueOf(System.currentTimeMillis()));
		}else if(this.plazaKind==PLAZA_SEARCH){
			httpParams.put("page_no", "1");
		}else if(this.plazaKind==PLAZA_ELE){
			httpParams.put("skip", "1");
		}else if(this.plazaKind == PLAZA_MY_SHARE){
			Object selfObj = app.getAttribute("self");
			if(selfObj == null || !(selfObj instanceof User)) return;
			User self = (User)selfObj;
			httpParams.put("user_id", String.valueOf(self.getId()));
			httpParams.put("create_timestamp", String.valueOf(System.currentTimeMillis()));
			httpParams.put("type", "0");
		}
		this.isRefresh = true;
		_load(null);
	}
	
	private ScrollViewStopDetector detector=new ScrollViewStopDetector();
	private PushDownRefreshView downRefresh;
	private void _bindEvent(){
		downRefresh = (PushDownRefreshView)this.plazaContainer.findViewById(R.id.down_refresh);
		downRefresh.setOnRefreshListener(this);
		detector.setOnScrollStop(new ScrollViewStopDetector.OnScrollStop() {
			@Override
			public void onScrollStop(View v, int action) {
				Logger.info("stop");
				leftLayout.avoidMemoryLeak(v.getScrollY()-screenHeight * 2, screenHeight+screenHeight * 2,  action);
				centerLayout.avoidMemoryLeak(v.getScrollY()-screenHeight * 2, screenHeight+screenHeight * 2,  action);
				rightLayout.avoidMemoryLeak(v.getScrollY()-screenHeight * 2, screenHeight+screenHeight * 2,  action);
			}
		});
		this.plazaContainer.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(final View v, MotionEvent event) {
				if(downRefresh.touch(v,event)){
					return true;
				}
				detector.touch(v, event);
				if(event.getAction()==MotionEvent.ACTION_MOVE){
					int leftValue = _getContainerShowHeight()-plazaContainer.getScrollY();
					int rightValue = screenHeight;
					
					if(leftValue < rightValue){
						_load(null);
					}
				}
				
				return false;
			}
		});
	}
	
	public interface GetNextBlogByIdCallback{void onComplete(int status,String msg,Blog blog);}
	public void getNextBlogById(long blogId, final GetNextBlogByIdCallback cb) {
		int index = blogs.getIndexById(this.hashCode(), blogId);
		if (index == -1 || index + 1 > blogs.getPlazaBlogSize(this.hashCode()) - 1) {
			_load(new BlogsLoader.Callback() {
				@Override
				public void onComplete(int status, String msg, ArrayList<Blog> blogs) {
					// TODO Auto-generated method stub
					if (cb != null){
						cb.onComplete(status,msg,blogs == null || blogs.size() == 0 ? null	: blogs.get(0));
					}
				}
			});
		} else {
			if (cb != null)
				cb.onComplete(Service.STATUS_SUCCESS, null,blogs.getByIndex(this.hashCode(), index+1));
		}
	}

	public Blog getPreviousBlogByIndex(long blogId) {
		int index = blogs.getIndexById(this.hashCode(), blogId);
		if (index < 1 || index == -1) {
			return null;
		}
		if (blogs.getPlazaBlogSize(this.hashCode()) - 1 < index - 1) {
			return null;
		}
		return blogs.getByIndex(this.hashCode(), index - 1);
	}
}
