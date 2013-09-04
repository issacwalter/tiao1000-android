package com.zdmddd.activity;

import com.zdmddd.R;
import com.zdmddd.config.UrlConfig;
import com.zdmddd.modal.Blog;
import com.zdmddd.service.BlogService;
import com.zdmddd.service.Service;
import com.zdmddd.service.ServiceFactory;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

class TaoBaoBuyActivityEventManage extends WebViewClient{
	private TaoBaoBuyActivity taoBaoBuyActivity;
	public TaoBaoBuyActivityEventManage(TaoBaoBuyActivity taoBaoBuyActivity){
		this.taoBaoBuyActivity = taoBaoBuyActivity;
	}
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		return super.shouldOverrideUrlLoading(view, url);
	}
	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		super.onPageStarted(view, url, favicon);
		taoBaoBuyActivity.getLoading().setVisibility(View.VISIBLE);
	}
	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);
		taoBaoBuyActivity.getLoading().setVisibility(View.GONE);
	}
}


public class TaoBaoBuyActivity extends BaseActivity{
	WebView taobaoBuy;
	TextView loading;
	TextView titleText;
	
	Blog blog;
	
	private TaoBaoBuyActivityEventManage eventManage;
	
	public TextView getLoading(){
		return this.loading;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.taobao_buy_activity);
		
		
		this.titleText.setText("查看详情");
		
		blog = Blog.parseBlogFromBundle(this.getIntent().getBundleExtra("blog"));
		this.taobaoBuy.getSettings().setJavaScriptEnabled(true);
		
		String url = blog.getUrl();
		if(url==null || url.equals("")){
			if(blog.getPid()==0){
				ServiceFactory.createService(BlogService.class).getBlogById(UrlConfig.BLOG_AJAX_GET, blog.getId(), new BlogService.GetBlogByIdCallback() {
					@Override
					public void onComplete(int status, String msg, Blog blog) {
						if(Service.noticeExceptSuccess(TaoBaoBuyActivity.this, status, msg)){
							finish();
						}else{
							if(blog.getPid()==0){
								Toast.makeText(TaoBaoBuyActivity.this, "抱歉，该商品无法跳转到淘宝页面！", Toast.LENGTH_SHORT).show();
								finish();
							}else{
								taobaoBuy.loadUrl("http://item.taobao.com/item.htm?id="+blog.getPid());
							}
						}
					}
				});
			}else{
				url = "http://item.taobao.com/item.htm?id="+blog.getPid();
				this.taobaoBuy.loadUrl(url);
			}
		}else{
			this.taobaoBuy.loadUrl(url);
		}
	}
	@Override
	protected void _findViews() {
		taobaoBuy = (WebView)findViewById(R.id.taobao_buy);
		loading = (TextView)findViewById(R.id.loading);
		this.titleText = (TextView)findViewById(R.id.title_text);
	}
	@Override
	protected void _bindEvent() {
		eventManage = new TaoBaoBuyActivityEventManage(this);
		this.taobaoBuy.setWebViewClient(eventManage);
	}
}
