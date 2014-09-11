package com.huassit.imenu.android.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.biz.GetVerifyCodeResp;
import com.huassit.imenu.android.model.AreaCode;
import com.huassit.imenu.android.model.Member;
import com.huassit.imenu.android.model.Verify;
import com.huassit.imenu.android.model.VerifyEntity;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.view.NavigationView;

public class RegisterTwoActivity extends BaseActivity {

	private int count = 30;
	private TextView tv_mobile;
	private EditText et_verifyCode;
	private TextView tv_time;
	private LinearLayout btn_next;
	private LinearLayout btn_resend;
	private Verify verify;
	private AreaCode selectedAreaCode;
	private NavigationView navigationView;
	private TextView tv_resend;
	private ImageView icon_resend;
	private TextView tv_next;
	private LinearLayout ll_verify;

	@Override
	public int getContentView() {
		return R.layout.register_two;
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
		tv_mobile = (TextView) findViewById(R.id.tv_mobile);
		et_verifyCode = (EditText) findViewById(R.id.verifyCode);
		tv_time = (TextView) findViewById(R.id.time);
		btn_next = (LinearLayout) findViewById(R.id.next);
		btn_resend = (LinearLayout) findViewById(R.id.resend);
		tv_resend =(TextView) findViewById(R.id.tv_resend);
		icon_resend =(ImageView) findViewById(R.id.icon_resend);
		tv_next =(TextView) findViewById(R.id.tv_next);
		ll_verify =(LinearLayout) findViewById(R.id.ll_verify);
	}

	@Override
	public void initData() {
		verify = (Verify) getIntent().getSerializableExtra("VERIFY-ENTITY");
		selectedAreaCode = (AreaCode) getIntent().getSerializableExtra(
				"selectedAreaCode");
		final String mobile = getIntent().getStringExtra("mobile");
		String newMobile = hideString(new StringBuffer(mobile));
		tv_mobile.setText(newMobile);
		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 如果输入的验证码正确
				if (verify.verifyCode
						.equals(et_verifyCode.getText().toString())) {
					if (getIntent().getStringExtra("sourceActivity") != null) {
						// 找回密码页面跳转至此
						Intent intent = new Intent(context,
								ResetPasswordActivity.class);
						intent.putExtra("loginType", getIntent()
								.getStringExtra("loginType"));
						intent.putExtra("loginName", getIntent()
								.getStringExtra("mobile"));
						intent.putExtra("menuCode", verify.menuCode);
						startActivity(intent);
						finish();
					} else {
						Intent intent = new Intent(RegisterTwoActivity.this,
								RegisterThreeActivity.class);
						intent.putExtra("mobile", mobile);
						intent.putExtra("menuCode", verify.menuCode);
						startActivity(intent);
						finish();
					}
				} else {
					prompt(StringUtils.getString(RegisterTwoActivity.this,
							R.string.verify_false));
				}
			}
		});
		// 重新发送验证码
		btn_resend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Member user = new Member(mobile);
				GetVerifyCodeResp register_resp = new GetVerifyCodeResp(
						RegisterTwoActivity.this, handler, user,
						selectedAreaCode, "1");
				register_resp.asyncInvoke(100, 101);
			}
		});
	}

	// 倒计时
	Handler timeHandler = new Handler();
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			if (count > 0) {
				count--;
				tv_time.setText("" + count);
				timeHandler.postDelayed(this, 1000);
				ll_verify.setVisibility(View.VISIBLE);
				btn_resend.setEnabled(false);
				icon_resend.setImageResource(R.drawable.resend_disabled);
				tv_resend.setTextColor(Color.parseColor("#CFCFCF"));
			}else{
				ll_verify.setVisibility(View.GONE);
				btn_resend.setEnabled(true);
				icon_resend.setImageResource(R.drawable.resend_normal);
				tv_resend.setTextColor(getResources().getColor(R.color.black));
			}

		}
	};

	Handler handler = new Handler() {

		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 101:
				prompt(StringUtils.getString(RegisterTwoActivity.this,
						R.string.loading_false));
				break;
			case 100:
				// 返回数据：verifyCode 以及menuCode
				Verify verify = (Verify) msg.obj;
//				verify = verify;
				// 重新开始倒计时
				count = 30;
				timeHandler.postDelayed(runnable, 1000);
				break;
			}
		}

	};

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return true;
	}

	private String hideString(StringBuffer sb) {
		sb.replace(3, 7, "****");
		return sb.toString();

	}

	@Override
	protected void onResume() {
		timeHandler.postDelayed(runnable, 1000);
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
	}
}
