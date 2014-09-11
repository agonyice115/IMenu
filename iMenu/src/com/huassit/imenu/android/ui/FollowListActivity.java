package com.huassit.imenu.android.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;

import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.adapter.FansListAdapter;
import com.huassit.imenu.android.adapter.FollowListAdapter;
import com.huassit.imenu.android.biz.EditFollowStatusResp;
import com.huassit.imenu.android.biz.GetFansOrFollowListlResp;
import com.huassit.imenu.android.model.Fans;
import com.huassit.imenu.android.model.Follow;
import com.huassit.imenu.android.model.FollowAndFans;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.view.NavigationView;
import com.huassit.imenu.android.view.SegmentControllerView;

/**
 * 关注列表界面
 * 
 * @author shang_guan
 */
public class FollowListActivity extends BaseActivity {
	private NavigationView navigationView;
	/** listView */
	private ListView follow_list;
	/** 关注的用户数据源 */
	private List<Fans> mFansList;
	/** 关注的商家数据源 */
	private List<Follow> mFollowsList;
	/** 用户适配器 */
	private FansListAdapter mAdapter;
	/** 商家适配器 */
	private FollowListAdapter mFollowListAdapter;
	/** 商家或用户类型选择 */
	private int followType;
	/** 商家类型 */
	private static final int STORE = 100;
	/** 用户类型 */
	private static final int USER = 200;
	/** 返回数据 */
	private FollowAndFans mAndFans;
	/** 更改状态成功 */
	private static final int CHANGESTATUSSSUCCESS = 300;
	/** 存储状态改变的会员ID */
	private String following_member_id;
	/** 存储状态改变的商家ID */
	private String following_store_id;
	private SegmentControllerView segmentControllerView;
	/** 标石头部按钮式第一回加载 */
	private boolean isOne = false;

	@Override
	public int getContentView() {
		return R.layout.follow_list;
	}

	@Override
	public void initView() {
		follow_list = (ListView) findViewById(R.id.follow_list);
		navigationView = (NavigationView) findViewById(R.id.navigationView);
		navigationView.setNavigateItemText(NavigationView.TITLE, StringUtils.getString(this,R.string.follow));
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
						navigationView.showCategoryView();
					}
				});
		segmentControllerView = (SegmentControllerView) findViewById(R.id.segmentView);
		segmentControllerView.init(getResources().getStringArray(
				R.array.follow_segment_items));
		segmentControllerView.setOnChangeListener(onChangeListener);
		segmentControllerView.setPosition(0);
		segmentControllerView.draw();
	}

	@Override
	public void initData() {
		followType = USER;
		String member_id = PreferencesUtils.getString(this, "member_id");
		String following_member_id = member_id;
		if (getIntent().getExtras().getString("following_member_id") != null
				&& !"".equals(getIntent().getExtras().getString(
						"following_member_id"))) {
			following_member_id = getIntent().getExtras().getString(
					"following_member_id");
		}
		showProgressDialog(StringUtils.getString(this,R.string.loading));
		GetFansOrFollowListlResp mFansListlResp = new GetFansOrFollowListlResp(
				this, handler, member_id, "", "", token, 2, following_member_id);// 2代表...种类标石：1/获取粉丝列表接口,2/获取关注列表接口
		mFansListlResp.asyncInvoke(SUCCESS, FAILURE);
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SUCCESS:
				mAndFans = (FollowAndFans) msg.obj;
				if (mAndFans != null) {
					setUserAdapter();
				}
				closeProgressDialog();
				break;
			case FAILURE:
				if (msg.obj != null) {
					navigationView.showMessage(msg.obj.toString());
				}
				closeProgressDialog();
				break;
			case CHANGESTATUSSSUCCESS:
				JSONObject data = (JSONObject) msg.obj;
				String follow_status = data.optString("follow_status");
				if (followType == USER) {
					if (mFansList != null && mFansList.size() > 0) {
						for (int i = 0; i < mFansList.size(); i++) {
							if (mFansList.get(i).member_id
									.equals(following_member_id)) {
								mFansList.get(i).follow_status = follow_status;
							}
						}
					}
					mAdapter.getDataList().clear();
					mAdapter.getDataList().addAll(mFansList);
					mAdapter.notifyDataSetChanged();
				}
				if (followType == STORE) {
					if (mFollowsList != null && mFollowsList.size() > 0) {
						for (int i = 0; i < mFollowsList.size(); i++) {
							if (mFollowsList.get(i).store_id
									.equals(following_store_id)) {
								mFollowsList.get(i).follow_status = follow_status;
							}
						}
					}
					mFollowListAdapter.getDataList().clear();
					mFollowListAdapter.getDataList().addAll(mFollowsList);
					mFollowListAdapter.notifyDataSetChanged();
				}
				break;
			}
		}
	};

	/** 请求更改关注状态,code标石：200-用户，100-商家 */
	public void ChangeStatus(String following_member_id,
			String following_store_id, String following_status, int code) {
		followType = code;
		String member_id = PreferencesUtils.getString(this, "member_id");
		this.following_member_id = following_member_id;
		this.following_store_id = following_store_id;
		EditFollowStatusResp mStatusResp = new EditFollowStatusResp(
				FollowListActivity.this, handler, token, member_id,
				following_member_id, following_store_id, following_status);
		mStatusResp.asyncInvoke(CHANGESTATUSSSUCCESS, FAILURE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		navigationView.setBackgroundColor(Color.parseColor(colorValue));
	}

	/** 设置商家适配器 */
	private void setStoreAdapter() {
		mFollowListAdapter = new FollowListAdapter(FollowListActivity.this,
				R.layout.fans_follow_list_item);
		if (mAndFans.mFollowsList != null && mAndFans.mFollowsList.size() > 0) {
			mFollowsList = mAndFans.mFollowsList;
			mFollowListAdapter.getDataList().clear();
			mFollowListAdapter.getDataList().addAll(mFollowsList);
			mFollowListAdapter.notifyDataSetChanged();
		} else {
			mFollowsList = new ArrayList<Follow>();
			mFollowListAdapter.getDataList().clear();
			mFollowListAdapter.getDataList().addAll(mFollowsList);
			mFollowListAdapter.notifyDataSetChanged();
		}
		follow_list.setAdapter(mFollowListAdapter);
	}

	/** 设置用户适配器 */
	private void setUserAdapter() {
		mAdapter = new FansListAdapter(FollowListActivity.this,
				R.layout.fans_follow_list_item, 2);
		if (mAndFans.mFansList != null && mAndFans.mFansList.size() > 0) {
			mFansList = mAndFans.mFansList;
			mAdapter.getDataList().clear();
			mAdapter.getDataList().addAll(mFansList);
			mAdapter.notifyDataSetChanged();
		} else {
			mFansList = new ArrayList<Fans>();
			mAdapter.getDataList().addAll(mFansList);
			mAdapter.notifyDataSetChanged();
		}
		follow_list.setAdapter(mAdapter);
	}

	private SegmentControllerView.OnChangeListener onChangeListener = new SegmentControllerView.OnChangeListener() {
		@Override
		public void onChange() {
			switch (segmentControllerView.getPosition()) {
			case 0:// 用户
				if (isOne) {
					followType = USER;
					setUserAdapter();
				}
				isOne = true;
				break;
			case 1:// 商家
				followType = STORE;
				setStoreAdapter();
				break;
			}
		}
	};
}
