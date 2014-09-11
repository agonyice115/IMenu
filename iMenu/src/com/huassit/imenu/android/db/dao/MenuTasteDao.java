package com.huassit.imenu.android.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.huassit.imenu.android.db.DbConstants;
import com.huassit.imenu.android.model.MenuTaste;

/**
 * Created by Sylar on 14-7-2.
 */
public class MenuTasteDao extends BaseDao<MenuTaste> {
    public MenuTasteDao(Context context) {
        super(context);
    }

    public void save(MenuTaste menuTaste) {
        ContentValues values = new ContentValues();
        values.put(DbConstants.COLUMN_MENU_TASTE.MENU_TASTE_ID, menuTaste.menu_taste_id);
        values.put(DbConstants.COLUMN_MENU_TASTE.MENU_TASTE_NAME, menuTaste.menu_taste_name);
        values.put(DbConstants.COLUMN_MENU_TASTE.MENU_TASTE_IMAGE, menuTaste.menu_taste_image);
        values.put(DbConstants.COLUMN_MENU_TASTE.SORT_ORDER, menuTaste.sort_order);
        database.insert(DbConstants.TABLE.TABLE_MENU_TASTE, null, values);
    }

    public void update(MenuTaste menuTaste) {
        ContentValues values = new ContentValues();
        values.put(DbConstants.COLUMN_MENU_TASTE.MENU_TASTE_ID, menuTaste.menu_taste_id);
        values.put(DbConstants.COLUMN_MENU_TASTE.MENU_TASTE_NAME, menuTaste.menu_taste_name);
        values.put(DbConstants.COLUMN_MENU_TASTE.MENU_TASTE_IMAGE, menuTaste.menu_taste_image);
        values.put(DbConstants.COLUMN_MENU_TASTE.SORT_ORDER, menuTaste.sort_order);
        database.update(DbConstants.TABLE.TABLE_MENU_TASTE, values, DbConstants.COLUMN_MENU_TASTE.MENU_TASTE_ID + "=?", new String[]{menuTaste.menu_taste_id});
    }

    public boolean isExits(MenuTaste menuTaste) {
        boolean result = false;
        Cursor cursor = null;
        try {
            final String sql = "select count(*) from " + DbConstants.TABLE.TABLE_MENU_TASTE + " where " + DbConstants.COLUMN_MENU_TASTE.MENU_TASTE_ID + " =?";
            cursor = database.rawQuery(sql, new String[]{menuTaste.menu_taste_id});
            if (cursor != null && cursor.moveToFirst()) {
                result = cursor.getLong(0) > 0;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    public void saveOrUpdate(MenuTaste menuTaste) {
        if (isExits(menuTaste)) {
            update(menuTaste);
        } else {
            save(menuTaste);
        }
    }
}
