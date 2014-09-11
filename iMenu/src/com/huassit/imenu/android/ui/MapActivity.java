package com.huassit.imenu.android.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import android.widget.LinearLayout.LayoutParams;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.MyApplication;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.adapter.StoreListOnMapViewAdapter;
import com.huassit.imenu.android.biz.GetStoreListAndMenusResp;
import com.huassit.imenu.android.model.Store;
import com.huassit.imenu.android.util.ScreenUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.view.NavigationView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sylar on 14-6-26.
 */
public class MapActivity extends BaseActivity {
	private static final String SORT_TYPE = "ASC";
	private static final int MENU_COUNT = 6;

	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private NavigationView mNavigationView;
	private LocationClient mLocationClient;
	private ListView mStoreListView;
	private StoreListOnMapViewAdapter mAdapter;
	private ImageView mShowStoreListButton;
	private ImageView mNextButton;
	private ImageView mPreviewButton;
	private List<Store> mStoreList;
	private int currentPage = 1;
	private int total;
	private static final int SIZE = 5;
	private String mSortBy;
	private String mRegion;
	private String mCategory;
	private String mService;
	private boolean isFirst = true;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private TextView divider;

	private BDLocationListener mLocationListener = new BDLocationListener() {
		@Override
		public void onReceiveLocation(BDLocation bdLocation) {
			setMyLocation(bdLocation);
		}

		@Override
		public void onReceivePoi(BDLocation bdLocation) {

		}
	};

	@Override
	public int getContentView() {
		return R.layout.map_activity;
	}

	@Override
	public void initView() {
		imageLoader = ImageLoader.getInstance();
		options = MyApplication.getDisplayImageOptions(this, 30);
		divider =(TextView) findViewById(R.id.divider);
		mMapView = (MapView) findViewById(R.id.bMapView);
		mNavigationView = (NavigationView) findViewById(R.id.navigationView);
		mNavigationView.setNavigateItemVisibility(NavigationView.TITLE,
				View.GONE);
		mNavigationView.setNavigateItemVisibility(
				NavigationView.FIRST_RIGHT_TEXT_VIEW, View.GONE);
		mNavigationView.setNavigateItemText(NavigationView.LEFT_TEXT_VIEW,
				R.string.store_position_title);
		mNavigationView.setNavigateItemVisibility(
				NavigationView.LEFT_IMAGE_VIEW, View.GONE);
		mNavigationView.setNavigateItemBackground(
				NavigationView.SECOND_RIGHT_TEXT_VIEW, R.drawable.close);
		mNavigationView.setNavigateItemOnClickListener(
				NavigationView.SECOND_RIGHT_TEXT_VIEW,
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		mStoreListView = (ListView) findViewById(R.id.storeListViewOnMapView);
		mAdapter = new StoreListOnMapViewAdapter(this,
				R.layout.store_on_map_list_view_item);
		mStoreListView.setAdapter(mAdapter);
		mStoreListView.setVisibility(View.GONE);
		mStoreListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Store store = (Store) parent
								.getItemAtPosition(position);
						Intent intent = new Intent(MapActivity.this,
								StoreDetailActivity.class);
						intent.putExtra("store", store);
						startActivity(intent);
						finish();
					}
				});

		mNextButton = (ImageView) findViewById(R.id.nextButton);
		mNextButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mStoreList.size() > currentPage * SIZE) {
					List<Store> subList = mStoreList.subList((currentPage - 1)
							* SIZE, currentPage * SIZE);
					addStoreData(subList);
				} else if (mStoreList.size() < currentPage * SIZE
						&& mStoreList.size() > (currentPage - 1) * SIZE) {
					List<Store> subList = mStoreList.subList((currentPage - 1)
							* SIZE, mStoreList.size());
					addStoreData(subList);
				} else {
					loadData(mSortBy);
				}
				currentPage++;
			}
		});

		mPreviewButton = (ImageView) findViewById(R.id.previewButton);
		mPreviewButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (currentPage < 1) {
					currentPage = 1;
				}
				if (mStoreList.size() > currentPage * SIZE) {
					List<Store> subList = mStoreList.subList((currentPage - 1)
							* SIZE, currentPage * SIZE);
					addStoreData(subList);
				} else if (mStoreList.size() < currentPage * SIZE
						&& mStoreList.size() > (currentPage - 1) * SIZE) {
					List<Store> subList = mStoreList.subList((currentPage - 1)
							* SIZE, mStoreList.size());
					addStoreData(subList);
				} else {
					loadData(mSortBy);
				}
				currentPage--;
			}
		});

		mShowStoreListButton = (ImageView) findViewById(R.id.showStoreListButton);
		mShowStoreListButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mStoreListView.isShown()) {
					mStoreListView.setVisibility(View.GONE);
					divider.setVisibility(View.GONE);
					mShowStoreListButton
							.setBackgroundResource(R.drawable.menu_blue);
				} else {
					mStoreListView.setVisibility(View.VISIBLE);
					divider.setVisibility(View.VISIBLE);
					mShowStoreListButton
							.setBackgroundResource(R.drawable.minus_pressed);
				}
			}
		});

		ImageView myLocation = (ImageView) findViewById(R.id.myLocation);
		myLocation.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				BDLocation location = mLocationClient.getLastKnownLocation();
				setMyLocation(location);
				animateMapView(location);
			}
		});
		initBaiDuMap();
	}

	private void initBaiDuMap() {
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMyLocationEnabled(true);
		mBaiduMap.setTrafficEnabled(false);
		mBaiduMap.getUiSettings().setAllGesturesEnabled(true);
		mBaiduMap.getUiSettings().setCompassEnabled(false);
		mBaiduMap.getUiSettings().setRotateGesturesEnabled(false);
		mBaiduMap.getUiSettings().setZoomGesturesEnabled(true);
		mBaiduMap.getUiSettings().setOverlookingGesturesEnabled(false);

		for (int i = 0; i < mMapView.getChildCount(); i++) {
			View child = mMapView.getChildAt(i);
			if (child instanceof ZoomControls) {
				child.setVisibility(View.INVISIBLE);
			}
		}
		mBaiduMap
				.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
					@Override
					public boolean onMarkerClick(Marker marker) {
						Bundle info = marker.getExtraInfo();
						int index = info.getInt("index", -1);
						if (!mStoreListView.isShown()) {
							mStoreListView.setVisibility(View.VISIBLE);
							mShowStoreListButton
									.setBackgroundResource(R.drawable.minus_pressed);
						}
						mAdapter.setSelectedPosition(index);
						mAdapter.notifyDataSetChanged();
						mStoreListView.setSelection(index);
						return true;
					}
				});
		mLocationClient = new LocationClient(this);
		mLocationClient.registerLocationListener(mLocationListener);
		mLocationClient.setForBaiduMap(true);
		mLocationClient.start();
		mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
			@Override
			public void onMapLoaded() {
				mSortBy = getIntent().getStringExtra("SortBy");
				mRegion = getIntent().getStringExtra("Region");
				mCategory = getIntent().getStringExtra("Category");
				mService = getIntent().getStringExtra("Service");
				mAdapter.setSortType(mSortBy);
				mStoreList = (List<Store>) getIntent().getSerializableExtra(
						"Data");
				if (mStoreList != null && mStoreList.size() > 0) {
					total = (mStoreList.size() - 1) / SIZE + 1;
					if (mStoreList.size() > SIZE) {
						List<Store> subList = mStoreList.subList(0, SIZE);
						addStoreData(subList);
					} else if (mStoreList.size() < SIZE
							&& mStoreList.size() > 0) {
						addStoreData(mStoreList);
					} else {
						loadData(mSortBy);
					}
				} else {
					loadData(mSortBy);
				}
			}
		});
	}

	private void setMyLocation(BDLocation location) {
		if (location != null) {
			// 构造定位数据
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			// 设置定位数据
			mBaiduMap.setMyLocationData(locData);
			if (isFirst) {
				animateMapView(location);
				isFirst = false;
			}
		}
	}

	private void animateMapView(BDLocation location) {
		MapStatusUpdate update = MapStatusUpdateFactory
				.newLatLngZoom(
						new LatLng(location.getLatitude(), location
								.getLongitude()), 16);
		mBaiduMap.animateMapStatus(update);
	}

	private void animateMapView(LatLng location) {
		MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(location,
				16);
		mBaiduMap.animateMapStatus(update);
	}

	@Override
	public void initData() {

	}

	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.requestLocation();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	@Override
	protected void onDestroy() {
		mLocationClient.stop();
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

	private void loadData(String sortBy) {
		BDLocation location = mLocationClient.getLastKnownLocation();
		String lat = "0";
		String lng = "0";
		if (location != null) {
			lat = String.valueOf(location.getLatitude());
			lng = String.valueOf(location.getLongitude());
		}
		GetStoreListAndMenusResp storeResp = new GetStoreListAndMenusResp(this,
				handler, mCategory, mRegion, sortBy, SORT_TYPE, SIZE,
				currentPage, MENU_COUNT, lng, lat, mService);
		storeResp.asyncInvoke(SUCCESS, FAILURE);
		showProgressDialog(StringUtils.getString(MapActivity.this,
				R.string.loading));
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FAILURE:
				closeProgressDialog();
				prompt(StringUtils.getString(MapActivity.this,
						R.string.get_data_false));
				break;
			case SUCCESS:
				closeProgressDialog();
				List<Store> storeList = (ArrayList<Store>) msg.obj;
				mStoreList.addAll(storeList);
				addStoreData(storeList);
				break;
			}
		}
	};

	private void addStoreData(List<Store> storeList) {
		mBaiduMap.clear();
		for (int i = 0; i < storeList.size(); i++) {
			addMarker(storeList.get(i), i);
		}
		mAdapter.getDataList().clear();
		mAdapter.getDataList().addAll(storeList);
		mAdapter.notifyDataSetChanged();
	}

	private void addMarker(final Store store, final int index) {
		final View logoView = LayoutInflater.from(this).inflate(
				R.layout.store_logo_on_map_view, null);
		ImageView logoImage = (ImageView) logoView.findViewById(R.id.storeLogo);
		TextView indexText = (TextView) logoView.findViewById(R.id.storeIndex);
		indexText.setText(String.valueOf(index + 1));
		imageLoader.displayImage(store.logoLocation + store.logoName,
				logoImage, options, new SimpleImageLoadingListener() {

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						MarkerOptions markerOptions = new MarkerOptions();
						markerOptions.position(new LatLng(Double
								.valueOf(store.latitude), Double
								.valueOf(store.longitude)));
						markerOptions.icon(BitmapDescriptorFactory
								.fromView(logoView));
						Overlay overlay = mBaiduMap.addOverlay(markerOptions);
						Bundle info = new Bundle();
						info.putInt("index", index);
						overlay.setExtraInfo(info);
						overlay.setVisible(true);
						if (index == 0) {
							animateMapView(markerOptions.getPosition());
						}
					}
				});
	}
}