package com.huassit.imenu.android.ui.settings;

import java.io.File;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Map;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.biz.CheckClientConfigResp;
import com.huassit.imenu.android.db.dao.AboutInfoDao;
import com.huassit.imenu.android.http.HttpInvoker;
import com.huassit.imenu.android.model.AboutInfo;
import com.huassit.imenu.android.model.BaseModel;
import com.huassit.imenu.android.model.ClientConfig;
import com.huassit.imenu.android.model.KeyValuePair;
import com.huassit.imenu.android.model.Version;
import com.huassit.imenu.android.ui.SubimtFeedbackActivity;
import com.huassit.imenu.android.ui.WebViewActivity;
import com.huassit.imenu.android.util.PackageUtils;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.view.NavigationView;

/**
 * Created by Sylar on 14-7-15.
 */
public class AboutSettingActivity extends BaseActivity implements
		View.OnClickListener {
	private static final String WEBSITE_KEY = "website";
	private static final String WECHAT_KEY = "wechat";

	private TextView currentVersion;
	private AboutInfoDao aboutInfoDao;
	private AboutInfo officialWebSite;
	private AboutInfo officialWeChat;
	private NavigationView navigationView;
	private String needUpdate;
	private ProgressDialog downloadDialog;

	@Override
	public int getContentView() {
		return R.layout.about_setting_activity;
	}

	@Override
	public void initView() {
		aboutInfoDao = new AboutInfoDao(this);

		navigationView = (NavigationView) findViewById(R.id.navigationView);
		navigationView.setNavigateItemText(NavigationView.TITLE,
				R.string.about_setting);
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

		currentVersion = (TextView) findViewById(R.id.currentVersion);
		findViewById(R.id.feedbackLayout).setOnClickListener(this);
		findViewById(R.id.checkVersionLayout).setOnClickListener(this);
		findViewById(R.id.officialWebSite).setOnClickListener(this);
		findViewById(R.id.officialWeChat).setOnClickListener(this);
		findViewById(R.id.serviceTerms).setOnClickListener(this);
		setDownloadDialog();
	}

	@Override
	public void initData() {
		currentVersion.setText(getString(R.string.current_version,
				PackageUtils.getAppVersionName(this)));
		officialWebSite = aboutInfoDao.getAboutInfoByKey(WEBSITE_KEY);
		officialWeChat = aboutInfoDao.getAboutInfoByKey(WECHAT_KEY);
	}

	private ClientConfig getConfig() {
		ClientConfig config = new ClientConfig();
		config.region = PreferencesUtils.getString(this, "regionVersion", "1");
		config.category = PreferencesUtils.getString(this, "categoryVersion",
				"1");
		config.service = PreferencesUtils
				.getString(this, "serviceVersion", "1");
		config.menu_unit = PreferencesUtils.getString(this, "menuUnitVersion",
				"1");
		config.menu_taste = PreferencesUtils.getString(this,
				"menuTasteVersion", "1");
		config.area_code = PreferencesUtils.getString(this, "areaCodeVersion",
				"1");
		config.share_menu = PreferencesUtils.getString(this,
				"shareMenuVersion", "1");
		config.share_store = PreferencesUtils.getString(this,
				"shareStoreVersion", "1");
		config.share_dynamic = PreferencesUtils.getString(this,
				"shareDynamicVersion", "1");
		config.client_skin = PreferencesUtils.getString(this,
				"clientSkinVersion", "1");
		config.feed_back = PreferencesUtils.getString(this, "feedbackVersion",
				"1");
		config.about_info = PreferencesUtils.getString(this,
				"aboutInfoVersion", "1");
		config.mobile_token = PackageUtils.getUniqueId(this);
		config.version_android = PackageUtils.getAppVersionName(this);
		config.device_type = "2";// 设备类型:10-iPhone/11-iPad/2-Android
		return config;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.feedbackLayout:
			Intent intent = new Intent(this, SubimtFeedbackActivity.class);
			startActivity(intent);
			break;
		case R.id.checkVersionLayout:
			showProgressDialog(getString(R.string.check_new_version));
			CheckClientConfigResp resp = new CheckClientConfigResp(this,
					handler, getConfig());
			resp.asyncInvoke(SUCCESS, FAILURE);
			break;
		case R.id.officialWebSite:
			Intent websiteIntent = new Intent(this, WebViewActivity.class);
			websiteIntent.putExtra(WebViewActivity.URL, officialWebSite.value);
			websiteIntent.putExtra(WebViewActivity.NAME,
					getString(R.string.official_website));
			startActivity(websiteIntent);
			break;
		case R.id.officialWeChat:
			Intent weChatIntent = new Intent(this, WebViewActivity.class);
			weChatIntent.putExtra(WebViewActivity.URL, officialWeChat.value);
			weChatIntent.putExtra(WebViewActivity.NAME,
					getString(R.string.official_wechat));
			startActivity(weChatIntent);
			break;
		case R.id.serviceTerms:
			Intent serviceTerms = new Intent(this, WebViewActivity.class);
			startActivity(serviceTerms);
			break;
		}
	}

	private void setDownloadDialog() {
		downloadDialog = new ProgressDialog(AboutSettingActivity.this);
		downloadDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		downloadDialog.setTitle(R.string.dataasylistadapter_in_the_download);
		downloadDialog.setIndeterminate(false);
		downloadDialog.setCancelable(false);
		downloadDialog.setCanceledOnTouchOutside(false);
	}

	/** singIn更新APK */
	private void UpdateApk(final String link) {
		AlertDialog.Builder builer = new Builder(AboutSettingActivity.this);
		builer.setTitle(R.string.singin_version_update);
		builer.setCancelable(false);
		builer.setMessage(R.string.singin_download_the_new_version);
		// 当点确定按钮时从服务器上下载 新的apk 然后安装
		builer.setPositiveButton(R.string.singin_yes_download_version,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, final int which) {
						dialog.dismiss();
						new Thread() {
							@Override
							public void run() {
								downloadNewVersion(link);
								super.run();
							}
						}.start();
					}
				});
		if ("0".equals(needUpdate)) {
			builer.setNegativeButton(R.string.cancel,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
		}
		AlertDialog dialog = builer.create();
		dialog.show();
	}

	private void downloadNewVersion(final String url) {
		handler.sendEmptyMessage(DownloadManager.STATUS_PENDING);
		HttpInvoker invoker = new HttpInvoker();
		InputStream in = null;
		RandomAccessFile randomAccessFile = null;
		try {
			File dir = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(dir, "iMenu.dl");
			File newFile = new File(dir, "iMenu.apk");
			if (newFile.exists()) {
				newFile.delete();
			}
			long downloadedSize = 0;
			if (file.exists()) {
				downloadedSize = file.length();
			}
			HttpGet httpGet = new HttpGet(url);
			httpGet.setHeader("range", "bytes=" + downloadedSize + "-");
			HttpResponse response = invoker.executeHttpRequest(httpGet);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK
					|| response.getStatusLine().getStatusCode() == HttpStatus.SC_PARTIAL_CONTENT) {
				Header header = response.getFirstHeader("Content-Length");
				long totalLength = Long.parseLong(header.getValue());
				totalLength += downloadedSize;
				in = response.getEntity().getContent();
				randomAccessFile = new RandomAccessFile(file, "rw");
				randomAccessFile.seek(downloadedSize);
				byte[] bytes = new byte[1024 * 8];
				int readCount;
				int progress;
				while ((readCount = in.read(bytes)) != -1) {
					randomAccessFile.write(bytes, 0, readCount);
					progress = (int) (((float) randomAccessFile.length() / (float) totalLength) * 100);
					downloadDialog.setProgress(progress);
				}
				file.renameTo(newFile);
				handler.sendMessage(handler.obtainMessage(
						DownloadManager.STATUS_SUCCESSFUL,
						newFile.getAbsolutePath()));
			} else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_REQUESTED_RANGE_NOT_SATISFIABLE) {
				HttpGet getMethod = new HttpGet(url);
				HttpResponse httpResponse = invoker
						.executeHttpRequest(getMethod);
				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					Header header = httpResponse
							.getFirstHeader("Content-Length");
					long totalLength = Long.parseLong(header.getValue());
					if (totalLength == downloadedSize) {
						file.renameTo(newFile);
						handler.sendMessage(handler.obtainMessage(
								DownloadManager.STATUS_SUCCESSFUL,
								newFile.getAbsolutePath()));
					} else {
						handler.sendEmptyMessage(DownloadManager.STATUS_FAILED);
					}
				} else if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_REQUEST_TIMEOUT) {
					handler.sendEmptyMessage(DownloadManager.STATUS_FAILED);
				} else {
					file.delete();
					handler.sendEmptyMessage(DownloadManager.STATUS_FAILED);
				}
			} else {
				handler.sendEmptyMessage(DownloadManager.STATUS_FAILED);
			}
		} catch (IOException e) {
			e.printStackTrace();
			handler.sendEmptyMessage(DownloadManager.STATUS_FAILED);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				if (randomAccessFile != null) {
					randomAccessFile.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/** 显示无需更新对话框 */
	protected void showNotUpdataDialog() {
		AlertDialog.Builder builer = new Builder(this);
		builer.setTitle("提示");
		builer.setMessage("已经是最新版本！");
		builer.setPositiveButton("确定", null);
		AlertDialog dialog = builer.create();
		dialog.setCancelable(false);
		dialog.show();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			closeProgressDialog();
			switch (msg.what) {
			case SUCCESS:
				Map<KeyValuePair, List<? extends BaseModel>> configMap = (Map<KeyValuePair, List<? extends BaseModel>>) msg.obj;
				saveToPreference(configMap);
				String url = PreferencesUtils.getString(context,
						"version_android_url");
				needUpdate = PreferencesUtils.getString(context, "need_update");
				if (!PackageUtils.getAppVersionName(AboutSettingActivity.this)
						.equals(PreferencesUtils.getString(
								AboutSettingActivity.this, "version_android"))) {
					if (!StringUtils.isBlank(url)) {
						UpdateApk(url);
					} else {
						showNotUpdataDialog();
					}
				} else {
					showNotUpdataDialog();
				}
				break;
			case FAILURE:

				break;
			case DownloadManager.STATUS_RUNNING:
				if (downloadDialog.isShowing()) {
					downloadDialog.setProgress((Integer) msg.obj);
				}
				break;
			case DownloadManager.STATUS_SUCCESSFUL:
				if (downloadDialog.isShowing()) {
					downloadDialog.dismiss();
				}
				boolean result = PackageUtils.installNormal(
						AboutSettingActivity.this, (String) msg.obj);
				break;
			case DownloadManager.STATUS_FAILED:
				if (downloadDialog.isShowing()) {
					downloadDialog.dismiss();
				}
				prompt(StringUtils.getString(AboutSettingActivity.this,
						R.string.download_new_version_failed));
				break;
			case DownloadManager.STATUS_PENDING:
				if (!downloadDialog.isShowing()) {
					downloadDialog.show();
				}
				break;
			}
		}
	};

	public void saveToPreference(
			Map<KeyValuePair, List<? extends BaseModel>> configMap) {
		for (KeyValuePair keyValuePair : configMap.keySet()) {
			List list = configMap.get(keyValuePair);
			if (list != null && list.size() > 0) {
				if (keyValuePair.key.equals("version_android")) {
					PreferencesUtils.putString(context, "version_android",
							keyValuePair.value);
					PreferencesUtils.putString(context, "version_android_url",
							((Version) list.get(0)).url);
				} else if (keyValuePair.key.equals("need_update")) {
					PreferencesUtils.putString(context, "need_update",
							keyValuePair.value);
				}
			}
		}
	}

}
