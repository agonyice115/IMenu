package com.huassit.imenu.android.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.renren.Renren;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.model.API;
import com.huassit.imenu.android.model.Share;
import com.huassit.imenu.android.ui.DynamicActivity;
import com.huassit.imenu.android.ui.MainActivity;
import com.huassit.imenu.android.ui.MineActivity;
import com.huassit.imenu.android.ui.SearchActivity;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.StringUtils;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Sylar on 14-6-27.
 */
public class NavigationView extends RelativeLayout implements View.OnClickListener, PlatformActionListener {
    private static final long interval = 3 * 1000L;

    private static final int DISMISS_MESSAGE = 2000;

    private static final String MESSAGE_INFO_TYPE = "11";
    private static final String MESSAGE_ERROR_TYPE = "12";

    public static final int LEFT_TEXT_VIEW = 1000;
    public static final int LEFT_IMAGE_VIEW = 1001;
    public static final int TITLE = 1002;
    public static final int FIRST_RIGHT_TEXT_VIEW = 1003;
    public static final int SECOND_RIGHT_TEXT_VIEW = 1004;

    private static final int CATEGORY_VIEW = 1006;
    private static final int NAVIGATE_VIEW = 1011;
    private static final int SHARE_VIEW = 1012;

    public static final int TRIANGLE_MINE = 1007;
    public static final int TRIANGLE_ACTIVITIES = 1008;
    public static final int TRIANGLE_STORE = 1009;
    public static final int TRIANGLE_SEARCH = 1010;

    private Context mContext;
    private Activity mActivity;
    public static TextView mLeftTextView;
    private ImageView mLeftImageView;
    private TextView mTitleView;
    private TextView mFirstRightTextView;
    private TextView mSecondRightTextView;
    private LinearLayout mCategoryLayout;
    private ImageView mMineTriangle;
    private ImageView mActivitiesTriangle;
    private ImageView mStoreTriangle;
    private ImageView mSearchTriangle;
    private LinearLayout mShareView;
    private LinearLayout mLeftTextViewLayout;
    private LinearLayout mFirstRightTextViewLayout;
    private LinearLayout mSecondRightTextViewLayout;
    private LinearLayout mMessageLayout;
    private TextView mMessageTextView;
    private ImageView mMessageIcon;
    private Share mShare;
    private Timer timer;
    private PopupWindow popupWindow;
    /**
     * 动态右上角小红点
     */
    private TextView dynamic_red;
    private View childView;

    public NavigationView(Context context) {
        super(context);
        this.mContext = context;
        this.mActivity = (Activity) mContext;
        init();
    }

    public NavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.mActivity = (Activity) mContext;
        init();
    }

    public NavigationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        this.mActivity = (Activity) mContext;
        init();
    }

    private void init() {
        timer = new Timer();
        childView = LayoutInflater.from(mContext).inflate(R.layout.navigation_bar, null);
        mLeftTextView = (TextView) childView.findViewById(R.id.leftTextView);
        mLeftTextViewLayout = (LinearLayout) childView.findViewById(R.id.leftTextViewLayout);
        mLeftImageView = (ImageView) childView.findViewById(R.id.leftImageView);
        mTitleView = (TextView) childView.findViewById(R.id.title);
        mFirstRightTextView = (TextView) childView.findViewById(R.id.firstRightTextView);
        mFirstRightTextViewLayout = (LinearLayout) childView.findViewById(R.id.firstRightTextViewLayout);
        mSecondRightTextView = (TextView) childView.findViewById(R.id.secondRightTextView);
        mSecondRightTextViewLayout = (LinearLayout) childView.findViewById(R.id.secondRightTextViewLayout);
        mCategoryLayout = (LinearLayout) childView.findViewById(R.id.navigate_category);
        mMineTriangle = (ImageView) childView.findViewById(R.id.mineTriangle);
        mActivitiesTriangle = (ImageView) childView.findViewById(R.id.activitiesTriangle);
        dynamic_red = (TextView) childView.findViewById(R.id.dynamic_red);
        mStoreTriangle = (ImageView) childView.findViewById(R.id.storeTriangle);
        mSearchTriangle = (ImageView) childView.findViewById(R.id.searchTriangle);
        mShareView = (LinearLayout) childView.findViewById(R.id.share_view);

        childView.findViewById(R.id.mineLayout).setOnClickListener(this);
        childView.findViewById(R.id.activitiesLayout).setOnClickListener(this);
        childView.findViewById(R.id.storeLayout).setOnClickListener(this);
        childView.findViewById(R.id.searchLayout).setOnClickListener(this);
        childView.findViewById(R.id.closeLayout).setOnClickListener(this);
        childView.findViewById(R.id.shareCloseLayout).setOnClickListener(this);
        childView.findViewById(R.id.weChat).setOnClickListener(this);
        childView.findViewById(R.id.weChatMoment).setOnClickListener(this);
        childView.findViewById(R.id.sinaWeibo).setOnClickListener(this);
        childView.findViewById(R.id.renren).setOnClickListener(this);
        setCurrentCategoryTriangle(TRIANGLE_STORE);
        addView(childView);
        String colorValue = PreferencesUtils.getString(mContext, "ColorValue", "");
        if (!StringUtils.isBlank(colorValue)) {
            setBackgroundColor(Color.parseColor(colorValue));
        }

        View popWindowView = LayoutInflater.from(mContext).inflate(R.layout.navigation_pop_window_layaout, null);
        mMessageLayout = (LinearLayout) popWindowView.findViewById(R.id.messageLayout);
        mMessageTextView = (TextView) popWindowView.findViewById(R.id.message);
        mMessageIcon = (ImageView) popWindowView.findViewById(R.id.messageIcon);
        popupWindow = new PopupWindow(popWindowView, LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
    }

    public void showErrorMessage(String errorMessage) {
        mMessageTextView.setText(errorMessage);
        mMessageTextView.setTextColor(getResources().getColor(R.color.orange));
        mMessageLayout.setBackgroundResource(R.drawable.error_message_bg);
        mMessageIcon.setImageResource(R.drawable.ic_error_msg);
//        mMessageLayout.setVisibility(View.VISIBLE);
        try {
            popupWindow.showAsDropDown(childView);
        } catch (Exception ignored) {
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(DISMISS_MESSAGE);
            }
        }, interval);
    }

    public void showInfoMessage(String infoMessage) {
        mMessageTextView.setText(infoMessage);
        mMessageTextView.setTextColor(getResources().getColor(R.color.green));
        mMessageLayout.setBackgroundResource(R.drawable.info_message_bg);
        mMessageIcon.setImageResource(R.drawable.ic_info_msg);
//        mMessageLayout.setVisibility(View.VISIBLE);
        try {
            popupWindow.showAsDropDown(childView);
        } catch (Exception ignored) {
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(DISMISS_MESSAGE);
            }
        }, interval);
    }

    public void showMessage(String message) {
        if (message.contains("|")) {
            String[] messages = message.split("\\|");
            if (MESSAGE_INFO_TYPE.equalsIgnoreCase(messages[0])) {
                showInfoMessage(messages[1]);
            } else if (MESSAGE_ERROR_TYPE.equalsIgnoreCase(messages[0])) {
                showErrorMessage(messages[1]);
            }
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DISMISS_MESSAGE:
//                    mMessageLayout.setVisibility(View.GONE);
                    try {
                        popupWindow.dismiss();
                    } catch (Exception ignored) {
                    }
                    break;
            }
        }
    };

    public void setNavigateItemVisibility(int resource, int visibility) {
        switch (resource) {
            case LEFT_TEXT_VIEW:
                mLeftTextViewLayout.setVisibility(visibility);
                break;
            case LEFT_IMAGE_VIEW:
                mLeftImageView.setVisibility(visibility);
                break;
            case TITLE:
                mTitleView.setVisibility(visibility);
                break;
            case FIRST_RIGHT_TEXT_VIEW:
                mFirstRightTextViewLayout.setVisibility(visibility);
                break;
            case SECOND_RIGHT_TEXT_VIEW:
                mSecondRightTextViewLayout.setVisibility(visibility);
                break;
            case CATEGORY_VIEW:
                if (API.isHave) {
                    dynamic_red.setVisibility(View.VISIBLE);
                } else {
                    dynamic_red.setVisibility(View.GONE);
                }
                mCategoryLayout.setVisibility(visibility);
                break;
            case TRIANGLE_MINE:
                mMineTriangle.setVisibility(visibility);
                break;
            case TRIANGLE_ACTIVITIES:
                mActivitiesTriangle.setVisibility(visibility);
                break;
            case TRIANGLE_STORE:
                mStoreTriangle.setVisibility(visibility);
                break;
            case TRIANGLE_SEARCH:
                mSearchTriangle.setVisibility(visibility);
                break;
            case SHARE_VIEW:
                mShareView.setVisibility(visibility);
        }
    }

    /**
     * 显示分类导航View
     */
    public void showCategoryView() {
        setNavigateItemVisibility(CATEGORY_VIEW, View.VISIBLE);
        setNavigateItemVisibility(NAVIGATE_VIEW, View.GONE);
    }

    /**
     * 隐藏分类导航View
     */
    public void hiddenCategoryView() {
        setNavigateItemVisibility(CATEGORY_VIEW, View.GONE);
        setNavigateItemVisibility(NAVIGATE_VIEW, View.VISIBLE);
    }

    /**
     * 显示分享View
     */
    public void showShareView(Share share) {
        this.mShare = share;
        setNavigateItemVisibility(SHARE_VIEW, View.VISIBLE);
        setNavigateItemVisibility(NAVIGATE_VIEW, View.GONE);
    }

    /**
     * 隐藏分享View
     */
    private void hiddenShareView() {
        setNavigateItemVisibility(SHARE_VIEW, View.GONE);
        setNavigateItemVisibility(NAVIGATE_VIEW, View.VISIBLE);
    }

    /**
     * 显示当前的选中的分类下面的小三角图标
     *
     * @param triangle
     */
    public void setCurrentCategoryTriangle(int triangle) {
        mMineTriangle.setVisibility(View.GONE);
        mActivitiesTriangle.setVisibility(View.GONE);
        mStoreTriangle.setVisibility(View.GONE);
        mSearchTriangle.setVisibility(View.GONE);
        setNavigateItemVisibility(triangle, View.VISIBLE);
    }

    /**
     * 设置导航栏中item的文字
     *
     * @param item
     * @param textResourceId
     */
    public void setNavigateItemText(int item, int textResourceId) {
        switch (item) {
            case LEFT_TEXT_VIEW:
                mLeftTextView.setText(textResourceId);
                break;
            case TITLE:
                mTitleView.setText(textResourceId);
                break;
            case FIRST_RIGHT_TEXT_VIEW:
                mFirstRightTextView.setText(textResourceId);
                break;
            case SECOND_RIGHT_TEXT_VIEW:
                mSecondRightTextView.setText(textResourceId);
                break;
        }
    }

    /**
     * 设置导航栏中item的文字
     *
     * @param item
     * @param text
     */
    public void setNavigateItemText(int item, String text) {
        switch (item) {
            case LEFT_TEXT_VIEW:
                mLeftTextView.setText(text);
                break;
            case TITLE:
                mTitleView.setText(text);
                break;
            case FIRST_RIGHT_TEXT_VIEW:
                mFirstRightTextView.setText(text);
                break;
            case SECOND_RIGHT_TEXT_VIEW:
                mSecondRightTextView.setText(text);
                break;
        }
    }

    /**
     * 设置导航栏中item的背景
     *
     * @param item
     * @param bgResourceId
     */
    public void setNavigateItemBackground(int item, int bgResourceId) {
        switch (item) {
            case LEFT_TEXT_VIEW:
                mLeftTextView.setBackgroundResource(bgResourceId);
                break;
            case LEFT_IMAGE_VIEW:
                mLeftImageView.setBackgroundResource(bgResourceId);
                break;
            case TITLE:
                mTitleView.setBackgroundResource(bgResourceId);
                break;
            case FIRST_RIGHT_TEXT_VIEW:
                mFirstRightTextView.setBackgroundResource(bgResourceId);
                break;
            case SECOND_RIGHT_TEXT_VIEW:
                mSecondRightTextView.setBackgroundResource(bgResourceId);
                break;
        }
    }

    /**
     * 设置导航栏中item的背景
     *
     * @param item
     * @param drawable
     */
    public void setNavigateItemBackground(int item, Drawable drawable) {
        switch (item) {
            case LEFT_TEXT_VIEW:
            	
                mLeftTextView.setBackgroundDrawable(drawable);
                break;
            case LEFT_IMAGE_VIEW:
                mLeftImageView.setBackgroundDrawable(drawable);
                break;
            case TITLE:
                mTitleView.setBackgroundDrawable(drawable);
                break;
            case FIRST_RIGHT_TEXT_VIEW:
                mFirstRightTextView.setBackgroundDrawable(drawable);
                break;
            case SECOND_RIGHT_TEXT_VIEW:
                mSecondRightTextView.setBackgroundDrawable(drawable);
                break;
        }
    }

    /**
     * 设置导航栏中item的点击事件
     *
     * @param item
     * @param onClickListener
     */
    public void setNavigateItemOnClickListener(int item, OnClickListener onClickListener) {
        switch (item) {
            case LEFT_TEXT_VIEW:
                mLeftTextViewLayout.setOnClickListener(onClickListener);
                break;
            case TITLE:
                mTitleView.setOnClickListener(onClickListener);
                break;
            case FIRST_RIGHT_TEXT_VIEW:
                mFirstRightTextViewLayout.setOnClickListener(onClickListener);
                break;
            case SECOND_RIGHT_TEXT_VIEW:
                mSecondRightTextViewLayout.setOnClickListener(onClickListener);
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mineLayout:
                Intent intent = new Intent(mContext, MineActivity.class);
                mContext.startActivity(intent);
                hiddenCategoryView();
                break;
            case R.id.activitiesLayout:
                Intent activityIntent = new Intent(mContext, DynamicActivity.class);
                mContext.startActivity(activityIntent);
                hiddenCategoryView();
                API.isHave = false;
                break;
            case R.id.storeLayout:
                Intent storeIntent = new Intent(mContext, MainActivity.class);
                mContext.startActivity(storeIntent);
                hiddenCategoryView();
                break;
            case R.id.searchLayout:
                Intent searchIntent = new Intent(mContext, SearchActivity.class);
                mContext.startActivity(searchIntent);
                hiddenCategoryView();
                break;
            case R.id.closeLayout:
                hiddenCategoryView();
                break;
            case R.id.shareCloseLayout:
                hiddenShareView();
                break;
            case R.id.sinaWeibo:
                shareTo(R.id.sinaWeibo);
                break;
            case R.id.renren:
                shareTo(R.id.renren);
                break;
            case R.id.weChat:
                shareTo(R.id.weChat);
                break;
            case R.id.weChatMoment:
                shareTo(R.id.weChatMoment);
                break;
        }
    }

    private void shareTo(int platform) {
        if (mShare == null)
            return;
        ShareSDK.initSDK(mContext);
        switch (platform) {
            case R.id.sinaWeibo:
                Platform sina = ShareSDK.getPlatform(mContext, SinaWeibo.NAME);
                SinaWeibo.ShareParams sinaParams = new SinaWeibo.ShareParams();
                sinaParams.setTitle(mShare.title);
                sinaParams.setText(mShare.content);
                sinaParams.setSite(mShare.description);
                sinaParams.setSiteUrl(mShare.link);
                sinaParams.setImagePath(mShare.imagePath);
                sina.setPlatformActionListener(this);
                sina.share(sinaParams);
                break;
            case R.id.renren:
                Platform renren = ShareSDK.getPlatform(mContext, Renren.NAME);
                Renren.ShareParams renrenParams = new Renren.ShareParams();
                renrenParams.setTitle(mShare.title);
                renrenParams.setTitleUrl(mShare.link);
                renrenParams.setText(mShare.description);
                renrenParams.setComment(mShare.content);
                renrenParams.setImageUrl(mShare.imageUrl);
                renren.setPlatformActionListener(this);
                renren.share(renrenParams);
                break;
            case R.id.weChat:
                Platform weChat = ShareSDK.getPlatform(mContext, Wechat.NAME);
                Wechat.ShareParams weChatParams = new Wechat.ShareParams();
                weChatParams.setTitle(mShare.title);
                weChatParams.setText(mShare.content);
                weChatParams.setUrl(mShare.link);
                weChatParams.setImageUrl(mShare.imageUrl);
                weChatParams.setShareType(Platform.SHARE_WEBPAGE);
                weChat.setPlatformActionListener(this);
                weChat.share(weChatParams);
                break;
            case R.id.weChatMoment:
                Platform weChatMoment = ShareSDK.getPlatform(mContext, WechatMoments.NAME);
                WechatMoments.ShareParams weChatMomentParams = new WechatMoments.ShareParams();
                weChatMomentParams.setTitle(mShare.content);
//                weChatMomentParams.setText(mShare.content);
                weChatMomentParams.setUrl(mShare.link);
                weChatMomentParams.setImageUrl(mShare.imageUrl);
                weChatMomentParams.setShareType(Platform.SHARE_WEBPAGE);
                weChatMoment.setPlatformActionListener(this);
                weChatMoment.share(weChatMomentParams);
                break;
        }
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> stringObjectHashMap) {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showInfoMessage(mContext.getString(R.string.share_successful));
            }
        });
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showErrorMessage(mContext.getString(R.string.share_failed));
            }
        });
        throwable.printStackTrace();
    }

    @Override
    public void onCancel(Platform platform, int i) {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showInfoMessage(mContext.getString(R.string.share_canceled));
            }
        });
    }
}
