package com.huassit.imenu.android.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.model.Service;
import com.huassit.imenu.android.ui.StoreCategoryActivity;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Sylar on 14-7-15.
 */
public class ServiceListAdapter extends AbsAdapter<Service> {

    private ImageLoader imageLoader;
    public int currentIndex =-1;

    public ServiceListAdapter(Activity context, int layout) {
        super(context, layout);
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public ViewHolder<Service> getHolder() {
        return new ServiceViewHolder();
    }
    
//    @Override
//    public void notifyDataSetChanged() {
//    	super.notifyDataSetChanged();
//    	currentIndex=((StoreCategoryActivity)context).getIndex();
//    }

    private class ServiceViewHolder implements ViewHolder<Service> {
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
        public void updateDatas(Service service, int position) {
            imageLoader.displayImage(service.thumb_image, icon);
            title.setText(service.serviceName);
            rightTriangle.setVisibility(View.GONE);
//            currentIndex =Integer.valueOf(PreferencesUtils.getString(context, "ServiceCurrentIndex"));
            System.out.println("currentIndex:"+currentIndex);
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
        public void doOthers(Service service, int position) {

        }
    }
}
