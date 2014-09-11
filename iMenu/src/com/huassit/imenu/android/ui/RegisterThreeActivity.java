package com.huassit.imenu.android.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.biz.RegistMemberResp;
import com.huassit.imenu.android.model.Member;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.view.NavigationView;

public class RegisterThreeActivity extends BaseActivity {

	private EditText et_password;
	private EditText memberName;
	private String sex = "0";
	private ImageView iv_male;
	private ImageView iv_famale;
	private LinearLayout quickRegister;
	private LinearLayout fullRegister;
	private String mobile;
	private String menuCode;
	private Member user;
	private NavigationView navigationView;

	@Override
	public void initData() {
		menuCode = getIntent().getStringExtra("menuCode");
		mobile = getIntent().getStringExtra("mobile");
		// 点击快速注册
		quickRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				user = new Member(mobile, et_password.getText().toString(),
						memberName.getText().toString(), sex);
				RegistMemberResp resp = new RegistMemberResp(
						RegisterThreeActivity.this, handler, user, menuCode);
				resp.asyncInvoke(SUCCESS, FAILURE);
			}
		});
		// 点击完善个人信息
		fullRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				user = new Member(mobile, et_password.getText().toString(),
						memberName.getText().toString(), sex);
				Intent intent = new Intent(RegisterThreeActivity.this,
						UserInfoActivity.class);
				intent.putExtra("user", user);
				intent.putExtra("menuCode", menuCode);
				startActivity(intent);
			}
		});
	}

	@Override
	public int getContentView() {
		return R.layout.register_three;
	}

	@Override
	public void initView() {
		navigationView = (NavigationView) findViewById(R.id.navigationView);
		navigationView.setNavigateItemText(NavigationView.LEFT_TEXT_VIEW,
				R.string.login_with_mobile_phone);
		navigationView.setNavigateItemBackground(
				NavigationView.SECOND_RIGHT_TEXT_VIEW, R.drawable.close);
		navigationView.setNavigateItemVisibility(
				NavigationView.LEFT_IMAGE_VIEW, View.GONE);
		navigationView.setNavigateItemOnClickListener(
				NavigationView.SECOND_RIGHT_TEXT_VIEW, new OnClickListener() {
					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});
		et_password = (EditText) findViewById(R.id.password);
		memberName = (EditText) findViewById(R.id.membername);
		iv_male = (ImageView) findViewById(R.id.male);
		iv_famale = (ImageView) findViewById(R.id.female);
		quickRegister = (LinearLayout) findViewById(R.id.quickRegister);
		fullRegister = (LinearLayout) findViewById(R.id.fullRegister);
		iv_male.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				iv_male.setImageResource(R.drawable.male_pressed);
				iv_famale.setImageResource(R.drawable.female);
				sex = "1";
			}
		});
		iv_famale.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				iv_famale.setImageResource(R.drawable.female_pressed);
				iv_male.setImageResource(R.drawable.male);
				sex = "2";
			}
		});

		// 点击快速注册
		quickRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				user = new Member(mobile, et_password.getText().toString(),
						memberName.getText().toString(), sex);
				RegistMemberResp resp = new RegistMemberResp(
						RegisterThreeActivity.this, handler, user, menuCode);
				resp.asyncInvoke(100, 101);
			}
		});
		// 点击完善个人信息
		fullRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				user = new Member(mobile, et_password.getText().toString(),
						memberName.getText().toString(), sex);
				Intent intent = new Intent(RegisterThreeActivity.this,
						UserInfoActivity.class);
				intent.putExtra("user", user);
				intent.putExtra("menuCode", menuCode);
				startActivity(intent);

			}
		});
	}

	Handler handler = new Handler() {

		public void handleMessage(Message msg) {

			switch (msg.what) {

			case 101:
				prompt(StringUtils.getString(RegisterThreeActivity.this,
						R.string.loading_false));
				break;
			case 100:
				// 注册成功
				Member user = (Member) msg.obj;
				PreferencesUtils.putString(context, "token", user.token);
				PreferencesUtils.putString(context, "member_id", user.memberId);
				PreferencesUtils.putString(context, "member_name",
						user.memberName);
				PreferencesUtils.putString(context, "iconLocation",
						user.iconLocation);
				PreferencesUtils.putString(context, "iconName", user.iconName);
				Intent intent = new Intent(RegisterThreeActivity.this,
						MainActivity.class);
				startActivity(intent);
				onBackPressed();
				break;
			}
		}
	};

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
	}

}
