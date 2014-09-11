package com.huassit.imenu.android.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.model.ClientSkin;

/**
 * Created by Sylar on 14-7-11.
 */
public class ClientSkinListAdapter extends AbsAdapter<ClientSkin> {
    public ClientSkinListAdapter(Activity context, int layout) {
        super(context, layout);
    }

    @Override
    public ViewHolder<ClientSkin> getHolder() {
        return new SkinViewHolder();
    }

    private final class SkinViewHolder implements ViewHolder<ClientSkin> {
        private ImageView colorView;
        private TextView colorName;
        private ImageView selectedImage;

        @Override
        public void initViews(View v, int position) {
            colorName = (TextView) v.findViewById(R.id.colorName);
            colorView = (ImageView) v.findViewById(R.id.colorImageView);
            selectedImage = (ImageView) v.findViewById(R.id.selectImageView);
        }

        @Override
        public void updateDatas(ClientSkin skin, int position) {
            colorName.setText(skin.client_skin_name);
            colorView.setBackgroundColor(Color.parseColor(skin.client_skin_value));
            if (skin.isSelected) {
                selectedImage.setVisibility(View.VISIBLE);
            } else {
                selectedImage.setVisibility(View.GONE);
            }
        }

        @Override
        public void doOthers(ClientSkin skin, int position) {

        }
    }
}
