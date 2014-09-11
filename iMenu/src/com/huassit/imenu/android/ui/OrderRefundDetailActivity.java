package com.huassit.imenu.android.ui;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.biz.GetReturnDetailResp;
import com.huassit.imenu.android.model.OrderInfo;
import com.huassit.imenu.android.util.NumberFormatUtils;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.util.TimeUtil;
import com.huassit.imenu.android.view.NavigationView;

/**
 * 退款订单详细界面
 * 
 * @author shang_guan
 */
public class OrderRefundDetailActivity extends BaseActivity {

	private int orderType;
	/** 退款中 */
	public static final int REFUND_OF = 31;
	/** 已退款 */
	public static final int REFUNDED = 32;
	private NavigationView navigationView;
	/** 商家名称显示 */
	private TextView store_name;
	/** 人数 */
	private TextView refund_detail;
	/** 数量 */
	// private TextView order_count;
	/** 合计 */
	private TextView total;
	/** 订单ID */
	private TextView order_id;
	/** 订单id */
	private String orderId;
	/** 退款流程layout */
	private LinearLayout layout_refund_process;
	/** 退款状态显示 */
	private TextView tv_order_type;

	@Override
	public int getContentView() {
		return R.layout.refund_detail;
	}

	@Override
	public void initView() {
		if (getIntent().getExtras() != null) {
			orderType = getIntent().getExtras().getInt("orderType");
			orderId = getIntent().getExtras().getString("orderId");
		}
		tv_order_type = (TextView) findViewById(R.id.tv_order_type);
		layout_refund_process = (LinearLayout) findViewById(R.id.layout_refund_process);
		store_name = (TextView) findViewById(R.id.store_name);
		refund_detail = (TextView) findViewById(R.id.refund_detail);
		// order_count = (TextView) findViewById(R.id.order_count);
		total = (TextView) findViewById(R.id.total);
		order_id = (TextView) findViewById(R.id.order_id);
		navigationView = (NavigationView) findViewById(R.id.navigationView);
		navigationView.setNavigateItemText(NavigationView.TITLE,
				StringUtils.getString(this, R.string.return_detail));
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
		if (orderType == REFUNDED) {
			tv_order_type.setText(StringUtils.getString(this,
					R.string.return_success));
			tv_order_type.setTextColor(Color.parseColor("#339933"));
		} else {
			tv_order_type.setText(StringUtils.getString(this,
					R.string.submit_apply_return));
			tv_order_type.setTextColor(Color.parseColor("#ffff8800"));
		}
	}

	@Override
	public void initData() {
		String member_id = PreferencesUtils.getString(this, "member_id");
		GetReturnDetailResp mGetOrderDetailResp = new GetReturnDetailResp(this,
				handler, token, member_id, orderId);
		mGetOrderDetailResp.asyncInvoke(SUCCESS, FAILURE);
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SUCCESS:
				if (msg.obj != null) {
					OrderInfo info = (OrderInfo) msg.obj;
					showDetail(info);
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
	private void showDetail(OrderInfo info) {
		/** 暂时生成假数据 */
		store_name.setText(info.mStoreInfo.name);
		order_id.setText(StringUtils.getString(this, R.string.return_number)
				+ info.order_no);
		Float f_total = Float.valueOf(info.total);
		total.setText("￥" + NumberFormatUtils.format(f_total));
		if (orderType == REFUNDED) {
			refund_detail.setText(StringUtils.getString(this,
					R.string.imenu_has)
					+ TimeUtil.FormatTime(info.returnInfo.date_added,
							"yyyy-MM-dd HH:mm")
					+ StringUtils.getString(this, R.string.return_ok));
		} else {
			refund_detail.setText(StringUtils.getString(this,
					R.string.return_ok_tishi));
		}
	}
}
