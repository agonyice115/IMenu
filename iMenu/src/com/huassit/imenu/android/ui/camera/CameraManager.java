package com.huassit.imenu.android.ui.camera;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Sylar on 14-7-17.
 */
public class CameraManager {
    private static CameraManager manager = null;
    private Camera camera;
    private CameraConfiguration cameraConfiguration;
    private FlashlightManager flashlightManager;
    private AtomicBoolean isInitialized = new AtomicBoolean(false);
    private AtomicBoolean isReviewing = new AtomicBoolean(false);

    public static interface TakePictureCallback {
        public void onPictureTaken(byte[] data);
    }

    private CameraManager(Context context) {
        cameraConfiguration = new CameraConfiguration(context);
        flashlightManager = FlashlightManager.getFlashlightManager(context);
    }

    public static CameraManager getCameraManager(Context context) {
        if (manager == null) {
            synchronized (CameraManager.class) {
                if (manager == null) {
                    manager = new CameraManager(context);
                }
            }
        }
        return manager;
    }

    public void openCamera(SurfaceHolder holder) {
        try {
            camera = Camera.open();
            camera.setPreviewDisplay(holder);
            if (!isInitialized.getAndSet(true)) {
                cameraConfiguration.initCameraParameters(camera);
            }
            cameraConfiguration.setDesiredCameraParameters(camera);
            flashlightManager.initFlashlight(camera);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startPreview() {
        if (camera != null && !isReviewing.get()) {
            camera.startPreview();
            isReviewing.set(true);
        }
    }

    public void stopPreview() {
        if (camera != null && isReviewing.get()) {
            camera.stopPreview();
            isReviewing.set(false);
        }
    }

    public void closeCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    public Camera getCamera() {
        return camera;
    }

    public void takePicture(final TakePictureCallback callback) {
        camera.takePicture(new Camera.ShutterCallback() {
            @Override
            public void onShutter() {

            }
        }, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                callback.onPictureTaken(data);
            }
        });
    }

    public boolean isFlashlightEnable() {
        return flashlightManager.isFlashlightEnable();
    }

    /**
     * flashlightMode :
     * FlashlightManager.FLASH_MODE_AUTO
     * FlashlightManager.FLASH_MODE_ON
     * FlashlightManager.FLASH_MODE_OFF
     *
     * @param flashlightMode
     */
    public void setFlashlightMode(String flashlightMode) {
        flashlightManager.setFlashlight(flashlightMode);
    }
}
