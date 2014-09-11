package com.huassit.imenu.android.ui.camera;

import java.util.ArrayList;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.model.MemberMenu;
import com.huassit.imenu.android.model.Menu;
import com.huassit.imenu.android.util.ImageUtils;
import com.huassit.imenu.android.util.ScreenUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PictureDetailsActivity extends BaseActivity {

    private ImageView close;
    private ImageView iv_menu;
    private TextView reTakePhoto;
    private Map<String, String> thumbnailMap;
    private ImageLoader imageLoader;
    /** map中图片路径：1-网络图片路径，0-SDCard路径 */
    private int mapImgType;

    @Override
    public int getContentView() {
        return R.layout.picture_details;
    }

    @Override
    public void initView() {
        close = (ImageView) findViewById(R.id.close);
        iv_menu = (ImageView) findViewById(R.id.iv_menu);
        reTakePhoto = (TextView) findViewById(R.id.re_take_photo);
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public void initData() {
        String filePath = getIntent().getStringExtra("PICTURE");
        mapImgType=getIntent().getExtras().getInt("filePath_type");
        thumbnailMap = (Map<String, String>) getIntent().getSerializableExtra("ThumbnailMap");
        final ArrayList<MemberMenu> menu_list = (ArrayList<MemberMenu>) getIntent().getSerializableExtra("MENU_LIST");
        ScreenUtils.ScreenResolution screenResolution = ScreenUtils.getScreenResolution(this);
        LinearLayout.LayoutParams newLayoutParams = new LinearLayout.LayoutParams(screenResolution.getWidth(), screenResolution.getWidth());
        iv_menu.setLayoutParams(newLayoutParams);
        if(filePath.contains("http")){
        	imageLoader.displayImage(filePath, iv_menu);
        }else{
        	Bitmap bitmap = ImageUtils.getScaleBitmap(filePath, screenResolution.getWidth(), screenResolution.getWidth());
        	iv_menu.setImageBitmap(bitmap);
        	bitmap=null;
        }
//        if(mapImgType == 1){
//        	imageLoader.displayImage(filePath, iv_menu);
//        }else{
//        	Bitmap bitmap = ImageUtils.getScaleBitmap(filePath, screenResolution.getWidth(), screenResolution.getWidth());
//        	iv_menu.setImageBitmap(bitmap);
//        	bitmap=null;
//        }
        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        reTakePhoto.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PictureDetailsActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.NEED_TAKE_PIC_COUNT, getList(menu_list));
                intent.putExtra("sourceActivity", "PictureDetails");
                intent.putExtra("ThumbnailMap", (java.io.Serializable) thumbnailMap);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * 封装菜单List
     */
    public ArrayList<Menu> getList(ArrayList<MemberMenu> memberMenu) {
        ArrayList<Menu> menusList = new ArrayList<Menu>();
        for (int i = 0; i < memberMenu.size(); i++) {
            Menu menu = new Menu();
            menu.menu_id = memberMenu.get(i).menu_id;
            menu.menu_name = memberMenu.get(i).menu_name;
            menu.menu_image_name = memberMenu.get(i).image_name;
            menu.menu_image_location = memberMenu.get(i).image_location;
            menusList.add(menu);
        }
        return menusList;
    }

}
