package com.huassit.imenu.android.ui.settings;

import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.db.dao.ClientSkinDao;
import com.huassit.imenu.android.model.ClientSkin;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.ScreenUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.view.ColorView;
import com.huassit.imenu.android.view.NavigationView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sylar on 14-7-11.
 */
public class CommonSettingActivity extends BaseActivity implements ColorView.OnColorSelectedListener, View.OnClickListener {

    private NavigationView navigationView;
    private List<ColorView> mColorViewList;
    private CheckBox loadOnWifi;
    private TextView cacheSizeTextView;
    public static final String LoadImageOnWifiOnly ="com.huassit.imenu.LoadImageOnWifiOnly";

    @Override
    public int getContentView() {
        return R.layout.common_setting_activity;
    }

    @Override
    public void initView() {
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigateItemText(NavigationView.TITLE, R.string.common_setting);
        navigationView.setNavigateItemVisibility(NavigationView.LEFT_IMAGE_VIEW, View.GONE);
        navigationView.setNavigateItemVisibility(NavigationView.FIRST_RIGHT_TEXT_VIEW, View.GONE);
        navigationView.setNavigateItemBackground(NavigationView.SECOND_RIGHT_TEXT_VIEW, R.drawable.navi);
        navigationView.setNavigateItemBackground(NavigationView.LEFT_TEXT_VIEW, R.drawable.back);
        navigationView.setCurrentCategoryTriangle(NavigationView.TRIANGLE_MINE);
        navigationView.setNavigateItemOnClickListener(NavigationView.LEFT_TEXT_VIEW, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        LinearLayout mColorLayout = (LinearLayout) findViewById(R.id.colorPickerLayout);
        ClientSkinDao mSkinDao = new ClientSkinDao(this);
        List<ClientSkin> skinList = mSkinDao.getAllSkin();
        String currentSkinId = PreferencesUtils.getString(this, "CurrentSkinId");
        ClientSkin currentSkin = null;
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
//        int countInLine = screenWidth / ScreenUtils.dpToPxInt(this, 100);
        int countInLine = 5;
        if (!StringUtils.isBlank(currentSkinId)) {
            for (ClientSkin skin : skinList) {
                if (skin.client_skin_id.equals(currentSkinId)) {
                    currentSkin = skin;
                    break;
                }
            }
            skinList.remove(currentSkin);
            skinList.add(0, currentSkin);
        }
        mColorViewList = new ArrayList<ColorView>(countInLine);
        for (int i = 0; i < countInLine; i++) {
            ColorView view = new ColorView(this);
            ClientSkin skin = skinList.get(i);
            view.setClientSkin(skin);
            if (i == 0) {
                view.setSelected(true);
            }
            view.setOnColorSelectedListener(this);
            mColorViewList.add(view);
            mColorLayout.addView(view);
        }
        mColorLayout.invalidate();
        findViewById(R.id.moreColorLayout).setOnClickListener(this);
        findViewById(R.id.clearCacheLayout).setOnClickListener(this);
        loadOnWifi = (CheckBox) findViewById(R.id.loadImageOnWifiCheckBox);
        loadOnWifi.setOnClickListener(this);
        cacheSizeTextView = (TextView) findViewById(R.id.cacheSizeTextView);
    }

    @Override
    public void initData() {
        File cacheDir = ImageLoader.getInstance().getDiskCache().getDirectory();
        String size = Float.valueOf(cacheDir.length() / 1024f) + "MB";
        cacheSizeTextView.setText(size);
        loadOnWifi.setChecked(PreferencesUtils.getBoolean(this, "LoadImageOnWifiOnly", true));
    }

    @Override
    public void onColorSelected(ColorView colorView, ClientSkin selectedSkin) {
        for (ColorView view : mColorViewList) {
            if (colorView != view)
                view.setSelected(false);
        }
        navigationView.setBackgroundColor(Color.parseColor(selectedSkin.client_skin_value));
        PreferencesUtils.putString(this, "CurrentSkinId", selectedSkin.client_skin_id);
        PreferencesUtils.putString(this, "ColorValue", selectedSkin.client_skin_value);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.moreColorLayout:
                Intent colorTheme = new Intent(CommonSettingActivity.this, ClientSkinListActivity.class);
                startActivity(colorTheme);
                break;
            case R.id.clearCacheLayout:
                ImageLoader.getInstance().clearDiskCache();
                cacheSizeTextView.setText("0MB");
                navigationView.showInfoMessage(getString(R.string.clear_cache_finished));
                break;
            case R.id.loadImageOnWifiCheckBox:
                PreferencesUtils.putBoolean(this, "LoadImageOnWifiOnly", loadOnWifi.isChecked());
                Intent intent =new Intent(LoadImageOnWifiOnly);
                intent.putExtra("isLoadOnlyOnWifi", loadOnWifi.isChecked());
                sendBroadcast(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.setBackgroundColor(Color.parseColor(colorValue));
    }
}
