package com.huassit.imenu.android.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.huassit.imenu.android.MyApplication;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.model.Store;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Sylar on 14-6-28.
 */
public class SearchStoreAdapter extends AbsAdapter<Store> {

    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public SearchStoreAdapter(Activity context, int layout) {
        super(context, layout);
        imageLoader = ImageLoader.getInstance();
        options = MyApplication.getDisplayImageOptions(context, 40);
    }

    @Override
    public ViewHolder<Store> getHolder() {
        return new StoreViewHolder();
    }

    private class StoreViewHolder implements ViewHolder<Store> {

        private ImageView storeIcon;
        private TextView storeName;
        private TextView storeAddress;
        private TextView storeDistance;

        @Override
        public void initViews(View v, int position) {
            storeIcon = (ImageView) v.findViewById(R.id.icon);
            storeName = (TextView) v.findViewById(R.id.storeName);
            storeAddress = (TextView) v.findViewById(R.id.storeAddress);
            storeDistance = (TextView) v.findViewById(R.id.storeDistance);
        }

        @Override
        public void updateDatas(Store store, int position) {
            storeName.setText(store.name);
            storeAddress.setText(store.address);
            storeDistance.setText(context.getString(R.string.store_distance_label, store.distance));
            imageLoader.displayImage(store.logoLocation + store.logoName, storeIcon, options);
        }

        @Override
        public void doOthers(Store store, int position) {

        }
    }
}
