package com.huassit.imenu.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huassit.imenu.android.MyApplication;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.model.Dynamic;
import com.huassit.imenu.android.model.DynamicMessageList;
import com.huassit.imenu.android.model.Member;
import com.huassit.imenu.android.ui.DynamicDetailActivity;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.util.TimeUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 消息列表适配器
 * 
 * @author 上官明月
 * 
 */
public class DynamicMessageListAdapter extends AbsAdapter<DynamicMessageList> {

	private Context context;
	private ImageLoader imageLoader;
	private DisplayImageOptions options_member;

	public DynamicMessageListAdapter(Activity context, int layout) {
		super(context, layout);
		this.context = context;
		imageLoader = ImageLoader.getInstance();
		options_member = MyApplication.getDisplayImageOptions(context, 30);
	}

	@Override
	public ViewHolder<DynamicMessageList> getHolder() {
		return new MessageViewHolder();
	}

	private final class MessageViewHolder implements
			ViewHolder<DynamicMessageList> {

		/** 用户头像 */
		private ImageView member_img;
		/** 用户昵称 */
		// private TextView member_name;
		/** 消息内容 */
		private TextView dynamic_message_content;
		/** 消息时间 */
		private TextView dynamic_message_time;
		/** 动态图片 */
		private ImageView dynamic_img;
		/** 全局 */
		private RelativeLayout layout_dynamic_message_item;

		@Override
		public void initViews(View v, int position) {
			member_img = (ImageView) v.findViewById(R.id.member_img);
			dynamic_img = (ImageView) v.findViewById(R.id.dynamic_img);
			// member_name = (TextView) v.findViewById(R.id.member_name);
			dynamic_message_content = (TextView) v
					.findViewById(R.id.dynamic_message_content);
			dynamic_message_time = (TextView) v
					.findViewById(R.id.dynamic_message_time);
			layout_dynamic_message_item = (RelativeLayout) v
					.findViewById(R.id.layout_dynamic_message_item);
		}

		@Override
		public void updateDatas(final DynamicMessageList mDynamicMessageList,
				int position) {
			// 消息类型(1-动态赞/2-动态评论/3-图片赞/4-图片评论)
			if (mDynamicMessageList.message_type.equals("2")
					|| mDynamicMessageList.message_type.equals("4")) {
				dynamic_message_content
						.setText(mDynamicMessageList.member_info.memberName
								+"  "+ StringUtils.getString(context,
										R.string.give_you_comment)
								+ mDynamicMessageList.message_content);
			} else {
				dynamic_message_content
						.setText(mDynamicMessageList.member_info.memberName
								+"  "+ StringUtils.getString(context,
										R.string.give_you_like));
			}
			imageLoader.displayImage(
					mDynamicMessageList.member_info.iconLocation
							+ mDynamicMessageList.member_info.iconName,
					member_img, options_member);
			imageLoader.displayImage(mDynamicMessageList.image_location
					+ mDynamicMessageList.image_name, dynamic_img);
			// member_name.setText(mDynamicMessageList.member_info.memberName);
			if (TimeUtil.isToday(mDynamicMessageList.date_added)) {
				long time = TimeUtil.getTime(mDynamicMessageList.date_added,
						"yyyy-MM-dd HH:mm:ss");
				dynamic_message_time
						.setText(TimeUtil.getStrTime(time, "HH:mm"));
			} else {
				dynamic_message_time.setText(TimeUtil.FormatTime(
						mDynamicMessageList.date_added, "MM/dd"));
			}
			layout_dynamic_message_item
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Intent intent = new Intent(context,
									DynamicDetailActivity.class);
							intent.putExtra(
									"dynamic",
									getDynamic(
											mDynamicMessageList.dynamic_id,
											mDynamicMessageList.member_info.token));
							context.startActivity(intent);
						}
					});
		}

		@Override
		public void doOthers(DynamicMessageList mFans, int position) {

		}
	}

	private Dynamic getDynamic(String dynamicId, String token) {
		Dynamic dynamic = new Dynamic();
		dynamic.setDynamicId(dynamicId);
		Member member = new Member();
		member.memberId = PreferencesUtils.getString(context, "member_id");
		member.token = token;
		dynamic.setMemberInfo(member);
		return dynamic;
	}
}
