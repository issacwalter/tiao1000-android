package com.zdmddd.view.event;


import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

class CheckScrollStopHandler extends Handler{
	public int preY=-1;
	public int checkDelay=50;
	
	ScrollView scrollView;
	ScrollViewStopDetector detector=null;
	
	public CheckScrollStopHandler(ScrollView scrollView,ScrollViewStopDetector detector){
		this.scrollView = scrollView;
		this.detector = detector;
	}
	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		int scrollTop = scrollView.getScrollY();
		int action=msg.what;
		if(preY!=scrollTop){
			this.sendMessageDelayed(this.obtainMessage(action),checkDelay);
			preY = scrollTop;
		}else{
			if(detector.getOnScrollStop()!=null)
				detector.getOnScrollStop().onScrollStop(scrollView,action);
			preY = -1;
		}
	}
}
public class ScrollViewStopDetector {
	CheckScrollStopHandler handler = null;
	OnScrollStop onScrollStop=null;
	
	int action = OnScrollStop.SCROLL_NO;
	int preY=-1;
	public void setOnScrollStop(OnScrollStop onScrollStop){
		this.onScrollStop = onScrollStop;
	}
	public OnScrollStop getOnScrollStop(){
		return this.onScrollStop;
	}
	public void touch(View v, MotionEvent event){
		if(handler==null){
			handler = new CheckScrollStopHandler((ScrollView)v,this);
		}
		if(event.getAction() == MotionEvent.ACTION_UP){
			if(action == OnScrollStop.SCROLL_NO && preY != -1){
				if(preY-v.getScrollY()>0){
					action = OnScrollStop.SCROLL_UP;
				}else if(preY-v.getScrollY()<0){
					action = OnScrollStop.SCROLL_DOWN;
				}
				handler.sendMessageDelayed(handler.obtainMessage(action),handler.checkDelay);
				preY=-1;
				action = OnScrollStop.SCROLL_NO;
			}
		}else if(event.getAction() == MotionEvent.ACTION_MOVE){
			handler.preY = v.getScrollY();
			preY = v.getScrollY();
		}
	}
	
	public interface OnScrollStop{
		public static final int SCROLL_DOWN=-1;/*向下滚动*/
		public static final int SCROLL_UP=1;/*向上滚动*/
		public static final int SCROLL_NO=3;/*无法判断*/
		void onScrollStop(View v,int action);
	}
}
