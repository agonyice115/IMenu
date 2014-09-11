package com.huassit.imenu.android.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huassit.imenu.android.MyApplication;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.model.Fans;
import com.huassit.imenu.android.ui.FansListActivity;
import com.huassit.imenu.android.ui.FollowListActivity;
import com.huassit.imenu.android.ui.MineActivity;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.ScreenUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * 粉丝列表适配器
 * 
 * @author shang_guan
 * 
 */
public class FansListAdapter extends AbsAdapter<Fans> {

	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	/** 从哪个activity进来，1-粉丝界面/2-关注界面 */
	private int activityCode;

	/***
	 * 
	 * @param context
	 * @param layout
	 * @param activityCode从哪个activity进来
	 *            ，1-粉丝界面/2-关注界面
	 */
	public FansListAdapter(Activity context, int layout, int activityCode) {
		super(context, layout);
		this.activityCode = activityCode;
		imageLoader = ImageLoader.getInstance();
		options = MyApplication.getDisplayImageOptions(context, 30);
	}

	@Override
	public ViewHolder<Fans> getHolder() {
		return new FansViewHolder();
	}

	private final class FansViewHolder implements ViewHolder<Fans> {

		/** 粉丝头像 */
		private ImageView fans_icon;
		/** 粉丝名称 */
		private TextView member_name;
		/** 粉丝数量 */
		private TextView followed_count;
		/** 粉丝关注状态:0-未关注，1-已关注，2-互相关注，3-自己 */
		private TextView follow_status;
		/** 全局 */
		private RelativeLayout layout_fans_list_item;
		/** 关注状态图片 */
		private ImageView iv_follow;
		/** 关注状态布局 */
		private LinearLayout layout_follow_status;
		private TextView fans_count;

		@Override
		public void initViews(View v, int position) {
			fans_icon = (ImageView) v.findViewById(R.id.fans_icon);
			member_name = (TextView) v.findViewById(R.id.member_name);
			fans_count = (TextView) v.findViewById(R.id.fans_count);
			followed_count = (TextView) v.findViewById(R.id.followed_count);
			follow_status = (TextView) v.findViewById(R.id.follow_status);
			layout_fans_list_item = (RelativeLayout) v
					.findViewById(R.id.layout_fans_list_item);
			iv_follow = (ImageView) v.findViewById(R.id.iv_follow);
			layout_follow_status = (LinearLayout) v
					.findViewById(R.id.layout_follow_status);
		}

		@Override
		public void updateDatas(final Fans mFans, int position) {
			followed_count.setVisibility(View.GONE);
			fans_count.setVisibility(View.VISIBLE);
			layout_follow_status.setVisibility(View.VISIBLE);
			if (PreferencesUtils.getString(context, "token") == null
					|| PreferencesUtils.getString(context, "token").equals("")) {
				layout_follow_status.setVisibility(View.INVISIBLE);
			}
			imageLoader.displayImage(mFans.iconLocation + mFans.iconName,
					fans_icon, options);
			member_name.setText(mFans.member_name);
			fans_count.setText(mFans.followed_count);
			final String statue = FollowListAdapter
					.showStatus(mFans.follow_status);
			if (statue.equals(StringUtils.getString(context, R.string.not_follow))) {
				iv_follow.setImageResource(R.drawable.order);
			} else {
				iv_follow.setImageResource(R.drawable.icon_follow);
			}
			follow_status.setText(statue);
			if (statue.equals(StringUtils.getString(context, R.string.myself))) {
				layout_follow_status.setVisibility(View.INVISIBLE);
			}
			if (!statue.equals(StringUtils.getString(context, R.string.myself))) {
				layout_follow_status
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								if (activityCode == 1) {
									// 粉丝界面
									((FansListActivity) context).ChangeStatus(
											mFans.member_id,
											AddOrCancel(statue));
								} else {
									/** code标石：200-用户，100-商家 */
									((FollowListActivity) context)
											.ChangeStatus(mFans.member_id, "",
													AddOrCancel(statue), 200);
								}
							}
						});
			}
			layout_fans_list_item
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(context,
									MineActivity.class);
							intent.putExtra("view_member_id", mFans.member_id);
							intent.putExtra("view_member_name",
									mFans.member_name);
							context.startActivity(intent);
						}
					});
		}

		@Override
		public void doOthers(Fans mFans, int position) {

		}
	}

	/** 更改状态请求是添加或取消 */
	public static String AddOrCancel(String statue) {
		String following_status = "";
		if (statue.equals(StringUtils.getString(context, R.string.not_follow))) {
			following_status = "1";
		}
		if (statue.equals(StringUtils.getString(context, R.string.followed))) {
			following_status = "0";
		}
		if (statue.equals(StringUtils.getString(context, R.string.follow_each_other))) {
			following_status = "0";
		}
		return following_status;
	}
}
