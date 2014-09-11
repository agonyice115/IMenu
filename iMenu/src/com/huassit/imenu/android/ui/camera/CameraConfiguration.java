package com.huassit.imenu.android.ui.camera;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.util.Log;
import com.huassit.imenu.android.util.ScreenUtils;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Sylar on 14-7-17.
 */
public class CameraConfiguration {
    private static final String TAG = CameraConfiguration.class.getSimpleName();
    private static final Pattern COMMA_PATTERN = Pattern.compile(",");
    private Context context;
    private ScreenUtils.ScreenResolution screenResolution;
    private Point cameraResolution;

    public CameraConfiguration(Context context) {
        this.context = context;
    }

    void initCameraParameters(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        screenResolution = ScreenUtils.getScreenResolution(context);
        Log.e(TAG, "Screen resolution: " + screenResolution);
        cameraResolution = getCameraResolution(parameters, screenResolution);
        Log.e(TAG, "Camera resolution: " + cameraResolution);
    }

    void setDesiredCameraParameters(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        parameters.setPreviewSize(cameraResolution.x, cameraResolution.y);
        setPictureSize(parameters);
        setDisplayOrientation(camera, 0);
        camera.setParameters(parameters);
    }

    private void setPictureSize(Camera.Parameters parameters) {
        List<Camera.Size> supportedPictureSizes = parameters.getSupportedPictureSizes();
        Collections.sort(supportedPictureSizes, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size lhs, Camera.Size rhs) {
                if (lhs.width * lhs.height > rhs.width * rhs.height) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
        if (supportedPictureSizes != null && supportedPictureSizes.size() > 0) {
            Camera.Size size = supportedPictureSizes.get(0);
            parameters.setPictureSize(size.width, size.height);
        }
    }

    /**
     * 获取相机预览分辨率
     *
     * @param parameters
     * @param screenResolution
     * @return
     */
    private static Point getCameraResolution(Camera.Parameters parameters, ScreenUtils.ScreenResolution screenResolution) {

        String previewSizeValueString = parameters.get("preview-size-values");
        if (previewSizeValueString == null) {
            previewSizeValueString = parameters.get("preview-size-value");
        }

        Point cameraResolution = null;

        if (previewSizeValueString != null) {
            Log.d(TAG, "preview-size-values parameter: " + previewSizeValueString);
            cameraResolution = findBestPreviewSizeValue(previewSizeValueString, screenResolution);
        }

        if (cameraResolution == null) {
            cameraResolution = new Point(
                    (screenResolution.getWidth() >> 3) << 3,
                    (screenResolution.getHeight() >> 3) << 3);
        }

        return cameraResolution;
    }

    /**
     * 根据屏幕分辨率大小获取最适合的预览分辨率
     *
     * @param previewSizeValueString
     * @param screenResolution
     * @return
     */
    private static Point findBestPreviewSizeValue(CharSequence previewSizeValueString, ScreenUtils.ScreenResolution screenResolution) {
        int bestX = 0;
        int bestY = 0;
        int diff = Integer.MAX_VALUE;
        for (String previewSize : COMMA_PATTERN.split(previewSizeValueString)) {

            previewSize = previewSize.trim();
            int dimPosition = previewSize.indexOf('x');
            if (dimPosition < 0) {
                Log.e(TAG, "Bad preview-size: " + previewSize);
                continue;
            }

            int newX;
            int newY;
            try {
                newX = Integer.parseInt(previewSize.substring(0, dimPosition));
                newY = Integer.parseInt(previewSize.substring(dimPosition + 1));
            } catch (NumberFormatException nfe) {
                Log.e(TAG, "Bad preview-size: " + previewSize);
                continue;
            }

            int newDiff = Math.abs(newX - screenResolution.getWidth()) + Math.abs(newY - screenResolution.getHeight());
            if (newDiff == 0) {
                bestX = newX;
                bestY = newY;
                break;
            } else if (newDiff < diff) {
                bestX = newX;
                bestY = newY;
                diff = newDiff;
            }

        }

        if (bestX > 0 && bestY > 0) {
            return new Point(bestX, bestY);
        }
        return null;
    }

    /**
     * 兼容Android SDK 1.6
     *
     * @param camera
     * @param angle
     */
    private void setDisplayOrientation(Camera camera, int angle) {
        Method downPolymorphic;
        try {
            downPolymorphic = camera.getClass().getMethod("setDisplayOrientation", new Class[]{int.class});
            if (downPolymorphic != null)
                downPolymorphic.invoke(camera, new Object[]{angle});
        } catch (Exception ignored) {
        }
    }
}
