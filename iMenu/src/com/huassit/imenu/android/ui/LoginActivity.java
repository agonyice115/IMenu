package com.huassit.imenu.android.ui;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.MyApplication;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.biz.GetCartResp;
import com.huassit.imenu.android.biz.LoginResp;
import com.huassit.imenu.android.db.dao.AreaCodeDao;
import com.huassit.imenu.android.model.AreaCode;
import com.huassit.imenu.android.model.Cart;
import com.huassit.imenu.android.model.Member;
import com.huassit.imenu.android.model.Store;
import com.huassit.imenu.android.service.MessageService;
import com.huassit.imenu.android.ui.cart.CartActivity;
import com.huassit.imenu.android.util.PollingUtils;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.view.NavigationView;

public class LoginActivity extends BaseActivity {

	/** 标石手机类型 */
	private static final String TYPE_MOBILE = "1";
	/** 标石邮箱类型 */
	private static final String TYPE_EMAIL = "2";
	/** 返回值 */
	private static final int REQUEST_CODE = 100;
	/** 获取购物车成功标石 */
	private static final int GET_CART_SUCCESS = 200;
	/** 获取购物车失败标石 */
	private static final int GET_CART_FAILED = 201;
	private NavigationView mNavigationView;
	private EditText loginName;
	private EditText password;
	private ImageView iv_switch;
	private TextView forgetPassword;
	private RelativeLayout mLoginLayout;
	private TextView areaCode;
	private TextView loginText;
	private String loginNameStr;
	private String passwordStr;
	private String loginType = TYPE_MOBILE;
	private Member user;
	private AreaCodeDao mAreaCodeDao;
	private AreaCode selectedAreaCode;
	private RelativeLayout areaCodeLayout;
	private MyApplication application;
	/** 标石从用户验证过来的 */
	private int code = 0;

	@Override
	public int getContentView() {
		return R.layout.login;
	}

	@Override
	public void initView() {
		application = (MyApplication) getApplicationContext();
		mAreaCodeDao = new AreaCodeDao(this);
		mNavigationView = (NavigationView) findViewById(R.id.navigationView);
		mNavigationView.setNavigateItemText(NavigationView.LEFT_TEXT_VIEW,
				R.string.login_with_mobile_phone);
		mNavigationView.setNavigateItemBackground(
				NavigationView.SECOND_RIGHT_TEXT_VIEW, R.drawable.close);
		mNavigationView.setNavigateItemVisibility(
				NavigationView.LEFT_IMAGE_VIEW, View.GONE);
		mNavigationView.setNavigateItemOnClickListener(
				NavigationView.SECOND_RIGHT_TEXT_VIEW, new OnClickListener() {
					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});
		loginText = (TextView) findViewById(R.id.login_text);
		areaCode = (TextView) findViewById(R.id.area_code);
		areaCodeLayout = (RelativeLayout) findViewById(R.id.area_code_layout);
		loginName = (EditText) findViewById(R.id.login_name);
		password = (EditText) findViewById(R.id.password);
		iv_switch = (ImageView) findViewById(R.id.iv_switch);
		forgetPassword = (TextView) findViewById(R.id.forget_password);
		mLoginLayout = (RelativeLayout) findViewById(R.id.loginLayout);
		forgetPassword = (TextView) findViewById(R.id.forget_password);
	}

	@Override
	public void initData() {
		selectedAreaCode = mAreaCodeDao.getDefault();
		if (selectedAreaCode != null) {
			areaCode.setText(selectedAreaCode.area_code_name);
		}
		areaCode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,
						SelectedAreaCodeActivity.class);
				intent.putExtra("selectedAreaCode", selectedAreaCode);
				startActivityForResult(intent, REQUEST_CODE);
				overridePendingTransition(R.anim.translate_in,
						R.anim.translate_out);
			}
		});

		iv_switch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (loginType.equals(TYPE_MOBILE)) {
					// 切换至邮箱登录
					loginType = TYPE_EMAIL;
					mNavigationView.setNavigateItemText(
							NavigationView.LEFT_TEXT_VIEW,
							R.string.login_with_email);
					areaCodeLayout.setVisibility(View.GONE);
					loginText.setText(R.string.login_email_label);
				} else {
					loginType = TYPE_MOBILE;
					mNavigationView.setNavigateItemText(
							NavigationView.LEFT_TEXT_VIEW,
							R.string.login_with_mobile_phone);
					areaCodeLayout.setVisibility(View.VISIBLE);
					loginText.setText(R.string.mobile_phone);
				}
			}
		});
		forgetPassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,
						ForgetPasswordActivity.class);
				startActivity(intent);
				finish();
			}
		});
		mLoginLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loginNameStr = loginName.getText().toString();
				passwordStr = password.getText().toString();
				if (("".equals(loginNameStr) || loginNameStr == null)
						|| ("".equals(passwordStr)) || passwordStr == null) {
					prompt(StringUtils.getString(LoginActivity.this,
							R.string.fill_complete));
					return;
				}
				user = new Member();
				if ("1".equals(loginType)) {
					user.setMobile(loginNameStr);
				} else {
					user.setEmail(loginNameStr);
				}
				user.setPassWord(passwordStr);

				LoginResp resp = new LoginResp(LoginActivity.this, handler,
						user, loginType);
				resp.asyncInvoke(SUCCESS, FAILURE);
			}
		});
		if (getIntent().getExtras() != null) {
			code = getIntent().getExtras().getInt("code");
		}
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FAILURE:
				mNavigationView.showMessage((String) msg.obj);
				break;
			case SUCCESS:
				// 登录成功后
				user = (Member) msg.obj;
				isLogin = true;
				PreferencesUtils.putString(context, "token", user.token);
				PreferencesUtils.putString(context, "member_id", user.memberId);
				PreferencesUtils.putString(context, "member_name",
						user.memberName);
				PreferencesUtils.putString(context, "iconLocation",
						user.iconLocation);
				PreferencesUtils.putString(context, "iconName", user.iconName);
				PreferencesUtils
						.putString(context, "member_phone", user.mobile);
				PreferencesUtils.putString(context, "member_email", user.email);
				PreferencesUtils.putString(context, "dynamicLocation",
						user.dynamicLocation);
				PreferencesUtils.putString(context, "dynamicName",
						user.dynamicName);
				PreferencesUtils.putString(context, "regionId", user.regionId);
				PreferencesUtils.putString(context, "realname", user.realname);
				PreferencesUtils.putString(context, "birthday", user.birthday);
				PreferencesUtils.putString(context, "sex", user.sex);
				PreferencesUtils
						.putString(context, "signature", user.signature);
				/** 开始轮询服务，暂时注释，等接口好后，再开启 */
				PollingUtils.startPollingService(LoginActivity.this, 5,
						MessageService.class, MessageService.ACTION);
				if (code == 100) {
					// 快速登陆成功后跳转购物车
					Store store = (Store) getIntent().getSerializableExtra(
							"store");
					Intent intent = new Intent(LoginActivity.this,
							CartActivity.class);
					intent.putExtra("store", store);
					startActivity(intent);
					finish();
				} else {
					/** 获取该用户的购物车中的内容存储在本地 */
					if(!StringUtils.isBlank(user.token)){
						GetCartResp getCartResp = new GetCartResp(context, handler,
								user.token, user.memberId);
						getCartResp.asyncInvoke(GET_CART_SUCCESS, GET_CART_FAILED);
					}
				}
				break;
			case GET_CART_SUCCESS:
				Cart cart = (Cart) msg.obj;
				if (cart != null && cart.menuList.size() > 0) {
					Map<String, Cart> cartMap = new HashMap<String, Cart>();
					cartMap.put(cart.storeInfo.id, cart);
					application.setCartMap(cartMap);
				}
				finish();
				break;
			case GET_CART_FAILED:
				// prompt("获取购物车失败");
				finish();
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			selectedAreaCode = (AreaCode) data
					.getSerializableExtra("selectedAreaCode");
			areaCode.setText(selectedAreaCode.area_code_name);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return true;
	}

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
