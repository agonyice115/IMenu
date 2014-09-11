package com.huassit.imenu.android.ui;

import java.text.MessageFormat;
import java.util.ArrayList;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.MyApplication;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.adapter.FansListAdapter;
import com.huassit.imenu.android.adapter.StoreEnvironmentAdapter;
import com.huassit.imenu.android.adapter.StoreServiceAdapter;
import com.huassit.imenu.android.biz.EditFollowStatusResp;
import com.huassit.imenu.android.biz.GetStoreDetailsResp;
import com.huassit.imenu.android.db.dao.EnvironmentDao;
import com.huassit.imenu.android.db.dao.ServiceDao;
import com.huassit.imenu.android.db.dao.ShareStoreDao;
import com.huassit.imenu.android.model.Environment;
import com.huassit.imenu.android.model.Recommand;
import com.huassit.imenu.android.model.Service;
import com.huassit.imenu.android.model.Share;
import com.huassit.imenu.android.model.Store;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.util.TimeUtil;
import com.huassit.imenu.android.view.HorizontalListView;
import com.huassit.imenu.android.view.NavigationView;
import com.huassit.imenu.android.view.TriangleView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class StoreDetailActivity extends BaseActivity {

	private NavigationView mNavigationView;
	private ImageView dynamicPic;
	private ImageView storeLogo;
	private TextView storeName;
	private ImageView cashOrCredit;
	private ImageView star1;
	private ImageView star2;
	private ImageView star3;
	private ImageView star4;
	private ImageView star5;
	private TextView pricePerPerson;
	private TextView openingHour;
	private TextView isFollow;
	private TextView forsale;
	private TextView fansCount;
	private TextView browseCount;
	private TextView btn_recommand;
	private TextView btn_seeMenuList;
	private TextView address;
	private TextView phoneNumber;
	private TextView orderCount;
	private Store store;
	private ImageLoader imageLoader;
	private TextView tv_service;
	private TextView tv_environment;
	private TextView tv_activities;
	private HorizontalListView horizontalListView;
	private StoreServiceAdapter storeServiceAdapter;
	private StoreEnvironmentAdapter storeEnvironmentAdapter;
	private ServiceDao serviceDao;
	private EnvironmentDao environmentDao;
	private RelativeLayout rl_fans;
	private RelativeLayout rl_address;
	private RelativeLayout rl_phone;
	private RelativeLayout rl_orderCount;
	private TriangleView serviceTriangle;
	private TriangleView enviromentTriangle;
	private TriangleView activitiesTriangle;
	private PullToRefreshScrollView mPullRefreshScrollView;
	private ShareStoreDao shareStoreDao;
	/**
	 * 关注状态改变成功标石
	 */
	private final int CHANGEFOLLOWSUCCESS = 1000;
	private DisplayImageOptions options;

	@Override
	public int getContentView() {
		return R.layout.store_detail;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initView() {
		shareStoreDao = new ShareStoreDao(this);
		store = (Store) getIntent().getSerializableExtra("store");
		imageLoader = ImageLoader.getInstance();
		options = MyApplication.getDisplayImageOptions(context, 50);
		serviceDao = new ServiceDao(context);
		environmentDao = new EnvironmentDao(context);
		tv_service = (TextView) findViewById(R.id.service);
		tv_environment = (TextView) findViewById(R.id.environment);
		tv_activities = (TextView) findViewById(R.id.activities);
		mNavigationView = (NavigationView) findViewById(R.id.navigationView);
		mNavigationView.setNavigateItemBackground(
				NavigationView.LEFT_TEXT_VIEW, R.drawable.back);
		mNavigationView.setNavigateItemVisibility(
				NavigationView.LEFT_IMAGE_VIEW, View.GONE);
		mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.refreshScrollView);
		mPullRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(
				"上次刷新时间：" + TimeUtil.getCurrentTime());
		mPullRefreshScrollView
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {

					@Override
					public void onRefresh(PullToRefreshBase refreshView) {
						String memberId = PreferencesUtils.getString(context,
								"member_id");
						GetStoreDetailsResp resp = new GetStoreDetailsResp(
								StoreDetailActivity.this, handler, store,
								memberId, token);
						resp.asyncInvoke(SUCCESS, FAILURE);
						mPullRefreshScrollView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(
										StringUtils.getString(StoreDetailActivity.this,R.string.last_refresh_time) + TimeUtil.getCurrentTime());
					}
				});
		dynamicPic = (ImageView) findViewById(R.id.dynamic_pic);

		dynamicPic.setImageResource(R.drawable.store_dynamic_img);
		storeLogo = (ImageView) findViewById(R.id.store_logo);
		storeName = (TextView) findViewById(R.id.store_name);
		cashOrCredit = (ImageView) findViewById(R.id.cash_or_credit);
		star1 = (ImageView) findViewById(R.id.star_1);
		star2 = (ImageView) findViewById(R.id.star_2);
		star3 = (ImageView) findViewById(R.id.star_3);
		star4 = (ImageView) findViewById(R.id.star_4);
		star5 = (ImageView) findViewById(R.id.star_5);
		pricePerPerson = (TextView) findViewById(R.id.price_per_person);
		openingHour = (TextView) findViewById(R.id.opening_hour);
		forsale = (TextView) findViewById(R.id.forsale);
		// forsale1 = (TextView) findViewById(R.id.forsale1);
		// forsale2 = (TextView) findViewById(R.id.forsale2);
		isFollow = (TextView) findViewById(R.id.is_follow);
		fansCount = (TextView) findViewById(R.id.fans_count);
		browseCount = (TextView) findViewById(R.id.browse_count);
		address = (TextView) findViewById(R.id.address);
		phoneNumber = (TextView) findViewById(R.id.phone_number);
		orderCount = (TextView) findViewById(R.id.order_couunt);
		btn_recommand = (TextView) findViewById(R.id.store_recommend);
		btn_seeMenuList = (TextView) findViewById(R.id.see_menu_list);
		tv_service = (TextView) findViewById(R.id.service);
		tv_environment = (TextView) findViewById(R.id.environment);
		tv_activities = (TextView) findViewById(R.id.activities);
		horizontalListView = (HorizontalListView) findViewById(R.id.horizontallistview);
		rl_fans = (RelativeLayout) findViewById(R.id.rl_fans);
		rl_address = (RelativeLayout) findViewById(R.id.rl_address);
		rl_phone = (RelativeLayout) findViewById(R.id.rl_phone);
		rl_orderCount = (RelativeLayout) findViewById(R.id.rl_orderCount);

		serviceTriangle = (TriangleView) findViewById(R.id.service_triangle);
		enviromentTriangle = (TriangleView) findViewById(R.id.environment_triangle);
		activitiesTriangle = (TriangleView) findViewById(R.id.activities_triangle);
		btn_recommand.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ArrayList<Recommand> recommend_list = store.recommandList;
				Intent recommandIntent = new Intent(StoreDetailActivity.this,
						StoreRecommandActivity.class);
				recommandIntent.putExtra("store", store);
				recommandIntent.putExtra("recommend_list", recommend_list);
				startActivity(recommandIntent);
			}
		});
		rl_fans.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(StoreDetailActivity.this,
						FansListActivity.class);
				intent.putExtra("store_id", store.id);
				startActivity(intent);
			}
		});
		rl_address.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 跳转至地图
				Intent intent = new Intent(StoreDetailActivity.this,
						StoreMapActivity.class);
				intent.putExtra(StoreMapActivity.DATA, store);
				startActivity(intent);
			}
		});
		rl_phone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(context)
						.setMessage(StringUtils.getString(StoreDetailActivity.this,R.string.sure_to_dial) + store.tel1 + "?")
						.setPositiveButton(StringUtils.getString(StoreDetailActivity.this,R.string.confirm),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										Uri uri = Uri
												.parse("tel:" + store.tel1);
										Intent intent = new Intent(
												Intent.ACTION_CALL, uri);
										startActivity(intent);
									}
								}).setNegativeButton(StringUtils.getString(StoreDetailActivity.this,R.string.cancel), null).create()
						.show();
			}
		});
		rl_orderCount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(StoreDetailActivity.this,
						DynamicActivity.class);
				intent.putExtra("store", store);
				startActivity(intent);
			}
		});
		tv_service.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				resetTextStyleAndTriangle();
				tv_service.setText(Html.fromHtml("<b>"
						+ getString(R.string.store_service) + "</b>"));
				serviceTriangle.setVisibility(View.VISIBLE);
				loadService();
			}
		});
		tv_environment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				resetTextStyleAndTriangle();
				tv_environment.setText(Html.fromHtml("<b>"
						+ getString(R.string.store_enviroment) + "</b>"));
				enviromentTriangle.setVisibility(View.VISIBLE);
				loadEnvironment();
			}
		});
		tv_activities.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadActivities();
			}
		});
		isFollow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 关注
				ChangeStatus("", store.id, FansListAdapter
						.AddOrCancel(returnFollowStatus(store.followStatus)));
			}
		});
	}

	private void resetTextStyleAndTriangle() {
		tv_service.setText(R.string.store_service);
		tv_environment.setText(R.string.store_enviroment);
		tv_activities.setText(R.string.store_activities);
		serviceTriangle.setVisibility(View.INVISIBLE);
		enviromentTriangle.setVisibility(View.INVISIBLE);
		activitiesTriangle.setVisibility(View.INVISIBLE);
	}

	/**
	 * 请求更改关注状态
	 */
	public void ChangeStatus(String following_member_id,
			String following_store_id, String following_status) {
		String member_id = PreferencesUtils.getString(this, "member_id");
		EditFollowStatusResp mStatusResp = new EditFollowStatusResp(
				StoreDetailActivity.this, handler, token, member_id,
				following_member_id, following_store_id, following_status);
		mStatusResp.asyncInvoke(CHANGEFOLLOWSUCCESS, FAILURE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		//未登录不显示关注按钮
		String token = PreferencesUtils.getString(context, "token");
		if (!StringUtils.isBlank(token)) {
			isFollow.setVisibility(View.VISIBLE);
			mNavigationView.setNavigateItemText(
					NavigationView.FIRST_RIGHT_TEXT_VIEW, "");
			mNavigationView.setNavigateItemText(
					NavigationView.SECOND_RIGHT_TEXT_VIEW, "");
			mNavigationView.setNavigateItemText(NavigationView.TITLE, "商户");
			mNavigationView.setNavigateItemBackground(
					NavigationView.FIRST_RIGHT_TEXT_VIEW, R.drawable.share);
			mNavigationView.setNavigateItemBackground(
					NavigationView.SECOND_RIGHT_TEXT_VIEW, R.drawable.navi);
		} else {
			isFollow.setVisibility(View.GONE);
			mNavigationView.setNavigateItemText(
					NavigationView.FIRST_RIGHT_TEXT_VIEW, R.string.login);
			mNavigationView.setNavigateItemText(
					NavigationView.SECOND_RIGHT_TEXT_VIEW, R.string.register);
			mNavigationView.setNavigateItemBackground(
					NavigationView.SECOND_RIGHT_TEXT_VIEW, null);
			mNavigationView.setNavigateItemBackground(
					NavigationView.FIRST_RIGHT_TEXT_VIEW, null);
		}
		tv_activities.setBackgroundColor(Color.parseColor(colorValue));
		tv_service.setBackgroundColor(Color.parseColor(colorValue));
		tv_environment.setBackgroundColor(Color.parseColor(colorValue));
	}

	/**
	 * 根据服务id查找到完整服务信息
	 * 
	 * @param oldServiceList
	 * @return
	 */
	private ArrayList<Service> getServiceList(ArrayList<Service> oldServiceList) {
		ArrayList<Service> newServiceList = new ArrayList<Service>();
		for (int i = 0; i < oldServiceList.size(); i++) {
			Service service = serviceDao
					.getServiceById(oldServiceList.get(i).serviceId);
			newServiceList.add(service);
		}
		return newServiceList;

	}

	/**
	 * 根据环境id查找到完整环境信息
	 * 
	 * @param oldServiceList
	 * @return
	 */
	private ArrayList<Environment> getEnvironmentList(
			ArrayList<Environment> oldEnvironmentList) {
		ArrayList<Environment> newEnvironmentList = new ArrayList<Environment>();
		for (int i = 0; i < oldEnvironmentList.size(); i++) {
			Environment environment = environmentDao
					.getEnvironmentById(oldEnvironmentList.get(i).environment_id);
			newEnvironmentList.add(environment);
		}
		return newEnvironmentList;

	}

	// 加载商家服务信息
	private void loadService() {
		// tpService =tv_service.getPaint();
		// tpService.setFakeBoldText(true);
		final ArrayList<Service> serviceList = getServiceList(store.serviceList);
		storeServiceAdapter = new StoreServiceAdapter(StoreDetailActivity.this,
				R.layout.store_service_item);
		horizontalListView.setAdapter(storeServiceAdapter);

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				storeServiceAdapter.getDataList().clear();
				storeServiceAdapter.getDataList().addAll(serviceList);
				storeServiceAdapter.notifyDataSetChanged();
			}
		});
	}

	// 加载环境信息
	private void loadEnvironment() {
		final ArrayList<Environment> environmentList = getEnvironmentList(store.environmentList);
		storeEnvironmentAdapter = new StoreEnvironmentAdapter(
				StoreDetailActivity.this, R.layout.store_service_item);
		horizontalListView.setAdapter(storeEnvironmentAdapter);

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				storeEnvironmentAdapter.getDataList().addAll(environmentList);
				storeEnvironmentAdapter.notifyDataSetChanged();
			}
		});
	}

	private void loadActivities() {
//		horizontalListView.removeAllViewsInLayout();
		resetTextStyleAndTriangle();
		tv_activities.setText(Html.fromHtml("<b>"
				+ getString(R.string.store_activities) + "</b>"));
		activitiesTriangle.setVisibility(View.VISIBLE);
		final ArrayList<Environment> activityList = new ArrayList<Environment>();
		storeEnvironmentAdapter = new StoreEnvironmentAdapter(
				StoreDetailActivity.this, R.layout.store_service_item);
		horizontalListView.setAdapter(storeEnvironmentAdapter);

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				storeEnvironmentAdapter.getDataList().addAll(activityList);
				storeEnvironmentAdapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	public void initData() {
		String memberId = PreferencesUtils.getString(context, "member_id");
		GetStoreDetailsResp resp = new GetStoreDetailsResp(
				StoreDetailActivity.this, handler, store, memberId, token);
		resp.asyncInvoke(SUCCESS, FAILURE);

		btn_seeMenuList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 跳转至菜单列表
				Intent intent = new Intent(StoreDetailActivity.this,
						MenuListActivity.class);
				intent.putExtra("store", store);
				startActivity(intent);
			}
		});
		mNavigationView.setNavigateItemOnClickListener(
				NavigationView.LEFT_TEXT_VIEW, new OnClickListener() {

					@Override
					public void onClick(View v) {

						finish();
					}
				});
		mNavigationView.setNavigateItemOnClickListener(
				NavigationView.FIRST_RIGHT_TEXT_VIEW, new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (!StringUtils.isBlank(token)) {
							// 分享
							Share share = shareStoreDao.getShareStore();
							share.content = MessageFormat.format(share.content,
									store.name);
							share.imagePath = imageLoader.getDiskCache()
									.get(store.logoLocation + store.logoName)
									.getAbsolutePath();
							share.imageUrl = store.logoLocation
									+ store.logoName;
							mNavigationView.showShareView(share);
						} else {
							// 登录
							Intent intent = new Intent(
									StoreDetailActivity.this,
									LoginActivity.class);
							startActivity(intent);
						}

					}
				});
		mNavigationView.setNavigateItemOnClickListener(
				NavigationView.SECOND_RIGHT_TEXT_VIEW, new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (!StringUtils.isBlank(token)) {
							// 栏目导航
							mNavigationView.showCategoryView();
						} else {
							// 注册
							Intent intent = new Intent(
									StoreDetailActivity.this,
									RegisterActivity.class);
							startActivity(intent);
						}
					}
				});
		forsale.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mPullRefreshScrollView.setFullScrollView();
				loadActivities();
			}
		});
	}

	private void loadStore(final Store store) {
		imageLoader.displayImage(store.logoLocation + store.logoName,
				storeLogo, options);
		storeName.setText(store.name);
		if (store.signingType != null) {// 1credit 2cash 3no
			if ("1".equals(store.signingType)) {
				cashOrCredit.setImageResource(R.drawable.pay_online);
			} else {
				cashOrCredit.setImageResource(R.drawable.cash_in_shop);
			}
		}
		String starNum = store.star;
		if ("1".equals(starNum)) {
			star1.setImageResource(R.drawable.star_on);
		} else if ("2".equals(starNum)) {
			star1.setImageResource(R.drawable.star_on);
			star2.setImageResource(R.drawable.star_on);
		} else if ("3".equals(starNum)) {
			star1.setImageResource(R.drawable.star_on);
			star2.setImageResource(R.drawable.star_on);
			star3.setImageResource(R.drawable.star_on);
		} else if ("4".equals(starNum)) {
			star1.setImageResource(R.drawable.star_on);
			star2.setImageResource(R.drawable.star_on);
			star3.setImageResource(R.drawable.star_on);
			star4.setImageResource(R.drawable.star_on);
		} else if ("5".equals(starNum)) {
			star1.setImageResource(R.drawable.star_on);
			star2.setImageResource(R.drawable.star_on);
			star3.setImageResource(R.drawable.star_on);
			star4.setImageResource(R.drawable.star_on);
			star5.setImageResource(R.drawable.star_on);
		}
		pricePerPerson.setText(store.per);
		openingHour.setText(store.hours);
		// if (store.couponList != null) {
		// if ("1".equals(store.couponList.size())) {
		// forsale1.setText(store.couponList.get(0).couponTitle);
		// } else if ("1".equals(store.couponList.size())) {
		// forsale1.setText(store.couponList.get(0).couponTitle);
		// forsale2.setText(store.couponList.get(1).couponTitle);
		// }
		// }
		String followStatus = store.followStatus;// 1已关注 2互相关注 3未关注
		showFollowStatus(followStatus);
		if ("1".equals(store.couponType)) {// 促销类型(1-有促销|0-无促销)
			// forsale.setImageResource(R.drawable.for_sale);
			forsale.setVisibility(View.VISIBLE);
		}
		fansCount.setText(store.followCount);
		browseCount.setText(store.viewCount);
		address.setText(store.address);
		phoneNumber.setText(store.tel1);
		orderCount.setText(store.dynamicCount);
	}

	/**
	 * 关注状态显示
	 */
	private void showFollowStatus(String followStatus) {
		if ("1".equals(followStatus)) {
			isFollow.setText(StringUtils.getString(StoreDetailActivity.this,R.string.followed));
		} else if ("2".equals(followStatus)) {
			isFollow.setText(StringUtils.getString(StoreDetailActivity.this,R.string.follow_each_other));
		} else if ("0".equals(followStatus)) {
			isFollow.setText(StringUtils.getString(StoreDetailActivity.this,R.string.not_follow));
		}
	}

	/**
	 * 返回关注状态
	 */
	private String returnFollowStatus(String followStatus) {
		if ("1".equals(followStatus)) {
			return StringUtils.getString(StoreDetailActivity.this,R.string.followed);
		} else if ("2".equals(followStatus)) {
			return StringUtils.getString(StoreDetailActivity.this,R.string.follow_each_other);
		} else if ("0".equals(followStatus)) {
			return StringUtils.getString(StoreDetailActivity.this,R.string.not_follow);
		}
		return "";
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS:
				store = (Store) msg.obj;
				loadStore(store);
				if (store.recommandList != null
						&& store.recommandList.size() > 0) {
					btn_recommand.setVisibility(View.VISIBLE);
				}
				// loadService();
				tv_service.performClick();
				mPullRefreshScrollView.onRefreshComplete();
				break;
			case FAILURE:
				if (msg.obj != null) {
					mNavigationView.showMessage(msg.obj.toString());
					mPullRefreshScrollView.onRefreshComplete();
				}
				break;
			case CHANGEFOLLOWSUCCESS:
				if (msg.obj != null) {
					JSONObject data = (JSONObject) msg.obj;
					String follow_status = data.optString("follow_status");
					showFollowStatus(follow_status);
					store.followStatus = follow_status;
				}
				break;

			default:
				break;
			}
		}
	};
}
