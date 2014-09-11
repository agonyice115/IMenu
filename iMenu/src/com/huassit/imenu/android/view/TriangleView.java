package com.huassit.imenu.android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.huassit.imenu.android.util.PreferencesUtils;

/**
 * Created by Sylar on 14-8-1.
 */
public class TriangleView extends ImageView {
    private String colorValue;
    private Paint paint;
    private Path path;
    private int bottom;
    private int top;
    private int right;
    private int left;

    public TriangleView(Context context) {
        super(context);
        init(context);
    }

    public TriangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TriangleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        isInEditMode();
        paint = new Paint();
        path = new Path();
        colorValue = PreferencesUtils.getString(context, "ColorValue");
        left = getWidth() / 2;
        right = getWidth() / 2 + 40;
        top = getTop() - 4;
        bottom = getTop() + 20;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor(colorValue));
        paint.setStyle(Paint.Style.FILL);
        path.moveTo(left, top);
        path.lineTo(right, top);
        path.lineTo((left + right) / 2, bottom);
        path.close();
        canvas.drawPath(path, paint);
    }
}
