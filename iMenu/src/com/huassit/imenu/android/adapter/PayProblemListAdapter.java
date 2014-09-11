package com.huassit.imenu.android.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huassit.imenu.android.R;
import com.huassit.imenu.android.model.Feedback;
import com.huassit.imenu.android.ui.cart.PayProblemActivity;
import com.huassit.imenu.android.util.StringUtils;

/**
 * 支付遇到问题列表适配器
 * 
 * @author shang_guan
 * 
 */
public class PayProblemListAdapter extends AbsAdapter<Feedback> {

	public PayProblemListAdapter(Activity context, int layout) {
		super(context, layout);
	}

	@Override
	public ViewHolder<Feedback> getHolder() {
		return new PayProblemViewHolder();
	}

	private class PayProblemViewHolder implements ViewHolder<Feedback> {

		/** 选中的复选框图片 */
		private ImageView item_checkbox;
		/** 问题原因内容 */
		private TextView item_tv_reason;
		/** 全局 */
		private LinearLayout layout_pay_problem;

		@Override
		public void initViews(View v, int position) {
			item_checkbox = (ImageView) v.findViewById(R.id.item_checkbox);
			item_tv_reason = (TextView) v.findViewById(R.id.item_tv_reason);
			layout_pay_problem = (LinearLayout) v
					.findViewById(R.id.layout_pay_problem);
		}

		@Override
		public void updateDatas(final Feedback mPayProblem, int position) {
			if (mPayProblem.isSelect) {
				item_checkbox
						.setImageResource(R.drawable.icon_onlinepay_chonse_down);
			} else {
				item_checkbox
						.setImageResource(R.drawable.icon_onlinepay_chonse_up);
			}
			item_tv_reason.setText(mPayProblem.feedback_name);
			layout_pay_problem.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!mPayProblem.isSelect) {
						((PayProblemActivity) context)
								.changeStatus(mPayProblem.feedback_id);
						item_checkbox
								.setImageResource(R.drawable.icon_onlinepay_chonse_down);
						mPayProblem.isSelect = true;
					}
					if (mPayProblem.feedback_name.equals(StringUtils.getString(context, R.string.other_reason))) {
						((PayProblemActivity) context).showEdit(true);
					} else {
						((PayProblemActivity) context).showEdit(false);
					}
				}
			});
		}

		@Override
		public void doOthers(Feedback mPayProblem, int position) {

		}
	}
}
