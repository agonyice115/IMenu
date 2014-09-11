package com.huassit.imenu.android.wxapi;

import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;

import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.biz.SubmitPayOrderResp;
import com.huassit.imenu.android.ui.pay.OnLinePayActivity;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends BaseActivity implements
		IWXAPIEventHandler {

	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

	private IWXAPI api;

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
		prompt(req.openId);
		System.out.println("arg0:" + req.openId + req.transaction
				+ req.getType());
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			// AlertDialog.Builder builder = new AlertDialog.Builder(this);
			// builder.setTitle("提示");
			// builder.setMessage("微信支付结果：" + resp.errCode);
			// builder.show();
			finish();
		}
	}

	@Override
	public int getContentView() {
		return R.layout.on_line_pay;
	}

	@Override
	public void initView() {
		api = WXAPIFactory.createWXAPI(this, "wx76e86073a6e91077");
		api.handleIntent(getIntent(), this);
	}

	// /** 支付成功，向服务器核对，提交支付订单请求 */
	// private void UploadAdapter() {
	// showProgressDialog();
	// SubmitPayOrderResp mOrderResp = new SubmitPayOrderResp(this, handler,
	// token, PreferencesUtils.getString(this, "member_id"),
	// orderInfo.order_id, payment_type, "123");
	// mOrderResp.asyncInvoke(SUCCESS, FAILURE);
	// }

	@Override
	public void initData() {

	}
}