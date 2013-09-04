package com.zdmddd.activity;

import java.io.File;

import zl.android.http.ZLHttpParameters;
import zl.android.utils.ObjectConvertor;
import zl.android.view.imageview.MemorySafeImageView;

import com.zdmddd.R;
import com.zdmddd.config.UrlConfig;
import com.zdmddd.modal.Group;
import com.zdmddd.service.BlogService;
import com.zdmddd.service.Service;
import com.zdmddd.service.ServiceFactory;
import com.zdmddd.view.GroupListView;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;


public class PhotoUploadActivity extends BaseActivity implements View.OnClickListener,OnItemClickListener{
	MemorySafeImageView photoImageView;
	TextView titleText;
	Button cancelButton;
	Button publishButton;
	EditText content;
	
	Button chooseGroupButton;
	
	GroupListView groupListView;
	Group currentGroup;
	
	String imgPath;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.upload_activity);
		
		imgPath = this.getIntent().getStringExtra("img_path");
		Bitmap bmp = BitmapFactory.decodeFile(imgPath);
		this._showBitmap(bmp, this.photoImageView, (LinearLayout.LayoutParams)this.photoImageView.getLayoutParams());
		this.titleText.setText("发布");
		
		this.groupListView.setOnGroupLoadComplete(new GroupListView.OnGroupLoadComplete() {
			@Override
			public void onComplete() {
				// TODO Auto-generated method stub
				groupListView.performItemClick(null, 0, 0);
			}
		});
		this.groupListView.load();
	}
	@Override
	protected void _findViews() {
		// TODO Auto-generated method stub
		this.photoImageView = (MemorySafeImageView)this.findViewById(R.id.photo);
		this.titleText = (TextView)findViewById(R.id.title_text);
		this.cancelButton = (Button)findViewById(R.id.cancel);
		this.publishButton = (Button)findViewById(R.id.publish);
		this.content = (EditText)findViewById(R.id.content);
		this.chooseGroupButton = (Button)findViewById(R.id.choose_group);
		this.groupListView = (GroupListView)findViewById(R.id.groupListView);
	}

	@Override
	protected void _bindEvent() {
		// TODO Auto-generated method stub
		this.cancelButton.setOnClickListener(this);
		this.publishButton.setOnClickListener(this);
		this.chooseGroupButton.setOnClickListener(this);
		this.groupListView.setOnItemClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==cancelButton){
			this.finish();
		}else if(v==publishButton){
			_publish();
		}else if(v==this.chooseGroupButton){
			if(this.groupListView.getVisibility() == View.VISIBLE){
				this.groupListView.setVisibility(View.GONE);
			}else{
				this.groupListView.setVisibility(View.VISIBLE);
			}
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		currentGroup = this.groupListView.getGroupByIndex(position);
		if(currentGroup.getType()==2){
			this.chooseGroupButton.setText("问答咨询");
		}else{
			this.chooseGroupButton.setText(currentGroup.getName().equals("")?"默认组":currentGroup.getName());
		}
		this.groupListView.setVisibility(View.GONE);
	}
	
	
	private void _publish(){
		final String content = this.content.getText().toString();
		if(content.trim().equals("")){
			Toast.makeText(this, "请填写内容！", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(currentGroup==null){
			Toast.makeText(this, "请选择发布的专辑！", Toast.LENGTH_SHORT).show();
			return;
		}
		
		final ProgressDialog progressDlg = ProgressDialog.show(this, "加载中", "正在上传图片...",false,false);
		_uploadImage(this.imgPath, new BlogService.UploadImageCallback() {
			@Override
			public void onComplete(int status, String msg, String href) {
				// TODO Auto-generated method stub
				if(!Service.noticeExceptSuccess(PhotoUploadActivity.this, status, msg)){
					ZLHttpParameters params = new ZLHttpParameters();
					params.put("type", "1");
					params.put("url", UrlConfig.HOST);
					params.put("content", content);
					params.put("qid", currentGroup.getId());
					params.put("images", href);
					if(currentGroup.getType()==2){
						params.put("is_q", "1");
					}
					progressDlg.setMessage("正在发布...");
					ServiceFactory.createService(BlogService.class).publish(UrlConfig.AJAX_SHARE, params, new BlogService.PublishCallback() {
						@Override
						public void onComplete(int status, String msg) {
							if(!Service.noticeExceptSuccess(PhotoUploadActivity.this, status, msg)){
								PhotoUploadActivity.this.finish();
								Toast.makeText(PhotoUploadActivity.this, "发布成功！", Toast.LENGTH_SHORT).show();
							}
							progressDlg.dismiss();
						}
					});
				}else{
					progressDlg.dismiss();
				}
			}
		});
	}
	
	private void _uploadImage(String path,BlogService.UploadImageCallback cb){
		ServiceFactory.createService(BlogService.class).uploadImage(UrlConfig.CUTOOL_IMAGE_UPLOAD, new File(path), cb);
	}
	
	private void _showBitmap(Bitmap bmp,ImageView imgView,LinearLayout.LayoutParams params){
		int maxWidth = ObjectConvertor.dip2px(this, 80);
		params.width = maxWidth;
		params.height = (int)(bmp.getHeight() / (float)bmp.getWidth() * maxWidth);
		imgView.setImageBitmap(bmp);
	}

}
