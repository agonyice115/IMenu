package com.huassit.imenu.android.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.model.ClientSkin;

/**
 * Created by Sylar on 14-7-11.
 */
public class ColorView extends FrameLayout {

    private ClientSkin mClientSkin;
    private Context mContext;
    private ImageView mColorImageView;
    private ImageView mSelectedImageView;
    private OnColorSelectedListener mListener;

    public ColorView(Context context) {
        super(context);
        init(context);
    }

    public ColorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ColorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        View colorView = LayoutInflater.from(mContext).inflate(R.layout.theme_color_view, null);
        mColorImageView = (ImageView) colorView.findViewById(R.id.colorImageView);
        mSelectedImageView = (ImageView) colorView.findViewById(R.id.selectImageView);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelected(true);
                if (mListener != null) {
                    mListener.onColorSelected((ColorView) v, mClientSkin);
                }
            }
        });
        addView(colorView);
    }

    public void setClientSkin(ClientSkin skin) {
        this.mClientSkin = skin;
        if (mClientSkin != null) {
            mColorImageView.setBackgroundColor(Color.parseColor(mClientSkin.client_skin_value));
        }
    }

    public void setSelected(boolean isSelected) {
        if (isSelected) {
            mSelectedImageView.setVisibility(View.VISIBLE);
        } else {
            mSelectedImageView.setVisibility(View.GONE);
        }
    }

    public void setOnColorSelectedListener(OnColorSelectedListener clickListener) {
        this.mListener = clickListener;
    }

    public static interface OnColorSelectedListener {
        public void onColorSelected(ColorView view, ClientSkin selectedSkin);
    }
}
