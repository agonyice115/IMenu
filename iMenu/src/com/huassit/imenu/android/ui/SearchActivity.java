package com.huassit.imenu.android.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.baidu.location.BDLocation;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.adapter.*;
import com.huassit.imenu.android.biz.*;
import com.huassit.imenu.android.db.dao.SearchHistoryDao;
import com.huassit.imenu.android.model.*;
import com.huassit.imenu.android.util.LocationUtils;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.view.NavigationView;
import com.huassit.imenu.android.view.SegmentControllerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

/**
 * Created by Sylar on 14-6-28.
 */
public class SearchActivity extends BaseActivity implements
		View.OnClickListener, PullToRefreshBase.OnLastItemVisibleListener {

	/**
	 * 当前页数
	 */
	private int currentPage;
	private static final String PAGE_SIZE = "20";
	/**
	 * 搜索菜单标石
	 */
	private static final int SEARCH_MENU = 1;
	/**
	 * 搜索商家标石
	 */
	private static final int SEARCH_STORE = 2;
	/**
	 * 搜索动态标石
	 */
	private static final int SEARCH_ACTIVITIES = 3;
	/**
	 * 搜索用户标石
	 */
	private static final int SEARCH_MEMBER = 4;
	/**
	 * 模糊搜索成功标石
	 */
	private static final int VAGUE_SUCCESS = 10001;
	/**
	 * 模糊搜索失败标石
	 */
	private static final int VAGUE_FAILURE = 10002;
	/**
	 * 搜索菜单成功标石
	 */
	private static final int SEARCH_MENU_SUCCESS = 30001;
	/**
	 * 搜索用户成功标石
	 */
	private static final int SEARCH_MEMBER_SUCCESS = 40001;
	/**
	 * 搜索商家成功标石
	 */
	private static final int SEARCH_STORE_SUCCESS = 50001;
	private static final int FOLLOW_SUCCESS = 20001;
	private static final int FOLLOW_FAILURE = 20002;
	/**
	 * **************************************************************
	 */
	private NavigationView mNavigationView;
	/**
	 * 菜单listView
	 */
	private PullToRefreshListView menuRefreshListView;
	/**
	 * 商家listView
	 */
	private PullToRefreshListView storeRefreshListView;
	/**
	 * 用户listView
	 */
	private PullToRefreshListView memberRefreshListView;
	/**
	 * 动态listView
	 */
	private PullToRefreshListView activitiesRefreshListView;
	/**
	 * 菜单按钮
	 */
	// private Button mTypeMenu;
	// /**商家按钮*/
	// private Button mTypeStore;
	// /**动态按钮*/
	// private Button mTypeActivities;
	// /**用户按钮*/
	// private Button mTypeMember;
	private SegmentControllerView segmentControllerView;
	/**
	 * 搜索输入框
	 */
	private EditText mSearchEdit;
	/**
	 * 取消按钮
	 */
	private Button mCancelButton;
	/**
	 * 清除搜索历史按钮
	 */
	private TextView mClearTextView;
	/**
	 * 搜索历史layout
	 */
	private LinearLayout searchHistoryLayout;
	/**
	 * 模糊搜索layout
	 */
	private LinearLayout searchVagueLayout;
	/**
	 * 搜索提示关键词listView
	 */
	private ListView mSearchKeywordListView;
	/**
	 * 模糊搜索listView
	 */
	private ListView searchVagueListView;
	/******************************************************************/
	/**
	 * 要搜索的种类
	 */
	private int searchType = SEARCH_MENU;
	/**
	 * 操控软键盘
	 */
	private InputMethodManager mInputMethodManager;
	/**
	 * 商家列表适配器
	 */
	private SearchStoreAdapter mSearchStoreAdapter;
	/**
	 * 用户列表适配器
	 */
	private SearchMemberAdapter mSearchMemberAdapter;
	/**
	 * 动态列表适配器
	 */
	private SearchActivitiesAdapter mSearchActivitiesAdapter;
	/**
	 * 菜单列表适配器
	 */
	private SearchMenuAdapter mSearchMenuAdapter;
	/**
	 * 搜索历史操作类
	 */
	private SearchHistoryDao searchHistoryDao;
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat dateFormat = new SimpleDateFormat();
	/**
	 * 搜索历史数据源
	 */
	private List<SearchHistory> searchHistoryList;
	/**
	 * 搜索历史列表适配器
	 */
	private ArrayAdapter<SearchHistory> searchHistoryAdapter;
	/**
	 * 模糊搜索列表适配器
	 */
	private SearchVagueListAdapter vagueListAdapter;
	/**
	 * 判断软键盘是否显示
	 */
	private boolean isInput = true;
	private boolean needDisplayVagueList = true;
	private List<Member> memberList;
	private String following_member_id;

	@Override
	public int getContentView() {
		return R.layout.search_activity;
	}

	@Override
	public void initView() {
		searchHistoryDao = new SearchHistoryDao(this);
		mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mNavigationView = (NavigationView) findViewById(R.id.navigationView);
		mNavigationView.setNavigateItemText(NavigationView.LEFT_TEXT_VIEW,
				R.string.search);
		mNavigationView.setNavigateItemBackground(
				NavigationView.SECOND_RIGHT_TEXT_VIEW, R.drawable.navi);
		mNavigationView.setNavigateItemVisibility(
				NavigationView.LEFT_IMAGE_VIEW, View.GONE);
		mNavigationView
				.setCurrentCategoryTriangle(NavigationView.TRIANGLE_SEARCH);
		mNavigationView.setNavigateItemOnClickListener(
				NavigationView.SECOND_RIGHT_TEXT_VIEW,
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mNavigationView.showCategoryView();
					}
				});
		// mTypeActivities = (Button) findViewById(R.id.typeActivities);
		// mTypeMenu = (Button) findViewById(R.id.typeMenu);
		// mTypeStore = (Button) findViewById(R.id.typeStore);
		// mTypeMember = (Button) findViewById(R.id.typeMember);
		mSearchEdit = (EditText) findViewById(R.id.editText);
		// mTypeActivities.setOnClickListener(this);
		// mTypeMenu.setOnClickListener(this);
		// mTypeStore.setOnClickListener(this);
		// mTypeMember.setOnClickListener(this);
		segmentControllerView = (SegmentControllerView) findViewById(R.id.searchType);
		segmentControllerView.setOnChangeListener(segmentOnChangerListener);
		segmentControllerView.init(getResources().getStringArray(
				R.array.search_segment_items));
		segmentControllerView.draw();
		mSearchEdit.setOnClickListener(this);
		menuRefreshListView = (PullToRefreshListView) findViewById(R.id.menuListView);
		menuRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
		menuRefreshListView.setOnLastItemVisibleListener(this);

		activitiesRefreshListView = (PullToRefreshListView) findViewById(R.id.activitiesListView);
		activitiesRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
		activitiesRefreshListView.setOnLastItemVisibleListener(this);

		memberRefreshListView = (PullToRefreshListView) findViewById(R.id.memberListView);
		memberRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
		memberRefreshListView.setOnLastItemVisibleListener(this);

		storeRefreshListView = (PullToRefreshListView) findViewById(R.id.storeListView);
		storeRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
		storeRefreshListView.setOnLastItemVisibleListener(this);

		mSearchStoreAdapter = new SearchStoreAdapter(this,
				R.layout.search_store_list_item);
		storeRefreshListView.setAdapter(mSearchStoreAdapter);

		mSearchMemberAdapter = new SearchMemberAdapter(this,
				R.layout.search_member_list_item, callback);
		memberRefreshListView.setAdapter(mSearchMemberAdapter);

		mSearchMenuAdapter = new SearchMenuAdapter(this,
				R.layout.search_menu_list_item);
		menuRefreshListView.setAdapter(mSearchMenuAdapter);

		mSearchActivitiesAdapter = new SearchActivitiesAdapter(this,
				R.layout.search_activities_list_item);
		activitiesRefreshListView.setAdapter(mSearchActivitiesAdapter);

		searchHistoryList = searchHistoryDao.getSearchHistoryList();
		mSearchKeywordListView = (ListView) findViewById(R.id.keywordHistory);
		searchHistoryAdapter = new ArrayAdapter<SearchHistory>(this,
				android.R.layout.simple_list_item_1, searchHistoryList);
		mSearchKeywordListView.setAdapter(searchHistoryAdapter);
		mSearchKeywordListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						SearchHistory item = (SearchHistory) parent
								.getItemAtPosition(position);
						mSearchEdit.setText(item.keyword);
						currentPage = 1;
						if (searchType == SEARCH_MENU) {
							mSearchMenuAdapter.getDataList().clear();
						}
						search();
					}
				});
		mClearTextView = (TextView) findViewById(R.id.clearHistory);
		mClearTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				searchHistoryDao.deleteAll();
				searchHistoryAdapter.clear();
				searchHistoryAdapter.notifyDataSetChanged();
			}
		});
		searchHistoryLayout = (LinearLayout) findViewById(R.id.searchHistoryLayout);
		searchVagueLayout = (LinearLayout) findViewById(R.id.searchVagueLayout);
		mCancelButton = (Button) findViewById(R.id.cancelSearch);
		mCancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				hideInputMethod();
				searchHistoryLayout.setVisibility(View.GONE);
				mCancelButton.setVisibility(View.GONE);
				searchVagueLayout.setVisibility(View.GONE);
			}
		});

		mSearchEdit.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN
						&& keyCode == KeyEvent.KEYCODE_ENTER) {
					System.out
							.println("KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER");
					currentPage = 1;
					if (searchType == SEARCH_MENU) {
						mSearchMenuAdapter.getDataList().clear();
					}
					search();
					return true;
				}
				return false;
			}
		});

		mSearchEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (isInput) {
					if (!StringUtils.isBlank(s.toString())) {
						System.out.println("s.toString():" + s.toString());
						BDLocation location = LocationUtils
								.getInstance(context).getLocation();
						String lat = null, lng = null;
						if (location != null) {
							lat = String.valueOf(location.getLatitude());
							lng = String.valueOf(location.getLongitude());
						}
						needDisplayVagueList = true;
						SearchVagueResp resp = new SearchVagueResp(context,
								handler, s.toString(), searchType, lat, lng);
						resp.asyncInvoke(VAGUE_SUCCESS, VAGUE_FAILURE);
					}
				}
			}
		});

		searchVagueListView = (ListView) findViewById(R.id.searchVagueList);
		vagueListAdapter = new SearchVagueListAdapter(this,
				R.layout.search_vague_list_item);
		searchVagueListView.setAdapter(vagueListAdapter);
		searchVagueListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						SearchVague vague = (SearchVague) parent
								.getItemAtPosition(position);
						isInput = false;
						mSearchEdit.setText(vague.resultName);
						currentPage = 1;
						if (searchType == SEARCH_MENU) {
							mSearchMenuAdapter.getDataList().clear();
						}
						search();
					}
				});

	}

	private void search() {
		showProgressDialog("正在加载");
		hideInputMethod();
		needDisplayVagueList = false;
		searchHistoryLayout.setVisibility(View.GONE);
		mCancelButton.setVisibility(View.GONE);
		searchVagueLayout.setVisibility(View.GONE);
		String keyword = mSearchEdit.getText().toString();
		if (!StringUtils.isBlank(keyword)) {
			SearchHistory history = new SearchHistory();
			history.keyword = keyword;
			history.lastUpdated = dateFormat.format(new Date());
			searchHistoryDao.saveOrUpdate(history);
			loadSearchResult();
		}
	}

	private void loadSearchResult() {
		switch (searchType) {
		case SEARCH_ACTIVITIES:
			searchActivities();
			break;
		case SEARCH_MEMBER:
			searchMember();
			break;
		case SEARCH_MENU:
			searchMenu();
			break;
		case SEARCH_STORE:
			searchStore();
			break;
		}
	}

	@Override
	public void initData() {
		// 跳转至相应的商家详情界面
		storeRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Store store = (Store) parent.getItemAtPosition(position);
				Intent intent = new Intent(context, StoreDetailActivity.class);
				intent.putExtra("store", store);
				startActivity(intent);
			}

		});
		// 跳转至相应的用户主页面
		memberRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Member member = (Member) parent.getItemAtPosition(position);
				Intent intent = new Intent(context, MineActivity.class);
				intent.putExtra("view_member_id", member.memberId);
				startActivity(intent);

			}

		});
		// 跳转至相应的动态
		activitiesRefreshListView
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Dynamic dynamic = (Dynamic) parent
								.getItemAtPosition(position);
						Intent intent = new Intent(context,
								DynamicDetailActivity.class);
						intent.putExtra("dynamic", dynamic);
						startActivity(intent);
					}

				});
		menuRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Menu menu = (Menu) parent.getItemAtPosition(position);
				Intent intent = new Intent(context, MenuDetailActivity.class);
				intent.putExtra("menuId", menu.menu_id);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		hideInputMethod();
		segmentControllerView.setColor(Color.parseColor(colorValue));
	}

	private void hideInputMethod() {
		if (getCurrentFocus() != null) {
			mInputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	@Override
	public void onClick(View v) {
		final int id = v.getId();
		searchHistoryList = searchHistoryDao.getSearchHistoryList();
		searchHistoryAdapter.clear();
		for (SearchHistory history : searchHistoryList) {
			searchHistoryAdapter.add(history);
		}
		searchHistoryAdapter.notifyDataSetChanged();
		searchHistoryLayout.setVisibility(View.VISIBLE);
		mCancelButton.setVisibility(View.VISIBLE);
		isInput = true;
	}

	private void searchActivities() {
		SearchActivitiesResp activitiesResp = new SearchActivitiesResp(this,
				handler, token, mSearchEdit.getText().toString(), PAGE_SIZE,
				String.valueOf(currentPage));
		activitiesResp.asyncInvoke(SUCCESS, FAILURE);
	}

	private void searchMenu() {
		SearchMenuResp searchMenuResp = new SearchMenuResp(this, handler,
				token, mSearchEdit.getText().toString(), PAGE_SIZE,
				String.valueOf(currentPage));
		searchMenuResp.asyncInvoke(SEARCH_MENU_SUCCESS, FAILURE);
	}

	private void searchStore() {
		BDLocation location = LocationUtils.getInstance(this).getLocation();
		SearchStoreResp searchStoreResp = new SearchStoreResp(this, handler,
				token, mSearchEdit.getText().toString(), PAGE_SIZE,
				String.valueOf(currentPage), String.valueOf(location
						.getLatitude()),
				String.valueOf(location.getLongitude()));
		searchStoreResp.asyncInvoke(SEARCH_STORE_SUCCESS, FAILURE);
	}

	private void searchMember() {
		String memberId = PreferencesUtils.getString(context, "member_id");
		SearchMemberResp searchMemberResp = new SearchMemberResp(this, handler,
				token, mSearchEdit.getText().toString(), memberId, PAGE_SIZE,
				String.valueOf(currentPage));
		searchMemberResp.asyncInvoke(SEARCH_MEMBER_SUCCESS, FAILURE);
	}

	@SuppressLint("HandlerLeak")
	@SuppressWarnings("unchecked")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SUCCESS:
				resetListView();
				List<Dynamic> dynamicList = (List<Dynamic>) msg.obj;
				if (dynamicList.size() > 0) {
					activitiesRefreshListView.setVisibility(View.VISIBLE);
					activitiesRefreshListView.getRefreshableView()
							.setVisibility(View.VISIBLE);
					mSearchActivitiesAdapter.getDataList().addAll(dynamicList);
					mSearchActivitiesAdapter.notifyDataSetChanged();
				} else {
					if (currentPage == 1) {
						mNavigationView.showMessage(String.valueOf(msg.obj));
					}
				}
				break;
			case FAILURE:
				if (currentPage == 1) {
					mNavigationView.showMessage(String.valueOf(msg.obj));
				}
				break;
			case VAGUE_SUCCESS:
				System.out.println("needDisplayVagueList:"
						+ needDisplayVagueList);
				if (needDisplayVagueList) {
					List<SearchVague> vagueList = (List<SearchVague>) msg.obj;
					if (vagueList != null && vagueList.size() > 0) {
						vagueListAdapter.getDataList().clear();
						vagueListAdapter.getDataList().addAll(vagueList);
						vagueListAdapter.notifyDataSetChanged();
						searchVagueLayout.setVisibility(View.VISIBLE);
						searchHistoryLayout.setVisibility(View.GONE);
					} else {
						searchVagueLayout.setVisibility(View.GONE);
						searchHistoryLayout.setVisibility(View.VISIBLE);
					}
				}
				break;
			case VAGUE_FAILURE:
				break;
			case FOLLOW_FAILURE:

				break;
			case FOLLOW_SUCCESS:
				if (msg.obj != null) {
					JSONObject data = (JSONObject) msg.obj;
					String follow_status = data.optString("follow_status");
					if (memberList != null && memberList.size() > 0) {
						for (int i = 0; i < memberList.size(); i++) {
							if (memberList.get(i).memberId
									.equals(following_member_id)) {
								memberList.get(i).followStatus = follow_status;
							}
						}
						mSearchMemberAdapter.getDataList().clear();
						mSearchMemberAdapter.getDataList().addAll(memberList);
						mSearchMemberAdapter.notifyDataSetChanged();
					}
				}
				break;
			case SEARCH_MEMBER_SUCCESS:
				resetListView();
				memberList = (List<Member>) msg.obj;
				if (memberList.size() > 0) {
					memberRefreshListView.setVisibility(View.VISIBLE);
					memberRefreshListView.getRefreshableView().setVisibility(
							View.VISIBLE);
					mSearchMemberAdapter.getDataList().clear();
					mSearchMemberAdapter.getDataList().addAll(memberList);
					mSearchMemberAdapter.notifyDataSetChanged();
				} else {
					if (currentPage == 1) {
						mNavigationView.showMessage(String.valueOf(msg.obj));
					}
				}
				break;
			case SEARCH_MENU_SUCCESS:
				resetListView();
				List<Menu> menuList = (List<Menu>) msg.obj;
				if (menuList.size() > 0) {
					menuRefreshListView.setVisibility(View.VISIBLE);
					menuRefreshListView.getRefreshableView().setVisibility(
							View.VISIBLE);
					mSearchMenuAdapter.getDataList().addAll(menuList);
					mSearchMenuAdapter.notifyDataSetChanged();
				} else {
					if (currentPage == 1) {
						mNavigationView.showMessage(String.valueOf(msg.obj));
					}
					menuRefreshListView.setVisibility(View.VISIBLE);
					menuRefreshListView.getRefreshableView().setVisibility(
							View.VISIBLE);
				}
				break;
			case SEARCH_STORE_SUCCESS:
				resetListView();
				List<Store> storeList = (List<Store>) msg.obj;
				if (storeList.size() > 0) {
					storeRefreshListView.setVisibility(View.VISIBLE);
					storeRefreshListView.getRefreshableView().setVisibility(
							View.VISIBLE);
					mSearchStoreAdapter.getDataList().addAll(storeList);
					mSearchStoreAdapter.notifyDataSetChanged();
				} else {
					if (currentPage == 1) {
						mNavigationView.showMessage(String.valueOf(msg.obj));
					}
				}
				break;
			}
			closeProgressDialog();
		}
	};

	// private void resetSearchTypeButton() {
	// mTypeActivities.setBackgroundResource(R.drawable.center_button_up);
	// mTypeMenu.setBackgroundResource(R.drawable.left_button_up);
	// mTypeStore.setBackgroundResource(R.drawable.center_button_up);
	// mTypeMember.setBackgroundResource(R.drawable.right_button_up);
	// mTypeActivities.setTextColor(Color.parseColor(colorValue));
	// mTypeMenu.setTextColor(Color.parseColor(colorValue));
	// mTypeStore.setTextColor(Color.parseColor(colorValue));
	// mTypeMember.setTextColor(Color.parseColor(colorValue));
	// }

	private void resetListView() {
		menuRefreshListView.setVisibility(View.GONE);
		activitiesRefreshListView.setVisibility(View.GONE);
		memberRefreshListView.setVisibility(View.GONE);
		storeRefreshListView.setVisibility(View.GONE);
		menuRefreshListView.onRefreshComplete();
		activitiesRefreshListView.onRefreshComplete();
		memberRefreshListView.onRefreshComplete();
		storeRefreshListView.onRefreshComplete();
	}

	@Override
	public void onLastItemVisible() {
		currentPage++;
		loadSearchResult();
	}

	private SearchMemberAdapter.SearchMemberCallback callback = new SearchMemberAdapter.SearchMemberCallback() {
		@Override
		public void onFollowButtonClick(Member member) {
			String memberId = PreferencesUtils.getString(SearchActivity.this,
					"member_id");
			// EditFollowStatusResp mStatusResp = new
			// EditFollowStatusResp(SearchActivity.this, handler, token,
			// memberId, member.memberId, "", member.followStatus);
			following_member_id = member.memberId;
			EditFollowStatusResp mStatusResp = new EditFollowStatusResp(
					SearchActivity.this, handler, token, memberId,
					member.memberId, "", AddOrCancel(member.followStatus));
			mStatusResp.asyncInvoke(FOLLOW_SUCCESS, FOLLOW_FAILURE);
		}
	};

	/** 更改状态请求是添加或取消 */
	public static String AddOrCancel(String statue) {
		String following_status = "";
		if (statue.equals("0")) {
			following_status = "1";
		}
		if (statue.equals("1")) {
			following_status = "0";
		}
		if (statue.equals("2")) {
			following_status = "0";
		}
		return following_status;
	}

	private SegmentControllerView.OnChangeListener segmentOnChangerListener = new SegmentControllerView.OnChangeListener() {
		@Override
		public void onChange() {
			resetListView();
			switch (segmentControllerView.getPosition()) {
			case 0:// 菜单
				searchType = SEARCH_MENU;
				if (!mSearchEdit.getText().toString().equals("")) {
					showProgressDialog("正在加载");
					mSearchMenuAdapter.getDataList().clear();
					currentPage = 1;
					searchMenu();
				}
				break;
			case 1:// 商家
				searchType = SEARCH_STORE;
				if (!mSearchEdit.getText().toString().equals("")) {
					showProgressDialog("正在加载");
					mSearchStoreAdapter.getDataList().clear();
					currentPage = 1;
					searchStore();
				}
				break;
			case 2:// 动态
				searchType = SEARCH_ACTIVITIES;
				if (!mSearchEdit.getText().toString().equals("")) {
					showProgressDialog("正在加载");
					mSearchActivitiesAdapter.getDataList().clear();
					currentPage = 1;
					searchActivities();
				}
				break;
			case 3:// 用户
				searchType = SEARCH_MEMBER;
				if (!mSearchEdit.getText().toString().equals("")) {
					showProgressDialog("正在加载");
					mSearchMemberAdapter.getDataList().clear();
					currentPage = 1;
					searchMember();
				}
				break;
			}
		}
	};
}
