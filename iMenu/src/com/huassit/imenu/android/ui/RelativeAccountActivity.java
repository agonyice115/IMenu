package com.huassit.imenu.android.ui;

import java.util.HashMap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.renren.Renren;
import cn.sharesdk.sina.weibo.SinaWeibo;

import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.view.NavigationView;

/**
 * 相关账户页面
 * 
 * @author shang_guan
 */
public class RelativeAccountActivity extends BaseActivity implements
		OnClickListener, PlatformActionListener {

	private NavigationView mNavigationView;
	/** 手机号码 */
	private RelativeLayout rl_phone;
	private TextView tv_phone;
	/** 邮箱 */
	private RelativeLayout rl_email;
	private TextView tv_email;
	/** 新浪 */
	private ImageView iv_sina;
	/** 人人 */
	private ImageView iv_renren;
	/** sin a操作 */
	private Platform SinaPlatform;
	/** ren_ren操作 */
	private Platform RenrenPlatform;
	/** 新浪授权标石 */
	private boolean isSina;
	/** 人人授权标石 */
	private boolean isRenren;

	@Override
	public int getContentView() {
		return R.layout.relative_account;
	}

	@Override
	public void initView() {
		ShareSDK.initSDK(this);
		SinaPlatform = ShareSDK.getPlatform(this, SinaWeibo.NAME);
		RenrenPlatform = ShareSDK.getPlatform(this, Renren.NAME);
		mNavigationView = (NavigationView) findViewById(R.id.navigationView);
		mNavigationView.setNavigateItemVisibility(
				NavigationView.LEFT_IMAGE_VIEW, View.GONE);
		mNavigationView.setNavigateItemBackground(
				NavigationView.LEFT_TEXT_VIEW, R.drawable.back);
		mNavigationView.setNavigateItemText(NavigationView.TITLE,
				StringUtils.getString(this, R.string.relative_account));
		mNavigationView.setNavigateItemVisibility(
				NavigationView.SECOND_RIGHT_TEXT_VIEW, View.GONE);
		mNavigationView.setNavigateItemOnClickListener(
				NavigationView.LEFT_TEXT_VIEW, new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		iv_sina = (ImageView) findViewById(R.id.iv_sina);
		iv_sina.setOnClickListener(this);
		iv_renren = (ImageView) findViewById(R.id.iv_renren);
		iv_renren.setOnClickListener(this);
		rl_email = (RelativeLayout) findViewById(R.id.rl_email);
		rl_email.setOnClickListener(this);
		rl_phone = (RelativeLayout) findViewById(R.id.rl_phone);
		rl_phone.setOnClickListener(this);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_email = (TextView) findViewById(R.id.tv_email);
		if (!StringUtils.isBlank(PreferencesUtils.getString(this,
				"member_phone"))) {
			tv_phone.setText(changePhone(PreferencesUtils.getString(this,
					"member_phone")));
		}
		if (!StringUtils.isBlank(PreferencesUtils.getString(this,
				"member_email"))) {
			tv_email.setText(PreferencesUtils.getString(this, "member_email"));
		}
	}

	@Override
	public void initData() {
		isRelativeSina();
		isRelativeRenren();
	}

	/** 请求是否已授权新浪微博 */
	private void isRelativeSina() {
		SinaPlatform = ShareSDK.getPlatform(this, SinaWeibo.NAME);
		if (SinaPlatform.isValid()) {
			// 如果已经授权
			isSina = true;
			iv_sina.setImageResource(R.drawable.switch_on);
		} else {
			// 如果未授权
			isSina = false;
			iv_sina.setImageResource(R.drawable.switch_off);
		}
	}

	/** 请求是否已授权人人 */
	private void isRelativeRenren() {
		RenrenPlatform = ShareSDK.getPlatform(this, Renren.NAME);
		if (RenrenPlatform.isValid()) {
			// 如果已经授权
			isRenren = true;
			iv_renren.setImageResource(R.drawable.switch_on);
		} else {
			// 如果未授权
			isRenren = false;
			iv_renren.setImageResource(R.drawable.switch_off);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_email:
			final EditText et = new EditText(context);
			et.setText(tv_email.getText().toString());
			new AlertDialog.Builder(context)
					.setTitle(StringUtils.getString(this, R.string.email))
					.setView(et)
					.setPositiveButton(
							StringUtils.getString(this, R.string.confirm),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									tv_email.setText(et.getText().toString());
									PreferencesUtils.putString(
											RelativeAccountActivity.this,
											"member_email", et.getText()
													.toString());
									dialog.dismiss();
								}
							})
					.setNegativeButton(
							StringUtils.getString(this, R.string.cancel), null)
					.show();
			break;
		case R.id.iv_sina:
			if (isSina) {
				// 点击后取消授权,移除授权
				SinaPlatform.removeAccount();
				isSina = false;
				iv_sina.setImageResource(R.drawable.switch_off);
			} else {
				// 点击后授权
				SinaPlatform.setPlatformActionListener(this);
				SinaPlatform.authorize();
				isSina = true;
				iv_sina.setImageResource(R.drawable.switch_on);
			}
			break;
		case R.id.iv_renren:
			if (isRenren) {
				// 点击后取消授权,移除授权
				RenrenPlatform.removeAccount();
				isRenren = false;
				iv_renren.setImageResource(R.drawable.switch_off);
			} else {
				// 点击后授权
				RenrenPlatform.setPlatformActionListener(this);
				RenrenPlatform.authorize();
				isRenren = true;
				iv_renren.setImageResource(R.drawable.switch_on);
			}
			break;
		}
	}

	@Override
	public void onCancel(Platform arg0, int arg1) {
		if (arg0 == SinaPlatform) {
			System.out.println("SinaPlatform..onCancel");
		}
	}

	/** 显示加密手机号 */
	private String changePhone(String phone) {
		String mobile = "";
		String str = phone.substring(3, 7);
		System.out.println("str:" + str);
		mobile = phone.replace(str, "****");
		return mobile;
	}

	@Override
	public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
		if (arg0 == SinaPlatform) {
			isSina = true;
			isRelativeSina();
			System.out.println("SinaPlatform..onComplete");
		} else {
			isRenren = true;
			isRelativeRenren();
			System.out.println("RenrenPlatform..onComplete");
		}
	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		if (arg0 == SinaPlatform) {
			isSina = true;
			isRelativeSina();
			System.out.println("SinaPlatform...onError");
		} else {
			isRenren = true;
			isRelativeRenren();
			System.out.println("RenrenPlatform...onError");
		}
	}
}
