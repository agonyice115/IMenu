package com.huassit.imenu.android.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huassit.imenu.android.MyApplication;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.model.Cart;
import com.huassit.imenu.android.model.Menu;
import com.huassit.imenu.android.model.Store;
import com.huassit.imenu.android.ui.MenuListActivity;
import com.huassit.imenu.android.util.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MenuAdapter extends BaseAdapter {

	private Context context;
	private ImageLoader imageLoader;
	private LayoutInflater inflater;
	private Store store;
	private ArrayList<Menu> menuList = new ArrayList<Menu>();
	private ViewHolder holder;
	private MyApplication application;
	private ArrayList<Menu> orderedMenuList = new ArrayList<Menu>();
	private Map<String, Cart> cartMap;

	public MenuAdapter(Context context, Store store) {
		this.context = context;
		imageLoader = ImageLoader.getInstance();
		inflater = LayoutInflater.from(context);
		this.store = store;
		application = (MyApplication) context.getApplicationContext();
	}

	public ArrayList<Menu> getDataList() {
		return menuList;
	}

	@Override
	public int getCount() {
		return menuList.size();
	}

	@Override
	public Object getItem(int position) {
		return menuList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		holder = new ViewHolder();
		if (v == null) {
			v = inflater.inflate(R.layout.menu_list_item, null);
			holder.menuPic = (ImageView) v.findViewById(R.id.menu_pic);
			holder.menuName = (TextView) v.findViewById(R.id.menu_name);
			holder.menuPrice = (TextView) v.findViewById(R.id.price);
			holder.order = (TextView) v.findViewById(R.id.order);
			holder.iv_order = (ImageView) v.findViewById(R.id.iv_order);
			holder.layout_order = (LinearLayout) v
					.findViewById(R.id.layout_order);
			holder.coupon_price = (TextView) v.findViewById(R.id.coupon_price);
			holder.is_sale = (TextView) v.findViewById(R.id.is_sale);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}

		final Menu t = (Menu) getItem(position);
		if (t != null) {
			imageLoader.displayImage(t.menu_image_location + t.menu_image_name,
					holder.menuPic);
			holder.menuName.setText(t.menu_name);
			if (!StringUtils.isBlank(t.menu_coupon_price)) {
				holder.coupon_price.setText("￥" + t.menu_price);
				holder.coupon_price.getPaint().setFlags(
						Paint.STRIKE_THRU_TEXT_FLAG);
				holder.menuPrice.setText("￥" + t.menu_coupon_price);
				holder.is_sale.setVisibility(View.VISIBLE);
			} else {
				holder.menuPrice.setText("￥" + t.menu_price);
				holder.is_sale.setVisibility(View.GONE);
			}

			holder.layout_order.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					t.menu_count = 1;
					// }
					if (t.isOrdered) {
						// 取消下单
						t.isOrdered = false;
						application.getCartMap().get(store.id).menuList
								.remove(t);
					} else {
						// 下单
						t.isOrdered = true;
						if (application.getCartMap() != null
								&& null != application.getCartMap().get(
										store.id)
								&& application.getCartMap().get(store.id).menuList
										.size() > 0) {
							cartMap = application.getCartMap();
							Cart cart = cartMap.get(store.id);
							if (null == cart) {
								Cart cart1 = new Cart();
								orderedMenuList.add(t);
								cart1.menuList = orderedMenuList;
								cartMap.put(store.id, cart1);
								application.setCartMap(cartMap);
							} else {
								orderedMenuList.add(t);
								for (int i = 0; i < orderedMenuList.size(); i++) {
									if (!cart.menuList.contains(orderedMenuList
											.get(i))) {
										cart.menuList.add(orderedMenuList
												.get(i));
									}
								}
								cartMap.put(store.id, cart);
								application.setCartMap(cartMap);
							}
						} else {
							// 购物车为空
							cartMap = new HashMap<String, Cart>();
							Cart cart = new Cart();
							orderedMenuList.add(t);
							cart.menuList = orderedMenuList;
							cartMap.put(store.id, cart);
							application.setCartMap(cartMap);
						}
					}
					((MenuListActivity) context).showOrHideLayout();
					MenuAdapter.this.notifyDataSetChanged();
				}
			});
			if (t.isOrdered) {
				if (!orderedMenuList.contains(t)) {
					// if (t.menu_count == 0) {
					t.menu_count = 1;
					// }
					orderedMenuList.add(t);
				}
				// 标成已下单
				holder.order.setText(context.getResources().getString(
						R.string.already_choosed));
				holder.order.setTextColor(context.getResources().getColor(
						R.color.blue));
				holder.iv_order.setImageResource(R.drawable.check);

			} else {
				if (orderedMenuList.contains(t)) {
					orderedMenuList.remove(t);
				}
				// 取消下单
				holder.order.setText(context.getResources().getString(
						R.string.choose_menu));
				holder.order.setTextColor(Color.parseColor("#666666"));
				holder.iv_order.setImageResource(R.drawable.order);
			}
		}
		return v;
	}

	private class ViewHolder {
		ImageView menuPic;
		TextView menuName;
		TextView menuPrice;
		ImageView iv_order;
		TextView order;
		LinearLayout layout_order;
		TextView coupon_price;
		TextView is_sale;
	}

}
