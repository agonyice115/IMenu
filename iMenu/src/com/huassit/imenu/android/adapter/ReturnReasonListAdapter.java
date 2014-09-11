package com.huassit.imenu.android.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huassit.imenu.android.R;
import com.huassit.imenu.android.model.ReturnReason;
import com.huassit.imenu.android.ui.ApplyReturnActivity;

/**
 * 退款原因适配器
 * 
 * @author shang_guan
 * 
 */
public class ReturnReasonListAdapter extends AbsAdapter<ReturnReason> {

	public ReturnReasonListAdapter(Activity context, int layout) {
		super(context, layout);
	}

	@Override
	public ViewHolder<ReturnReason> getHolder() {
		return new FansViewHolder();
	}

	private final class FansViewHolder implements ViewHolder<ReturnReason> {

		/** 复选框 */
		private ImageView check_box;
		/** 退款原因 */
		private TextView return_reason_name;
		/** 全局 */
		private LinearLayout layout_return_reason_list_item;

		@Override
		public void initViews(View v, int position) {
			check_box = (ImageView) v.findViewById(R.id.check_box);
			return_reason_name = (TextView) v
					.findViewById(R.id.return_reason_name);
			layout_return_reason_list_item = (LinearLayout) v
					.findViewById(R.id.layout_return_reason_list_item);
		}

		@Override
		public void updateDatas(final ReturnReason mReason, int position) {
			return_reason_name.setText(mReason.return_reason_name);
//			if (mReason.isTrue) {
//				check_box
//				.setImageResource(R.drawable.icon_onlinepay_chonse_down);
//			}else{
//				check_box
//				.setImageResource(R.drawable.icon_onlinepay_chonse_up);
//			}
			check_box.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (mReason.isTrue) {
						// 取消选中
						mReason.isTrue = false;
						check_box
						.setImageResource(R.drawable.icon_onlinepay_chonse_up);
						((ApplyReturnActivity) context).changeCheckBox(mReason,false);
					} else {
						mReason.isTrue = true;
						check_box
						.setImageResource(R.drawable.icon_onlinepay_chonse_down);
						((ApplyReturnActivity) context).changeCheckBox(mReason,true);
					}
				}
			});
//			layout_return_reason_list_item
//					.setOnClickListener(new View.OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							if (mReason.isTrue) {
//								// 取消选中
//								mReason.isTrue = false;
//								check_box
//										.setImageResource(R.drawable.icon_onlinepay_chonse_up);
//							} else {
//								mReason.isTrue = true;
//								check_box
//										.setImageResource(R.drawable.icon_onlinepay_chonse_down);
//							}
//						}
//					});
		}

		@Override
		public void doOthers(ReturnReason mReason, int position) {

		}
	}
}
