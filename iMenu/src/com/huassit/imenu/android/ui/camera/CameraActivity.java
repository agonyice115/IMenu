package com.huassit.imenu.android.ui.camera;

import android.R.integer;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.MyApplication;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.model.Menu;
import com.huassit.imenu.android.ui.LoginActivity;
import com.huassit.imenu.android.util.ImageUtils;
import com.huassit.imenu.android.util.ScreenUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.view.ThumbnailView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sylar on 14-7-17.
 */
public class CameraActivity extends BaseActivity implements
		SurfaceHolder.Callback, View.OnClickListener {

	public static final String NEED_TAKE_PIC_COUNT = "com.huassit.imenu.android.ui.camera.NEED_TAKE_PIC_COUNT";
	public static final String DATA = "com.huassit.imenu.android.ui.camera.DATA";
	public static final String ACTION = "com.huassit.imenu.android.ui.camera.ACTION";
	private CameraPreview cameraPreview;
	private CameraManager cameraManager;
	private LinearLayout thumbnailLayout;
	private TextView menuName;
	private int currentIndex = 0;
//	private List<Bitmap> bitmapList;
	// 拍照后存放MenuId，照片存放在系统相册的文件名
	private Map<String, String> thumbnailMap = new HashMap<String, String>();
	/** 闪光灯控制 */
	private TextView flashlightMode;
	/** 闪光灯状态 */
	private int flashLightStatus;
	private ImageView thumbnailImageView;
	private ImageLoader imageLoader;

	// private Handler handler;
	private Bitmap bitmap;

	@Override
	public int getContentView() {
		return R.layout.camera_activity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initView() {
		// initHandler();
		cameraPreview = (CameraPreview) findViewById(R.id.cameraPreview);
		imageLoader = ImageLoader.getInstance();
		cameraPreview.getHolder().addCallback(this);
		cameraPreview.getHolder().setType(
				SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		cameraManager = CameraManager.getCameraManager(this);
		findViewById(R.id.takePhoto).setOnClickListener(this);
		findViewById(R.id.backImageView).setOnClickListener(this);
		findViewById(R.id.done).setOnClickListener(this);
		findViewById(R.id.picFromAlbum).setOnClickListener(this);
		flashlightMode = (TextView) findViewById(R.id.flashlightMode);
		flashlightMode.setOnClickListener(this);
		cameraManager.setFlashlightMode(FlashlightManager.FLASH_MODE_AUTO);
		flashLightStatus = 2;
		changeFlashlightImg(2);
		menuName = (TextView) findViewById(R.id.menuName);
		thumbnailLayout = (LinearLayout) findViewById(R.id.imageThumbnailLayout);
//		bitmapList = new ArrayList<Bitmap>();
		List<Menu> menuList = (List<Menu>) getIntent().getSerializableExtra(
				NEED_TAKE_PIC_COUNT);
		if (menuList != null && menuList.size() > 0) {
			for (int i = 0; i < menuList.size(); i++) {
				final Menu menu = menuList.get(i);
				ThumbnailView view = new ThumbnailView(this);
				view.setIndex(i);
				view.setMenuInfo(menu);
				thumbnailImageView = (ImageView) view
						.findViewById(R.id.thumbnailImageView);
				view.setThumbnailViewClickCallback(new ThumbnailView.ThumbnailViewClickCallback() {
					@Override
					public void onThumbnailClick(ThumbnailView thumbnailView) {
						currentIndex = thumbnailView.getIndex();
						menuName.setText(thumbnailView.getMenuInfo().menu_name);
						for (int i = 0; i < thumbnailLayout.getChildCount(); i++) {
							ThumbnailView childView = (ThumbnailView) thumbnailLayout
									.getChildAt(i);
							if (childView != thumbnailView) {
								childView.setSelected(false);
							}
						}
					}
				});
				if(!StringUtils.isBlank(menu.menu_image_location)){
					thumbnailMap.put(menu.menu_id, "1");
				}
				else{
					thumbnailMap.put(menu.menu_id, null);
				}
				thumbnailLayout.addView(view);
				if (i == 0) {
					menuName.setText(menu.menu_name);
					thumbnailImageView
							.setBackgroundResource(R.drawable.defult_red_background_stroke);
				}
			}
		}

//		if (getIntent().getExtras().getSerializable("ThumbnailMap") != null) {
//			thumbnailMap = (Map<String, String>) getIntent().getExtras()
//					.getSerializable("ThumbnailMap");
//			for (String filePath : thumbnailMap.values()) {
//				if (filePath != null) {
//					if (!StringUtils.isBlank(filePath)) {
//						Bitmap bitmap = ImageUtils.getScaleBitmap(filePath,
//								ScreenUtils.dpToPxInt(context, 80),
//								ScreenUtils.dpToPxInt(context, 80));
//						displayThumbnail(bitmap, filePath);
//						bitmapList.add(bitmap);
//					}
//				}
//			}
//			currentIndex = 0;
//		}
	}

	@Override
	public void initData() {
		// bitmapList = new ArrayList<Bitmap>();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		cameraManager.openCamera(holder);
		cameraManager.startPreview();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		cameraManager.stopPreview();
		cameraManager.closeCamera();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.takePhoto:
			try {
				cameraManager
						.takePicture(new CameraManager.TakePictureCallback() {
							@Override
							public void onPictureTaken(byte[] data) {
								cameraManager.stopPreview();
								Bitmap bitmap = ImageUtils.getScaleBitmap(data,
										640, 640);
								// Bitmap bitmap =
								// BitmapFactory.decodeByteArray(data, 0,
								// data.length);
								int height = bitmap.getHeight();
								float scaleSize = (float) (640.00 / height);
								Matrix matrix = new Matrix();
								matrix.setScale(scaleSize, scaleSize);
								Bitmap scaledBitmap = Bitmap.createBitmap(
										bitmap, 0, 0, bitmap.getWidth(),
										bitmap.getHeight(), matrix, true);
								if (!bitmap.isRecycled()) {
									bitmap.recycle();
								}
								int x = (scaledBitmap.getWidth() - 640) / 2;
								Bitmap newBitmap = Bitmap.createBitmap(
										scaledBitmap, x, 0, 640, 640);
								if (!scaledBitmap.isRecycled()) {
									scaledBitmap.recycle();
								}
								saveThumbnail(newBitmap);
								cameraManager.startPreview();
							}
						});
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.backImageView:
			finish();
			break;
		case R.id.picFromAlbum:
			Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
			getAlbum.setType("image/*");
			startActivityForResult(getAlbum, 10);
			break;
		case R.id.done:
			// Intent intent = new
			// Intent(CameraActivity.this,AddDynamicActivity.class);
			Intent intent = new Intent(ACTION);
			intent.putExtra(DATA, (Serializable) thumbnailMap);
			// if
			// ("PictureDetails".equals(getIntent().getStringExtra("sourceActivity")))
			// {
			// startActivity(intent);
			// finish();
			// } else {
			// setResult(RESULT_OK, intent);
			// finish();
			// }
			finish();
			sendBroadcast(intent);
			break;
		case R.id.flashlightMode:
			// 0开启1关闭2自动
			if (flashLightStatus == 0) {
				flashLightStatus = 1;
				changeFlashlightImg(1);
				return;
			}
			if (flashLightStatus == 2) {
				flashLightStatus = 0;
				changeFlashlightImg(0);
				return;
			}
			if (flashLightStatus == 1) {
				flashLightStatus = 2;
				changeFlashlightImg(2);
				return;
			}
			break;
		}
	}

	// 保存文件到系统相册中
	private void saveThumbnail(Bitmap mBitmap) {
		String uri = MediaStore.Images.Media.insertImage(getContentResolver(),
				mBitmap, "", "");
		if (!mBitmap.isRecycled()) {
			mBitmap.recycle();
		}
		String filePath = getFilePathByContentResolver(Uri.parse(uri));
		if (!StringUtils.isBlank(filePath)) {
			Bitmap bitmap = ImageUtils.getScaleBitmap(filePath,
					ScreenUtils.dpToPxInt(context, 80),
					ScreenUtils.dpToPxInt(context, 80));
			displayThumbnail(bitmap, filePath);
//			bitmapList.add(bitmap);
		}
	}

	private void displayThumbnail(Bitmap bitmap, String filePath) {
		if (currentIndex < thumbnailLayout.getChildCount()) {
			ThumbnailView view = (ThumbnailView) thumbnailLayout
					.getChildAt(currentIndex);
			view.setThumbnailImage(bitmap);
			thumbnailMap.put(view.getMenuInfo().menu_id, filePath);
			currentIndex++;
			ThumbnailView view_next = (ThumbnailView) thumbnailLayout
					.getChildAt(currentIndex);
			if (null != view_next
					&& !StringUtils.isBlank(view_next.getMenuInfo().menu_name)) {
				menuName.setText(view_next.getMenuInfo().menu_name);
				for (int i = 0; i < thumbnailLayout.getChildCount(); i++) {
					if (thumbnailLayout.getChildAt(i) == view_next) {
						thumbnailLayout.getChildAt(i).setSelected(true);
					} else {
						thumbnailLayout.getChildAt(i).setSelected(false);
					}
				}
			}
		}
	}

	private String getFilePathByContentResolver(Uri uri) {
		if (null == uri) {
			return null;
		}
		Cursor c = context.getContentResolver().query(uri, null, null, null,
				null);
		String filePath = null;
		try {
			if (c != null && c.moveToFirst()) {
				filePath = c.getString(c
						.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
			}
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return filePath;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Uri originalUri = data.getData();
			// ContentResolver resolver = getContentResolver();
			// Bitmap bitmap = MediaStore.Images.Media.getBitmap(resolver,
			// originalUri);
			String filePath = getFilePathByContentResolver(originalUri);
			if (!StringUtils.isBlank(filePath)) {
				Bitmap bitmap = ImageUtils.getScaleBitmap(filePath,
						ScreenUtils.dpToPxInt(context, 80),
						ScreenUtils.dpToPxInt(context, 80));
				displayThumbnail(bitmap, filePath);
//				bitmapList.add(bitmap);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/** 改变闪光灯图片:0开启1关闭2自动 */
	private void changeFlashlightImg(int Mode) {
		switch (Mode) {
		case 0:
			// 开启
			cameraManager.setFlashlightMode(FlashlightManager.FLASH_MODE_ON);
			flashlightMode.setText("开启");
			Drawable drawable = context.getResources().getDrawable(
					R.drawable.camera_on);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			flashlightMode.setCompoundDrawables(null, drawable, null, null);
			break;
		case 1:
			// 关闭
			cameraManager.setFlashlightMode(FlashlightManager.FLASH_MODE_OFF);
			flashlightMode.setText("关闭");
			Drawable drawable1 = context.getResources().getDrawable(
					R.drawable.camera_off);
			drawable1.setBounds(0, 0, drawable1.getMinimumWidth(),
					drawable1.getMinimumHeight());
			flashlightMode.setCompoundDrawables(null, drawable1, null, null);
			break;
		case 2:
			// 自动
			cameraManager.setFlashlightMode(FlashlightManager.FLASH_MODE_AUTO);
			flashlightMode.setText("自动");
			Drawable drawable2 = context.getResources().getDrawable(
					R.drawable.camera_auto);
			drawable2.setBounds(0, 0, drawable2.getMinimumWidth(),
					drawable2.getMinimumHeight());
			flashlightMode.setCompoundDrawables(null, drawable2, null, null);
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		if (bitmapList.size() > 0) {
//			for (Bitmap bitmap : bitmapList) {
//				if (bitmap != null && !bitmap.isRecycled()) {
//					bitmap.recycle();
//				}
//			}
//		}
//		bitmapList.clear();
//		bitmapList = null;
	}
}
