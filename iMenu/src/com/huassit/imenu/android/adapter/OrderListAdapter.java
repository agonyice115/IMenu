package com.huassit.imenu.android.adapter;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.model.Dynamic;
import com.huassit.imenu.android.model.OrderDate;
import com.huassit.imenu.android.model.OrderInfo;
import com.huassit.imenu.android.ui.AddDynamicActivity;
import com.huassit.imenu.android.ui.DynamicDetailActivity;
import com.huassit.imenu.android.ui.OrderActivity;
import com.huassit.imenu.android.ui.OrderDetailActivity;
import com.huassit.imenu.android.ui.pay.OnLinePayActivity;
import com.huassit.imenu.android.util.NumberFormatUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.util.TimeUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class OrderListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private LayoutInflater inflater;
	private ImageLoader imageLoader;
	int screenWidth;
	/** 父项数据 */
	private List<OrderDate> mOrderDatesList;
	/**
	 * 订单状态: 10-待付款 20-已支付 21-已支付未消费 22-已支付已消费 30-退款 31-退款中 32-已退款 40-到店支付
	 * 41-到店支付未消费 42-到店支付已消费 50-过期 51-代付款过期 52-已付款过期 60-未签约
	 */
	private int orderType;
	/** 订单点击事件区分，1-付款，2-评价 */
	private int payOrEvaluate = 1;

	public OrderListAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		imageLoader = ImageLoader.getInstance();
		DisplayMetrics metric = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(metric);
		screenWidth = metric.widthPixels; // 屏幕宽度（像素）
		/** 从map中取出父项包含子项的集合 */
		// mOrderDatesList = (List<OrderDate>) map.get("filter_date_list");
		mOrderDatesList = new ArrayList<OrderDate>();
	}

	public List<OrderDate> getDataList() {
		return mOrderDatesList;
	}

	class ViewHolder {
		/** 订单商家lo_go */
		ImageView order_icon;
		/** 商家名称 */
		TextView store_name;
		/** 订单过期时间 */
		private TextView exprie_date;
		/** 订单总价 */
		TextView total;
		/** 订单状态 */
		private TextView order_status;
		/** 待付款全局 */
		private LinearLayout order_not_payment_list_item_layout;
		/** 待付款的付款区域 */
		private RelativeLayout order_right;
		/** 每项全局layout */
		private LinearLayout order_list_item_layout;
		/** 已付款订单未消费，过期后需显示的布局 */
		private RelativeLayout order_time_status;
		/** 订单时间 */
		private TextView order_date;
		/** 发动态,需根据动态类型的发布与未发布来判断，改变按钮背景及状态 */
		private TextView add_dynamic_status;
		/** 订单类型，退款中或退款成功，需改变字体颜色 */
		private TextView order_type;
	}

	/************************************* 子项 ***************************************/
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mOrderDatesList.get(groupPosition).mOrderLists
				.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mOrderDatesList.get(groupPosition).mOrderLists.size();
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View v, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		orderType = ((OrderActivity) context).getOrderType();
		if (mOrderDatesList != null && mOrderDatesList.size() > 0) {
			final OrderInfo mInfo = mOrderDatesList.get(groupPosition).mOrderLists
					.get(childPosition);
			switch (orderType) {
			case OrderActivity.PENDING_ORDER:
				// 待付款订单
				v = inflater
						.inflate(R.layout.order_non_payment_list_item, null);
				holder.order_time_status = (RelativeLayout) v
						.findViewById(R.id.order_time_status);
				holder.exprie_date = (TextView) v
						.findViewById(R.id.exprie_date);
				holder.order_right = (RelativeLayout) v
						.findViewById(R.id.order_right);
				holder.order_not_payment_list_item_layout = (LinearLayout) v
						.findViewById(R.id.order_not_payment_list_item_layout);
				holder.exprie_date = (TextView) v
						.findViewById(R.id.exprie_date);
				holder.order_icon = (ImageView) v.findViewById(R.id.order_icon);
				holder.store_name = (TextView) v.findViewById(R.id.store_name);
				holder.total = (TextView) v.findViewById(R.id.total);
				// 数据
				imageLoader.displayImage(mInfo.mStoreInfo.logoLocation
						+ mInfo.mStoreInfo.logoName, holder.order_icon);
				holder.store_name.setText(mInfo.store_name);
				String total = NumberFormatUtils.format(Float
						.parseFloat(mInfo.total));
				holder.total.setText("￥" + total);
				holder.exprie_date.setText(TimeUtil.FormatTime(
						mInfo.mConsumeInfo.exprie_date, "yyyy/MM/dd")
						+ StringUtils.getString(context,
								R.string.effective_pay_before));
				holder.order_right.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 直接付款
						Intent intent = new Intent(context,
								OnLinePayActivity.class);
						intent.putExtra("orderInfo", mInfo);
						((OrderActivity) context).startActivityForResult(
								intent, 0);
					}
				});
				if (!mInfo.order_type.equals("10")) {
					holder.order_right.setVisibility(View.INVISIBLE);
					holder.order_time_status.setVisibility(View.VISIBLE);
				}
				holder.order_not_payment_list_item_layout
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent intent = new Intent(context,
										OrderDetailActivity.class);
								intent.putExtra("order_id", mInfo.order_id);
								context.startActivity(intent);
							}
						});
				break;
			case OrderActivity.REFUND_OF:
			case OrderActivity.REFUNDED:
			case OrderActivity.REFUND:
				// 已支付退款
				v = inflater.inflate(R.layout.order_patment_refund_list_item,
						null);
				holder.order_list_item_layout = (LinearLayout) v
						.findViewById(R.id.order_list_item_layout);
				holder.order_date = (TextView) v.findViewById(R.id.order_date);
				holder.order_type = (TextView) v.findViewById(R.id.order_type);
				holder.order_icon = (ImageView) v.findViewById(R.id.order_icon);
				holder.store_name = (TextView) v.findViewById(R.id.store_name);
				holder.total = (TextView) v.findViewById(R.id.total);
				// 数据
				imageLoader.displayImage(mInfo.mStoreInfo.logoLocation
						+ mInfo.mStoreInfo.logoName, holder.order_icon);
				holder.store_name.setText(mInfo.store_name);
				String total1 = NumberFormatUtils.format(Float
						.parseFloat(mInfo.total));
				holder.total.setText("￥" + total1);
				holder.order_list_item_layout
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent intent = new Intent(context,
										OrderDetailActivity.class);
								intent.putExtra("order_id", mInfo.order_id);
								context.startActivity(intent);
							}
						});
				if (mInfo.order_type.equals("31")) {
					holder.order_date.setText(StringUtils.getString(context,
							R.string.at)
							+ TimeUtil.FormatTime(mInfo.order_date,
									"yyyy/MM/dd")
							+ StringUtils.getString(context, R.string.apply));
					holder.order_type.setText(StringUtils.getString(context,
							R.string.returning));
					holder.order_type.setTextColor(Color.parseColor("#cc3300"));
				}
				if (mInfo.order_type.equals("32")) {
					holder.order_date.setText(StringUtils.getString(context,
							R.string.at)
							+ TimeUtil.FormatTime(mInfo.order_date,
									"yyyy/MM/dd"));
					holder.order_type.setText(StringUtils.getString(context,
							R.string.return_success));
					holder.order_type.setTextColor(Color.parseColor("#339933"));
				}
				break;
			case OrderActivity.PAY_NOT_SPEND:
				// 已支付未消费
				v = inflater.inflate(
						R.layout.order_payment_not_spend_list_item, null);
				holder.exprie_date = (TextView) v
						.findViewById(R.id.exprie_date);
				holder.order_time_status = (RelativeLayout) v
						.findViewById(R.id.order_time_status);
				holder.order_list_item_layout = (LinearLayout) v
						.findViewById(R.id.order_list_item_layout);
				holder.order_icon = (ImageView) v.findViewById(R.id.order_icon);
				holder.store_name = (TextView) v.findViewById(R.id.store_name);
				holder.total = (TextView) v.findViewById(R.id.total);
				// 数据
				imageLoader.displayImage(mInfo.mStoreInfo.logoLocation
						+ mInfo.mStoreInfo.logoName, holder.order_icon);
				holder.store_name.setText(mInfo.store_name);
				String total2 = NumberFormatUtils.format(Float
						.parseFloat(mInfo.total));
				holder.total.setText("￥" + total2);
				holder.order_list_item_layout
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent intent = new Intent(context,
										OrderDetailActivity.class);
								intent.putExtra("order_id", mInfo.order_id);
								context.startActivity(intent);
							}
						});
				holder.exprie_date.setText(TimeUtil.FormatTime(
						mInfo.mConsumeInfo.exprie_date, "yyyy/MM/dd")
						+ StringUtils.getString(context,
								R.string.effective_before));
				if (!mInfo.order_type.equals("21")) {
					holder.order_time_status.setVisibility(View.VISIBLE);
				}
				break;
			case OrderActivity.PAY_SPEND:
				// 已支付已消费
				v = inflater.inflate(R.layout.order_payment_spend_list_item,
						null);
				holder.order_list_item_layout = (LinearLayout) v
						.findViewById(R.id.order_list_item_layout);
				holder.order_date = (TextView) v.findViewById(R.id.order_date);
				holder.add_dynamic_status = (TextView) v
						.findViewById(R.id.add_dynamic_status);
				holder.order_icon = (ImageView) v.findViewById(R.id.order_icon);
				holder.store_name = (TextView) v.findViewById(R.id.store_name);
				holder.total = (TextView) v.findViewById(R.id.total);
				holder.order_right = (RelativeLayout) v
						.findViewById(R.id.order_right);
				// 数据
				imageLoader.displayImage(mInfo.mStoreInfo.logoLocation
						+ mInfo.mStoreInfo.logoName, holder.order_icon);
				holder.store_name.setText(mInfo.store_name);
				String total3 = NumberFormatUtils.format(Float
						.parseFloat(mInfo.total));
				holder.total.setText("￥" + total3);
				holder.order_list_item_layout
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent intent = new Intent(context,
										OrderDetailActivity.class);
								intent.putExtra("order_id", mInfo.order_id);
								context.startActivity(intent);
							}
						});
				holder.order_date.setText(StringUtils.getString(context,
						R.string.consumed_at)
						+ TimeUtil.FormatTime(mInfo.order_date, "yyyy/MM/dd"));
				if (mInfo.mDynamicInfo.getDynamicType().equals("1")) {
					// 动态已发布，意味已评价
					holder.add_dynamic_status.setText(StringUtils.getString(
							context, R.string.commented));
					holder.add_dynamic_status.setTextColor(Color
							.parseColor("#49b8ef"));
					holder.add_dynamic_status
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									Intent intent = new Intent(context,
											DynamicDetailActivity.class);
									intent.putExtra("dynamic",
											getDynamic(mInfo.mDynamicInfo
													.getDynamicId()));
									context.startActivity(intent);
								}
							});
				} else {
					// 未评价，点击去评价
					holder.add_dynamic_status
							.setBackgroundResource(R.drawable.blue_btn_shape);
					holder.add_dynamic_status.setTextColor(Color
							.parseColor("#FFFFFF"));
					holder.add_dynamic_status
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									Intent intent = new Intent(context,
											AddDynamicActivity.class);
									intent.putExtra("dynamic_id",
											mInfo.mDynamicInfo.getDynamicId());
									context.startActivity(intent);
								}
							});
				}
				holder = (ViewHolder) v.getTag();
				break;
			case OrderActivity.CONSUME_NOT_SPEND:
				// 到店支付未消费
				v = inflater.inflate(
						R.layout.order_tostore_not_spend_list_item, null);
				holder.exprie_date = (TextView) v.findViewById(R.id.order_date);
				holder.order_list_item_layout = (LinearLayout) v
						.findViewById(R.id.order_list_item_layout);
				holder.order_icon = (ImageView) v.findViewById(R.id.order_icon);
				holder.store_name = (TextView) v.findViewById(R.id.store_name);
				holder.total = (TextView) v.findViewById(R.id.total);
				// 数据
				imageLoader.displayImage(mInfo.mStoreInfo.logoLocation
						+ mInfo.mStoreInfo.logoName, holder.order_icon);
				holder.store_name.setText(mInfo.store_name);
				String total4 = NumberFormatUtils.format(Float
						.parseFloat(mInfo.total));
				holder.total.setText("￥" + total4);
				holder.exprie_date.setText(TimeUtil.FormatTime(
						mInfo.mConsumeInfo.exprie_date, "yyyy/MM/dd")
						+ StringUtils.getString(context,
								R.string.effective_before));
				if (!mInfo.order_type.equals("41")) {
					holder.exprie_date.setVisibility(View.VISIBLE);
				}
				holder.order_list_item_layout
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent intent = new Intent(context,
										OrderDetailActivity.class);
								intent.putExtra("order_id", mInfo.order_id);
								context.startActivity(intent);
							}
						});
				holder = (ViewHolder) v.getTag();
				break;
			case OrderActivity.CONSUME_SPEND:
				// 到点支付已消费
				v = inflater.inflate(R.layout.order_tostore_spend_list_item,
						null);
				holder.order_list_item_layout = (LinearLayout) v
						.findViewById(R.id.order_list_item_layout);
				holder.order_date = (TextView) v.findViewById(R.id.order_date);
				holder.add_dynamic_status = (TextView) v
						.findViewById(R.id.add_dynamic_status);
				holder.order_icon = (ImageView) v.findViewById(R.id.order_icon);
				holder.store_name = (TextView) v.findViewById(R.id.store_name);
				holder.total = (TextView) v.findViewById(R.id.total);
				// 数据
				imageLoader.displayImage(mInfo.mStoreInfo.logoLocation
						+ mInfo.mStoreInfo.logoName, holder.order_icon);
				holder.store_name.setText(mInfo.store_name);
				String total5 = NumberFormatUtils.format(Float
						.parseFloat(mInfo.total));
				holder.total.setText("￥" + total5);
				holder.order_list_item_layout
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent intent = new Intent(context,
										OrderDetailActivity.class);
								intent.putExtra("order_id", mInfo.order_id);
								context.startActivity(intent);
							}
						});
				holder.order_date.setText(StringUtils.getString(context,
						R.string.consumed_at)
						+ TimeUtil.FormatTime(mInfo.order_date, "yyyy/MM/dd"));
				if (mInfo.mDynamicInfo.getDynamicType().equals("1")) {
					// 动态已发布，意味已评价
					holder.add_dynamic_status.setText(StringUtils.getString(
							context, R.string.commented));
					holder.add_dynamic_status.setTextColor(Color
							.parseColor("#49b8ef"));
					holder.add_dynamic_status
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									Intent intent = new Intent(context,
											DynamicDetailActivity.class);
									intent.putExtra("dynamic",
											getDynamic(mInfo.mDynamicInfo
													.getDynamicId()));
									context.startActivity(intent);
								}
							});
				} else {
					// 未评价，点击去评价
					holder.add_dynamic_status
							.setBackgroundResource(R.drawable.blue_btn_shape);
					holder.add_dynamic_status.setTextColor(Color
							.parseColor("#FFFFFF"));
					holder.add_dynamic_status
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									Intent intent = new Intent(context,
											AddDynamicActivity.class);
									intent.putExtra("dynamic_id",
											mInfo.mDynamicInfo.getDynamicId());
									context.startActivity(intent);
								}
							});
				}
				holder = (ViewHolder) v.getTag();
				break;
			}
		}
		return v;
	}

	/************************************* 父项 ***************************************/
	@Override
	public Object getGroup(int groupPosition) {
		return getGroup(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return mOrderDatesList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View v,
			ViewGroup parent) {
		OrderDate date = mOrderDatesList.get(groupPosition);
		v = inflater.inflate(R.layout.dynamic_parent_item, null);
		TextView leftTextView = (TextView) v.findViewById(R.id.leftTextView);
		TextView rightTextView = (TextView) v.findViewById(R.id.rightTextView);
		String yearString = date.key.substring(0, 4);
		String monthString = date.key.substring(4, date.key.length());
		// 月
		leftTextView.setText(yearString
				+ StringUtils.getString(context, R.string.year) + monthString
				+ StringUtils.getString(context, R.string.month));
		System.out.println("date.value:"+date.value);
		rightTextView.setText(StringUtils.getString(context, R.string.total)
				+ date.value + StringUtils.getString(context, R.string.count));
		return v;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	/** 封装动态实体类 */
	private Dynamic getDynamic(String dynamicId) {
		Dynamic dynamic = new Dynamic();
		dynamic.setDynamicId(dynamicId);
		return dynamic;
	}

	/** 父项是否可点击 */
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}
