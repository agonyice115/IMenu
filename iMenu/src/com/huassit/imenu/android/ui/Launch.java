package com.huassit.imenu.android.ui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.biz.CheckClientConfigResp;
import com.huassit.imenu.android.biz.GetFrontPageResp;
import com.huassit.imenu.android.db.dao.AboutInfoDao;
import com.huassit.imenu.android.db.dao.AreaCodeDao;
import com.huassit.imenu.android.db.dao.CategoryDao;
import com.huassit.imenu.android.db.dao.ClientSkinDao;
import com.huassit.imenu.android.db.dao.EnvironmentDao;
import com.huassit.imenu.android.db.dao.FeedbackDao;
import com.huassit.imenu.android.db.dao.MenuTasteDao;
import com.huassit.imenu.android.db.dao.MenuUnitDao;
import com.huassit.imenu.android.db.dao.RegionDao;
import com.huassit.imenu.android.db.dao.ReturnReasonDao;
import com.huassit.imenu.android.db.dao.ServiceDao;
import com.huassit.imenu.android.db.dao.ShareDynamicDao;
import com.huassit.imenu.android.db.dao.ShareMemberDao;
import com.huassit.imenu.android.db.dao.ShareMenuDao;
import com.huassit.imenu.android.db.dao.ShareStoreDao;
import com.huassit.imenu.android.http.HttpInvoker;
import com.huassit.imenu.android.model.BaseModel;
import com.huassit.imenu.android.model.Category;
import com.huassit.imenu.android.model.ClientConfig;
import com.huassit.imenu.android.model.ClientSkin;
import com.huassit.imenu.android.model.Dynamic;
import com.huassit.imenu.android.model.KeyValuePair;
import com.huassit.imenu.android.model.Member;
import com.huassit.imenu.android.model.Menu;
import com.huassit.imenu.android.model.Store;
import com.huassit.imenu.android.model.Version;
import com.huassit.imenu.android.ui.settings.AboutSettingActivity;
import com.huassit.imenu.android.util.LocationUtils;
import com.huassit.imenu.android.util.PackageUtils;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class Launch extends BaseActivity {
	/** 获取导航页成功 */
	private static final int FRONT_SUCCESS = 1000;
	/** 获取导航页失败 */
	private static final int FRONT_FAILURE = 1001;
	/** 皮肤默认值 */
	private static final String SKIN_DEFAULT_VALUE = "1";
	private RegionDao mRegionDao;
	private CategoryDao mCategoryDao;
	private AreaCodeDao mAreaCodeDao;
	private FeedbackDao mFeedbackDao;
	private ReturnReasonDao mReturnReasonDao;
	private MenuUnitDao mMenuUnitDao;
	private MenuTasteDao mMenuTasteDao;
	private ClientSkinDao mClientSkinDao;
	private EnvironmentDao mEnvironmentDao;
	private AboutInfoDao mAboutInfoDao;
	private ShareDynamicDao mShareDynamicDao;
	private ShareMemberDao mShareMemberDao;
	private ShareMenuDao mShareMenuDao;
	private ShareStoreDao mShareStoreDao;
	private ServiceDao mServiceDao;
	private FrameLayout frontLayout;
	private ImageView frontImageView;
	private TextView memberNameTextView;
	private TextView storeNameTextView;
	private TextView menuNameTextView;
	private ImageView launchBackground;
	private AlertDialog alertDialog;
	private ImageView storeCategoryImageView;
	private LocationManager locationManager;
	private ProgressDialog downloadDialog;

	@Override
	protected void onResume() {
		super.onResume();
		setDownloadDialog();
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			alertDialog.show();
		} else {
			loadGlobalConfig();
		}
	}

	@Override
	public int getContentView() {
		return R.layout.launch;
	}

	private void createGpsDialog() {
		alertDialog = new AlertDialog.Builder(this)
				.setTitle(R.string.prompt)
				.setMessage(R.string.gps_not_available)
				.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								loadGlobalConfig();
							}
						})
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								startActivity(new Intent(
										Settings.ACTION_LOCATION_SOURCE_SETTINGS));
							}
						}).setCancelable(false).create();
	}

	@Override
	public void initView() {
		mRegionDao = new RegionDao(this);
		mCategoryDao = new CategoryDao(this);
		mAreaCodeDao = new AreaCodeDao(this);
		mFeedbackDao = new FeedbackDao(this);
		mMenuUnitDao = new MenuUnitDao(this);
		mMenuTasteDao = new MenuTasteDao(this);
		mClientSkinDao = new ClientSkinDao(this);
		mEnvironmentDao = new EnvironmentDao(this);
		mAboutInfoDao = new AboutInfoDao(this);
		mShareDynamicDao = new ShareDynamicDao(this);
		mShareMemberDao = new ShareMemberDao(this);
		mShareMenuDao = new ShareMenuDao(this);
		mShareStoreDao = new ShareStoreDao(this);
		mServiceDao = new ServiceDao(this);
		mReturnReasonDao = new ReturnReasonDao(this);
		LocationUtils.getInstance(getApplicationContext()).requestLocation();
		frontImageView = (ImageView) findViewById(R.id.frontImageView);
		frontLayout = (FrameLayout) findViewById(R.id.frontLayout);
		memberNameTextView = (TextView) findViewById(R.id.memberNameTextView);
		storeNameTextView = (TextView) findViewById(R.id.menuNameTextView);
		menuNameTextView = (TextView) findViewById(R.id.storeNameTextView);
		launchBackground = (ImageView) findViewById(R.id.launchBackground);
		storeCategoryImageView = (ImageView) findViewById(R.id.storeCategoryImageView);
		locationManager = (LocationManager) context.getApplicationContext()
				.getSystemService(Context.LOCATION_SERVICE);
		createGpsDialog();
	}

	@Override
	public void initData() {
	}

	private void loadFrontPage() {
		GetFrontPageResp resp = new GetFrontPageResp(context, handler);
		resp.asyncInvoke(FRONT_SUCCESS, FRONT_FAILURE);
	}

	private void loadGlobalConfig() {
		// 检查客户端配置文件 并缓存至数据库
		new Thread(new Runnable() {
			@Override
			public void run() {
				ClientConfig config = makeConfig();
				CheckClientConfigResp configResp = new CheckClientConfigResp(
						Launch.this, handler, config);
				configResp.asyncInvoke(SUCCESS, FAILURE);
			}
		}).start();
	}

//	protected void showUpdataDialog(String link, String needUpdate) {
//		final String linkadress = link;
//		AlertDialog.Builder builder = new Builder(this);
//		builder.setTitle("版本升级");
//		builder.setMessage("有新版本更新,请下载！");
//		/** 当点确定按钮时从服务器上下载 新的apk 然后安装 */
//		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int which) {
//				Uri uri = Uri.parse(linkadress);
//				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//				startActivity(intent);
//				System.exit(0);
//				android.os.Process.killProcess(android.os.Process.myPid());
//			}
//		});
//		if ("0".equals(needUpdate)) {
//			// 非强制更新
//			builder.setNegativeButton("取消",
//					new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface dialog, int which) {
//							dialog.dismiss();
//							loadFrontPage();
//						}
//					});
//		}
//		AlertDialog dialog = builder.create();
//		dialog.setCancelable(false);
//		dialog.show();
//	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FAILURE:
				loadFrontPage();
				break;
			case SUCCESS:
				Map<KeyValuePair, List<? extends BaseModel>> configMap = (Map<KeyValuePair, List<? extends BaseModel>>) msg.obj;
				saveToDB(configMap);
				if (StringUtils.isBlank(colorValue)) {
					ClientSkin defaultSkin = mClientSkinDao
							.getDefaultSkin(SKIN_DEFAULT_VALUE);
					if (defaultSkin != null) {
						PreferencesUtils.putString(context, "CurrentSkinId",
								defaultSkin.client_skin_id);
						PreferencesUtils.putString(context, "ColorValue",
								defaultSkin.client_skin_value);
					} else {
						PreferencesUtils.putString(context, "CurrentSkinId",
								"1");
						PreferencesUtils.putString(context, "ColorValue",
								"#008fff");
					}
				}
				String url = PreferencesUtils.getString(context,
						"version_android_url");
				String needUpdate = PreferencesUtils.getString(context,
						"need_update");
				if (!PackageUtils.getAppVersionName(Launch.this).equals(
						PreferencesUtils.getString(Launch.this,
								"version_android"))) {
					if (!StringUtils.isBlank(url)) {
						UpdateApk(url, needUpdate);
					} else {
						loadFrontPage();
					}
				} else {
					loadFrontPage();
				}
				break;
			case FRONT_FAILURE:
				startActivity(new Intent(Launch.this, MainActivity.class));
				finish();
				break;
			case FRONT_SUCCESS:
				Map<String, BaseModel> result = (Map<String, BaseModel>) msg.obj;
				Store store = (Store) result.get("store_info");
				Member member = (Member) result.get("member_info");
				Menu menu = (Menu) result.get("menu_info");
				Dynamic dynamic = (Dynamic) result.get("dynamic_info");
				Category category = mCategoryDao
						.getCategoryById(store.categoryId);
				frontLayout.setVisibility(View.VISIBLE);
				launchBackground
						.setBackgroundResource(R.drawable.frontpage_logo);
				ImageLoader.getInstance().displayImage(
						menu.menu_image_location + menu.menu_image_name,
						frontImageView);
				ImageLoader.getInstance().displayImage(category.category_image,
						storeCategoryImageView);
				storeNameTextView.setText(store.name);
				memberNameTextView.setText(member.memberName);
				menuNameTextView.setText(menu.menu_name);
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						startActivity(new Intent(Launch.this,
								MainActivity.class));
						finish();
					}
				}, 3000);
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
				boolean result1 = PackageUtils.installNormal(
						Launch.this, (String) msg.obj);
				break;
			case DownloadManager.STATUS_FAILED:
				if (downloadDialog.isShowing()) {
					downloadDialog.dismiss();
				}
				prompt(StringUtils.getString(Launch.this,
						R.string.download_new_version_failed));
				break;
			case DownloadManager.STATUS_PENDING:
				if (!downloadDialog.isShowing()) {
					downloadDialog.show();
				}
				break;
			default:
				break;
			}
		}
	};

	public void saveToDB(Map<KeyValuePair, List<? extends BaseModel>> configMap) {
		for (KeyValuePair keyValuePair : configMap.keySet()) {
			List list = configMap.get(keyValuePair);
			if (list != null && list.size() > 0) {
				if (keyValuePair.key.equals("region")) {
					mRegionDao.bulkSaveOrUpdate(list);
					PreferencesUtils.putString(this, "regionVersion",
							keyValuePair.value);
				} else if (keyValuePair.key.equals("category")) {
					mCategoryDao.bulkSaveOrUpdate(list);
					PreferencesUtils.putString(this, "categoryVersion",
							keyValuePair.value);
				} else if (keyValuePair.key.equals("environment")) {
					mEnvironmentDao.bulkSaveOrUpdate(list);
					PreferencesUtils.putString(this, "environmentVersion",
							keyValuePair.value);
				} else if (keyValuePair.key.equals("service")) {
					mServiceDao.bulkSaveOrUpdate(list);
					PreferencesUtils.putString(this, "serviceVersion",
							keyValuePair.value);
				} else if (keyValuePair.key.equals("menu_unit")) {
					mMenuUnitDao.bulkSaveOrUpdate(list);
					PreferencesUtils.putString(this, "menuUnitVersion",
							keyValuePair.value);
				} else if (keyValuePair.key.equals("menu_taste")) {
					mMenuTasteDao.bulkSaveOrUpdate(list);
					PreferencesUtils.putString(this, "menuTasteVersion",
							keyValuePair.value);
				} else if (keyValuePair.key.equals("area_code")) {
					mAreaCodeDao.bulkSaveOrUpdate(list);
					PreferencesUtils.putString(this, "areaCodeVersion",
							keyValuePair.value);
				} else if (keyValuePair.key.equals("share_menu")) {
					mShareMenuDao.bulkSaveOrUpdate(list);
					PreferencesUtils.putString(this, "shareMenuVersion",
							keyValuePair.value);
				} else if (keyValuePair.key.equals("share_member")) {
					mShareMemberDao.bulkSaveOrUpdate(list);
					PreferencesUtils.putString(this, "shareStoreVersion",
							keyValuePair.value);
				} else if (keyValuePair.key.equals("share_store")) {
					mShareStoreDao.bulkSaveOrUpdate(list);
					PreferencesUtils.putString(this, "shareMemberVersion",
							keyValuePair.value);
				} else if (keyValuePair.key.equals("share_dynamic")) {
					mShareDynamicDao.bulkSaveOrUpdate(list);
					PreferencesUtils.putString(this, "shareDynamicVersion",
							keyValuePair.value);
				} else if (keyValuePair.key.equals("client_skin")) {
					mClientSkinDao.bulkSaveOrUpdate(list);
					PreferencesUtils.putString(this, "clientSkinVersion",
							keyValuePair.value);
				} else if (keyValuePair.key.equals("feedback")) {
					mFeedbackDao.bulkSaveOrUpdate(list);
					PreferencesUtils.putString(this, "feedbackVersion",
							keyValuePair.value);
				} else if (keyValuePair.key.equals("about_info")) {
					mAboutInfoDao.bulkSaveOrUpdate(list);
					PreferencesUtils.putString(this, "aboutInfoVersion",
							keyValuePair.value);
				} else if (keyValuePair.key.equals("return_reason")) {
					mReturnReasonDao.bulkSaveOrUpdate(list);
					PreferencesUtils.putString(this, "returnReasonVersion",
							keyValuePair.value);
				} else if (keyValuePair.key.equals("version_android")) {
					PreferencesUtils.putString(context, "version_android",
							keyValuePair.value);
					PreferencesUtils.putString(context, "version_android_url",
							((Version) list.get(0)).url);
					PreferencesUtils.putString(context, "version_android",
							((Version) list.get(0)).version);
				} else if (keyValuePair.key.equals("need_update")) {
					PreferencesUtils.putString(context, "need_update",
							keyValuePair.value);
				}
			}
		}
	}

	private ClientConfig makeConfig() {
		ClientConfig config = new ClientConfig();
		// 获取所有的版本号
		config.region = PreferencesUtils.getString(this, "regionVersion", "0");
		config.category = PreferencesUtils.getString(this, "categoryVersion",
				"0");
		config.environment = PreferencesUtils.getString(this,
				"environmentVersion", "0");
		config.service = PreferencesUtils
				.getString(this, "serviceVersion", "0");
		config.menu_unit = PreferencesUtils.getString(this, "menuUnitVersion",
				"0");
		config.menu_taste = PreferencesUtils.getString(this,
				"menuTasteVersion", "0");
		config.area_code = PreferencesUtils.getString(this, "areaCodeVersion",
				"0");
		config.share_menu = PreferencesUtils.getString(this,
				"shareMenuVersion", "0");
		config.share_store = PreferencesUtils.getString(this,
				"shareStoreVersion", "0");
		config.share_dynamic = PreferencesUtils.getString(this,
				"shareDynamicVersion", "0");
		config.client_skin = PreferencesUtils.getString(this,
				"clientSkinVersion", "0");
		config.feed_back = PreferencesUtils.getString(this, "feedbackVersion",
				"0");
		config.share_member = PreferencesUtils.getString(this,
				"shareMemberVersion", "0");
		config.about_info = PreferencesUtils.getString(this,
				"aboutInfoVersion", "0");
		config.return_reason = PreferencesUtils.getString(this,
				"returnReasonVersion", "0");
		config.mobile_token = PackageUtils.getUniqueId(this);
		config.version_android = PackageUtils.getAppVersionName(this);
		config.device_type = "2";// 设备类型:10-iPhone/11-iPad/2-Android
		return config;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	private void setDownloadDialog() {
		downloadDialog = new ProgressDialog(Launch.this);
		downloadDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		downloadDialog.setTitle(R.string.dataasylistadapter_in_the_download);
		downloadDialog.setIndeterminate(false);
		downloadDialog.setCancelable(false);
		downloadDialog.setCanceledOnTouchOutside(false);
	}
	
	/** singIn更新APK */
	private void UpdateApk(final String link,String needUpdate) {
		AlertDialog.Builder builer = new Builder(Launch.this);
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
							loadFrontPage();
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
}
