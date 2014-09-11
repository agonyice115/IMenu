package com.huassit.imenu.android.ui;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.MyApplication;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.biz.EditDynamicResp;
import com.huassit.imenu.android.biz.EditMenuImageResp;
import com.huassit.imenu.android.biz.GetDynamicDetailResp;
import com.huassit.imenu.android.model.*;
import com.huassit.imenu.android.ui.camera.CameraActivity;
import com.huassit.imenu.android.ui.camera.PictureDetailsActivity;
import com.huassit.imenu.android.util.ImageUtils;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.util.TimeUtil;
import com.huassit.imenu.android.view.NavigationView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发布动态界面
 * 
 * @author shang_guan
 */
public class AddDynamicActivity extends BaseActivity implements OnClickListener {

	/**
	 * 上传图片成功标石
	 */
	private static final int UPLOAD_IMAGE_SUCCESS = 1000;
	/**
	 * 上传图片失败标石
	 */
	private static final int UPLOAD_IMAGE_FALSE = 1001;
	/**
	 * 发送动态成功标石
	 */
	private static final int EDIT_SUCCESS = 2000;
	/**
	 * 发送动态失败标石
	 */
	private static final int EDIT_FALSE = 2001;
	private NavigationView mNavigationView;
	/**
	 * 用户头像
	 */
	private ImageView user_img;
	/**
	 * 用户昵称
	 */
	private TextView user_name;
	/**
	 * 时间显示
	 */
	private TextView time;
	/**
	 * 动态标题输入
	 */
	private EditText dynamic_content;
	/**
	 * 保存动态
	 */
	private TextView save_dynamic;
	/**
	 * 发布动态
	 */
	private TextView add_dynamic;
	/**
	 * 显示菜品图片layout
	 */
	private LinearLayout layout_dynamic_img;
	/**
	 * 商家名称
	 */
	private TextView store_name;
	/**
	 * 就餐人数
	 */
	private TextView people;
	/**
	 * 商户地址
	 */
	private TextView store_address;
	private ImageLoader imageLoader;
	DisplayImageOptions options;
	/**
	 * 动态ID
	 */
	private String dynamic_id;
	/**
	 * 动态实体类
	 */
	private Dynamic dynamic_info;
	/**
	 * 用户信息
	 */
	private Member view_member;
	/**
	 * 菜品信息
	 */
	private ArrayList<MemberMenu> menu_list;
	/**
	 * 商户信息
	 */
	private Store store_info;
	/***/
	private ImageView iv1;
	/***/
	private ImageView iv2;
	/***/
	private ImageView iv3;
	/**
	 * 成员变量图片和菜品IDmap
	 */
	private Map<String, String> dynamicImageMap;
	/**
	 * 图片宽度,根据屏幕宽度计算
	 */
	private int width;
	List<ImageView> mImageViewsList = new ArrayList<ImageView>();
	private List<Bitmap> bitmapList = new ArrayList<Bitmap>();
	/**
	 * 本地数据
	 */
	private MyApplication application;
	private Bitmap bitmap;
	private Map<String, String> thumbnailMap;
	/** 动态类型:保存动态，2-保存/1-保存并发布/0-删除 */
	private String dynamic_type;
	/** 显示图片上传记录 */
	private TextView show_upload_img;
	/** 上传进度 */
	private int nowCount = 0;
	/** map中图片路径：1-网络图片路径，0-SDCard路径 */
	private int mapImgType;

	@Override
	public int getContentView() {
		return R.layout.add_dynamic;
	}

	@Override
	public void initView() {
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		int screenWidth = metric.widthPixels; // 屏幕宽度（像素）
		width = screenWidth / 3;
		mNavigationView = (NavigationView) findViewById(R.id.navigationView);
		mNavigationView.setNavigateItemText(NavigationView.LEFT_TEXT_VIEW,
				StringUtils.getString(this, R.string.dynamic_detail));
		mNavigationView.setNavigateItemBackground(
				NavigationView.SECOND_RIGHT_TEXT_VIEW, R.drawable.close);
		mNavigationView.setNavigateItemVisibility(
				NavigationView.LEFT_IMAGE_VIEW, View.GONE);
		mNavigationView.setNavigateItemOnClickListener(
				NavigationView.SECOND_RIGHT_TEXT_VIEW, new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		imageLoader = ImageLoader.getInstance();
		options = MyApplication.getDisplayImageOptions(context, 40);
		user_img = (ImageView) findViewById(R.id.user_img);
		user_name = (TextView) findViewById(R.id.user_name);
		time = (TextView) findViewById(R.id.time);
		store_name = (TextView) findViewById(R.id.store_name);
		people = (TextView) findViewById(R.id.people);
		store_address = (TextView) findViewById(R.id.store_address);
		dynamic_content = (EditText) findViewById(R.id.dynamic_content);
		layout_dynamic_img = (LinearLayout) findViewById(R.id.layout_dynamic_img);
		save_dynamic = (TextView) findViewById(R.id.save_dynamic);
		save_dynamic.setOnClickListener(this);
		add_dynamic = (TextView) findViewById(R.id.add_dynamic);
		add_dynamic.setOnClickListener(this);
		show_upload_img = (TextView) findViewById(R.id.show_upload_img);
		show_upload_img.setVisibility(View.INVISIBLE);
		dynamic_content = (EditText) findViewById(R.id.dynamic_content);
		dynamic_info = new Dynamic();

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(CameraActivity.ACTION);
		registerReceiver(takePicReceiver, intentFilter);
	}

	@Override
	public void initData() {
		if (getIntent().getExtras() != null) {
			dynamic_id = getIntent().getExtras().getString("dynamic_id");
			UploadAdapter();
		}
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			closeProgressDialog();
			switch (msg.what) {
			case SUCCESS:
				if (msg.obj != null) {
					Map<String, Object> map = (Map<String, Object>) msg.obj;
					view_member = (Member) map.get("view_member");
					dynamic_info = (Dynamic) map.get("dynamic_info");
					menu_list = (ArrayList<MemberMenu>) map.get("menu_list");
					store_info = (Store) map.get("store_info");
					showData();
					if (dynamic_info != null) {
						nowCount = 0;
						initImageView(newMap(menu_list), 1);
					} else {
						initImageView(null, 0);
					}
					// 重拍后显示图片
					if (getIntent().getSerializableExtra(CameraActivity.DATA) != null) {
						thumbnailMap = (Map<String, String>) getIntent()
								.getSerializableExtra(CameraActivity.DATA);
						layout_dynamic_img.removeAllViews();
						nowCount = 0;
						initImageView(thumbnailMap, 0);
					}
				}
				break;
			case UPLOAD_IMAGE_SUCCESS:
				if (msg.obj != null) {
					MemberMenu memberMenu = (MemberMenu) msg.obj;
					for (int i = 0; i < mImageViewsList.size(); i++) {
						String tag = (String) mImageViewsList.get(i).getTag();
						if (tag.equals(memberMenu.menu_id)) {
							imageLoader.displayImage(memberMenu.image_location
									+ memberMenu.image_name,
									mImageViewsList.get(i));
						}
					}
					show_upload_img.setVisibility(View.VISIBLE);
					show_upload_img.setText("已上传" + nowCount + "/共"
							+ dynamicImageMap.size() + "张");
				}
				break;
			case EDIT_SUCCESS:
				if (dynamic_type.equals("1")) {
					// 保存并发布
					Intent intent = new Intent(AddDynamicActivity.this,
							DynamicActivity.class);
					startActivity(intent);
					finish();
				}
				if (dynamic_type.equals("2")) {
					// 保存
					Intent intent=new Intent(AddDynamicActivity.this,DynamicActivity.class);
					setResult(1, intent);
					finish();
				}
				break;
			case UPLOAD_IMAGE_FALSE:
				if (msg.obj != null) {
					mNavigationView.showErrorMessage(msg.obj.toString());
				}
				break;
			case EDIT_FALSE:
				if (msg.obj != null) {
					mNavigationView.showErrorMessage(msg.obj.toString());
				}
				break;
			case FAILURE:
				if (msg.obj != null) {
					mNavigationView.showErrorMessage(msg.obj.toString());
				}
				break;
			default:
				break;
			}
		}
	};

	/***
	 * 动态加载图片控件
	 * 
	 * @param map以menuID为键的图片路径
	 * @param mapType类型
	 *            ，1-保存的动态类型，展示图片
	 */
	private void initImageView(Map<String, String> map, int mapType) {
		boolean hasBitmap = false;
		dynamicImageMap = map;
		mapImgType = mapType;
		if (menu_list != null && menu_list.size() > 0) {
			for (int i = 0; i < menu_list.size(); i++) {
				if (i % 3 == 0) {
					View v = getLayoutInflater().inflate(
							R.layout.menu_picture_item, null);
					FrameLayout fl1 = (FrameLayout) v.findViewById(R.id.fl1);
					FrameLayout fl2 = (FrameLayout) v.findViewById(R.id.fl2);
					FrameLayout fl3 = (FrameLayout) v.findViewById(R.id.fl3);
					iv1 = (ImageView) v.findViewById(R.id.iv1);
					iv2 = (ImageView) v.findViewById(R.id.iv2);
					iv3 = (ImageView) v.findViewById(R.id.iv3);
					LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
							width, width);
					fl1.setLayoutParams(layoutParams);
					fl2.setLayoutParams(layoutParams);
					fl3.setLayoutParams(layoutParams);
					TextView tv1 = (TextView) v.findViewById(R.id.tv1);
					TextView tv2 = (TextView) v.findViewById(R.id.tv2);
					TextView tv3 = (TextView) v.findViewById(R.id.tv3);
					LinearLayout layout_menu1 = (LinearLayout) v
							.findViewById(R.id.layout_menu1);
					LinearLayout layout_menu2 = (LinearLayout) v
							.findViewById(R.id.layout_menu2);
					LinearLayout layout_menu3 = (LinearLayout) v
							.findViewById(R.id.layout_menu3);
					layout_menu1.setVisibility(View.GONE);
					layout_menu2.setVisibility(View.GONE);
					layout_menu3.setVisibility(View.GONE);
					MemberMenu menu1 = menu_list.get(i);
					String filePath = null;
					if (map != null && map.size() > 0) {
						if (map.get(menu1.menu_id) != null
								&& !map.get(menu1.menu_id).equals("")) {
							filePath = map.get(menu1.menu_id);
							if (mapType == 1 || filePath.equals("1")) {
								hasBitmap = true;
								if (filePath.equals("1")) {
									imageLoader.displayImage(
											menu1.image_location
													+ menu1.image_name, iv1);
								} else {
									imageLoader.displayImage(filePath, iv1);
								}
							} else {
								bitmap = ImageUtils.getScaleBitmap(
										map.get(menu1.menu_id), width, width);
								iv1.setImageBitmap(bitmap);
								hasBitmap = true;
								UploadAdapter(filePath, menu1.menu_id);
							}
						} else {
							hasBitmap = false;
							iv1.setBackgroundColor(Color
									.parseColor("#20fdfdfd"));
							iv1.setImageResource(R.drawable.defult_add_dynamic);
						}
					} else {
						hasBitmap = false;
						iv1.setBackgroundColor(Color.parseColor("#20fdfdfd"));
						iv1.setImageResource(R.drawable.defult_add_dynamic);
					}
					iv1.setTag(menu1.menu_id);
					mImageViewsList.add(iv1);
					tv1.setText(menu1.menu_name);
					tv1.setBackgroundColor(Color.parseColor("#20f4f4f4"));
					fl1.setBackgroundColor(Color.parseColor("#20fdfdfd"));
					fl1.setOnClickListener(setListener(menu1, hasBitmap,
							filePath));
					if (i + 1 < menu_list.size()) {
						MemberMenu menu2 = menu_list.get(i + 1);
						if (map != null && map.size() > 0) {
							if (map.get(menu2.menu_id) != null
									&& !map.get(menu2.menu_id).equals("")) {
								filePath = map.get(menu2.menu_id);
								if (mapType == 1 || filePath.equals("1")) {
									hasBitmap = true;
									if (filePath.equals("1")) {
										imageLoader
												.displayImage(
														menu2.image_location
																+ menu2.image_name,
														iv2);
									} else {
										imageLoader.displayImage(filePath, iv2);
									}
								} else {
									bitmap = ImageUtils.getScaleBitmap(
											map.get(menu2.menu_id), width,
											width);
									iv2.setImageBitmap(bitmap);
									hasBitmap = true;
									UploadAdapter(filePath, menu2.menu_id);
								}
							} else {
								hasBitmap = false;
								iv2.setBackgroundColor(Color
										.parseColor("#20fdfdfd"));
								iv2.setImageResource(R.drawable.defult_add_dynamic);
							}
						} else {
							hasBitmap = false;
							iv2.setBackgroundColor(Color
									.parseColor("#20fdfdfd"));
							iv2.setImageResource(R.drawable.defult_add_dynamic);
						}
						fl2.setOnClickListener(setListener(menu2, hasBitmap,
								filePath));
						fl2.setVisibility(View.VISIBLE);
						tv2.setText(menu2.menu_name);
						tv2.setBackgroundColor(Color.parseColor("#20f4f4f4"));
						iv2.setTag(menu2.menu_id);
						mImageViewsList.add(iv2);
					}
					if (i + 2 < menu_list.size()) {
						MemberMenu menu3 = menu_list.get(i + 2);
						if (map != null && map.size() > 0) {
							if (map.get(menu3.menu_id) != null
									&& !map.get(menu3.menu_id).equals("")) {
								filePath = map.get(menu3.menu_id);
								if (mapType == 1 || filePath.equals("1")) {
									hasBitmap = true;
									if (filePath.equals("1")) {
										imageLoader
												.displayImage(
														menu3.image_location
																+ menu3.image_name,
														iv3);
									} else {
										imageLoader.displayImage(filePath, iv3);
									}
								} else {
									bitmap = ImageUtils.getScaleBitmap(
											map.get(menu3.menu_id), width,
											width);
									iv3.setImageBitmap(bitmap);
									hasBitmap = true;
									UploadAdapter(filePath, menu3.menu_id);
								}
							} else {
								hasBitmap = false;
								iv3.setBackgroundColor(Color
										.parseColor("#20fdfdfd"));
								iv3.setImageResource(R.drawable.defult_add_dynamic);
							}
						} else {
							hasBitmap = false;
							iv3.setBackgroundColor(Color
									.parseColor("#20fdfdfd"));
							iv3.setImageResource(R.drawable.defult_add_dynamic);
						}
						fl3.setOnClickListener(setListener(menu3, hasBitmap,
								filePath));
						fl3.setVisibility(View.VISIBLE);
						tv3.setText(menu3.menu_name);
						tv3.setBackgroundColor(Color.parseColor("#20f4f4f4"));
						fl3.setBackgroundColor(Color.parseColor("#20fdfdfd"));
						iv3.setTag(menu3.menu_id);
						mImageViewsList.add(iv3);
					}
					layout_dynamic_img.addView(v);
				}
			}
		}
	}

	/**
	 * 请求动态详情接口
	 */
	private void UploadAdapter() {
		dynamic_info = new Dynamic();
		Member member = new Member();
		member.token = token;
		member.memberId = PreferencesUtils.getString(this, "member_id");
		dynamic_info.setDynamicId(dynamic_id);
		dynamic_info.setMemberInfo(member);
		GetDynamicDetailResp mDetailResp = new GetDynamicDetailResp(this,
				handler, dynamic_info, token, member.memberId);
		mDetailResp.asyncInvoke(SUCCESS, FAILURE);
	}

	/**
	 * 上传菜品图片请求
	 */
	private void UploadAdapter(String filePath, String menu_id) {
		nowCount++;
		showProgressDialog(getDialogMessage(nowCount, dynamicImageMap.size()));
		EditMenuImageResp menuImageResp = new EditMenuImageResp(this, handler,
				PreferencesUtils.getString(this, "member_id"), dynamic_id,
				menu_id, filePath, token);
		menuImageResp.asyncInvoke(UPLOAD_IMAGE_SUCCESS, UPLOAD_IMAGE_FALSE);
	}

	/**
	 * 保存或发送动态
	 */
	private void UploadAdapterAdd(String title, String dynamic_type) {
		EditDynamicResp mDynamicResp = new EditDynamicResp(this, handler,
				PreferencesUtils.getString(this, "member_id"), dynamic_id,
				title, dynamic_type, token);
		mDynamicResp.asyncInvoke(EDIT_SUCCESS, EDIT_FALSE);
	}

	/**
	 * 图片监听
	 */
	private OnClickListener setListener(final MemberMenu menu,
			final boolean hasBitmap, final String filePath) {
		return new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (hasBitmap) {
					// 有照片,显示大图
					Intent intent = new Intent(AddDynamicActivity.this,
							PictureDetailsActivity.class);
					intent.putExtra("PICTURE", filePath);
					intent.putExtra("filePath_type", mapImgType);
					intent.putExtra("MENU_LIST", menu_list);
					intent.putExtra("ThumbnailMap", (Serializable) thumbnailMap);
					startActivity(intent);
				} else {
					// 无照片,拍照
					Intent intent = new Intent(AddDynamicActivity.this,
							CameraActivity.class);
					intent.putExtra(CameraActivity.NEED_TAKE_PIC_COUNT,
							getList(menu_list));

					startActivity(intent);
				}
			}
		};
	}

	/**
	 * 显示基本信息
	 */
	private void showData() {
		imageLoader.displayImage(view_member.iconLocation
				+ view_member.iconName, user_img, options);
		user_name.setText(view_member.memberName);
		time.setText(TimeUtil.getCurrentTime());
		store_name.setText(store_info.name);
		people.setText(dynamic_info.getPeople() + "人");
		store_address.setText(store_info.address);
		dynamic_content.setText(dynamic_info.getTitle());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.save_dynamic:
			// 保存动态，2-保存/1-保存并发布/0-删除
			dynamic_type = "2";
			if ("".equals(dynamic_content.getText().toString())) {
				mNavigationView.showErrorMessage(StringUtils.getString(this,
						R.string.title_cannot_be_null));
			} else {
				UploadAdapterAdd(dynamic_content.getText().toString(),
						dynamic_type);
			}
			break;
		case R.id.add_dynamic:
			// 发布动态，2-保存/1-保存并发布/0-删除
			dynamic_type = "1";
			if ("".equals(dynamic_content.getText().toString())) {
				mNavigationView.showErrorMessage(StringUtils.getString(this,
						R.string.title_cannot_be_null));
			} else {
				UploadAdapterAdd(dynamic_content.getText().toString(),
						dynamic_type);
			}
			break;
		}
	}

	/**
	 * 封装图片map
	 */
	private Map<String, String> newMap(ArrayList<MemberMenu> menu_list) {
		Map<String, String> map = new HashMap<String, String>();
		// ArrayList<MemberMenu> menus = dynamic.getMenuList();
		if (menu_list != null && menu_list.size() > 0) {
			for (int i = 0; i < menu_list.size(); i++) {
				map.put(menu_list.get(i).menu_id,
						menu_list.get(i).image_location
								+ menu_list.get(i).image_name);
			}
		}
		return map;
	}

	/**
	 * 封装菜单List
	 */
	public ArrayList<Menu> getList(ArrayList<MemberMenu> memberMenu) {
		ArrayList<Menu> menusList = new ArrayList<Menu>();
		for (int i = 0; i < memberMenu.size(); i++) {
			Menu menu = new Menu();
			menu.menu_id = memberMenu.get(i).menu_id;
			menu.menu_name = memberMenu.get(i).menu_name;
			menu.menu_image_name = memberMenu.get(i).image_name;
			menu.menu_image_location = memberMenu.get(i).image_location;
			menusList.add(menu);
		}
		return menusList;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(takePicReceiver);
		if (bitmapList.size() > 0) {
			for (Bitmap bitmap : bitmapList) {
				if (bitmap != null && !bitmap.isRecycled()) {
					bitmap.recycle();
				}
			}
		}
		bitmapList.clear();
		bitmapList = null;
	}

	private BroadcastReceiver takePicReceiver = new BroadcastReceiver() {
		@SuppressWarnings("unchecked")
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			thumbnailMap = (Map<String, String>) bundle
					.getSerializable(CameraActivity.DATA);
			if (thumbnailMap != null) {
				layout_dynamic_img.removeAllViews();
				nowCount = 0;
				initImageView(thumbnailMap, 0);
			}
		}
	};

	/** 进度条显示内容 */
	private String getDialogMessage(int now, int count) {
		return "正在上传图片（+" + now + "/" + count + "）";
	}
}
