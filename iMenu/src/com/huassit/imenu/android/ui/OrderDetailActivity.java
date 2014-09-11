package com.huassit.imenu.android.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.adapter.OrderedMenuAdapter;
import com.huassit.imenu.android.biz.GetOrderDetailResp;
import com.huassit.imenu.android.model.OrderInfo;
import com.huassit.imenu.android.ui.pay.OnLinePayActivity;
import com.huassit.imenu.android.util.NumberFormatUtils;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.ScreenUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.util.TimeUtil;
import com.huassit.imenu.android.util.Utility;
import com.huassit.imenu.android.view.NavigationView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * 订单详细界面
 * 
 * @author shang_guan
 */
public class OrderDetailActivity extends BaseActivity {

	/**
	 * 订单状态: 10-待付款 20-已支付 21-已支付未消费 22-已支付已消费 30-退款 31-退款中 32-已退款 40-到店支付
	 * 41-到店支付未消费 42-到店支付已消费 50-过期 51-代付款过期 52-已付款过期 60-未签约
	 */
	private int orderType;
	/***************** spendStatus订单类型 *******************/
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
	/** 过期 */
	public static final int OVERDUE = 50;
	/** 代付款过期 */
	public static final int PENDING_OVERDUE = 51;
	/** 已付款过期 */
	public static final int PAY_SPEND_OVERDUE = 52;
	/********************************************************/
	private NavigationView navigationView;
	private ImageLoader imageLoader;
	DisplayImageOptions options;
	/** 商家头像展示 */
	private ImageView order_icon_img;
	/** 商家名称显示 */
	private TextView order_member_name;
	/** 消费二维码 */
	private ImageView consume_code_iv;
	/** 消费码 */
	private TextView consume_code;
	/** 人数 */
	private TextView people;
	/** 单品数量 */
	private TextView menu_type_count;
	/** 合计 */
	private TextView total_tv;
	/** 人均消费 */
	private TextView per_people;
	/** 总积分 */
	private TextView total_score;
	/** 获得积分 */
	private TextView add_score;
	/** 扣除积分 */
	private TextView minus_score;
	/** 底部layout */
	private RelativeLayout buttom_btn_layout;
	/** 底部按钮 */
	private TextView buttom_btn;
	/** 消费码显示layout */
	private RelativeLayout consume_ercode_layout;
	/** 订单ID */
	private String order_id;
	/** 订单数据源 */
	private OrderInfo mInfo;
	// /** 菜单list */
	// private List<Menu> menusList;
	/** 动态ID */
	private String dynamic_id;
	/** 订单详情菜品列表 */
	private ListView order_detail_list;
	/** 适配器，与购物车列表使用同一个适配器 */
	private OrderedMenuAdapter menuAdapter;
	/** 退款layout */
	private LinearLayout layout_refund;
	/** 查看退款详情控件 */
	private TextView return_info;
	/** 退款状态显示空间 */
	private TextView order_type_tv;
	/** 订单有效期显示 */
	private TextView order_time;
	/** 过期订单显示空间 */
	private TextView order_status;
	/** 全局 */
	private ScrollView layout_orderdetail_scrollview;
	/** 时间小图标 */
	private ImageView time;
	/** 订单状态layout */
	private RelativeLayout order_status_layout;
	/** 独立的订单时间 */
	private TextView order_time1;
	/** 独立的订单过期状态 */
	private TextView order_status1;
	/** 申请退款时间 */
	private TextView exprie_date;

	@Override
	public int getContentView() {
		return R.layout.order_detail;
	}

	@Override
	public void initView() {
		if (getIntent().getExtras() != null) {
			// orderType = getIntent().getExtras().getInt("orderType");
			order_id = getIntent().getExtras().getString("order_id");
		}
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.displayer(
						new RoundedBitmapDisplayer(ScreenUtils.dpToPxInt(
								context, 40)))
				.showImageOnLoading(R.drawable.defult_img)
				.showImageOnFail(R.drawable.defult_img)
				.showImageForEmptyUri(R.drawable.defult_img).build();
		order_icon_img = (ImageView) findViewById(R.id.order_icon_img);
		order_status1 = (TextView) findViewById(R.id.order_status1);
		order_time1 = (TextView) findViewById(R.id.order_time1);
		order_member_name = (TextView) findViewById(R.id.order_member_name);
		consume_code_iv = (ImageView) findViewById(R.id.consume_code_iv);
		consume_code = (TextView) findViewById(R.id.consume_code);
		people = (TextView) findViewById(R.id.people);
		menu_type_count = (TextView) findViewById(R.id.menu_type_count);
		total_tv = (TextView) findViewById(R.id.total);
		per_people = (TextView) findViewById(R.id.per_people);
		total_score = (TextView) findViewById(R.id.total_score);
		add_score = (TextView) findViewById(R.id.add_score);
		exprie_date = (TextView) findViewById(R.id.exprie_date);
		minus_score = (TextView) findViewById(R.id.minus_score);
		buttom_btn = (TextView) findViewById(R.id.buttom_btn);
		buttom_btn_layout = (RelativeLayout) findViewById(R.id.buttom_btn_layout);
		consume_ercode_layout = (RelativeLayout) findViewById(R.id.consume_ercode_layout);
		layout_refund = (LinearLayout) findViewById(R.id.layout_refund);
		return_info = (TextView) findViewById(R.id.return_info);
		order_type_tv = (TextView) findViewById(R.id.order_type_tv);
		order_detail_list = (ListView) findViewById(R.id.order_detail_list);
		order_status = (TextView) findViewById(R.id.order_status);
		layout_orderdetail_scrollview = (ScrollView) findViewById(R.id.layout_orderdetail_scrollview);
		menuAdapter = new OrderedMenuAdapter(this,
				R.layout.ordered_menu_list_item, 1);
		order_detail_list.setAdapter(menuAdapter);
		order_time = (TextView) findViewById(R.id.order_time);
		time = (ImageView) findViewById(R.id.time);
		order_status_layout = (RelativeLayout) findViewById(R.id.order_status_layout);
		navigationView = (NavigationView) findViewById(R.id.navigationView);
		navigationView.setNavigateItemText(NavigationView.TITLE,
				StringUtils.getString(this, R.string.order_detail));
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
	}

	@Override
	public void initData() {
		String member_id = PreferencesUtils.getString(this, "member_id");
		GetOrderDetailResp mGetOrderDetailResp = new GetOrderDetailResp(this,
				handler, token, member_id, order_id);
		mGetOrderDetailResp.asyncInvoke(SUCCESS, FAILURE);
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SUCCESS:
				if (msg.obj != null) {
					mInfo = (OrderInfo) msg.obj;
					HideOrShow(Integer.parseInt(mInfo.order_type));
					showDetail(mInfo);
					dynamic_id = mInfo.dynamicId;
					orderType = Integer.parseInt(mInfo.order_type);
					runOnUiThread(new Runnable() {
						public void run() {
							menuAdapter.getDataList().clear();
							menuAdapter.getDataList().addAll(mInfo.menuList);
							Utility.setListViewHeightBasedOnChildren(order_detail_list);
							// menuAdapter.notifyDataSetChanged();
							order_detail_list.requestLayout();
						}
					});
				}
				break;
			case FAILURE:
				if (msg.obj != null) {
					navigationView.showErrorMessage(msg.obj.toString());
				}
				break;
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		navigationView.setBackgroundColor(Color.parseColor(colorValue));
	}

	/** 显示信息 */
	private void showDetail(OrderInfo mInfo) {
		imageLoader.displayImage(mInfo.mStoreInfo.logoLocation
				+ mInfo.mStoreInfo.logoName, order_icon_img, options);
		order_member_name.setText(mInfo.mStoreInfo.name);
		people.setText(mInfo.people
				+ StringUtils.getString(this, R.string.people));
		menu_type_count.setText(mInfo.menu_type_count
				+ StringUtils.getString(this, R.string.kind));
		Float total = Float.valueOf(mInfo.total);
		Float people = Float.parseFloat(mInfo.people);
		total_tv.setText("￥" + NumberFormatUtils.format(total));
		per_people.setText("￥" + NumberFormatUtils.format(total / people));
		total_score.setText(mInfo.mScoreInfo.total_score);
		add_score.setText("+" + mInfo.mScoreInfo.add_score);
		minus_score.setText("-" + mInfo.mScoreInfo.minus_score);
		consume_code.setText(consumeCode(mInfo.mConsumeInfo.consume_code));
		layout_orderdetail_scrollview.setVisibility(View.VISIBLE);
	}

	/** 处理控件的显示和隐藏 */
	private void HideOrShow(int type) {
		switch (type) {
		case OrderActivity.PENDING_ORDER:
			// 待付款订单,支付按钮，无消费码,监听事件跳转至支付
			order_status1.setVisibility(View.GONE);
			buttom_btn.setText(StringUtils.getString(this, R.string.go_to_pay));
			consume_ercode_layout.setVisibility(View.GONE);
			order_status_layout.setVisibility(View.VISIBLE);
			layout_refund.setVisibility(View.GONE);
			order_time1.setText(TimeUtil.FormatTime(
					mInfo.mConsumeInfo.exprie_date, "yyyy/MM/dd")
					+ StringUtils.getString(context,
							R.string.effective_pay_before));
			buttom_btn_layout.setVisibility(View.VISIBLE);
			buttom_btn_layout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, OnLinePayActivity.class);
					intent.putExtra("orderInfo", mInfo);
					startActivityForResult(intent, 0);
				}
			});
			break;
		case OrderActivity.REFUND_OF:
			// 退款中
			order_status1.setVisibility(View.GONE);
			layout_refund.setVisibility(View.VISIBLE);
			consume_ercode_layout.setVisibility(View.GONE);
			order_status_layout.setVisibility(View.GONE);
			buttom_btn_layout.setVisibility(View.GONE);
			order_type_tv.setText(StringUtils.getString(this,
					R.string.returning));
			order_type_tv.setTextColor(Color.parseColor("#ffff8800"));
			return_info.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 查看退款详情
					Intent intent = new Intent(OrderDetailActivity.this,
							OrderRefundDetailActivity.class);
					intent.putExtra("orderType", orderType);
					intent.putExtra("orderId", order_id);
					startActivity(intent);
				}
			});
			exprie_date.setText(TimeUtil.FormatTime(mInfo.order_date,
					"yyyy/MM/dd"));
			order_time1.setText(StringUtils.getString(context, R.string.at)
					+ TimeUtil.FormatTime(mInfo.order_date, "yyyy/MM/dd"
							+ StringUtils.getString(context, R.string.apply)));
			break;
		case OrderActivity.REFUNDED:
			// 退款成功
			order_status1.setVisibility(View.GONE);
			layout_refund.setVisibility(View.VISIBLE);
			consume_ercode_layout.setVisibility(View.GONE);
			order_status_layout.setVisibility(View.GONE);
			buttom_btn_layout.setVisibility(View.GONE);
			order_type_tv.setText(StringUtils
					.getString(this, R.string.returned));
			order_type_tv.setTextColor(Color.parseColor("#339933"));
			return_info.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 查看退款详情
					Intent intent = new Intent(OrderDetailActivity.this,
							OrderRefundDetailActivity.class);
					intent.putExtra("orderType", orderType);
					intent.putExtra("orderId", order_id);
					startActivity(intent);
				}
			});
			exprie_date.setText(TimeUtil.FormatTime(mInfo.order_date,
					"yyyy/MM/dd"));
			order_time1.setText(StringUtils.getString(context, R.string.at)
					+ TimeUtil.FormatTime(mInfo.order_date, "yyyy/MM/dd"
							+ StringUtils.getString(context, R.string.apply)));
			break;
		case OVERDUE:// 过期
		case PENDING_OVERDUE:
			// 待付款过期
			order_time1.setText(TimeUtil.FormatTime(
					mInfo.mConsumeInfo.exprie_date, "yyyy/MM/dd")
					+ StringUtils.getString(context,
							R.string.effective_pay_before));
			order_status1.setVisibility(View.VISIBLE);
			buttom_btn.setVisibility(View.GONE);
			layout_refund.setVisibility(View.GONE);
			consume_ercode_layout.setVisibility(View.GONE);
			order_status_layout.setVisibility(View.VISIBLE);
			break;
		case PAY_SPEND_OVERDUE:
		case OrderActivity.PAY_NOT_SPEND:
			// 已支付未消费,监听事件跳转至退款界面
			order_status.setVisibility(View.GONE);
			order_time
					.setText(TimeUtil.FormatTime(
							mInfo.mConsumeInfo.exprie_date, "yyyy/MM/dd")
							+ StringUtils.getString(context,
									R.string.effective_before));
			if (type == PAY_SPEND_OVERDUE) {
				order_status.setVisibility(View.VISIBLE);
			}
			buttom_btn.setText(StringUtils.getString(this,
					R.string.apply_return));
			consume_ercode_layout.setVisibility(View.VISIBLE);
			order_status_layout.setVisibility(View.GONE);
			layout_refund.setVisibility(View.GONE);
			buttom_btn_layout.setVisibility(View.VISIBLE);
			buttom_btn_layout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(OrderDetailActivity.this,
							ApplyReturnActivity.class);
					intent.putExtra("orderInfo", mInfo);
					startActivity(intent);
				}
			});
			break;
		case OrderActivity.PAY_SPEND:
			// 已支付已消费,监听事件跳转至发动态
			order_status.setVisibility(View.GONE);
			order_time.setText(StringUtils.getString(context,
					R.string.consumed_at)
					+ TimeUtil.FormatTime(mInfo.order_date, "yyyy/MM/dd"));
			buttom_btn.setText(StringUtils
					.getString(this, R.string.add_dynamic));
			layout_refund.setVisibility(View.GONE);
			order_status_layout.setVisibility(View.GONE);
			consume_ercode_layout.setVisibility(View.VISIBLE);
			buttom_btn_layout.setVisibility(View.VISIBLE);
			buttom_btn_layout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(OrderDetailActivity.this,
							AddDynamicActivity.class);
					intent.putExtra("dynamic_id", dynamic_id);
					startActivity(intent);
				}
			});
			break;
		case OrderActivity.CONSUME_NOT_SPEND:
			// 到店支付未消费,无监听事件
			order_status.setVisibility(View.GONE);
			layout_refund.setVisibility(View.GONE);
			buttom_btn_layout.setVisibility(View.GONE);
			order_status_layout.setVisibility(View.GONE);
			consume_ercode_layout.setVisibility(View.VISIBLE);
			order_time
					.setText(TimeUtil.FormatTime(
							mInfo.mConsumeInfo.exprie_date, "yyyy/MM/dd")
							+ StringUtils.getString(context,
									R.string.effective_before));
			break;
		case OrderActivity.CONSUME_SPEND:
			// 到点支付已消费,监听事件跳转至发动态
			order_status.setVisibility(View.GONE);
			order_time.setText(StringUtils.getString(context,
					R.string.consumed_at)
					+ TimeUtil.FormatTime(mInfo.order_date, "yyyy/MM/dd"));
			buttom_btn.setText(StringUtils
					.getString(this, R.string.add_dynamic));
			layout_refund.setVisibility(View.GONE);
			consume_ercode_layout.setVisibility(View.VISIBLE);
			order_status_layout.setVisibility(View.GONE);
			buttom_btn_layout.setVisibility(View.VISIBLE);
			buttom_btn_layout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(OrderDetailActivity.this,
							AddDynamicActivity.class);
					intent.putExtra("dynamic_id", dynamic_id);
					startActivity(intent);
				}
			});
			break;
		}
	}

	/** 消费码每四位四位隔开 */
	private String consumeCode(String code) {
		StringBuffer str = new StringBuffer();
		if (!StringUtils.isBlank(code)) {
			for (int i = 0; i < code.length(); i++) {
				str.append(code.charAt(i));
				if (i == 3) {
					str.append(" ");
				}
				if (i == 7) {
					str.append(" ");
				}
			}
		}
		return str.toString();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0) {
			finish();
		}
	}
}
