package com.huassit.imenu.android.ui.cart;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.biz.GetOrderDetailResp;
import com.huassit.imenu.android.model.OrderInfo;
import com.huassit.imenu.android.ui.MainActivity;
import com.huassit.imenu.android.ui.OrderDetailActivity;
import com.huassit.imenu.android.util.NumberFormatUtils;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.view.NavigationView;

/**
 * 支付成功或下单成功
 * 
 * @author shangshang
 * 
 */
public class SuccesActivity extends BaseActivity implements OnClickListener {

	private NavigationView mNavigationView;
	/** 合计价格 */
	private TextView money;
	/** 获得积分数量 */
	private TextView much_integral;
	/** 商家名称 */
	private TextView name_text;
	/** 消费吗 */
	private TextView order_number;
	/** 查看订单详情 */
	private Button see_order_detail;
	/** 继续浏览 */
	private Button continue_browse;
	/** 订单实体类 */
	private OrderInfo orderInfo;
	/** 支付成功或下单成功的提示语 */
	private TextView pay_success;
	/** 1:支付成功-2:下单成功标石 */
	private int whatSuccess;

	public void initView() {
		whatSuccess = getIntent().getExtras().getInt("whatSuccess");
		mNavigationView = (NavigationView) findViewById(R.id.navigationView);
		pay_success = (TextView) findViewById(R.id.pay_success);
		switch (whatSuccess) {
		case 1:
			mNavigationView.setNavigateItemText(NavigationView.LEFT_TEXT_VIEW,
					StringUtils.getString(context, R.string.pay_result));
			pay_success.setText(StringUtils.getString(context,
					R.string.pay_success));
			break;
		case 2:
			mNavigationView.setNavigateItemText(NavigationView.LEFT_TEXT_VIEW,
					StringUtils
							.getString(context, R.string.order_submit_result));
			pay_success.setText(StringUtils.getString(context,
					R.string.order_submit_success));
			break;
		}

		mNavigationView.setNavigateItemVisibility(
				NavigationView.LEFT_IMAGE_VIEW, View.GONE);
		mNavigationView.setNavigateItemBackground(
				NavigationView.SECOND_RIGHT_TEXT_VIEW, R.drawable.close);
		mNavigationView.setNavigateItemOnClickListener(
				NavigationView.SECOND_RIGHT_TEXT_VIEW, new OnClickListener() {

					@Override
					public void onClick(View v) {
						setResult(0);
						finish();
					}
				});
		money = (TextView) findViewById(R.id.money);
		much_integral = (TextView) findViewById(R.id.much_integral);
		name_text = (TextView) findViewById(R.id.name_text);
		order_number = (TextView) findViewById(R.id.order_number);
		see_order_detail = (Button) findViewById(R.id.see_order_detail);
		continue_browse = (Button) findViewById(R.id.continue_browse);
	}

	@Override
	public int getContentView() {
		return R.layout.stroe_pay_order_success;
	}

	@Override
	public void initData() {
		if (getIntent().getExtras() != null) {
			orderInfo = (OrderInfo) getIntent().getExtras().getSerializable(
					"orderInfo");
			UploadAdapter();
		}
	}

	/** 获取订单详情请求 */
	private void UploadAdapter() {
		String member_id = PreferencesUtils.getString(this, "member_id");
		GetOrderDetailResp mGetOrderDetailResp = new GetOrderDetailResp(this,
				handler, token, member_id, orderInfo.order_id);
		mGetOrderDetailResp.asyncInvoke(SUCCESS, FAILURE);
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SUCCESS:
				if (msg.obj != null) {
					OrderInfo mInfo = (OrderInfo) msg.obj;
					showData(mInfo);
				}
				break;
			case FAILURE:
				if (msg.obj != null) {
					mNavigationView.showErrorMessage(msg.obj.toString());
				}
				break;
			}
		}
	};

	/** 显示信息 */
	private void showData(OrderInfo mInfo) {
		String total = NumberFormatUtils.format(Float.parseFloat(mInfo.total));
		money.setText("￥" + total);
		name_text.setText(mInfo.mStoreInfo.name);
		order_number.setText(mInfo.mConsumeInfo.consume_code);
		see_order_detail.setOnClickListener(this);
		continue_browse.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.see_order_detail:
			// 查看订单详情
			Intent intent = new Intent(SuccesActivity.this,
					OrderDetailActivity.class);
			intent.putExtra("order_id", orderInfo.order_id);
			System.out.println("orderInfo.order_type:" + orderInfo.order_type);
			intent.putExtra("orderType", orderInfo.order_type);
			startActivity(intent);
			finish();
			break;
		case R.id.continue_browse:
			// 继续浏览
//			Intent intent1 = new Intent(SuccesActivity.this, MainActivity.class);
//			startActivity(intent1);
			finish();
			break;
		}
	}
}
