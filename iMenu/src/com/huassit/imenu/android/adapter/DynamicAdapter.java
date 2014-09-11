package com.huassit.imenu.android.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huassit.imenu.android.MyApplication;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.model.Dynamic;
import com.huassit.imenu.android.model.FilterDate;
import com.huassit.imenu.android.model.Member;
import com.huassit.imenu.android.model.MemberMenu;
import com.huassit.imenu.android.ui.AddDynamicActivity;
import com.huassit.imenu.android.ui.DynamicActivity;
import com.huassit.imenu.android.ui.DynamicDetailActivity;
import com.huassit.imenu.android.ui.MineActivity;
import com.huassit.imenu.android.util.NumberFormatUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.util.TimeUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DynamicAdapter extends BaseExpandableListAdapter {

	private Context context;
	private LayoutInflater inflater;
	private ImageLoader imageLoader;
	int screenWidth;
	DisplayImageOptions options;
	/** 父项数据 */
	private List<FilterDate> mFilterDatesList;
	/** 个人信息数据 */
	private Member viewMember;
	/** 时间的种类 */
	private String timeType;
	/** 发布状态 */
	private String dynamicType;

	public DynamicAdapter(Context context, String timeType, String dynamicType) {
		this.context = context;
		// this.mFilterDatesList = mFilterDatesList;
		this.timeType = timeType;
		this.dynamicType = dynamicType;
		inflater = LayoutInflater.from(context);
		imageLoader = ImageLoader.getInstance();
		options = MyApplication.getDisplayImageOptions(context, 40);
		DisplayMetrics metric = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(metric);
		screenWidth = metric.widthPixels; // 屏幕宽度（像素）
		mFilterDatesList = new ArrayList<FilterDate>();
	}

	public List<FilterDate> getDataList() {
		return mFilterDatesList;
	}

	@Override
	public void notifyDataSetChanged() {
		mFilterDatesList = getDataList();
		super.notifyDataSetChanged();
	}

	class ViewHolder {
		ImageView memberIcon;
		TextView memberName;
		TextView dynamicDate;
		TextView dynamicTitle;
		ImageView menu1;
		ImageView menu2;
		ImageView menu3;
		TextView menuName1;
		TextView menuName2;
		TextView menuName3;
		TextView store;
		TextView distance;
		TextView goodsCount;
		TextView commentCount;
		RelativeLayout layout_member;
		FrameLayout layout1;
		FrameLayout layout2;
		FrameLayout layout3;
		LinearLayout layout_menu_img;
		/** 动态发布状态显示 */
		private TextView dyanmic_type;
	}

	/************************************* 子项 ***************************************/
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mFilterDatesList.get(groupPosition).mDynamicsList
				.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mFilterDatesList.get(groupPosition).mDynamicsList.size();
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View v, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		if (v == null) {
			v = inflater.inflate(R.layout.dynamic_list_item, null);
			holder.layout_menu_img = (LinearLayout) v
					.findViewById(R.id.layout_menu_img);
			holder.dyanmic_type = (TextView) v.findViewById(R.id.dyanmic_type);
			holder.layout1 = (FrameLayout) v.findViewById(R.id.layout1);
			holder.layout2 = (FrameLayout) v.findViewById(R.id.layout2);
			holder.layout3 = (FrameLayout) v.findViewById(R.id.layout3);
			holder.memberIcon = (ImageView) v.findViewById(R.id.memberInfoIcon);
			holder.memberName = (TextView) v.findViewById(R.id.memberName);
			holder.dynamicDate = (TextView) v.findViewById(R.id.date);
			holder.dynamicTitle = (TextView) v.findViewById(R.id.title);
			holder.menu1 = (ImageView) v.findViewById(R.id.menu1);
			holder.menu2 = (ImageView) v.findViewById(R.id.menu2);
			holder.menu3 = (ImageView) v.findViewById(R.id.menu3);
			holder.menuName1 = (TextView) v.findViewById(R.id.menu1Name);
			holder.menuName2 = (TextView) v.findViewById(R.id.menu2Name);
			holder.menuName3 = (TextView) v.findViewById(R.id.menu3Name);
			FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
					screenWidth / 3, screenWidth / 3);
			holder.menu1.setLayoutParams(layoutParams);
			holder.menu2.setLayoutParams(layoutParams);
			holder.menu3.setLayoutParams(layoutParams);
			holder.store = (TextView) v.findViewById(R.id.storeName);
			holder.distance = (TextView) v.findViewById(R.id.distance);
			holder.goodsCount = (TextView) v.findViewById(R.id.goodsCount);
			holder.commentCount = (TextView) v.findViewById(R.id.commentCount);
			holder.layout_member = (RelativeLayout) v
					.findViewById(R.id.layout_member);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		final Dynamic dynamic = mFilterDatesList.get(groupPosition).mDynamicsList
				.get(childPosition);
		Member memberInfo = dynamic.getMemberInfo();
		imageLoader.displayImage(memberInfo.iconLocation + memberInfo.iconName,
				holder.memberIcon, options);
		holder.memberName.setText(memberInfo.memberName);
		if (TimeUtil.isToday(dynamic.getDynamicDate())) {
			long time = TimeUtil.getTime(dynamic.getDynamicDate(),
					"yyyy-MM-dd HH:mm:ss");
			holder.dynamicDate.setText(TimeUtil.getStrTime(time, "HH:mm"));
		} else {
			holder.dynamicDate.setText(TimeUtil.FormatTime(
					dynamic.getDynamicDate(), "MM/dd"));
		}
		if (((DynamicActivity) context).getMeDynamicType()) {
			holder.dyanmic_type
					.setText(getDynamicType(dynamic.getDynamicType()));
			holder.dyanmic_type.setVisibility(View.VISIBLE);
			holder.dynamicTitle.setPadding(0, 0, 0, 0);
			holder.dynamicTitle.setText(dynamic.getTitle());
		} else {
			if (!StringUtils.isBlank(dynamic.getTitle())) {
				holder.dynamicTitle.setPadding(15, 0, 0, 0);
				holder.dynamicTitle.setText(dynamic.getTitle());
			}
		}
		List<ImageView> mImageViews = new ArrayList<ImageView>();
		mImageViews.add(holder.menu1);
		mImageViews.add(holder.menu2);
		mImageViews.add(holder.menu3);
		List<TextView> mTextViews = new ArrayList<TextView>();
		mTextViews.add(holder.menuName1);
		mTextViews.add(holder.menuName2);
		mTextViews.add(holder.menuName3);
		List<FrameLayout> mFrameLayouts = new ArrayList<FrameLayout>();
		mFrameLayouts.add(holder.layout1);
		mFrameLayouts.add(holder.layout2);
		mFrameLayouts.add(holder.layout3);
		for (int i = 0; i < mFrameLayouts.size(); i++) {
			mFrameLayouts.get(i).setVisibility(View.INVISIBLE);
		}
		showData(dynamic.getMenuList(), mImageViews, mTextViews, mFrameLayouts);
		holder.store.setText(dynamic.getStoreInfo().name);
		if (!StringUtils.isBlank(dynamic.getDistance())) {
			float distance = Integer.valueOf(dynamic.getDistance());
			// 大于1000米的显示km并且小数点后保留一位
			if (distance > 999) {
				Float distanceF = (float) (distance / 1000);
				holder.distance.setText(NumberFormatUtils
						.formatToOne(distanceF) + "km");
			} else {
				holder.distance.setText(((int) distance) + "m");
			}
		}
		holder.goodsCount.setText(dynamic.getGoodsCount());
		holder.commentCount.setText(dynamic.getCommentCount());
		OnClickListener onMenuClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				dynamicType = ((DynamicActivity) context).getDynamicType();
				if (Integer.parseInt(dynamicType) == 1) {
					Intent intent = new Intent(context,
							DynamicDetailActivity.class);
					intent.putExtra("dynamic", dynamic);
					context.startActivity(intent);
				} else {
					Intent intent = new Intent((DynamicActivity)context,
							AddDynamicActivity.class);
					intent.putExtra("dynamic_id", dynamic.getDynamicId());
					System.out.println("dynamic.getDynamicId():"
							+ dynamic.getDynamicId());
					((DynamicActivity)context).startActivityForResult(intent, 1);
				}

			}
		};
		OnClickListener personHomeClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, MineActivity.class);
				intent.putExtra("view_member_id",
						dynamic.getMemberInfo().memberId);
				context.startActivity(intent);
			}
		};
		holder.layout_member.setOnClickListener(personHomeClickListener);
		v.setOnClickListener(onMenuClickListener);
		return v;
	}

	/************************************* 父项 ***************************************/
	@Override
	public Object getGroup(int groupPosition) {
		return getGroup(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return mFilterDatesList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View v,
			ViewGroup parent) {
		FilterDate date = mFilterDatesList.get(groupPosition);
		v = inflater.inflate(R.layout.dynamic_parent_item, null);
		TextView leftTextView = (TextView) v.findViewById(R.id.leftTextView);
		TextView rightTextView = (TextView) v.findViewById(R.id.rightTextView);
		String yearString = "";
		String monthString = "";
		String weekString = "";

		if (((DynamicActivity) context).getTimeType().equals("1")) {
			// 年
			leftTextView.setText(date.key
					+ StringUtils.getString(context, R.string.year));
		}
		if (((DynamicActivity) context).getTimeType().equals("2")) {
			// 月
			yearString = date.key.substring(0, 4);
			monthString = date.key.substring(4, date.key.length());
			leftTextView.setText(yearString
					+ StringUtils.getString(context, R.string.year)
					+ monthString
					+ StringUtils.getString(context, R.string.month));
		}
		if (((DynamicActivity) context).getTimeType().equals("3")) {
			// 周
			yearString = date.key.substring(0, date.key.length() - 2);
			weekString = date.key.substring(date.key.length() - 2,
					date.key.length());
			leftTextView.setText(yearString
					+ StringUtils.getString(context, R.string.year)
					+ weekString
					+ StringUtils.getString(context, R.string.week));
		}
		rightTextView.setText(StringUtils.getString(context, R.string.total)
				+ date.value + StringUtils.getString(context, R.string.count));
		return v;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	/** 父项是否可点击 */
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	/** 显示数据 */
	private void showData(List<MemberMenu> memberMenusList,
			List<ImageView> mImageViews, List<TextView> mTextViews,
			List<FrameLayout> mFrameLayouts) {
		if (memberMenusList != null && memberMenusList.size() > 0) {
			for (int i = 0; i < memberMenusList.size(); i++) {
				if (i < 3) {
					imageLoader.displayImage(
							memberMenusList.get(i).image_location
									+ memberMenusList.get(i).image_name,
							mImageViews.get(i));
					mTextViews.get(i).setText(memberMenusList.get(i).menu_name);
					mTextViews.get(i)
							.setBackgroundColor(
									context.getResources().getColor(
											R.color.half_black));
					mFrameLayouts.get(i).setVisibility(View.VISIBLE);
				}
			}
		}
	}

	/** 返回动态发布状态 */
	private String getDynamicType(String type) {
		if (type.equals("1")) {
			return StringUtils.getString(context, R.string.published);
		}
		if (type.equals("2")) {
			return StringUtils.getString(context, R.string.not_published);
		}
		return StringUtils.getString(context, R.string.delete);
	}
}
