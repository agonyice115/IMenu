package com.huassit.imenu.android.ui;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ZoomControls;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.MyApplication;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.model.Store;
import com.huassit.imenu.android.view.NavigationView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by Sylar on 14-7-19.
 */
public class StoreMapActivity extends BaseActivity {
    public static final String DATA = "com.huassit.imenu.android.STORE_DATA";
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private NavigationView mNavigationView;
    private DisplayImageOptions options;

    @Override
    public int getContentView() {
        return R.layout.store_map_activity;
    }

    @Override
    public void initView() {
        mMapView = (MapView) findViewById(R.id.bMapView);
        mNavigationView = (NavigationView) findViewById(R.id.navigationView);
        mNavigationView.setNavigateItemVisibility(NavigationView.TITLE, View.GONE);
        mNavigationView.setNavigateItemVisibility(NavigationView.FIRST_RIGHT_TEXT_VIEW, View.GONE);
        mNavigationView.setNavigateItemText(NavigationView.LEFT_TEXT_VIEW, R.string.store_position_title);
        mNavigationView.setNavigateItemVisibility(NavigationView.LEFT_IMAGE_VIEW, View.GONE);
        mNavigationView.setNavigateItemBackground(NavigationView.SECOND_RIGHT_TEXT_VIEW, R.drawable.close);
        mNavigationView.setNavigateItemOnClickListener(NavigationView.SECOND_RIGHT_TEXT_VIEW, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        options = MyApplication.getDisplayImageOptions(context, 30);
        initBaiDuMap();
    }

    private void initBaiDuMap() {
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(false);
        mBaiduMap.setTrafficEnabled(false);
        mBaiduMap.getUiSettings().setAllGesturesEnabled(true);
        mBaiduMap.getUiSettings().setCompassEnabled(false);
        mBaiduMap.getUiSettings().setRotateGesturesEnabled(false);
        mBaiduMap.getUiSettings().setZoomGesturesEnabled(true);
        mBaiduMap.getUiSettings().setOverlookingGesturesEnabled(false);

        for (int i = 0; i < mMapView.getChildCount(); i++) {
            View child = mMapView.getChildAt(i);
            if (child instanceof ZoomControls) {
                child.setVisibility(View.INVISIBLE);
            }
        }
        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                Store store = (Store) getIntent().getSerializableExtra(DATA);
                addMarker(store);
            }
        });
    }

    private void animateMapView(LatLng location) {
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(location, 14);
        mBaiduMap.animateMapStatus(update);
    }

    @Override
    public void initData() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    private void addMarker(final Store store) {
        if (store == null) return;
        final View logoView = LayoutInflater.from(this).inflate(R.layout.store_logo_on_map_view, null);
        ImageView logoImage = (ImageView) logoView.findViewById(R.id.storeLogo);
        TextView indexText = (TextView) logoView.findViewById(R.id.storeIndex);
        indexText.setVisibility(View.GONE);
        ImageLoader.getInstance().displayImage(store.logoLocation + store.logoName, logoImage, options, new SimpleImageLoadingListener() {

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(Double.valueOf(store.latitude), Double.valueOf(store.longitude)));
                markerOptions.icon(BitmapDescriptorFactory.fromView(logoView));
                Overlay overlay = mBaiduMap.addOverlay(markerOptions);
                overlay.setVisible(true);
                animateMapView(markerOptions.getPosition());
            }
        });
    }
}
