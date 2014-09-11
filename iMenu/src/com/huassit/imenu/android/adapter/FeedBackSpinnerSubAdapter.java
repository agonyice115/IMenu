package com.huassit.imenu.android.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.huassit.imenu.android.R;
import com.huassit.imenu.android.model.Feedback;

/**
 * 反馈下拉列表适配器
 * 
 * @author shang_guan
 * 
 */
public class FeedBackSpinnerSubAdapter extends AbsAdapter<Feedback> {

	public FeedBackSpinnerSubAdapter(Activity context, int layout) {
		super(context, layout);
	}

	@Override
	public ViewHolder<Feedback> getHolder() {
		return new FeedbackViewHolder();
	}

	private final class FeedbackViewHolder implements ViewHolder<Feedback> {

		private TextView feedback_name;

		@Override
		public void initViews(View v, int position) {
			feedback_name = (TextView) v.findViewById(R.id.feedback_name);
		}

		@Override
		public void updateDatas(final Feedback mFeedback, int position) {
			feedback_name.setText(mFeedback.feedback_name);
		}

		@Override
		public void doOthers(Feedback mFeedback, int position) {

		}
	}
}
