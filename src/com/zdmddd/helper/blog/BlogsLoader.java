package com.zdmddd.helper.blog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.zdmddd.modal.Blog;
import com.zdmddd.service.BlogService;
import com.zdmddd.service.ServiceFactory;

import zl.android.http.ZLHttpParameters;

public class BlogsLoader {
	private static BlogsLoader blogsLoader = null;
	private BlogsLoader(){}
	public static BlogsLoader getInstance(){
		if(blogsLoader == null){
			blogsLoader = new BlogsLoader();
		}
		return blogsLoader;
	}
	
	public interface Callback{void onComplete(int status, String msg, ArrayList<Blog> blogs);}
	private HashMap<ZLHttpParameters,Callback> callbacks = new HashMap<ZLHttpParameters,Callback>();
	private HashMap<ZLHttpParameters,Integer> hashCodes = new HashMap<ZLHttpParameters,Integer>();
	private LinkedList<ZLHttpParameters> tasks = new LinkedList<ZLHttpParameters>();
	
	public void addTask(ZLHttpParameters params,Callback cb,int hashCode){
		this.callbacks.put(params, cb);
		this.tasks.addFirst(params);
		this.hashCodes.put(params, hashCode);
		
		_load();
	}
	
	
	private boolean isLoading = false;
	private void _load(){
		if(isLoading) return;
		if(tasks.size() == 0) return;
		
		final ZLHttpParameters params = tasks.get(0);
		final Callback cb = callbacks.get(params);
		final int hashCode = hashCodes.get(params)==null?0:hashCodes.get(params);
		if(params == null || cb == null || hashCode == 0) return;
		
		ServiceFactory.createService(BlogService.class).getBlogs(params, new BlogService.GetBlogsCallback() {
			@Override
			public void onComplete(int status, String msg, ArrayList<Blog> blogs) {
				tasks.remove(params);
				callbacks.remove(params);
				hashCodes.remove(params);
				
				if(blogs != null){
					Blogs.getInstance().addBlogs(blogs, hashCode);
				}
				
				cb.onComplete(status,msg,blogs);
				
				isLoading = false;
				
				if(tasks.size()>0){
					_load();
				}
			}
		});
	}
}
