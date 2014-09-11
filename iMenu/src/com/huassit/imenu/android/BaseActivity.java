package com.huassit.imenu.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;
import com.huassit.imenu.android.util.ActivityStackManager;
import com.huassit.imenu.android.util.NetStateUtil;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.view.LoadingDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

public abstract class BaseActivity extends Activity {

	// private ProgressDialog dialog;
	public Context context;
	public boolean isLogin;
	protected final static int SUCCESS = 100;
	protected final static int FAILURE = 101;
	protected String token;
	protected String colorValue;
	private NetworkChangedReceiver receiver;
	private AlertDialog dialog2;
	private LoadingDialog loadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityStackManager.getActivityManager().push(this);
		context = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(getContentView());
		token = PreferencesUtils.getString(context, "token");
		colorValue = PreferencesUtils.getString(context, "ColorValue");
		receiver = new NetworkChangedReceiver();
		initView();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		token = PreferencesUtils.getString(context, "token");
		colorValue = PreferencesUtils.getString(context, "ColorValue");
		registerReceiver(receiver, new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION));
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}

	/**
	 * 返回当前所需要的布局文件 *
	 */
	public abstract int getContentView();

	/**
	 * 统一初始化view的方法
	 * 
	 * @author Sylar *
	 */
	public abstract void initView();

	/**
	 * 统一初始化数据的方法
	 * 
	 * @author Sylar *
	 */
	public abstract void initData();

	/**
	 * 非阻塞提示方式 *
	 */
	public void prompt(String content) {
		Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 进行耗时阻塞操作时,需要调用改方法,显示等待效果
	 * 
	 * @author Sylar *
	 */
	public void showProgressDialog(String content) {
		// if (dialog == null) {
		// dialog = new ProgressDialog(this);
		// dialog.setCancelable(false);
		// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// }
		// dialog.setMessage(content);
		// if (!isFinishing() && !dialog.isShowing()) {
		// dialog.show();
		// }
		if (loadingDialog == null) {
			loadingDialog = new LoadingDialog(this,content);
		}
		if (!isFinishing() && !loadingDialog.isShowing()) {
			loadingDialog.show();
		}
	}

//	public void showProgressDialog() {
//		if (loadingDialog == null) {
//			loadingDialog = new LoadingDialog(this,content);
//		}
//		if (!isFinishing() && !loadingDialog.isShowing()) {
//			loadingDialog.show();
//		}
//	}

	/**
	 * 耗时阻塞操作结束时,需要调用改方法,关闭等待效果
	 * 
	 * @author Sylar *
	 */
	public void closeProgressDialog() {
		// if (dialog != null && !isFinishing()) {
		// dialog.cancel();
		// dialog = null;
		// }
		if (loadingDialog != null && !isFinishing()) {
			loadingDialog.dismiss();
		}
	}

	/**
	 * 显示对户框，只有一个确定按钮
	 */
	public AlertDialog showDialog(Activity context, String msg,
			OnClickListener lister) {
		dialog2 = new AlertDialog.Builder(context).create();
		dialog2.setButton("确定", lister);
		dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog2.setMessage(msg);
		dialog2.show();
		return dialog2;
	}

	/***
	 * token过期显示对话框，两个按钮
	 * @param context
	 * @param msg
	 * @param lister确定
	 * @param lister2取消
	 * @return
	 */
	public void showTokenDialog(Activity context, String msg,
			OnClickListener lister) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setNegativeButton("取消", null);
		builder.setPositiveButton("确定", lister);
		builder.setMessage(msg);
		builder.setTitle("提示");
		AlertDialog dialog = builder.create();
		dialog.setCancelable(false);
		dialog.show();
	}

	private class NetworkChangedReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					ConnectivityManager.CONNECTIVITY_ACTION)) {
				// 仅在Wi-Fi下加载图片
				if (PreferencesUtils.getBoolean(context, "LoadImageOnWifiOnly",
						true)) {
					if (NetStateUtil.isWifiConnected(context)) {
						ImageLoader.getInstance().denyNetworkDownloads(false);
					} else {
						ImageLoader.getInstance().denyNetworkDownloads(true);
					}
				} else {
					if (NetStateUtil.isNetConnected(context)) {
						ImageLoader.getInstance().denyNetworkDownloads(false);
					} else {
						ImageLoader.getInstance().denyNetworkDownloads(true);
					}
				}
			}
		}
	}
}
