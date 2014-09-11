package com.huassit.imenu.android.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.huassit.imenu.android.R;
import com.huassit.imenu.android.model.Coupon;
import com.huassit.imenu.android.ui.cart.CartActivity;

/**
 * 购物车优惠对话框适配器
 * 
 * @author shang_guan
 * 
 */
public class CartDialogListAdapter extends AbsAdapter<Coupon> {

	public CartDialogListAdapter(Activity context, int layout) {
		super(context, layout);
	}

	@Override
	public ViewHolder<Coupon> getHolder() {
		return new FansViewHolder();
	}

	private final class FansViewHolder implements ViewHolder<Coupon> {

		/** 粉丝名称 */
		private TextView tv_coupon;

		@Override
		public void initViews(View v, int position) {
			tv_coupon = (TextView) v.findViewById(R.id.tv_coupon);
		}

		@Override
		public void updateDatas(final Coupon mCoupon, int position) {
			tv_coupon.setText(mCoupon.couponTitle);
//			tv_coupon.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					
//				}
//			});
		}

		@Override
		public void doOthers(Coupon mFans, int position) {

		}
	}
}
