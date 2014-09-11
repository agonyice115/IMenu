package com.huassit.imenu.android.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.model.Menu;
import com.huassit.imenu.android.util.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Sylar on 14-7-17.
 */
public class ThumbnailView extends FrameLayout {
	private Context mContext;
	private int index;
	private Menu menu;
	private ImageView thumbnailImageView;
	private ImageView deleteImageView;
	private TextView title;
	private ThumbnailViewClickCallback callback;
	private ImageLoader imageLoader;

	public ThumbnailView(Context context) {
		super(context);
		init(context);
	}

	public ThumbnailView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ThumbnailView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		this.mContext = context;
		imageLoader = ImageLoader.getInstance();
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.picture_thumbnail_view, null);
		thumbnailImageView = (ImageView) view
				.findViewById(R.id.thumbnailImageView);
		title = (TextView) view.findViewById(R.id.title);
		deleteImageView = (ImageView) view.findViewById(R.id.deleteImageView);
		deleteImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				thumbnailImageView.setImageBitmap(null);
			}
		});
		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setSelected(true);
				if (callback != null) {
					callback.onThumbnailClick((ThumbnailView) v);
				}
			}
		});
		addView(view);
	}

	public void setThumbnailImage(Bitmap bitmap) {
		thumbnailImageView.setImageBitmap(bitmap);
	}

	public void setMenuInfo(Menu menuInfo) {
		if (menuInfo != null) {
			this.menu = menuInfo;
			title.setText(menuInfo.menu_name);
			if (!StringUtils.isBlank(menu.menu_image_location)) {
				System.out.println("menuInfo.menu_image_location:"+menuInfo.menu_image_location);
				imageLoader.displayImage(menuInfo.menu_image_location
						+ menuInfo.menu_image_name, thumbnailImageView);
			}
		}
	}

	public void setSelected(boolean selected) {
		if (selected) {
			thumbnailImageView
					.setBackgroundResource(R.drawable.defult_red_background_stroke);
		} else {
			thumbnailImageView.setBackgroundColor(Color.TRANSPARENT);
		}
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public Menu getMenuInfo() {
		return menu;
	}

	public void setThumbnailViewClickCallback(
			ThumbnailViewClickCallback callback) {
		this.callback = callback;
	}

	public static interface ThumbnailViewClickCallback {
		public void onThumbnailClick(ThumbnailView thumbnailView);
	}
}
