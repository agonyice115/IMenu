package com.huassit.imenu.android.adapter;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.huassit.imenu.android.R;
import com.huassit.imenu.android.model.Menu;
import com.huassit.imenu.android.ui.cart.CartActivity;
import com.huassit.imenu.android.util.NumberFormatUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class OrderedMenuAdapter extends AbsAdapter<Menu> {

	ImageLoader imageLoader;
	/** 标石从购物车使用还是订单详情使用1为购物车 */
	private int code;
	/** 判断是否有优惠的菜品 */
	public boolean isCoupon;

	public OrderedMenuAdapter(Activity context, int layout, int code) {
		super(context, layout);
		this.code = code;
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public com.huassit.imenu.android.adapter.AbsAdapter.ViewHolder<Menu> getHolder() {
		return new OrderedMenuViewHolder();
	}

	class OrderedMenuViewHolder implements ViewHolder<Menu> {

		ImageView menuPic;
		TextView menuName;
		TextView menuPrice;
		ImageView add;
		ImageView reduce;
		TextView count;
		TextView coupon_price;
		TextView is_sale;

		// int countInt=1;

		@Override
		public void initViews(View v, int position) {
			menuPic = (ImageView) v.findViewById(R.id.menu_pic);
			menuName = (TextView) v.findViewById(R.id.menu_name);
			menuPrice = (TextView) v.findViewById(R.id.price);
			add = (ImageView) v.findViewById(R.id.add);
			reduce = (ImageView) v.findViewById(R.id.reduce);
			count = (TextView) v.findViewById(R.id.count);
			is_sale = (TextView) v.findViewById(R.id.is_sale);
			coupon_price = (TextView) v.findViewById(R.id.coupon_price);
		}

		@Override
		public void updateDatas(final Menu t, final int position) {
			imageLoader.displayImage(t.menu_image_location + t.menu_image_name,
					menuPic);
			menuName.setText(t.menu_name);
			float price = Float.valueOf(t.menu_price);
			if (!StringUtils.isBlank(t.menu_coupon_price)) {
				isCoupon = true;
				coupon_price.setText("￥" + NumberFormatUtils.format(price));
				coupon_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
				float coupon_price = Float.valueOf(t.menu_coupon_price);
				menuPrice.setText("￥" + NumberFormatUtils.format(coupon_price));
				is_sale.setVisibility(View.VISIBLE);
			} else {
				menuPrice.setText("￥" + NumberFormatUtils.format(price));
				is_sale.setVisibility(View.GONE);
			}
			count.setText(t.menu_count + "");
			if (code == 1) {
				add.setVisibility(View.GONE);
				reduce.setVisibility(View.GONE);
				count.setVisibility(View.GONE);
			}
			add.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					t.menu_count++;
					count.setText(t.menu_count + "");
					((CartActivity) context).countTotalPrice(position);
				}
			});
			reduce.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (t.menu_count > 0) {
						t.menu_count--;
						count.setText(t.menu_count + "");
					}
					if (t.menu_count == 0) {
						new AlertDialog.Builder(context)
								.setMessage(
										StringUtils.getString(context,
												R.string.sure_to_clear_menu))
								.setPositiveButton(
										StringUtils.getString(context,
												R.string.confirm),
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												((CartActivity) context)
														.removeItem(position);
												((CartActivity) context)
														.countTotalPrice(position);
											}
										})
								.setNegativeButton(
										StringUtils.getString(context,
												R.string.cancel),
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												t.menu_count++;
												count.setText(t.menu_count + "");
											}
										}).create().show();
					} else {
						((CartActivity) context).countTotalPrice(position);
					}
				}
			});
		}

		@Override
		public void doOthers(Menu t, int position) {

		}

	}
	
	public boolean haveCoupon(){
		return isCoupon;
	}

}
