package com.huassit.imenu.android.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.model.AreaCode;

/**
 * Created by Sylar on 14-7-3.
 */
public class AreaCodeAdapter extends AbsAdapter<AreaCode> {
    public AreaCodeAdapter(Activity context, int layout) {
        super(context, layout);
    }

    @Override
    public ViewHolder<AreaCode> getHolder() {
        return new AreaCodeViewHolder();
    }

    private final class AreaCodeViewHolder implements ViewHolder<AreaCode> {

        private ImageView imageView;
        private TextView title;

        @Override
        public void initViews(View v, int position) {
            imageView = (ImageView) v.findViewById(R.id.imageView);
            title = (TextView) v.findViewById(R.id.title);
        }

        @Override
        public void updateDatas(AreaCode areaCode, int position) {
            if (areaCode.isSelected) {
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
            }
            title.setText(areaCode.area_code_name);
        }

        @Override
        public void doOthers(AreaCode areaCode, int position) {

        }
    }
}
