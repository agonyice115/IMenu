package com.huassit.imenu.android.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.huassit.imenu.android.R;
import com.huassit.imenu.android.model.Recommand;
import com.huassit.imenu.android.util.StringUtils;

/**
 * 店长推荐对话框适配器
 * 
 * @author shang_guan
 * 
 */
public class StoreRecommandDialogListAdapter extends AbsAdapter<Recommand> {

	public StoreRecommandDialogListAdapter(Activity context, int layout) {
		super(context, layout);
	}

	@Override
	public ViewHolder<Recommand> getHolder() {
		return new FansViewHolder();
	}

	private final class FansViewHolder implements ViewHolder<Recommand> {

		/** 粉丝名称 */
		private TextView tv_coupon;

		@Override
		public void initViews(View v, int position) {
			tv_coupon = (TextView) v.findViewById(R.id.tv_coupon);
		}

		@Override
		public void updateDatas(final Recommand mCoupon, int position) {
			tv_coupon.setText(mCoupon.recommandPeople + StringUtils.getString(context, R.string.people));
		}

		@Override
		public void doOthers(Recommand mFans, int position) {

		}
	}
}
