package com.huassit.imenu.android.ui;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.MyApplication;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.adapter.MenuFromMemberAdapter;
import com.huassit.imenu.android.biz.AddOrderResp;
import com.huassit.imenu.android.biz.EditCartResp;
import com.huassit.imenu.android.biz.GetMenuDetailResp;
import com.huassit.imenu.android.biz.GetMenuInfoListResp;
import com.huassit.imenu.android.db.dao.MenuUnitDao;
import com.huassit.imenu.android.db.dao.ShareMenuDao;
import com.huassit.imenu.android.model.Cart;
import com.huassit.imenu.android.model.MemberMenu;
import com.huassit.imenu.android.model.Menu;
import com.huassit.imenu.android.model.MenuUnit;
import com.huassit.imenu.android.model.OrderInfo;
import com.huassit.imenu.android.model.Share;
import com.huassit.imenu.android.model.Store;
import com.huassit.imenu.android.ui.cart.CartActivity;
import com.huassit.imenu.android.util.NumberFormatUtils;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.ScreenUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.util.TimeUtil;
import com.huassit.imenu.android.view.HorizontalListView;
import com.huassit.imenu.android.view.NavigationView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MenuDetailActivity extends BaseActivity {

	private String memberId;
	private static final int GET_MENU_SUCCESS = 800;
	private static final int GET_MENU_FAILED = 801;
	/** 获取评论成功 */
	private static final int GET_COMMENT_SUCCESS = 300;
	/** 获取评论失败 */
	private static final int GET_COMMENT_FAILED = 301;
	/** 获取赞成功 */
	private static final int GET_GOODS_SUCCESS = 400;
	/** 获取赞失败 */
	private static final int GET_GOODS_FAILED = 401;
	/** 编辑购物车成功 */
	private static final int EDIT_CART_SUCCESS = 200;
	/** 编辑购物车失败 */
	private static final int EDIT_CART_FAILED = 201;
	/**
	 * 生成订单成功标石
	 */
	private static final int ADD_ORDER_SUCCESS = 600;
	/**
	 * 生成订单失败标石
	 */
	private static final int ADD_ORDER_FAILED = 601;
	/**
	 * 提交购物车数据成功标石
	 */
	private static final int SUBMIT_SUCCESS = 500;
	/**
	 * 提交购物车数据失败标石
	 */
	private static final int SUBMIT_FAILED = 501;
	/**
	 * 未登录时更新购物车数据成功标石
	 */
	private static final int NOT_LOGIN_EDIT_CART = 700;
	/** 订单 */
	private static final String ORDER = "1";
	/** 取消订单 */
	private static final String CANCEL_ORDER = "2";
	private ImageView iv_menu;
	private TextView menu_name;
	private TextView menu_price;
	private TextView menu_unit;
	private TextView menu_count;
	private TextView menu_count_type;
	private TextView order;
	private Menu menu = new Menu();
	private Store store;
	private ArrayList<MemberMenu> memberMenuList;
	private ImageLoader imageLoader;
	private NavigationView mNavigationView;
	private LinearLayout cartLayout;
	private TextView tv_orderCount;
	private TextView tv_totalPrice;
	private TextView gotoCart;
	private ArrayList<Menu> orderedMenuList = new ArrayList<Menu>();
	private MyApplication application;
	private float totalPrice;
	private HashMap<String, Cart> cartMap;
	private HorizontalListView menuListView;
	private MenuFromMemberAdapter menuFromMemberAdapter;
	private String menuId;
	/**
	 * 店铺类型,签约类型(1-在线付|2-到店付|0-未签约)
	 */
	private String signing_type = "";
	/**
	 * 促销类型(1-有促销|0-无促销)
	 */
	private String coupon_type;
	private PullToRefreshScrollView mPullRefreshScrollView;
	private ShareMenuDao shareMenuDao;
	private MenuUnitDao menuUnitDao;
	private TextView is_sale;

	@Override
	public int getContentView() {
		return R.layout.menu_detail;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initView() {
		shareMenuDao = new ShareMenuDao(this);
		menuListView = (HorizontalListView) findViewById(R.id.horizontallistview);
		application = (MyApplication) getApplicationContext();
		imageLoader = ImageLoader.getInstance();
		mNavigationView = (NavigationView) findViewById(R.id.navigationView);
		mNavigationView.setNavigateItemBackground(
				NavigationView.LEFT_TEXT_VIEW, R.drawable.back);
		mNavigationView.setNavigateItemVisibility(
				NavigationView.LEFT_IMAGE_VIEW, View.GONE);
		mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.refreshScrollView);
		mPullRefreshScrollView.getLoadingLayoutProxy()
				.setLastUpdatedLabel(
						StringUtils.getString(MenuDetailActivity.this,
								R.string.last_refresh_time)
								+ TimeUtil.getCurrentTime());
		mPullRefreshScrollView
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {

					@Override
					public void onRefresh(PullToRefreshBase refreshView) {
						loadMenuDetail();
						showCartLayout();
						// 更新评论以及赞的数目
						GetMenuInfoListResp resp = new GetMenuInfoListResp(
								MenuDetailActivity.this, handler, token, menuId,
								Integer.MAX_VALUE + "", "", "");
						resp.asyncInvoke(SUCCESS, FAILURE);
						mPullRefreshScrollView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(
										StringUtils.getString(
												MenuDetailActivity.this,
												R.string.last_refresh_time)
												+ TimeUtil.getCurrentTime());
					}
				});
		tv_orderCount = (TextView) findViewById(R.id.orderCount);
		tv_totalPrice = (TextView) findViewById(R.id.totalPrice);
		gotoCart = (TextView) findViewById(R.id.gotoCart);
		cartLayout = (LinearLayout) findViewById(R.id.menu_detail_cartLayout);
		iv_menu = (ImageView) findViewById(R.id.iv_menu);
		menu_name = (TextView) findViewById(R.id.menu_name);
		menu_price = (TextView) findViewById(R.id.menu_price);
		menu_unit = (TextView) findViewById(R.id.menu_unit);
		menu_count = (TextView) findViewById(R.id.menu_count);
		menu_count_type = (TextView) findViewById(R.id.menu_count_type);
		order = (TextView) findViewById(R.id.orderMenu);
		ScreenUtils.ScreenResolution screenResolution = ScreenUtils
				.getScreenResolution(this);
		LinearLayout.LayoutParams newLayoutParams = new LinearLayout.LayoutParams(
				screenResolution.getWidth(), screenResolution.getWidth());
		iv_menu.setLayoutParams(newLayoutParams);
		is_sale = (TextView) findViewById(R.id.is_sale);
	}

	public void countTotalPrice(ArrayList<Menu> menuList) {
		totalPrice = 0;
		for (Menu menu : menuList) {
			totalPrice += Float.valueOf(menu.menu_price) * menu.menu_count;
		}
	}

	public void loadMenuDetail() {
		GetMenuDetailResp getMenuDetailResp = new GetMenuDetailResp(
				MenuDetailActivity.this, handler, menuId);
		getMenuDetailResp.asyncInvoke(GET_MENU_SUCCESS, GET_MENU_FAILED);
	}

	@Override
	public void initData() {
		menuFromMemberAdapter = new MenuFromMemberAdapter(
				MenuDetailActivity.this, R.layout.menu_detail_list_item);
		menuListView.setAdapter(menuFromMemberAdapter);
		memberId = PreferencesUtils.getString(context, "member_id");
		menuId = getIntent().getStringExtra("menuId");
		loadMenuDetail();
		// loadMenuRelatedPicture();

		mNavigationView.setNavigateItemOnClickListener(
				NavigationView.LEFT_TEXT_VIEW, new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();

					}
				});
		menuListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MemberMenu memberMenu = (MemberMenu) parent
						.getItemAtPosition(position);
				Intent intent = new Intent(MenuDetailActivity.this,
						MenuDetailDynamic.class);
				intent.putExtra("memberMenu", memberMenu);
				intent.putExtra("sourceActivity", "MenuDetail");
				startActivity(intent);
			}
		});

		iv_menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MenuDetailActivity.this,
						MenuDetailWithStoreInfo.class);
				intent.putExtra("store", store);
				intent.putExtra("menu", menu);
				startActivity(intent);
			}
		});
		order.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (menu.isOrdered) {
					// 取消下单
					orderedMenuList.remove(menu);
					application.getCartMap().get(store.id).menuList
							.remove(menu);
					menu.isOrdered = false;
					showNotOrderdImage();
					showTotalPriceAndOrderCount();
					if (orderedMenuList.size() == 0) {
						cartLayout.setVisibility(View.GONE);
					}
				} else {
					// 下单
					if (!orderedMenuList.contains(menu)) {
						menu.menu_count = 1;
						orderedMenuList.add(menu);
					}
					menu.isOrdered = true;
					showTotalPriceAndOrderCount();
					showOrderedImage();
					if (!"".equals(signing_type) && signing_type.equals("1")) {
						// 在线付商家
						gotoCart.setText(StringUtils.getString(
								MenuDetailActivity.this, R.string.cart));
					}
					if (!"".equals(signing_type) && signing_type.equals("2")) {
						// 到店付商家
						gotoCart.setText(StringUtils.getString(
								MenuDetailActivity.this, R.string.cart));
					}
					if (!"".equals(signing_type) && signing_type.equals("0")) {
						// 非签约商家
						gotoCart.setText(StringUtils.getString(
								MenuDetailActivity.this, R.string.add_dynamic));
					}
					cartLayout.setVisibility(View.VISIBLE);

					if (application.getCartMap() == null) {
						// 购物车为空
						cartMap = new HashMap<String, Cart>();
						Cart cart = new Cart();
						cart.menuList = orderedMenuList;
						cartMap.put(store.id, cart);
						application.setCartMap(cartMap);
					} else {
						// 在此商家中还未点菜，先删除其他商家已点的菜
						for (String storeId : cartMap.keySet()) {
							if (!store.id.equals(storeId)) {
								cartMap.remove(storeId);
							}
						}
						Cart cart = new Cart();
						cart.menuList = orderedMenuList;
						application.getCartMap().put(store.id, cart);
					}
				}
			}
		});

		gotoCart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!StringUtils.isBlank(token)) {
					// 跳转至购物车界面
					// if (!"".equals(signing_type) && signing_type.equals("0"))
					// {
					// // 非签约商家
					// UploadAdapter();
					// } else {
					// 在线付商家及到店付商家
					Intent intent = new Intent(MenuDetailActivity.this,
							CartActivity.class);
					if (!orderedMenuList.contains(menu) && menu.isOrdered) {
						orderedMenuList.add(menu);
					}
					intent.putExtra("orderedMenuList", orderedMenuList);
					intent.putExtra("store", store);
					startActivity(intent);
					// }
				} else {
					// 未登录跳转至用户验证
					if (!orderedMenuList.contains(menu) && menu.isOrdered) {
						orderedMenuList.add(menu);
					}
					Cart cart = new Cart();
					cart.storeInfo = store;
					cart.menuList = orderedMenuList;
					cart.total = tv_totalPrice.getText().toString();
					Intent to_authenti_intent = new Intent(context,
							UserAuthenticationActivity.class);
					to_authenti_intent.putExtra("cart", cart);
					startActivity(to_authenti_intent);
				}
			}
		});

		mNavigationView.setNavigateItemOnClickListener(
				NavigationView.FIRST_RIGHT_TEXT_VIEW, new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (!StringUtils.isBlank(token)) {
							// 分享
							Share share = shareMenuDao.getShareMenu();
							share.content = MessageFormat.format(share.content,
									menu.menu_name);
							share.imagePath = imageLoader
									.getDiskCache()
									.get(menu.menu_image_location
											+ menu.menu_image_name)
									.getAbsolutePath();
							share.imageUrl = menu.menu_image_location
									+ menu.menu_image_name;
							mNavigationView.showShareView(share);
						} else {
							// 登录
							Intent intent = new Intent(MenuDetailActivity.this,
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
							// 栏目导航
							mNavigationView.showCategoryView();
						} else {
							// 注册
							Intent intent = new Intent(MenuDetailActivity.this,
									RegisterActivity.class);
							startActivity(intent);
						}
					}
				});
	}

	private void showTotalPriceAndOrderCount() {
		tv_orderCount.setText(orderedMenuList.size() + "");
		countTotalPrice(orderedMenuList);
		tv_totalPrice.setText(totalPrice + "");
	}

	private void loadMenuRelatedPicture() {
		GetMenuInfoListResp resp = new GetMenuInfoListResp(
				MenuDetailActivity.this, handler, token, menuId,
				Integer.MAX_VALUE + "", "", "");
		resp.asyncInvoke(SUCCESS, FAILURE);
		showProgressDialog(StringUtils.getString(this,
				R.string.loading_menuinfo_img));
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (null != getIntent().getSerializableExtra("store")) {
			store = (Store) getIntent().getSerializableExtra("store");
			signing_type = store.signingType;
		}
		String token = PreferencesUtils.getString(context, "token");
		if (!StringUtils.isBlank(token)) {
			mNavigationView.setNavigateItemText(
					NavigationView.FIRST_RIGHT_TEXT_VIEW, "");
			mNavigationView.setNavigateItemText(
					NavigationView.SECOND_RIGHT_TEXT_VIEW, "");
			mNavigationView.setNavigateItemText(NavigationView.TITLE,
					StringUtils.getString(this, R.string.menu_detail));
			mNavigationView.setNavigateItemBackground(
					NavigationView.FIRST_RIGHT_TEXT_VIEW, R.drawable.share);
			mNavigationView.setNavigateItemBackground(
					NavigationView.SECOND_RIGHT_TEXT_VIEW, R.drawable.navi);
		} else {
			mNavigationView.setNavigateItemText(
					NavigationView.FIRST_RIGHT_TEXT_VIEW, R.string.login);
			mNavigationView.setNavigateItemText(
					NavigationView.SECOND_RIGHT_TEXT_VIEW, R.string.register);
			mNavigationView.setNavigateItemBackground(
					NavigationView.SECOND_RIGHT_TEXT_VIEW, null);
			mNavigationView.setNavigateItemBackground(
					NavigationView.FIRST_RIGHT_TEXT_VIEW, null);
		}
		showCartLayout();
		// 更新评论以及赞的数目
		GetMenuInfoListResp resp = new GetMenuInfoListResp(
				MenuDetailActivity.this, handler, token, menuId,
				Integer.MAX_VALUE + "", "", "");
		resp.asyncInvoke(SUCCESS, FAILURE);
	}

	/** 处理底部购物车布局 */
	private void showCartLayout() {
		if (application.getCartMap() != null) {
			cartMap = (HashMap<String, Cart>) application.getCartMap();
			if (null != store) {
				if (null != cartMap.get(store.id)) {
					// //如果购物车中有此商家菜品
					orderedMenuList = application.getCartMap().get(store.id).menuList;
					tv_orderCount.setText(orderedMenuList.size() + "");
					countTotalPrice(orderedMenuList);
					tv_totalPrice.setText(totalPrice + "");
					if (!"".equals(signing_type) && signing_type.equals("1")) {
						// 在线付商家
						gotoCart.setText(StringUtils.getString(
								MenuDetailActivity.this, R.string.cart));
					}
					if (!"".equals(signing_type) && signing_type.equals("2")) {
						// 到店付商家
						gotoCart.setText(StringUtils.getString(
								MenuDetailActivity.this, R.string.cart));
					}
					if (!"".equals(signing_type) && signing_type.equals("0")) {
						// 非签约商家
						gotoCart.setText(StringUtils.getString(
								MenuDetailActivity.this, R.string.add_dynamic));
					}
					cartLayout.setVisibility(View.VISIBLE);
				} else {
					cartLayout.setVisibility(View.GONE);
					// // 在此商家中还未点菜，先删除其他商家已点的菜
					// for (String storeId : cartMap.keySet()) {
					// if (!store.id.equals(storeId)) {
					// cartMap.remove(storeId);
					// }
					// }
				}
			}
		}
		if (orderedMenuList.size() == 0) {
			cartLayout.setVisibility(View.GONE);
			showNotOrderdImage();
			menu.isOrdered = false;
		}
	}

	/**
	 * 向服务器提交购物车数据
	 */
	private void UploadAdapter() {
		EditCartResp editCartResp = new EditCartResp(context, handler, token,
				"1", memberId, store.id, "1", totalPrice + "",
				makeJSONArray(orderedMenuList), "");
		editCartResp.asyncInvoke(SUBMIT_SUCCESS, SUBMIT_FAILED);
	}

	private JSONArray makeJSONArray(ArrayList<Menu> menuList) {
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < menuList.size(); i++) {
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("menu_id", menuList.get(i).menu_id);
				jsonObject.put("menu_count",
						String.valueOf(menuList.get(i).menu_count));
				jsonArray.put(i, jsonObject);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return jsonArray;
	}

	/**
	 * 生成订单，获取动态ID，去发动态
	 */
	private void UploadAdapter(Cart cart) {
		String mobile = PreferencesUtils.getString(context, "member_phone");
		String email = PreferencesUtils.getString(context, "member_email");
		AddOrderResp addOrderResp = new AddOrderResp(context, handler,
				memberId, mobile, email, cart, token);
		addOrderResp.asyncInvoke(ADD_ORDER_SUCCESS, ADD_ORDER_FAILED);
	}

	/** 显示未下单时的加号 */
	private void showNotOrderdImage() {
		order.setText(getResources().getString(R.string.choose_menu));
		Drawable drawable = context.getResources().getDrawable(
				R.drawable.order_white);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		order.setCompoundDrawables(null, drawable, null, null);
	}

	/** 显示已下单的对勾 */
	private void showOrderedImage() {
		order.setText(getResources().getString(R.string.already_choosed));
		Drawable drawable = getResources().getDrawable(R.drawable.check_white);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		order.setCompoundDrawables(null, drawable, null, null);
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case FAILURE:
				prompt(StringUtils.getString(MenuDetailActivity.this,
						R.string.loading_false));
				closeProgressDialog();
				mPullRefreshScrollView.onRefreshComplete();
				break;
			case SUCCESS:
				memberMenuList = (ArrayList<MemberMenu>) msg.obj;
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						menuFromMemberAdapter.getDataList().clear();
						menuFromMemberAdapter.getDataList().addAll(
								memberMenuList);
						menuFromMemberAdapter.notifyDataSetChanged();
					}
				});
				closeProgressDialog();
				mPullRefreshScrollView.onRefreshComplete();
				break;
			case EDIT_CART_SUCCESS:
				if (msg.obj != null) {
					Cart cart = (Cart) msg.obj;
					cartMap = new HashMap<String, Cart>();
					cartMap.put(store.id, cart);
					application.setCartMap(cartMap);
				}
				break;
			case EDIT_CART_FAILED:
				break;
			case SUBMIT_SUCCESS:
				if (msg.obj != null) {
					Cart mCart = (Cart) msg.obj;
					UploadAdapter(mCart);
				}
				break;
			case SUBMIT_FAILED:

				break;
			case ADD_ORDER_SUCCESS:
				// 生成订单
				orderedMenuList.clear();
				final OrderInfo orderInfo = (OrderInfo) msg.obj;
				Intent intent = new Intent(MenuDetailActivity.this,
						AddDynamicActivity.class);
				intent.putExtra("dynamic_id", orderInfo.dynamicId);
				startActivity(intent);
				finish();
				break;
			case ADD_ORDER_FAILED:
				mPullRefreshScrollView.onRefreshComplete();
				break;
			case GET_MENU_SUCCESS:
				Map<String, Object> map = (Map) msg.obj;
				menu = (Menu) map.get("menu");
				store = (Store) map.get("store");
				signing_type = store.signingType;
				imageLoader.displayImage(menu.menu_image_location
						+ menu.menu_image_name, iv_menu);
				menu_name.setText(menu.menu_name);
				if (orderedMenuList.contains(menu)) {
					showOrderedImage();
				} else {
					showNotOrderdImage();
				}
				if (!StringUtils.isBlank(menu.menu_coupon_price)) {
					// 有优惠，显示优惠价格惠字
					is_sale.setVisibility(View.VISIBLE);
					float price = Float.valueOf(menu.menu_coupon_price);
					menu_price.setText(NumberFormatUtils.format(price) + "/");
				} else {
					// 无优惠价格，显示原价
					is_sale.setVisibility(View.INVISIBLE);
					float price = Float.valueOf(menu.menu_price);
					menu_price.setText(NumberFormatUtils.format(price) + "/");
				}
				showCartLayout();
				if (!StringUtils.isBlank(menu.menu_unit_id)) {
					menuUnitDao = new MenuUnitDao(MenuDetailActivity.this);
					MenuUnit unit = menuUnitDao
							.getMenuUnitById(menu.menu_unit_id);
					menu_unit.setText(unit.menu_unit_title);
				}
				menu_count.setText(menu.menu_count + "");
				mPullRefreshScrollView.onRefreshComplete();
				break;
			case GET_MENU_FAILED:
				mPullRefreshScrollView.onRefreshComplete();
				break;
			}
		}
	};

	@Override
	protected void onDestroy() {
		// 提交至服务器
		String operatorStatus = "";
		if (orderedMenuList != null && orderedMenuList.size() > 0) {
			operatorStatus = "1";
		} else {
			operatorStatus = "2";
		}
		if (!StringUtils.isBlank(token)) {
			EditCartResp editCartResp = new EditCartResp(context, handler,
					token, operatorStatus, memberId, store.id, "0", totalPrice
							+ "", makeJSONArray(orderedMenuList), "");
			editCartResp.asyncInvoke(EDIT_CART_SUCCESS, EDIT_CART_FAILED);
		}
		super.onDestroy();
	}
}
