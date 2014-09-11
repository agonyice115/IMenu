package com.huassit.imenu.android.ui;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.MyApplication;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.biz.EditCartResp;
import com.huassit.imenu.android.biz.GetVerifyCodeResp;
import com.huassit.imenu.android.biz.QuickLoginResp;
import com.huassit.imenu.android.db.dao.AreaCodeDao;
import com.huassit.imenu.android.model.Cart;
import com.huassit.imenu.android.model.Member;
import com.huassit.imenu.android.model.Menu;
import com.huassit.imenu.android.model.Store;
import com.huassit.imenu.android.model.Verify;
import com.huassit.imenu.android.service.MessageService;
import com.huassit.imenu.android.ui.cart.CartActivity;
import com.huassit.imenu.android.util.PollingUtils;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.view.NavigationView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 用户验证界面
 * 
 * @author shang_shang
 * 
 */
public class UserAuthenticationActivity extends BaseActivity implements
		OnClickListener {

	/** 商家lo go */
	private ImageView storeImage;
	/** 商家名称 */
	private TextView storeName;
	/** 人数 */
	private TextView peopleCount;
	/** 总价 */
	private TextView totalPrice;
	/** 原价 */
	// private TextView originalPrice;
	/** 电话输入 */
	private EditText phoneNumber;
	/** 获取验证码按钮 */
	private TextView getVerifyCode;
	/** 验证码输入 */
	private EditText verifyCode;
	/** 提交订单按钮 */
	private Button submitOrder;
	/** 登陆按钮 */
	private TextView login;
	private NavigationView mNavigationView;
	private Cart cart;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private AreaCodeDao mAreaCodeDao;
	private MyApplication application;
	/** 验证实体类 */
	private Verify verify;
	/** 存储本地购物车数据map */
	private Map<String, Cart> map;
	/** 登陆成功标石 */
	private static final int LOGIN_SUCCESS = 200;
	/** 登陆失败标石 */
	private static final int LOGIN_FAILURE = 201;
	/** 是否校验手机号 */
	private String validate_mobile;
	/** 提交购物车成功标石 */
	private static final int SUBMIT_SUCCESS = 300;
	/** 提交购物车失败标石 */
	private static final int SUBMIT_FALSE = 301;

	@Override
	public int getContentView() {
		return R.layout.user_authentication;
	}

	@Override
	public void initView() {
		imageLoader = ImageLoader.getInstance();
		mAreaCodeDao = new AreaCodeDao(context);
		application = (MyApplication) context.getApplicationContext();
		options = MyApplication.getDisplayImageOptions(context, 40);
		mNavigationView = (NavigationView) findViewById(R.id.navigationView);
		storeImage = (ImageView) findViewById(R.id.order_store_image);
		storeName = (TextView) findViewById(R.id.order_store_name);
		peopleCount = (TextView) findViewById(R.id.order_people);
		totalPrice = (TextView) findViewById(R.id.order_total);
		// originalPrice = (TextView) findViewById(R.id.order_before_price);
		phoneNumber = (EditText) findViewById(R.id.phone_number);
		getVerifyCode = (TextView) findViewById(R.id.get_verify_code);
		getVerifyCode.setOnClickListener(this);
		verifyCode = (EditText) findViewById(R.id.verify_code);
		submitOrder = (Button) findViewById(R.id.submit_order);
		submitOrder.setOnClickListener(this);
		login = (TextView) findViewById(R.id.login);
		login.setOnClickListener(this);
	}

	@Override
	public void initData() {
		// map = application.getCartMap();
		cart = (Cart) getIntent().getSerializableExtra("cart");
		mNavigationView.setNavigateItemVisibility(
				NavigationView.LEFT_IMAGE_VIEW, View.GONE);
		mNavigationView.setNavigateItemBackground(
				NavigationView.SECOND_RIGHT_TEXT_VIEW, R.drawable.close);
		mNavigationView.setNavigateItemText(NavigationView.LEFT_TEXT_VIEW,
				StringUtils.getString(this, R.string.UserAuthentication));
		mNavigationView.setNavigateItemOnClickListener(
				NavigationView.SECOND_RIGHT_TEXT_VIEW, new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		showStoreInfo(cart);
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS:
				verify = (Verify) msg.obj;
				prompt(StringUtils.getString(UserAuthenticationActivity.this,
						R.string.verify_to_phone));
				break;
			case FAILURE:
				if (msg.obj != null) {
					String errorStr = (String) msg.obj;
					String[] errorStrings = errorStr.split("\\|");
					if (errorStrings[1].contains(StringUtils.getString(
							UserAuthenticationActivity.this,
							R.string.reged_phone))) {
						// 用户已注册 直接登录
						validate_mobile = "0";
						UploadGetAdapter(validate_mobile);
					}
				}
				break;
			case LOGIN_SUCCESS:
				if (msg.obj != null) {
					Member member = (Member) msg.obj;
					PreferencesUtils.putString(context, "token", member.token);
					PreferencesUtils.putString(context, "member_id",
							member.memberId);
					PreferencesUtils.putString(context, "member_name",
							member.memberName);
					PreferencesUtils.putString(context, "iconLocation",
							member.iconLocation);
					PreferencesUtils.putString(context, "iconName",
							member.iconName);
					PreferencesUtils.putString(context, "member_phone",
							member.mobile);
					PreferencesUtils.putString(context, "member_email",
							member.email);
					PreferencesUtils.putString(context, "dynamicLocation",
							member.dynamicLocation);
					PreferencesUtils.putString(context, "dynamicName",
							member.dynamicName);
					PreferencesUtils.putString(context, "regionId",
							member.regionId);
					PreferencesUtils.putString(context, "realname",
							member.realname);
					PreferencesUtils.putString(context, "birthday",
							member.birthday);
					PreferencesUtils.putString(context, "sex", member.sex);
					PreferencesUtils.putString(context, "signature",
							member.signature);
					PollingUtils.startPollingService(
							UserAuthenticationActivity.this, 5,
							MessageService.class, MessageService.ACTION);
					UploadAdapter(member.memberId, member.token);
				}
				break;
			case LOGIN_FAILURE:
				if (msg.obj != null) {

				}
				break;
			case SUBMIT_SUCCESS:
				if (msg.obj != null) {
					Cart mCart = (Cart) msg.obj;
					Intent intent = new Intent(UserAuthenticationActivity.this,
							CartActivity.class);
					intent.putExtra("store", mCart.storeInfo);
					startActivity(intent);
					finish();
				}
				break;
			case SUBMIT_FALSE:
				if (msg.obj != null) {

				}
				break;
			}
		};
	};

	/** 显示商家及购物车信息 */
	private void showStoreInfo(Cart cart) {
		Store store = cart.storeInfo;
		imageLoader.displayImage(store.logoLocation + store.logoName,
				storeImage);
		storeName.setText(store.name);
		peopleCount.setText(cart.people
				+ StringUtils.getString(UserAuthenticationActivity.this,
						R.string.people));
		totalPrice.setText("￥" + cart.total);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.get_verify_code:
			// 获取验证码
			String mobile = phoneNumber.getText().toString();
			if (StringUtils.isBlank(mobile)) {
				prompt(StringUtils.getString(UserAuthenticationActivity.this,
						R.string.pls_phone));
			} else {
				validate_mobile = "1";
				UploadGetAdapter(validate_mobile);
			}
			break;
		case R.id.submit_order:
			// 提交订单
			String verifyCodeStr = verifyCode.getText().toString();
			if (StringUtils.isBlank(verifyCodeStr)) {
				prompt(StringUtils.getString(UserAuthenticationActivity.this,
						R.string.pls_verify_code));
			} else {
				if (verify.verifyCode.equals(verifyCodeStr)) {
					// 验证码正确,登陆，提交订单,跳转至购物车或发动态
					UploadLoginAdapter(mAreaCodeDao.getDefault().area_code_value);
				} else {
					prompt(StringUtils.getString(
							UserAuthenticationActivity.this,
							R.string.verify_false));
				}
			}
			break;
		case R.id.login:
			// 登陆
			Intent intent = new Intent(UserAuthenticationActivity.this,
					LoginActivity.class);
			intent.putExtra("code", 100);
			intent.putExtra("store", cart.storeInfo);
			startActivity(intent);
			finish();
			break;
		}
	}

	/** 快速登录请求 */
	private void UploadLoginAdapter(String areaCode) {
		QuickLoginResp resp = new QuickLoginResp(this, handler,
				verify.menuCode, areaCode, phoneNumber.getText().toString());
		resp.asyncInvoke(LOGIN_SUCCESS, LOGIN_FAILURE);
	}

	/** 获取效验码请求 */
	private void UploadGetAdapter(String validate_mobile) {
		Member member = new Member();
		member.mobile = phoneNumber.getText().toString();
		GetVerifyCodeResp resp = new GetVerifyCodeResp(
				UserAuthenticationActivity.this, handler, member,
				mAreaCodeDao.getDefault(), validate_mobile);
		resp.asyncInvoke(SUCCESS, FAILURE);
	}

	/** 更新购物车请求 */
	private void UploadAdapter(String memberId, String token) {
		EditCartResp editCartResp = new EditCartResp(context, handler, token,
				"1", memberId, cart.storeInfo.id, "1", cart.total,
				makeJSONArray(cart.menuList), "");
		editCartResp.asyncInvoke(SUBMIT_SUCCESS, SUBMIT_FALSE);
	}

	private JSONArray makeJSONArray(ArrayList<Menu> menuList) {
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < menuList.size(); i++) {
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("menu_id", menuList.get(i).menu_id);
				jsonObject.put("menu_count",
						String.valueOf(menuList.get(i).menu_count));
				jsonArray.put(i, jsonObject);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return jsonArray;
	}
}
