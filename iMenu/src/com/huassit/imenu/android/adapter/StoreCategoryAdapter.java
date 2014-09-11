package com.huassit.imenu.android.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.model.Category;
import com.huassit.imenu.android.ui.StoreCategoryActivity;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Sylar on 14-6-30.
 */
public class StoreCategoryAdapter extends AbsAdapter<Category> {

    private ImageLoader imageLoader;
    public int currentIndex = -1;
    
    public StoreCategoryAdapter(Activity context, int layout) {
        super(context, layout);
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public ViewHolder<Category> getHolder() {
        return new CategoryViewHolder();
    }

    private final class CategoryViewHolder implements ViewHolder<Category> {
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
        public void updateDatas(Category category, int position) {
        	if(!StringUtils.isBlank(category.category_image)){
        		icon.setVisibility(View.VISIBLE);
        		imageLoader.displayImage(category.category_image, icon);
        	}else{
        		icon.setVisibility(View.GONE);
        	}
            title.setText(category.category_name);
            if (category.level.equals(StoreCategoryActivity.PARENT_LEVEL) && category.children.size() > 0) {
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
        public void doOthers(Category category, int position) {

        }
    }
}
