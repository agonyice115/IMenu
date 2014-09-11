package com.huassit.imenu.android.ui;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.MyApplication;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.adapter.CommentAdapter;
import com.huassit.imenu.android.biz.EditDynamicCommentResp;
import com.huassit.imenu.android.biz.EditDynamicGoodsResp;
import com.huassit.imenu.android.biz.GetDynamicCommentResp;
import com.huassit.imenu.android.biz.GetDynamicDetailResp;
import com.huassit.imenu.android.biz.GetDynamicGoodsResp;
import com.huassit.imenu.android.db.dao.ShareDynamicDao;
import com.huassit.imenu.android.model.Comment;
import com.huassit.imenu.android.model.Dynamic;
import com.huassit.imenu.android.model.Goods;
import com.huassit.imenu.android.model.Member;
import com.huassit.imenu.android.model.MemberGoods;
import com.huassit.imenu.android.model.MemberMenu;
import com.huassit.imenu.android.model.RMember;
import com.huassit.imenu.android.model.Share;
import com.huassit.imenu.android.model.Store;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.ScreenUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.util.TimeUtil;
import com.huassit.imenu.android.util.Utility;
import com.huassit.imenu.android.view.NavigationView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DynamicDetailActivity extends BaseActivity {

	/**
	 * 是否加载更多，不清除数据
	 */
	private static final String LIKE = "1";
	private static final String CANCEL_LIKE = "0";
	private boolean isFoot = false;
	private ImageView memberIcon;
	private ImageView dynamicPic;
	private TextView memberName;
	private TextView storeAddress;
	private TextView people;
	private LinearLayout goodsBackground;
	private TextView likeCount;
	private LinearLayout likeView;
	private int currentPage = 1;
	private TextView title;
	private ImageView commentButton;
	private LinearLayout llComment;
	private EditText et_comment;
	private static final String PAGE_SIZE = "10";
	private Button send;
	private ListView commentListView;
	private CommentAdapter commentAdapter;
	private NavigationView mNavigationView;
	private PullToRefreshScrollView parentScrollView;
	private static final int SUCCESS = 100;
	private static final int FAILED = 101;
	private static final int EDIT_COMMENT_SUCCESS = 200;
	private static final int EDIT_COMMENT_FAILED = 201;
	private static final int GET_COMMENT_SUCCESS = 300;
	private static final int GET_COMMENT_FAILED = 301;
	private static final int EDIT_GOODS_SUCCESS = 400;
	private static final int EDIT_GOODS_FAILED = 401;
	private static final int GET_GOODS_SUCCESS = 500;
	private static final int GET_GOODS_FAILED = 501;
	private String goodsType = "0";
	private Dynamic dynamic = new Dynamic();
	private SimpleDateFormat sDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd   hh:mm:ss");
	private ImageLoader imageLoader;
	private String dynamic_goods_id;
	private LinearLayout menuPictures;
	private int screenWidth;
	private Goods mygoods;
	private boolean isGood = false;
	// private ArrayList<LinearLayout> llList = new ArrayList<LinearLayout>();
	private String memberId;
	private DisplayImageOptions options;
	private DisplayImageOptions options2;
	private Member viewMember;
	private ImageView likeImg;
	private String iconLoaction;
	private String iconName;
	private ShareDynamicDao shareDynamicDao;
	private Store store;
	private ArrayList<MemberMenu> memberMenuList;

	@Override
	public int getContentView() {
		return R.layout.dynamic_detail;
	}

	@Override
	public void initView() {
		shareDynamicDao = new ShareDynamicDao(this);
		iconLoaction = PreferencesUtils.getString(context, "iconLocation");
		iconName = PreferencesUtils.getString(context, "iconName");
		options = MyApplication.getDisplayImageOptions(context, 19);
		options2 = MyApplication.getDisplayImageOptions(context, 50);
		memberId = PreferencesUtils.getString(context, "member_id");
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		screenWidth = metric.widthPixels; // 屏幕宽度（像素）
		imageLoader = ImageLoader.getInstance();
		menuPictures = (LinearLayout) findViewById(R.id.menuPictrues);
		likeImg = (ImageView) findViewById(R.id.like_img);
		goodsBackground = (LinearLayout) findViewById(R.id.goods_background);
		parentScrollView = (PullToRefreshScrollView) findViewById(R.id.srcollView);
		commentListView = (ListView) findViewById(R.id.commentListView);
		commentAdapter = new CommentAdapter(context,
				R.layout.dynamic_comment_list_item);
		commentListView.setAdapter(commentAdapter);
		llComment = (LinearLayout) findViewById(R.id.ll_comment);
		memberIcon = (ImageView) findViewById(R.id.memberIcon);
		dynamicPic = (ImageView) findViewById(R.id.dynamicPic);
		memberName = (TextView) findViewById(R.id.memberName);
		storeAddress = (TextView) findViewById(R.id.store_address);
		people = (TextView) findViewById(R.id.people);
		likeCount = (TextView) findViewById(R.id.like_count);
		likeView = (LinearLayout) findViewById(R.id.like_view);
		title = (TextView) findViewById(R.id.dynamic_title);
		commentButton = (ImageView) findViewById(R.id.comment);
		et_comment = (EditText) findViewById(R.id.et_comment);
		send = (Button) findViewById(R.id.send);
		mNavigationView = (NavigationView) findViewById(R.id.navigationView);
		mNavigationView.setNavigateItemOnClickListener(
				NavigationView.FIRST_RIGHT_TEXT_VIEW, new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (!StringUtils.isBlank(token)) {
							// 分享
							Share share = shareDynamicDao.getShareDynamic();
							share.content = MessageFormat.format(share.content,
									viewMember.memberName, store.name);
							share.imagePath = imageLoader
									.getDiskCache()
									.get(memberMenuList.get(0).image_location
											+ memberMenuList.get(0).image_name)
									.getAbsolutePath();
							share.imageUrl = memberMenuList.get(0).image_location
									+ memberMenuList.get(0).image_name;
							mNavigationView.showShareView(share);
						} else {
							// 登录
							Intent intent = new Intent(
									DynamicDetailActivity.this,
									LoginActivity.class);
							startActivity(intent);
						}
					}
				});
		mNavigationView.setNavigateItemOnClickListener(
				NavigationView.SECOND_RIGHT_TEXT_VIEW, new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (!StringUtils.isBlank(token)) {
							mNavigationView.showCategoryView();
						} else {
							// 注册
							Intent intent = new Intent(
									DynamicDetailActivity.this,
									RegisterActivity.class);
							startActivity(intent);
						}
					}
				});

		commentButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!StringUtils.isBlank(token)) {
					llComment.setVisibility(View.VISIBLE);
					et_comment.requestFocus();
					InputMethodManager m = (InputMethodManager) llComment
							.getContext().getSystemService(
									Context.INPUT_METHOD_SERVICE);
					m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				} else {
					new AlertDialog.Builder(context)
							.setMessage(
									StringUtils.getString(
											DynamicDetailActivity.this,
											R.string.login_to_continue))
							.setPositiveButton(
									StringUtils.getString(
											DynamicDetailActivity.this,
											R.string.login_now),
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											Intent intent = new Intent(
													DynamicDetailActivity.this,
													LoginActivity.class);
											intent.putExtra("sourceActivity",
													"MenuLikeAndComment");
											startActivity(intent);
										}
									})
							.setNegativeButton(
									StringUtils.getString(
											DynamicDetailActivity.this,
											R.string.reg_new_member),
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											Intent intent = new Intent(
													DynamicDetailActivity.this,
													RegisterActivity.class);
											intent.putExtra("sourceActivity",
													"MenuLikeAndComment");
											startActivity(intent);
										}
									}).create().show();
				}

			}
		});

		goodsBackground.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!StringUtils.isBlank(token)) {
					mygoods = new Goods();
					mygoods.member_id = PreferencesUtils.getString(context,
							"member_id");
					mygoods.icon_location = iconLoaction;
					mygoods.icon_name = iconName;
					if (CANCEL_LIKE.equals(goodsType)) {
						// 发布赞
						mygoods.dynamic_goods_id = "";
						showLikeView();
						EditDynamicGoodsResp resp = new EditDynamicGoodsResp(
								DynamicDetailActivity.this, handler, dynamic,
								mygoods, token, goodsType);
						resp.asyncInvoke(EDIT_GOODS_SUCCESS, EDIT_GOODS_FAILED);
					} else {
						// 删除赞
						mygoods.dynamic_goods_id = dynamic_goods_id;
						showNoLikeView();
						EditDynamicGoodsResp resp = new EditDynamicGoodsResp(
								DynamicDetailActivity.this, handler, dynamic,
								mygoods, token, goodsType);
						resp.asyncInvoke(EDIT_GOODS_SUCCESS, EDIT_GOODS_FAILED);
					}
				} else {
					new AlertDialog.Builder(context)
							.setMessage(
									StringUtils.getString(
											DynamicDetailActivity.this,
											R.string.login_to_continue))
							.setPositiveButton(
									StringUtils.getString(
											DynamicDetailActivity.this,
											R.string.login_now),
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											Intent intent = new Intent(
													DynamicDetailActivity.this,
													LoginActivity.class);
											intent.putExtra("sourceActivity",
													"MenuLikeAndComment");
											startActivity(intent);
										}
									})
							.setNegativeButton(
									StringUtils.getString(
											DynamicDetailActivity.this,
											R.string.reg_new_member),
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											Intent intent = new Intent(
													DynamicDetailActivity.this,
													RegisterActivity.class);
											intent.putExtra("sourceActivity",
													"MenuLikeAndComment");
											startActivity(intent);
										}
									}).create().show();
				}
			}

		});
		parentScrollView.setMode(Mode.BOTH);
		parentScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(
				"上次刷新时间：" + TimeUtil.getCurrentTime());
		parentScrollView
				.setOnRefreshListener(new OnRefreshListener2<ScrollView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						// 下拉刷新
						currentPage = 1;
						getDynamicComment(dynamic, currentPage + "");
						loadDynamicDetail(dynamic);
						GetDynamicGoods(dynamic);
						parentScrollView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(
										StringUtils.getString(
												DynamicDetailActivity.this,
												R.string.last_refresh_time)
												+ TimeUtil.getCurrentTime());
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						// 上拉加载更多
						currentPage++;
						isFoot = true;
						getDynamicComment(dynamic, currentPage + "");
						parentScrollView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(
										StringUtils.getString(
												DynamicDetailActivity.this,
												R.string.last_refresh_time)
												+ TimeUtil.getCurrentTime());
					}
				});

	}

	private void showNoLikeView() {
		goodsType = CANCEL_LIKE;
		goodsBackground.setBackgroundColor(getResources().getColor(
				android.R.color.transparent));
		likeImg.setImageResource(R.drawable.like_big_gray);
		likeCount.setTextColor(getResources().getColor(R.color.black));
	}

	private void showLikeView() {
		goodsType = LIKE;
		goodsBackground.setBackgroundColor(getResources()
				.getColor(R.color.blue));
		likeImg.setImageResource(R.drawable.like_big_white);
		likeCount.setTextColor(getResources().getColor(R.color.white));
	}

	@Override
	public void initData() {
		dynamic = (Dynamic) getIntent().getSerializableExtra("dynamic");
		loadDynamicDetail(dynamic);
		getDynamicComment(dynamic, currentPage + "");
		GetDynamicGoods(dynamic);
		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Comment comment = new Comment();
				RMember r = new RMember();
				r.r_member_id = "";
				comment.r_member_info = r;
				comment.dynamic_comment_id = "";
				comment.member_id = PreferencesUtils.getString(context,
						"member_id");
				comment.comment_content = et_comment.getText().toString();
				EditDynamicCommentResp resp = new EditDynamicCommentResp(
						DynamicDetailActivity.this, handler, comment, dynamic,
						"1", token);
				resp.asyncInvoke(EDIT_COMMENT_SUCCESS, EDIT_COMMENT_FAILED);
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				llComment.setVisibility(View.GONE);
			}
		});
	}

	private void GetDynamicGoods(Dynamic dynamic) {
		String memberId = PreferencesUtils.getString(context, "member_id");
		GetDynamicGoodsResp resp = new GetDynamicGoodsResp(
				DynamicDetailActivity.this, handler, memberId, dynamic, "10",
				"", token);
		resp.asyncInvoke(GET_GOODS_SUCCESS, GET_GOODS_FAILED);
	}

	private void loadDynamicDetail(Dynamic dynamic) {
		String memberId = PreferencesUtils.getString(this, "member_id");
		GetDynamicDetailResp resp = new GetDynamicDetailResp(
				DynamicDetailActivity.this, handler, dynamic, token, memberId);
		resp.asyncInvoke(SUCCESS, FAILED);
	}

	private void getDynamicComment(Dynamic dynamic, String page) {
		GetDynamicCommentResp getDynamicCommentResp = new GetDynamicCommentResp(
				DynamicDetailActivity.this, handler, dynamic, PAGE_SIZE, page,token);
		getDynamicCommentResp.asyncInvoke(GET_COMMENT_SUCCESS,
				GET_COMMENT_FAILED);

	}

	Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			if (parentScrollView != null && parentScrollView.isShown()) {
				parentScrollView.onRefreshComplete();
			}
			switch (msg.what) {
			case SUCCESS:
				@SuppressWarnings("unchecked")
				final Map<String, Object> dynamicPicMap = (Map<String, Object>) msg.obj;
				viewMember = (Member) dynamicPicMap.get("view_member");
				store = (Store) dynamicPicMap.get("store_info");
				dynamic = (Dynamic) dynamicPicMap.get("dynamic_info");
				memberMenuList = (ArrayList<MemberMenu>) dynamicPicMap
						.get("menu_list");
				loadMenuView(memberMenuList);
				imageLoader.displayImage(viewMember.dynamicLocation
						+ viewMember.dynamicName, dynamicPic);
				imageLoader.displayImage(viewMember.iconLocation
						+ viewMember.iconName, memberIcon, options2);
				memberName.setText(viewMember.memberName);
				storeAddress.setText(store.address);
				people.setText(dynamic.getPeople());
				title.setText(dynamic.getTitle());
				break;
			case FAILED:

				break;
			case EDIT_COMMENT_FAILED:
				prompt(StringUtils.getString(DynamicDetailActivity.this,
						R.string.conmment_false));
				break;
			case EDIT_COMMENT_SUCCESS:
				String dynamic_comment_id = (String) msg.obj;

				final Comment comment = new Comment();

				comment.icon_location = iconLoaction;
				comment.icon_name = iconName;
				comment.member_name = PreferencesUtils.getString(context,
						"member_name");
				comment.comment_content = et_comment.getText().toString();
				comment.comment_date = sDateFormat.format(new Date());
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						commentAdapter.getDataList().add(comment);
						commentAdapter.notifyDataSetChanged();
						Utility.setListViewHeightBasedOnChildren(commentListView);
					}

				});
				et_comment.getText().clear();
				llComment.setVisibility(View.GONE);
				break;

			case GET_COMMENT_SUCCESS:
				@SuppressWarnings("unchecked")
				final ArrayList<Comment> commentList = (ArrayList<Comment>) msg.obj;
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (!isFoot) {
							commentAdapter.getDataList().clear();
						}
						isFoot = false;
						commentAdapter.getDataList().addAll(commentList);
						commentAdapter.notifyDataSetChanged();
						// 计算出Listview的高度直接展开
						Utility.setListViewHeightBasedOnChildren(commentListView);
					}
				});
				llComment.setVisibility(View.GONE);
				break;
			case GET_COMMENT_FAILED:
				break;
			case GET_GOODS_SUCCESS:
				@SuppressWarnings("unchecked")
				Map<String, Object> map = (HashMap<String, Object>) msg.obj;
				String goodsCount = (String) map.get("goodsCount");
				@SuppressWarnings("unchecked")
				ArrayList<Goods> goodsList = (ArrayList<Goods>) map
						.get("goodsList");
				MemberGoods memberGoods = (MemberGoods) map.get("memberGoods");
				likeCount.setText(goodsCount);
				if (memberGoods.goods_status.equals("1")) {
					// 已赞
					dynamic_goods_id = memberGoods.dynamic_goods_id;
					showLikeView();
				} else {
					// 未赞
					showNoLikeView();
				}
				likeView.removeAllViews();
				for (Goods goods : goodsList) {
					if (goods.dynamic_goods_id
							.equals(memberGoods.dynamic_goods_id)) {
						isGood = true;
					}
					likeView.addView(getImageView(goods, 0));
				}
				break;
			case GET_GOODS_FAILED:
				prompt(StringUtils.getString(DynamicDetailActivity.this,
						R.string.goods_list_loading_false));
				break;
			case EDIT_GOODS_SUCCESS:
				dynamic_goods_id = (String) msg.obj;
				if (goodsType.equals("1")) {
					// 发布赞
					goodsType = "1";
					goodsBackground.setBackgroundColor(getResources().getColor(
							R.color.blue));
				} else {
					// 删除赞
					goodsType = "0";
					// goodsBackground.setBackgroundColor(getResources().getColor(
					// R.color.black));
				}
				GetDynamicGoodsResp goodsResp = new GetDynamicGoodsResp(
						DynamicDetailActivity.this, handler, memberId, dynamic,
						"10", "", token);
				goodsResp.asyncInvoke(GET_GOODS_SUCCESS, GET_GOODS_FAILED);
				break;
			case EDIT_GOODS_FAILED:
				break;
			default:
				break;
			}
		}

	};

	private void loadMenuView(ArrayList<MemberMenu> memberMenuList) {
		menuPictures.removeAllViews();
		for (int i = 0; i < memberMenuList.size(); i++) {
			if (i % 3 == 0) {
				View relativeLayout = getLayoutInflater().inflate(
						R.layout.menu_picture_item, null);
				FrameLayout fl1 = (FrameLayout) relativeLayout
						.findViewById(R.id.fl1);
				FrameLayout fl2 = (FrameLayout) relativeLayout
						.findViewById(R.id.fl2);
				FrameLayout fl3 = (FrameLayout) relativeLayout
						.findViewById(R.id.fl3);
				ImageView iv1 = (ImageView) relativeLayout
						.findViewById(R.id.iv1);
				ImageView iv2 = (ImageView) relativeLayout
						.findViewById(R.id.iv2);
				ImageView iv3 = (ImageView) relativeLayout
						.findViewById(R.id.iv3);
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
						screenWidth / 3, screenWidth / 3);
				fl1.setLayoutParams(layoutParams);
				fl2.setLayoutParams(layoutParams);
				fl3.setLayoutParams(layoutParams);
				TextView tv1 = (TextView) relativeLayout.findViewById(R.id.tv1);
				TextView goodsCount1 = (TextView) relativeLayout
						.findViewById(R.id.like_count1);
				TextView commentCount1 = (TextView) relativeLayout
						.findViewById(R.id.comments_count1);
				TextView tv2 = (TextView) relativeLayout.findViewById(R.id.tv2);
				TextView goodsCount2 = (TextView) relativeLayout
						.findViewById(R.id.like_count2);
				TextView commentCount2 = (TextView) relativeLayout
						.findViewById(R.id.comments_count2);
				TextView tv3 = (TextView) relativeLayout.findViewById(R.id.tv3);
				TextView goodsCount3 = (TextView) relativeLayout
						.findViewById(R.id.like_count3);
				TextView commentCount3 = (TextView) relativeLayout
						.findViewById(R.id.comments_count3);

				MemberMenu memberMenu1 = memberMenuList.get(i);
				imageLoader.displayImage(memberMenu1.image_location
						+ memberMenu1.image_name, iv1);
				tv1.setText(memberMenu1.menu_name);
				goodsCount1.setText(memberMenu1.goods_count);
				commentCount1.setText(memberMenu1.comment_count);
				fl1.setOnClickListener(getListener(memberMenu1));
				if (i + 1 < memberMenuList.size()) {
					fl2.setVisibility(View.VISIBLE);
					MemberMenu memberMenu2 = memberMenuList.get(i + 1);
					imageLoader.displayImage(memberMenu2.image_location
							+ memberMenu2.image_name, iv2);
					tv2.setText(memberMenu2.menu_name);
					goodsCount2.setText(memberMenu2.goods_count);
					commentCount2.setText(memberMenu2.comment_count);
					fl2.setOnClickListener(getListener(memberMenu2));
				}
				if (i + 2 < memberMenuList.size()) {
					fl3.setVisibility(View.VISIBLE);
					MemberMenu memberMenu3 = memberMenuList.get(i + 2);
					imageLoader.displayImage(memberMenu3.image_location
							+ memberMenu3.image_name, iv3);
					tv3.setText(memberMenu3.menu_name);
					goodsCount3.setText(memberMenu3.goods_count);
					commentCount3.setText(memberMenu3.comment_count);
					fl3.setOnClickListener(getListener(memberMenu3));
				}
				menuPictures.addView(relativeLayout);
			}
		}
	}

	private OnClickListener getListener(final MemberMenu memberMenu) {
		return new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(DynamicDetailActivity.this,
						MenuDetailDynamic.class);
				intent.putExtra("memberMenu", memberMenu);
				intent.putExtra("view_member", viewMember);
				intent.putExtra("sourceActivity", "DynamicDetail");
				startActivity(intent);
			}
		};
	};

	private View getImageView(Goods goods, int code) {

		ImageView imageView = new ImageView(this);
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ScreenUtils
				.dpToPxInt(context, 30), ScreenUtils.dpToPxInt(context, 30));
		params.setMargins(5, 10, 5, 5);
		imageView.setLayoutParams(params);
		imageLoader.displayImage(goods.icon_location + goods.icon_name,
				imageView, options);
		if (code == 1 || isGood == true) {
			// 我点的赞
			imageView.setTag("myGood");
			isGood = false;
		}
		return imageView;
	}

	/**
	 * 是否把滚动事件交给父scrollview
	 * 
	 * @param flag
	 */
	public void setParentScrollAble(boolean flag) {
		parentScrollView.requestDisallowInterceptTouchEvent(!flag);// 这里的parentScrollView就是listview外面的那个scrollview
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!StringUtils.isBlank(token)) {
			mNavigationView.setNavigateItemVisibility(
					NavigationView.LEFT_IMAGE_VIEW, View.GONE);
			mNavigationView.setNavigateItemBackground(
					NavigationView.LEFT_TEXT_VIEW, R.drawable.back);
			mNavigationView.setNavigateItemText(NavigationView.TITLE,
					R.string.activities);
			mNavigationView.setNavigateItemBackground(
					NavigationView.FIRST_RIGHT_TEXT_VIEW, R.drawable.share);
			mNavigationView.setNavigateItemBackground(
					NavigationView.SECOND_RIGHT_TEXT_VIEW, R.drawable.navi);

			mNavigationView.setNavigateItemOnClickListener(
					NavigationView.LEFT_TEXT_VIEW, new OnClickListener() {

						@Override
						public void onClick(View v) {
							finish();
						}
					});
		} else {
			mNavigationView.setNavigateItemBackground(
					NavigationView.LEFT_TEXT_VIEW, R.drawable.back);
			mNavigationView.setNavigateItemOnClickListener(
					NavigationView.LEFT_TEXT_VIEW, new OnClickListener() {

						@Override
						public void onClick(View v) {
							finish();
						}
					});
			mNavigationView.setNavigateItemVisibility(
					NavigationView.LEFT_IMAGE_VIEW, View.GONE);
			mNavigationView.setNavigateItemText(
					NavigationView.FIRST_RIGHT_TEXT_VIEW, R.string.login);
			mNavigationView.setNavigateItemText(
					NavigationView.SECOND_RIGHT_TEXT_VIEW, R.string.register);
			mNavigationView.setNavigateItemBackground(
					NavigationView.SECOND_RIGHT_TEXT_VIEW, null);
			mNavigationView.setNavigateItemBackground(
					NavigationView.FIRST_RIGHT_TEXT_VIEW, null);
		}
	}
}
