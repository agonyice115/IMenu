package com.huassit.imenu.android.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huassit.imenu.android.R;
import com.huassit.imenu.android.model.MemberMenu;
import com.nostra13.universalimageloader.core.ImageLoader;


public class MenuFromMemberAdapter extends AbsAdapter<MemberMenu>{

	private ImageLoader imageLoader;
	
	public MenuFromMemberAdapter(Activity context, int layout) {
		super(context, layout);
		imageLoader =ImageLoader.getInstance();
	}

	@Override
	public com.huassit.imenu.android.adapter.AbsAdapter.ViewHolder<MemberMenu> getHolder() {
		
		return new MenuFromMemberViewHolder();
	}

	class MenuFromMemberViewHolder implements ViewHolder<MemberMenu>{

		ImageView menuImg;
		TextView likeCount;
		TextView commentCount;
		TextView date;
		@Override
		public void initViews(View v, int position) {
			menuImg =(ImageView) v.findViewById(R.id.menu_img);
			likeCount =(TextView) v.findViewById(R.id.like_count);
			commentCount =(TextView) v.findViewById(R.id.comment_count);
			date =(TextView) v.findViewById(R.id.date);
		}

		@Override
		public void updateDatas(MemberMenu t, int position) {
			imageLoader.clearMemoryCache();
			imageLoader.displayImage(t.image_location+t.image_name, menuImg);
			likeCount.setText(t.goods_count);
			commentCount.setText(t.comment_count);
			date.setText(t.member_menu_image_date);
		}

		@Override
		public void doOthers(MemberMenu t, int position) {
		}
		
	}
}
