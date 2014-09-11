package com.huassit.imenu.android.ui;

import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.adapter.ReturnReasonListAdapter;
import com.huassit.imenu.android.biz.ApplyReturnResp;
import com.huassit.imenu.android.db.dao.ReturnReasonDao;
import com.huassit.imenu.android.model.OrderInfo;
import com.huassit.imenu.android.model.ReturnReason;
import com.huassit.imenu.android.util.NumberFormatUtils;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.util.Utility;
import com.huassit.imenu.android.view.NavigationView;

/**
 * 申请退款界面
 * 
 * @author shang_guan
 */
public class ApplyReturnActivity extends BaseActivity {

	private NavigationView navigationView;
	/** 退款金额显示 */
	private TextView refund_money;
	/** 商家名称显示 */
	private TextView refund_store_name;
	/** 退款方式待确定 */
	private TextView refund_action;
	/** 退款原因list */
	private ListView return_reason_list;
	/** 提交申请退款 */
	private TextView submit;
	private ReturnReasonListAdapter mAdapter;
	private ReturnReasonDao mReturnReasonDao;
	/** 退款原因数据 */
	private List<ReturnReason> mReasonsList;
	/** 订单详情 */
	private OrderInfo mInfo;

	@Override
	public int getContentView() {
		return R.layout.refund;
	}

	@Override
	public void initView() {
		if (getIntent().getExtras() != null) {
			mInfo = (OrderInfo) getIntent().getSerializableExtra("orderInfo");
		}
		mReturnReasonDao = new ReturnReasonDao(context);
		refund_money = (TextView) findViewById(R.id.refund_money);
		refund_store_name = (TextView) findViewById(R.id.refund_store_name);
		refund_action = (TextView) findViewById(R.id.refund_action);
		return_reason_list = (ListView) findViewById(R.id.return_reason_list);
		submit = (TextView) findViewById(R.id.submit);
		navigationView = (NavigationView) findViewById(R.id.navigationView);
		navigationView.setNavigateItemText(NavigationView.TITLE,
				StringUtils.getString(this, R.string.return_money));
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
		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				UploadAdapter();
			}
		});
	}

	/** 初始化退款原因列表 */
	@Override
	public void initData() {
		showDetail();
		mReasonsList = mReturnReasonDao.getReturnReasonList();
		mAdapter = new ReturnReasonListAdapter(this,
				R.layout.return_reason_list_item);
		return_reason_list.setAdapter(mAdapter);
		if (mReasonsList != null && mReasonsList.size() > 0) {
			mAdapter.getDataList().clear();
			mAdapter.getDataList().addAll(mReasonsList);
			mAdapter.notifyDataSetChanged();
			Utility.setListViewHeightBasedOnChildren(return_reason_list);
		}
	}

	/** 改变列表中数据，并获取所选择退款原因ID */
	public void changeCheckBox(ReturnReason reason, boolean isTrue) {
		if (isTrue) {
			for (int i = 0; i < mReasonsList.size(); i++) {
				if (!mReasonsList.get(i).return_reason_id
						.equals(reason.return_reason_id)) {
					mReasonsList.get(i).isTrue = true;
				}
			}
		} else {
			for (int i = 0; i < mReasonsList.size(); i++) {
				if (!mReasonsList.get(i).return_reason_id
						.equals(reason.return_reason_id)) {
					mReasonsList.get(i).isTrue = false;
				}
			}
		}
		// mAdapter.getDataList().clear();
		// mAdapter.getDataList().addAll(mReasonsList);
		// mAdapter.notifyDataSetChanged();
		// Utility.setListViewHeightBasedOnChildren(return_reason_list);
	}

	private void UploadAdapter() {
		String member_id = PreferencesUtils.getString(this, "member_id");
		String return_reason_id = "";
		// 暂时只取一个选中项
		boolean isChoose = false;
		for (int i = 0; i < mReasonsList.size(); i++) {
			if (!isChoose) {
				if (mReasonsList.get(i).isTrue) {
					isChoose = true;
					return_reason_id = mReasonsList.get(i).return_reason_id;
					ApplyReturnResp mResp = new ApplyReturnResp(this, handler,
							member_id, mInfo.order_id, token, return_reason_id);
					mResp.asyncInvoke(SUCCESS, FAILURE);
					return;
				}
				prompt(StringUtils.getString(this,
						R.string.pls_choose_one_return_reason));
			}
		}

	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SUCCESS:
				if (msg.obj != null) {
					prompt(StringUtils.getString(ApplyReturnActivity.this,
							R.string.apply_return_money_success));
					finish();
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
	private void showDetail() {
		refund_money.setText("￥："
				+ NumberFormatUtils.format(Float.parseFloat(mInfo.total)));
		refund_store_name.setText(mInfo.mStoreInfo.name);
		// refund_action
	}
}
