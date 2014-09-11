package com.huassit.imenu.android.adapter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;

import com.huassit.imenu.android.MyApplication;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.db.dao.ServiceDao;
import com.huassit.imenu.android.model.Menu;
import com.huassit.imenu.android.model.Service;
import com.huassit.imenu.android.model.Store;
import com.huassit.imenu.android.ui.MainActivity;
import com.huassit.imenu.android.ui.MenuDetailActivity;
import com.huassit.imenu.android.ui.StoreDetailActivity;
import com.huassit.imenu.android.ui.settings.CommonSettingActivity;
import com.huassit.imenu.android.util.NumberFormatUtils;
import com.huassit.imenu.android.util.ScreenUtils;
import com.huassit.imenu.android.view.HorizontalListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class StoreAdapter extends BaseAdapter {

	private ArrayList<Store> storeList = new ArrayList<Store>();
	private Context context;
	private ImageLoader imageLoader;
	private LayoutInflater inflater;
	private DisplayImageOptions options;
	private ServiceDao serviceDao;
	public static final String DISTANCE = "1";
	public String sortType;
	public static final String PRICE = "2";
	public static final String POPULAR = "3";
	public StoreAdapter.LoadImageReceiver liReceiver = new LoadImageReceiver();
	private boolean isLoadOnlyOnWifi;
	private StoreMenuAdapter storeMenuAdapter;

	public StoreAdapter(Context context, String sortType) {
		serviceDao = new ServiceDao(context);
		this.context = context;
		this.sortType = sortType;
		this.inflater = LayoutInflater.from(context);
		imageLoader = ImageLoader.getInstance();
		options = MyApplication.getDisplayImageOptions(context, 40);
		IntentFilter filter = new IntentFilter(
				CommonSettingActivity.LoadImageOnWifiOnly);
		context.registerReceiver(liReceiver, filter);
	}

	public ArrayList<Store> getDataList() {
		return storeList;
	}

	@Override
	public int getCount() {
		return storeList.size();
	}

	@Override
	public Object getItem(int position) {
		return storeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.store_list_item, null);
			holder.layout_store_top = (RelativeLayout) convertView
					.findViewById(R.id.layout_store_top);
			holder.storename = (TextView) convertView
					.findViewById(R.id.storename);
			holder.service1 = (ImageView) convertView
					.findViewById(R.id.service1);
			holder.service2 = (ImageView) convertView
					.findViewById(R.id.service2);
			holder.service3 = (ImageView) convertView
					.findViewById(R.id.service3);
			holder.service4 = (ImageView) convertView
					.findViewById(R.id.service4);
			holder.layoutStar = (LinearLayout) convertView
					.findViewById(R.id.layout_star);
			holder.star1 = (ImageView) convertView.findViewById(R.id.star_1);
			holder.star2 = (ImageView) convertView.findViewById(R.id.star_2);
			holder.star3 = (ImageView) convertView.findViewById(R.id.star_3);
			holder.star4 = (ImageView) convertView.findViewById(R.id.star_4);
			holder.star5 = (ImageView) convertView.findViewById(R.id.star_5);
			holder.layoutPerPrice = (LinearLayout) convertView
					.findViewById(R.id.layout_per_price);
			holder.perPrice = (TextView) convertView
					.findViewById(R.id.per_price);
			holder.distance = (TextView) convertView
					.findViewById(R.id.distance);
			holder.distanceUnit = (TextView) convertView
					.findViewById(R.id.distance_unit);
			holder.layoutDistance = (LinearLayout) convertView
					.findViewById(R.id.layout_distance);
			holder.iv_store_logo = (ImageView) convertView
					.findViewById(R.id.iv_store_logo);
			holder.cashOrCredit = (ImageView) convertView
					.findViewById(R.id.cash_or_credit);
			holder.forsale = (ImageView) convertView
					.findViewById(R.id.for_sale);
			holder.horizontalListView = (HorizontalListView) convertView
					.findViewById(R.id.horizontallistview);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final Store t = (Store) getItem(position);
		if (t != null) {
			boolean isWifi = MainActivity.isWifi(context);
			holder.storename.setText(t.name);
			String logo_uri = t.logoLocation + t.logoName;
			imageLoader.displayImage(logo_uri, holder.iv_store_logo, options);
			storeMenuAdapter = new StoreMenuAdapter((Activity) context,
					R.layout.store_menu_item,t);
			holder.horizontalListView.setAdapter(storeMenuAdapter);
			// storeMenuAdapter.getDataList().clear();
			storeMenuAdapter.getDataList().addAll(t.menuList);
			storeMenuAdapter.notifyDataSetChanged();
			/***************** 显示 *********************/
			// 仅在wifi环境下 加载菜单图片（3G环境下不显示）
			if ((isWifi && isLoadOnlyOnWifi) || !isLoadOnlyOnWifi) {
				// 加载菜单图片
				holder.horizontalListView.setVisibility(View.VISIBLE);
				// storeMenuAdapter.getDataList().addAll(t.menuList);
				// storeMenuAdapter.notifyDataSetChanged();
			} else {
				holder.horizontalListView.setVisibility(View.GONE);
			}

			/** 显示服务缩略图 */
			List<ImageView> serverImageViews = new ArrayList<ImageView>();
			serverImageViews.add(holder.service1);
			serverImageViews.add(holder.service2);
			serverImageViews.add(holder.service3);
			serverImageViews.add(holder.service4);
			for (int i = 0; i < serverImageViews.size(); i++) {
				serverImageViews.get(i).setVisibility(View.GONE);
			}
			if (t.serviceList != null && t.serviceList.size() > 0) {
				ArrayList<Service> serviceList = new ArrayList<Service>();
				Service service = new Service();
				for (int i = 0; i < t.serviceList.size(); i++) {
					service = serviceDao
							.getServiceById(t.serviceList.get(i).serviceId);
					serviceList.add(service);
				}
				showService(serverImageViews, t.serviceList.size(), serviceList);
			}
			/** 付款及促销显示 */
			if (t.signingType != null) {// 1credit 2cash 3no
				if ("0".equals(t.signingType)) {
					holder.cashOrCredit.setVisibility(View.GONE);
				}
				if ("1".equals(t.signingType)) {
					holder.cashOrCredit.setVisibility(View.VISIBLE);
					holder.cashOrCredit.setImageResource(R.drawable.pay_online);
				}
				if ("2".equals(t.signingType)) {
					holder.cashOrCredit.setVisibility(View.VISIBLE);
					holder.cashOrCredit
							.setImageResource(R.drawable.cash_in_shop);
				}
			}
			if ("1".equals(t.couponType)) {// 促销类型(1-有促销|0-无促销)
				holder.forsale.setImageResource(R.drawable.for_sale);
			} else {
				holder.forsale.setImageBitmap(null);
			}
			holder.layout_store_top.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context,
							StoreDetailActivity.class);
					intent.putExtra("store", t);
					context.startActivity(intent);
				}
			});
			if (POPULAR.equals(sortType)) {
				/** 显示星级 */
				holder.layoutStar.setVisibility(View.VISIBLE);
				holder.layoutPerPrice.setVisibility(View.GONE);
				holder.layoutDistance.setVisibility(View.GONE);
				List<ImageView> mImageViews = new ArrayList<ImageView>();
				mImageViews.add(holder.star1);
				mImageViews.add(holder.star2);
				mImageViews.add(holder.star3);
				mImageViews.add(holder.star4);
				mImageViews.add(holder.star5);
				showStar(mImageViews, Integer.parseInt(t.star));
			} else if (PRICE.equals(sortType)) {
				/** 显示人均价格 */
				holder.layoutStar.setVisibility(View.GONE);
				holder.layoutPerPrice.setVisibility(View.VISIBLE);
				holder.layoutDistance.setVisibility(View.GONE);
				holder.perPrice.setText(t.per);
			} else if (DISTANCE.equals(sortType)) {
				/** 显示距离 */
				holder.layoutStar.setVisibility(View.GONE);
				holder.layoutPerPrice.setVisibility(View.GONE);
				holder.layoutDistance.setVisibility(View.VISIBLE);
				//大于1000米的显示km并且小数点后保留一位
				float distance = Integer.valueOf(t.distance);
				if (distance > 999) {
					Float distanceF = (float) (distance / 1000);
					holder.distance.setText(NumberFormatUtils.formatToOne(distanceF)
							+ "");
					holder.distanceUnit.setText("km");
				} else {
					holder.distance.setText(((int) distance) + "");
					holder.distanceUnit.setText("m");
				}
			}
		}
		return convertView;
	}

	private class ViewHolder {
		TextView storename;
		ImageView service1, service2, service3, service4;
		ImageView star1, star2, star3, star4, star5;
		ImageView iv_store_logo;
		RelativeLayout layout_store_top;
		HorizontalListView horizontalListView;
		ImageView cashOrCredit;
		ImageView forsale;
		LinearLayout layoutStar;
		LinearLayout layoutPerPrice;
		LinearLayout layoutDistance;
		TextView perPrice;
		TextView distance;
		TextView distanceUnit;

	}

	/**
	 * 星级数量控制展示
	 */
	private void showStar(List<ImageView> starts, int count) {
		for (int i = 0; i < count; i++) {
			starts.get(i).setImageResource(R.drawable.star_on);
		}
	}

	/**
	 * 服务数量控制及缩略图展示
	 */
	private void showService(List<ImageView> serviceImageViews, int count,
			ArrayList<Service> serviceList) {
		if (count > 4) {
			count = 4;
		}
		for (int i = 0; i < count; i++) {
			serviceImageViews.get(i).setVisibility(View.VISIBLE);
			imageLoader.displayImage(serviceList.get(i).thumb_image,
					serviceImageViews.get(i));
		}
	}

	/**
	 * 四舍五入
	 */
	static double convert(double value) {
		long l1 = Math.round(value * 100); // 四舍五入
		double ret = l1 / 100.0; // 注意:使用 100.0 而不是 100
		return ret;
	}

	class LoadImageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			isLoadOnlyOnWifi = intent
					.getBooleanExtra("isLoadOnlyOnWifi", false);

		}

	}
}
