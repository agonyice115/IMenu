package com.huassit.imenu.android.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.huassit.imenu.android.db.DbConstants;
import com.huassit.imenu.android.model.MenuUnit;

/**
 * Created by Sylar on 14-7-2.
 */
public class MenuUnitDao extends BaseDao<MenuUnit> {
    public MenuUnitDao(Context context) {
        super(context);
    }

    public void save(MenuUnit menuUnit) {
        ContentValues values = new ContentValues();
        values.put(DbConstants.COLUMN_MENU_UNIT.MENU_UNIT_ID, menuUnit.menu_unit_id);
        values.put(DbConstants.COLUMN_MENU_UNIT.MENU_UNIT_TITLE, menuUnit.menu_unit_title);
        database.insert(DbConstants.TABLE.TABLE_MENU_UNIT, null, values);
    }

    public void update(MenuUnit menuUnit) {
        ContentValues values = new ContentValues();
        values.put(DbConstants.COLUMN_MENU_UNIT.MENU_UNIT_ID, menuUnit.menu_unit_id);
        values.put(DbConstants.COLUMN_MENU_UNIT.MENU_UNIT_TITLE, menuUnit.menu_unit_title);
        database.update(DbConstants.TABLE.TABLE_MENU_UNIT, values, DbConstants.COLUMN_MENU_UNIT.MENU_UNIT_ID + "=?", new String[]{menuUnit.menu_unit_id});
    }

    public boolean isExits(MenuUnit menuUnit) {
        boolean result = false;
        Cursor cursor = null;
        try {
            final String sql = "select count(*) from " + DbConstants.TABLE.TABLE_MENU_UNIT + " where " + DbConstants.COLUMN_MENU_UNIT.MENU_UNIT_ID + " =?";
            cursor = database.rawQuery(sql, new String[]{menuUnit.menu_unit_id});
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


    public void saveOrUpdate(MenuUnit menuUnit) {
        if (isExits(menuUnit)) {
            update(menuUnit);
        } else {
            save(menuUnit);
        }
    }

    public MenuUnit getMenuUnitById(String id) {
        MenuUnit menuUnit = null;
        Cursor cursor = null;
        try {
            cursor = database.rawQuery("select * from " + DbConstants.TABLE.TABLE_MENU_UNIT + " where " + DbConstants.COLUMN_MENU_UNIT.MENU_UNIT_ID + "=?", new String[]{id});
            if (cursor != null && cursor.moveToFirst()) {
                menuUnit = new MenuUnit();
                menuUnit.menu_unit_id = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_MENU_UNIT.MENU_UNIT_ID));
                menuUnit.menu_unit_title = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_MENU_UNIT.MENU_UNIT_TITLE));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return menuUnit;
    }
}
