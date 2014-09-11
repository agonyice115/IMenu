package com.huassit.imenu.android.ui.camera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;

import java.util.List;

/**
 * Created by Sylar on 14-7-17.
 */
public class FlashlightManager {
    public static final String FLASH_MODE_AUTO = Camera.Parameters.FLASH_MODE_AUTO;
    public static final String FLASH_MODE_ON = Camera.Parameters.FLASH_MODE_ON;
    public static final String FLASH_MODE_OFF = Camera.Parameters.FLASH_MODE_OFF;

    private static FlashlightManager flashlightManager = null;
    private Context context;
    private Camera camera;

    private FlashlightManager(Context context) {
        this.context = context;
    }

    public static FlashlightManager getFlashlightManager(Context context) {
        if (flashlightManager == null) {
            synchronized (FlashlightManager.class) {
                if (flashlightManager == null) {
                    flashlightManager = new FlashlightManager(context);
                }
            }
        }
        return flashlightManager;
    }

    private boolean hasFlashlight() {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public void initFlashlight(Camera camera) {
        this.camera = camera;
    }

    public boolean isFlashlightEnable() {
        boolean enabled = false;
        if (hasFlashlight()) {
            List<String> supportedFlashModes = camera.getParameters().getSupportedFlashModes();
            if (!(supportedFlashModes == null
                    || supportedFlashModes.isEmpty()
                    || supportedFlashModes.size() == 1
                    && supportedFlashModes.get(0).equals(Camera.Parameters.FLASH_MODE_OFF))) {
                enabled = true;
            }
        }
        return enabled;
    }

    /**
     * flashlightMode :
     *
     * @param flashlightMode
     */
    public void setFlashlight(String flashlightMode) {
        try {
            if (isFlashlightEnable()) {
                Camera.Parameters parameters = camera.getParameters();
                parameters.setFlashMode(flashlightMode);
                camera.setParameters(parameters);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
