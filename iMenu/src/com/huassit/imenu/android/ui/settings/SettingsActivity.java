package com.huassit.imenu.android.ui.settings;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.MyApplication;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.biz.LogoutResp;
import com.huassit.imenu.android.service.MessageService;
import com.huassit.imenu.android.ui.MainActivity;
import com.huassit.imenu.android.ui.UserInfoActivity;
import com.huassit.imenu.android.util.ActivityStackManager;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.view.NavigationView;

/**
 * Created by Sylar on 14-7-11.
 */
public class SettingsActivity extends BaseActivity implements
		View.OnClickListener {
	private NavigationView navigationView;
	private MyApplication application;

	@Override
	public int getContentView() {
		return R.layout.setting_activity;
	}

	@Override
	public void initView() {
		application = (MyApplication) getApplicationContext();
		navigationView = (NavigationView) findViewById(R.id.navigationView);
		navigationView.setNavigateItemText(NavigationView.TITLE,
				R.string.settings);
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
		findViewById(R.id.personal_setting).setOnClickListener(this);
		findViewById(R.id.common_setting).setOnClickListener(this);
		findViewById(R.id.about_setting).setOnClickListener(this);
		findViewById(R.id.logout).setOnClickListener(this);
	}

	@Override
	public void initData() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.personal_setting:
			// goTo(UserInfoActivity.class);
			Intent intent = new Intent(context, UserInfoActivity.class);
			intent.putExtra("sourceActivity", "Settings");
			startActivity(intent);
			break;
		case R.id.common_setting:
			goTo(CommonSettingActivity.class);
			break;
		case R.id.about_setting:
			goTo(AboutSettingActivity.class);
			break;
		case R.id.logout:
			new AlertDialog.Builder(SettingsActivity.this)
					.setTitle(R.string.prompt)
					.setMessage(R.string.logout_message)
					.setPositiveButton(android.R.string.ok,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									logout();
								}
							})
					.setNegativeButton(android.R.string.cancel,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).create().show();
			break;
		}
	}

	private void goTo(Class clazz) {
		Intent intent = new Intent(this, clazz);
		startActivity(intent);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SUCCESS:
				// PreferencesUtils.clear(SettingsActivity.this);
				PreferencesUtils.putString(context, "token", "");
				PreferencesUtils.putString(context, "member_id", "");
				PreferencesUtils.putString(context, "member_name", "");
				PreferencesUtils.putString(context, "iconLocation", "");
				PreferencesUtils.putString(context, "iconName", "");
				application.cartMap = null;
				ActivityStackManager.getActivityManager().finishAllExceptOne(
						MainActivity.class);
				stopService(new Intent(context, MessageService.class));
				finish();
				break;
			case FAILURE:
				navigationView.showMessage((String) msg.obj);
				break;
			}
		}
	};

	private void logout() {
		String memberId = PreferencesUtils.getString(this, "member_id");
		LogoutResp resp = new LogoutResp(this, handler, token, memberId);
		resp.asyncInvoke(SUCCESS, FAILURE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		navigationView.setBackgroundColor(Color.parseColor(colorValue));
	}
}
