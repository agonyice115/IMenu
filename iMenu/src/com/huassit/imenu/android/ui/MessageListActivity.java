package com.huassit.imenu.android.ui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.adapter.MessageListAdapter;
import com.huassit.imenu.android.biz.GetSystemMessageListResp;
import com.huassit.imenu.android.model.Message;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.view.NavigationView;

/**
 * 消息列表界面
 * 
 * @author shang_guan
 * 
 */
public class MessageListActivity extends BaseActivity {

	/** listView控件 */
	private ListView message_list;
	/** list适配器 */
	private MessageListAdapter messageListAdapter;
	/** 数据源 */
	private ArrayList<Message> messagesList;
	/** 实体类 */
	private Message message;
	private NavigationView mNavigationView;
	/** 消息数量 */
	private int new_count = 10;
	/** 请求已读或未读1-已读，2-未读 */
	private String is_open = "";
	/** page */
	private String page = "1";

	@Override
	public int getContentView() {
		return R.layout.message_list;
	}

	@Override
	public void initView() {
		message_list = (ListView) findViewById(R.id.message_list);
		mNavigationView = (NavigationView) findViewById(R.id.navigationView);
		mNavigationView.setNavigateItemVisibility(
				NavigationView.SECOND_RIGHT_TEXT_VIEW, View.GONE);
		mNavigationView.setNavigateItemVisibility(
				NavigationView.LEFT_IMAGE_VIEW, View.GONE);
		mNavigationView.setNavigateItemBackground(
				NavigationView.LEFT_TEXT_VIEW, R.drawable.back);
		mNavigationView.setNavigateItemText(NavigationView.TITLE,
				StringUtils.getString(context, R.string.message));
		mNavigationView.setNavigateItemOnClickListener(
				NavigationView.LEFT_TEXT_VIEW, new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		messageListAdapter = new MessageListAdapter(MessageListActivity.this,
				R.layout.message_list_item);
		message_list.setAdapter(messageListAdapter);
	}

	@Override
	public void initData() {
		GetSystemMessageListResp menuResp = new GetSystemMessageListResp(this,
				handler, token, PreferencesUtils.getString(this, "member_id"),
				new_count + "", "", is_open);
		menuResp.asyncInvoke(SUCCESS, FAILURE);
		showProgressDialog(StringUtils.getString(this, R.string.loading));
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			closeProgressDialog();
			switch (msg.what) {
			case SUCCESS:
				List<Message> messagesList = (List<Message>) msg.obj;
				messageListAdapter.getDataList().clear();
				messageListAdapter.getDataList().addAll(messagesList);
				messageListAdapter.notifyDataSetChanged();
				break;
			case FAILURE:
				if (msg.obj != null) {

				}
				break;
			default:
				break;
			}
		}
	};
}
