package com.huassit.imenu.android.ui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.adapter.ScoreRecordAdapter;
import com.huassit.imenu.android.biz.GetScoreListResp;
import com.huassit.imenu.android.model.Score;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.view.NavigationView;

/**
 * 积分记录界面
 * 
 * @author shang_guan
 * 
 */
public class ScoreRecordActivity extends BaseActivity {

	/** 头部 */
	private NavigationView mNavigationView;
	/** 刷新listView */
	private PullToRefreshListView score_list;
	/** listView */
	private ListView mListView;
	/** 请求第几页 */
	private int page = 0;
	/** 每页请求多少条数据 */
	private int pageSize = 10;
	/** 积分适配器 */
	private ScoreRecordAdapter mScoreRecordAdapter;
	/** 列表数据源 */
	private List<Score> mScoresList;

	@Override
	public int getContentView() {
		return R.layout.score_record;
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	@Override
	public void initView() {
		score_list = (PullToRefreshListView) findViewById(R.id.score_list);
		mListView = score_list.getRefreshableView();
		score_list.setMode(Mode.PULL_UP_TO_REFRESH);
		score_list.setShowIndicator(false);
		score_list.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh(PullToRefreshBase refreshView) {
				// 上拉加载更多
				page++;
				// loadData(mSortBy);
			}
		});
		/** 头部初始化 */
		mNavigationView = (NavigationView) findViewById(R.id.navigationView);
		mNavigationView.setNavigateItemText(NavigationView.LEFT_TEXT_VIEW,
				R.string.score_record);
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
		mScoreRecordAdapter = new ScoreRecordAdapter(this,
				R.layout.score_record_list_item);
	}

	@Override
	public void initData() {
		String memberId = PreferencesUtils.getString(context, "member_id");
		GetScoreListResp mGetScoreListResp = new GetScoreListResp(
				ScoreRecordActivity.this, handler, memberId, pageSize + "",
				page + "", token);
		mGetScoreListResp.asyncInvoke(SUCCESS, FAILURE);
		showProgressDialog(StringUtils.getString(this,R.string.loading));
	}

	@SuppressWarnings("unchecked")
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			closeProgressDialog();
			switch (msg.what) {
			case FAILURE:
				if (msg.obj.toString().equals("response null")) {
					mScoresList = new ArrayList<Score>();
					for (int i = 0; i < 7; i++) {
						Score mScore1 = new Score();
						mScore1.score = "+20" + i;
						mScore1.score_date = "2014-7-1" + i;
						mScore1.score_description = "小熊寿司代金券";
						mScore1.score_title = "评价";
						mScoresList.add(mScore1);
					}
					mScoreRecordAdapter.getDataList().clear();
					mScoreRecordAdapter.getDataList().addAll(mScoresList);
					mScoreRecordAdapter.notifyDataSetChanged();
					mListView.setAdapter(mScoreRecordAdapter);
				}
				break;
			case SUCCESS:
				mScoresList = (List<Score>) msg.obj;
				mScoreRecordAdapter.getDataList().clear();
				mScoreRecordAdapter.getDataList().addAll(mScoresList);
				mScoreRecordAdapter.notifyDataSetChanged();
				mListView.setAdapter(mScoreRecordAdapter);
				finish();
				break;
			}
			super.handleMessage(msg);
		}
	};
}
