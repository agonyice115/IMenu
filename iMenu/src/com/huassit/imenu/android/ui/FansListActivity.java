package com.huassit.imenu.android.ui;

import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.adapter.FansListAdapter;
import com.huassit.imenu.android.biz.EditFollowStatusResp;
import com.huassit.imenu.android.biz.GetFansOrFollowListlResp;
import com.huassit.imenu.android.model.Fans;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.view.NavigationView;

/**
 * 粉丝列表界面
 * 
 * @author shang_guan
 */
public class FansListActivity extends BaseActivity {
	private NavigationView navigationView;
	/** listView */
	private ListView fans_list;
	/** 适配器 */
	private FansListAdapter mAdapter;
	/** 数据源 */
	private List<Fans> mFansList;
	/** 更改状态成功 */
	private static final int CHANGESTATUSSSUCCESS = 300;
	/** 储存关注会员ID */
	private String following_member_id;
	/** 商户ID */
	private String store_id = "";
	/** 粉丝标题 */
	private String member_name;

	@Override
	public int getContentView() {
		return R.layout.fans_list;
	}

	@Override
	public void initView() {
		if (getIntent().getExtras().getString("store_id") != null) {
			store_id = getIntent().getExtras().getString("store_id");
		}
		fans_list = (ListView) findViewById(R.id.fans_list);
		navigationView = (NavigationView) findViewById(R.id.navigationView);
		
		navigationView.setNavigateItemOnClickListener(
				NavigationView.LEFT_TEXT_VIEW, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		navigationView.setNavigateItemOnClickListener(NavigationView.FIRST_RIGHT_TEXT_VIEW, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(StringUtils.isBlank(token)){
					// 登录
					Intent intent = new Intent(FansListActivity.this,
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
						if(!StringUtils.isBlank(token)){
							navigationView.showCategoryView();
						}else{
							// 注册
							Intent intent = new Intent(FansListActivity.this,
									RegisterActivity.class);
							startActivity(intent);
							overridePendingTransition(R.anim.translate_in,
									R.anim.translate_out);
						}
					}
				});
		mAdapter = new FansListAdapter(this, R.layout.fans_follow_list_item, 1);

	}

	@Override
	public void initData() {
		String member_id = PreferencesUtils.getString(this, "member_id");
		// 粉丝用户ID
		String member_id_ = "";
		if (getIntent().getExtras().getString("member_id") != null) {
			if (getIntent().getExtras().getString("code").equals("")) {
				member_id = getIntent().getExtras().getString("member_id");
				member_id_ = getIntent().getExtras().getString("member_id");
				member_name = getIntent().getExtras().getString("member_name");
			} else {
				member_id_ = getIntent().getExtras().getString("member_id");
				member_name = getIntent().getExtras().getString("member_name");
			}
		}
		if (!store_id.equals("")) {
			member_id_ = "";
		}
		GetFansOrFollowListlResp mFansListlResp = new GetFansOrFollowListlResp(
				this, handler, member_id, member_id_, store_id, token, 1, "");// 1代表...种类标石：1/获取粉丝列表接口,2/获取关注列表接口
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
					mFansList = (List<Fans>) msg.obj;
					mAdapter.getDataList().clear();
					mAdapter.getDataList().addAll(mFansList);
					mAdapter.notifyDataSetChanged();
					fans_list.setAdapter(mAdapter);
				}
				break;
			case FAILURE:
				if (msg.obj != null) {
					navigationView.showErrorMessage(msg.obj.toString());
				}
				break;
			case CHANGESTATUSSSUCCESS:
				if (msg.obj != null) {
					JSONObject data = (JSONObject) msg.obj;
					String follow_status = data.optString("follow_status");
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
				break;
			}
		}
	};

	/** 请求更改粉丝状态 */
	public void ChangeStatus(String following_member_id, String following_status) {
		String member_id = PreferencesUtils.getString(this, "member_id");
		this.following_member_id = following_member_id;
		EditFollowStatusResp mStatusResp = new EditFollowStatusResp(
				FansListActivity.this, handler, token, member_id,
				following_member_id, "", following_status);
		mStatusResp.asyncInvoke(CHANGESTATUSSSUCCESS, FAILURE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		navigationView.setBackgroundColor(Color.parseColor(colorValue));
		if(!StringUtils.isBlank(token)){
			navigationView.setNavigateItemText(NavigationView.TITLE, StringUtils.getString(this,R.string.fans));
			navigationView.setNavigateItemVisibility(
					NavigationView.LEFT_IMAGE_VIEW, View.GONE);
			navigationView.setNavigateItemVisibility(
					NavigationView.FIRST_RIGHT_TEXT_VIEW, View.GONE);
			navigationView.setNavigateItemBackground(
					NavigationView.SECOND_RIGHT_TEXT_VIEW, R.drawable.navi);
			navigationView.setNavigateItemBackground(NavigationView.LEFT_TEXT_VIEW,
					R.drawable.back);
			navigationView.setCurrentCategoryTriangle(NavigationView.TRIANGLE_MINE);
			navigationView.setNavigateItemText(
					NavigationView.SECOND_RIGHT_TEXT_VIEW, "");
		}else{
			navigationView.setNavigateItemBackground(NavigationView.LEFT_TEXT_VIEW, R.drawable.back);
			navigationView.setNavigateItemVisibility(NavigationView.LEFT_IMAGE_VIEW,View.GONE);
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
