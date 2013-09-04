package com.zdmddd.helper.blog;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.zdmddd.modal.Blog;

import android.util.SparseArray;


public class Blogs{
	private static Blogs blogs = null;
	private Blogs(){}
	public static Blogs getInstance(){
		if(blogs == null){
			blogs = new Blogs();
		}
		return blogs;
	}
	
	private SparseArray<List<Blog>> plazaListView_blogList_map = new SparseArray<List<Blog>>();
	private Map<Long,Blog> id_blog_map = new HashMap<Long,Blog>();
	
	private void _add_to_plazaListView_blogList_map(int hashCode,Blog blog){
		List<Blog> blogs = plazaListView_blogList_map.get(hashCode);
		if(blogs == null){
			blogs = new LinkedList<Blog>();
			plazaListView_blogList_map.put(hashCode, blogs);
		}
		blogs.add(blog);
	}
	private void _add_id_blog_map(Blog blog){
		this.id_blog_map.put(blog.getId(), blog);
	}
	public Blogs append(Blog blog,int hashCode){
		if(blog == null) return this;
		this._add_id_blog_map(blog);
		if(hashCode >= 0) this._add_to_plazaListView_blogList_map(hashCode, blog);
		return this;
	}
	public void addBlogs(List<Blog> blogs,int hashCode){
		for(Blog blog : blogs){
			this.append(blog, hashCode);
		}
	}
	
	public Blog getById(long id){
		return this.id_blog_map.get(id);
	}
	
	public Blog getByIndex(int hashCode,int index){
		if(hashCode < 0) return null;
		List<Blog> blogs = this.plazaListView_blogList_map.get(hashCode);
		if(blogs == null) return null;
		return blogs.get(index);
	}
	
	public int getPlazaBlogSize(int hashCode){
		if(hashCode < 0) return 0;
		List<Blog> blogs = this.plazaListView_blogList_map.get(hashCode);
		if(blogs == null) return 0;
		return blogs.size();
	}
	
	public void clearPlazaBlogs(int hashCode){
		if(hashCode < 0) return;
		List<Blog> blogs = this.plazaListView_blogList_map.get(hashCode);
		if(blogs == null) return;
		blogs.clear();
	}
	
	public int getIndexById(int hashCode,long blogId){
		if(hashCode < 0) return -1;
		List<Blog> blogs = this.plazaListView_blogList_map.get(hashCode);
		if(blogs == null) return -1;
		
		int index = -1;
		for(int i=0,il=blogs.size();i<il;i++){
			if(blogs.get(i).getId() == blogId){
				index = i;
				break;
			}
		}
		return index;
	}
	
}
