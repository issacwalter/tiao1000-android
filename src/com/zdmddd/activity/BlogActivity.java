package com.zdmddd.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.zdmddd.R;

import zl.android.http.ZLHttpParameters;
import zl.android.http.ZLHttpService;
import zl.android.log.Logger;
import com.zdmddd.config.UrlConfig;
import com.zdmddd.helper.blog.Blogs;
import com.zdmddd.modal.Blog;
import com.zdmddd.modal.User;
import com.zdmddd.service.Service;
import com.zdmddd.service.ServiceFactory;
import com.zdmddd.service.UserService;
import com.zdmddd.view.blog.BlogContentView;
import com.zdmddd.view.plaza.Plaza;
import com.zdmddd.view.plaza.PlazaListView;
import com.zdmddd.view.plaza.PlazaView;

public final class BlogActivity extends BaseActivity implements View.OnClickListener{
	TextView textView;
	Button buyButton;
	Button commentBtn;
	Button likeBtn;
	Button forwardBtn;
	ViewFlipper viewFlipper;
/*	Button preBlogButton;
	Button nextBlogButton;*/
	
	private BlogContentSwitcher switcher;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blog_activity);
		
		long blogId = this.getIntent().getLongExtra("blog_id", 0);
		int plazaHash = this.getIntent().getIntExtra("plaza_hash", -1);
		int plazaKind = this.getIntent().getIntExtra("plaza_kind", Plaza.PLAZA_VIEW);
		Blog blog = Blogs.getInstance().getById(blogId);
		switcher = new BlogContentSwitcher(viewFlipper, plazaHash, null,plazaKind);
		switcher.start(blog);
	}
	
	protected void _findViews(){
		textView = (TextView)findViewById(R.id.blog_title);
		buyButton = (Button)findViewById(R.id.buy);
		commentBtn = (Button)findViewById(R.id.comment);
		likeBtn = (Button)findViewById(R.id.like);
		forwardBtn = (Button)findViewById(R.id.forward);
		viewFlipper = (ViewFlipper)findViewById(R.id.viewFlipper);
		/*this.preBlogButton = (Button)findViewById(R.id.pre_blog);
		this.nextBlogButton = (Button)findViewById(R.id.next_blog);*/
	}
	protected void _bindEvent(){
		this.buyButton.setOnClickListener(this);
		this.forwardBtn.setOnClickListener(this);
		this.likeBtn.setOnClickListener(this);
		this.commentBtn.setOnClickListener(this);
	/*	this.preBlogButton.setOnClickListener(this);
		this.nextBlogButton.setOnClickListener(this);*/
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		BlogContentView currentBlogContentView = this.switcher.getCurrentBlogContentView();
		if(currentBlogContentView!=null){
			currentBlogContentView.loadComments();
		}
	}
	
	/*event start*/
	@Override
	public void onClick(View v) {
		if(v==this.buyButton){
			_onBuyButtonClick(v);
		}else if(v==this.forwardBtn){
			_onForwardButtonClick(v);
		}else if(v==this.likeBtn){
			_onLikeButtonClick(v);
		}else if(v==this.commentBtn){
			_onCommentButtonClick(v);
		}
	}
	private void _onBuyButtonClick(View v){
		Intent intent = new Intent();
		intent.setClass(v.getContext(), TaoBaoBuyActivity.class);
		intent.putExtra("blog", switcher.getCurrentBlog().toBundle());
		this.startActivity(intent);
	}
	private void _onForwardButtonClick(View v){
		if(!_checkLogin()) return;
		Intent intent = new Intent();
		intent.setClass(v.getContext(), ForwardActivity.class);
		Blog blog = switcher.getCurrentBlog();
		intent.putExtras(blog.toBundle());
		this.startActivity(intent);
	}
	private void _onLikeButtonClick(View v){
		if(!_checkLogin()) return;
		ZLHttpParameters ap = new ZLHttpParameters();
		ap.put("blog_id", String.valueOf(switcher.getCurrentBlog().getId()));
		
		ServiceFactory.createService(UserService.class).likeIt(UrlConfig.LIKE_IT_URL, ap, new UserService.LikeItCallback() {
			@Override
			public void onComplete(int status, String msg) {
				if(msg!=null && msg.trim().equals("not login")){
					app.removeAttribute("self");
					app.removeInfoFromDb("self");
					ZLHttpService.removeCookie("token");
				}
				if(Service.noticeExceptSuccess(BlogActivity.this, status, msg))return;
				Toast.makeText(BlogActivity.this, "喜欢成功！", Toast.LENGTH_SHORT).show();
				switcher.getCurrentBlog().setLike_no(switcher.getCurrentBlog().getLike_no()+1);
			}
		});
	}
	private void _onCommentButtonClick(View v){
		if(!_checkLogin()) return;
		Intent intent = new Intent();
		intent.setClass(v.getContext(), UserCommentActivity.class);
		Bundle bundle = new Bundle();
		bundle.putLong("uid", switcher.getCurrentBlog().getUid());
		bundle.putLong("blog_id", switcher.getCurrentBlog().getId());
		intent.putExtras(bundle);
		this.startActivity(intent);
	}
	/*event end*/
	
	private boolean _checkLogin(){
		User self = null;
		try {
			self = User.parseFromJSON(app.getInfoFromDb("self"));
		} catch (Exception e) {
			Logger.error(e);
		}
		if(self == null){
			Intent intent = new Intent();
			intent.setClass(this, LoginActivity.class);
			this.startActivity(intent);
		}else{
			return true;
		}
		return false;
	}
}
final class BlogContentSwitcher{
	ViewFlipper viewFlipper;
	Button viewDetailButton;
	
	BlogContentView blogContentView0 = null;
	BlogContentView blogContentView1 = null;
	
	Blog currentBlog=null;
	int plazaViewId=-1;
	
	private Animation in_lefttoright;
	private Animation out_lefttoright;
	private Animation in_righttoleft;
	private Animation out_righttoleft;
	private GestureDetector detector;
	
	private int plazaKind;
	public BlogContentSwitcher(ViewFlipper viewFlipper,int plazaViewId,Button viewDetailButton,int plazaKind){
		this.viewFlipper = viewFlipper;
		this.plazaViewId = plazaViewId;
		this.viewDetailButton = viewDetailButton;
		this.plazaKind = plazaKind;
		
		if(this.viewDetailButton!=null){
			this.viewDetailButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(v.getContext(), TaoBaoBuyActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("taobao_url", currentBlog.getUrl());
					intent.putExtras(bundle);
					v.getContext().startActivity(intent);
				}
			});
		}
		
		detector = new GestureDetector(this.viewFlipper.getContext(),new MyOnGestureListener());
		in_lefttoright = AnimationUtils.loadAnimation(this.viewFlipper.getContext(), R.anim.enter_left_to_right);
		out_lefttoright = AnimationUtils.loadAnimation(this.viewFlipper.getContext(), R.anim.out_left_to_right);
		in_righttoleft = AnimationUtils.loadAnimation(this.viewFlipper.getContext(), R.anim.enter_right_to_left);
		out_righttoleft = AnimationUtils.loadAnimation(this.viewFlipper.getContext(), R.anim.out_right_to_left);
	}
	
	public Blog getCurrentBlog() {
		return this.currentBlog;
	}

	public void start(Blog blog){
		this.setCurrentBlog(blog);
		blogContentView0 = new BlogContentView(_createBlogContentContainer(true),detector);
		blogContentView0.setBlog(blog);
		
		progressDialog = new ProgressDialog(viewFlipper.getContext());
		progressDialog.setCancelable(false);
	}
	private ViewGroup _createBlogContentContainer(boolean isFirst){
		LayoutInflater inflater = LayoutInflater.from(viewFlipper.getContext());
		ViewGroup vg = (ViewGroup)inflater.inflate(R.layout.blog_content, viewFlipper,false);
		if(isFirst){
			viewFlipper.addView(vg, 0);
		}else{
			viewFlipper.addView(vg);
		}
		return vg;
	}
	public void setCurrentBlog(Blog blog){
		if(blog.getPrice()!=0 && viewDetailButton!=null){
			viewDetailButton.setText("￥"+blog.getPrice()+" | 查看详情");
			viewDetailButton.setVisibility(View.VISIBLE);
		}
		this.currentBlog = blog;
	}
	
	private ProgressDialog progressDialog;
	
	private void _showNextCallback(int status, String msg, Blog nextBlog){
		if(Service.noticeExceptSuccess(viewFlipper.getContext(), status, msg)) {
			progressDialog.dismiss();
			return;
		}
		
		if(nextBlog==null){
			progressDialog.dismiss();
			Toast.makeText(viewFlipper.getContext(), "后面没有了", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(viewFlipper.getCurrentView()==blogContentView0.getContainer()){
			if(blogContentView1!=null) blogContentView1.unloadImage();
			if(blogContentView1==null) blogContentView1 = new BlogContentView(_createBlogContentContainer(false),detector);
			blogContentView1.setBlog(nextBlog);
			
			progressDialog.dismiss();
			setCurrentBlog(nextBlog);
			viewFlipper.showNext();
		}else if(viewFlipper.getCurrentView()==blogContentView1.getContainer()){
			if(blogContentView0!=null) blogContentView0.unloadImage();
			if(blogContentView0==null) blogContentView0 = new BlogContentView(_createBlogContentContainer(true),detector);
			blogContentView0.setBlog(nextBlog);
			
			progressDialog.dismiss();
			setCurrentBlog(nextBlog);
			viewFlipper.showPrevious();
		}
	}
	public void showNext(){
		progressDialog.show();
		long blogId = currentBlog.getId();
		if(this.plazaKind == Plaza.PLAZA_LIST_VIEW){
			PlazaListView.getPlazaViewById(plazaViewId).getNextBlogById(blogId, new PlazaListView.GetNextBlogByIdCallback() {
				@Override
				public void onComplete(int status, String msg, Blog blog) {
					_showNextCallback(status,msg,blog);
				}
			});
		}else if(this.plazaKind == Plaza.PLAZA_VIEW){
			PlazaView.getPlazaViewById(plazaViewId).getNextBlogById(blogId, new PlazaView.GetNextBlogByIdCallback() {
				@Override
				public void onComplete(int status, String msg, Blog blog) {
					_showNextCallback(status,msg,blog);
				}
			});
		}
	}
	
	public void showPrevious(){
		Blog previousBlog = null;
		if(this.plazaKind == Plaza.PLAZA_VIEW){
			previousBlog = PlazaView.getPlazaViewById(plazaViewId).getPreviousBlogByIndex(currentBlog.getId());
		}else if(this.plazaKind == Plaza.PLAZA_LIST_VIEW){
			previousBlog = PlazaListView.getPlazaViewById(plazaViewId).getPreviousBlogByIndex(currentBlog.getId());
		}else{
			Toast.makeText(viewFlipper.getContext(), "前面没有了", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(previousBlog!=null){
			if(viewFlipper.getCurrentView()==blogContentView0.getContainer()){
				if(blogContentView1!=null) blogContentView1.unloadImage();
				if(blogContentView1==null) blogContentView1 = new BlogContentView(_createBlogContentContainer(false),detector);
				blogContentView1.setBlog(previousBlog);
				
				progressDialog.dismiss();
				setCurrentBlog(previousBlog);
				viewFlipper.showNext();
			}else if(viewFlipper.getCurrentView()==blogContentView1.getContainer()){
				if(blogContentView0!=null) blogContentView0.unloadImage();
				if(blogContentView0==null) blogContentView0 = new BlogContentView(_createBlogContentContainer(true),detector);
				blogContentView0.setBlog(previousBlog);
				
				progressDialog.dismiss();
				setCurrentBlog(previousBlog);
				viewFlipper.showPrevious();
			}
		}else{
			Toast.makeText(viewFlipper.getContext(), "前面没有了", Toast.LENGTH_SHORT).show();
		}
	}
	public BlogContentView getCurrentBlogContentView(){
		if(blogContentView0!=null && viewFlipper.getCurrentView()==blogContentView0.getContainer()){
			return blogContentView0;
		}else if(blogContentView1!=null && viewFlipper.getCurrentView()==blogContentView1.getContainer()){
			return blogContentView1;
		}else{
			return null;
		}
	}
	private final class MyOnGestureListener implements android.view.GestureDetector.OnGestureListener{
		
		public MyOnGestureListener(){
		}
		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			if(blogContentView0!=null && viewFlipper.getCurrentView()==blogContentView0.getContainer()){
				blogContentView0.checkDescriptionContainerPos();
			}else if(blogContentView1!=null && viewFlipper.getCurrentView()==blogContentView1.getContainer()){
				blogContentView1.checkDescriptionContainerPos();
			}
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			if(blogContentView0!=null && viewFlipper.getCurrentView()==blogContentView0.getContainer()){
				blogContentView0.checkDescriptionContainerPos();
			}else if(blogContentView1!=null && viewFlipper.getCurrentView()==blogContentView1.getContainer()){
				blogContentView1.checkDescriptionContainerPos();
			}
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			
			if(blogContentView0!=null && viewFlipper.getCurrentView()==blogContentView0.getContainer()){
				blogContentView0.checkDescriptionContainerPos();
			}else if(blogContentView1!=null && viewFlipper.getCurrentView()==blogContentView1.getContainer()){
				blogContentView1.checkDescriptionContainerPos();
			}
			Logger.info("velocityX:"+velocityX+",velocityY:"+velocityY+",distanceX:"+(e1.getX()-e2.getX())+",distanceY:"+(e1.getY()-e2.getY()));
			
			float distanceY = e1.getY()-e2.getY();
			if(distanceY<0) distanceY = -distanceY;
			if(distanceY>500) return false;
			
			float distanceX = e1.getX()-e2.getX();
			if(distanceX<0) distanceX = -distanceX;
			if(distanceX<200) return false;
			
			if(e1.getX()>e2.getX()){
				viewFlipper.setInAnimation(in_righttoleft);
				viewFlipper.setOutAnimation(out_righttoleft);
				showNext();
			}else if(e1.getX()<e2.getX()){
				viewFlipper.setInAnimation(in_lefttoright);
				viewFlipper.setOutAnimation(out_lefttoright);
				showPrevious();
			}
			return false;
		}
	}
}
