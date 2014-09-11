package com.huassit.imenu.android.ui;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.TextView;

import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.biz.GetSystemMessageDetailResp;
import com.huassit.imenu.android.model.Message;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.util.TimeUtil;
import com.huassit.imenu.android.view.NavigationView;

/**
 * 消息详情界面
 * 
 * @author shang_guan
 * 
 */
public class MessageDetailActivity extends BaseActivity {

	private NavigationView mNavigationView;
	/** 消息标题 */
	private TextView detail_title;
	/** 消息内容 */
	private WebView detail_context;
	/** 消息时间 */
	private TextView detail_date;
	/** 消息ID */
	private String system_message_id;
	public final static String WEB_STYLE = "<style>p{color:#ccc;}</style>";

	@Override
	public int getContentView() {
		return R.layout.message_detail;
	}

	@Override
	public void initView() {
		detail_title = (TextView) findViewById(R.id.detail_title);
		detail_context = (WebView) findViewById(R.id.detail_context);
		detail_date = (TextView) findViewById(R.id.detail_date);
		mNavigationView = (NavigationView) findViewById(R.id.navigationView);
		mNavigationView.setNavigateItemText(NavigationView.LEFT_TEXT_VIEW,
				R.string.message);
		mNavigationView.setNavigateItemBackground(
				NavigationView.SECOND_RIGHT_TEXT_VIEW, R.drawable.close);
		mNavigationView.setNavigateItemVisibility(
				NavigationView.LEFT_IMAGE_VIEW, View.GONE);
		mNavigationView.setNavigateItemOnClickListener(
				NavigationView.SECOND_RIGHT_TEXT_VIEW, new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
	}

	@Override
	public void initData() {
		if (getIntent().getExtras() != null) {
			system_message_id = getIntent().getExtras().getString(
					"system_message_id");
			// initFalseData();
		}
		GetSystemMessageDetailResp menuResp = new GetSystemMessageDetailResp(
				MessageDetailActivity.this, handler,
				PreferencesUtils.getString(this, "member_id"),
				system_message_id, token);
		menuResp.asyncInvoke(100, 101);
		showProgressDialog(StringUtils.getString(this,R.string.loading));
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 101:

				break;
			case 100:
				if (msg.obj != null) {
					Message message = (Message) msg.obj;
					detail_title.setText(message.title);
					// detail_context.getSettings().setDefaultTextEncodingName(
					// "utf-8");
					detail_context.loadDataWithBaseURL(null, WEB_STYLE+message.content,
							"text/html", "utf-8", null);
					detail_context.getSettings().setLayoutAlgorithm(
							LayoutAlgorithm.SINGLE_COLUMN);
					detail_date.setText(TimeUtil.FormatTime(
							message.date_modified, "yyyy-MM-dd HH:mm"));
				}
				closeProgressDialog();
				break;
			default:
				break;
			}
		}
	};
}
