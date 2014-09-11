package com.huassit.imenu.android.view;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.huassit.imenu.android.R;
import com.huassit.imenu.android.adapter.CartDialogListAdapter;
import com.huassit.imenu.android.model.Coupon;
import com.huassit.imenu.android.ui.cart.CartActivity;
import com.huassit.imenu.android.util.StringUtils;

/**
 * 购物车选择优惠对话框
 * 
 * @author shang_guan
 * 
 */
public class CartDialog extends Dialog {

	private Activity mContext;
	/** 促销列表 */
	private ListView sales_list;
	/** 适配器 */
	private CartDialogListAdapter mAdapter;
	/** 数据源 */
	private List<Coupon> mCouponsList;
	private LayoutInflater inflater;

	public CartDialog(Activity context) {
		super(context);
		this.mContext = context;
	}

	public CartDialog(Activity context, List<Coupon> mCouponsList) {
		super(context);
		this.mContext = context;
		this.mCouponsList = mCouponsList;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cart_dialog);
		sales_list = (ListView) findViewById(R.id.sales_list);
		mAdapter = new CartDialogListAdapter(mContext,
				R.layout.cart_coupon_list_item);
		sales_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				((CartActivity) mContext).UploadAdapter(mAdapter.getDataList()
						.get(position).couponId,
						mAdapter.getDataList().get(position).couponTitle);
				CartDialog.this.dismiss();
			}
		});
		inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.listview_footview_item, null);
		TextView foot_item_tv = (TextView) v.findViewById(R.id.foot_item_tv);
		foot_item_tv.setText(StringUtils.getString(mContext, R.string.cancel));
		foot_item_tv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				((CartActivity) mContext).UploadAdapter("", "");
				CartDialog.this.dismiss();
			}
		});
		sales_list.addFooterView(v);
		sales_list.setFooterDividersEnabled(true);
		sales_list.setAdapter(mAdapter);
		initData();
	}

	/** 初始化对话框 */
	private void initData() {
		mAdapter.getDataList().clear();
		mAdapter.getDataList().addAll(mCouponsList);
		mAdapter.notifyDataSetChanged();
	}
}
