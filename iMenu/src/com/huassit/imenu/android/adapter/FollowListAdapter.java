package com.huassit.imenu.android.adapter;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huassit.imenu.android.MyApplication;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.model.Follow;
import com.huassit.imenu.android.model.Store;
import com.huassit.imenu.android.ui.FollowListActivity;
import com.huassit.imenu.android.ui.StoreDetailActivity;
import com.huassit.imenu.android.ui.StoreMapActivity;
import com.huassit.imenu.android.util.StringUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 关注列表适配器
 * 
 * @author shang_guan
 * 
 */
public class FollowListAdapter extends AbsAdapter<Follow> {

	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private Animation animation;
	private Timer timer = new Timer();

	/** 当前二层布局是否展开状态 */
	// private boolean isOpen = false;

	public FollowListAdapter(Activity context, int layout) {
		super(context, layout);
		imageLoader = ImageLoader.getInstance();
		options = MyApplication.getDisplayImageOptions(context, 30);
	}

	@Override
	public ViewHolder<Follow> getHolder() {
		return new FollowViewHolder();
	}

	private final class FollowViewHolder implements ViewHolder<Follow> {

		/** 关注头像 */
		private ImageView fans_icon;
		/** 关注名称 */
		private TextView member_name;
		/** 关注数量 */
		private TextView followed_count;
		/** 关注状态0-未关注，1-已关注，2-互相关注，3-自己 */
		private TextView follow_status;
		/** 全局 */
		private RelativeLayout layout_fans_list_item;
		/** 关注状态图片 */
		private ImageView iv_follow;
		/** 关注状态布局 */
		private LinearLayout layout_follow_status;
		private TextView fans_count;
		/** 展开layout */
		private LinearLayout layout_open;
		/** 电话 */
		private TextView phone;
		/** 地址 */
		private TextView address;
		/** 商家的第二层关注按钮 */
		private TextView follow_statu;
		/** 收起按钮 */
		private TextView close;
		/** 关注内容布局 */
		private LinearLayout layout_follow_store_info;

		@Override
		public void initViews(View v, int position) {
			layout_follow_store_info = (LinearLayout) v
					.findViewById(R.id.layout_follow_store_info);
			close = (TextView) v.findViewById(R.id.close);
			follow_statu = (TextView) v.findViewById(R.id.follow_statu);
			address = (TextView) v.findViewById(R.id.address);
			phone = (TextView) v.findViewById(R.id.phone);
			layout_open = (LinearLayout) v.findViewById(R.id.layout_open);
			fans_icon = (ImageView) v.findViewById(R.id.fans_icon);
			fans_count = (TextView) v.findViewById(R.id.fans_count);
			member_name = (TextView) v.findViewById(R.id.member_name);
			followed_count = (TextView) v.findViewById(R.id.followed_count);
			follow_status = (TextView) v.findViewById(R.id.follow_status);
			layout_fans_list_item = (RelativeLayout) v
					.findViewById(R.id.layout_fans_list_item);
			iv_follow = (ImageView) v.findViewById(R.id.iv_follow);
			layout_follow_status = (LinearLayout) v
					.findViewById(R.id.layout_follow_status);
		}

		@Override
		public void updateDatas(final Follow mFollow, int position) {
			fans_count.setVisibility(View.GONE);
			followed_count.setVisibility(View.VISIBLE);
			imageLoader.displayImage(mFollow.store_logo_location
					+ mFollow.store_logo_name, fans_icon, options);
			member_name.setText(mFollow.store_name);
			followed_count.setText(mFollow.address);
			final String statue = FollowListAdapter
					.showStatus(mFollow.follow_status);
			/******************************************************************************/
			follow_status.setText("展开");
			layout_follow_status.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					animation = AnimationUtils.loadAnimation(context,
							R.anim.translate_left);
					layout_open.startAnimation(animation);
					// runTherad(100, layout_follow_store_info,
					// layout_follow_status, layout_open);
					layout_open.setVisibility(View.VISIBLE);
					layout_follow_store_info.setVisibility(View.INVISIBLE);
					layout_follow_status.setVisibility(View.INVISIBLE);
				}
			});
			close.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					animation = AnimationUtils.loadAnimation(context,
							R.anim.translate_right);
					layout_open.startAnimation(animation);
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {

						@Override
						public void run() {
							layout_open.setVisibility(View.INVISIBLE);
							layout_follow_store_info
									.setVisibility(View.VISIBLE);
							layout_follow_status.setVisibility(View.VISIBLE);
						}
					}, 100);
				}
			});

			// 判断关注状态，显示不同的关注图片
			if (statue.equals(StringUtils.getString(context,
					R.string.not_follow))) {
				showOrderedImage(follow_statu, 0);
			} else {
				showOrderedImage(follow_statu, 1);
			}
			// 关注按钮的监听
			follow_statu.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					/** code标石：200-用户，100-商家 */
					((FollowListActivity) context).ChangeStatus("",
							mFollow.store_id,
							FansListAdapter.AddOrCancel(statue), 100);
				}
			});
			address.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 跳转至地图
					Intent intent = new Intent(context, StoreMapActivity.class);
					intent.putExtra(StoreMapActivity.DATA, setStore(mFollow));
					context.startActivity(intent);
				}
			});
			layout_fans_list_item
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							Store mStore1 = setStore(mFollow);
							Intent intent = new Intent(context,
									StoreDetailActivity.class);
							intent.putExtra("store", mStore1);
							context.startActivity(intent);
						}
					});
			phone.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new AlertDialog.Builder(context)
							.setMessage(
									StringUtils.getString(context,
											R.string.sure_to_dial)
											+ mFollow.tel_1 + "?")
							.setPositiveButton(
									StringUtils.getString(context,
											R.string.confirm),
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											Uri uri = Uri.parse("tel:"
													+ mFollow.tel_1);
											Intent intent = new Intent(
													Intent.ACTION_CALL, uri);
											context.startActivity(intent);
										}
									})
							.setNegativeButton(
									StringUtils.getString(context,
											R.string.cancel), null).create()
							.show();
				}
			});
		}

		@Override
		public void doOthers(Follow mFans, int position) {

		}
	}

	/** 显示状态方法 */
	public static String showStatus(String status) {
		String textString = "";
		if (status.equals("0")) {
			textString = StringUtils.getString(context, R.string.not_follow);
		}
		if (status.equals("1")) {
			textString = StringUtils.getString(context, R.string.followed);
		}
		if (status.equals("2")) {
			textString = StringUtils.getString(context,
					R.string.follow_each_other);
		}
		if (status.equals("3")) {
			textString = StringUtils.getString(context, R.string.myself);
		}
		return textString;
	}

	/** 跳转商家 */
	private Store setStore(Follow follow) {
		Store mStore = new Store();
		mStore.followStatus = follow.follow_status;
		mStore.id = follow.store_id;
		mStore.name = follow.store_name;
		mStore.logoLocation = follow.store_logo_location;
		mStore.logoName = follow.store_logo_name;
		mStore.logoDate = follow.store_logo_date;
		mStore.tel1 = follow.tel_1;
		mStore.tel2 = follow.tel_2;
		mStore.address = follow.address;
		mStore.latitude = follow.store_latitude_num;
		mStore.longitude = follow.store_longitude_num;
		return mStore;
	}

	/** 显示已下单的对勾 */
	private void showOrderedImage(TextView view, int status) {
		if (status == 1) {
			// 未关注
			view.setText(context.getResources().getString(R.string.followed));
			Drawable drawable = context.getResources().getDrawable(
					R.drawable.close);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			view.setCompoundDrawables(null, drawable, null, null);
		} else {
			// 已关注
			view.setText(context.getResources().getString(R.string.not_follow));
			Drawable drawable = context.getResources().getDrawable(
					R.drawable.order_white);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			view.setCompoundDrawables(null, drawable, null, null);
		}
	}
}
