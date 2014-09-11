package com.huassit.imenu.android.service;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.huassit.imenu.android.R;
import com.huassit.imenu.android.biz.HaveNewDynamicResp;
import com.huassit.imenu.android.biz.HaveNewsMessageResp;
import com.huassit.imenu.android.model.API;
import com.huassit.imenu.android.ui.DynamicActivity;
import com.huassit.imenu.android.ui.LoginActivity;
import com.huassit.imenu.android.ui.MessageListActivity;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.StringUtils;

/**
 * 消息服务
 * 
 * @author shangshang
 * 
 */
public class MessageService extends Service {
	public Context context;
	public static final String ACTION = "com.huassit.imenu.android.service.PollingService";
	private Notification mNotification;
	private NotificationManager mManager;
	private Handler handler;
	/** 轮询换取成功 */
	private static final int POLLING_MESSAGE_SUCCESS = 100;
	/** 轮询换取失败 */
	private static final int POLLING_MESSAGE_FALSE = 101;
	/** 获取动态消息提示成功 */
	private static final int POLLING_DYNAMICMESSAGE_SUCCESS = 200;
	/** 获取动态消息提示失败 */
	private static final int POLLING_DYNAMICMESSAGE_FALSE = 201;
	/** 是否有消息1:有0：无 */
	private int have_new;
	/** 新消息数量 */
	private int new_count;
	/** 是否是第一次获取到消息的标石 */
	public static boolean isFirst = true;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		context = getApplicationContext();
		initNotifiManager();
		initHander();

	}

	@Override
	public void onStart(Intent intent, int startId) {
		if (PreferencesUtils.getString(MessageService.this, "token") != null
				&& !PreferencesUtils.getString(MessageService.this, "token")
						.equals("")) {
			new PollingThread().start();
		}
	}

	// 初始化通知栏配置
	private void initNotifiManager() {
		mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		int icon = R.drawable.defult_headimg_circle;
		mNotification = new Notification();
		mNotification.icon = icon;
		mNotification.tickerText = StringUtils.getString(context,
				R.string.iMenu_news);
		mNotification.defaults |= Notification.DEFAULT_SOUND;
		mNotification.flags = Notification.FLAG_AUTO_CANCEL;
	}

	/***
	 * 弹出Notification
	 * 
	 * @param code
	 *            :1-系统消息，2-动态消息
	 */
	@SuppressWarnings("deprecation")
	private void showNotification(int code) {
		mNotification.when = System.currentTimeMillis();
		if (code == 1) {
			// 系统消息操作
			Intent i = new Intent(this, MessageListActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i,
					Intent.FLAG_ACTIVITY_NEW_TASK);
			mNotification.setLatestEventInfo(this,
					getResources().getString(R.string.app_name),
					StringUtils.getString(context, R.string.new_news),
					pendingIntent);
		}
		if (code == 2) {
			// 动态消息操作
			Intent i = new Intent(this, DynamicActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i,
					Intent.FLAG_ACTIVITY_NEW_TASK);
			mNotification.setLatestEventInfo(this,
					getResources().getString(R.string.app_name),
					StringUtils.getString(context, R.string.new_dynamic_news),
					pendingIntent);
		}
		mManager.notify(0, mNotification);
	}

	/** 向Server轮询的异步线程 */
	class PollingThread extends Thread {
		@Override
		public void run() {
			System.out.println("Polling...");
			UploadAdater();
			UploadAdapter();

		}
	}

	@SuppressLint("HandlerLeak")
	private void initHander() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case POLLING_MESSAGE_SUCCESS:
					if (msg.obj != null) {
						JSONObject data = (JSONObject) msg.obj;
						try {
							have_new = Integer.parseInt(data
									.getString("have_new"));
							new_count = Integer.parseInt(data
									.getString("new_count"));
							API.systemMessageCount = new_count;
							if (isFirst) {
								if (have_new == 1) {
									isFirst = false;
									showNotification(1);
								}
							} else {
								if (have_new == 0) {
									isFirst = true;
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					break;
				case POLLING_DYNAMICMESSAGE_SUCCESS:
					if (msg.obj != null) {
						String have_new = (String) msg.obj;
						if (Integer.parseInt(have_new) == 1) {
							// 有新的动态,显示小红点
							// showNotification(2);
							API.isHave = true;
						}
					}
					break;
				case POLLING_DYNAMICMESSAGE_FALSE:
					if (msg.obj != null) {
					}
					break;
				case POLLING_MESSAGE_FALSE:
					if (msg.obj != null) {
					}
					break;
				}
			}
		};
	}

	/** 请求轮询获取是否有新消息提示 */
	private void UploadAdater() {
		HaveNewsMessageResp mHaveNewsResp = new HaveNewsMessageResp(this,
				handler, PreferencesUtils.getString(MessageService.this,
						"member_id"), PreferencesUtils.getString(
						MessageService.this, "token"));
		mHaveNewsResp.asyncInvoke(POLLING_MESSAGE_SUCCESS,
				POLLING_MESSAGE_FALSE);
	}

	/** 请求轮询获取是否有心动态消息，需要在应用头部展示小红点 */
	private void UploadAdapter() {
		HaveNewDynamicResp mDynamicResp = new HaveNewDynamicResp(this, handler,
				PreferencesUtils.getString(MessageService.this, "member_id"),
				PreferencesUtils.getString(MessageService.this, "token"));
		mDynamicResp.asyncInvoke(POLLING_DYNAMICMESSAGE_SUCCESS,
				POLLING_DYNAMICMESSAGE_FALSE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
