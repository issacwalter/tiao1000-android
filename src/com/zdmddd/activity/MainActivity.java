package com.zdmddd.activity;


import java.io.File;

import zl.android.local.sdcard.ZLLocalSDCard;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zdmddd.R;
import com.zdmddd.application.Tiao1000Initializer;
import com.zdmddd.view.MenuView;
import com.zdmddd.view.dialog.SplashDialog;
import com.zdmddd.view.page.AboutPageView;
import com.zdmddd.view.page.FavorPageView;
import com.zdmddd.view.page.FirstPageView;
import com.zdmddd.view.page.HotPageView;
import com.zdmddd.view.page.LikePageView;
import com.zdmddd.view.page.PageView;

public final class MainActivity extends BaseActivity implements View.OnClickListener,MenuView.MenuButtonClickListener{
	Button searchButton;
	Button showMenuButton;
	TextView titleText;
	ImageView up_downView;
	MenuView menuView;
	
	private FirstPageView firstPageView = null;
	private LikePageView likePageView = null;
	private FavorPageView favorPageView = null;
	private HotPageView hotPageView = null;
	private AboutPageView aboutPageView = null;
	
	private LinearLayout pageViewContainer = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main_activity);
		
		final SplashDialog dlg = new SplashDialog();
		dlg.show(this);
		new Tiao1000Initializer().initialize(app, new Tiao1000Initializer.OnInitializeComplete() {
			@Override
			public void onComplete() {
				firstPageView.setTitleText(titleText);
				firstPageView.startLoad();
				dlg.close();
			}
		});
		
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		app.destroy();
	}
	
	
	protected void _findViews(){
		searchButton = (Button)findViewById(R.id.search);
		titleText = (TextView)findViewById(R.id.title);
		this.up_downView = (ImageView)findViewById(R.id.up_down);
		
		this.firstPageView = (FirstPageView)findViewById(R.id.first_page);
		this.likePageView = (LikePageView)findViewById(R.id.like_page);
		this.favorPageView = (FavorPageView)findViewById(R.id.favor_page);
		this.hotPageView = (HotPageView)findViewById(R.id.hot_page);
		this.aboutPageView = (AboutPageView)findViewById(R.id.about_page);
		
		this.menuView = (MenuView)findViewById(R.id.menu);
		this.showMenuButton = (Button)findViewById(R.id.show_menu);
		this.pageViewContainer = (LinearLayout)findViewById(R.id.page_view_container);
	}
	protected void _bindEvent(){
		searchButton.setOnClickListener(this);
		this.showMenuButton.setOnClickListener(this);
		this.menuView.setClickListener(this);
	}
	
	/*event start*/
	@Override
	public void onClick(View v) {
		if(v == this.showMenuButton){
			if(menuView.getVisibility() == View.VISIBLE){
				this.menuView.slideOut();
			}else{
				this.menuView.slideIn();
			}
		}else if(v == this.searchButton){
			_onSearchButtonClick(v);
		}
	}
	private void _onPhotoButtonClick(){
		if(!ZLLocalSDCard.instance().hasSDCard()){
			Toast.makeText(MainActivity.this,"请插入SD卡", Toast.LENGTH_LONG).show();
			return;
		}
		if(!isUserInfoCached()){
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), LoginActivity.class);
			MainActivity.this.startActivity(intent);
			return;
		}
		
        String[] choices={"拍摄照片","从相册里选择"};
        AlertDialog dialog = new AlertDialog.Builder(this)  
                 .setTitle("选择照片")  
                 .setItems(choices, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if(which==0){
							Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				        	intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(ZLLocalSDCard.instance().getRealPath("/tiao1000/tmp.jpg"))));
				        	startActivityForResult(intent, 3);
						}else{
							Intent intent = new Intent();
							intent.setType("image/*");
							intent.setAction(Intent.ACTION_GET_CONTENT);  
							startActivityForResult(intent, 5); 
						}
						dialog.dismiss();
					}
				}).create();  
        dialog.show(); 
	}
	private void _onSearchButtonClick(View v){
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), SearchActivity.class);
		startActivity(intent);
	}
	/*event end*/
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==3){
			/*photo*/
			if(resultCode!=-1) return;
			Intent intent = new Intent();
			intent.setClass(this, PhotoUploadActivity.class);
			intent.putExtra("img_path", ZLLocalSDCard.instance().getRealPath("/tiao1000/tmp.jpg"));
			this.startActivity(intent);
		}else if(requestCode == 5){
			/*本地选择图片*/
			if(data==null) return;
            Uri uri = data.getData();  
            try {  
                String[] pojo = {MediaStore.Images.Media.DATA};  
                Cursor cursor = getContentResolver().query(uri, pojo, null, null,null);
                if(cursor!=null){  
                    int colunm_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);  
                    cursor.moveToFirst();  
                    String path = cursor.getString(colunm_index); 
                    if(path.endsWith("jpg")||path.endsWith("png")){  
                    	Intent intent = new Intent();
            			intent.setClass(this, PhotoUploadActivity.class);
            			intent.putExtra("img_path", path);
            			this.startActivity(intent);
                    }else{
                    	Toast.makeText(MainActivity.this, "错误的图片格式！", Toast.LENGTH_SHORT).show();
                    }
                }else{
                	Toast.makeText(MainActivity.this, "无法选择图片！", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {  
            	Toast.makeText(MainActivity.this, "出现错误啦，无法选择图片！"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }  
		}
	}
	
	
	
	private void _hideAllPageView(){
		for(int i=0,il=this.pageViewContainer.getChildCount();i<il;i++){
			this.pageViewContainer.getChildAt(i).setVisibility(View.GONE);
		}
	}
	private boolean _login(){
		if(!this.isUserInfoCached()){
			Intent intent = new Intent();
			intent.setClass(this.getApplicationContext(), LoginActivity.class);
			this.startActivity(intent);
			menuView.slideOut();
			return false;
		}else{
			return true;
		}
	}
	private void _switchUpDown(int btnIndex){
		if(btnIndex == 0){
			this.up_downView.setVisibility(View.VISIBLE);
		}else{
			this.up_downView.setVisibility(View.GONE);
		}
	}
	@Override
	public void onClick(View v, int btnIndex) {
		// TODO Auto-generated method stub
		switch(btnIndex){
		case 1://我喜欢的
			if(!_login()) return;
		case 2://我关注的
			if(!_login()) return;
		case 3://我分享的
			if(!_login()) return;
		case 0://值得买的东东
			;
		case 4://发现-热门
			;
		case 5://发现-分类
			{_hideAllPageView();
			PageView pageView = (PageView)this.pageViewContainer.getChildAt(btnIndex);
			pageView.setVisibility(View.VISIBLE);
			pageView.setTitleText(titleText);
			pageView.startLoad();
			break;}
		case 6://照相
			_onPhotoButtonClick();
			menuView.slideOut();
			break;
		case 7://搜索
			_onSearchButtonClick(v);
			menuView.slideOut();
			break;
		case 8://登录
			{Intent intent = new Intent();
			intent.setClass(this.getApplicationContext(), LoginActivity.class);
			this.startActivity(intent);
			menuView.slideOut();
			break;}
		case 9://注册
			{Intent intent = new Intent();
			intent.setClass(this.getApplicationContext(), RegisteActivity.class);
			this.startActivity(intent);
			menuView.slideOut();
			break;}
		case 10://关于
			_hideAllPageView();
			this.aboutPageView.setVisibility(View.VISIBLE);
			this.aboutPageView.setTitleText(titleText);
			this.aboutPageView.startLoad();
			break;
		case 11://退出
			this.userExit();
			break;
		}
		
		menuView.slideOut();
		_switchUpDown(btnIndex);
	}
}
