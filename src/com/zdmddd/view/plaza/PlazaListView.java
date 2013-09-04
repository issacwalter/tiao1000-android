package com.zdmddd.view.plaza;

import java.util.ArrayList;
import java.util.Hashtable;

import com.zdmddd.R;

import zl.android.http.ZLHttpParameters;
import zl.android.view.listview.TouchLoadListView;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.zdmddd.application.ZLApplication;
import com.zdmddd.config.UrlConfig;
import com.zdmddd.helper.blog.Blogs;
import com.zdmddd.helper.blog.BlogsLoader;
import com.zdmddd.modal.Blog;
import com.zdmddd.modal.User;
import com.zdmddd.service.Service;
import com.zdmddd.view.plaza.component.PlazaListItem;

public class PlazaListView extends TouchLoadListView implements TouchLoadListView.OnRefreshListener,Plaza{
	private ZLApplication app;

	private Blogs blogs = Blogs.getInstance();

	private ListViewAdapter adapter;

	private static Hashtable<Integer, PlazaListView> plazaViews = new Hashtable<Integer, PlazaListView>();

	public PlazaListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		_init();
		plazaViews.put(this.hashCode(), this);
	}

	public PlazaListView(Context context) {
		super(context);
		_init();
		plazaViews.put(this.hashCode(), this);
	}

	public static PlazaListView getPlazaViewById(int id) {
		return plazaViews.get(id);
	}

	private void _init() {
		app = (ZLApplication) this.getContext().getApplicationContext();
	}

	private final class ListViewAdapter extends BaseAdapter {
		private LayoutInflater inflater = LayoutInflater.from(getContext());

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.plaza_list_item,PlazaListView.this, false);
			}
			Blog blog = blogs.getByIndex(PlazaListView.this.hashCode(), position);
			((PlazaListItem) convertView).setBlog(blog,PlazaListView.this.hashCode());
			convertView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					PlazaListView.this.performItemClick(v, position, 0);
				}
			});
			return convertView;
		}

		public long getItemId(int position) {
			// return position;
			return 0;
		}

		public Object getItem(int position) {
			return blogs.getByIndex(PlazaListView.this.hashCode(), position);
		}

		public int getCount() {
			return blogs.getPlazaBlogSize(PlazaListView.this.hashCode());
		}
	}

	private int plazaKind;
	private boolean isStarted = false;

	public boolean isStarted() {
		return this.isStarted;
	}

	/**
	 * 开始加载blog
	 */
	ZLHttpParameters httpParams = null;

	private void _constructHttpParams(Bundle bundleParams) {
		if (httpParams != null) {
			switch(this.plazaKind){
			case PLAZA_NEW:
			case PLAZA_HOT:
			case PLAZA_CATEGORY:
			case PLAZA_SUB_CATEGORY:
			case PLAZA_RECOMMAND:
			case PLAZA_SHOW_SCATE:
			case PLAZA_ELE:{
				int skip = 1+blogs.getPlazaBlogSize(this.hashCode());
				httpParams.put("skip", String.valueOf(skip));
				break;
			}
			case PLAZA_LIKE:
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
			case PLAZA_RECOMMAND:{
				httpParams.setUrl(UrlConfig.PLAZA_RECOMMAND);
				httpParams.append("skip", "0");
				break;
			}
			case PLAZA_SHOW_SCATE:{
				String scate = bundleParams.getString("scate");
				httpParams.setUrl(UrlConfig.SHOW_SCATE+scate+".json");
				httpParams.append("skip", "0");
				break;
			}
			}
		}
	}

	private void _load(final BlogsLoader.Callback cb){
		BlogsLoader.getInstance().addTask(httpParams, new BlogsLoader.Callback() {
			@Override
			public void onComplete(int status, String msg, ArrayList<Blog> blogs) {
				_constructHttpParams(null);
				if(cb!=null) cb.onComplete(status, msg, blogs);
			}
		}, this.hashCode());
	}
	
	public void startLoadBlogs(int plazaKind, Bundle bundleParams) {
		isStarted = true;

		this.plazaKind = plazaKind;
		_constructHttpParams(bundleParams);
		if (httpParams == null)
			return;

		adapter = new ListViewAdapter();
		this.setonRefreshListener(this);

		_load(new BlogsLoader.Callback() {
			@Override
			public void onComplete(int status, String msg, ArrayList<Blog> blogs) {
				if (blogs != null) {
					adapter.notifyDataSetChanged();
				}
			}
		});
		setAdapter(adapter);
		this.setonLoadListener(new OnLoadListener() {
			@Override
			public void onLoad() {
				_load(new BlogsLoader.Callback() {
					@Override
					public void onComplete(int status, String msg, ArrayList<Blog> blogs) {
						if (blogs != null) {
							if (blogs.size() == 0) {
								Toast.makeText(getContext(), "没有更多了！",Toast.LENGTH_SHORT).show();
							}else{
								_constructHttpParams(null);
							}
						}
						PlazaListView.this.onLoadComplete();
					}
				});
			}
		});
		
		this.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (view instanceof PlazaListItem) {
					PlazaListItem item = (PlazaListItem) view;
					item.startBlogActivity();
				}
			}
		});
	}

	public interface GetNextBlogByIdCallback {
		void onComplete(int status, String msg, Blog blog);
	}

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

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if(this.plazaKind == PLAZA_NEW || this.plazaKind == PLAZA_HOT 
				|| this.plazaKind == PLAZA_CATEGORY || this.plazaKind == PLAZA_SUB_CATEGORY
				|| this.plazaKind == PLAZA_RECOMMAND || this.plazaKind == PLAZA_SHOW_SCATE){
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
		}
		
		_load(new BlogsLoader.Callback() {
			@Override
			public void onComplete(int status, String msg, ArrayList<Blog> blogs) {
				// TODO Auto-generated method stub
				if(blogs != null){
					PlazaListView.this.blogs.clearPlazaBlogs(PlazaListView.this.hashCode());
					PlazaListView.this.blogs.addBlogs(blogs, PlazaListView.this.hashCode());
					adapter.notifyDataSetChanged();
				}
				PlazaListView.this.onRefreshComplete();
			}
		});
	}
}