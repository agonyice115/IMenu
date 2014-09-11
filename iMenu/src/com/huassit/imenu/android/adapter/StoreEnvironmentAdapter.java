package com.huassit.imenu.android.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huassit.imenu.android.R;
import com.huassit.imenu.android.model.Environment;
import com.huassit.imenu.android.model.Service;
import com.nostra13.universalimageloader.core.ImageLoader;

public class StoreEnvironmentAdapter extends AbsAdapter<Environment>{

	private ImageLoader imageLoader;
	
	public StoreEnvironmentAdapter(Activity context, int layout) {
		super(context, layout);
		imageLoader =ImageLoader.getInstance();
	}

	@Override
	public ViewHolder<Environment> getHolder() {
		return new EnvironmentViewHolder();
	}
	
	class EnvironmentViewHolder implements ViewHolder<Environment>{

		TextView name;
		ImageView image;
		@Override
		public void initViews(View v, int position) {
			name =(TextView) v.findViewById(R.id.serviceName);
			image =(ImageView) v.findViewById(R.id.serviceImage);
		}

		@Override
		public void updateDatas(Environment t, int position) {
			name.setText(t.environment_name);
			imageLoader.displayImage(t.environment_image, image);
			
		}

		@Override
		public void doOthers(Environment t, int position) {
			
		}
		
	}

}
