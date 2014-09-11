package com.huassit.imenu.android.ui;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.MyApplication;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.adapter.MenuAdapter;
import com.huassit.imenu.android.adapter.MenuCategoryAdapter;
import com.huassit.imenu.android.biz.AddOrderResp;
import com.huassit.imenu.android.biz.EditCartResp;
import com.huassit.imenu.android.biz.GetMenuListAndCategoryListResp;
import com.huassit.imenu.android.model.Cart;
import com.huassit.imenu.android.model.Menu;
import com.huassit.imenu.android.model.MenuAndCategory;
import com.huassit.imenu.android.model.MenuCategory;
import com.huassit.imenu.android.model.OrderInfo;
import com.huassit.imenu.android.model.Store;
import com.huassit.imenu.android.ui.cart.CartActivity;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.view.NavigationView;

public class MenuListActivity extends BaseActivity {

	private ListView categoryListView;
	private ListView menuListView;
	private MenuCategoryAdapter cateAdapter;
	private MenuAdapter menuAdapter;
	private ArrayList<Menu> menuList = new ArrayList<Menu>();
	private ArrayList<Menu> partOfMenuList = new ArrayList<Menu>();
	private ArrayList<MenuCategory> cateList = new ArrayList<MenuCategory>();
	private Store store;
	private NavigationView mNavigationView;
	private LinearLayout cartLayout;
	private TextView tv_orderCount;
	private TextView tv_totalPrice;
	private TextView gotoCart;
	private float totalPrice;
	private RelativeLayout all;
	private ImageView allChoosen;
	private ArrayList<Menu> orderedMenuList;
	/** 标诗 */
	public static boolean isTrue = false;
	/** 店铺类型,签约类型(1-在线付|2-到店付|0-未签约) */
	private String signing_type;
	/** 促销类型(1-有促销|0-无促销) */
	private String coupon_type;
	/** 生成订单成功标石 */
	private static final int ADD_ORDER_SUCCESS = 400;
	/** 生成订单失败标石 */
	private static final int ADD_ORDER_FAILED = 401;
	/** 提交购物车数据成功标石 */
	private static final int SUBMIT_SUCCESS = 300;
	/** 提交购物车数据失败标石 */
	private static final int SUBMIT_FAILED = 301;
	/** 编辑购物车成功 */
	private static final int EDIT_CART_SUCCESS = 200;
	/** 编辑购物车失败 */
	private static final int EDIT_CART_FAILED = 201;
	private String memberId;
	private MyApplication application;

	private TextView tv_all;

	@Override
	public int getContentView() {
		return R.layout.menu_choose_list;
	}

	@Override
	public void initView() {
		store = (Store) getIntent().getSerializableExtra("store");
		application = (MyApplication) context.getApplicationContext();
		memberId = PreferencesUtils.getString(context, "member_id");
		mNavigationView = (NavigationView) findViewById(R.id.navigationView);
		mNavigationView.setNavigateItemVisibility(
				NavigationView.LEFT_IMAGE_VIEW, View.GONE);
		mNavigationView.setNavigateItemBackground(
				NavigationView.LEFT_TEXT_VIEW, R.drawable.back);
		cartLayout = (LinearLayout) findViewById(R.id.cartLayout);
		all = (RelativeLayout) findViewById(R.id.all);
		tv_all = (TextView) findViewById(R.id.tv_all);
		all.setBackgroundColor(Color.parseColor(PreferencesUtils.getString(
				context, "ColorValue")));
		tv_all.setTextColor(Color.parseColor("#FFFFFF"));
		allChoosen = (ImageView) findViewById(R.id.choosen);
		tv_orderCount = (TextView) findViewById(R.id.orderCount);
		tv_totalPrice = (TextView) findViewById(R.id.totalPrice);
		gotoCart = (TextView) findViewById(R.id.gotoCart);
		categoryListView = (ListView) findViewById(R.id.categoryList);
		menuListView = (ListView) findViewById(R.id.menuList);
		cateAdapter = new MenuCategoryAdapter(MenuListActivity.this);
		categoryListView.setAdapter(cateAdapter);
		orderedMenuList = new ArrayList<Menu>();
		menuAdapter = new MenuAdapter(MenuListActivity.this, store);
		menuListView.setAdapter(menuAdapter);
		categoryListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				tv_all.setTextColor(Color.parseColor("#555555"));
				cateAdapter.currentIndex = position;
				cateAdapter.notifyDataSetChanged();
				all.setBackgroundColor(getResources().getColor(R.color.gray));
				allChoosen.setVisibility(View.GONE);
				partOfMenuList.clear();
				MenuCategory cate = (MenuCategory) parent
						.getItemAtPosition(position);
				String[] menuIds = cate.getMenu_ids().split("\\|");
				for (int j = 0; j < menuIds.length; j++) {
					for (int k = 0; k < menuList.size(); k++) {
						String menu_id = menuList.get(k).menu_id;
						if (menuIds[j].equals(menu_id)) {
							partOfMenuList.add(menuList.get(k));
						}
					}
				}
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						menuAdapter.getDataList().clear();
						menuAdapter.getDataList().addAll(partOfMenuList);
						menuAdapter.notifyDataSetChanged();
					}
				});
			}
		});
		all.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cateAdapter.currentIndex = -1;
				cateAdapter.notifyDataSetChanged();
				allChoosen.setVisibility(View.VISIBLE);
				tv_all.setTextColor(Color.parseColor("#FFFFFF"));
				all.setBackgroundColor(Color.parseColor(PreferencesUtils
						.getString(context, "ColorValue")));
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						menuAdapter.getDataList().clear();
						menuAdapter.getDataList().addAll(menuList);
						menuAdapter.notifyDataSetChanged();
					}
				});
			}
		});
		menuListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Menu menu = (Menu) parent.getItemAtPosition(position);
				Intent intent = new Intent(MenuListActivity.this,
						MenuDetailActivity.class);
				intent.putExtra("store", store);
				intent.putExtra("menu", menu);
				startActivity(intent);
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
							Intent intent = new Intent(MenuListActivity.this,
									RegisterActivity.class);
							startActivity(intent);
						}
					}
				});
		mNavigationView.setNavigateItemOnClickListener(
				NavigationView.LEFT_TEXT_VIEW, new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		mNavigationView.setNavigateItemOnClickListener(
				NavigationView.FIRST_RIGHT_TEXT_VIEW, new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (!StringUtils.isBlank(token)) {
							// 地图
							Intent intent = new Intent(MenuListActivity.this,
									MapActivity.class);
							startActivity(intent);
						} else {
							// 登录
							Intent intent = new Intent(MenuListActivity.this,
									LoginActivity.class);
							startActivity(intent);
						}
					}
				});
		gotoCart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!StringUtils.isBlank(token)) {
					for (int i = 0; i < menuList.size(); i++) {
						if (menuList.get(i).isOrdered) {
							orderedMenuList.add(menuList.get(i));
						}
					}
//					if (!"".equals(signing_type) && signing_type.equals("0")) {
//						// 非签约商家
//						UploadAdapter();
//					} else {
						// 在线付商家及到店付商家
						Intent intent = new Intent(MenuListActivity.this,
								CartActivity.class);
						intent.putExtra("orderedMenuList", orderedMenuList);
						intent.putExtra("store", store);
						startActivity(intent);
						finish();
//					}
				} else {
					// 未登录跳转至用户验证
					for (int i = 0; i < menuList.size(); i++) {
						if (menuList.get(i).isOrdered) {
							orderedMenuList.add(menuList.get(i));
						}
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
	}

	@Override
	public void initData() {
		GetMenuListAndCategoryListResp menuResp = new GetMenuListAndCategoryListResp(
				MenuListActivity.this, handler, store.id);
		menuResp.asyncInvoke(SUCCESS, FAILURE);
		showProgressDialog(StringUtils.getString(this, R.string.loading));
	}

	/** 生成订单，获取动态ID，去发动态 */
	private void UploadAdapter(Cart cart) {
		String mobile = PreferencesUtils.getString(context, "member_phone");
		String email = PreferencesUtils.getString(context, "member_email");
		AddOrderResp addOrderResp = new AddOrderResp(context, handler,
				memberId, mobile, email, cart, token);
		addOrderResp.asyncInvoke(ADD_ORDER_SUCCESS, ADD_ORDER_FAILED);
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case FAILURE:
				prompt(StringUtils.getString(MenuListActivity.this,
						R.string.loading_menu_list_false));
				break;

			case SUCCESS:
				closeProgressDialog();
				MenuAndCategory menuObjectList = (MenuAndCategory) msg.obj;
				signing_type = menuObjectList.mStore.signingType;
				if (menuObjectList.menu_list != null
						&& menuObjectList.menu_list.size() > 0) {
					for (int i = 0; i < menuObjectList.menu_list.size(); i++) {
						Menu menu = (Menu) menuObjectList.menu_list.get(i);
						menuList.add(menu);
					}
					/** 获取本地购物车数据 */
					if (application.getAppCart(store.id) != null) {
						Cart mCart = application.getAppCart(store.id);
						ArrayList<Menu> orderedMenuList = mCart.menuList;
						for (int i = 0; i < menuList.size(); i++) {
							Menu menu = menuList.get(i);
							for (int j = 0; j < orderedMenuList.size(); j++) {
								if (menu.menu_id
										.equals(orderedMenuList.get(j).menu_id)) {
									// 下过单的标记为TRUE
									menu.isOrdered = true;
									menu.menu_count = orderedMenuList.get(j).menu_count;
								}
							}
						}
					}
					showOrHideLayout();
				}
				if (menuObjectList.menu_category_list != null
						&& menuObjectList.menu_category_list.size() > 0) {
					for (int i = 0; i < menuObjectList.menu_category_list
							.size(); i++) {
						MenuCategory cate = (MenuCategory) menuObjectList.menu_category_list
								.get(i);
						cateList.add(cate);
					}
				}
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						cateAdapter.getDataList().clear();
						cateAdapter.getDataList().addAll(cateList);
						cateAdapter.notifyDataSetChanged();
						menuAdapter.getDataList().clear();
						menuAdapter.getDataList().addAll(menuList);
						menuAdapter.notifyDataSetChanged();
					}
				});
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
				Intent intent = new Intent(MenuListActivity.this,
						AddDynamicActivity.class);
				intent.putExtra("dynamic_id", orderInfo.dynamicId);
				startActivity(intent);
				finish();
				break;
			case ADD_ORDER_FAILED:

				break;
			}
		}
	};

	/** 向服务器提交购物车数据 */
	private void UploadAdapter() {
		EditCartResp editCartResp = new EditCartResp(context, handler, token,
				"1", memberId, store.id, "0", totalPrice + "",
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

	// 计算菜单总价以及菜单数量
	private void countTotalPriceAndShowCount(ArrayList<Menu> menuList) {
		totalPrice = 0;
		for (Menu menu : menuList) {
			totalPrice += Float.valueOf(menu.menu_price) * menu.menu_count;
		}
		tv_orderCount.setText(menuList.size() + "");
		tv_totalPrice.setText(totalPrice + "");
	}

	private void showCartLayout(ArrayList<Menu> orderedMenu) {
		if (orderedMenu.size() > 0) {
			cartLayout.setVisibility(View.VISIBLE);
			if (!"".equals(signing_type) && signing_type.equals("1")) {
				// 在线付商家
				gotoCart.setText(StringUtils.getString(this, R.string.cart));
			}
			if (!"".equals(signing_type) && signing_type.equals("2")) {
				// 到店付商家
				gotoCart.setText(StringUtils.getString(this, R.string.cart));
			}
			if (!"".equals(signing_type) && signing_type.equals("0")) {
				// 非签约商家
				gotoCart.setText(StringUtils.getString(this,
						R.string.add_dynamic));
			}
		} else {
			cartLayout.setVisibility(View.GONE);
		}
	}

	/** 根据本地数据显示或隐藏购物车底部条 */
	public void showOrHideLayout() {
		if (application.getAppCart(store.id) != null) {
			Cart cart = application.getAppCart(store.id);
			if (null != cart) {
				countTotalPriceAndShowCount(cart.menuList);
				showCartLayout(cart.menuList);
			}
		} else {
			cartLayout.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!StringUtils.isBlank(token)) {
			mNavigationView.setNavigateItemText(
					NavigationView.FIRST_RIGHT_TEXT_VIEW, "");
			mNavigationView.setNavigateItemText(
					NavigationView.SECOND_RIGHT_TEXT_VIEW, "");
			mNavigationView.setNavigateItemText(NavigationView.TITLE,
					store.name);
			mNavigationView.setNavigateItemBackground(
					NavigationView.SECOND_RIGHT_TEXT_VIEW, R.drawable.navi);
		} else {
			mNavigationView.setNavigateItemText(
					NavigationView.FIRST_RIGHT_TEXT_VIEW, R.string.login);
			mNavigationView.setNavigateItemText(
					NavigationView.SECOND_RIGHT_TEXT_VIEW, R.string.register);
			mNavigationView.setNavigateItemBackground(
					NavigationView.SECOND_RIGHT_TEXT_VIEW, null);
		}
	}

	@Override
	protected void onDestroy() {
		// 提交至服务器
		String operatorStatus = "";
		if (orderedMenuList != null && orderedMenuList.size() > 0) {
			operatorStatus = "1";
		} else {
			operatorStatus = "2";
		}
		if(!StringUtils.isBlank(token)){
			EditCartResp editCartResp = new EditCartResp(context, handler, token,
					operatorStatus, memberId, store.id, "0", totalPrice + "",
					makeJSONArray(orderedMenuList), "");
			editCartResp.asyncInvoke(EDIT_CART_SUCCESS, EDIT_CART_FAILED);
		}
		super.onDestroy();
	}
}
