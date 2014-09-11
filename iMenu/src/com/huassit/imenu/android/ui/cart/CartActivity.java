package com.huassit.imenu.android.ui.cart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.MyApplication;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.adapter.OrderedMenuAdapter;
import com.huassit.imenu.android.biz.AddOrderResp;
import com.huassit.imenu.android.biz.EditCartResp;
import com.huassit.imenu.android.biz.GetCartResp;
import com.huassit.imenu.android.model.Cart;
import com.huassit.imenu.android.model.Menu;
import com.huassit.imenu.android.model.OrderInfo;
import com.huassit.imenu.android.model.Store;
import com.huassit.imenu.android.ui.AddDynamicActivity;
import com.huassit.imenu.android.ui.MenuListActivity;
import com.huassit.imenu.android.ui.StoreDetailActivity;
import com.huassit.imenu.android.ui.pay.OnLinePayActivity;
import com.huassit.imenu.android.util.NumberFormatUtils;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.view.CartDialog;
import com.huassit.imenu.android.view.NavigationView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 购物车界面
 * 
 * @author shang_guan
 * 
 */
public class CartActivity extends BaseActivity implements OnClickListener {

	private static final int EDIT_ONLY_SUCCESS = 300;
	private static final int EDIT_ONLY_FAILED = 301;
	private static final int ADD_ORDER_SUCCESS = 200;
	private static final int ADD_ORDER_FAILED = 201;
	/** 获取优惠信息是编辑购物车成功标石 */
	private static final int GET_CART_SUCCESS = 400;
	/** 获取优惠信息是编辑购物车失败标石 */
	private static final int GET_CART_FAILED = 401;
	/** 选择优惠信息后刷新购物车数据成功标石 */
	private static final int REFESH_CART_SUCCESS = 500;
	/** 选择优惠信息后刷新购物车数据失败标石 */
	private static final int REFESH_CART_FAILED = 501;
	/** 提交购物车数据成功标石 */
	private static final int EDIT_CART_SUCCESS = 600;
	private static final String OPERATE_STATUS_EDIT = "1";
	private static final String OPERATE_STATUS_CLEAR = "2";
	private ImageView mStore_image;
	private TextView mStore_name;
	private ImageView mSee_store_info;
	private TextView tv_average_price;
	private ImageView reducePeople;
	private TextView tv_people;
	private ImageView addPeople;
	/** 菜品优惠计算按钮 */
	private TextView discount_image;
	// 此订单中点了几个菜
	private TextView menu_count;
	/** 原价 */
	// private TextView mDiscounts;
	private TextView tv_totalPrice;
	// private TextView mDiscount_image;
	private TextView mAdd_dishes;// 加菜
	private TextView addOrder;// 下单
	private TextView mClear;// 清空
	private NavigationView mNavigationView;
	private ImageLoader imageLoader;
	// private DisplayImageOptions options;
	private int peopleCountInt = 0;// 人数
	private float totalPrice = 0;// 总钱数
	private float averagePrice = 0;
	private ListView menuListView;
	private OrderedMenuAdapter orderedMenuAdapter;
	private Store store;
	private ArrayList<Menu> orderedMenuList = new ArrayList<Menu>();
	private MyApplication application;
	private String memberId;
	private Map<String, Cart> cartMap = new HashMap<String, Cart>();
	private String operateStatus;
	/** 店铺类型,签约类型(1-在线付|2-到店付|0-未签约) */
	private String signing_type;
	/** 促销类型(1-有促销|0-无促销) */
	private String coupon_type;
	private DisplayImageOptions options;
	/** 标石是否去下单或支付或发动态，则需清空购物车 */
	private boolean isAddOrder = false;
	/** 显示菜品优惠控件 */
	private TextView menu_coupon_tv;
	/** 显示菜品促销的优惠控件 */
	private TextView coupon_type_tv;

	@Override
	public int getContentView() {
		return R.layout.cart;
	}

	public void initView() {
		application = (MyApplication) getApplicationContext();
		options = MyApplication.getDisplayImageOptions(context, 40);
		memberId = PreferencesUtils.getString(context, "member_id");
		imageLoader = ImageLoader.getInstance();
		mNavigationView = (NavigationView) findViewById(R.id.navigationView);
		mNavigationView.setNavigateItemVisibility(
				NavigationView.LEFT_IMAGE_VIEW, View.GONE);
		mNavigationView.setNavigateItemVisibility(NavigationView.TITLE,
				View.GONE);
		mNavigationView.setNavigateItemText(NavigationView.LEFT_TEXT_VIEW,
				R.string.cart);
		mNavigationView.setNavigateItemBackground(
				NavigationView.SECOND_RIGHT_TEXT_VIEW, R.drawable.close);
		menuListView = (ListView) findViewById(R.id.dishes_list);
		orderedMenuAdapter = new OrderedMenuAdapter(CartActivity.this,
				R.layout.ordered_menu_list_item, 0);
		menuListView.setAdapter(orderedMenuAdapter);
		mStore_image = (ImageView) findViewById(R.id.order_image);
		mStore_name = (TextView) findViewById(R.id.order_text);
		mSee_store_info = (ImageView) findViewById(R.id.see_store_info);
		tv_average_price = (TextView) findViewById(R.id.average_price);
		reducePeople = (ImageView) findViewById(R.id.reducePeople);
		tv_people = (TextView) findViewById(R.id.people);
		addPeople = (ImageView) findViewById(R.id.addPeople);
		menu_count = (TextView) findViewById(R.id.menu_count);
		// mDiscounts = (TextView) findViewById(R.id.discounts);
		// mDiscounts.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		tv_totalPrice = (TextView) findViewById(R.id.cart_totalPrice);
		// mDiscount_image = (TextView) findViewById(R.id.discount_image);
		mAdd_dishes = (TextView) findViewById(R.id.add_dishes);
		addOrder = (TextView) findViewById(R.id.add_order);
		mClear = (TextView) findViewById(R.id.clear);
		discount_image = (TextView) findViewById(R.id.discount_image);
		discount_image.setOnClickListener(this);
		discount_image.setText(StringUtils.getString(context,
				R.string.choose_forsale));
		menu_coupon_tv = (TextView) findViewById(R.id.menu_coupon_tv);
		coupon_type_tv = (TextView) findViewById(R.id.coupon_type);
		// mDiscounts = (TextView) findViewById(R.id.discounts);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onResume() {
		store = (Store) getIntent().getSerializableExtra("store");
		signing_type = store.signingType;
		discount_image.setVisibility(View.GONE);
		if (store.couponType.equals("1")) {
			discount_image.setVisibility(View.VISIBLE);
		}
		cartMap = application.getCartMap();
		orderedMenuList = (ArrayList<Menu>) getIntent().getSerializableExtra(
				"orderedMenuList");
		if (cartMap != null) {
			if (cartMap.get(store.id) != null) {
				if (cartMap.get(store.id).menuList != null) {
					orderedMenuList = cartMap.get(store.id).menuList;
					// 从店长推荐获取套餐总价
					if (getIntent().getExtras().getString("total_price") != null
							&& !"".equals(getIntent().getExtras().getString(
									"total_price"))) {
						totalPrice = Float.parseFloat(getIntent().getExtras()
								.getString("total_price"));
					}
					if (null != cartMap.get(store.id).storeInfo
							&& !"".equals(cartMap.get(store.id).storeInfo.signingType)) {
						signing_type = cartMap.get(store.id).storeInfo.signingType;
					}
				}
			}
		}
		if (!"".equals(signing_type) && signing_type.equals("1")) {
			// 在线付商家
			addOrder.setText(StringUtils.getString(context, R.string.pay));
		}
		if (!"".equals(signing_type) && signing_type.equals("2")) {
			// 到店付商家
			addOrder.setText(StringUtils.getString(context, R.string.confirm));
		}
		if (!"".equals(signing_type) && signing_type.equals("0")) {
			// 非签约商家
			addOrder.setText(StringUtils.getString(context,
					R.string.publish_dynamic));
		}
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				orderedMenuAdapter.getDataList().clear();
				orderedMenuAdapter.getDataList().addAll(orderedMenuList);
				orderedMenuAdapter.notifyDataSetChanged();
			}
		});
		// 基础数据：店名，图片，单品数量
		imageLoader.displayImage(store.logoLocation + store.logoName,
				mStore_image, options);
		mStore_name.setText(store.name);
		menu_count.setText(orderedMenuList.size()
				+ StringUtils.getString(context, R.string.kind));
		// 从本地取出上次存储的人数信息
		if (cartMap.get(store.id).people == null
				|| cartMap.get(store.id).people.equals("")) {
			peopleCountInt = 0;
		} else {
			peopleCountInt = Integer.valueOf(cartMap.get(store.id).people);
		}
		// 菜品总价，均价，人数
		countTotalPrice(0);
		tv_totalPrice.setText("￥" + totalPrice + "");
		averagePrice = totalPrice / peopleCountInt;
		if (peopleCountInt == 0) {
			averagePrice = 0;
			tv_average_price.setText("￥" + averagePrice + "/"
					+ StringUtils.getString(context, R.string.people));
		} else {
			tv_average_price.setText("￥"
					+ NumberFormatUtils.format(averagePrice) + "/"
					+ StringUtils.getString(context, R.string.people));
		}
		tv_people.setText(peopleCountInt
				+ StringUtils.getString(context, R.string.people));
		// mDiscounts.setText(original_total + "");
		menu_coupon_tv.setVisibility(View.INVISIBLE);
		if (orderedMenuList != null && orderedMenuList.size() > 0) {
			boolean haveCoupon = false;
			for (int i = 0; i < orderedMenuList.size(); i++) {
				if (null != orderedMenuList.get(i).menu_coupon_price
						&& !"".equals(orderedMenuList.get(i).menu_coupon_price)) {
					haveCoupon = true;
				}
			}
			if (haveCoupon) {
				menu_coupon_tv.setVisibility(View.VISIBLE);
			}
		}
		UploadAdapter("", "");
		super.onResume();
	}

	@Override
	public void initData() {
		mNavigationView.setNavigateItemOnClickListener(
				NavigationView.SECOND_RIGHT_TEXT_VIEW, new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		mSee_store_info.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CartActivity.this,
						StoreDetailActivity.class);
				intent.putExtra("store", store);
				startActivity(intent);

			}
		});
		reducePeople.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (peopleCountInt > 0) {
					peopleCountInt--;
				}
				if (peopleCountInt != 0) {
					averagePrice = totalPrice / peopleCountInt;
					tv_average_price.setText("￥"
							+ NumberFormatUtils.format(averagePrice) + "/"
							+ StringUtils.getString(context, R.string.people));
				} else {
					averagePrice = 0;
					tv_average_price.setText("￥" + averagePrice + "/"
							+ StringUtils.getString(context, R.string.people));
				}
				tv_people.setText(peopleCountInt
						+ StringUtils.getString(context, R.string.people));
			}
		});
		addPeople.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				peopleCountInt++;
				tv_people.setText(peopleCountInt
						+ StringUtils.getString(context, R.string.people));
				averagePrice = totalPrice / peopleCountInt;
				tv_average_price.setText("￥"
						+ NumberFormatUtils.format(averagePrice) + "/"
						+ StringUtils.getString(context, R.string.people));
			}
		});
		mAdd_dishes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 数据保存至本地
				Map<String, Cart> cartMap = application.getCartMap();
				Cart cart = cartMap.get(store.id);
				cart.people = peopleCountInt + "";
				cart.menuList = orderedMenuList;
				application.setCartMap(cartMap);

				Intent intent = new Intent(CartActivity.this,
						MenuListActivity.class);
				intent.putExtra("store", store);
				intent.putExtra("orderedMenuList", orderedMenuList);
				startActivity(intent);
				finish();
			}
		});
		mClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(context)
						.setMessage(
								StringUtils.getString(context,
										R.string.sure_to_clear_cart))
						.setPositiveButton(
								StringUtils
										.getString(context, R.string.confirm),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										orderedMenuList.clear();
										finish();
									}
								})
						.setNegativeButton(
								StringUtils.getString(context, R.string.cancel),
								null).create().show();
			}
		});

		addOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isAddOrder = true;
				if (peopleCountInt == 0) {
					mNavigationView
							.showErrorMessage(getString(R.string.choose_people));
				} else {
					// 先向服务器提交购物车数据
					EditCartResp editCartResp = new EditCartResp(context,
							handler, token, OPERATE_STATUS_EDIT, memberId,
							store.id, peopleCountInt + "", totalPrice + "",
							makeJSONArray(orderedMenuList), "");
					editCartResp.asyncInvoke(SUCCESS, FAILURE);
				}
			}
		});
	}

	/** 封装上传菜单数据 */
	private JSONArray makeJSONArray(ArrayList<Menu> menuList) {
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < menuList.size(); i++) {
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("menu_id", menuList.get(i).menu_id);
				jsonObject.put("menu_count", menuList.get(i).menu_count + "");
				jsonArray.put(i, jsonObject);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return jsonArray;
	}

	/** 某菜品数量的加减导致总价以及均价的变化 */
	public void countTotalPrice(int position) {
		totalPrice = 0;
		List<Menu> orderedMenu = orderedMenuAdapter.getDataList();
		for (int i = 0; i < orderedMenu.size(); i++) {
			if (!StringUtils.isBlank(orderedMenuList.get(i).menu_coupon_price)) {
				totalPrice += Float
						.valueOf(orderedMenu.get(i).menu_coupon_price)
						* Float.valueOf(orderedMenu.get(i).menu_count);
			} else {
				totalPrice += Float.valueOf(orderedMenu.get(i).menu_price)
						* Float.valueOf(orderedMenu.get(i).menu_count);
			}
		}
		tv_totalPrice.setText("￥" + totalPrice + "");
		averagePrice = totalPrice / peopleCountInt;
		if (peopleCountInt == 0) {
			averagePrice = 0;
			tv_average_price.setText("￥" + averagePrice + "/"
					+ StringUtils.getString(context, R.string.people));
		} else {
			tv_average_price.setText("￥"
					+ NumberFormatUtils.format(averagePrice) + "/"
					+ StringUtils.getString(context, R.string.people));
		}
		if (totalPrice == 0) {
			finish();
		}
		if (orderedMenuList != null && orderedMenuList.size() > 0) {
			boolean haveCoupon = false;
			for (int i = 0; i < orderedMenuList.size(); i++) {
				if (null != orderedMenuList.get(i).menu_coupon_price
						&& !"".equals(orderedMenuList.get(i).menu_coupon_price)) {
					haveCoupon = true;
				}
			}
			if (haveCoupon) {
				menu_coupon_tv.setVisibility(View.VISIBLE);
			} else {
				menu_coupon_tv.setVisibility(View.INVISIBLE);
			}
		}
	}

	/** 初始计算所点菜品总价 */
	public void countTotalPrice() {
		List<Menu> orderedMenu = orderedMenuAdapter.getDataList();
		for (int i = 0; i < orderedMenu.size(); i++) {
			totalPrice += Float.valueOf(orderedMenu.get(i).menu_price);
		}
	}

	public void removeItem(int position) {
		orderedMenuList.remove(position);
		// 保存至本地
		Map<String, Cart> cartMap = application.getCartMap();
		Cart cart = cartMap.get(store.id);
		cart.people = peopleCountInt + "";
		cart.menuList = orderedMenuList;
		application.setCartMap(cartMap);

		runOnUiThread(new Runnable() {
			public void run() {
				orderedMenuAdapter.getDataList().clear();
				orderedMenuAdapter.getDataList().addAll(orderedMenuList);
				orderedMenuAdapter.notifyDataSetChanged();
			}
		});
		menu_count.setText(orderedMenuList.size()
				+ StringUtils.getString(context, R.string.kind));
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS:
				Cart cart = (Cart) msg.obj;
				tv_totalPrice.setText("￥" + cart.total);
				// if (!StringUtils.isBlank(cart.originalTotal)) {
				// mDiscounts.setText(cart.originalTotal);
				// }
				// original_total = Float.parseFloat(cart.originalTotal);
				String mobile = PreferencesUtils.getString(context,
						"member_phone");
				String email = PreferencesUtils.getString(context,
						"member_email");
				AddOrderResp addOrderResp = new AddOrderResp(context, handler,
						memberId, mobile, email, cart, token);
				addOrderResp.asyncInvoke(ADD_ORDER_SUCCESS, ADD_ORDER_FAILED);
				break;
			case FAILURE:
				break;
			case ADD_ORDER_SUCCESS:
				final OrderInfo orderInfo = (OrderInfo) msg.obj;
				if (!"".equals(signing_type) && signing_type.equals("1")) {
					// 在线付商家-支付
					Intent intent = new Intent(context, OnLinePayActivity.class);
					intent.putExtra("orderInfo", orderInfo);
					startActivity(intent);
				}
				if (!"".equals(signing_type) && signing_type.equals("2")) {
					// 到店付商家-下单
					Intent intent = new Intent(context, SuccesActivity.class);
					intent.putExtra("orderInfo", orderInfo);
					intent.putExtra("whatSuccess", 2);// 1:支付成功-2:下单成功标石
					startActivity(intent);
				}
				if (!"".equals(signing_type) && signing_type.equals("0")) {
					// 非签约商家-发动态
					Intent intent = new Intent(CartActivity.this,
							AddDynamicActivity.class);
					intent.putExtra("dynamic_id", orderInfo.dynamicId);
					startActivity(intent);
				}
				finish();
				break;
			case ADD_ORDER_FAILED:
				if (!"".equals(signing_type) && signing_type.equals("1")) {
					// 在线付商家-支付

				}
				if (!"".equals(signing_type) && signing_type.equals("2")) {
					// 到店付商家-下单
					Intent intent = new Intent();
					intent = new Intent(context, FailureActivity.class);
					startActivity(intent);
				}
				if (!"".equals(signing_type) && signing_type.equals("0")) {
					// 非签约商家-发动态

				}
				break;
			case GET_CART_SUCCESS:
				if (msg.obj != null) {
					Cart cart1 = (Cart) msg.obj;
					CartDialog mCartDialog = new CartDialog(CartActivity.this,
							cart1.couponList);
					mCartDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					mCartDialog.show();
				}
				break;
			case GET_CART_FAILED:

				break;
			case REFESH_CART_SUCCESS:
				Cart mCart = (Cart) msg.obj;
				totalPrice = Float.parseFloat(mCart.total);
				peopleCountInt = Integer.parseInt(mCart.people);
				averagePrice = totalPrice / peopleCountInt;
				if (peopleCountInt == 0) {
					averagePrice = 0;
					tv_average_price.setText("￥" + averagePrice + "/"
							+ StringUtils.getString(context, R.string.people));
				} else {
					tv_average_price.setText("￥"
							+ NumberFormatUtils.format(averagePrice) + "/"
							+ StringUtils.getString(context, R.string.people));
				}
				tv_totalPrice.setText("￥" + totalPrice);
				if (mCart.storeInfo.couponType.equals("1")) {
					discount_image.setVisibility(View.VISIBLE);
				} else {
					discount_image.setVisibility(View.INVISIBLE);
				}
				break;
			case REFESH_CART_FAILED:

				break;
			case EDIT_CART_SUCCESS:
				UploadAdapter();
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 选择优惠信息后更新数据u
	 * */
	public void UploadAdapter(String couponId, String couponTitle) {
		EditCartResp editCartResp = new EditCartResp(context, handler, token,
				OPERATE_STATUS_EDIT, memberId, store.id, peopleCountInt + "",
				totalPrice + "", makeJSONArray(orderedMenuList), couponId);
		editCartResp.asyncInvoke(REFESH_CART_SUCCESS, REFESH_CART_FAILED);
		discount_image.setText(couponTitle);
		coupon_type_tv.setText(couponTitle);
		coupon_type_tv.setVisibility(View.VISIBLE);
		if (couponTitle.equals("")) {
			discount_image.setText("选择优惠");
			coupon_type_tv.setVisibility(View.INVISIBLE);
		}
	}

	/** 获取促销时先获取购物车数据 */
	private void UploadAdapter() {
		GetCartResp getCartResp = new GetCartResp(context, handler, token,
				memberId);
		getCartResp.asyncInvoke(GET_CART_SUCCESS, GET_CART_FAILED);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.discount_image:
			// 优惠按钮
			EditCartResp editCartResp = new EditCartResp(context, handler,
					token, OPERATE_STATUS_EDIT, memberId, store.id,
					peopleCountInt + "", totalPrice + "",
					makeJSONArray(orderedMenuList), "");
			editCartResp.asyncInvoke(EDIT_CART_SUCCESS, FAILURE);
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// 退出购物车页面时 将数据保存至本地并提交至服务器
		// 保存至本地
		Map<String, Cart> cartMap = application.getCartMap();
		Cart cart = cartMap.get(store.id);
		cart.people = peopleCountInt + "";
		cart.menuList = orderedMenuList;
		application.setCartMap(cartMap);
		if (orderedMenuList == null || orderedMenuList.size() == 0) {
			operateStatus = OPERATE_STATUS_CLEAR;
		} else {
			operateStatus = OPERATE_STATUS_EDIT;
		}
		if (isAddOrder) {
			// 生成订单成功，清空购物车
			application.DeleteCartMap();
			orderedMenuList.clear();
			operateStatus = "2";
		}
		// 提交至服务器
		EditCartResp editCartResp = new EditCartResp(context, handler, token,
				operateStatus, memberId, store.id, peopleCountInt + "",
				totalPrice + "", makeJSONArray(orderedMenuList), "");
		editCartResp.asyncInvoke(EDIT_ONLY_SUCCESS, EDIT_ONLY_FAILED);
		super.onDestroy();
	}
}
