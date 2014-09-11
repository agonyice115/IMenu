package com.huassit.imenu.android.ui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.adapter.FeedBackSpinnerAdapter;
import com.huassit.imenu.android.adapter.FeedBackSpinnerSubAdapter;
import com.huassit.imenu.android.biz.SubmitFeedbackResp;
import com.huassit.imenu.android.db.dao.FeedbackDao;
import com.huassit.imenu.android.model.Feedback;
import com.huassit.imenu.android.model.OrderInfo;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.view.NavigationView;

/**
 * 提交反馈界面
 * 
 * @author shang_guan
 */
public class SubimtFeedbackActivity extends BaseActivity {

	private NavigationView navigationView;
	/** 问题分类 */
	private Spinner feedback_question;
	/** 问题子分类 */
	private Spinner feedback_question_sub;
	/** 反馈内容 */
	private EditText feedback_content;
	/** 用户名 */
	private TextView member_name;
	/** 电话 */
	private TextView phone;
	/** 邮箱 */
	private TextView member_email;
	/** 提交按钮 */
	private TextView submit;
	/** 重写按钮 */
	private TextView reset;
	/** 问题分类 */
	private List<String> question;
	/** 问题分类子分类 */
	private List<String> questionSub;
	/** 适配器 */
	private FeedBackSpinnerAdapter mQuestionAdapter;
	/** 子分类适配器 */
	private FeedBackSpinnerSubAdapter mQuestionSubAdapter;
	/** 储存父分类内容 */
	private String questionString = "";
	/** 储存子分类内容 */
	private String questionSubString;
	/** 判断是第一次点击父选项，true为删除第一项 */
	private boolean isOne = true;
	/** 判断是第一次点击子选项，true为删除第一项 */
	private boolean isSubOne = true;

	/** 为选择分类是显示空白子分类 */
	private TextView tv_kong;
	private FeedbackDao feedbackDao;
	/** 筛选后的数据 */
	private List<Feedback> mFeedbacks;
	/** 去除本地存储的所有下拉列表数据 */
	private List<Feedback> mFeedbacksList;
	private String feedBackId;
	private String feedBackType;
	private String phoneString = "";
	private String emailString = "";

	@Override
	public int getContentView() {
		return R.layout.feedback;
	}

	@Override
	public void initView() {
		feedbackDao = new FeedbackDao(this);
		mFeedbacks = new ArrayList<Feedback>();
		mFeedbacksList = new ArrayList<Feedback>();
		mFeedbacksList = feedbackDao.getFeedBackList();
		feedback_content = (EditText) findViewById(R.id.feedback_content);
		member_name = (TextView) findViewById(R.id.member_name);
		phone = (TextView) findViewById(R.id.phone);
		member_email = (TextView) findViewById(R.id.member_email);
		submit = (TextView) findViewById(R.id.submit);
		reset = (TextView) findViewById(R.id.reset);

		tv_kong = (TextView) findViewById(R.id.tv_kong);
		feedback_question = (Spinner) findViewById(R.id.feedback_question);
		feedback_question_sub = (Spinner) findViewById(R.id.feedback_question_sub);
		navigationView = (NavigationView) findViewById(R.id.navigationView);
		navigationView.setNavigateItemText(NavigationView.TITLE,
				StringUtils.getString(this, R.string.feedback));
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
		if (null != PreferencesUtils.getString(this, "member_name")
				&& !"".equals(PreferencesUtils.getString(this, "member_name"))) {
			member_name
					.setText(PreferencesUtils.getString(this, "member_name"));
		}
		if (null != PreferencesUtils.getString(this, "member_phone")
				&& !("").equals(PreferencesUtils
						.getString(this, "member_phone"))) {
			phoneString = changePhone(PreferencesUtils.getString(this,
					"member_phone"));
			phone.setText(phoneString);
		}
		if (null != PreferencesUtils.getString(this, "member_email")
				&& !("").equals(PreferencesUtils
						.getString(this, "member_email"))) {
			emailString = PreferencesUtils.getString(this, "member_email");
			member_email.setText(emailString);
		}
		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isNull()) {
					SubmitFeedbackResp mFeedbackResp = new SubmitFeedbackResp(
							SubimtFeedbackActivity.this, handler,
							PreferencesUtils.getString(
									SubimtFeedbackActivity.this, "member_id"),
							phoneString, emailString, feedBackId,
							feedback_content.getText().toString(),
							feedBackType, "");
					mFeedbackResp.asyncInvoke(SUCCESS, FAILURE);
				} else {
					prompt(StringUtils.getString(SubimtFeedbackActivity.this,
							R.string.feedback_not_null));
				}
			}
		});
		reset.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mFeedbacks = new ArrayList<Feedback>();
				initAdapter();
				feedback_content.setText("");
			}
		});
		initAdapter();
		feedback_question
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						mQuestionSubAdapter = new FeedBackSpinnerSubAdapter(
								SubimtFeedbackActivity.this,
								R.layout.feedback_spinner_list_item);
						feedBackId = ((Feedback) mQuestionAdapter
								.getItem(position)).feedback_id;
						feedBackType = ((Feedback) mQuestionAdapter
								.getItem(position)).feedback_type;
						setQuestionSubList(feedBackId);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});
		feedback_question_sub
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});
	}

	/** 初始化设置适配器及数据 */
	private void initAdapter() {
		setQuestionList();
	}

	@Override
	public void initData() {

	}

	/** 填充问题父分类list数据 */
	private void setQuestionList() {
		if (mFeedbacksList != null && mFeedbacksList.size() > 0) {
			for (int i = 0; i < mFeedbacksList.size(); i++) {
				if (mFeedbacksList.get(i).level.equals("1")) {
					mFeedbacks.add(mFeedbacksList.get(i));
				}
			}
		}
		mQuestionAdapter = new FeedBackSpinnerAdapter(this, mFeedbacks);
		feedback_question.setAdapter(mQuestionAdapter);
	}

	/** 填充问题子分类list数据 */
	private void setQuestionSubList(String feedBackId) {
		mFeedbacks = new ArrayList<Feedback>();
		for (int i = 0; i < mFeedbacksList.size(); i++) {
			if (mFeedbacksList.get(i).level.equals("2")) {
				if (mFeedbacksList.get(i).parent_id.equals(feedBackId)) {
					mFeedbacks.add(mFeedbacksList.get(i));
				}
			}
		}
		mQuestionSubAdapter.getDataList().addAll(mFeedbacks);
		feedback_question_sub.setAdapter(mQuestionSubAdapter);
		tv_kong.setVisibility(View.GONE);
		feedback_question_sub.setVisibility(View.VISIBLE);
	}

	/** 判断是否为空 */
	private boolean isNull() {
		boolean isNull = false;
		if (feedback_content.getText().toString().equals("")) {
			isNull = false;
		} else {
			isNull = true;
		}
		return isNull;
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SUCCESS:
				if (msg.obj != null) {
					// navigationView.showErrorMessage(msg.obj.toString());
					showDialog(SubimtFeedbackActivity.this,
							StringUtils.getString(SubimtFeedbackActivity.this,
									R.string.feedback_thanks),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
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

	/** 显示加密手机号 */
	private String changePhone(String phone) {
		String mobile = "";
		String str = phone.substring(3, 7);
		System.out.println("str:" + str);
		mobile = phone.replace(str, "****");
		return mobile;
	}

	@Override
	protected void onResume() {
		super.onResume();
		navigationView.setBackgroundColor(Color.parseColor(colorValue));
	}

	/** 显示信息 */
	private void showDetail(OrderInfo mInfo) {

	}
}
