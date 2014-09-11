package com.huassit.imenu.android.ui;

import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.biz.ResetPasswordResp;
import com.huassit.imenu.android.model.Member;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.view.NavigationView;

public class ResetPasswordActivity extends BaseActivity{

	private NavigationView mNavigationView;
	private EditText password;
	private EditText passwordAgain;
	private RelativeLayout finishLayout;
	private Member user;
	private String loginType;
	private String loginName;
	private String newPassword;
	private String menuCode;
	@Override
	public int getContentView() {
		return R.layout.reset_password;
	}

	@Override
	public void initView() {
		mNavigationView =(NavigationView) findViewById(R.id.navigationView);
		mNavigationView.setNavigateItemText(NavigationView.LEFT_TEXT_VIEW, StringUtils.getString(this,R.string.rest_pass));
		mNavigationView.setNavigateItemBackground(NavigationView.SECOND_RIGHT_TEXT_VIEW, R.drawable.close);
		mNavigationView.setNavigateItemOnClickListener(NavigationView.SECOND_RIGHT_TEXT_VIEW, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		password =(EditText) findViewById(R.id.reset_password);
		passwordAgain =(EditText) findViewById(R.id.password_again);
		finishLayout =(RelativeLayout) findViewById(R.id.finishLayout);
		finishLayout.setOnClickListener(new OnClickListener() {
			
			

			@Override
			public void onClick(View v) {
				String passwordStr =password.getText().toString();
				String passwordAgainStr =passwordAgain.getText().toString();
				loginType =getIntent().getStringExtra("loginType");
				loginName =getIntent().getStringExtra("loginName");
				menuCode =getIntent().getStringExtra("menuCode");
				ResetPasswordResp resp =new ResetPasswordResp(
						context, handler, loginType, loginName, passwordStr, menuCode);
				resp.asyncInvoke(SUCCESS, FAILURE);
			}
		});
		
	}

	Handler handler =new Handler(){
		

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS:
				user = (Member) msg.obj;
				isLogin = true;
				PreferencesUtils.putString(context, "token", user.token);
				PreferencesUtils.putString(context, "member_id", user.memberId);
				PreferencesUtils.putString(context, "member_name",user.memberName);
				PreferencesUtils.putString(context, "iconLocation",user.iconLocation);
				PreferencesUtils.putString(context, "iconName", user.iconName);
				/** 开始轮询服务，暂时注释，等接口好后，再开启 */
				// PollingUtils.startPollingService(LoginActivity.this, 30,
				// MessageService.class, MessageService.ACTION);
				finish();
				break;
			case FAILURE:
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
