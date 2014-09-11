package com.huassit.imenu.android.ui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.adapter.DynamicMessageListAdapter;
import com.huassit.imenu.android.biz.GetDynamicMessageListResp;
import com.huassit.imenu.android.model.DynamicMessageList;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.view.NavigationView;

/**
 * 动态消息列表界面
 * 
 * @author shang_guan
 * 
 */
public class DynamicMessageListActivity extends BaseActivity {

	/** listView控件 */
	private ListView dynamic_message_list;
	/** list适配器 */
	private DynamicMessageListAdapter mDynamicMessageListAdapter;
	/** 数据源 */
	private ArrayList<DynamicMessageList> messagesList;
	/** 实体类 */
	private DynamicMessageList message;
	private NavigationView navigationView;
	/** 获取新动态消息列表成功标石 */
	private static final int GET_DYNAMICMESSAGE_LIST_SUCCESS = 400;
	/** 获取新动态消息列表失败标石 */
	private static final int GET_DYNAMICMESSAGE_LIST_FALSE = 401;
	private int new_count = 10;
	private int page = 1;
	private LayoutInflater inflater;
	/** listView底部显示 */
	private TextView foot_item_tv;
	/** 1已读0未读 */
	private String isOpen;
	/** 最后一个动态消息的(在未读完成后读取已读列表需要传入此字段) */
	private String last_dynamic_message_to_member_id;
	/** 底部 */
	private View v;
	/** 是否是第一次记录最后一条动态消息ID */
	private boolean isOneSaveID = false;
	/** 是否是第一次查看更早信息 */
	private boolean isOneAgo = false;

	@Override
	public int getContentView() {
		return R.layout.dynamic_message_list;
	}

	@Override
	public void initView() {
		dynamic_message_list = (ListView) findViewById(R.id.dynamic_message_list);
		navigationView = (NavigationView) findViewById(R.id.navigationView);
		navigationView.setNavigateItemText(NavigationView.TITLE,
				StringUtils.getString(this, R.string.dyanmic_message));
		navigationView.setNavigateItemVisibility(
				NavigationView.LEFT_IMAGE_VIEW, View.GONE);
		navigationView.setNavigateItemVisibility(
				NavigationView.FIRST_RIGHT_TEXT_VIEW, View.GONE);
		navigationView.setNavigateItemVisibility(
				NavigationView.SECOND_RIGHT_TEXT_VIEW, View.GONE);
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
		inflater = LayoutInflater.from(context);
		v = inflater.inflate(R.layout.listview_footview_item, null);
		foot_item_tv = (TextView) v.findViewById(R.id.foot_item_tv);
		mDynamicMessageListAdapter = new DynamicMessageListAdapter(this,
				R.layout.dynamic_message_list_item);
		dynamic_message_list.setFooterDividersEnabled(true);
		dynamic_message_list.addFooterView(v);
		dynamic_message_list.setAdapter(mDynamicMessageListAdapter);
		foot_item_tv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 底部显示更多按钮
				if (foot_item_tv.getText().toString().equals("显示更早信息")) {
					isOpen = "1";
					if (isOneAgo) {
						page++;
					} else {
						page = 1;
						isOneAgo = true;
					}
					UploadApater(isOpen, last_dynamic_message_to_member_id);
				}
				if (foot_item_tv.getText().toString().equals("显示更多未读信息")) {
					isOpen = "0";
					UploadApater(isOpen, last_dynamic_message_to_member_id);
				}
			}
		});
	}

	@Override
	public void initData() {
		// 初始化时，不传此两个参数
		isOpen = "0";
		last_dynamic_message_to_member_id = "";
		UploadApater(isOpen, last_dynamic_message_to_member_id);
	}

	/** 获取新动态消息列表 */
	private void UploadApater(String isOpen,
			String last_dynamic_message_to_member_id) {
		String memberId = PreferencesUtils.getString(this, "member_id");
		GetDynamicMessageListResp messageListResp = new GetDynamicMessageListResp(
				DynamicMessageListActivity.this, handler, memberId, new_count
						+ "", page + "", isOpen,
				last_dynamic_message_to_member_id, token);
		messageListResp.asyncInvoke(GET_DYNAMICMESSAGE_LIST_SUCCESS,
				GET_DYNAMICMESSAGE_LIST_FALSE);
	}

	@SuppressLint("HandlerLeak")
	@SuppressWarnings("unchecked")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GET_DYNAMICMESSAGE_LIST_SUCCESS:
				if (msg.obj != null) {
					messagesList = (ArrayList<DynamicMessageList>) msg.obj;
					if (messagesList.size() > 0
							&& messagesList.get(messagesList.size() - 1).unread_count
									.equals("0")) {
						// 最后一条未读消息已显示,记录未读动态消息的最后一条消息ID
						if (!isOneSaveID) {
							isOneSaveID = true;
							last_dynamic_message_to_member_id = messagesList
									.get(messagesList.size() - 1).dynamic_message_to_member_id;
						}
						foot_item_tv.setText("显示更早信息");
					} else {
						if (messagesList.size() <= 0) {
							foot_item_tv.setText("没有更早的信息了");
						} else {
							foot_item_tv.setText("显示更多未读信息");
						}
					}
					// mDynamicMessageListAdapter.getDataList().clear();
					mDynamicMessageListAdapter.getDataList().addAll(
							messagesList);
					mDynamicMessageListAdapter.notifyDataSetChanged();
				}
				break;
			case GET_DYNAMICMESSAGE_LIST_FALSE:

				break;
			}
			closeProgressDialog();
		}
	};

}
