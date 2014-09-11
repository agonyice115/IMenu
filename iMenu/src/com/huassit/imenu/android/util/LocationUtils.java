package com.huassit.imenu.android.util;

import android.content.Context;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;

/**
 * Created by Sylar on 14-7-1.
 */
public class LocationUtils {
    private static LocationUtils instance = null;
    private static BDLocation mLocation;
    private Context mContext;
    private static LocationClient mLocationClient;

    private BDLocationListener mLocationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            mLocation = bdLocation;
        }

        @Override
        public void onReceivePoi(BDLocation bdLocation) {

        }
    };

    private LocationUtils(Context context) {
        mContext = context;
        mLocationClient = new LocationClient(mContext);
        mLocationClient.registerLocationListener(mLocationListener);
        mLocationClient.start();
    }

    public static synchronized LocationUtils getInstance(Context context) {
        if (instance == null) {
            instance = new LocationUtils(context);
        }
        return instance;
    }

    public BDLocation getLocation() {
        if (mLocation == null) {
            mLocation = mLocationClient.getLastKnownLocation();
        }
        return mLocation;
    }

    public void requestLocation() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.requestLocation();
        }
    }

    public void stop() {
        mLocationClient.unRegisterLocationListener(mLocationListener);
        mLocationClient.stop();
    }
}
