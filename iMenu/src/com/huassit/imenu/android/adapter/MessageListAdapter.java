package com.huassit.imenu.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huassit.imenu.android.R;
import com.huassit.imenu.android.model.Message;
import com.huassit.imenu.android.ui.MessageDetailActivity;

/**
 * 消息列表适配器
 * 
 * @author 上官明月
 * 
 */
public class MessageListAdapter extends AbsAdapter<Message> {
	private Context context;

	public MessageListAdapter(Activity context, int layout) {
		super(context, layout);
		this.context = context;
	}

	@Override
	public ViewHolder<Message> getHolder() {
		return new MessageViewHolder();
	}

	private final class MessageViewHolder implements ViewHolder<Message> {

		/** new */
		private TextView new_tv;
		/** 消息时间 */
		private TextView date_modified;
		/** 消息摘要 */
		private TextView summary;
		/** 全局 */
		private RelativeLayout layout_message_item;

		@Override
		public void initViews(View v, int position) {
			new_tv = (TextView) v.findViewById(R.id.new_tv);
			date_modified = (TextView) v.findViewById(R.id.date_modified);
			summary = (TextView) v.findViewById(R.id.summary);
			layout_message_item = (RelativeLayout) v
					.findViewById(R.id.layout_message_item);
		}

		@Override
		public void updateDatas(final Message message, int position) {
			if (message.is_open.equals("0")) {
				new_tv.setVisibility(View.VISIBLE);
			} else {
				new_tv.setVisibility(View.INVISIBLE);
			}
			date_modified.setText(message.date_modified);
			summary.setText(message.summary);
			layout_message_item.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(context,
							MessageDetailActivity.class);
					intent.putExtra("system_message_id",
							message.system_message_id);
					context.startActivity(intent);
					new_tv.setVisibility(View.INVISIBLE);
				}
			});
		}

		@Override
		public void doOthers(Message mFans, int position) {

		}
	}
}
