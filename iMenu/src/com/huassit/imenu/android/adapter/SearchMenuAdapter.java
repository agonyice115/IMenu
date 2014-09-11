package com.huassit.imenu.android.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.huassit.imenu.android.MyApplication;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.model.Menu;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Sylar on 14-6-28.
 */
public class SearchMenuAdapter extends AbsAdapter<Menu> {
    private DisplayImageOptions options;

    public SearchMenuAdapter(Activity context, int layout) {
        super(context, layout);
        options = MyApplication.getDisplayImageOptions(context, 40);
    }

    @Override
    public ViewHolder<Menu> getHolder() {
        return new MenuViewHolder();
    }

    private class MenuViewHolder implements ViewHolder<Menu> {
        private ImageView menuIcon;
        private TextView menuName;
        private TextView menuPrice;
        private TextView menuCount;

        @Override
        public void initViews(View v, int position) {
            menuIcon = (ImageView) v.findViewById(R.id.menuIcon);
            menuName = (TextView) v.findViewById(R.id.menuName);
            menuPrice = (TextView) v.findViewById(R.id.menuPrice);
            menuCount = (TextView) v.findViewById(R.id.menuCount);
        }

        @Override
        public void updateDatas(Menu menu, int position) {
            ImageLoader.getInstance().displayImage(menu.menu_image_location + menu.menu_image_name, menuIcon, options);
            menuName.setText(menu.menu_name);
            menuPrice.setText(context.getString(R.string.search_result_menu_price, menu.menu_price));
            menuCount.setText(context.getString(R.string.search_result_menu_count, menu.menu_count));
        }

        @Override
        public void doOthers(Menu menu, int position) {

        }
    }
}
