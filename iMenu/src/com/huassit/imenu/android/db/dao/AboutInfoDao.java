package com.huassit.imenu.android.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.huassit.imenu.android.db.DbConstants;
import com.huassit.imenu.android.model.AboutInfo;

/**
 * Created by Sylar on 14-7-2.
 */
public class AboutInfoDao extends BaseDao<AboutInfo> {
    public AboutInfoDao(Context context) {
        super(context);
    }

    public void save(AboutInfo aboutInfo) {
        ContentValues values = new ContentValues();
        values.put(DbConstants.COLUMN_ABOUT_INFO.KEY, aboutInfo.key);
        values.put(DbConstants.COLUMN_ABOUT_INFO.VALUE, aboutInfo.value);
        database.insert(DbConstants.TABLE.TABLE_ABOUT_INFO, null, values);
    }

    public void update(AboutInfo aboutInfo) {
        ContentValues values = new ContentValues();
        values.put(DbConstants.COLUMN_ABOUT_INFO.KEY, aboutInfo.key);
        values.put(DbConstants.COLUMN_ABOUT_INFO.VALUE, aboutInfo.value);
        database.update(DbConstants.TABLE.TABLE_ABOUT_INFO, values, DbConstants.COLUMN_ABOUT_INFO.KEY + "=?", new String[]{aboutInfo.key});
    }

    public boolean isExits(AboutInfo info) {
        boolean result = false;
        Cursor cursor = null;
        try {
            final String sql = "select count(*) from " + DbConstants.TABLE.TABLE_ABOUT_INFO + " where " + DbConstants.COLUMN_ABOUT_INFO.KEY + " =?";
            cursor = database.rawQuery(sql, new String[]{info.key});
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

    public void saveOrUpdate(AboutInfo aboutInfo) {
        if (isExits(aboutInfo)) {
            update(aboutInfo);
        } else {
            save(aboutInfo);
        }
    }

    public AboutInfo getAboutInfoByKey(String key) {
        AboutInfo result = null;
        Cursor cursor = null;
        try {
            final String sql = "select * from " + DbConstants.TABLE.TABLE_ABOUT_INFO + " where " + DbConstants.COLUMN_ABOUT_INFO.KEY + " =?";
            cursor = database.rawQuery(sql, new String[]{key});
            if (cursor != null && cursor.moveToFirst()) {
                result = new AboutInfo();
                result.key = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_ABOUT_INFO.KEY));
                result.value = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_ABOUT_INFO.VALUE));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }
}
