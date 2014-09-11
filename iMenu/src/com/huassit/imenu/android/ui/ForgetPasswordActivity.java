package com.huassit.imenu.android.ui;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.biz.ForgetPasswordResp;
import com.huassit.imenu.android.model.Verify;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.view.NavigationView;

/***
 * 忘记密码
 * 
 * @author shangshang
 * 
 */
public class ForgetPasswordActivity extends BaseActivity {

	/** 类型：手机标石 */
	private static final String TYPE_MOBILE = "1";
	/** 类型：邮箱标石 */
	private static final String TYPE_EMAIL = "2";
	private String loginType = TYPE_MOBILE;
	private NavigationView mNavigationView;
	private ImageView switchButton;
	private TextView loginText;
	private RelativeLayout mComfirmLayout;
	private String loginName;
	private EditText et_loginName;

	@Override
	public int getContentView() {
		return R.layout.forget_password;
	}

	@Override
	public void initView() {
		mNavigationView = (NavigationView) findViewById(R.id.navigationView);
		mNavigationView.setNavigateItemText(NavigationView.LEFT_TEXT_VIEW,
				StringUtils.getString(this, R.string.password_found_by_mobile));
		mNavigationView.setNavigateItemVisibility(
				NavigationView.LEFT_IMAGE_VIEW, View.GONE);
		mNavigationView.setNavigateItemBackground(
				NavigationView.SECOND_RIGHT_TEXT_VIEW, R.drawable.close);
		mNavigationView.setNavigateItemOnClickListener(
				NavigationView.SECOND_RIGHT_TEXT_VIEW, new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		loginText = (TextView) findViewById(R.id.login_text);
		et_loginName = (EditText) findViewById(R.id.login_name);
		switchButton = (ImageView) findViewById(R.id.iv_switch);
		mComfirmLayout = (RelativeLayout) findViewById(R.id.loginLayout);

		switchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (loginType.equals(TYPE_MOBILE)) {
					// 切换至邮箱登录
					loginType = TYPE_EMAIL;
					mNavigationView.setNavigateItemText(
							NavigationView.LEFT_TEXT_VIEW, StringUtils
									.getString(ForgetPasswordActivity.this,
											R.string.found_by_email_password));
					loginText.setText(R.string.login_email_label);
				} else {
					loginType = TYPE_MOBILE;
					mNavigationView.setNavigateItemText(
							NavigationView.LEFT_TEXT_VIEW, StringUtils
									.getString(ForgetPasswordActivity.this,
											R.string.password_found_by_mobile));
					loginText.setText(R.string.mobile_phone);
				}

			}
		});

		mComfirmLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loginName = et_loginName.getText().toString();
				if (!"".equals(loginName)) {
					ForgetPasswordResp resp = new ForgetPasswordResp(context,
							handler, loginName, loginType);
					resp.asyncInvoke(SUCCESS, FAILURE);
				} else {
					prompt(StringUtils.getString(ForgetPasswordActivity.this,
							R.string.pls_type_in_your_account));
				}
			}
		});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case FAILURE:

				break;
			case SUCCESS:
				// 返回数据：verifyCode 以及menuCode
				Verify verify = (Verify) msg.obj;
				Intent intent = new Intent(ForgetPasswordActivity.this,
						RegisterTwoActivity.class);
				intent.putExtra("sourceActivity", "ForgetPassword");
				intent.putExtra("VERIFY-ENTITY", verify);
				intent.putExtra("mobile", loginName);
				intent.putExtra("loginType", loginType);
				startActivity(intent);
				finish();
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void initData() {

	}

}
