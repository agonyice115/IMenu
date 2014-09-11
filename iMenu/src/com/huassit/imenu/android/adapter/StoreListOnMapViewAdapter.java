package com.huassit.imenu.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huassit.imenu.android.MyApplication;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.db.dao.ServiceDao;
import com.huassit.imenu.android.model.Service;
import com.huassit.imenu.android.model.Store;
import com.huassit.imenu.android.ui.MainActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Sylar on 14-6-27.
 */
public class StoreListOnMapViewAdapter extends AbsAdapter<Store> {

	private Context mContext;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private int selectedPosition = -1;
	private String sortType;
	private static final int START_SIZE = 5;
	private ServiceDao serviceDao;
	private LinearLayout.LayoutParams serviceParams = new LinearLayout.LayoutParams(
			40, 40);

	public void setSelectedPosition(int position) {
		selectedPosition = position;
	}

	public StoreListOnMapViewAdapter(Activity context, int layout) {
		super(context, layout);
		mContext = context;
		imageLoader = ImageLoader.getInstance();
		serviceDao = new ServiceDao(context);
		options = MyApplication.getDisplayImageOptions(context, 40);
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	@Override
	public ViewHolder<Store> getHolder() {
		return new StoreViewHolder();
	}

	private class StoreViewHolder implements ViewHolder<Store> {

		private TextView mStoreName;
		private TextView mStoreAddress;
		private TextView mDistance;
		private ImageView mStoreLogo;
		private RelativeLayout mItem;
		private ImageView locationImageView;
		private LinearLayout startLayout;
		private LinearLayout serviceLayout;
		private TextView tv_store_index;
		private ImageView iv_storeType;

		@Override
		public void initViews(View v, int position) {
			iv_storeType = (ImageView) v.findViewById(R.id.iv_storeType);
			mStoreName = (TextView) v.findViewById(R.id.storeName);
			mStoreAddress = (TextView) v.findViewById(R.id.storeAddress);
			mDistance = (TextView) v.findViewById(R.id.distance);
			mStoreLogo = (ImageView) v.findViewById(R.id.storeLogo);
			mItem = (RelativeLayout) v.findViewById(R.id.item);
			locationImageView = (ImageView) v
					.findViewById(R.id.locationImageView);
			serviceLayout = (LinearLayout) v.findViewById(R.id.serviceLayout);
			startLayout = (LinearLayout) v.findViewById(R.id.startLayout);
			tv_store_index = (TextView) v.findViewById(R.id.tv_store_index);
		}

		@Override
		public void updateDatas(Store store, int position) {
			System.out.println("store.signingType:" + store.signingType);
			if (store.signingType.equals("1")) {
				// 在线
				iv_storeType.setVisibility(View.VISIBLE);
				iv_storeType.setImageResource(R.drawable.pay_online);
			} else if (store.signingType.equals("2")) {
				// 到店
				iv_storeType.setVisibility(View.VISIBLE);
				iv_storeType.setImageResource(R.drawable.cash_in_shop);
			} else {
				// 非签约
				iv_storeType.setVisibility(View.INVISIBLE);
			}
			tv_store_index.setText(position + 1 + "");
			mStoreName.setText(store.name);
			mStoreAddress.setText(store.address);
			if (sortType.equals(MainActivity.POPULAR)) {
				locationImageView.setBackgroundResource(R.drawable.pop_pressed);
				mDistance.setText(mContext.getString(R.string.popular_label));
				int starts = Integer.parseInt(store.star);
				startLayout.removeAllViews();
				for (int i = 0; i < START_SIZE; i++) {
					ImageView startImage = new ImageView(context);
					if (i < starts) {
						startImage.setBackgroundResource(R.drawable.star_on);
					} else {
						startImage.setBackgroundResource(R.drawable.star_off);
					}
					startLayout.addView(startImage);
				}
			} else if (sortType.equals(MainActivity.DISTANCE)) {
				locationImageView.setBackgroundResource(R.drawable.map_pressed);
				mDistance.setText(mContext.getString(
						R.string.store_distance_label, store.distance));
			} else if (sortType.equals(MainActivity.PRICE)) {
				locationImageView
						.setBackgroundResource(R.drawable.price_pressed);
				mDistance.setText(mContext.getString(R.string.price_label,
						store.per));
			}

			List<Service> serviceList = store.serviceList;
			if (serviceList.size() > 4) {
				serviceList = serviceList.subList(0, 4);
			}
			serviceLayout.removeAllViews();
			for (Service service : serviceList) {
				service = serviceDao.getServiceById(service.serviceId);
				ImageView serviceImage = new ImageView(mContext);
				serviceImage.setLayoutParams(serviceParams);
				serviceLayout.addView(serviceImage);
				imageLoader.displayImage(service.thumb_image, serviceImage);
			}
			imageLoader.displayImage(store.logoLocation + store.logoName,
					mStoreLogo, options);
			if (selectedPosition == position) {
				mItem.setBackgroundColor(Color.WHITE);
			} else {
				mItem.setBackgroundColor(Color.parseColor("#e9e9e9"));
			}
		}

		@Override
		public void doOthers(Store store, int position) {

		}
	}
}
