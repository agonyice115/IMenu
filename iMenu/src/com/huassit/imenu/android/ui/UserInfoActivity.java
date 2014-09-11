package com.huassit.imenu.android.ui;

import java.io.File;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.MyApplication;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.biz.EditMemberDynamicImageResp;
import com.huassit.imenu.android.biz.EditMemberIconResp;
import com.huassit.imenu.android.biz.EditMemberInfoResp;
import com.huassit.imenu.android.biz.LoginResp;
import com.huassit.imenu.android.biz.RegistMemberResp;
import com.huassit.imenu.android.model.DynamicImage;
import com.huassit.imenu.android.model.Member;
import com.huassit.imenu.android.model.MemberIcon;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.util.TimeUtil;
import com.huassit.imenu.android.view.NavigationView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 填写个人信息页面
 * 
 * @author think
 */
public class UserInfoActivity extends BaseActivity {

	private static final int EDIT_MEMBER_INFO_SUCCESS = 400;
	private static final int EDIT_MEMBER_INFO_FAILED = 401;
	private static final int EDIT_ICON_SUCCESS = 200;
	private static final int EDIT_ICON_FAILED = 201;
	/** 上传头像成功 */
	private static final int ICON_SUCCESS = 1000;
	/** 上传头像失败 */
	private static final int ICON_FAILED = 1001;
	/** 修改个人信息成功 */
	private static final int MEMBER_INFO_SUCCESS = 2000;
	/** 修改个人信息失败 */
	private static final int MEMBER_INFO_FAILED = 2001;
	/** 上传动态封面成功 */
	private static final int DYNAMIC_SUCCESS = 3000;
	/** 上传动态封面失败 */
	private static final int DYNAMIC_FAILED = 3001;
	private static final int REGISTER_SUCCESS = 300;
	private static final int REGISTER_FAILED = 301;
	private ImageView dynamic;
	private ImageView icon;
	private ImageView takePhoto;
	private RelativeLayout rl_memberName;
	private RelativeLayout rl_signature;
	private RelativeLayout rl_realName;
	private RelativeLayout rl_birthday;
	private RelativeLayout rl_sex;
	private RelativeLayout rl_region;
	private RelativeLayout rl_relativeAccounts;
	private TextView tv_memberName;
	private TextView tv_signature;
	private TextView tv_realName;
	private TextView tv_birthday;
	private TextView tv_sex;
	private TextView tv_region;
	private Button btn_finish;
	private static final int CAMERA = 1;
	private static final int CHOOSE_REGION = 2;
	private static final int CAMERA_FOR_DYNAMIC = 3;
	private DatePickerDialog.OnDateSetListener dateListener;
	private Member member;
	private String filePath;
	private String menuCode;
	private Member memberFromServer;
	private NavigationView mNavigationView;
	/** 个人设置标石 */
	private boolean isMember = false;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private MyApplication application;

	@Override
	public int getContentView() {
		return R.layout.fill_user_info;
	}

	@Override
	public void initView() {
		application = (MyApplication) getApplicationContext();
		imageLoader = ImageLoader.getInstance();
		options = MyApplication.getDisplayImageOptions(context, 50);
		mNavigationView = (NavigationView) findViewById(R.id.navigationView);
		mNavigationView.setNavigateItemVisibility(
				NavigationView.LEFT_IMAGE_VIEW, View.GONE);
		mNavigationView.setNavigateItemBackground(
				NavigationView.LEFT_TEXT_VIEW, R.drawable.back);
		mNavigationView.setNavigateItemText(NavigationView.TITLE, StringUtils.getString(context, R.string.personal_info));
		mNavigationView.setNavigateItemOnClickListener(
				NavigationView.LEFT_TEXT_VIEW, new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		imageLoader = ImageLoader.getInstance();
		dynamic = (ImageView) findViewById(R.id.iv_dynamic);
		icon = (ImageView) findViewById(R.id.icon);
		takePhoto = (ImageView) findViewById(R.id.take_photo);
		rl_memberName = (RelativeLayout) findViewById(R.id.rl_membername);
		rl_signature = (RelativeLayout) findViewById(R.id.rl_signature);
		rl_realName = (RelativeLayout) findViewById(R.id.rl_realname);
		rl_birthday = (RelativeLayout) findViewById(R.id.rl_birthday);
		rl_sex = (RelativeLayout) findViewById(R.id.rl_sex);
		rl_region = (RelativeLayout) findViewById(R.id.rl_region);
		rl_relativeAccounts = (RelativeLayout) findViewById(R.id.rl_relative_accounts);
		tv_memberName = (TextView) findViewById(R.id.tv_membername);
		tv_realName = (TextView) findViewById(R.id.tv_realname);
		tv_signature = (TextView) findViewById(R.id.tv_signature);
		tv_birthday = (TextView) findViewById(R.id.tv_birthday);
		tv_sex = (TextView) findViewById(R.id.tv_sex);
		tv_region = (TextView) findViewById(R.id.tv_region);
		btn_finish = (Button) findViewById(R.id.finish);
	}

	@Override
	public void initData() {
		if ("Settings".equals(getIntent().getStringExtra("sourceActivity"))) {
			// 登陆后进入个人设置
			isMember = true;
			member = new Member();
			member.token = token;
			member.memberId = PreferencesUtils.getString(context, "member_id");
			member.memberName = PreferencesUtils.getString(context,
					"member_name");
			member.iconLocation = PreferencesUtils.getString(context,
					"iconLocation");
			member.iconName = PreferencesUtils.getString(context, "iconName");
			member.dynamicLocation = PreferencesUtils.getString(context,
					"dynamicLocation");
			member.dynamicName = PreferencesUtils.getString(context,
					"dynamicName");
			imageLoader.displayImage(member.iconLocation + member.iconName,
					icon, options);
			imageLoader.displayImage(member.dynamicLocation
					+ member.dynamicName, dynamic);
			btn_finish.setVisibility(View.GONE);
			takePhoto.setVisibility(View.GONE);
			mNavigationView.setNavigateItemVisibility(
					NavigationView.SECOND_RIGHT_TEXT_VIEW, View.GONE);
			mNavigationView.setNavigateItemOnClickListener(
					NavigationView.LEFT_TEXT_VIEW, new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (!StringUtils.isBlank(tv_memberName.getText()
									.toString())) {
								if (tv_memberName.getText().toString()
										.equals(member.memberName)) {
									member.memberName = "";
								} else {
									member.memberName = tv_memberName.getText()
											.toString();
								}
							}
							if (!StringUtils.isBlank(tv_signature.getText()
									.toString())) {
								member.signature = tv_signature.getText()
										.toString();
							}
							if (!StringUtils.isBlank(tv_realName.getText()
									.toString())) {
								member.realname = tv_realName.getText()
										.toString();
							}
							if (!StringUtils.isBlank(tv_birthday.getText()
									.toString())) {
								member.birthday = tv_birthday.getText()
										.toString();
							}
							if (!StringUtils.isBlank(tv_sex.getText()
									.toString())) {
								member.sex = setSex(tv_sex.getText().toString());
							}
							if (!StringUtils.isBlank(tv_region.getText()
									.toString())) {
								member.regionId = tv_region.getText()
										.toString();
							}
							UploadAdapter();
						}
					});
		} else {
			member = (Member) getIntent().getSerializableExtra("user");
			menuCode = getIntent().getStringExtra("menuCode");
		}
		member.signature = PreferencesUtils.getString(context, "signature");
		member.realname = PreferencesUtils.getString(context, "realname");
		member.birthday = PreferencesUtils.getString(context, "birthday");
		member.regionId = PreferencesUtils.getString(context, "region");
		member.sex = PreferencesUtils.getString(context, "sex");
		tv_memberName.setText(member.memberName);
		if (!StringUtils.isBlank(member.signature)) {
			tv_signature.setText(member.signature);
		}
		if (!StringUtils.isBlank(member.realname)) {
			tv_realName.setText(member.realname);
		}
		if (!StringUtils.isBlank(member.birthday)) {
			tv_birthday.setText(TimeUtil.FormatTime(member.birthday,
					"yyyy-MM-dd"));
		}
		tv_sex.setText(getSex(member.sex));
		if (!StringUtils.isBlank(member.regionId)) {
			tv_region.setText(member.regionId);
		}
		icon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 调用系统相机
				startCamera();
			}
		});
		dynamic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 调用系统相机
				startCameraForDynamic();

			}
		});
		rl_memberName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final EditText et = new EditText(context);
				et.setText(tv_memberName.getText().toString());
				new AlertDialog.Builder(context)
						.setTitle(StringUtils.getString(context, R.string.nickname))
						.setView(et)
						.setPositiveButton(StringUtils.getString(context, R.string.confirm),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										tv_memberName.setText(et.getText()
												.toString());
										dialog.dismiss();
									}
								}).setNegativeButton(StringUtils.getString(context, R.string.cancel), null).show();
			}
		});
		rl_signature.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final EditText et = new EditText(context);
				et.setText(tv_signature.getText().toString());
				new AlertDialog.Builder(context)
						.setTitle(StringUtils.getString(context, R.string.signature))
						.setView(et)
						.setPositiveButton(StringUtils.getString(context, R.string.confirm),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										tv_signature.setText(et.getText()
												.toString());
										dialog.dismiss();
									}
								}).setNegativeButton(StringUtils.getString(context, R.string.cancel), null).show();

			}
		});
		rl_realName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final EditText et = new EditText(context);
				et.setText(tv_realName.getText().toString());
				new AlertDialog.Builder(context)
						.setTitle(StringUtils.getString(context, R.string.realname))
						.setView(et)
						.setPositiveButton(StringUtils.getString(context, R.string.confirm),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										tv_realName.setText(et.getText()
												.toString());
										dialog.dismiss();
									}
								}).setNegativeButton(StringUtils.getString(context, R.string.cancel), null).show();
			}
		});
		rl_birthday.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Calendar calendar = Calendar.getInstance();
				new DatePickerDialog(context, dateListener, calendar
						.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
						calendar.get(Calendar.DAY_OF_MONTH)).show();

			}
		});

		dateListener = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker datePicker, int year, int month,
					int dayOfMonth) {
				// Calendar月份是从0开始,所以month要加1
				tv_birthday
						.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
			}
		};
		rl_sex.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(context)
						.setTitle(StringUtils.getString(context, R.string.gender))
						.setSingleChoiceItems(new String[] { StringUtils.getString(context, R.string.male),StringUtils.getString(context, R.string.female) },
								getSexIndex((tv_sex.getText().toString())),
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										if (which == 0) {
											member.setSex("1");
											tv_sex.setText(StringUtils.getString(context, R.string.male));
										} else {
											member.setSex("2");
											tv_sex.setText(StringUtils.getString(context, R.string.female
													));
										}
										dialog.dismiss();
									}
								}).setNegativeButton(StringUtils.getString(context, R.string.cancel), null).show();

			}
		});
		// 地区选择
		rl_region.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UserInfoActivity.this,
						ChooseRegionActivity.class);
				startActivityForResult(intent, CHOOSE_REGION);

			}
		});
		// 相关账户
		rl_relativeAccounts.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UserInfoActivity.this,
						RelativeAccountActivity.class);
				startActivity(intent);
			}
		});
		// 点击完成
		// 提交编辑信息
		btn_finish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				member.memberName = tv_memberName.getText().toString();
				member.realname = tv_realName.getText().toString();
				member.birthday = tv_birthday.getText().toString();
				member.sex = tv_sex.getText().toString();
				member.regionId = tv_region.getText().toString();
				member.areaCode = "+86";
				// 先注册
				RegistMemberResp registResp = new RegistMemberResp(
						UserInfoActivity.this, handler, member, menuCode);
				registResp.asyncInvoke(REGISTER_SUCCESS, REGISTER_FAILED);
			}
		});
	}

	/** 返回现在性别序号 */
	private int getSexIndex(String sex) {
		if (sex.equals(StringUtils.getString(context, R.string.male))) {
			return 0;
		}
		if (sex.equals(StringUtils.getString(context, R.string.female))) {
			return 1;
		}
		return 0;
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Intent intent = new Intent(UserInfoActivity.this,
					MainActivity.class);
			switch (msg.what) {
			case DYNAMIC_SUCCESS:
				closeProgressDialog();
				prompt(StringUtils.getString(context, R.string.dynamic_image_upload_success));
				DynamicImage mImage = (DynamicImage) msg.obj;
				member.dynamicLocation = mImage.iconLocation;
				member.dynamicName = mImage.iconName;
				member.dynamicDate = mImage.iconDate;
				PreferencesUtils.putString(context, "dynamicLocation",
						member.dynamicLocation);
				PreferencesUtils.putString(context, "dynamicName",
						member.dynamicName);
				imageLoader.displayImage(member.dynamicLocation
						+ member.dynamicName, dynamic);
				break;
			case DYNAMIC_FAILED:
				closeProgressDialog();
				if (msg.obj != null) {
					mNavigationView.showErrorMessage(msg.obj.toString());
				}
				break;
			case MEMBER_INFO_SUCCESS:
				closeProgressDialog();
				saveMember();
				prompt(StringUtils.getString(context, R.string.user_info_edit_success));
				finish();
				break;
			case MEMBER_INFO_FAILED:
				closeProgressDialog();
				if (msg.obj != null) {
					mNavigationView.showErrorMessage(msg.obj.toString());
				}
				break;
			case ICON_SUCCESS:
				prompt(StringUtils.getString(context, R.string.icon_upload_success));
				MemberIcon icon1 = (MemberIcon) msg.obj;
				member.iconLocation = icon1.iconLocation;
				member.iconName = icon1.iconName;
				member.iconDate = icon1.iconDate;
				PreferencesUtils.putString(context, "iconLocation",
						member.iconLocation);
				PreferencesUtils
						.putString(context, "iconName", member.iconName);
				imageLoader.displayImage(member.iconLocation + member.iconName,
						icon, options);
				break;
			case ICON_FAILED:
				prompt(StringUtils.getString(context, R.string.icon_upload_fail));
				break;
			case EDIT_MEMBER_INFO_FAILED:
				prompt(StringUtils.getString(context, R.string.user_info_edit_fail));
				break;
			case EDIT_MEMBER_INFO_SUCCESS:
				PreferencesUtils.putString(context, "token", member.token);
				PreferencesUtils.putString(context, "member_id",
						member.memberId);
				PreferencesUtils.putString(context, "member_name",
						member.memberName);

				// 提交用户信息后 编辑用户头像
				if (filePath != null && !"".equals(filePath)) {
					EditMemberIconResp iconResp = new EditMemberIconResp(
							UserInfoActivity.this, handler, member, filePath);
					iconResp.asyncInvoke(EDIT_ICON_SUCCESS, EDIT_ICON_FAILED);
				} else {
					intent.putExtra("user", member);
					startActivity(intent);
					finish();
				}
				break;
			case EDIT_ICON_FAILED:
				prompt(StringUtils.getString(context, R.string.icon_submit_fail));
				break;
			case EDIT_ICON_SUCCESS:
				prompt(StringUtils.getString(context, R.string.icon_upload_success));
				MemberIcon icon = (MemberIcon) msg.obj;
				member.iconLocation = icon.iconLocation;
				member.iconName = icon.iconName;
				member.iconDate = icon.iconDate;
				PreferencesUtils.putString(context, "iconLocation",
						member.iconLocation);
				PreferencesUtils
						.putString(context, "iconName", member.iconName);
				intent.putExtra("user", member);
				startActivity(intent);
				finish();
				break;
			case REGISTER_SUCCESS:
				memberFromServer = (Member) msg.obj;
				// 注册成功后登录
				LoginResp loginResp = new LoginResp(UserInfoActivity.this,
						handler, memberFromServer, "1");
				loginResp.asyncInvoke(SUCCESS, FAILURE);
				break;
			case REGISTER_FAILED:
				break;
			case SUCCESS:
				Member finalMember = (Member) msg.obj;
				// 登录成功后编辑用户信息
				member.memberId = finalMember.memberId;
				member.token = finalMember.token;
				member.status = finalMember.status;
				// 编辑用户时用户名给空 避免昵称已被使用
				member.memberName = "";
				EditMemberInfoResp resp = new EditMemberInfoResp(
						UserInfoActivity.this, handler, member);
				resp.asyncInvoke(EDIT_MEMBER_INFO_SUCCESS,
						EDIT_MEMBER_INFO_FAILED);
				break;
			case FAILURE:
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 调用系统照相功能，返回所照相片到PicUtils.PIC_PATH下
	 */
	private void startCamera() {
		Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file =new File(getExternalFilesDir("/"),"avatar.jpg");
		System.out.println(file.getAbsolutePath());
		Uri uri = Uri.fromFile(file);
		camera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		startActivityForResult(camera, CAMERA);
	}

	private void startCameraForDynamic() {
		Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file =new File(getExternalFilesDir("/"),"dynamic.jpg");
		System.out.println(file.getAbsolutePath());
		Uri uri = Uri.fromFile(file);
		camera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		startActivityForResult(camera, CAMERA_FOR_DYNAMIC);
	}

	@Override
	protected void onResume() {
		setTitle(StringUtils.getString(context, R.string.personal_info));
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
//		String name = "avatar.jpg";
		if (requestCode == CAMERA && resultCode == Activity.RESULT_OK) {
			takePhoto.setVisibility(View.GONE);
			if (isMember) {
				uploadAdapter(new File(getExternalFilesDir("/"),"avatar.jpg").getAbsolutePath());
				System.out.println("filePath:" + new File(getExternalFilesDir("/"),"avatar.jpg").getAbsolutePath());
			}
		} else if (requestCode == CHOOSE_REGION && resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String city = bundle.getString("city");
			String province = bundle.getString("province");
			tv_region.setText(province + "/" + city);
		} else if (requestCode == CAMERA_FOR_DYNAMIC
				&& resultCode == Activity.RESULT_OK) {
			takePhoto.setVisibility(View.GONE);
			if (isMember) {
				uploadDynamicAdapter(new File(getExternalFilesDir("/"),"dynamic.jpg").getAbsolutePath());
				System.out.println("filePath:" + new File(getExternalFilesDir("/"),"dynamic.jpg").getAbsolutePath());
			}
		}
	}

	/** 上传头像请求 */
	private void uploadAdapter(String filePath) {
		if (filePath != null && !"".equals(filePath)) {
			EditMemberIconResp memberIconResp = new EditMemberIconResp(this,
					handler, member, filePath);
			memberIconResp.asyncInvoke(ICON_SUCCESS, ICON_FAILED);
		}
	}

	/** 上传动态封面请求 */
	private void uploadDynamicAdapter(String filePath) {
		if (filePath != null && !"".equals(filePath)) {
			EditMemberDynamicImageResp memberDynamicImageResp = new EditMemberDynamicImageResp(
					this, handler, member, filePath);
			memberDynamicImageResp.asyncInvoke(DYNAMIC_SUCCESS, DYNAMIC_FAILED);
		}
	}

	/** 提交用户信息请求 */
	private void UploadAdapter() {
		showProgressDialog(StringUtils.getString(context, R.string.upload_info_wait));
		EditMemberInfoResp resp = new EditMemberInfoResp(this, handler, member);
		resp.asyncInvoke(MEMBER_INFO_SUCCESS, MEMBER_INFO_FAILED);
	}

	/** 修改个人信息成功后存储信息 */
	private void saveMember() {
		PreferencesUtils.putString(context, "member_name", tv_memberName
				.getText().toString());
		PreferencesUtils.putString(context, "signature", member.signature);
		PreferencesUtils.putString(context, "realName", member.realname);
		PreferencesUtils.putString(context, "birthday", member.birthday);
		PreferencesUtils.putString(context, "sex", member.sex);
		PreferencesUtils.putString(context, "region", member.regionId);
	}

	/** 显示性别 */
	private String getSex(String sex) {
		if (sex.equals("1")) {
			return StringUtils.getString(context, R.string.male);
		}
		if (sex.equals("2")) {
			return StringUtils.getString(context, R.string.female);
		}
		return StringUtils.getString(context, R.string.invalid);
	}

	/** 显示性别 */
	private String setSex(String sex) {
		if (sex.equals(StringUtils.getString(context, R.string.male))) {
			return "1";
		}
		if (sex.equals(StringUtils.getString(context, R.string.female))) {
			return "2";
		}
		return StringUtils.getString(context, R.string.invalid);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
}
