package com.huassit.imenu.android.ui;

import java.text.MessageFormat;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.MyApplication;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.adapter.FansListAdapter;
import com.huassit.imenu.android.biz.EditFollowStatusResp;
import com.huassit.imenu.android.biz.GetMemberDetailResp;
import com.huassit.imenu.android.db.dao.ShareMemberDao;
import com.huassit.imenu.android.model.API;
import com.huassit.imenu.android.model.MemberDetail;
import com.huassit.imenu.android.model.Share;
import com.huassit.imenu.android.ui.cart.CartActivity;
import com.huassit.imenu.android.ui.settings.SettingsActivity;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.util.TimeUtil;
import com.huassit.imenu.android.view.NavigationView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Sylar on 14-7-8.
 */
public class MineActivity extends BaseActivity implements View.OnClickListener {
	private static final int HAS_CART = 1;
	private static final int HAS_NEW_DYNAMIC = 1;
	private NavigationView navigationView;
	// 用户背景
	private ImageView mUserBackground;
	// 头像
	private ImageView mUserAvatar;
	// 名字
	private TextView userNameTextView;
	// 个性签名
	private TextView userSignature;
	// 粉丝
	private TextView followedCountTextView;
	// 关注
	private TextView followingCountTextView;
	// 动态
	private TextView activitiesCountTextView;
	// 购物车商家名称
	private TextView cartStoreNameTextView;
	// 消息数
	private TextView messageCountTextView;
	// 积分
	private TextView scoreCountTextView;
	// 未付款订单数
	private TextView pendingOrderTextView;
	// 到店支付订单数
	private TextView consumeOrderTextView;
	// 已支付订单数
	private TextView paymentOrderTextView;
	private ImageView dynamicNewIcon;
	/** 购物车布局 */
	private RelativeLayout cartLayout;
	/** 待付款订单布局 */
	private RelativeLayout pendingOrderLayout;
	/** 已付款订单布局 */
	private RelativeLayout paymentOrderLayout;
	/** 到点支付订单布局 */
	private RelativeLayout consumeOrderLayout;
	/** 设置布局 */
	private RelativeLayout settingsLayout;
	/** 判断是我的主页还是别人的主页 */
	private int meType;
	/** 我的主页标石 */
	private final int ME = 100;
	/** 别人的主页标石 */
	private final int OTHER = 200;
	/** 储存粉丝ID */
	private String view_member_id;
	/** 储存粉丝名称 */
	private String view_member_name;
	/** 关注按钮 */
	private TextView userFolowTextView;
	/** 储存用户的关注状态 */
	private String follow_status;
	/** 改变关注状态成功标石 */
	private final int CHANGEFOLLOWSUCCESS = 1000;
	/** 数据源 */
	private MemberDetail memberDetail;
	/** 消息layout */
	private FrameLayout userMessageLayout;

	private ShareMemberDao shareMemberDao;
	private PullToRefreshScrollView mPullRefreshScrollView;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private String memberId;
	private MyApplication application;
	private String member_name;

	@Override
	public int getContentView() {
		return R.layout.mine_activity;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initView() {
		member_name = PreferencesUtils.getString(context, "member_name");
		application = (MyApplication) getApplicationContext();
		imageLoader = ImageLoader.getInstance();
		options = MyApplication.getDisplayImageOptions(context, 50);
		shareMemberDao = new ShareMemberDao(this);
		navigationView = (NavigationView) findViewById(R.id.navigationView);

		navigationView.setNavigateItemOnClickListener(
				NavigationView.FIRST_RIGHT_TEXT_VIEW,
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (!StringUtils.isBlank(token)) {
							Share share = shareMemberDao.getShareMember();
							share.content = MessageFormat.format(share.content,
									memberDetail.viewMember.memberName);
							navigationView.showShareView(share);
						} else {
							// 登录
							Intent intent = new Intent(MineActivity.this,
									LoginActivity.class);
							startActivity(intent);
							overridePendingTransition(R.anim.translate_in,
									R.anim.translate_out);
						}

					}
				});

		navigationView.setNavigateItemOnClickListener(
				NavigationView.SECOND_RIGHT_TEXT_VIEW,
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (!StringUtils.isBlank(token)) {
							navigationView.showCategoryView();
						} else {
							// 注册
							Intent intent = new Intent(MineActivity.this,
									RegisterActivity.class);
							startActivity(intent);
							overridePendingTransition(R.anim.translate_in,
									R.anim.translate_out);
						}
					}
				});

		mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.refreshScrollView);
		mPullRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(
				"上次刷新时间：" + TimeUtil.getCurrentTime());
		mPullRefreshScrollView
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {

					@Override
					public void onRefresh(PullToRefreshBase refreshView) {
						reloadUserData();
						mPullRefreshScrollView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(
										StringUtils.getString(
												MineActivity.this,
												R.string.last_refresh_time)
												+ TimeUtil.getCurrentTime());
					}
				});
		mUserBackground = (ImageView) findViewById(R.id.userBackground);
		mUserAvatar = (ImageView) findViewById(R.id.userAvatar);
		userNameTextView = (TextView) findViewById(R.id.userNameTextView);
		userSignature = (TextView) findViewById(R.id.userSignature);
		followedCountTextView = (TextView) findViewById(R.id.fansCountTextView);
		followingCountTextView = (TextView) findViewById(R.id.followedCountTextView);
		pendingOrderTextView = (TextView) findViewById(R.id.pendingOrderTextView);
		paymentOrderTextView = (TextView) findViewById(R.id.paymentOrderTextView);
		consumeOrderTextView = (TextView) findViewById(R.id.consumeOrderTextView);
		activitiesCountTextView = (TextView) findViewById(R.id.activitiesCountTextView);
		cartStoreNameTextView = (TextView) findViewById(R.id.cartStoreNameTextView);
		scoreCountTextView = (TextView) findViewById(R.id.userScoreTextView);
		dynamicNewIcon = (ImageView) findViewById(R.id.dynamic_new_icon);
		userFolowTextView = (TextView) findViewById(R.id.userFolowTextView);
		userFolowTextView.setOnClickListener(this);
		cartLayout = (RelativeLayout) findViewById(R.id.cartLayout);
		userMessageLayout = (FrameLayout) findViewById(R.id.userMessageLayout);
		userMessageLayout.setOnClickListener(this);
		pendingOrderLayout = (RelativeLayout) findViewById(R.id.pendingOrderLayout);
		paymentOrderLayout = (RelativeLayout) findViewById(R.id.paymentOrderLayout);
		consumeOrderLayout = (RelativeLayout) findViewById(R.id.consumeOrderLayout);
		settingsLayout = (RelativeLayout) findViewById(R.id.settingsLayout);
		messageCountTextView = (TextView) findViewById(R.id.messageCount);

		scoreCountTextView.setOnClickListener(this);
		cartLayout.setOnClickListener(this);
		findViewById(R.id.pendingOrderLayout).setOnClickListener(this);
		findViewById(R.id.paymentOrderLayout).setOnClickListener(this);
		findViewById(R.id.consumeOrderLayout).setOnClickListener(this);
		findViewById(R.id.activitiesLayout).setOnClickListener(this);
		findViewById(R.id.settingsLayout).setOnClickListener(this);
		findViewById(R.id.fansLayout).setOnClickListener(this);
		findViewById(R.id.followedLayout).setOnClickListener(this);
	}

	private void reloadUserData() {
		meType = ME;
		memberId = PreferencesUtils.getString(this, "member_id");
		view_member_id = memberId;
		if (getIntent().getExtras() != null) {
			meType = OTHER;
			view_member_id = getIntent().getExtras()
					.getString("view_member_id");
			view_member_name = getIntent().getExtras().getString(
					"view_member_name");
			if (!view_member_id.equals(memberId)) {
				cartLayout.setVisibility(View.GONE);
				pendingOrderLayout.setVisibility(View.GONE);
				paymentOrderLayout.setVisibility(View.GONE);
				consumeOrderLayout.setVisibility(View.GONE);
				settingsLayout.setVisibility(View.GONE);
				userMessageLayout.setVisibility(View.GONE);
			}
		}
		GetMemberDetailResp resp = new GetMemberDetailResp(this, handler,
				memberId, view_member_id, TimeUtil.getTime(
						TimeUtil.getCurrentTime(), "yyyy-MM-dd HH:mm") / 1000,
				token);
		resp.asyncInvoke(SUCCESS, FAILURE);
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SUCCESS:
				memberDetail = (MemberDetail) msg.obj;
				showHead();
				navigationView.setNavigateItemOnClickListener(
						NavigationView.LEFT_TEXT_VIEW, new OnClickListener() {

							@Override
							public void onClick(View v) {
								System.out.println("目前浏览的"
										+ memberDetail.viewMember.memberName);
								System.out.println("当前登录用户" + member_name);
								if (!memberDetail.viewMember.memberName
										.equals(member_name)) {
									finish();
								}

							}
						});
				imageLoader.displayImage(memberDetail.viewMember.iconLocation
						+ memberDetail.viewMember.iconName, mUserAvatar,
						options);
				if (memberDetail.viewMember.dynamicLocation != null
						&& !memberDetail.viewMember.dynamicLocation.equals("")) {
					imageLoader.displayImage(
							memberDetail.viewMember.dynamicLocation
									+ memberDetail.viewMember.dynamicName,
							mUserBackground);
				} else {
					mUserBackground
							.setBackgroundResource(R.drawable.dynamic_member_top_bg);
				}
				userNameTextView.setText(memberDetail.viewMember.memberName);
				userSignature.setText(memberDetail.viewMember.signature);
				followedCountTextView.setText(getString(
						R.string.followed_count,
						memberDetail.viewMember.followedCount));
				followingCountTextView.setText(getString(
						R.string.following_count,
						memberDetail.viewMember.followingCount));
				activitiesCountTextView.setText(memberDetail.dynamicCount + "");
				follow_status = changeStatues(memberDetail.viewMember.followStatus);
				userFolowTextView
						.setText(changeStatues(memberDetail.viewMember.followStatus));
				scoreCountTextView.setText(getString(R.string.user_score,
						memberDetail.score));
				if (memberDetail.cartInfo != null
						&& memberDetail.cartInfo.hasCart == HAS_CART) {
					cartLayout.setVisibility(View.VISIBLE);
					cartStoreNameTextView
							.setText(memberDetail.cartInfo.storeInfo.name);
				} else {
					cartLayout.setVisibility(View.GONE);
				}
				if (API.systemMessageCount == 0) {
					messageCountTextView.setVisibility(View.GONE);
				} else {
					messageCountTextView.setText(API.systemMessageCount + "");
				}
				if (memberDetail.orderInfo != null) {
					pendingOrderTextView
							.setText(memberDetail.orderInfo.orderPendingCount
									+ "");
					paymentOrderTextView
							.setText(Integer
									.parseInt(memberDetail.orderInfo.order_consume_count)
									+ Integer
											.parseInt(memberDetail.orderInfo.order_non_consume_count)
									+ "");
					int allConsumeCount = memberDetail.orderInfo.orderConsumeCount
							+ memberDetail.orderInfo.orderNonConsumeCount;
					consumeOrderTextView.setText(allConsumeCount + "");
				}
				dynamicNewIcon.setVisibility(View.GONE);
				if (memberDetail.dynamicNew == HAS_NEW_DYNAMIC) {
					if (meType == ME) {
						dynamicNewIcon.setVisibility(View.VISIBLE);
					}
				}
				mPullRefreshScrollView.onRefreshComplete();
				break;
			case FAILURE:
				mPullRefreshScrollView.onRefreshComplete();
				if (msg.obj != null) {
					navigationView.showMessage((String) msg.obj);
				}
				break;
			case CHANGEFOLLOWSUCCESS:
				if (msg.obj != null) {
					JSONObject data = (JSONObject) msg.obj;
					String follow_status = data.optString("follow_status");
					memberDetail.viewMember.followStatus = follow_status;
					MineActivity.this.follow_status = changeStatues(follow_status);
					userFolowTextView.setText(changeStatues(follow_status));
				}
				break;
			}
		}
	};

	@Override
	public void initData() {
		reloadUserData();
	}

	@Override
	public void onClick(View v) {
		final int id = v.getId();
		switch (id) {
		case R.id.userMessageLayout:
			/** 消息 */
			Intent messageIntent = new Intent(this, MessageListActivity.class);
			startActivity(messageIntent);
			break;
		case R.id.activitiesLayout:
			/** 动态 */
			if (memberDetail.dynamicCount > 0) {
				Intent activitiesIntent = new Intent(this,
						DynamicActivity.class);
				activitiesIntent.putExtra("code", 100);
				if (view_member_id.equals(memberId)) {
					meType = ME;
				}
				if (meType == OTHER) {
					activitiesIntent.putExtra("view_member_id", view_member_id);
				}
				startActivity(activitiesIntent);
			}
			break;
		case R.id.settingsLayout:
			Intent settingIntent = new Intent(this, SettingsActivity.class);
			startActivity(settingIntent);
			break;
		case R.id.fansLayout:
			/** 粉丝 */
			int fansCount = Integer
					.parseInt(memberDetail.viewMember.followedCount);
			System.out.println("fansCount:" + fansCount);
			if (fansCount > 0) {
				Intent fansIntent = new Intent(this, FansListActivity.class);
				if (meType == ME) {
					fansIntent.putExtra("code", "");
					fansIntent.putExtra("member_id",
							PreferencesUtils.getString(this, "member_id"));
					fansIntent.putExtra("member_name",
							PreferencesUtils.getString(this, "member_name"));
				}
				if (meType == OTHER) {
					fansIntent.putExtra("code", "code");
					fansIntent.putExtra("member_id", view_member_id);
					fansIntent.putExtra("member_name", view_member_name);
				}
				startActivity(fansIntent);
			}
			break;
		case R.id.followedLayout:
			/** 关注 */
			int followedCount = Integer
					.parseInt(memberDetail.viewMember.followingCount);
			System.out.println("count:" + followedCount);
			if (followedCount > 0) {
				Intent followIntent = new Intent(this, FollowListActivity.class);
				if (meType == ME) {
					followIntent.putExtra("following_member_id", "");
				}
				if (meType == OTHER) {
					followIntent
							.putExtra("following_member_id", view_member_id);
				}
				startActivity(followIntent);
			}
			break;
		case R.id.cartLayout:
			Intent cartIntent = new Intent(this, CartActivity.class);
			cartIntent.putExtra("store", memberDetail.cartInfo.storeInfo);
			startActivity(cartIntent);
			break;
		case R.id.pendingOrderLayout:
			Intent pendingOrderIntent = new Intent(this, OrderActivity.class);
			pendingOrderIntent.putExtra("orderType", 10);
			startActivity(pendingOrderIntent);
			break;
		case R.id.paymentOrderLayout:
			Intent paymentOrderntent = new Intent(this, OrderActivity.class);
			paymentOrderntent.putExtra("orderType", 20);
			startActivity(paymentOrderntent);
			break;
		case R.id.consumeOrderLayout:
			Intent consumeOrderIntent = new Intent(this, OrderActivity.class);
			consumeOrderIntent.putExtra("orderType", 40);
			startActivity(consumeOrderIntent);
			break;
		case R.id.userScoreTextView:
			/** 积分 */
			// Intent scoreIntent = new Intent(MineActivity.this,
			// ScoreRecordActivity.class);
			// startActivity(scoreIntent);
			break;
		case R.id.userFolowTextView:
			/** 关注或取消关注此用户 */
			System.out.println("follow_status:" + follow_status);
			ChangeStatus(view_member_id, "",
					FansListAdapter.AddOrCancel(follow_status));
			break;
		}
	}

	/** 转换关注状态 */
	private String changeStatues(String status) {
		String statuString = "";
		if (status.equals("0")) {
			statuString = StringUtils.getString(MineActivity.this,
					R.string.not_follow);
		}
		if (status.equals("1")) {
			statuString = StringUtils.getString(MineActivity.this,
					R.string.followed);
		}
		if (status.equals("2")) {
			statuString = StringUtils.getString(MineActivity.this,
					R.string.follow_each_other);
		}
		return statuString;
	}

	/** 请求更改关注状态 */
	public void ChangeStatus(String following_member_id,
			String following_store_id, String following_status) {
		String member_id = PreferencesUtils.getString(this, "member_id");
		EditFollowStatusResp mStatusResp = new EditFollowStatusResp(this,
				handler, token, member_id, following_member_id,
				following_store_id, following_status);
		mStatusResp.asyncInvoke(CHANGEFOLLOWSUCCESS, FAILURE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		navigationView.setBackgroundColor(Color.parseColor(colorValue));
		// showHead();
	}

	private void showHead() {
		if (!StringUtils.isBlank(token)) {
			if (memberDetail.viewMember.memberName.equals(member_name)) {
				// 当前用户
				navigationView.setNavigateItemText(
						NavigationView.LEFT_TEXT_VIEW, R.string.mine);
				navigationView.setNavigateItemVisibility(
						NavigationView.LEFT_IMAGE_VIEW, View.GONE);
				userFolowTextView.setVisibility(View.GONE);
				scoreCountTextView.setVisibility(View.VISIBLE);
			} else {
				// 其他用户
				userFolowTextView.setVisibility(View.VISIBLE);
				scoreCountTextView.setVisibility(View.GONE);
				navigationView.setNavigateItemBackground(
						NavigationView.LEFT_TEXT_VIEW, R.drawable.back);
				navigationView.setNavigateItemText(NavigationView.TITLE,
						memberDetail.viewMember.memberName);
				navigationView.setNavigateItemVisibility(
						NavigationView.LEFT_IMAGE_VIEW, View.GONE);

			}
			navigationView.setNavigateItemBackground(
					NavigationView.FIRST_RIGHT_TEXT_VIEW, R.drawable.share);
			navigationView.setNavigateItemBackground(
					NavigationView.SECOND_RIGHT_TEXT_VIEW, R.drawable.navi);
			navigationView
					.setCurrentCategoryTriangle(NavigationView.TRIANGLE_MINE);
			navigationView.setNavigateItemText(
					NavigationView.FIRST_RIGHT_TEXT_VIEW, "");
			navigationView.setNavigateItemText(
					NavigationView.SECOND_RIGHT_TEXT_VIEW, "");
		} else {
			navigationView.setNavigateItemBackground(
					NavigationView.LEFT_TEXT_VIEW, R.drawable.back);
			navigationView.setNavigateItemVisibility(
					NavigationView.LEFT_IMAGE_VIEW, View.GONE);
			navigationView.setNavigateItemText(
					NavigationView.FIRST_RIGHT_TEXT_VIEW, R.string.login);
			navigationView.setNavigateItemText(
					NavigationView.SECOND_RIGHT_TEXT_VIEW, R.string.register);
			navigationView.setNavigateItemBackground(
					NavigationView.FIRST_RIGHT_TEXT_VIEW, null);
			navigationView.setNavigateItemBackground(
					NavigationView.SECOND_RIGHT_TEXT_VIEW, null);
		}
	}
}
