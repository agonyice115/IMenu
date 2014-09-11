package com.huassit.imenu.android.ui;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.MyApplication;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.adapter.DynamicAdapter;
import com.huassit.imenu.android.biz.GetFansOrFollowListlResp;
import com.huassit.imenu.android.biz.GetFriendsDynamicListResp;
import com.huassit.imenu.android.biz.GetRecommandDynamicListResp;
import com.huassit.imenu.android.biz.GetStoreDynamicListResp;
import com.huassit.imenu.android.biz.HaveNewDynamicMessageResp;
import com.huassit.imenu.android.model.DynamicMessageList;
import com.huassit.imenu.android.model.Fans;
import com.huassit.imenu.android.model.FilterDate;
import com.huassit.imenu.android.model.FollowAndFans;
import com.huassit.imenu.android.model.Member;
import com.huassit.imenu.android.model.Store;
import com.huassit.imenu.android.util.LocationUtils;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.util.TimeUtil;
import com.huassit.imenu.android.util.Utility;
import com.huassit.imenu.android.view.NavigationView;
import com.huassit.imenu.android.view.SegmentControllerView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DynamicActivity extends BaseActivity implements OnClickListener {

	private static final int GET_MEMBER_DETAIL_SUCCESS = 300;
	/**
	 * 获取新动态消息成功标石
	 */
	private static final int GET_DYNAMICMESSAGE_SUCCESS = 400;
	private static final int GET_STORE_DYNAMIC_SUCCESS = 200;
	private static final int GET_STORE_DYNAMIC_FAILED = 201;
	private ImageView dynamicPic;
	private ImageView memberIcon;
	private TextView memberName;
	private ExpandableListView dynamicListView;
	private DynamicAdapter adapter;
	private SimpleExpandableListAdapter adapter2;
	private NavigationView mNavigationView;
	private ImageLoader imageLoader;
	private DisplayImageOptions optionsSmall;
	private DisplayImageOptions options_small;
	private String memberId;
	private SegmentControllerView segmentControllerView;
	/**
	 * 周状态
	 */
	private static final String WEEK = "3";
	/**
	 * 月状态
	 */
	private static final String MONTH = "2";
	/**
	 * 年状态
	 */
	private static final String YEAR = "1";
	/**
	 * 时间的种类
	 */
	private String sortBy = WEEK;
	/**
	 * 父项数据源
	 */
	private List<FilterDate> mFilterDatesList;
	/**
	 * popupWindow
	 */
	private PopupWindow popupWindow;
	/**
	 * 已发布或未发布按钮
	 */
	private SegmentControllerView segmentView_dynamic_type;
	// private TextView dynamic_type;
	/**
	 * 存储动态状态
	 */
	private String dynamicType = "1";
	/**
	 * 加载全城-000、朋友-111、我的-222,商家-333,某个朋友的-444动态标石
	 */
	private int whatDynamicType = 111;
	/**
	 * 商家
	 */
	private Store store;
	/**
	 * 朋友ID
	 */
	private String view_member_id;
	/**
	 * pop window的朋友动态控件
	 */
	private TextView popwindow_friends;
	/**
	 * pop window布局
	 */
	private View view;
	/**
	 * 新动态消息提示layout
	 */
	private LinearLayout layout_newdynamic;
	/**
	 * 新动态消息数量显示
	 */
	private TextView new_dynamic_count;
	/**
	 * 请求第几页
	 */
	private int page = 1;
	/**
	 * 每页请求多少条数据
	 */
	private String pageSize = 10 + "";
	/**
	 * 刷新View
	 */
	private PullToRefreshScrollView refreshScrollView;
	/**
	 * 是否是加载更多的标石
	 */
	private boolean isPullUp = false;
	/**
	 * 新消息实体类
	 */
	private DynamicMessageList mDynamicMessageList;
	/**
	 * 新动态消息头像
	 */
	private ImageView new_dynamic_icon;
	/**
	 * 个性签名控件
	 */
	private TextView signature;
	// private List<FilterDate> mDatesList;
	private boolean isOne = false;
	private boolean is_One = false;
	/**
	 * 用于存储所有动态列表的list集合
	 */
	private List<FilterDate> totalList;
	/**
	 * 标石为进入的是我自己的动态列表，反馈给适配器，显示发布状态
	 */
	private boolean isMe = false;
	/** 存放资源图片bitmap */
	private Bitmap mBitmap = null;
	private BDLocation location;
	private String lat;
	private String lng;
	/** 商家距离 */
	private String distance;

	@Override
	public int getContentView() {
		return R.layout.dynamic;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initView() {
		memberId = PreferencesUtils.getString(context, "member_id");
		location = LocationUtils.getInstance(this).getLocation();
		if (location != null) {
			lat = String.valueOf(location.getLatitude());
			lng = String.valueOf(location.getLongitude());
		}
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		view = layoutInflater.inflate(R.layout.popwidow_item, null);
		popwindow_friends = (TextView) view
				.findViewById(R.id.popwindow_friends);
		imageLoader = ImageLoader.getInstance();
		optionsSmall = MyApplication.getDisplayImageOptions(context, 50);
		options_small = MyApplication.getDisplayImageOptions(context, 19);
		mNavigationView = (NavigationView) findViewById(R.id.navigationView);
		mNavigationView.setNavigateItemOnClickListener(
				NavigationView.SECOND_RIGHT_TEXT_VIEW, new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (!StringUtils.isBlank(token)) {
							// 栏目导航
							mNavigationView.showCategoryView();
						} else {
							// 注册
							Intent intent = new Intent(DynamicActivity.this,
									RegisterActivity.class);
							startActivity(intent);
						}
					}
				});
		mNavigationView.setNavigateItemOnClickListener(
				NavigationView.FIRST_RIGHT_TEXT_VIEW, new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 登录
						Intent intent = new Intent(DynamicActivity.this,
								LoginActivity.class);
						startActivity(intent);
					}
				});
		dynamicPic = (ImageView) findViewById(R.id.dynamicPic);
		memberIcon = (ImageView) findViewById(R.id.memberIcon);
		memberName = (TextView) findViewById(R.id.memberName);
		signature = (TextView) findViewById(R.id.signature);
		// dynamic_type = (TextView) findViewById(R.id.dynamic_type);
		// dynamic_type.setOnClickListener(this);
		dynamicListView = (ExpandableListView) findViewById(R.id.dynamicListView);
		/** 去掉ExpandableListView 默认的箭头 */
		dynamicListView.setGroupIndicator(null);
		layout_newdynamic = (LinearLayout) findViewById(R.id.layout_newdynamic);
		layout_newdynamic.setOnClickListener(this);
		new_dynamic_count = (TextView) findViewById(R.id.new_dynamic_count);
		new_dynamic_icon = (ImageView) findViewById(R.id.new_dynamic_icon);
		refreshScrollView = (PullToRefreshScrollView) findViewById(R.id.refreshScrollView);
		refreshScrollView.setMode(Mode.BOTH);
		refreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(
				StringUtils.getString(this, R.string.last_refresh_time)
						+ TimeUtil.getCurrentTime());
		refreshScrollView
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase refreshView) {
						System.out.println("onPullDownToRefresh");
						refreshScrollView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(
										StringUtils.getString(
												DynamicActivity.this,
												R.string.last_refresh_time)
												+ TimeUtil.getCurrentTime());
						page = 1;
						isPullUp = true;
						totalList = new ArrayList<FilterDate>();
						UploadTo();
					}

					@Override
					public void onPullUpToRefresh(PullToRefreshBase refreshView) {
						System.out.println("onPullUpToRefresh");
						page++;
						isPullUp = true;
						UploadTo();
					}
				});
		segmentControllerView = (SegmentControllerView) findViewById(R.id.segmentView);

		segmentView_dynamic_type = (SegmentControllerView) findViewById(R.id.segmentView_dynamic_type);
		segmentView_dynamic_type.init(getResources().getStringArray(
				R.array.dynamic_type_segment_items));
		segmentView_dynamic_type.setOnChangeListener(onChangeListener1);
		segmentView_dynamic_type.setPosition(0);
		segmentView_dynamic_type.draw();

		totalList = new ArrayList<FilterDate>();
		adapter = new DynamicAdapter(context, sortBy, dynamicType);
		dynamicListView.setAdapter(adapter);
		if (!StringUtils.isBlank(token)) {
			UploadDynamicMessage();
		}
	}

	public String getTimeType() {
		return sortBy;
	}

	/**
	 * 获取当前是否是我自己的动态列表
	 */
	public boolean getMeDynamicType() {
		return isMe;
	}

	@Override
	public void initData() {
		if (getIntent().getSerializableExtra("store") != null) {
			/** 查看商家动态 */
			segmentControllerView.init(getResources().getStringArray(
					R.array.dynamic_segment_items));
			segmentControllerView.setOnChangeListener(onChangeListener);
			segmentControllerView.setPosition(0);
			segmentControllerView.draw();
			store = (Store) getIntent().getSerializableExtra("store");
			whatDynamicType = 333;
			dynamicPic
					.setImageBitmap(readBitmap(R.drawable.dynamic_store_top_bg));
			UploadTo();
		} else {
			dynamicPic
					.setImageBitmap(readBitmap(R.drawable.dynamic_member_top_bg));
			if (getIntent().getExtras() != null
					&& getIntent().getExtras().getInt("code") == 100) {
				/*** 查看我的动态 */
				whatDynamicType = 222;
				if (getIntent().getExtras().getString("view_member_id") != null
						&& !"".equals(getIntent().getExtras().getString(
								"view_member_id"))) {
					/*** 查看某个朋友的动态 */
					segmentControllerView.init(getResources().getStringArray(
							R.array.dynamic_segment_items));
					segmentControllerView.setOnChangeListener(onChangeListener);
					segmentControllerView.setPosition(0);
					segmentControllerView.draw();
					view_member_id = getIntent().getExtras().getString(
							"view_member_id");
					whatDynamicType = 444;
					UploadAdapter(memberId, view_member_id);
				} else {
					segmentControllerView.setVisibility(View.GONE);
					segmentView_dynamic_type.setVisibility(View.VISIBLE);
					isMe = true;
					sortBy = MONTH;
					UploadTo();
				}
			} else {
				segmentControllerView.init(getResources().getStringArray(
						R.array.dynamic_segment_items));
				segmentControllerView.setOnChangeListener(onChangeListener);
				segmentControllerView.setPosition(0);
				segmentControllerView.draw();
				UploadMine();
			}
		}
		initTopBar();
	}

	@SuppressLint("HandlerLeak")
	@SuppressWarnings("unchecked")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (popupWindow != null && popupWindow.isShowing()) {
				popupWindow.dismiss();
			}
			if (refreshScrollView != null && refreshScrollView.isShown()) {
				refreshScrollView.onRefreshComplete();
			}
			switch (msg.what) {
			case FAILURE:
				closeProgressDialog();
				break;
			case SUCCESS:
				Map<String, Object> Map = (Map<String, Object>) msg.obj;
				showPersonData(Map);
				final List<FilterDate> mFilterDatesList = (List<FilterDate>) Map
						.get("filter_date_list");
				filterList(mFilterDatesList);
				adapter.getDataList().clear();
				adapter.getDataList().addAll(totalList);
				adapter.notifyDataSetChanged();
				openParentView();
				Utility.setListViewHeightBasedOnChildren(dynamicListView);
				handler.post(new Runnable() {
					@Override
					public void run() {
						if (page == 1) {
							refreshScrollView.getRefreshableView().fullScroll(
									ScrollView.FOCUS_UP);
						}
					}
				});
				closeProgressDialog();
				break;
			case GET_STORE_DYNAMIC_SUCCESS:
				final Map<String, Object> storeDynamicMap = (Map<String, Object>) msg.obj;
				final List<FilterDate> mFilterDatesList1 = (List<FilterDate>) storeDynamicMap
						.get("filter_date_list");
				Store store = (Store) storeDynamicMap.get("view_store");
				distance = store.distance;
				imageLoader.displayImage(store.logoLocation + store.logoName,
						memberIcon, optionsSmall);
				memberName.setText(store.name);
				filterList(mFilterDatesList1);
				adapter.getDataList().clear();
				adapter.getDataList().addAll(totalList);
				adapter.notifyDataSetChanged();
				openParentView();
				Utility.setListViewHeightBasedOnChildren(dynamicListView);
				handler.post(new Runnable() {
					@Override
					public void run() {
						if (page == 1) {
							refreshScrollView.getRefreshableView().fullScroll(
									ScrollView.FOCUS_UP);
						}
					}
				});
				closeProgressDialog();
				break;
			case GET_STORE_DYNAMIC_FAILED:
				closeProgressDialog();
				break;
			case GET_MEMBER_DETAIL_SUCCESS:
				if (msg.obj != null) {
					FollowAndFans mAndFans = (FollowAndFans) msg.obj;
					if (mAndFans != null) {
						List<Fans> mFans = mAndFans.mFansList;
						if (mFans != null && mFans.size() > 0) {
							whatDynamicType = 111;
							popwindow_friends.setVisibility(View.VISIBLE);
						} else {
							whatDynamicType = 000;
							popwindow_friends.setVisibility(View.GONE);
						}
					}
					System.out.println("visibile3");
					UploadTo();
				}
				break;
			case GET_DYNAMICMESSAGE_SUCCESS:
				if (msg.obj != null) {
					mDynamicMessageList = (DynamicMessageList) msg.obj;
					if (Integer.parseInt(mDynamicMessageList.new_info.have_new) == 1) {
						// 有新消息
						showMessageData();
						layout_newdynamic.setVisibility(View.VISIBLE);
					} else {
						// 无新消息
						layout_newdynamic.setVisibility(View.GONE);
					}
				}
				break;
			}
		}
	};

	/** 获取商家距离 */
	public String getDistance() {
		return distance;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_newdynamic:
			Intent intent = new Intent();
			intent.setClass(DynamicActivity.this,
					DynamicMessageListActivity.class);
			intent.putExtra("new_count",
					Integer.parseInt(mDynamicMessageList.new_info.new_count));
			startActivity(intent);
			layout_newdynamic.setVisibility(View.GONE);
			break;
		}
	}

	/**
	 * 加载朋友圈动态
	 */
	private void UploadAdapter(String memberId, String view_member_id) {
		System.out.println("sortBy:" + sortBy);
		if (isMe) {
			sortBy = MONTH;
		}
		GetFriendsDynamicListResp mDynamicListResp = new GetFriendsDynamicListResp(
				this, handler, token, memberId, view_member_id, pageSize, page
						+ "", "3", sortBy, lng, lat, "", dynamicType);// 1-已发布，2-未发布
		mDynamicListResp.asyncInvoke(SUCCESS, FAILURE);
		showProgressDialog(StringUtils.getString(this, R.string.loading));
	}

	/**
	 * 加载全城动态
	 */
	private void UploadAdapter() {
		System.out.println("visibile5");
		GetRecommandDynamicListResp resp = new GetRecommandDynamicListResp(
				DynamicActivity.this, handler, "", memberId, pageSize, page
						+ "", "3", sortBy, lng, lat, "");
		resp.asyncInvoke(SUCCESS, FAILURE);
		showProgressDialog(StringUtils.getString(this, R.string.loading));
	}

	/**
	 * 加载商家动态
	 */
	private void UploadAdapter(Store store) {
		System.out.println("visibile6");
		GetStoreDynamicListResp resp = new GetStoreDynamicListResp(context,
				handler, token, memberId, store, pageSize, page + "", "3",
				sortBy, "", lat, lng);
		resp.asyncInvoke(GET_STORE_DYNAMIC_SUCCESS, GET_STORE_DYNAMIC_FAILED);
		showProgressDialog(StringUtils.getString(this, R.string.loading));
	}

	/**
	 * 获取个人主页，判断是否有关注的人
	 */
	private void UploadMine() {
		System.out.println("visibile7");
		GetFansOrFollowListlResp mFansListlResp = new GetFansOrFollowListlResp(
				this, handler, memberId, "", "", token, 2, memberId);// 2代表...种类标石：1/获取粉丝列表接口,2/获取关注列表接口
		mFansListlResp.asyncInvoke(GET_MEMBER_DETAIL_SUCCESS, FAILURE);
		showProgressDialog(StringUtils.getString(this, R.string.loading));
	}

	/**
	 * 获取新动态消息提示
	 */
	private void UploadDynamicMessage() {
		HaveNewDynamicMessageResp mDynamicMessageResp = new HaveNewDynamicMessageResp(
				this, handler, memberId, token);
		mDynamicMessageResp.asyncInvoke(GET_DYNAMICMESSAGE_SUCCESS, FAILURE);
	}

	/**
	 * 显示pup window对话框
	 */
	private void showPupUpWindow(View parent) {
		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		int width = windowManager.getDefaultDisplay().getWidth();
		if (popupWindow == null) {
			popupWindow = new PopupWindow(view, width / 3,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			setPopClickLinsenter(view);
		}
		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
		// int xPos = parent.getWidth();
		// System.out.println("xPos:"+xPos);
		// System.out.println("width / 6:"+width / 6);
		// System.out.println("parent.getWidth() / 5:"+parent.getWidth() / 5);
		popupWindow.showAsDropDown(parent, 5, 20);
	}

	/**
	 * 设置POP内的事件监听等
	 */
	private void setPopClickLinsenter(View view) {
		TextView popwindow_all = (TextView) view
				.findViewById(R.id.popwindow_all);
		popwindow_friends.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 朋友动态
				whatDynamicType = 111;
				mNavigationView.setNavigateItemText(NavigationView.TITLE,
						R.string.friends_activities);
				UploadAdapter(memberId, "");
			}
		});
		popwindow_all.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 全程动态
				whatDynamicType = 000;
				mNavigationView.setNavigateItemText(NavigationView.TITLE,
						R.string.all_activities);
				UploadAdapter();
			}
		});
	}

	/**
	 * 显示头部个人信息数据
	 */
	private void showPersonData(Map<String, Object> dynamicMap) {
		Member viewMember = (Member) dynamicMap.get("view_member");
		if (whatDynamicType == 444) {
			imageLoader.displayImage(viewMember.iconLocation
					+ viewMember.iconName, memberIcon, optionsSmall);
			memberName.setText(viewMember.memberName);
			signature.setText(viewMember.signature);
			if (!StringUtils.isBlank(viewMember.dynamicLocation)) {
				imageLoader.displayImage(viewMember.dynamicLocation
						+ viewMember.dynamicName, dynamicPic);
			}
		} else {
			if (!StringUtils.isBlank(PreferencesUtils.getString(context,
					"dynamicLocation"))) {
				imageLoader.displayImage(
						PreferencesUtils.getString(context, "dynamicLocation")
								+ PreferencesUtils.getString(context,
										"dynamicName"), dynamicPic);
			}
			imageLoader.displayImage(
					PreferencesUtils.getString(DynamicActivity.this,
							"iconLocation")
							+ PreferencesUtils.getString(DynamicActivity.this,
									"iconName"), memberIcon, optionsSmall);
			memberName.setText(PreferencesUtils.getString(DynamicActivity.this,
					"member_name"));
			signature.setText(PreferencesUtils.getString(DynamicActivity.this,
					"signature"));
		}
	}

	/**
	 * 显示新消息layout数据
	 */
	private void showMessageData() {
		new_dynamic_count.setText(mDynamicMessageList.new_info.new_count
				+ StringUtils.getString(this, R.string.new_message));
		imageLoader.displayImage(mDynamicMessageList.member_info.iconLocation
				+ mDynamicMessageList.member_info.iconName, new_dynamic_icon,
				options_small);
		// imageLoader.displayImage(urlString, new_dynamic_icon
		// );
	}

	/**
	 * 判断当前查看的是什么动态，及请求处理
	 */
	private void UploadTo() {
		System.out.println("whatDynamicType:" + whatDynamicType);
		switch (whatDynamicType) {
		case 000:
			// 全城动态
			System.out.println("visibile7");
			mNavigationView.setNavigateItemText(NavigationView.TITLE,
					R.string.all_activities);
			UploadAdapter();
			break;
		case 111:
			// 朋友动态
			System.out.println("visibile8");
			UploadAdapter(memberId, "");
			break;
		case 222:
			// 我的动态
			System.out.println("visibile9");
			UploadAdapter(memberId, memberId);
			break;
		case 333:
			// 商家动态
			System.out.println("visibile10");
			UploadAdapter(store);
			break;
		case 444:
			// 某个朋友的动态
			System.out.println("visibile11");
			UploadAdapter(memberId, view_member_id);
			break;
		}
	}

	/**
	 * 发布状态转换器监听
	 */
	private SegmentControllerView.OnChangeListener onChangeListener1 = new SegmentControllerView.OnChangeListener() {
		@Override
		public void onChange() {
			switch (segmentView_dynamic_type.getPosition()) {
			case 0:// 已发布
				dynamicType = "1";
				if (is_One) {
					System.out.println("visibile17");
					totalList = new ArrayList<FilterDate>();
					isMe = true;
					UploadAdapter(memberId, memberId);
				}
				is_One = true;
				break;
			case 1:// 未发布
				dynamicType = "2";
				isMe = true;
				System.out.println("visibile12");
				totalList = new ArrayList<FilterDate>();
				UploadAdapter(memberId, memberId);
				break;
			}
		}
	};

	public String getDynamicType() {
		return dynamicType;
	}

	/**
	 * 时间分类转换器监听
	 */
	private SegmentControllerView.OnChangeListener onChangeListener = new SegmentControllerView.OnChangeListener() {
		@Override
		public void onChange() {
			switch (segmentControllerView.getPosition()) {
			case 0:// 周
				sortBy = WEEK;
				if (isOne) {
					System.out.println("visibile13");
					page = 1;
					totalList = new ArrayList<FilterDate>();
					UploadTo();
				}
				isOne = true;
				break;
			case 1:// 月
				sortBy = MONTH;
				page = 1;
				totalList = new ArrayList<FilterDate>();
				UploadTo();
				break;
			case 2:// 年
				sortBy = YEAR;
				page = 1;
				totalList = new ArrayList<FilterDate>();
				UploadTo();
				break;
			}
		}
	};

	/**
	 * 加载头部方法
	 */
	private void initTopBar() {
		if (!StringUtils.isBlank(token)) {
			if (getIntent().getExtras() != null) {
				initTopMeBar();
			} else {
				initTopAllBar();
			}
		} else {
			mNavigationView.setNavigateItemBackground(
					NavigationView.LEFT_TEXT_VIEW, R.drawable.back);
			mNavigationView.setNavigateItemVisibility(
					NavigationView.LEFT_IMAGE_VIEW, View.GONE);
			mNavigationView.setNavigateItemOnClickListener(
					NavigationView.LEFT_TEXT_VIEW, new OnClickListener() {

						@Override
						public void onClick(View v) {
							finish();
						}
					});
			mNavigationView.setNavigateItemText(
					NavigationView.FIRST_RIGHT_TEXT_VIEW, R.string.login);
			mNavigationView.setNavigateItemText(
					NavigationView.SECOND_RIGHT_TEXT_VIEW, R.string.register);
			mNavigationView.setNavigateItemBackground(
					NavigationView.SECOND_RIGHT_TEXT_VIEW, null);
			mNavigationView.setNavigateItemBackground(
					NavigationView.FIRST_RIGHT_TEXT_VIEW, null);
		}
	}

	/**
	 * 加载全城及朋友动态头部
	 */
	private void initTopAllBar() {
		mNavigationView.setNavigateItemText(NavigationView.LEFT_TEXT_VIEW,
				R.string.activities);
		if (whatDynamicType == 000) {
			mNavigationView.setNavigateItemText(NavigationView.TITLE,
					R.string.all_activities);
		} else {
			mNavigationView.setNavigateItemText(NavigationView.TITLE,
					R.string.friends_activities);
		}
		mNavigationView.setNavigateItemBackground(
				NavigationView.SECOND_RIGHT_TEXT_VIEW, R.drawable.navi);
		mNavigationView.setNavigateItemOnClickListener(
				NavigationView.SECOND_RIGHT_TEXT_VIEW, new OnClickListener() {

					@Override
					public void onClick(View v) {
						mNavigationView.showCategoryView();
						mNavigationView
								.setCurrentCategoryTriangle(NavigationView.TRIANGLE_ACTIVITIES);
					}
				});
		mNavigationView.setNavigateItemOnClickListener(
				NavigationView.LEFT_TEXT_VIEW, new OnClickListener() {
					@Override
					public void onClick(View v) {
						showPupUpWindow(NavigationView.mLeftTextView);
					}
				});
	}

	/**
	 * 加载我的动态及某朋友动态
	 */
	private void initTopMeBar() {
		mNavigationView.setNavigateItemText(NavigationView.TITLE,
				R.string.activities);
		mNavigationView.setNavigateItemVisibility(
				NavigationView.LEFT_IMAGE_VIEW, View.GONE);
		mNavigationView.setNavigateItemVisibility(
				NavigationView.FIRST_RIGHT_TEXT_VIEW, View.GONE);
		mNavigationView.setNavigateItemBackground(
				NavigationView.SECOND_RIGHT_TEXT_VIEW, R.drawable.navi);
		mNavigationView.setNavigateItemBackground(
				NavigationView.LEFT_TEXT_VIEW, R.drawable.back);
		mNavigationView
				.setCurrentCategoryTriangle(NavigationView.TRIANGLE_MINE);
		mNavigationView.setNavigateItemOnClickListener(
				NavigationView.LEFT_TEXT_VIEW, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		mNavigationView.setNavigateItemOnClickListener(
				NavigationView.SECOND_RIGHT_TEXT_VIEW,
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mNavigationView.showCategoryView();
					}
				});
	}

	/**
	 * 设置默认ExpandableListView每个父项是展开状态
	 */
	private void openParentView() {
		if (adapter.getGroupCount() > 0) {
			for (int i = 0; i < adapter.getGroupCount(); i++) {
				dynamicListView.expandGroup(i);
			}
		}
	}

	/**
	 * 过滤掉父项中没有子项的list
	 */
	private void filterList(List<FilterDate> mFilterDatesList) {
		List<FilterDate> mDatesList = new ArrayList<FilterDate>();
		boolean isKey = false;
		if (totalList.size() <= 0) {
			totalList = new ArrayList<FilterDate>();
			for (int i = 0; i < mFilterDatesList.size(); i++) {
				if (mFilterDatesList.get(i).mDynamicsList != null
						&& mFilterDatesList.get(i).mDynamicsList.size() > 0) {
					totalList.add(mFilterDatesList.get(i));
				}
			}
			return;
		}
		int count = totalList.size();
		if (mFilterDatesList.size() > 0) {
			for (int i = 0; i < mFilterDatesList.size(); i++) {
				if (mFilterDatesList.get(i).mDynamicsList != null
						&& mFilterDatesList.get(i).mDynamicsList.size() > 0) {
					for (int j = 0; j < count; j++) {
						if (mFilterDatesList.get(i).key
								.equals(totalList.get(j).key)) {
							totalList.get(j).mDynamicsList
									.addAll(mFilterDatesList.get(i).mDynamicsList);
							isKey = true;
						}
					}
					if (!isKey) {
						totalList.add(mFilterDatesList.get(i));
					}
				}
			}
		}
		if (totalList.size() > 0) {
			for (int k = 0; k < totalList.size(); k++) {
				if (totalList.get(k).mDynamicsList != null
						&& totalList.get(k).mDynamicsList.size() > 0) {
					mDatesList.add(totalList.get(k));
				}
			}
		}
		totalList = mDatesList;
	}

	/** 将资源图片转换为bitmap */
	private Bitmap readBitmap(int resoureId) {
		if (mBitmap != null && !mBitmap.isRecycled()) { 
			mBitmap.recycle();
			mBitmap = null;
		}
		InputStream is = getResources().openRawResource(resoureId);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inPurgeable = true;
		options.inInputShareable = true;
		mBitmap = BitmapFactory.decodeStream(is, null, options);
		return mBitmap;
	}

	@Override
	protected void onDestroy() {
		if (mBitmap != null && !mBitmap.isRecycled()) {
			mBitmap.recycle();
			mBitmap = null;
		}
		super.onDestroy();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == 1){
			page = 1;
			UploadTo();
		}
	}
}
