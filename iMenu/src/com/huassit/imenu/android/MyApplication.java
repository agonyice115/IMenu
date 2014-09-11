package com.huassit.imenu.android;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import com.baidu.mapapi.SDKInitializer;
import com.huassit.imenu.android.model.Cart;
import com.huassit.imenu.android.util.LocationUtils;
import com.huassit.imenu.android.util.ScreenUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.MemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.Collection;
import java.util.Map;

/**
 * Created by Sylar on 14-6-24.
 */
public class MyApplication extends Application {

	public static HttpPost httpPost = null;
	public static DefaultHttpClient httpClient = null;
	public Map<String, Cart> cartMap;

	@Override
	public void onCreate() {
		super.onCreate();
		LocationUtils.getInstance(this.getApplicationContext());
		SDKInitializer.initialize(this.getApplicationContext());
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisk(true)
				.resetViewBeforeLoading(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.EXACTLY)
				.showImageOnLoading(R.drawable.defult_menu_img_big)
				.showImageForEmptyUri(R.drawable.defult_menu_img_big)
				.showImageOnFail(R.drawable.defult_menu_img_big).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.defaultDisplayImageOptions(defaultOptions)
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
//				.memoryCache(new WeakMemoryCache())
				.memoryCacheSize(2 * 1024 * 1024)
				.threadPoolSize(3)
				.diskCache(
						new UnlimitedDiscCache(getExternalFilesDir("/cache")))
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheExtraOptions(480, 320, null).writeDebugLogs().build();
		ImageLoader.getInstance().init(config);
	}

	public static DisplayImageOptions getDisplayImageOptions(Context context,
			int r) {
		return new DisplayImageOptions.Builder()
				.displayer(
						new RoundedBitmapDisplayer(ScreenUtils.dpToPxInt(
								context, r))).cacheInMemory(true)
				.cacheOnDisk(true).resetViewBeforeLoading(true)
				.showImageOnLoading(R.drawable.defult_headimg_small)
				.showImageOnFail(R.drawable.defult_headimg_small)
				.showImageForEmptyUri(R.drawable.defult_headimg_small)
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

	}

	/**
	 * 获取购物车map，集合
	 */
	public Map<String, Cart> getCartMap() {
		return cartMap;
	}

	/**
	 * 存储购物车map集合
	 */
	public void setCartMap(Map<String, Cart> cartMap) {
		this.cartMap = cartMap;
	}

	/**
	 * 根据商家ID，获取本地存储的购物车数据
	 */
	public Cart getAppCart(String storeId) {
		Cart cart = new Cart();
		if (getCartMap() != null && getCartMap().size() > 0) {
			cart = getCartMap().get(storeId);
		} else {
			return null;
		}
		return cart;
	}

	/**
	 * 清空本地map存储购物车内容
	 */
	public void DeleteCartMap() {
		getCartMap().clear();
	}
}
