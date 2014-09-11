package com.huassit.imenu.android.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.MyApplication;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.adapter.OrderListAdapter;
import com.huassit.imenu.android.biz.GetOrderListResp;
import com.huassit.imenu.android.model.Member;
import com.huassit.imenu.android.model.OrderDate;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.util.TimeUtil;
import com.huassit.imenu.android.util.Utility;
import com.huassit.imenu.android.view.NavigationView;
import com.huassit.imenu.android.view.SegmentControllerView;
import com.huassit.imenu.android.view.SegmentControllerView.OnChangeListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 订单界面
 * 
 * @author shang_guan
 */
public class OrderActivity extends BaseActivity {

	/**
	 * 10-待付款 20-已支付 21-已支付未消费 22-已支付已消费 30-退款 31-退款中 32-已退款 40-到店支付 41-到店支付未消费
	 * 42-到店支付已消费 50-过期 51-代付款过期 52-已付款过期 60-未签约
	 */
	private int orderType;
	/** 订单类型 */
	// private int spendStatus;
	/***************** spendStatus订单类型 *********************/
	/** 待付款订单 */
	public static final int PENDING_ORDER = 10;
	/** 已付款订单 */
	public static final int PAYMENT_ORDER = 20;
	/** 到点支付订单 */
	public static final int CONSUME_ORDER = 40;
	/***************** orderType订单状态 *********************/
	/** 退款 */
	public static final int REFUND = 30;
	/** 退款中 */
	public static final int REFUND_OF = 31;
	/** 已退款 */
	public static final int REFUNDED = 32;
	/** 已支付未消费 */
	public static final int PAY_NOT_SPEND = 21;
	/** 已支付已消费 */
	public static final int PAY_SPEND = 22;
	/** 到店支付未消费 */
	public static final int CONSUME_NOT_SPEND = 41;
	/** 到点支付已消费 */
	public static final int CONSUME_SPEND = 42;
	/********************************************************/
	private NavigationView navigationView;
	/** listView */
	private ExpandableListView order_list;
	/** 用户或商家头像展示 */
	private ImageView order_icon_img;
	/** 用户或商家名称显示 */
	private TextView order_member_name;
	/** 已付款订单进入显示订单分类 */
	// private LinearLayout order_type;
	/** 请求数量 */
	private int count = Integer.MAX_VALUE;
	/** 请求页数 */
	private int page = 1;
	/** 适配器 */
	private OrderListAdapter mAdapter;
	private ImageLoader imageLoader;
	DisplayImageOptions options;
	// private TextView refund;
	/** 标石退款按钮现状态，1-退款，2-已消费 */
	// private int buttonCode = 1;
	private SegmentControllerView segmentControllerView;
	private List<OrderDate> mDatesList;
	/** 退款请求成功标石 */
	private static final int REFUND_SUCCESS = 200;
	/** 退款请求失败标石 */
	private static final int REFUND_FALSE = 201;
	/** 存储退款中的列表数据 */
	private List<OrderDate> mDates;
	private PullToRefreshScrollView mPullRefreshScrollView;

	@Override
	public int getContentView() {
		return R.layout.order_list;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initView() {
		orderType = getIntent().getExtras().getInt("orderType");
		// spendStatus = orderType;
		getOrderType(orderType);
		imageLoader = ImageLoader.getInstance();
		options = MyApplication.getDisplayImageOptions(context, 40);
		order_list = (ExpandableListView) findViewById(R.id.order_list);
		order_list.setGroupIndicator(null);
		order_icon_img = (ImageView) findViewById(R.id.order_icon_img);
		order_member_name = (TextView) findViewById(R.id.order_member_name);
		navigationView = (NavigationView) findViewById(R.id.navigationView);
		navigationView
				.setNavigateItemText(NavigationView.TITLE, getTitleType());
		navigationView.setNavigateItemVisibility(
				NavigationView.LEFT_IMAGE_VIEW, View.GONE);
		navigationView.setNavigateItemVisibility(
				NavigationView.FIRST_RIGHT_TEXT_VIEW, View.GONE);
		navigationView.setNavigateItemBackground(
				NavigationView.SECOND_RIGHT_TEXT_VIEW, R.drawable.navi);
		navigationView.setNavigateItemBackground(NavigationView.LEFT_TEXT_VIEW,
				R.drawable.back);
		navigationView.setCurrentCategoryTriangle(NavigationView.TRIANGLE_MINE);
		navigationView.setNavigateItemOnClickListener(
				NavigationView.LEFT_TEXT_VIEW, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		navigationView.setNavigateItemOnClickListener(
				NavigationView.SECOND_RIGHT_TEXT_VIEW,
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						navigationView.showCategoryView();
					}
				});

		segmentControllerView = (SegmentControllerView) findViewById(R.id.segmentView);
		segmentControllerView.setOnChangeListener(new OnChangeListener() {

			@Override
			public void onChange() {

			}
		});
		mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.refreshScrollView);
		mPullRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(
				"上次刷新时间：" + TimeUtil.getCurrentTime());
		mPullRefreshScrollView
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {

					@Override
					public void onRefresh(PullToRefreshBase refreshView) {
						initData();
						mPullRefreshScrollView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(
										StringUtils.getString(
												OrderActivity.this,
												R.string.last_refresh_time)
												+ TimeUtil.getCurrentTime());
					}
				});
		if (orderType == CONSUME_ORDER || orderType == CONSUME_NOT_SPEND) {
			segmentControllerView.setVisibility(View.VISIBLE);
			segmentControllerView.init(getResources().getStringArray(
					R.array.consume_order_segment_items));
			segmentControllerView.setOnChangeListener(onChangeListener2);
		}
		if (orderType == PAYMENT_ORDER || orderType == PAY_NOT_SPEND
				|| orderType == PAY_SPEND) {
			segmentControllerView.setVisibility(View.VISIBLE);
			segmentControllerView.init(getResources().getStringArray(
					R.array.order_segment_items));
			segmentControllerView.setOnChangeListener(onChangeListener);
		}
		segmentControllerView.setPosition(0);
		segmentControllerView.draw();

		mDatesList = new ArrayList<OrderDate>();
		mAdapter = new OrderListAdapter(this);
		order_list.setAdapter(mAdapter);
	}

	@Override
	public void initData() {
		UploadAdapter();
	}

	/** 请求订单列表 */
	private void UploadAdapter() {
		showProgressDialog(StringUtils.getString(this, R.string.loading));
		String member_id = PreferencesUtils.getString(this, "member_id");
		GetOrderListResp mGetOrderListResp = new GetOrderListResp(this,
				handler, token, member_id, count + "", page + "", TimeUtil.MONTH
						+ "", orderType + "");
		mGetOrderListResp.asyncInvoke(SUCCESS, FAILURE);
	}

	private void UploadRefundAdapter() {
		String member_id = PreferencesUtils.getString(this, "member_id");
		GetOrderListResp mGetOrderListResp = new GetOrderListResp(this,
				handler, token, member_id, count + "", page + "", TimeUtil.MONTH
						+ "", orderType + "");
		mGetOrderListResp.asyncInvoke(REFUND_SUCCESS, REFUND_FALSE);
	}

	@SuppressLint("HandlerLeak")
	@SuppressWarnings("unchecked")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			closeProgressDialog();
			mPullRefreshScrollView.onRefreshComplete();
			switch (msg.what) {
			case SUCCESS:
				if (msg.obj != null) {
					Map<String, Object> map = (Map<String, Object>) msg.obj;
					mDatesList = (List<OrderDate>) map.get("filter_date_list");
					Member member = (Member) map.get("member_info");
					showMember(member);
					mAdapter.getDataList().clear();
					mAdapter.getDataList().addAll(mDatesList);
					mAdapter.notifyDataSetChanged();
					openParentView();
					Utility.setListViewHeightBasedOnChildren(order_list);
					handler.post(new Runnable() {
						@Override
						public void run() {
							if (page == 1) {
								mPullRefreshScrollView.getRefreshableView()
										.fullScroll(ScrollView.FOCUS_UP);
							}
						}
					});
				}
				break;
			case FAILURE:
				break;
			case REFUND_SUCCESS:
				Map<String, Object> map = (Map<String, Object>) msg.obj;
				List<OrderDate> mDatesList = (List<OrderDate>) map
						.get("filter_date_list");
				Member member = (Member) map.get("member_info");
				showMember(member);
				if (orderType == REFUNDED) {
					mAdapter.getDataList().clear();
					mAdapter.getDataList().addAll(
							getRefundList(mDates, mDatesList));
					mAdapter.notifyDataSetChanged();
					openParentView();
				} else {
					mDates = new ArrayList<OrderDate>();
					mDates = mDatesList;
					orderType = REFUNDED;
					UploadRefundAdapter();
				}
				break;
			case REFUND_FALSE:

				break;
			}
		}
	};

	public int getOrderType() {
		return orderType;
	}

	/***
	 * 获取退款列表
	 * 
	 * @param refund
	 *            -退款中
	 * @param refundEnd
	 *            -已退款
	 */
	protected List<OrderDate> getRefundList(List<OrderDate> refund,
			List<OrderDate> refundEnd) {
		if (refund.size() < 0 && refundEnd.size() < 0) {
			return new ArrayList<OrderDate>();
		}
		if (refund.size() > 0) {
			int size = refund.size();
			for (int i = 0; i < size; i++) {
				if (refundEnd.size() > 0) {
					if (refund.get(i).key.equals(refundEnd.get(i).key)) {
						refund.get(i).mOrderLists
								.addAll(refundEnd.get(i).mOrderLists);
					} else {
						refund.add(refundEnd.get(i));
					}
					return refund;
				} else {
					return refund;
				}
			}
		} else {
			if (refundEnd.size() > 0) {
				return refundEnd;
			}
		}
		return new ArrayList<OrderDate>();
	}

	protected void onActivityResult(int requestCode, int resultCode,
			android.content.Intent data) {
		if (requestCode == 0) {
			initData();
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		navigationView.setBackgroundColor(Color.parseColor(colorValue));
	}

	/** 显示标题内容 */
	private String getTitleType() {
		String text = "";
		switch (orderType) {
		case PENDING_ORDER:
			text = StringUtils.getString(this, R.string.pending_order);
			break;
		case PAYMENT_ORDER:
		case PAY_NOT_SPEND:
			text = StringUtils.getString(this, R.string.payment_order);
			break;
		case CONSUME_ORDER:
		case CONSUME_NOT_SPEND:
			text = StringUtils.getString(this, R.string.consume_order);
			break;
		}
		return text;
	}

	/** 显示个人信息 */
	private void showMember(Member member) {
		imageLoader.displayImage(member.iconLocation + member.iconName,
				order_icon_img, options);
		order_member_name.setText(member.memberName);
	}

	/** 设置默认ExpandableListView每个父项是展开状态 */
	public void openParentView() {
		if (mAdapter.getGroupCount() > 0) {
			for (int i = 0; i < mAdapter.getGroupCount(); i++) {
				order_list.expandGroup(i);
			}
		}
	}

	/** 已付款订单 */
	private SegmentControllerView.OnChangeListener onChangeListener = new SegmentControllerView.OnChangeListener() {
		@Override
		public void onChange() {
			switch (segmentControllerView.getPosition()) {
			case 0:// 未消费
				orderType = PAY_NOT_SPEND;
				initData();
				break;
			case 1:// 已消费
				orderType = PAY_SPEND;
				initData();
				break;
			case 2:// 退款
				orderType = REFUND;
				initData();
				// UploadRefundAdapter();
				break;
			}
		}
	};

	/** 到点支付订单 */
	private SegmentControllerView.OnChangeListener onChangeListener2 = new SegmentControllerView.OnChangeListener() {
		@Override
		public void onChange() {
			switch (segmentControllerView.getPosition()) {
			case 0:// 未消费
				orderType = CONSUME_NOT_SPEND;
				initData();
				break;
			case 1:// 已消费
				orderType = CONSUME_SPEND;
				initData();
				break;
			}
		}
	};

	/** 处理刚一进来时的orderType类型 */
	private void getOrderType(int type) {
		switch (type) {
		case PENDING_ORDER:
			orderType = PENDING_ORDER;
			break;
		case PAYMENT_ORDER:
			orderType = PAY_NOT_SPEND;
			break;
		case CONSUME_ORDER:
			orderType = CONSUME_NOT_SPEND;
			break;
		}
	}
}
