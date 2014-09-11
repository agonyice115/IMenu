package com.huassit.imenu.android.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huassit.imenu.android.R;
import com.huassit.imenu.android.model.Menu;
import com.nostra13.universalimageloader.core.ImageLoader;

/***
 * 店长推荐菜单列表适配器
 * 
 * @author shang_guan
 * 
 */
public class RecommandMenuListAdapter extends AbsAdapter<Menu> {

	ImageLoader imageLoader;

	public RecommandMenuListAdapter(Activity context, int layout) {
		super(context, layout);
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public com.huassit.imenu.android.adapter.AbsAdapter.ViewHolder<Menu> getHolder() {
		return new RecommandMenuViewHolder();
	}

	class RecommandMenuViewHolder implements ViewHolder<Menu> {

		ImageView menu_pic;
		TextView menu_name;
		// TextView menu_conut;
		TextView menu_pirce;

		@Override
		public void initViews(View v, int position) {
			menu_pic = (ImageView) v.findViewById(R.id.menu_pic);
			menu_name = (TextView) v.findViewById(R.id.menu_name);
			// menu_conut = (TextView) v.findViewById(R.id.menu_conut);
			menu_pirce = (TextView) v.findViewById(R.id.menu_pirce);
		}

		@Override
		public void updateDatas(final Menu t, final int position) {
			imageLoader.displayImage(t.menu_image_location + t.menu_image_name,
					menu_pic);
			menu_name.setText(t.menu_name);
			// menu_conut.setText(t.menu_count+"");
			menu_pirce.setText("￥" + t.menu_price);
		}

		@Override
		public void doOthers(Menu t, int position) {

		}
	}
}
