package com.huassit.imenu.android.ui.pay;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.android.app.sdk.AliPay;
import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.MyApplication;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.biz.GeneratePayDataResp;
import com.huassit.imenu.android.biz.SubmitPayOrderResp;
import com.huassit.imenu.android.model.OrderInfo;
import com.huassit.imenu.android.ui.cart.PayProblemActivity;
import com.huassit.imenu.android.ui.cart.SuccesActivity;
import com.huassit.imenu.android.util.NumberFormatUtils;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.util.Util;
import com.huassit.imenu.android.view.NavigationView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 在线支付
 * 
 * @author shang_shang
 * 
 */
public class OnLinePayActivity extends BaseActivity implements OnClickListener{

	private NavigationView mNavigationView;
	/** 商家lo_go */
	private ImageView order_store_image;
	/** 商家名称 */
	private TextView order_store_name;
	/** 人数 */
	private TextView order_people;
	/** 订单总价 */
	private TextView order_total;
	/** 订单原价 */
//	private TextView order_before_price;
	/** 手机号码 */
	private TextView information_phone;
	/** 优惠券和积分layout */
	private LinearLayout layout_order_score;
	/** 支付宝layout */
	private LinearLayout layout_pay;
	/** 支付宝支付按钮 */
	private ImageView iv_alipay_pay;
	/** 微信支付layout */
	private LinearLayout layout_pay2;
	/** 微信支付按钮 */
	private ImageView iv_weixin_pay;
	/** 确定支付 */
	private TextView pay_ok_btn;
	/** 订单实体类 */
	private OrderInfo orderInfo;
	private ImageLoader imageLoader;
	private MyApplication application;
	/** 支付类型1：支付宝，2-微信,默认为支付宝 */
	private String payment_type = "1";
	private static final int RQF_PAY = 1;
	private static final int RQF_LOGIN = 2;
	/** 提交支付订单成功标石 */
	private static final int SUBMIT_SUCCESS = 200;
	/** 提交支付订单失败标石 */
	private static final int SUBMIT_FALSE = 201;
	/** 提交支付订单三次标石 */
	private int connectCount = 0;
	/** 微信支付 */
	private IWXAPI api;
	/** 支付遇到问题 */
	private TextView tv_onlinepay_problem;
	private DisplayImageOptions options;
	/** 存储支付宝支付返回结果 */
	private String result;
	/** 标石已点击微信并跳转去支付 */
	private boolean isWX = false;

	public void initView() {
		imageLoader = ImageLoader.getInstance();
		options = MyApplication.getDisplayImageOptions(context, 50);
		application = (MyApplication) getApplicationContext();
		mNavigationView = (NavigationView) findViewById(R.id.navigationView);
		mNavigationView.setNavigateItemVisibility(NavigationView.TITLE,
				View.GONE);
		mNavigationView.setNavigateItemVisibility(
				NavigationView.FIRST_RIGHT_TEXT_VIEW, View.GONE);
		mNavigationView.setNavigateItemText(NavigationView.LEFT_TEXT_VIEW,
				StringUtils.getString(context, R.string.online_pay));
		mNavigationView.setNavigateItemVisibility(
				NavigationView.LEFT_IMAGE_VIEW, View.GONE);
		mNavigationView.setNavigateItemBackground(
				NavigationView.SECOND_RIGHT_TEXT_VIEW, R.drawable.close);
		mNavigationView.setNavigateItemOnClickListener(
				NavigationView.SECOND_RIGHT_TEXT_VIEW,
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		order_store_image = (ImageView) findViewById(R.id.order_store_image);
		order_store_name = (TextView) findViewById(R.id.order_store_name);
		order_people = (TextView) findViewById(R.id.order_people);
		order_total = (TextView) findViewById(R.id.order_total);
//		order_before_price = (TextView) findViewById(R.id.order_before_price);
		information_phone = (TextView) findViewById(R.id.information_phone);
		layout_order_score = (LinearLayout) findViewById(R.id.layout_order_score);
		layout_pay = (LinearLayout) findViewById(R.id.layout_pay);
		iv_alipay_pay = (ImageView) findViewById(R.id.iv_alipay_pay);
		layout_pay2 = (LinearLayout) findViewById(R.id.layout_pay2);
		iv_weixin_pay = (ImageView) findViewById(R.id.iv_weixin_pay);
		pay_ok_btn = (TextView) findViewById(R.id.pay_ok_btn);
		layout_order_score.setOnClickListener(this);
		layout_pay.setOnClickListener(this);
		iv_alipay_pay.setOnClickListener(this);
		layout_pay2.setOnClickListener(this);
		iv_weixin_pay.setOnClickListener(this);
		pay_ok_btn.setOnClickListener(this);
		tv_onlinepay_problem = (TextView) findViewById(R.id.tv_onlinepay_problem);
		tv_onlinepay_problem.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
		tv_onlinepay_problem.setOnClickListener(this);
//		order_before_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);// 删除线
	}

	@Override
	public int getContentView() {
		return R.layout.on_line_pay;
	}

	@Override
	public void initData() {
		if (getIntent().getExtras() != null) {
			orderInfo = (OrderInfo) getIntent().getExtras().getSerializable(
					"orderInfo");
			showData();
		}
	}

	/** 显示基本信息 */
	private void showData() {
		imageLoader.displayImage(orderInfo.mStoreInfo.logoLocation
				+ orderInfo.mStoreInfo.logoName, order_store_image, options);
		order_store_name.setText(orderInfo.mStoreInfo.name);
		String total = NumberFormatUtils.format(Float
				.parseFloat(orderInfo.total));
		order_total.setText("￥" + total);
		order_people.setText(orderInfo.people + StringUtils.getString(context, R.string.people));
		information_phone.setText(changePhone(PreferencesUtils.getString(this,
				"member_phone")));
//		if (!StringUtils.isBlank(orderInfo.originalTotal)) {
//			String originalTotal = NumberFormatUtils.format(Float
//					.parseFloat(orderInfo.originalTotal));
//			order_before_price.setText("￥" + originalTotal);
//		}
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SUCCESS:
				JSONObject data = (JSONObject) msg.obj;
				final String payment_order_param = data
						.optString("payment_order_param");
				if (payment_type.equals("1")) {
					new Thread() {
						public void run() {
							AliPay aliPay = new AliPay(OnLinePayActivity.this,
									handler);
							String result = aliPay.pay(payment_order_param);
							System.out.println("result:==" + result);
							Message msg2 = new Message();
							msg2.what = RQF_PAY;
							msg2.obj = result;
							handler.sendMessage(msg2);
						};
					}.start();
				}
				if (payment_type.equals("2")) {
					try {
						JSONObject jsonObject = new JSONObject(
								payment_order_param);
						sendPayReq(jsonObject);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				break;
			case FAILURE:
				if (msg.obj != null) {
					mNavigationView.showErrorMessage(msg.obj.toString());
				}
				break;
			case RQF_PAY:
			case RQF_LOGIN:
				// 支付宝支付返回结果
				Result result1 = new Result((String) msg.obj);
				result = msg.obj.toString();
				showProgressDialog(StringUtils.getString(context, R.string.wait_getting_result));
				UploadAdapter(result);
				break;
			case SUBMIT_SUCCESS:
				// 提交支付成功,跳转支付成功页面
				closeProgressDialog();
				OrderInfo info = (OrderInfo) msg.obj;
				if (info.order_type.equals("21")) {
					Intent intent = new Intent(OnLinePayActivity.this,
							SuccesActivity.class);
					intent.putExtra("whatSuccess", 1);
					intent.putExtra("orderInfo", orderInfo);
					startActivity(intent);
					finish();
				} else {
					handler.sendEmptyMessage(SUBMIT_FALSE);
				}
				break;
			case SUBMIT_FALSE:
				// 提交支付失败,跳转支付遇到问题界面
				if (connectCount < 2) {
					connectCount++;
					UploadAdapter(result);
					return;
				}
				connectCount = 0;
				Intent intent1 = new Intent(OnLinePayActivity.this,
						PayProblemActivity.class);
				intent1.putExtra("orderInfo", orderInfo);
				startActivity(intent1);
				closeProgressDialog();
				finish();
				break;
			}
		};
	};
	
	/** 生成支付数据请求 */
	private void UploadAdapter() {
		showProgressDialog("正在加载");
		GeneratePayDataResp mDataResp = new GeneratePayDataResp(this, handler,
				token, payment_type, PreferencesUtils.getString(this,
						"member_id"), orderInfo.order_id);
		mDataResp.asyncInvoke(SUCCESS, FAILURE);
	}

	/** 支付成功，向服务器核对，提交支付订单请求 */
	private void UploadAdapter(String payment_result) {
		SubmitPayOrderResp mOrderResp = new SubmitPayOrderResp(this, handler,
				token, PreferencesUtils.getString(this, "member_id"),
				orderInfo.order_id, payment_type, payment_result);
		mOrderResp.asyncInvoke(SUBMIT_SUCCESS, SUBMIT_FALSE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_order_score:
			/** 优惠券、积分 */
			break;
		case R.id.layout_pay:
			/** 支付宝支付layout */
		case R.id.iv_alipay_pay:
			/** 支付宝对勾 */
			payment_type = "1";
			iv_alipay_pay
					.setImageResource(R.drawable.icon_onlinepay_chonse_down);
			iv_weixin_pay.setImageResource(R.drawable.icon_onlinepay_chonse_up);
			break;
		case R.id.layout_pay2:
			/** 微信支付layout */
		case R.id.iv_weixin_pay:
			/** 微信对勾 */
			payment_type = "2";
			iv_alipay_pay.setImageResource(R.drawable.icon_onlinepay_chonse_up);
			iv_weixin_pay
					.setImageResource(R.drawable.icon_onlinepay_chonse_down);
			break;
		case R.id.pay_ok_btn:
			/** 确定支付 */
			if (payment_type.equals("2")) {
				isWX = true;
			}
			UploadAdapter();
			break;
		case R.id.tv_onlinepay_problem:
			/** 支付遇到问题 */
			Intent intent = new Intent(OnLinePayActivity.this,
					PayProblemActivity.class);
			intent.putExtra("orderInfo", orderInfo);
			startActivity(intent);
			break;
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

	private String genSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		int i = 0;
		for (; i < params.size() - 1; i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append(params.get(i).getName());
		sb.append('=');
		sb.append(params.get(i).getValue());

		String sha1 = Util.sha1(sb.toString());
		System.out.println("sign:" + sha1);
		return sha1;
	}

	private void sendPayReq(JSONObject obj) {
		api = WXAPIFactory.createWXAPI(context, obj.optString("appid"));
		PayReq req = new PayReq();
		req.appId = obj.optString("appid");
		req.partnerId = obj.optString("partnerid");
		req.prepayId = obj.optString("prepayid");
		req.nonceStr = obj.optString("noncestr");
		req.timeStamp = obj.optString("timestamp");
		req.packageValue = "Sign=" + obj.optString("sign");
		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams
				.add(new BasicNameValuePair("appkey", obj.optString("appkey")));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
		// signParams.add(new BasicNameValuePair("package", "Sign=WXPay"));
		req.sign = genSign(signParams);
		// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
		api.registerApp(req.appId);
		api.sendReq(req);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (isWX) {
			isWX = false;
			if (payment_type.equals("2")) {
				UploadAdapter("123");
			}
		}
	}
}
