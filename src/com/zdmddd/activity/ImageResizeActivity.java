package com.zdmddd.activity;

import zl.android.http.image_load.BitmapLoader;
import zl.android.movie.ZLMovie;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zdmddd.R;

class MulitPointTouchListener implements OnTouchListener { 
    Matrix matrix = new Matrix(); 
    Matrix savedMatrix = new Matrix(); 

    static final int NONE = 0; 
    static final int DRAG = 1; 
    static final int ZOOM = 2; 
    int mode = NONE; 

    PointF start = new PointF(); 
    PointF mid = new PointF(); 
    float oldDist = 1f; 

    @Override 
    public boolean onTouch(View v, MotionEvent event) { 

            ImageView view = (ImageView) v; 

            switch (event.getAction() & MotionEvent.ACTION_MASK) { 
            case MotionEvent.ACTION_DOWN: 

                    matrix.set(view.getImageMatrix()); 
                    savedMatrix.set(matrix); 
                    start.set(event.getX(), event.getY()); 
                    mode = DRAG; 

                   
                    break; 
            case MotionEvent.ACTION_POINTER_DOWN: 
                    oldDist = spacing(event); 
                    if (oldDist > 10f) { 
                            savedMatrix.set(matrix); 
                            midPoint(mid, event); 
                            mode = ZOOM; 
                    } 
                    break; 
            case MotionEvent.ACTION_UP: 
            case MotionEvent.ACTION_POINTER_UP: 
                    mode = NONE; 

                    break; 
            case MotionEvent.ACTION_MOVE: 
                    if (mode == DRAG) { 
                            matrix.set(savedMatrix); 
                            matrix.postTranslate(event.getX() - start.x, event.getY() 
                                            - start.y); 
                    } else if (mode == ZOOM) { 
                            float newDist = spacing(event); 
                            if (newDist > 10f) { 
                                    matrix.set(savedMatrix); 
                                    float scale = newDist / oldDist; 
                                    matrix.postScale(scale, scale, mid.x, mid.y); 
                            } 
                    } 
                    break; 
            } 

            view.setImageMatrix(matrix); 
            return true; 
    } 

    
   
    private float spacing(MotionEvent event) { 
            float x = event.getX(0) - event.getX(1); 
            float y = event.getY(0) - event.getY(1); 
            return FloatMath.sqrt(x * x + y * y); 
    } 

   
    private void midPoint(PointF point, MotionEvent event) { 
            float x = event.getX(0) + event.getX(1); 
            float y = event.getY(0) + event.getY(1); 
            point.set(x / 2, y / 2); 
    } 
} 
public class ImageResizeActivity extends BaseActivity{
	ImageView imgView;
	ProgressBar loading;
	TextView titleText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.image_resize_activity);
		
		this.titleText.setText("图片详情");
		Bundle bundle = this.getIntent().getExtras();
		new BitmapLoader(bundle.getString("bitmap"), imgView).setOnImageLoadStart(new BitmapLoader.OnImageLoadStart() {
			@Override
			public void onImageLoadStart(String url, ImageView imgView) {
				loading.setVisibility(View.VISIBLE);
			}
		}).setOnImageLoadedFailed(new BitmapLoader.OnImageLoadedFailed() {
			@Override
			public void onImageLoadedFailed(String url, ImageView imgView,
					String errorMsg, Exception e) {
				loading.setVisibility(View.GONE);
				Toast.makeText(ImageResizeActivity.this, "下载图片失败！请检查您的网络！", Toast.LENGTH_LONG).show();
			}
		}).setOnImageLoadedSuccess(new BitmapLoader.OnImageLoadedSuccess() {
			@Override
			public void onImageLoadedSuccess(String url, Bitmap bitmap,
					ZLMovie movie, int imageType, ImageView imgView) {
				// TODO Auto-generated method stub
				loading.setVisibility(View.GONE);
				imgView.setOnTouchListener(new MulitPointTouchListener());
			}
		}).execute(new String[]{});
	}
	@Override
	protected void _findViews() {
		// TODO Auto-generated method stub
		imgView = (ImageView)findViewById(R.id.blog_image);
		loading = (ProgressBar)findViewById(R.id.loading);
		titleText = (TextView)findViewById(R.id.title_text);
	}
	@Override
	protected void _bindEvent() {
		// TODO Auto-generated method stub
		
	}
	public void onBackButtonClick(View v){
		this.finish();
	}
	
}
