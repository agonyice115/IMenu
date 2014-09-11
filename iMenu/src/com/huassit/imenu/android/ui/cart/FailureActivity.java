package com.huassit.imenu.android.ui.cart;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.ui.OrderDetailActivity;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.view.NavigationView;

public class FailureActivity extends BaseActivity {
	// 提交失败
	// private TextView mPay_failure;
	// 合计
	// private TextView mTotal;
	// private TextView mMoney;
	// private TextView mYuan;
	// private TextView mGet;
	// private TextView mMuch_integral;
	// private TextView mIntegral_dian;
	// private TextView mIntegral;
	// 商家名称
	private LinearLayout order_title;
	// // 订单号
	// private ImageView mPay_image;
	// private TextView mTextnumber;
	// private Button mEnter_look;
	// // 两个按钮
	// private Button mPay_ok;
	// private Button mContinue_btn;
	private NavigationView mNavigationView;

	public void initView() {
		mNavigationView = (NavigationView) findViewById(R.id.navigationView);
		mNavigationView.setNavigateItemText(NavigationView.LEFT_TEXT_VIEW, StringUtils.getString(context, R.string.order_result));
		mNavigationView.setNavigateItemVisibility(NavigationView.LEFT_IMAGE_VIEW, View.GONE);
		mNavigationView.setNavigateItemBackground(NavigationView.SECOND_RIGHT_TEXT_VIEW, R.drawable.close);
		mNavigationView.setNavigateItemOnClickListener(NavigationView.SECOND_RIGHT_TEXT_VIEW, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		order_title = (LinearLayout) findViewById(R.id.order_title);
		order_title.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FailureActivity.this,
						OrderDetailActivity.class);
				intent.putExtra("order_id", "194");
				intent.putExtra("orderType", "41");
				startActivity(intent);
			}
		});
	}

	@Override
	public int getContentView() {
		return R.layout.stroe_pay_order_fail;
	}

	@Override
	public void initData() {

	}

}
