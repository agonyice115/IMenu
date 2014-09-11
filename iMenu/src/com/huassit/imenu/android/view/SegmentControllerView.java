package com.huassit.imenu.android.view;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.huassit.imenu.android.R;
import com.huassit.imenu.android.util.PreferencesUtils;

import java.util.ArrayList;

/**
 * Created by Sylar on 14-8-2.
 */
public class SegmentControllerView extends LinearLayout {
    public static int START_ID = 3829;
    private Context context;
    private DisplayMetrics displayMetrics;
    private int buttonCount;
    private int strokeColor;
    private int strokeWidth;
    private int corner;
    private int originColor;
    private int onPressedColor;
    private int textOriginColor;
    private int textOnPressedColor;
    private ArrayList<Button> buttonArr;
    private int buttonHeight;
    private int textSize;
    private int position = 0;
    private OnChangeListener mOnChangeListener;
    private RadioButton temp;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            mOnChangeListener.onChange();
        }
    };

    public SegmentControllerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        if (attrs == null) {
            setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }
        String colorValue = PreferencesUtils.getString(context, "ColorValue");
        setOrientation(HORIZONTAL);
        setBackgroundColor(getResources().getColor(android.R.color.transparent));
        buttonArr = new ArrayList<Button>();
        // Default values can change it by init***() methods;
        //边线颜色
        strokeColor = Color.parseColor(colorValue);
        //原始颜色
        originColor = getResources().getColor(android.R.color.white);
        //按钮按下的颜色
        onPressedColor = Color.parseColor(colorValue);
        //文字颜色
        textOriginColor = strokeColor;
        //按钮按下后，文字颜色
        textOnPressedColor = originColor;
        strokeWidth = 1;
        corner = 5;
        buttonHeight = -1;
        textSize =10;

        // Get ScreenMetrics and put it into displayMetrics
        // Translating dp-unit to px-unit depends on it
        displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    }

    // like setOnClickListener()
    public void setOnChangeListener(OnChangeListener mOnChangeListener) {
        this.mOnChangeListener = mOnChangeListener;
    }

    public void init(String[] str) {
        this.buttonCount = str.length;
        for (int i = 0; i < buttonCount; i++) {
        	final RadioButton temp = new RadioButton(context);
            buttonArr.add(temp);
            temp.setId(START_ID + i);
            temp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position = temp.getId() - START_ID;
                    handler.sendEmptyMessage(0);
                    draw();
                }
            });
            temp.setText(str[i]);
            temp.setTextSize(12);
            addView(temp);
        }
    }
    
    public void setText(String[] str){
    	for(int i=0;i<buttonArr.size();i++){
    		buttonArr.get(i).setText(str[i]);
    	}
    }


    // init height of button
    public void initButtonHeight(int dp) {
        buttonHeight = transDpToPx(dp);
        for (int i = 0; i < buttonCount; i++) {
            buttonArr.get(i).setHeight(buttonHeight);
        }
    }
    
//    //set text size
//    public void setTextSize(int dp){
//    	textSize = transDpToPx(dp);
//    	for()
//    }


    public void setColor(int newColor) {
        this.onPressedColor = newColor;
        this.textOriginColor = newColor;
        this.strokeColor = newColor;
        draw();
    }

    @SuppressWarnings("deprecation")
    public void draw() {

        float[] radiiLeft = {
                transDpToPx(corner), transDpToPx(corner),
                0, 0, 0, 0,
                transDpToPx(corner), transDpToPx(corner)
        };
        float[] radiiRight = {
                0, 0,
                transDpToPx(corner), transDpToPx(corner),
                transDpToPx(corner), transDpToPx(corner),
                0, 0
        };

        for (int i = 0; i < buttonCount; i++) {

            GradientDrawable temp = new GradientDrawable();
            temp.setStroke(transDpToPx(strokeWidth), strokeColor);
            temp.setColor(originColor);
            if (i == position) {

                buttonArr.get(i).setTextColor(textOnPressedColor);

                temp.setColor(onPressedColor);
                if (i == 0) {
                    temp.setCornerRadii(radiiLeft);
                    buttonArr.get(i).setBackgroundDrawable(temp);
                } else if (i == buttonCount - 1) {
                    temp.setCornerRadii(radiiRight);
                    buttonArr.get(i).setBackgroundDrawable(temp);
                } else {
                    buttonArr.get(i).setBackgroundDrawable(temp);
                }
                continue;
            } else {

                buttonArr.get(i).setTextColor(textOriginColor);
                if (i == 0) {
                    temp.setCornerRadii(radiiLeft);
                    buttonArr.get(i).setBackgroundDrawable(temp);
                    continue;
                } else if (i == buttonCount - 1) {
                    temp.setCornerRadii(radiiRight);
                    buttonArr.get(i).setBackgroundDrawable(temp);
                    continue;
                } else
                    buttonArr.get(i).setBackgroundDrawable(temp);
            }
        }
    }

    public int getPosition() {
        return position;
    }

    private class RadioButton extends Button {
        public RadioButton(Context context) {
            super(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
                    LayoutParams.MATCH_PARENT);
            params.weight = 1;
            setLayoutParams(params);
        }
    }

    private int transDpToPx(int dp) {
        return (displayMetrics.densityDpi / 160) * dp;
    }

    public void setPosition(int target) {
        position = target;
        handler.sendEmptyMessage(0);
        draw();
    }

    public interface OnChangeListener {
        void onChange();
    }
}
