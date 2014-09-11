package com.huassit.imenu.android.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.huassit.imenu.android.db.DbConstants;
import com.huassit.imenu.android.model.AreaCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sylar on 14-7-2.
 */
public class AreaCodeDao extends BaseDao<AreaCode> {
    public AreaCodeDao(Context context) {
        super(context);
    }

    public void save(AreaCode areaCode) {
        ContentValues values = new ContentValues();
        values.put(DbConstants.COLUMN_AREA_CODE.AREA_CODE_ID, areaCode.area_code_id);
        values.put(DbConstants.COLUMN_AREA_CODE.AREA_CODE_NAME, areaCode.area_code_name);
        values.put(DbConstants.COLUMN_AREA_CODE.AREA_CODE_VALUE, areaCode.area_code_value);
        values.put(DbConstants.COLUMN_AREA_CODE.DEFAULT_VALUE, areaCode.defaultValue);
        values.put(DbConstants.COLUMN_AREA_CODE.SORT_ORDER, areaCode.sort_order);
        database.insert(DbConstants.TABLE.TABLE_AREA_CODE, null, values);
    }

    public void update(AreaCode areaCode) {
        ContentValues values = new ContentValues();
        values.put(DbConstants.COLUMN_AREA_CODE.AREA_CODE_ID, areaCode.area_code_id);
        values.put(DbConstants.COLUMN_AREA_CODE.AREA_CODE_NAME, areaCode.area_code_name);
        values.put(DbConstants.COLUMN_AREA_CODE.AREA_CODE_VALUE, areaCode.area_code_value);
        values.put(DbConstants.COLUMN_AREA_CODE.DEFAULT_VALUE, areaCode.defaultValue);
        values.put(DbConstants.COLUMN_AREA_CODE.SORT_ORDER, areaCode.sort_order);
        database.update(DbConstants.TABLE.TABLE_AREA_CODE, values, DbConstants.COLUMN_AREA_CODE.AREA_CODE_ID + "=?", new String[]{areaCode.area_code_id});
    }

    public boolean isExits(AreaCode areaCode) {
        boolean result = false;
        Cursor cursor = null;
        try {
            final String sql = "select count(*) from " + DbConstants.TABLE.TABLE_AREA_CODE + " where " + DbConstants.COLUMN_AREA_CODE.AREA_CODE_ID + " =?";
            cursor = database.rawQuery(sql, new String[]{areaCode.area_code_id});
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

    public void saveOrUpdate(AreaCode areaCode) {
        if (isExits(areaCode)) {
            update(areaCode);
        } else {
            save(areaCode);
        }
    }

    public List<AreaCode> getAreaCodeList() {
        List<AreaCode> result = null;
        Cursor cursor = null;
        try {
            final String sql = "select * from " + DbConstants.TABLE.TABLE_AREA_CODE + " order by " + DbConstants.COLUMN_AREA_CODE.SORT_ORDER;
            cursor = database.rawQuery(sql, null);
            if (cursor != null) {
                result = new ArrayList<AreaCode>();
                while (cursor.moveToNext()) {
                    AreaCode areaCode = new AreaCode();
                    areaCode.area_code_id = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_AREA_CODE.AREA_CODE_ID));
                    areaCode.area_code_name = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_AREA_CODE.AREA_CODE_NAME));
                    areaCode.area_code_value = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_AREA_CODE.AREA_CODE_VALUE));
                    areaCode.defaultValue = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_AREA_CODE.DEFAULT_VALUE));
                    areaCode.sort_order = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_AREA_CODE.SORT_ORDER));
                    result.add(areaCode);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    public AreaCode getDefault() {
        AreaCode code = null;
        Cursor cursor = null;
        try {
            final String sql = "select * from " + DbConstants.TABLE.TABLE_AREA_CODE + " where " + DbConstants.COLUMN_AREA_CODE.DEFAULT_VALUE + "='1'";
            cursor = database.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                code = new AreaCode();
                code.area_code_id = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_AREA_CODE.AREA_CODE_ID));
                code.area_code_name = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_AREA_CODE.AREA_CODE_NAME));
                code.area_code_value = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_AREA_CODE.AREA_CODE_VALUE));
                code.defaultValue = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_AREA_CODE.DEFAULT_VALUE));
                code.sort_order = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_AREA_CODE.SORT_ORDER));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return code;
    }
}
