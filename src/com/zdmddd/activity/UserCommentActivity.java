package com.zdmddd.activity;

import zl.android.http.ZLHttpParameters;

import com.zdmddd.R;
import com.zdmddd.config.UrlConfig;
import com.zdmddd.service.Service;
import com.zdmddd.service.ServiceFactory;
import com.zdmddd.service.UserService;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UserCommentActivity extends BaseActivity implements View.OnClickListener{
	EditText commentContent;
	Button commentBtn;
	Button cancelBtn;
	TextView titleText;
	
	private long uid;
	private long blog_id;
	
	public EditText getCommentContent(){
		return this.commentContent;
	}
	public long getUid(){
		return this.uid;
	}
	public long getBlog_id(){
		return this.blog_id;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_comment_activity);
		
		Bundle bundle = this.getIntent().getExtras();
		this.uid = bundle.getLong("uid");
		this.blog_id = bundle.getLong("blog_id");
		
		this.titleText.setText("评论");
	}
	@Override
	protected void _findViews() {
		this.titleText = (TextView)findViewById(R.id.title_text);
		this.commentContent = (EditText)findViewById(R.id.comment_content);
		this.commentBtn = (Button)findViewById(R.id.comment);
		this.cancelBtn = (Button)findViewById(R.id.cancel);
	}
	@Override
	protected void _bindEvent() {
		this.commentBtn.setOnClickListener(this);
		this.cancelBtn.setOnClickListener(this);
	}
	
	/*event start*/
	@Override
	public void onClick(View v) {
		if(v == this.commentBtn){
			_onCommentBtnClick(v);
		}else if(v == this.cancelBtn){
			finish();
		}
	}
	private void _onCommentBtnClick(View v){
		ZLHttpParameters ap = new ZLHttpParameters();
		ap.put("type", String.valueOf(6));
		ap.put("content", commentContent.getText().toString());
		ap.put("target_id", String.valueOf(this.blog_id));
		ap.put("target_user_id", String.valueOf(this.uid));
		ap.put("refer_id", String.valueOf(this.blog_id));
		ap.put("refer_user_id", String.valueOf(this.uid));
		ServiceFactory.createService(UserService.class).reply(UrlConfig.REPLY, ap, new UserService.ReplyCallback() {
			@Override
			public void onComplete(int status, String msg) {
				if(Service.noticeExceptSuccess(UserCommentActivity.this, status, msg)) return;
				Toast.makeText(UserCommentActivity.this, "评论成功！", Toast.LENGTH_SHORT).show();
				UserCommentActivity.this.finish();
			}
		});
	}
	/*event end*/
}
