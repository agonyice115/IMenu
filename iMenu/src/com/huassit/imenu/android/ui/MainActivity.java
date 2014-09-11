package com.huassit.imenu.android.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import cn.sharesdk.framework.ShareSDK;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.MyApplication;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.adapter.StoreAdapter;
import com.huassit.imenu.android.biz.GetCartResp;
import com.huassit.imenu.android.biz.GetStoreListAndMenusResp;
import com.huassit.imenu.android.db.SQLiteManager;
import com.huassit.imenu.android.db.dao.RegionDao;
import com.huassit.imenu.android.model.Cart;
import com.huassit.imenu.android.model.Store;
import com.huassit.imenu.android.util.LocationUtils;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.util.TimeUtil;
import com.huassit.imenu.android.view.NavigationView;
import com.huassit.imenu.android.view.SegmentControllerView;

public class MainActivity extends BaseActivity {

	/** 距离 */
	public static final String DISTANCE = "1";
	/** 价格 */
	public static final String PRICE = "2";
	/** 热门 */
	public static final String POPULAR = "3";
	/** 默认地区 */
	private static final String DEFAULT_REGION = "1";
	/** 人们排序 */
	private String HOT_SORT_TYPE = "DESC";
	/** 价格排序 */
	private String PRICE_SORT_TYPE = "ASC";
	/** 距离排序 */
	private String DISTANCE_SORT_TYPE = "ASC";
	/** 分类 */
	private String SORT_TYPE = HOT_SORT_TYPE;
	/** 每页请求的数量 */
	private static final int SIZE = 10;
	/** 每页请求的菜品数量 */
	private static final int MENU_COUNT = 6;
	private static final int REQUEST_CODE = 100;
	private int currentPage = 1;
	private String mSortBy = POPULAR;
	// 默认全部，9
	private static final String defaultCategory = "9";
	private String mCategory = defaultCategory;
	// 默认西安 29
	private static final String defaultRegion = "29";
	private String mRegion = defaultRegion;
	private final static String defaultService = "0";
	private String mService = defaultService;
	private NavigationView mNavigationView;
	private ListView mListView;
	private StoreAdapter mStoreAdapter;
	private boolean isQuit;
	private Timer mTimer;
	private GeoCoder mGeoCoder;
	private RegionDao regionDao;
	private SegmentControllerView segmentControllerView;
	/** 获取购物车成功 */
	private static final int GET_CART_SUCCESS = 200;
	/** 获取购物车失败 */
	private static final int GET_CART_FAILED = 201;
	private MyApplication application;
	/** 标石切换种类时，需设置listView焦点处于第一行 */
	private boolean isTransition = false;

	/**
	 * 下拉刷新控件
	 */
	private PullToRefreshListView store_listView;
	/**
	 * 是否加载更多，不清除数据
	 */
	private boolean isFoot = false;

	@Override
	public int getContentView() {
		return R.layout.main;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initView() {
		boolean isFirstLaunch = PreferencesUtils.getBoolean(this,
				"isFirstLaunch", true);
		if (isFirstLaunch) {
			PreferencesUtils.putBoolean(this, "isFirstLaunch", false);
			startActivity(new Intent(this, WizardActivity.class));
		}

		mTimer = new Timer();
		mNavigationView = (NavigationView) findViewById(R.id.navigationView);
		mNavigationView.setNavigateItemVisibility(NavigationView.TITLE,
				View.GONE);
		store_listView = (PullToRefreshListView) findViewById(R.id.store_listView);
		mListView = store_listView.getRefreshableView();

		segmentControllerView = (SegmentControllerView) findViewById(R.id.segmentView);

		segmentControllerView.init(getResources().getStringArray(
				R.array.main_segment_items_hot_desc));
		segmentControllerView.setOnChangeListener(onChangeListener);
		segmentControllerView.setPosition(0);
		segmentControllerView.draw();

		mStoreAdapter = new StoreAdapter(MainActivity.this, mSortBy);
		mListView.setAdapter(mStoreAdapter);
		store_listView.setMode(Mode.BOTH);
		store_listView.setShowIndicator(false);
		store_listView.getLoadingLayoutProxy().setLastUpdatedLabel(
				StringUtils.getString(this, R.string.last_refresh_time)
						+ TimeUtil.getCurrentTime());
		store_listView.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				// 下拉刷新
				currentPage = 1;
				loadData(mSortBy, SORT_TYPE);
				store_listView.getLoadingLayoutProxy().setLastUpdatedLabel(
						StringUtils.getString(MainActivity.this,
								R.string.last_refresh)
								+ TimeUtil.getCurrentTime());
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				// 上拉加载更多
				currentPage++;
				isFoot = true;
				loadData(mSortBy, SORT_TYPE);
				store_listView.getLoadingLayoutProxy().setLastUpdatedLabel(
						StringUtils.getString(MainActivity.this,
								R.string.last_refresh)
								+ TimeUtil.getCurrentTime());
			}
		});
	}

	@Override
	public void initData() {
		regionDao = new RegionDao(this);
		mGeoCoder = GeoCoder.newInstance();
		BDLocation location = LocationUtils.getInstance(this).getLocation();
		if (mRegion.equals(DEFAULT_REGION)) {
			if (location != null) {
				mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption()
						.location(new LatLng(location.getLatitude(), location
								.getLongitude())));
				mGeoCoder
						.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
							@Override
							public void onGetGeoCodeResult(
									GeoCodeResult geoCodeResult) {
							}

							@Override
							public void onGetReverseGeoCodeResult(
									ReverseGeoCodeResult reverseGeoCodeResult) {
								if (reverseGeoCodeResult != null
										&& reverseGeoCodeResult.error == ReverseGeoCodeResult.ERRORNO.NO_ERROR) {
									ReverseGeoCodeResult.AddressComponent component = reverseGeoCodeResult
											.getAddressDetail();
									if (!StringUtils.isBlank(component.city)
											&& component.city.length() > 2) {
										String city = component.city.substring(
												0, 2);
										mRegion = regionDao
												.getRegionByName(city).region_id;
									}
								}
							}
						});
			}
		}
//		 loadData(mSortBy,SORT_TYPE);
		showProgressDialog("正在加载...");
		mNavigationView.setNavigateItemOnClickListener(
				NavigationView.SECOND_RIGHT_TEXT_VIEW, new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (!StringUtils.isBlank(token)) {
							// 栏目导航
							mNavigationView.showCategoryView();
						} else {
							// 注册
							Intent intent = new Intent(MainActivity.this,
									RegisterActivity.class);
							startActivity(intent);
							overridePendingTransition(R.anim.translate_in,
									R.anim.translate_out);
						}
					}
				});
		mNavigationView.setNavigateItemOnClickListener(
				NavigationView.FIRST_RIGHT_TEXT_VIEW, new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (!StringUtils.isBlank(token)) {
							// 地图
							Intent intent = new Intent(MainActivity.this,
									MapActivity.class);
							intent.putExtra("SortBy", mSortBy);
							intent.putExtra("Data", mStoreAdapter.getDataList());
							intent.putExtra("Region", mRegion);
							intent.putExtra("Category", mCategory);
							intent.putExtra("Service", mService);
							startActivity(intent);
						} else {
							// 登录
							Intent intent = new Intent(MainActivity.this,
									LoginActivity.class);
							startActivity(intent);
							overridePendingTransition(R.anim.translate_in,
									R.anim.translate_out);
						}
					}
				});

		mNavigationView.setNavigateItemOnClickListener(
				NavigationView.LEFT_TEXT_VIEW, new OnClickListener() {
					@Override
					public void onClick(View v) {
						startActivity(new Intent(MainActivity.this,
								StoreCategoryActivity.class));
//						startActivityForResult(new Intent(MainActivity.this,
//								StoreCategoryActivity.class), REQUEST_CODE);
						overridePendingTransition(R.anim.translate_in,
								R.anim.translate_out);
					}
				});
	}

	private void loadData(String sortBy, String SORT_TYPE) {
		mSortBy = sortBy;
		BDLocation location = LocationUtils.getInstance(this).getLocation();
		String lat = "0";
		String lng = "0";
		if (location != null) {
			lat = String.valueOf(location.getLatitude());
			lng = String.valueOf(location.getLongitude());
		}
		// 加载主界面
		// showProgressDialog("正在加载...");
		GetStoreListAndMenusResp storeResp = new GetStoreListAndMenusResp(
				MainActivity.this, handler, mCategory, mRegion, sortBy,
				SORT_TYPE, SIZE, currentPage, MENU_COUNT, lng, lat, mService);
		storeResp.asyncInvoke(SUCCESS, FAILURE);
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (store_listView != null && store_listView.isShown()) {
				store_listView.onRefreshComplete();
			}
			switch (msg.what) {
			case FAILURE:
				isFoot = false;
				mNavigationView.showMessage(getString(R.string.data_error));
				break;
			case SUCCESS:
				List<Store> storeList = (ArrayList<Store>) msg.obj;
				if (!isFoot) {
					mStoreAdapter.getDataList().clear();
					mStoreAdapter.getDataList().addAll(storeList);
					mStoreAdapter.notifyDataSetChanged();
					if (isTransition) {
						mListView.setSelection(0);
						isTransition = false;
					}
				} else {
					mStoreAdapter.getDataList().addAll(storeList);
					mStoreAdapter.notifyDataSetChanged();
					if (isTransition) {
						mListView.setSelection(0);
						isTransition = false;
					}
				}
				isFoot = false;
				if(!StringUtils.isBlank(token)){
					uploadAdapter();
				}
				break;
			case GET_CART_SUCCESS:
				Cart cart = (Cart) msg.obj;
				if (cart != null && cart.menuList.size() > 0) {
					Map<String, Cart> cartMap = new HashMap<String, Cart>();
					cartMap.put(cart.storeInfo.id, cart);
					application.setCartMap(cartMap);
				}
				break;
			case GET_CART_FAILED:
				break;
			default:
				break;
			}
			closeProgressDialog();
		}
	};

	/**
	 * 获取该用户的购物车中的内容存储在本地
	 */
	private void uploadAdapter() {
		if (!StringUtils.isBlank(PreferencesUtils.getString(this, "member_id"))) {
			application = (MyApplication) getApplicationContext();
			String memberId = PreferencesUtils.getString(this, "member_id");
			String token = PreferencesUtils.getString(this, "token");
			GetCartResp getCartResp = new GetCartResp(context, handler, token,
					memberId);
			getCartResp.asyncInvoke(GET_CART_SUCCESS, GET_CART_FAILED);
		}
	}

	protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);// must store the new intent unless getIntent() will
                          // return the old one
    }
	
	@Override
	protected void onResume() {
		super.onResume();
		mNavigationView.setBackgroundColor(Color.parseColor(colorValue));
		segmentControllerView.setColor(Color.parseColor(colorValue));
		if (!StringUtils.isBlank(token)) {
			mNavigationView.setNavigateItemText(NavigationView.LEFT_TEXT_VIEW,
					R.string.store);
			mNavigationView.setNavigateItemText(
					NavigationView.FIRST_RIGHT_TEXT_VIEW, "");
			mNavigationView.setNavigateItemText(
					NavigationView.SECOND_RIGHT_TEXT_VIEW, "");
			mNavigationView.setNavigateItemBackground(
					NavigationView.FIRST_RIGHT_TEXT_VIEW, R.drawable.mymap);
			mNavigationView.setNavigateItemBackground(
					NavigationView.SECOND_RIGHT_TEXT_VIEW, R.drawable.navi);
		} else {
			mNavigationView.setNavigateItemText(NavigationView.LEFT_TEXT_VIEW,
					R.string.store);
			mNavigationView.setNavigateItemText(
					NavigationView.FIRST_RIGHT_TEXT_VIEW, R.string.login);
			mNavigationView.setNavigateItemText(
					NavigationView.SECOND_RIGHT_TEXT_VIEW, R.string.register);
			mNavigationView.setNavigateItemBackground(
					NavigationView.FIRST_RIGHT_TEXT_VIEW, null);
			mNavigationView.setNavigateItemBackground(
					NavigationView.SECOND_RIGHT_TEXT_VIEW, null);
		}
		//选取了商家分类之后
		Intent intent =getIntent();
		String sourceActivity =intent.getStringExtra("sourceActivity");
		if("StoreCategoryActivity".equals(sourceActivity)){
			Intent data =getIntent();
			mCategory = data.getStringExtra("Category");
			mRegion = data.getStringExtra("Region");
			mService = data.getStringExtra("Service");
			if (StringUtils.isBlank(mCategory)) {
				mCategory = defaultCategory;
			}
			if (StringUtils.isBlank(mRegion)) {
				mRegion = defaultRegion;
			}
			if (StringUtils.isBlank(mService)) {
				mService = defaultService;
			}
			currentPage = 1;
			loadData(mSortBy, SORT_TYPE);
			showProgressDialog(StringUtils.getString(MainActivity.this,
					R.string.loading));
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			mCategory = data.getStringExtra("Category");
			mRegion = data.getStringExtra("Region");
			mService = data.getStringExtra("Service");
			if (StringUtils.isBlank(mCategory)) {
				mCategory = defaultCategory;
			}
			if (StringUtils.isBlank(mRegion)) {
				mRegion = defaultRegion;
			}
			if (StringUtils.isBlank(mService)) {
				mService = defaultService;
			}
			currentPage = 1;
			loadData(mSortBy, SORT_TYPE);
			showProgressDialog(StringUtils.getString(MainActivity.this,
					R.string.loading));
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onBackPressed() {
		if (!isQuit) {
			isQuit = true;
			prompt(getString(R.string.quit_app));
			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					isQuit = false;
				}
			};
			mTimer.schedule(task, 2000);
		} else {
			SQLiteManager.getInstance(this).release();
			LocationUtils.getInstance(this).stop();
			ShareSDK.stopSDK(this);
			finish();
			System.exit(0);
		}
	}

	/**
	 * 判断当前网络是否是wifi网络
	 * if(activeNetInfo.getType()==ConnectivityManager.TYPE_MOBILE) { //判断3G网
	 * 
	 * @param context
	 * @return boolean
	 */
	public static boolean isWifi(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	private SegmentControllerView.OnChangeListener onChangeListener = new SegmentControllerView.OnChangeListener() {
		@Override
		public void onChange() {
			switch (segmentControllerView.getPosition()) {
			case 0:// 热门
				if (mSortBy == POPULAR) {
					if ("ASC".equals(HOT_SORT_TYPE)) {
						HOT_SORT_TYPE = "DESC";
					} else {
						HOT_SORT_TYPE = "ASC";
					}
				}
				if ("DESC".equals(HOT_SORT_TYPE)) {
					segmentControllerView
							.setText(getResources().getStringArray(
									R.array.main_segment_items_hot_asc));
				} else {
					segmentControllerView
							.setText(getResources().getStringArray(
									R.array.main_segment_items_hot_desc));
				}
				SORT_TYPE = HOT_SORT_TYPE;
				isTransition = true;
				mStoreAdapter.getDataList().clear();
				currentPage = 1;
				loadData(POPULAR, SORT_TYPE);
				mStoreAdapter.sortType = POPULAR;
				showProgressDialog(StringUtils.getString(MainActivity.this,
						R.string.loading));
				break;
			case 1:// 价格
				if (mSortBy == PRICE) {
					if ("ASC".equals(PRICE_SORT_TYPE)) {
						PRICE_SORT_TYPE = "DESC";
					} else {
						PRICE_SORT_TYPE = "ASC";
					}
				}
				if ("DESC".equals(PRICE_SORT_TYPE)) {
					segmentControllerView.setText(getResources()
							.getStringArray(
									R.array.main_segment_items_price_asc));
				} else {
					segmentControllerView.setText(getResources()
							.getStringArray(
									R.array.main_segment_items_price_desc));
				}
				SORT_TYPE = PRICE_SORT_TYPE;
				isTransition = true;
				mStoreAdapter.getDataList().clear();
				currentPage = 1;
				loadData(PRICE, SORT_TYPE);
				mStoreAdapter.sortType = PRICE;
				showProgressDialog(StringUtils.getString(MainActivity.this,
						R.string.loading));
				break;
			case 2:// 距离
				if (mSortBy == DISTANCE) {
					if ("ASC".equals(DISTANCE_SORT_TYPE)) {
						DISTANCE_SORT_TYPE = "DESC";
					} else {
						DISTANCE_SORT_TYPE = "ASC";
					}
				}
				if ("DESC".equals(DISTANCE_SORT_TYPE)) {
					segmentControllerView.setText(getResources()
							.getStringArray(
									R.array.main_segment_items_distance_asc));
				} else {
					segmentControllerView.setText(getResources()
							.getStringArray(
									R.array.main_segment_items_distance_desc));
				}
				SORT_TYPE = DISTANCE_SORT_TYPE;
				isTransition = true;
				mStoreAdapter.getDataList().clear();
				currentPage = 1;
				loadData(DISTANCE, SORT_TYPE);
				mStoreAdapter.sortType = DISTANCE;
				showProgressDialog(StringUtils.getString(MainActivity.this,
						R.string.loading));
				break;
			}
		}
	};
}
