package com.zdmddd.view;

import java.util.ArrayList;

import android.app.Activity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zdmddd.R;

import zl.android.http.image_load.LightBitmapLoader;
import zl.android.log.Logger;
import zl.android.utils.ObjectConvertor;
import zl.android.view.ZLDevice;

import com.zdmddd.config.UrlConfig;
import com.zdmddd.modal.Blog;
import com.zdmddd.modal.Comment;
import com.zdmddd.service.CommentService;
import com.zdmddd.service.Service;
import com.zdmddd.service.ServiceFactory;
import com.zdmddd.view.blog.DetailBlogImageView;
import com.zdmddd.view.event.ScrollViewStopDetector;

import zl.android.view.textview.ZLHtmlTextView;
public class BlogContentView {
	private ViewGroup container;
	private Blog blog;
	
	private ViewGroup commentsContainer;
	private Button prePageButton;
	private Button nextPageButton;
	private ViewGroup goodsDescriptionContainer;
	private ViewGroup imagesContainer;
	
	GestureDetector detector;
	
	int screenWidth;
	int screenHeight;
	public BlogContentView(ViewGroup container,GestureDetector detector){
		this.container = container;
		commentsContainer = (ViewGroup)container.findViewById(R.id.comments_container);
		prePageButton = (Button)container.findViewById(R.id.pre_page);
		nextPageButton = (Button)container.findViewById(R.id.next_page);
		goodsDescriptionContainer = (ViewGroup)container.findViewById(R.id.goodsDescriptionContainer);
		imagesContainer = (ViewGroup)container.findViewById(R.id.imagesContainer);
		this.detector = detector;
		
		screenWidth = ZLDevice.getScreenWidth((Activity)this.container.getContext());
		screenHeight = ZLDevice.getScreenHeight((Activity)this.container.getContext());
		
		descriptionContainerOriginalTop = screenHeight-ObjectConvertor.dip2px(container.getContext(), 125);
	}
	public ViewGroup getContainer(){
		return this.container;
	}
	
	public void unloadImage(){
		for(int i=0,il=imagesContainer.getChildCount();i<il;i++){
			if(imagesContainer.getChildAt(i) instanceof DetailBlogImageView){
				((DetailBlogImageView)imagesContainer.getChildAt(i)).setImageBitmap(null);
				Logger.error("unload image");
			}
		}
	}
	public void reloadImages(){
		_loadImages();
	}
	public void show(){
		this.container.setVisibility(View.VISIBLE);
	} 
	public void hide(){
		this.container.setVisibility(View.GONE);
	}
	public Blog getBlog(){
		return this.blog;
	}
	
	private void _loadImages(){
		ArrayList<String> imgs = this.blog.getImgs();
		for(int i=0,il=imgs.size();i<il;i++){
			String imgUrl = imgs.get(i).split("#")[0];
			if(imagesContainer.getChildCount()-1<i){
				DetailBlogImageView imgView = new DetailBlogImageView(imagesContainer.getContext());
				imgView.setVisibility(View.GONE);
				imgView.setUrl(imgUrl);
				imagesContainer.addView(imgView);
			}else {
				((DetailBlogImageView)imagesContainer.getChildAt(i)).setUrl(imgUrl);
			}
		}
	}
	public void setBlog(Blog blog){
		this.blog = blog;
		
		/*blog title*/
		((TextView)this.container.findViewById(R.id.blog_title)).setText(this.blog.getTitle());
		
		/*user head*/
		String headUrl = blog.getHead();
		headUrl = (headUrl==null||headUrl.equals("")?UrlConfig.HEAD_NONE:headUrl);
		LightBitmapLoader.instance().load(headUrl,(ImageView)container.findViewById(R.id.head));
		
		/*user nick_name*/
		((TextView)this.container.findViewById(R.id.nick_name)).setText(blog.getName());
		
		/*like_no*/
		if(blog.getLike_no() != 0){
			((TextView)this.container.findViewById(R.id.like_no)).setText(blog.getLike_no()+"人喜欢");
		}
		
		/*price*/
		if(blog.getPrice()>0){
			((TextView)this.container.findViewById(R.id.price)).setText("￥"+String.valueOf(blog.getPrice()));
		}
		
		/*content*/
		((ZLHtmlTextView)this.container.findViewById(R.id.content)).setHtmlText(blog.getContent().replace("&#12288;", ""));
		
		_loadImages();
		
		_renderComments(1,blog.getId());
		
		this.container.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(detector!=null) detector.onTouchEvent(event);
				if(stopDetector!=null) stopDetector.touch(v, event);
				return false;
			}
		});
		
		
		prePageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(currentCommentsPageIndex > 0){
					currentCommentsPageIndex--;
					for(int i=0;i<20;i++){
						commentsContainer.getChildAt(i).setVisibility(View.GONE);
					}
					_renderComments(currentCommentsPageIndex,BlogContentView.this.blog.getId());
				}
			}
		});
		nextPageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(currentCommentsPageIndex < pageCount){
					currentCommentsPageIndex++;
					for(int i=0;i<20;i++){
						commentsContainer.getChildAt(i).setVisibility(View.GONE);
					}
					_renderComments(currentCommentsPageIndex,BlogContentView.this.blog.getId());
				}
			}
		});
		
		this.container.scrollTo(0, 0);
		
		/*FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)goodsDescriptionContainer.getLayoutParams();
		params.topMargin = descriptionContainerOriginalTop;
		goodsDescriptionContainer.setLayoutParams(params);
		
		try{
			LinearLayout switchBlogContainer = (LinearLayout)container.getRootView().findViewById(R.id.pre_blog).getParent();
			params = (FrameLayout.LayoutParams)switchBlogContainer.getLayoutParams();
			params.topMargin = descriptionContainerOriginalTop - ObjectConvertor.dip2px(this.container.getContext(), 40);
			switchBlogContainer.setLayoutParams(params);
		}catch(Exception e){
			Logger.error(e);
		}*/
	}
	
	
	public void loadComments(){
		_renderComments(1, blog.getId());
	}
	/**
	 * 检查“描述”的位置
	 */
	private int descriptionContainerOriginalTop;
	private ScrollViewStopDetector stopDetector = new ScrollViewStopDetector();
	public void checkDescriptionContainerPos(){
		/*if(stopDetector.getOnScrollStop()==null){
			stopDetector.setOnScrollStop(new ScrollViewStopDetector.OnScrollStop() {
				@Override
				public void onScrollStop(View v, int action) {
					checkDescriptionContainerPos();
				}
			});
		}
		if(descriptionContainerOriginalTop+container.getScrollY()+ObjectConvertor.dip2px(container.getContext(), 50) > imagesContainer.getBottom()) {
			goodsDescriptionContainer.layout(goodsDescriptionContainer.getLeft(), 
					imagesContainer.getBottom()-ObjectConvertor.dip2px(container.getContext(), 50),
					goodsDescriptionContainer.getRight(),
					imagesContainer.getBottom());
		}else{
			goodsDescriptionContainer.layout(goodsDescriptionContainer.getLeft(), 
					descriptionContainerOriginalTop+container.getScrollY(),
					goodsDescriptionContainer.getRight(),
					descriptionContainerOriginalTop+container.getScrollY()+ObjectConvertor.dip2px(container.getContext(), 50));
		}*/
	}
	
	
	/**
	 * 当前评论页
	 */
	int currentCommentsPageIndex=1;
	int pageSize = 20;
	int pageCount=0;
	/**
	 * 渲染评论
	 * @param page 显示哪一页
	 * @param blog_id 哪一条blog的评论
	 */
	private void _renderComments(int page,long blog_id){
		this._unloadComments();
		ServiceFactory.createService(CommentService.class).getCommentListByPageAndBlogId(UrlConfig.COMMENTS, page, blog_id, new CommentService.GetCommentListByPageAndBlogIdCallback() {
			@Override
			public void onComplete(int status, String msg, ArrayList<Comment> comments,int replyNo) {
				if(Service.noticeExceptSuccess(container.getContext(), status, msg))return;
				_showComments(comments,replyNo);
			}
		});
	}
	/**
	 * 显示多条评论
	 * @param comments 多条评论数据列表
	 */
	private void _showComments(ArrayList<Comment> comments,int replyNo){
		for(int i=0,il=comments.size();i<il;i++){
			_showComment((LinearLayout)this.commentsContainer.getChildAt(i),comments.get(i));
		}
		
		if(replyNo % pageSize==0)pageCount = replyNo / pageSize;
		else pageCount = replyNo / pageSize + 1;
		
		if(currentCommentsPageIndex < pageCount){
			nextPageButton.setVisibility(View.VISIBLE);
		}else{
			nextPageButton.setVisibility(View.GONE);
		}
		if(currentCommentsPageIndex > 1){
			prePageButton.setVisibility(View.VISIBLE);
		}else{
			prePageButton.setVisibility(View.GONE);
		}
	}
	/**
	 * 显示一条评论
	 * @param container 评论的容器
	 * @param comment 评论数据
	 */
	private void _showComment(LinearLayout container,Comment comment){
		ImageView imgView = (ImageView)container.findViewById(R.id.head);
		String headUrl = comment.getUser().getHead();
		headUrl = headUrl==null||headUrl.equals("")?UrlConfig.HEAD_NONE:headUrl;
		LightBitmapLoader.instance().load(headUrl, imgView);
		
		TextView nickNameView = (TextView)container.findViewById(R.id.nick_name);
		nickNameView.setText(comment.getUser().getNick_name());
		
		TextView commentContentView = (TextView)container.findViewById(R.id.comment_content);
		commentContentView.setText(comment.getComment());
		
		container.setVisibility(View.VISIBLE);
	}
	/**
	 * 去掉所有的评论
	 */
	private void _unloadComments(){
		for(int i=0,il=this.commentsContainer.getChildCount();i<il;i++){
			LinearLayout container = (LinearLayout)this.commentsContainer.getChildAt(i);
			ImageView imgView = (ImageView)container.findViewById(R.id.head);
			TextView nickNameView = (TextView)container.findViewById(R.id.nick_name);
			TextView commentContentView = (TextView)container.findViewById(R.id.comment_content);
			
			if(imgView!=null) imgView.setImageBitmap(null);
			if(nickNameView!=null) nickNameView.setText("");
			if(commentContentView!=null) commentContentView.setText("");
			container.setVisibility(View.GONE);
		}
	}
	private int commentsCurrentPageIndex = 1;/*评论当前页*/
	private int totalPageCount = 0;
	/**
	 * 评论上一页按钮点击
	 * @param v
	 */
	public void onPrePageButtonClick(View v){
		if(commentsCurrentPageIndex <= 1) return;
		commentsCurrentPageIndex--;
		_renderComments(commentsCurrentPageIndex,blog.getId());
		
		if(commentsCurrentPageIndex <= 1) this.prePageButton.setVisibility(View.GONE);
		else this.prePageButton.setVisibility(View.VISIBLE);
	}
	/**
	 * 评论下一页按钮点击
	 * @param v
	 */
	public void onNextPageButtonClick(View v){
		if(commentsCurrentPageIndex >= totalPageCount) return;
		commentsCurrentPageIndex++;
		_renderComments(commentsCurrentPageIndex,blog.getId());
		
		if(commentsCurrentPageIndex >= totalPageCount) this.nextPageButton.setVisibility(View.GONE);
		else this.nextPageButton.setVisibility(View.VISIBLE);
	}
	
	public int getMeasureHeight(){
		return this.container.getMeasuredHeight();
	}
	public void setMinimumHeight(int height){
		this.container.setMinimumHeight(height);
	}
}
