package com.huassit.imenu.android.view;

import com.huassit.imenu.android.ui.DynamicDetailActivity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class InnerListView extends ListView {

	private Context context;
	public InnerListView(Context context) {
		super(context);
	}

	public InnerListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context =context;

	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			((DynamicDetailActivity)context).setParentScrollAble(false);
			// 当手指触到listview的时候，
			// 让父ScrollView交出ontouch权限，也就是让父scrollview
			// 停住不能滚动
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			((DynamicDetailActivity)context).setParentScrollAble(true);
			// 当手指松开时，让父ScrollView重新拿到onTouch权限
			break;
		default:
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

}
