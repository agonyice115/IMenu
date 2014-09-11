package com.huassit.imenu.android.ui;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.MyApplication;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.adapter.RecommandMenuListAdapter;
import com.huassit.imenu.android.adapter.RecommandViewPagerAdapter;
import com.huassit.imenu.android.biz.GetRecommendMenuListResp;
import com.huassit.imenu.android.model.Cart;
import com.huassit.imenu.android.model.Menu;
import com.huassit.imenu.android.model.Recommand;
import com.huassit.imenu.android.model.Share;
import com.huassit.imenu.android.model.Store;
import com.huassit.imenu.android.model.StoreRecommand;
import com.huassit.imenu.android.ui.cart.CartActivity;
import com.huassit.imenu.android.util.NumberFormatUtils;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.view.NavigationView;
import com.huassit.imenu.android.view.StoreRecommandDialog;

/**
 * 店长推荐界面
 * 
 * @author shang_guan
 */
public class StoreRecommandActivity extends BaseActivity implements
		OnClickListener {
	private NavigationView navigationView;
	/** view pager */
	private ViewPager recommand_viewpager;
	/** 选择人数按钮 */
	private Button choose_people;
	/** 推荐套餐人数 */
	private String recommand_people;
	/** 适配器 */
	private RecommandMenuListAdapter mListAdapter;
	/** 数据源 */
	private List<StoreRecommand> mRecommandsList;
	/** 商户ID */
	private String store_id = "";
	/** 储存放入ViewPager的list */
	private List<View> mViewsList;
	/** viewPager下方小圆点 */
	private LinearLayout recommand_viewpager_dian;
	/** viewPage适配器 */
	private RecommandViewPagerAdapter mViewPagerAdapter;
	/** 小圆点的ImageView */
	private ImageView[] imageViews;
	/** recommend_list */
	private ArrayList<Recommand> recommend_list;
	/** 当前所在的页面指针 */
	private int viewPagerPostion = 0;
	private MyApplication application;
	private String memberId;
	/** 商家实体类 */
	private Store store;
	/** 每个套餐的总价 */
	private String totalPrice;

	@Override
	public int getContentView() {
		return R.layout.store_recommand;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initView() {
		memberId = PreferencesUtils.getString(context, "member_id");
		if (getIntent().getExtras().getSerializable("store") != null) {
			store = (Store) getIntent().getExtras().getSerializable("store");
			store_id = store.id;
			recommend_list = (ArrayList<Recommand>) getIntent().getExtras()
					.getSerializable("recommend_list");
			if (recommend_list != null && recommend_list.size() > 0) {
				recommand_people = recommend_list.get(0).recommandPeople;
			} else {
				recommand_people = "0";
			}
		}
		application = (MyApplication) getApplicationContext();
		recommand_viewpager = (ViewPager) findViewById(R.id.recommand_viewpager);
		recommand_viewpager_dian = (LinearLayout) findViewById(R.id.recommand_viewpager_dian);
		choose_people = (Button) findViewById(R.id.choose_people);
		choose_people.setOnClickListener(this);
		navigationView = (NavigationView) findViewById(R.id.navigationView);
		navigationView.setNavigateItemText(NavigationView.TITLE, store.name);
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
						if (!StringUtils.isBlank(token)) {
							navigationView.showCategoryView();
						} else {
							// 注册
							Intent intent = new Intent(
									StoreRecommandActivity.this,
									RegisterActivity.class);
							startActivity(intent);
						}
					}
				});
		navigationView.setNavigateItemOnClickListener(
				NavigationView.FIRST_RIGHT_TEXT_VIEW, new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 登录
						Intent intent = new Intent(StoreRecommandActivity.this,
								LoginActivity.class);
						startActivity(intent);
					}
				});
		changeButtonText(recommand_people);
	}

	@Override
	public void initData() {
		UploadAdapter(recommand_people);
	}

	/** 请求套餐 */
	public void UploadAdapter(String recommand_people) {
		this.recommand_people = recommand_people;
		GetRecommendMenuListResp mFansListlResp = new GetRecommendMenuListResp(
				this, handler, store_id, recommand_people);
		mFansListlResp.asyncInvoke(SUCCESS, FAILURE);
	}

	@SuppressLint("HandlerLeak")
	@SuppressWarnings("unchecked")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SUCCESS:
				if (msg.obj != null) {
					mRecommandsList = (List<StoreRecommand>) msg.obj;
					initViewPagerView();
				}
				break;
			case FAILURE:
				if (msg.obj != null) {
					navigationView.showErrorMessage(msg.obj.toString());
				}
				break;
			}
		}
	};

	/** 初始化ViewPager内控件 */
	private void initViewPagerView() {
		mViewsList = new ArrayList<View>();
		for (int i = 0; i < mRecommandsList.size(); i++) {
			LayoutInflater inflater = getLayoutInflater();
			View view = inflater.inflate(R.layout.store_recommand_list, null);
			TextView recommand_menu_name = (TextView) view
					.findViewById(R.id.recommand_menu_name);
			recommand_menu_name.setText(mRecommandsList.get(i).recommend_name);
			// TextView recommand_menu_price = (TextView) view
			// .findViewById(R.id.recommand_menu_price);
			totalPrice = "35.5";
			// recommand_menu_price.setText("￥"+totalPrice);
			TextView to_cart = (TextView) view.findViewById(R.id.to_cart);
			to_cart.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 获取当前页面的菜品集合
					ArrayList<Menu> menusList = new ArrayList<Menu>();
					menusList = mRecommandsList.get(viewPagerPostion).menu_list;
					menusList = getMenuList(menusList);
					if (!StringUtils.isBlank(token)) {
						Map<String, Cart> cartMap = new HashMap<String, Cart>();
						cartMap = application.getCartMap();
						if (null != cartMap && cartMap.size() > 0) {
							// 购物车不为空
							Cart cart = (Cart) cartMap.get("store_id");
							if (null != cart) {
								// 购物车里的菜品是此商家的，直接添加到购物车
								cart.menuList.addAll(menusList);
							} else {
								// 不是此商家，需先清空购物车，再放入菜品至购物车
								cartMap.clear();
								Cart toCart = new Cart();
								toCart.menuList = menusList;
								cartMap.put(store_id, toCart);
								application.setCartMap(cartMap);
							}
						} else {
							// 购物车为空,直接添加菜品至购物车
							cartMap = new HashMap<String, Cart>();
							Cart toCart = new Cart();
							toCart.menuList = menusList;
							cartMap.put(store_id, toCart);
							application.setCartMap(cartMap);
						}
						Intent intent = new Intent(StoreRecommandActivity.this,
								CartActivity.class);
						intent.putExtra("store", store);
						intent.putExtra("total_price", totalPrice);
						startActivity(intent);
						finish();
					} else {
						// 未登录跳转至用户验证
						Float total = Float.MIN_NORMAL;
						for (int j = 0; j < menusList.size(); j++) {
							total += Float.parseFloat(menusList.get(j).menu_price);
						}
						Cart cart = new Cart();
						cart.storeInfo = store;
						cart.menuList = menusList;
						cart.people = "1";
						cart.total = NumberFormatUtils.format(total);
						Map<String, Cart> map = new HashMap<String, Cart>();
						map.put(store_id, cart);
						application.setCartMap(map);
						Intent to_authenti_intent = new Intent(context,
								UserAuthenticationActivity.class);
						to_authenti_intent.putExtra("cart", cart);
						startActivity(to_authenti_intent);
					}
				}
			});
			ListView recomand_list = (ListView) view
					.findViewById(R.id.recomand_list);
			RecommandMenuListAdapter mListAdapter = new RecommandMenuListAdapter(
					StoreRecommandActivity.this,
					R.layout.store_recommand_list_item);
			mListAdapter.getDataList().clear();
			mListAdapter.getDataList().addAll(mRecommandsList.get(i).menu_list);
			recomand_list.setAdapter(mListAdapter);
			mViewsList.add(view);
		}
		mViewPagerAdapter = new RecommandViewPagerAdapter(this, mViewsList);
		recommand_viewpager.setAdapter(mViewPagerAdapter);
		recommand_viewpager.getParent()
				.requestDisallowInterceptTouchEvent(true);
		setImageDiao();
	}

	/** 封装菜品集合 */
	private ArrayList<Menu> getMenuList(ArrayList<Menu> menus) {
		for (int i = 0; i < menus.size(); i++) {
			menus.get(i).menu_count = 1;
		}
		return menus;
	}

	/** 设置图片下方的小圆点 */
	private void setImageDiao() {
		recommand_viewpager_dian.removeAllViews();
		imageViews = new ImageView[mViewsList.size()];
		System.out.println("mViewsList:" + mViewsList.size());
		for (int i = 0; i < mViewsList.size(); i++) {
			LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			// 设置每个小圆点距离左边的间距
			margin.setMargins(5, 0, 5, 0);
			ImageView imageView = new ImageView(this);
			imageViews[i] = imageView;
			if (i == 0) {
				// 默认选中第一张图片
				imageViews[i]
						.setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				// 其他图片都设置未选中状态
				imageViews[i]
						.setBackgroundResource(R.drawable.page_indicator_unfocused);
			}
			recommand_viewpager_dian.setGravity(Gravity.CENTER_VERTICAL
					| Gravity.CENTER_HORIZONTAL);
			recommand_viewpager_dian.addView(imageViews[i], margin);
			recommand_viewpager
					.setOnPageChangeListener(new PageChangeListener());
		}
	}

	/** 指引页面更改事件监听器 */
	class PageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			viewPagerPostion = arg0;
			// 遍历数组让当前选中图片下的小圆点设置颜色
			for (int i = 0; i < imageViews.length; i++) {
				imageViews[arg0]
						.setBackgroundResource(R.drawable.page_indicator_focused);
				if (arg0 != i) {
					imageViews[i]
							.setBackgroundResource(R.drawable.page_indicator_unfocused);
				}
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		navigationView.setBackgroundColor(Color.parseColor(colorValue));
		if (!StringUtils.isBlank(token)) {
			navigationView.setNavigateItemText(
					NavigationView.FIRST_RIGHT_TEXT_VIEW, "");
			navigationView.setNavigateItemText(
					NavigationView.SECOND_RIGHT_TEXT_VIEW, "");
			navigationView.setNavigateItemText(NavigationView.TITLE,
					StringUtils.getString(this, R.string.menu_detail));
			navigationView.setNavigateItemBackground(
					NavigationView.FIRST_RIGHT_TEXT_VIEW, R.drawable.share);
			navigationView.setNavigateItemBackground(
					NavigationView.SECOND_RIGHT_TEXT_VIEW, R.drawable.navi);
		} else {
			navigationView.setNavigateItemVisibility(
					NavigationView.FIRST_RIGHT_TEXT_VIEW, View.VISIBLE);
			navigationView.setNavigateItemText(
					NavigationView.FIRST_RIGHT_TEXT_VIEW, R.string.login);
			navigationView.setNavigateItemText(
					NavigationView.SECOND_RIGHT_TEXT_VIEW, R.string.register);
			navigationView.setNavigateItemBackground(
					NavigationView.SECOND_RIGHT_TEXT_VIEW, null);
			navigationView.setNavigateItemBackground(
					NavigationView.FIRST_RIGHT_TEXT_VIEW, null);
		}
	}

	/** 改变选择人数按钮 */
	public void changeButtonText(String peopleCount) {
		choose_people.setText(StringUtils.getString(
				StoreRecommandActivity.this, R.string.choosed)
				+ peopleCount
				+ StringUtils.getString(StoreRecommandActivity.this,
						R.string.people));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.choose_people:
			if (recommend_list != null && recommend_list.size() > 0) {
				StoreRecommandDialog mDialog = new StoreRecommandDialog(this,
						recommend_list);
				mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				mDialog.show();
			}
			break;
		}
	}
}
