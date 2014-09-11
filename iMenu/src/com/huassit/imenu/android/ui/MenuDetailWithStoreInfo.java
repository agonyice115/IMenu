package com.huassit.imenu.android.ui;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.MyApplication;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.db.dao.MenuUnitDao;
import com.huassit.imenu.android.model.Menu;
import com.huassit.imenu.android.model.MenuUnit;
import com.huassit.imenu.android.model.Store;
import com.huassit.imenu.android.util.NumberFormatUtils;
import com.huassit.imenu.android.util.ScreenUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MenuDetailWithStoreInfo extends BaseActivity {

    private Menu menu;
    private Store store;
    private ImageView close;
    private ImageView iv_store_pic;
    private TextView tv_storeName;
    private ImageView iv_menu;
    private TextView menu_name;
    private TextView menu_price;
    private TextView menu_unit;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private MenuUnitDao menuUnitDao;

    @Override
    public int getContentView() {
        return R.layout.menu_detail_with_store_info;
    }

    @Override
    public void initView() {
        options = MyApplication.getDisplayImageOptions(this, 30);
        imageLoader = ImageLoader.getInstance();
        close = (ImageView) findViewById(R.id.close);
        iv_store_pic = (ImageView) findViewById(R.id.iv_store);
        tv_storeName = (TextView) findViewById(R.id.store_name);
        iv_menu = (ImageView) findViewById(R.id.iv_menu);
        menu_name = (TextView) findViewById(R.id.menu_name);
        menu_price = (TextView) findViewById(R.id.menu_price);
        menu_unit = (TextView) findViewById(R.id.menu_unit);
        ScreenUtils.ScreenResolution screenResolution = ScreenUtils.getScreenResolution(this);
        LinearLayout.LayoutParams newLayoutParams = new LinearLayout.LayoutParams(screenResolution.getWidth(), screenResolution.getWidth());
        iv_menu.setLayoutParams(newLayoutParams);
    }

    @Override
    public void initData() {
        menu = (Menu) getIntent().getSerializableExtra("menu");
        store = (Store) getIntent().getSerializableExtra("store");
        imageLoader.displayImage(store.logoLocation + store.logoName, iv_store_pic, options);
        tv_storeName.setText(store.name);
        imageLoader.displayImage(menu.menu_image_location + menu.menu_image_name, iv_menu);
        menu_name.setText(menu.menu_name);
        if (!StringUtils.isBlank(menu.menu_price)) {
            float price = Float.valueOf(menu.menu_price);
            menu_price.setText(NumberFormatUtils.format(price) + "/");
        }

        if (!StringUtils.isBlank(menu.menu_unit_id)) {
            menuUnitDao = new MenuUnitDao(this);
            MenuUnit unit = menuUnitDao.getMenuUnitById(menu.menu_unit_id);
            menu_unit.setText(unit.menu_unit_title);
        }
        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
