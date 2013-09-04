package com.zdmddd.view.effect;


import zl.android.log.Logger;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class PageSwitcher extends ViewGroup {
	private float mTouchX;
    private float mLastMotionX;
    private int mActivePointerId = -1;
    private Scroller mScroller;
	public PageSwitcher(Context context, AttributeSet attrs) {
		super(context, attrs);
		mScroller = new Scroller(context);
	}

	public PageSwitcher(Context context) {
		super(context);
		mScroller = new Scroller(context);
	}

	@Override
    protected void dispatchDraw(Canvas canvas) {
        final long drawingTime = getDrawingTime();
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            drawChild(canvas, getChildAt(i), drawingTime);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode != MeasureSpec.EXACTLY) {
            throw new IllegalStateException("Workspace can only be used in EXACTLY mode.");
        }

        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode != MeasureSpec.EXACTLY) {
            throw new IllegalStateException("Workspace can only be used in EXACTLY mode.");
        }

        // The children are given the same width and height as the workspace
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft = 0;
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                final int childWidth = child.getMeasuredWidth();
                child.layout(childLeft, 0, childLeft + childWidth, child.getMeasuredHeight());
                childLeft += childWidth;
            }
        }

    }

    
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mTouchX = mScroller.getCurrX();
            int scrollY = mScroller.getCurrY();
            scrollTo((int)mTouchX, scrollY);
            postInvalidate();
        }
    }

    public int getShowIndex(){
    	Logger.info("current show index:"+mScroller.getCurrX()/getWidth());
    	return mScroller.getCurrX()/getWidth();
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
    	this.onTouchEvent(ev);
    	View v = this.getChildAt(getShowIndex());
    	v.onTouchEvent(ev);
    	
    	return super.onInterceptTouchEvent(ev);
    }
    
    
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_DOWN:
        	if(this.onSwitchTouchDownListener!=null) onSwitchTouchDownListener.onSwitchTouchDown();
            mLastMotionX = ev.getX();
            mActivePointerId = ev.getPointerId(0);
            break;
        case MotionEvent.ACTION_MOVE:
            final int pointerIndex = ev.findPointerIndex(mActivePointerId);
            try {
                final float x = ev.getX(pointerIndex);
                final float deltaX = mLastMotionX - x;
                mLastMotionX = x;
                if (deltaX > 0) {
                    scrollBy((int)deltaX, 0);
                } else if (deltaX < 0) {
                    scrollBy((int)deltaX, 0);
                } else {
                    awakenScrollBars();
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            	Logger.error(e);
            }
            break;
        case MotionEvent.ACTION_UP:
            int screenWidth = getWidth();
            int whichView = (getScrollX() + (screenWidth / 2)) / screenWidth;
            whichView = Math.max(0, Math.min(whichView, getChildCount() - 1));
            int newX = whichView * getWidth();
            int delta = newX - getScrollX();
            mScroller.startScroll(getScrollX(), 0, delta, 0, Math.abs(delta) * 2);
            invalidate();
            
            if(delta>0){
            	if(onSwitchToNextLitener!=null)onSwitchToNextLitener.onSwitchToNext();
            }else if(delta<0){
            	if(onSwitchToPreviousListener!=null)onSwitchToPreviousListener.onSwitchToPrevious();
            }
            break;
        case MotionEvent.ACTION_CANCEL:
            break;
        case MotionEvent.ACTION_POINTER_UP:
            break;
        }

        return true;
    }
    
    
    private OnSwitchToNextListener onSwitchToNextLitener=null;
    private OnSwitchToPreviousListener onSwitchToPreviousListener=null;
    private OnSwitchTouchDownListener onSwitchTouchDownListener=null;
    public void setOnSwitchToNextLitener(
			OnSwitchToNextListener onSwitchToNextLitener) {
		this.onSwitchToNextLitener = onSwitchToNextLitener;
	}
	public void setOnSwitchToPreviousListener(
			OnSwitchToPreviousListener onSwitchToPreviousListener) {
		this.onSwitchToPreviousListener = onSwitchToPreviousListener;
	}
	public void setOnSwitchTouchDownListener(OnSwitchTouchDownListener onSwitchTouchDownListener){
		this.onSwitchTouchDownListener = onSwitchTouchDownListener;
	}
	
	public interface OnSwitchToNextListener{
    	void onSwitchToNext();
    }
    public interface OnSwitchToPreviousListener{
    	void onSwitchToPrevious();
    }
    public interface OnSwitchTouchDownListener{
    	void onSwitchTouchDown();
    }
}
