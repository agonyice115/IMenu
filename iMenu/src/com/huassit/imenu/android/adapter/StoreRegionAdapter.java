package com.huassit.imenu.android.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.model.Region;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Sylar on 14-7-1.
 */
public class StoreRegionAdapter extends AbsAdapter<Region> {
    private ImageLoader imageLoader;
    public int currentIndex = -1;

    public StoreRegionAdapter(Activity context, int layout) {
        super(context, layout);
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public ViewHolder<Region> getHolder() {
        return new CategoryViewHolder();
    }

    private final class CategoryViewHolder implements ViewHolder<Region> {
        private TextView title;
        private ImageView rightTriangle;
        private ImageView icon;
        private ImageView confirm;
        private RelativeLayout rl_view; 

        @Override
        public void initViews(View v, int position) {
            icon = (ImageView) v.findViewById(R.id.icon);
            title = (TextView) v.findViewById(R.id.title);
            rightTriangle = (ImageView) v.findViewById(R.id.rightTriangle);
            confirm =(ImageView) v.findViewById(R.id.confirm);
            rl_view =(RelativeLayout) v.findViewById(R.id.rl_view);
        }

        @Override
        public void updateDatas(Region region, int position) {
        	if(!StringUtils.isBlank(region.region_image)){
        		icon.setVisibility(View.VISIBLE);
        		imageLoader.displayImage(region.region_image, icon);
        	}else{
        		icon.setVisibility(View.GONE);
        	}
            title.setText(region.region_name);
            if (region.children != null && region.children.size() > 0) {
                rightTriangle.setVisibility(View.VISIBLE);
            } else {
                rightTriangle.setVisibility(View.GONE);
            }
            if (currentIndex == position) {
    			title.setSelected(true);
    			confirm.setVisibility(View.VISIBLE);
    			rl_view.setBackgroundColor(Color.parseColor(PreferencesUtils
    					.getString(context, "ColorValue")));
    		} else {
    			title.setSelected(false);
    			confirm.setVisibility(View.GONE);
    			rl_view.setBackgroundColor(context.getResources().getColor(
    					R.color.gray));
    		}
        }

        @Override
        public void doOthers(Region region, int position) {

        }
    }
}
