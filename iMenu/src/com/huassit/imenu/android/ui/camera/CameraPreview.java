package com.huassit.imenu.android.ui.camera;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;

/**
 * Created by Sylar on 14-7-17.
 */
public class CameraPreview extends SurfaceView {
    private CameraManager manager;

    public CameraPreview(Context context) {
        super(context);
        init(context);
    }

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CameraPreview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        manager = CameraManager.getCameraManager(context);
    }
}
