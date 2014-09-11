package com.huassit.imenu.android.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.biz.GetVerifyCodeResp;
import com.huassit.imenu.android.db.dao.AreaCodeDao;
import com.huassit.imenu.android.model.AreaCode;
import com.huassit.imenu.android.model.Member;
import com.huassit.imenu.android.model.Verify;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.view.NavigationView;

public class RegisterActivity extends BaseActivity {
	private static final int REQUEST_CODE = 100;
	private EditText et_mobile;
	private RelativeLayout btn_next;
	private String mobile;
	private TextView article;
	private TextView mAreaCode;
	private AreaCodeDao mAreaCodeDao;
	private AreaCode selectedAreaCode;
	private NavigationView navigationView;

	@Override
	public int getContentView() {
		return R.layout.register;
	}

	@Override
	public void initView() {
		mAreaCodeDao = new AreaCodeDao(this);
		et_mobile = (EditText) findViewById(R.id.phone_number);
		btn_next = (RelativeLayout) findViewById(R.id.nextPage);
		article = (TextView) findViewById(R.id.article);
		mAreaCode = (TextView) findViewById(R.id.areaCode);
		navigationView = (NavigationView) findViewById(R.id.navigationView);
		navigationView.setNavigateItemVisibility(
				NavigationView.LEFT_IMAGE_VIEW, View.GONE);
		navigationView.setNavigateItemVisibility(
				NavigationView.FIRST_RIGHT_TEXT_VIEW, View.GONE);
		navigationView.setNavigateItemText(NavigationView.LEFT_TEXT_VIEW,
				R.string.register);
		navigationView.setNavigateItemBackground(
				NavigationView.SECOND_RIGHT_TEXT_VIEW, R.drawable.close);
		navigationView.setNavigateItemOnClickListener(
				NavigationView.SECOND_RIGHT_TEXT_VIEW, new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
						overridePendingTransition(R.anim.translate_in,
								R.anim.translate_out);
					}
				});
	}

	@Override
	public void initData() {
		selectedAreaCode = mAreaCodeDao.getDefault();
		if (selectedAreaCode != null) {
			mAreaCode.setText(selectedAreaCode.area_code_name);
		}

		// 软件协议
		article.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RegisterActivity.this,
						WebViewActivity.class);
				startActivity(intent);
			}
		});
		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mobile = et_mobile.getText().toString();
				if (!"".equals(mobile)) {
					Member user = new Member(mobile);
					GetVerifyCodeResp register_resp = new GetVerifyCodeResp(
							RegisterActivity.this, handler, user,
							selectedAreaCode, "1");
					register_resp.asyncInvoke(100, 101);
				} else {
					prompt(StringUtils.getString(RegisterActivity.this,R.string.pls_phone));
				}
			}
		});
		mAreaCode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RegisterActivity.this,
						SelectedAreaCodeActivity.class);
				intent.putExtra("selectedAreaCode", selectedAreaCode);
				startActivityForResult(intent, REQUEST_CODE);
				overridePendingTransition(R.anim.translate_in,
						R.anim.translate_out);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			selectedAreaCode = (AreaCode) data
					.getSerializableExtra("selectedAreaCode");
			mAreaCode.setText(selectedAreaCode.area_code_name);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	Handler handler = new Handler() {

		public void handleMessage(Message msg) {

			switch (msg.what) {

			case 101:
				String errorStr = (String) msg.obj;
				String[] errorStrings = errorStr.split("\\|");
				prompt(errorStrings[1]);
				break;
			case 100:
				// 返回数据：verifyCode 以及menuCode
				Verify verify = (Verify) msg.obj;
				Intent intent = new Intent(RegisterActivity.this,
						RegisterTwoActivity.class);
				intent.putExtra("VERIFY-ENTITY", verify);
				intent.putExtra("selectedAreaCode", selectedAreaCode);
				intent.putExtra("mobile", mobile);
				startActivity(intent);
				finish();
				break;
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
	}
}
