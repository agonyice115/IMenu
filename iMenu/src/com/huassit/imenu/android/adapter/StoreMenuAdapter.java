package com.huassit.imenu.android.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.huassit.imenu.android.R;
import com.huassit.imenu.android.model.Menu;
import com.huassit.imenu.android.model.Store;
import com.huassit.imenu.android.ui.MenuDetailActivity;
import com.huassit.imenu.android.util.NumberFormatUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class StoreMenuAdapter extends AbsAdapter<Menu> {

	private ImageLoader imageLoader;
	private Store store;

	public StoreMenuAdapter(Activity context, int layout, Store store) {
		super(context, layout);
		this.store = store;
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public ViewHolder<Menu> getHolder() {
		return new StoreMenuViewHolder();
	}

	class StoreMenuViewHolder implements ViewHolder<Menu> {
		ImageView menuPic;
		TextView menuName;
		TextView menuPrice;
		TextView isSale;

		@Override
		public void initViews(View v, int position) {
			menuPic = (ImageView) v.findViewById(R.id.menu_pic);
			menuName = (TextView) v.findViewById(R.id.menu_name);
			menuPrice = (TextView) v.findViewById(R.id.menu_price);
			isSale = (TextView) v.findViewById(R.id.is_sale);

		}

		@Override
		public void updateDatas(final Menu t, int position) {
			imageLoader.clearMemoryCache();
			imageLoader.displayImage(t.menu_image_location + t.menu_image_name,
					menuPic);
			menuName.setText(t.menu_name);
			if (!StringUtils.isBlank(t.menu_coupon_price)) {
				isSale.setVisibility(View.VISIBLE);
				menuPrice.setText(NumberFormatUtils.format(Float
						.parseFloat(t.menu_coupon_price)));
			} else {
				isSale.setVisibility(View.GONE);
				menuPrice.setText(NumberFormatUtils.format(Float
						.parseFloat(t.menu_price)));
			}
			menuPic.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context,
							MenuDetailActivity.class);
					intent.putExtra("menuId", t.menu_id);
					intent.putExtra("store", store);
					context.startActivity(intent);
				}
			});
		}

		@Override
		public void doOthers(Menu t, int position) {

		}

	}
}
