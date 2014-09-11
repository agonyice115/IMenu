package com.huassit.imenu.android.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huassit.imenu.android.R;
import com.huassit.imenu.android.model.Feedback;

/**
 * 反馈下拉列表适配器
 * 
 * @author shang_guan
 * 
 */
public class FeedBackSpinnerAdapter extends BaseAdapter {

	private List<Feedback> mFeedbacksList;
	private Context context;

	public FeedBackSpinnerAdapter(Activity context, List<Feedback> mFeedbacks) {
		this.context = context;
		this.mFeedbacksList = mFeedbacks;
	}

	//
	// @Override
	// public ViewHolder<Feedback> getHolder() {
	// return new FeedbackViewHolder();
	// }
	//
	// private final class FeedbackViewHolder implements ViewHolder<Feedback> {
	//
	// private TextView feedback_name;
	//
	// @Override
	// public void initViews(View v, int position) {
	// feedback_name = (TextView) v.findViewById(R.id.feedback_name);
	// }
	//
	// @Override
	// public void updateDatas(final Feedback mFeedback, int position) {
	// feedback_name.setText(mFeedback.feedback_name);
	// }
	//
	// @Override
	// public void doOthers(Feedback mFeedback, int position) {
	//
	// }
	// }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mFeedbacksList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mFeedbacksList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater _LayoutInflater = LayoutInflater.from(context);
		convertView = _LayoutInflater.inflate(
				R.layout.feedback_spinner_list_item, null);
		if (convertView != null) {
			TextView feedback_name = (TextView) convertView
					.findViewById(R.id.feedback_name);
			feedback_name.setText(mFeedbacksList.get(position).feedback_name);
		}
		return convertView;
	}
}
