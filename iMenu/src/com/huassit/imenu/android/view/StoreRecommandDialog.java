package com.huassit.imenu.android.view;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.huassit.imenu.android.R;
import com.huassit.imenu.android.adapter.StoreRecommandDialogListAdapter;
import com.huassit.imenu.android.model.Recommand;
import com.huassit.imenu.android.ui.StoreRecommandActivity;

/**
 * 店长推荐对话框
 * 
 * @author shang_guan
 * 
 */
public class StoreRecommandDialog extends Dialog {

	private Activity mContext;
	/** 促销列表 */
	private ListView sales_list;
	/** 适配器 */
	private StoreRecommandDialogListAdapter mAdapter;
	/** 数据源 */
	private List<Recommand> recommend_list;

	public StoreRecommandDialog(Activity context) {
		super(context);
		this.mContext = context;
	}

	public StoreRecommandDialog(Activity context, List<Recommand> recommend_list) {
		super(context);
		this.mContext = context;
		this.recommend_list = recommend_list;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cart_dialog);
		sales_list = (ListView) findViewById(R.id.sales_list);
		mAdapter = new StoreRecommandDialogListAdapter(mContext,
				R.layout.cart_coupon_list_item);
		sales_list.setAdapter(mAdapter);
		sales_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				((StoreRecommandActivity) mContext).UploadAdapter(mAdapter
						.getDataList().get(position).recommandPeople);
				((StoreRecommandActivity) mContext).changeButtonText(mAdapter
						.getDataList().get(position).recommandPeople);
				StoreRecommandDialog.this.dismiss();
			}
		});
		initData();
	}

	/** 初始化对话框 */
	private void initData() {
		mAdapter.getDataList().clear();
		mAdapter.getDataList().addAll(recommend_list);
		mAdapter.notifyDataSetChanged();
	}
}
