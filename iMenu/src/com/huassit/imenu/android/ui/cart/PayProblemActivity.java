package com.huassit.imenu.android.ui.cart;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.adapter.PayProblemListAdapter;
import com.huassit.imenu.android.biz.SubmitFeedbackResp;
import com.huassit.imenu.android.db.dao.FeedbackDao;
import com.huassit.imenu.android.model.Feedback;
import com.huassit.imenu.android.model.OrderInfo;
import com.huassit.imenu.android.util.NumberFormatUtils;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.util.Utility;
import com.huassit.imenu.android.view.NavigationView;

/**
 * 支付遇到问题页面
 * 
 * @author think
 */
public class PayProblemActivity extends BaseActivity implements OnClickListener {

	/** 商家名称 */
	private TextView order_store_name;
	/** 订单号 */
	private TextView order_no;
	/** 订单总金额 */
	private TextView order_total;
	/** 问题反馈List */
	private ListView problem_list;
	/** 问题输入 */
	private EditText et_problem;
	/** 提交问题按钮 */
	private TextView submit_question;
	private NavigationView mNavigationView;
	/** 订单实体类 */
	private OrderInfo orderInfo;
	/** 问题列表适配器 */
	private PayProblemListAdapter mAdapter;
	/** 问题列表加数据源 */
	private List<Feedback> mPayProblemsList;
	private FeedbackDao feedbackDao;
	/** 筛选后的数据 */
	private List<Feedback> mFeedbacks;
	private String feedBackId = "";

	@Override
	public int getContentView() {
		return R.layout.pay_problem;
	}

	@Override
	public void initView() {
		feedbackDao = new FeedbackDao(this);
		mFeedbacks = new ArrayList<Feedback>();
		mPayProblemsList = new ArrayList<Feedback>();
		mNavigationView = (NavigationView) findViewById(R.id.navigationView);
		mNavigationView.setNavigateItemVisibility(
				NavigationView.LEFT_IMAGE_VIEW, View.GONE);
		mNavigationView.setNavigateItemBackground(
				NavigationView.LEFT_TEXT_VIEW, R.drawable.back);
		mNavigationView.setNavigateItemText(NavigationView.TITLE, StringUtils.getString(context, R.string.question_during_pay));
		mNavigationView.setNavigateItemOnClickListener(
				NavigationView.LEFT_TEXT_VIEW, new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		order_store_name = (TextView) findViewById(R.id.order_store_name);
		order_no = (TextView) findViewById(R.id.order_no);
		order_total = (TextView) findViewById(R.id.order_total);
		problem_list = (ListView) findViewById(R.id.problem_list);
		et_problem = (EditText) findViewById(R.id.et_problem);
		submit_question = (TextView) findViewById(R.id.submit_question);
		submit_question.setOnClickListener(this);
		getList();
	}

	/** 更改选中状态并刷新列表 */
	public void changeStatus(String feedbackId) {
		for (int i = 0; i < mPayProblemsList.size(); i++) {
			if (!mPayProblemsList.get(i).equals(feedbackId)) {
				mPayProblemsList.get(i).isSelect = false;
			}
		}
		mAdapter.getDataList().clear();
		mAdapter.getDataList().addAll(mPayProblemsList);
		mAdapter.notifyDataSetChanged();
		Utility.setListViewHeightBasedOnChildren(problem_list);
	}

	@Override
	public void initData() {
		if (getIntent().getExtras() != null) {
			orderInfo = (OrderInfo) getIntent().getExtras().getSerializable(
					"orderInfo");
			order_store_name.setText(orderInfo.mStoreInfo.name);
			order_no.setText(StringUtils.getString(context, R.string.order_number) + orderInfo.order_no);
			String total = NumberFormatUtils.format(Float
					.parseFloat(orderInfo.total));
			order_total.setText("￥" + total);
		}
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS:
				showDialog(PayProblemActivity.this, StringUtils.getString(context, R.string.question_submit),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
							}
						});
				break;
			case FAILURE:
				if (msg.obj != null) {
					mNavigationView.showErrorMessage(msg.obj.toString());
				}
				break;
			}
		}
	};

	private void getList() {
		String parentId = "";
		mFeedbacks = feedbackDao.getFeedBackList();
		if (mFeedbacks != null && mFeedbacks.size() > 0) {
			for (int i = 0; i < mFeedbacks.size(); i++) {
				if (mFeedbacks.get(i).feedback_type.equals("5")) {
					parentId = mFeedbacks.get(i).feedback_id;
				}
			}
			for (int i = 0; i < mFeedbacks.size(); i++) {
				if (mFeedbacks.get(i).parent_id.equals(parentId)) {
					mPayProblemsList.add(mFeedbacks.get(i));
				}
			}
		}
		mPayProblemsList.get(0).isSelect = true;
		mAdapter = new PayProblemListAdapter(this,
				R.layout.pay_problem_list_item);
		mAdapter.getDataList().addAll(mPayProblemsList);
		mAdapter.notifyDataSetChanged();
		problem_list.setAdapter(mAdapter);
		Utility.setListViewHeightBasedOnChildren(problem_list);
	}

	/** 提交问题请求 */
	private void UploadAdapter() {
		SubmitFeedbackResp mFeedbackResp = new SubmitFeedbackResp(this,
				handler, PreferencesUtils.getString(this, "member_id"),
				PreferencesUtils.getString(this, "member_phone"),
				PreferencesUtils.getString(this, "member_email"), feedBackId,
				et_problem.getText().toString(), "5", orderInfo.order_id);
		mFeedbackResp.asyncInvoke(SUCCESS, FAILURE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.submit_question:
			if (et_problem.getVisibility() == View.VISIBLE) {
				if (!StringUtils.isBlank(et_problem.getText().toString())) {
					for (int i = 0; i < mPayProblemsList.size(); i++) {
						if (mPayProblemsList.get(i).isSelect) {
							feedBackId = mPayProblemsList.get(i).feedback_id;
						}
					}
					if ("".equals(feedBackId)) {
						mNavigationView.showErrorMessage(StringUtils.getString(context, R.string.fill_question));
					} else {
						UploadAdapter();
					}
				} else {
					mNavigationView.showErrorMessage(StringUtils.getString(context, R.string.choose_one_reason));
				}
			} else {
				for (int i = 0; i < mPayProblemsList.size(); i++) {
					if (mPayProblemsList.get(i).isSelect) {
						feedBackId = mPayProblemsList.get(i).feedback_id;
					}
				}
				if ("".equals(feedBackId)) {
					mNavigationView.showErrorMessage(StringUtils.getString(context, R.string.fill_question));
				} else {
					UploadAdapter();
				}
			}
			break;
		}
	}

	/** 显示输入框 */
	public void showEdit(boolean isV) {
		if (isV) {
			et_problem.setVisibility(View.VISIBLE);
		} else {
			et_problem.setVisibility(View.GONE);
		}
	}
}
