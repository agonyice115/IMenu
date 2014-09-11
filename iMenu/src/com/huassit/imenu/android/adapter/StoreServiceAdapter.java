package com.huassit.imenu.android.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huassit.imenu.android.R;
import com.huassit.imenu.android.model.Service;
import com.nostra13.universalimageloader.core.ImageLoader;

public class StoreServiceAdapter extends AbsAdapter<Service> {

	private ImageLoader imageLoader;

	public StoreServiceAdapter(Activity context, int layout) {
		super(context, layout);
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public ViewHolder<Service> getHolder() {
		return new ServiceViewHolder();
	}

	class ServiceViewHolder implements ViewHolder<Service> {

		TextView serviceName;
		ImageView serviceImage;

		@Override
		public void initViews(View v, int position) {
			serviceName = (TextView) v.findViewById(R.id.serviceName);
			serviceImage = (ImageView) v.findViewById(R.id.serviceImage);
		}

		@Override
		public void updateDatas(Service t, int position) {
			serviceName.setText(t.serviceName);
			try {
				// System.out.println("t.service_image:"+t.service_image);
				imageLoader.displayImage(t.service_image, serviceImage);
			} catch (OutOfMemoryError e) {
				
			}
		}

		@Override
		public void doOthers(Service t, int position) {

		}

	}

}
