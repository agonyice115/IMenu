package com.huassit.imenu.android.adapter;

import java.util.ArrayList;
import java.util.zip.Inflater;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huassit.imenu.android.R;
import com.huassit.imenu.android.adapter.AbsAdapter.ViewHolder;
import com.huassit.imenu.android.model.MenuCategory;
import com.huassit.imenu.android.util.PreferencesUtils;

public class MenuCategoryAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<MenuCategory> cateList = new ArrayList<MenuCategory>();
	private LayoutInflater inflater;
	public int currentIndex = -1;

	public MenuCategoryAdapter(Activity context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	public ArrayList<MenuCategory> getDataList() {
		return cateList;
	}

	@Override
	public int getCount() {
		return cateList.size();
	}

	@Override
	public Object getItem(int position) {
		return cateList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Hold h = new Hold();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.menu_category_list_item,
					null);
			h.categoryName = (TextView) convertView
					.findViewById(R.id.menu_category);
//			h.choosen = (ImageView) convertView.findViewById(R.id.choosen);
			convertView.setTag(h);
		} else {
			h = (Hold) convertView.getTag();
		}
		h.categoryName.setText(cateList.get(position).getMenu_category_name());
		if (currentIndex == position) {
			h.categoryName.setSelected(true);
//			h.choosen.setVisibility(View.VISIBLE);
			convertView.setBackgroundColor(Color.parseColor(PreferencesUtils
					.getString(context, "ColorValue")));
		} else {
			h.categoryName.setSelected(false);
//			h.choosen.setVisibility(View.GONE);
			convertView.setBackgroundColor(context.getResources().getColor(
					R.color.gray));
		}
		return convertView;
	}

	private class Hold {
		TextView categoryName;
//		ImageView choosen;
	}

}
