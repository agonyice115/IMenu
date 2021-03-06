package com.huassit.imenu.android.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.huassit.imenu.android.R;
import com.huassit.imenu.android.util.StringUtils;

/**
 * Created by Sylar on 14-7-31.
 */
public class LoadingDialog extends Dialog {
	private AnimationDrawable animationDrawable;
	private String message;

	public LoadingDialog(Context context, String message) {
		super(context, R.style.LoadingDialogTheme);
		this.message = message;
		init(context);
	}

	private void init(Context context) {
		setContentView(R.layout.loading_dialog);
		ImageView loadingImageView = (ImageView) findViewById(R.id.loadingImageView);
		TextView tv = (TextView) findViewById(R.id.tv);
		if(!StringUtils.isBlank(message)){
			tv.setText("正在加载...");
		}else{
			tv.setText(message);
		}
		loadingImageView.setImageResource(R.drawable.loading_anim);
		animationDrawable = (AnimationDrawable) loadingImageView.getDrawable();
		animationDrawable.setOneShot(false);
		animationDrawable.start();
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCanceledOnTouchOutside(false);
		setCancelable(false);
//		setContentView(loadingImageView);
	}
}
